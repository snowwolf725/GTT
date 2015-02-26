/**
 * 
 */
package gttlipse.scriptEditor.util;

import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.TestProject;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.util
 * 
 */
public class TestScriptNodeFinder {

	private AbstractNode m_parentnode;

	private TestScriptDocument m_doc;

	/**
	 * 
	 */
	public TestScriptNodeFinder() {
		// TODO Auto-generated constructor stub

	}

	public void rebuildSourceFolder(ProjectNode projectnode) {
		try {
			if (projectnode == null)
				return;
			ResourceFinder finder = new ResourceFinder();
			IProject project = finder.findIProject(projectnode);
			if (project == null)
				return;

			IJavaProject javaproject = JavaCore.create(project);
			IClasspathEntry classpath[] = javaproject.getRawClasspath();
			for (int i = 0; i < classpath.length; i++) {
				if (classpath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE
						&& classpath[i].getContentKind() == IPackageFragmentRoot.K_SOURCE) {
					String sourcefoldername = classpath[i].getPath()
							.toOSString();
					sourcefoldername = sourcefoldername.replaceAll("\\\\", "/");
					sourcefoldername = sourcefoldername.replaceAll("/"
							+ project.getName() + "/", "");
					BaseNode anode = projectnode
							.getChildrenByName(sourcefoldername);
					if (anode == null
							&& sourcefoldername.equals("/" + project.getName()) == false) {
						SourceFolderNode folder = new SourceFolderNode(
								sourcefoldername);
						projectnode.addChild(folder);
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AbstractNode findAbstractNode(TestCaseNode classnode, IPath path) {
		int scount = path.segmentCount();
		if (scount <= 2)
			return null;
		String[] paths = path.segments();
		TestMethodNode method = (TestMethodNode) classnode.getChildrenByName(paths[0]);
		if (method == null)
			return null;
		else {
			int docindex = Integer.parseInt(paths[1]);
			TestScriptDocument doc = method.getDocAt(docindex);
			if (doc != null) {
				AbstractNode parent = doc.getScript();
				for (int i = 2; i < scount && parent != null; i++) {
					int nodeindex = Integer.parseInt(paths[i]);
					AbstractNode child = parent.get(nodeindex);
					if (child != null)
						parent = child;
					else
						return null;
				}
				return parent;
			}
		}
		return null;
	}

	public BaseNode findNode(IPath path) {
		int scount = path.segmentCount();
		if (scount == 0)
			return null;
		String[] paths = path.segments();
		String projectname = paths[0];
		if (!TestProject.getProject().toString().equals(projectname))
			return null;
		// 重建 SourceFolder
		rebuildSourceFolder(TestProject.getProject());

		// 找出對應的 Node
		BaseNode foundNode = TestProject.getProject();
		for (int i = 1; i < paths.length; i++) {
			// 去掉 java file 的 file extension
			String nodename = paths[i];
			if (foundNode == null)
				return null;
			if (i == paths.length - 1) {
				if (paths[paths.length - 1].endsWith(".java")) {
					// class node
					nodename = paths[paths.length - 1].replaceAll(".java", "");
					CompositeNode cnode = (CompositeNode) foundNode;
					BaseNode child = cnode.getChildrenByName(nodename);
					if (child == null) {
						// ClassNode classnode = new ClassNode(nodename);
						// cnode.addChild(classnode);
						return null;
					}
					foundNode = cnode.getChildrenByName(nodename);
				} else {
					// package node
					CompositeNode cnode = (CompositeNode) foundNode;
					BaseNode child = cnode.getChildrenByName(nodename);
					if (child == null) {
						PackageNode packagenode = new PackageNode(nodename);
						cnode.addChild(packagenode);
					}
					foundNode = cnode.getChildrenByName(nodename);
				}
			} else if (foundNode instanceof ProjectNode) {
				// fix for source folder node
				CompositeNode cnode = (CompositeNode) foundNode;
				do {
					if (i >= paths.length)
						return null;
					nodename = paths[1];
					for (int index = 2; index <= i; index++)
						nodename = nodename + "/" + path.segment(index);
					foundNode = cnode.getChildrenByName(nodename);
					i++;
				} while (foundNode == null);
				i--;
			} else if (foundNode instanceof SourceFolderNode
					|| foundNode instanceof PackageNode) {
				// package Node
				CompositeNode cnode = (CompositeNode) foundNode;
				BaseNode child = cnode.getChildrenByName(nodename);
				if (child == null) {
					PackageNode packagenode = new PackageNode(nodename);
					cnode.addChild(packagenode);
				}
				foundNode = cnode.getChildrenByName(nodename);
			} else {
				return null;
			}
		}
		return foundNode;
	}

	public TestScriptDocument getTestScriptDocument(AbstractNode node) {
		m_parentnode = node;
		while (m_parentnode.getParent() != null) {
			m_parentnode = m_parentnode.getParent();
		}
		VisitAllScript(TestProject.getProject());

		return m_doc;
	}

	private void VisitAllScript(BaseNode node) {
		if (node instanceof ProjectNode || node instanceof SourceFolderNode
				|| node instanceof PackageNode || node instanceof TestCaseNode) {
			CompositeNode cmpnode = (CompositeNode) node;
			BaseNode[] childs = cmpnode.getChildren();
			if (childs == null)
				return;
			for (BaseNode child : childs) {
				VisitAllScript(child);
			}
		}

		if (node instanceof TestMethodNode) {
			TestMethodNode method = (TestMethodNode) node;
			TestScriptDocument[] docs = method.getDocuments();
			if (docs == null)
				return;
			for (TestScriptDocument doc : docs) {
				if (doc.getScript() == m_parentnode) {
					m_doc = doc;
				}
			}
		}
	}
}
