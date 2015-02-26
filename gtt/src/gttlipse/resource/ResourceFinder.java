/**
 * 
 */
package gttlipse.resource;

import gttlipse.EclipseProject;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;


/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class ResourceFinder {
	private IFile m_file;
	private IProject m_project;
	private IResource res;

	public ResourceFinder() {
	}

	public IProject findIProject(String projectName) {
		// 找出使用者指定的專案
		m_project = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projects[] = root.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].getName().matches(projectName)
					&& projects[i].isOpen())
				m_project = projects[i];
		}
		return m_project;
	}

	public IProject findIProject(ProjectNode anode) {
		// 找出對應的專案
		if (anode != null)
			return findIProject(anode.getName());

		return null;
	}

	public IProject findIProject(BaseNode anode) {
		// 由任意 Node 找出對應的專案
		if (anode == null)
			return null;
		BaseNode parent = anode;

		while (!(parent instanceof ProjectNode) && parent != null) {
			parent = parent.getParent();
		}

		if (parent == null)
			return null;
		return findIProject((ProjectNode) parent);
	}

	public IResource findJavaSourceFolder(final IPath srcpath) {
		IProject project = findIProject(srcpath.segment(0));
		if (project == null)
			return null;

		res = null;

		try {
			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() != IResource.FOLDER)
						return true;
					if (!resource.getFullPath().toOSString().equals(
							srcpath.toOSString()))
						return true;

					res = resource;
					return false;
				}

			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return res;
	}

	public IResource findJavaPackage(PackageNode javapackage) {
		IProject project = findIProject(javapackage);
		if (project == null)
			return null;

		try {
			project.accept(new IResourceVisitor() {

				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FOLDER) {
						System.out.println(resource.getName());
					}
					return true;
				}

			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IFile findIFile(IProject project, String fileName,
			final TestCaseNode classnode) {
		// 找出對應的檔案
		if (project == null)
			return null;

		final String filename = fileName;
		try {
			project.accept(new IResourceVisitor() {

				public boolean visit(IResource resource) throws CoreException {
					// TODO Auto-generated method stub
					// System.out.println(resource.getName());
					if (resource.getType() != IResource.FILE)
						return true;
					if (!resource.getName().matches(filename))
						return true;
					String fullpath = resource.getFullPath().toString();
					if (fullpath.matches(getFullPath(classnode)))
						m_file = (IFile) resource;
					return false;
				}
			}, IResource.DEPTH_INFINITE, 0);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return m_file;
	}

	public IFile findIFile(TestCaseNode classnode) {
		// 找出對應的檔案
		if (classnode == null)
			return null;
		IProject m_project = EclipseProject.getEclipseProject();
		if (m_project == null)
			return null;
		
		return findIFile(m_project, classnode.getName() + ".java", classnode);
	}

	public String getFullPath(TestCaseNode classnode) {
		// 得到 TestCaseNode 對應到實際檔案的 FullPath
		if (classnode == null)
			return null;

		IProject m_project = EclipseProject.getEclipseProject();
		if (m_project == null)
			return null;

		String fullpath = "/" + classnode.toString() + ".java";
		BaseNode parentnode = classnode;
		do {
			if (parentnode.getParent() == null)
				return null;
			fullpath = "/" + parentnode.getParent().toString() + fullpath;
			parentnode = parentnode.getParent();
		} while (!(parentnode instanceof ProjectNode));

		return fullpath;
	}

	public static String slurp(InputStream in) {
		StringBuffer out = new StringBuffer();
		byte[] buf = new byte[4096];
		try {
			while (true) {
				int n = in.read(buf);
				if (n == -1)
					break;
				out.append(new String(buf, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	public void rename(IFile file, final String oldname, String newname) {
		if (file == null)
			return;

		try {
			String context = slurp(file.getContents());
			context = context.replaceAll(oldname, newname);
			InputStream is = new ByteArrayInputStream(context.getBytes());
			file.setContents(is, true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void rename2(IFile file, final String oldname, String newname) {
		// 開始進行轉換
		if (file == null)
			return;

		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		ASTNode astroot = parser.createAST(null);

		astroot.accept(new ASTVisitor() {
			public boolean visit(SimpleName node) {
				System.out.println("== SimpleName ==");
				System.out.println(node.toString());
				if (node.toString().matches(oldname)) {
					// AST ast=node.getAST();
					node.setIdentifier("ff");
				}
				return true;
			}
		});
	}

	public Vector<String> getSourceFolderPaths(IProject project) {
		Vector<String> sourcefolder = new Vector<String>();
		try {
			IJavaProject javaproject = JavaCore.create(project);
			IClasspathEntry classpath[] = javaproject.getRawClasspath();

			if (classpath.length == 0)
				return null;

			// int index = 0;
			for (int i = 0; i < classpath.length; i++) {
				if (IPackageFragmentRoot.K_SOURCE == classpath[i]
						.getContentKind()
						&& IClasspathEntry.CPE_SOURCE == classpath[i]
								.getEntryKind()) {
					sourcefolder.add(classpath[i].getPath().toOSString());
					// index++;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		return sourcefolder;
	}

	public IPath getSourceFolderPath(IPath resourcepath) {
		Vector<String> paths = getSourceFolderPaths(EclipseProject
				.getEclipseProject());
		Iterator<String> ite = paths.iterator();
		while (ite.hasNext()) {
			String path = (String) ite.next();
			if (resourcepath.toOSString().startsWith(path)) {
				return new Path(path);
			}
		}
		return null;
	}
}
