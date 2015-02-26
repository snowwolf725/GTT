/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.swing.SwingComponent;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions.Node
 * 
 */
public class AddAssertNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public AddAssertNodeAction() {

	}

	/**
	 * @param text
	 */
	public AddAssertNodeAction(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddAssertNodeAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddAssertNodeAction(String text, int style) {
		super(text, style);
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if (selection.getFirstElement() instanceof TestScriptDocument) {
			ViewAssertNode assertnode = null;
			TestScriptDocument doc = (TestScriptDocument) selection
					.getFirstElement();
			assertnode = getDefaultAssertNode();
			doc.getScript().add(assertnode);
			assertnode.getComponent().setTitle(
					getWinTitle((FolderNode) doc.getScript(), assertnode));
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof FolderNode) {
			ViewAssertNode assertnode = null;
			FolderNode folderNode = (FolderNode) selection.getFirstElement();
			assertnode = getDefaultAssertNode();
			folderNode.add(assertnode);
			assertnode.getComponent().setTitle(
					getWinTitle(folderNode, assertnode));
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
	
	private ViewAssertNode getDefaultSwingAssertNode() {
		NodeFactory factory = new NodeFactory();
		ViewAssertNode assertnode;
		SwingComponent com=SwingComponent.createDefault();
		com.setName("Assert Object");
		Assertion a = new Assertion();
		assertnode = factory.createViewAssertNode(com, a);
		return assertnode;
	}

	private String getWinTitle(FolderNode folder, AbstractNode node) {
		int index = folder.indexOf(node);
		String title = "";
		if (index <= 0)
			return "";
		for (index--; index >= 0; index--) {
			if (folder.get(index) instanceof EventNode) {
				EventNode prenode = (EventNode) folder.get(index);
				if (prenode.getComponent().getTitle() != null
						|| prenode.getComponent().getTitle().equals("")) {
					title = prenode.getComponent().getTitle();
					return title;
				}
			} else if (folder.get(index) instanceof ViewAssertNode) {
				ViewAssertNode prenode = (ViewAssertNode) folder.get(index);
				if (prenode.getComponent().getTitle() != null
						|| prenode.getComponent().getTitle().equals("")) {
					title = prenode.getComponent().getTitle();
					return title;
				}
			}
		}
		return title;
	}
	
	private ViewAssertNode getDefaultAssertNode() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_SCRIPT_ACTION_DEFAULT_ASSERTNODE_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				IDefaultAssertNode defaultAssertNode = (IDefaultAssertNode)o;
				if (defaultAssertNode instanceof IDefaultAssertNode &&
					GTTlipse.getPlatformInfo().getTestPlatformID() == defaultAssertNode.getPlatformID()) {
					return defaultAssertNode.getDefaultAssertNode();
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return getDefaultSwingAssertNode();
	}
}
