/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.TestProject;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.TestScriptNodeFinder;
import gttlipse.scriptEditor.views.GTTTestScriptView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.StructuredSelection;


/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class GoToScript {
	private GTTTestScriptView view = null;
	private TestCaseNode m_classnode;
	private TestMethodNode m_methodnode;

	/**
	 * 
	 */
	public GoToScript() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 方法 gotoScriptFromMethodAndDoc
	 */
	public void gotoScriptFromMethodAndDoc(final IDocument doc,
			final int m_lineOfCursor, IFile file) {
		GTTlipse.showScriptView();
		final GTTTestScriptView view = GTTlipse.findScriptView();
		view.setFocus();

		String paths[] = file.getFullPath().segments();
		// don't care
		if (paths.length <= 2)
			return;
		// not match project name

		if (!paths[0].matches(TestProject.getProject().getName()))
			return;
		// source folder not found
		ResourceFinder finder = new ResourceFinder();
		IPath sourcepath = finder.getSourceFolderPath(file.getFullPath());
		if (sourcepath == null)
			return;
		int sourcepathoffset = sourcepath.segmentCount();
		TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
		CompositeNode sourcefolder = (CompositeNode) nodefinder
				.findNode(sourcepath);
		if (sourcefolder == null)
			return;

		CompositeNode parent = sourcefolder;
		BaseNode child = null;
		for (int i = sourcepathoffset; i < paths.length - 1; i++) {
			System.out.println(paths[i]);
			child = parent.getChildrenByName(paths[i]);
			if (child == null) {
				child = new PackageNode(paths[i]);
				parent.addChild(child);
			}
			if (child instanceof CompositeNode)
				parent = (CompositeNode) child;
			child = null;
		}

		String filename = paths[paths.length - 1].replaceAll(".java", "");
		child = parent.getChildrenByName(filename);
		if (child == null) {
			parent.addChild(new TestCaseNode(filename));
			child = parent.getChildrenByName(filename);
		}

		m_classnode = (TestCaseNode) child;
		List<BaseNode> items = new ArrayList<BaseNode>();
		items.add(m_classnode);
		view.getTreeViewer().refresh();
		view.getTreeViewer().setSelection(new StructuredSelection(items));

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		parser.setSource(unit);
		ASTNode astroot = parser.createAST(null);

		astroot.accept(new ASTVisitor() {
			private int docindex = -1; // 判斷是該 Method 的第幾個 TestScriptDocument
			private boolean inmethod = false; // 判斷是否在游標所在的 Method 中

			public boolean visit(MethodDeclaration node) {
				try {
					int lineOfMethodStart = doc.getLineOfOffset(node
							.getStartPosition());
					int lineOfMethodEnd = doc.getLineOfOffset(node
							.getStartPosition()
							+ node.getLength());
					inmethod = false;
					if (m_classnode == null)
						return true;

					if (m_lineOfCursor >= lineOfMethodStart
							&& m_lineOfCursor <= lineOfMethodEnd) {
						inmethod = true;
						String methodname = node.getName()
								.getFullyQualifiedName();
						m_methodnode = (TestMethodNode) m_classnode
								.getChildrenByName(methodname);
						if (m_methodnode == null) {
							m_methodnode = new TestMethodNode(node.getName()
									.getFullyQualifiedName());
							m_classnode.addChild(m_methodnode);
						}
						List<BaseNode> items = new ArrayList<BaseNode>();
						items.add(m_methodnode);
						view.getTreeViewer().refresh();
						/* 展開到該層目錄,要先更新樹狀結構不然會找不到 Node */
						view.getTreeViewer()
								.setExpandedState(m_classnode, true);
						view.getTreeViewer().setSelection(
								new StructuredSelection(items), true);
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}

			public boolean visit(ExpressionStatement node) {
				if (m_methodnode == null || inmethod == false)
					return true;

				String line = node.toString();
				Pattern pattern = Pattern
						.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					try {
						docindex++;
						// 分析出需要的 TestDocument 資訊
						String str = m.group();
						str = str
								.replaceAll(
										TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN,
										"");
						str = str.replaceAll("\"\\);", "");
						if (m_methodnode.getDocAt(docindex) == null) {
							// 如果發現有不存在的 TestScript 順便重建,使用 getDocAt
							// 找避免找到同名但不同位置的
							m_methodnode.addNewDoc(str);
						}
						if (m_lineOfCursor != doc.getLineOfOffset(node
								.getStartPosition()))
							return true;
						/* 到這裡表示找到了 */
						List<TestScriptDocument> items = new ArrayList<TestScriptDocument>();
						items.add(m_methodnode.getDocAt(docindex));
						view.getTreeViewer().refresh();
						/* 展開到該層目錄,要先更新樹狀結構不然會找不到 Node */
						view.getTreeViewer().setExpandedState(m_methodnode,
								true);
						view.getTreeViewer().setSelection(
								new StructuredSelection(items));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return true;
			}

		});
	}

	/**
	 * @param resource
	 *            方法 gotoScriptFromResource
	 */
	public void gotoScriptFromResource(IResource resource) {
		view = GTTlipse.showScriptView();
		view.setFocus();

		String paths[] = resource.getFullPath().segments();
		// don't care
		if (paths.length <= 2)
			return;

		// not match project name
		ProjectNode projectnode = TestProject.getProject();
		if (!paths[0].matches(projectnode.getName()))
			return;
		// source folder not found
		ResourceFinder finder = new ResourceFinder();
		IPath sourcepath = finder.getSourceFolderPath(resource.getFullPath());
		if (sourcepath == null)
			return;
		int sourcepathoffset = sourcepath.segmentCount();
		TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
		CompositeNode sourcefolder = (CompositeNode) nodefinder
				.findNode(sourcepath);
		if (sourcefolder == null)
			return;

		CompositeNode parent = sourcefolder;
		BaseNode child = null;
		for (int i = sourcepathoffset; i < paths.length - 1; i++) {
			System.out.println(paths[i]);
			child = parent.getChildrenByName(paths[i]);
			if (child == null) {
				child = new PackageNode(paths[i]);
				parent.addChild(child);
			}
			if (child instanceof CompositeNode)
				parent = (CompositeNode) child;
			child = null;
		}

		if (resource instanceof IFile) {
			String filename = paths[paths.length - 1].replaceAll(".java", "");
			child = parent.getChildrenByName(filename);
			if (child == null) {
				parent.addChild(new TestCaseNode(filename));
				child = parent.getChildrenByName(filename);
			}
		} else if (resource instanceof IFolder) {
			String foldername = paths[paths.length - 1];
			child = parent.getChildrenByName(foldername);
			if (child == null) {
				parent.addChild(new PackageNode(foldername));
				child = parent.getChildrenByName(foldername);
			}
		}

		List<BaseNode> items = new ArrayList<BaseNode>();
		items.add(child);
		view.getTreeViewer().refresh();
		view.getTreeViewer().setSelection(new StructuredSelection(items));
	}

}
