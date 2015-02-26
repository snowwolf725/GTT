/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
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
 * created first in project GTTlipse.actions.Node
 * 
 */
public class AddEventNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public AddEventNodeAction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public AddEventNodeAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddEventNodeAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddEventNodeAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		if (selection.getFirstElement() instanceof TestScriptDocument) {
			EventNode eventnode = null; 
			TestScriptDocument doc = (TestScriptDocument) selection.getFirstElement();
			eventnode = getDefaultEventNode();
			doc.getScript().add(eventnode);
			eventnode.getComponent().setTitle(getWinTitle((FolderNode)doc.getScript(),eventnode));
			// set dirty
			GTTlipse.makeScriptViewDirty();
			} else if (selection.getFirstElement() instanceof FolderNode) {
			EventNode eventnode = null;
			FolderNode folderNode = (FolderNode) selection.getFirstElement();
			eventnode = getDefaultEventNode();
			folderNode.add(eventnode);
			eventnode.getComponent().setTitle(getWinTitle(folderNode,eventnode));
			// set dirty
			GTTlipse.makeScriptViewDirty();
			}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}

	private EventNode getDefaultSwingEventNode() {
		NodeFactory factory = new NodeFactory();
		EventNode eventnode;
		SwingComponent com=SwingComponent.createDefault();
		com.setType("javax.swing.JButton");
		SwingEvent event=SwingEvent.create(902, "PUSH_NO_BLOCK");
		eventnode = factory.createEventNode(com, event);
		return eventnode;
	}
	
	private String getWinTitle(FolderNode folder,AbstractNode node){
		int index = folder.indexOf(node);
		String title = "";
		if(index <= 0)
			return "";
		for(index--;index>=0;index--){
			if(folder.get(index) instanceof EventNode){
				EventNode prenode = (EventNode)folder.get(index);
				if(prenode.getComponent().getTitle() != null||
				   prenode.getComponent().getTitle().equals("")){
					title = prenode.getComponent().getTitle();
					return title;
				}
			} else if(folder.get(index) instanceof ViewAssertNode){
				ViewAssertNode prenode = (ViewAssertNode)folder.get(index);
				if(prenode.getComponent().getTitle() != null||
						   prenode.getComponent().getTitle().equals("")){
					title = prenode.getComponent().getTitle();
					return title;
				}
			}
		}
		return title;
	}
	
	private EventNode getDefaultEventNode() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_SCRIPT_ACTION_DEFAULT_EVENTNODE_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				IDefaultEventNode defaultEventNode = (IDefaultEventNode)o;
				if (defaultEventNode instanceof IDefaultEventNode &&
					GTTlipse.getPlatformInfo().getTestPlatformID() == defaultEventNode.getPlatformID()) {
					return defaultEventNode.getDefaultEventNode();
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return getDefaultSwingEventNode();
	}
}
