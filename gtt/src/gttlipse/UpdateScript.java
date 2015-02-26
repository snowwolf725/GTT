/**
 * 
 */
package gttlipse;

import gtt.testscript.TestScriptDocument;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;


/**
 * @author 王榮麒
 * 
 * created first in project GTTlipse.util.IResource
 * 
 */
public class UpdateScript {

	/**
	 * 
	 */
	public UpdateScript() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * 方法 sync
	 */
	public void Sync() {
		ProjectNode projectnode = TestProject.getProject();
		ProjectNode oldprojectnode = projectnode.clone();
		ConvertResourceToScript();
		ProjectNode newprojectnode = TestProject.getProject();
		mergeScript(oldprojectnode,newprojectnode);
	}

	/**
	 * 
	 * 方法 ConvertResourceToScript
	 */
	private void ConvertResourceToScript() {
		ProjectNode projectnode = TestProject.getProject();
		projectnode.clearChildren();
		ResourceFinder finder = new ResourceFinder();
		IProject project = finder.findIProject(projectnode);
		if(project == null) return;
		IJavaProject javaproject = JavaCore.create(project);
		if(javaproject == null)return;
		try {
			IClasspathEntry classpath[] = javaproject.getRawClasspath();
			IFolder srcfolder = null;
			for(int j=0;j<classpath.length;j++) {
				if(classpath[j].getContentKind() == IPackageFragmentRoot.K_SOURCE &&
				   classpath[j].getEntryKind() == IClasspathEntry.CPE_SOURCE ) {
					String foldername = classpath[j].getPath().toOSString();
					foldername = foldername.replaceAll("\\\\","/");
					foldername = foldername.replaceAll("/"+project.getName()+"/", "");
					srcfolder = project.getFolder(new Path(foldername));
					if(srcfolder.exists() == false) continue;
					SourceFolderNode folder = new SourceFolderNode(foldername);
					projectnode.addChild(folder);
					BuildResourceTree(folder,srcfolder);
				}
			}
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param folder
	 * @throws CoreException
	 * 方法 BuildResourceTree
	 */
	private void BuildResourceTree(CompositeNode parent,IFolder folder) throws CoreException {
		if(folder == null)return;
		IResource [] res = folder.members();
		if(res == null)return;
		for(int i=0;i<res.length;i++) {
			if(res[i].getType() == IResource.FOLDER) {
				PackageNode javapackage = new PackageNode(res[i].getName());
				parent.addChild(javapackage);
				BuildResourceTree(javapackage,(IFolder)res[i]);
			} else if(res[i].getType() == IResource.FILE && 
					  res[i].getName().endsWith(".java")) {
				String filename = res[i].getName().replaceAll(".java", "");
				TestCaseNode classnode = new TestCaseNode(filename);
				parent.addChild(classnode);
				BuildMethodAndDoc(classnode,(IFile)res[i]);
			}
		}
	}
	
	private void BuildMethodAndDoc(final TestCaseNode classnode,IFile file) {
//		 rebuild all method and TestScriptDocument
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		parser.setSource(unit);
		ASTNode astroot = parser.createAST(null);
		astroot.accept(new ASTVisitor() {
			private TestMethodNode _methodnode=null;
			private TestMethodNode _oldmethodnode=null;
			
			public boolean visit(MethodDeclaration node) {
				String methodname = node.getName().getFullyQualifiedName();
				_methodnode = (TestMethodNode) classnode.getChildrenByName(methodname);
				if(_methodnode == null) {
					_methodnode = new TestMethodNode(methodname);
					classnode.addChild(_methodnode);
				}
				_oldmethodnode = _methodnode.clone();
				_methodnode.clearDoc();
				return true;
			}
			
			public boolean visit(ExpressionStatement node) {
				String line = node.toString();
				Pattern pattern = Pattern.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					// 分析出需要的 TestDocument 資訊
					String str = m.group();
					str=str.replaceAll(TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN, "");
					str=str.replaceAll("\"\\);", "");
					TestScriptDocument doc = _oldmethodnode.getDocByName(str,1);
					if(doc != null) {
						_methodnode.addDocument(doc.clone());
						_oldmethodnode.removeDoc(doc);
					} else {
						_methodnode.addNewDoc(str);
					}
//					_methodnode.addNewNode(str);
				}
				return true;
			}
		});
	}
	
	private void mergeScript(CompositeNode oldproject, CompositeNode newproject) {
		
		for(int i=0;i<oldproject.size();i++) {
			for(int j=0;j<newproject.size();j++) {
				if(oldproject.getChildrenAt(i).getName().matches(newproject.getChildrenAt(j).getName()) &&
				   oldproject.getChildrenAt(i).getClass().toString().matches(newproject.getChildrenAt(j).getClass().toString())
				   ){
					if(oldproject.getChildrenAt(i) instanceof TestCaseNode) {
						//用舊的 Node 換掉(保有原先的Doc下的資訊)
						CompositeNode parent = newproject.getChildrenAt(j).getParent();
						TestCaseNode classnode = ((TestCaseNode)oldproject.getChildrenAt(i)).clone();
						parent.removeChild(newproject.getChildrenAt(j));
						parent.addChild(classnode);
						ResourceFinder finder = new ResourceFinder();
						IFile file = finder.findIFile(classnode);
						//重新 ReBuild
						BuildMethodAndDoc(classnode,file);
					} else if(oldproject.getChildrenAt(i) instanceof PackageNode) {
						mergeScript((CompositeNode)oldproject.getChildrenAt(i),(CompositeNode)newproject.getChildrenAt(j));
					} else if(oldproject.getChildrenAt(i) instanceof SourceFolderNode) {
						mergeScript((CompositeNode)oldproject.getChildrenAt(i),(CompositeNode)newproject.getChildrenAt(j));
					}
				}
			}
			
		}
	}

}
