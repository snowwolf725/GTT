/**
 * 
 */
package gttlipse.resource;

import gtt.eventmodel.EventModelFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


/**
 * @author SnowWolf725
 * 
 *         created first in project GTTlipse.util.IResource
 * 
 */
public class TestFileManager {
	public boolean isMethodExist;

	public TestFileManager() {
	}

	public String getNewTestFile(String packagepath, String filename) {
		final String TEST_FILE_TEMPLATE = "TestFileTemplate.java";
		String context = readContext(TEST_FILE_TEMPLATE);
		if (packagepath.equals("") == false)
			context = "package " + packagepath + ";\n\n" + context;
		context = context.replaceAll("TestFileTemplate", filename);
		return context;
	}

	public boolean addMacroEntryPoint(IProject project) {
		try {
			// create source folder
			ResourceManager manager = new ResourceManager();
			manager.addJavaSourceFolder(project, "gtt");

			// create package
			final String MACRO_FOLDER = "gtt/gttMacroScript";
			IFolder folder = project.getFolder(MACRO_FOLDER);
			if (folder.exists() == false) {
				folder.create(false, true, null);
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}

			// create file
			final String MACRO_ENTRYPOINT_TEMPLATE = "MacroEntryPoint.java";
			IFile file = project.getFile(new Path(MACRO_FOLDER
					+ System.getProperty("file.separator")+ MACRO_ENTRYPOINT_TEMPLATE));
			if (file.exists() == false) {
				String context = readContext(MACRO_ENTRYPOINT_TEMPLATE);
				context = "package gttMacroScript;\n\n" + context;
				context = context.replaceAll("TestFileTemplate",
						"MacroEntryPoint");
				InputStream in = new ByteArrayInputStream(context.getBytes());
				file.create(in, true, null);

				return true;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String readContext(String filename) {
		try {
			File file = null;
			String context = "";
			String pluginPath = "";
			if(GTTlipse.getPlatformInfo() != null)
				pluginPath = JavaCore.getClasspathVariable("ECLIPSE_HOME")
				+ "/plugins/" + gttlipse.GTTlipse.getPlatformInfo().getPluginFolder();
			file = new File(EventModelFactory.getPluginPath() + System.getProperty("file.separator") +
						"descriptor/" + filename);
			if(isFileExistInPlugIn("descriptor/" + filename))
				file = new File(pluginPath + System.getProperty("file.separator") +
						"descriptor/" + filename);
			BufferedReader fr = new BufferedReader(new FileReader(file.getPath()));
			while (fr.ready()) {
				String line = fr.readLine();
				context = context + line + "\n";
			}
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void addScriptDocument(final TestScriptDocument doc, boolean dosave) {
		final TestMethodNode method = (TestMethodNode) doc.getParent();
		TestCaseNode classnode = (TestCaseNode) method.getParent();

		// final int index = method.indexOf(doc);
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		openEditor(file);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		// start record of the modifications
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		final AST ast = astRoot.getAST();
		astRoot.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				if (!node.getName().getFullyQualifiedName().matches(
						method.getName()))
					return true;

				MethodInvocation getname = ast.newMethodInvocation();
				getname.setExpression(ast.newThisExpression());
				getname.setName(ast.newSimpleName("getMethodName"));

				StringLiteral literal = ast.newStringLiteral();
				literal.setLiteralValue(doc.getName());

				MethodInvocation invoc = ast.newMethodInvocation();
				invoc.setExpression(ast.newSimpleName("runner"));
				invoc.setName(ast.newSimpleName("GTTTestScript"));
				invoc.arguments().add(getname);
				invoc.arguments().add(literal);

				ExpressionStatement expressionStatement = ast
						.newExpressionStatement(invoc);
				node.getBody().statements().add(expressionStatement);
				return true;
			}

		});

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			// update of the compilation unit
			unit.getBuffer().setContents(document.get());
			if (dosave)
				unit.commitWorkingCopy(true, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void openEditor(IFile file) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void deleteScriptDocument(final TestScriptDocument doc) {
		final TestMethodNode method = (TestMethodNode) doc.getParent();
		TestCaseNode classnode = (TestCaseNode) method.getParent();
		final int index = method.indexOf(doc);
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		openEditor(file);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		// start record of the modifications
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		// final AST ast = astRoot.getAST();
		astRoot.accept(new ASTVisitor() {
			boolean inmethod = false;
			int count = 1;

			public boolean visit(MethodDeclaration node) {
				if (node.getName().getFullyQualifiedName().matches(
						method.getName())) {
					inmethod = true;
				} else
					inmethod = false;
				return true;
			}

			public boolean visit(ExpressionStatement node) {
				if (!inmethod)
					return true;
				String line = node.toString();
				Pattern pattern = Pattern
						.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					if (inmethod && index == count++) {
						node.delete();
					}
				}
				return true;
			}

		});

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			String newSource = document.get();
			// update of the compilation unit
			unit.getBuffer().setContents(newSource);
			unit.commit(true, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void renameTestScriptDocument(final TestScriptDocument doc) {
		final TestMethodNode method = (TestMethodNode) doc.getParent();
		TestCaseNode classnode = (TestCaseNode) method.getParent();
		final int index = method.indexOf(doc);
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		// start record of the modifications
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		final AST ast = astRoot.getAST();
		astRoot.accept(new ASTVisitor() {
			boolean inmethod = false;
			int count = 1;

			public boolean visit(MethodDeclaration node) {
				if (node.getName().getFullyQualifiedName().matches(
						method.getName())) {
					inmethod = true;
				} else
					inmethod = false;
				return true;
			}

			@SuppressWarnings("unchecked")
			public boolean visit(ExpressionStatement node) {
				if (!inmethod)
					return true;
				String line = node.toString();
				Pattern pattern = Pattern
						.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					if (inmethod && index == count++) {
						MethodInvocation methodInvocation = ast
								.newMethodInvocation();
						methodInvocation.setExpression(ast
								.newSimpleName("runner"));
						methodInvocation.setName(ast
								.newSimpleName("GTTTestScript"));
						MethodInvocation getname = ast.newMethodInvocation();
						getname.setExpression(ast.newThisExpression());
						getname.setName(ast.newSimpleName("getMethodName"));
						methodInvocation.arguments().add(getname);
						StringLiteral literal = ast.newStringLiteral();
						literal.setLiteralValue(doc.getName());
						methodInvocation.arguments().add(literal);
						node.setExpression(methodInvocation);
					}
				}
				return true;
			}

		});

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			String newSource = document.get();
			// update of the compilation unit
			unit.getBuffer().setContents(newSource);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void swapTestScriptDocument(final TestMethodNode method,
			final int oldindex, final int newindex) {
		TestCaseNode classnode = (TestCaseNode) method.getParent();
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		// start record of the modifications
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		final AST ast = astRoot.getAST();
		astRoot.accept(new ASTVisitor() {
			boolean inmethod = false;
			int count = 1;

			public boolean visit(MethodDeclaration node) {
				if (node.getName().getFullyQualifiedName().matches(
						method.getName())) {
					inmethod = true;
				} else
					inmethod = false;
				return true;
			}

			@SuppressWarnings("unchecked")
			public boolean visit(ExpressionStatement node) {
				if (!inmethod)
					return true;
				String line = node.toString();
				Pattern pattern = Pattern
						.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					if (oldindex == count || newindex == count) {
						TestScriptDocument doc = (TestScriptDocument) method
								.getDocAt(count - 1);

						MethodInvocation methodInvocation = ast
								.newMethodInvocation();
						methodInvocation.setExpression(ast
								.newSimpleName("runner"));
						methodInvocation.setName(ast
								.newSimpleName("GTTTestScript"));
						MethodInvocation getname = ast.newMethodInvocation();
						getname.setExpression(ast.newThisExpression());
						getname.setName(ast.newSimpleName("getMethodName"));
						methodInvocation.arguments().add(getname);
						StringLiteral literal = ast.newStringLiteral();
						literal.setLiteralValue(doc.getName());
						methodInvocation.arguments().add(literal);
						node.setExpression(methodInvocation);
					}
					count++;
				}
				return true;
			}

		});

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			String newSource = document.get();
			// update of the compilation unit
			unit.getBuffer().setContents(newSource);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param project
	 * @throws JavaModelException
	 */
	public void addJarFile(IProject project, String jarfile)
			throws JavaModelException {
		boolean hasjar = false;
		IJavaProject javaproject = JavaCore.create(project);
		IClasspathEntry classpath[] = javaproject.getRawClasspath();
		IClasspathEntry varentry = null;
		String pluginPath = "";
		if(gttlipse.GTTlipse.getPlatformInfo() != null)
			pluginPath = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins/" + gttlipse.GTTlipse.getPlatformInfo().getPluginFolder();
		if(isFileExistInPlugIn("/jar/" + jarfile))
			varentry = JavaCore.newLibraryEntry(new Path(
					pluginPath + "/jar/" + jarfile), // library location
					null, // source archive location
					null, // source archive root path
					true); // exported
		else
			varentry = JavaCore.newVariableEntry(new Path(
				"GTT_HOME/jar/" + jarfile), // library location
				null, // source archive location
				null, // source archive root path
				true); // exported
		IClasspathEntry newclasspath[] = new IClasspathEntry[classpath.length + 1];
		jarfile = jarfile.replaceAll(".*/", "");
		for (int i = 0; i < classpath.length; i++) {
			if (classpath[i].getPath().toFile().getName().matches(jarfile))
				hasjar = true;
			newclasspath[i] = classpath[i];
		}
		newclasspath[classpath.length] = varentry;
		if (hasjar == false)
			javaproject.setRawClasspath(newclasspath, null);
	}
	
	private boolean isFileExistInPlugIn(String filename) {
		boolean isExist = false;
		File file = null;
		if(GTTlipse.getPlatformInfo() == null)
			return false;
		String pluginPath = JavaCore.getClasspathVariable("ECLIPSE_HOME") +
			"/plugins/"+ gttlipse.GTTlipse.getPlatformInfo().getPluginFolder();
		if(pluginPath != null && pluginPath != "") {
			file = new File(pluginPath + System.getProperty("file.separator") + filename);
			if(file.exists())
				isExist = true;
		}
		return isExist;
	}

	public void addDescriptorFile(IProject project) {
		IPath home = JavaCore.getClasspathVariable("ECLIPSE_HOME");
		System.out.println("ECLIPSE_HOME: " + home.toString());
		
		if (!project.getFolder(new Path("descriptor")).exists()) {
			try {
				checkSwingDescriptor(project);
				checkAssertionInfo(project);
				checkWebDescriptor(project);
				checkPluginDescriptor(project);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void checkPluginDescriptor(IProject project) throws CoreException {
		if(gttlipse.GTTlipse.getPlatformInfo() == null)
			return;
		String filepath = gttlipse.GTTlipse.getPlatformInfo().getDescriptionFilePath();
		IFile file = project.getFile(new Path(filepath));
		if (!file.exists()) {
			InputStream in = new ByteArrayInputStream(readContext(file.getName())
					.getBytes());
			file.create(in, true, null);
		}
		GTTlipse.getPlatformInfo().copyDescription();
	}

	private void checkWebDescriptor(IProject project) throws CoreException {
		IFile file = project.getFile(new Path("descriptor/web.xml"));
		if (!file.exists()) {
			InputStream in = new ByteArrayInputStream(readContext("web.xml")
					.getBytes());
			file.create(in, true, null);
		}
	}

	private void checkAssertionInfo(IProject project) throws CoreException {
		IFile file = project.getFile(new Path("descriptor/assertInfo.xml"));
		if (!file.exists()) {
			InputStream in = new ByteArrayInputStream(readContext(
					"assertInfo.xml").getBytes());
			file.create(in, true, null);
		}
	}

	private void checkSwingDescriptor(IProject project) throws CoreException {
		project.getFolder("descriptor").create(true, true, null);
		IFile file = project.getFile(new Path("descriptor/swing.xml"));
		if (!file.exists()) {
			InputStream in = new ByteArrayInputStream(readContext("swing.xml")
					.getBytes());
			file.create(in, true, null);
		}
	}

	public boolean addTestMethod(final TestCaseNode classnode,
			final String methodname) {
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		openEditor(file);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		final AST ast = astRoot.getAST();
		isMethodExist = false;
		astRoot.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getFullyQualifiedName().matches(methodname))
					isMethodExist = true;
				return true;
			}
		});
		if (isMethodExist == true)
			return false;
		astRoot.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(TypeDeclaration node) {
				MethodDeclaration methodDeclaration = ast
						.newMethodDeclaration();
				methodDeclaration.setConstructor(false);
				methodDeclaration
						.modifiers()
						.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
				methodDeclaration.setName(ast.newSimpleName(methodname));
				org.eclipse.jdt.core.dom.Block block = ast.newBlock();
				methodDeclaration.setBody(block);
				node.bodyDeclarations().add(methodDeclaration);
				return true;
			}

		});

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			String newSource = document.get();
			// update of the compilation unit
			unit.getBuffer().setContents(newSource);
			unit.commitWorkingCopy(true, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean delTestMethod(final TestCaseNode classnode,
			final String methodname) {
		ResourceFinder finder = new ResourceFinder();
		IFile file = finder.findIFile(classnode);
		openEditor(file);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		String source = null;
		Document document = null;
		try {
			source = unit.getBuffer().getContents();
			document = new Document(source);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		parser.setSource(unit);
		// start record of the modifications
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		isMethodExist = false;
		astRoot.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getFullyQualifiedName().matches(methodname)) {
					node.delete();
					isMethodExist = true;
				}
				return true;
			}
		});
		if (isMethodExist == false)
			return false;

		try {
			// computation of the text edits
			TextEdit edits = astRoot.rewrite(document, unit.getJavaProject()
					.getOptions(true));
			// computation of the new source code
			edits.apply(document);
			String newSource = document.get();
			// update of the compilation unit
			unit.getBuffer().setContents(newSource);
			unit.commitWorkingCopy(true, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return true;
	}
}
