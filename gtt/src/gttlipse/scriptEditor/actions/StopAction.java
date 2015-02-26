/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.editor.configuration.IConfiguration;
import gtt.runner.Controller;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.views.GTTTestScriptView;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class StopAction extends EnhancedAction {
	private Controller _controller; // 2006art.

	/**
	 * 
	 */
	public StopAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public StopAction(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StopAction(String arg0, ImageDescriptor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StopAction(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		if (_controller == null)
			return;

		// get EventNode List -zwshen 2007/11/20
		List<AbstractNode> result = _controller
				.stopRecordAndGetResult(IConfiguration.MEDIUM_LEVEL);
		_controller.reset();
		if (result == null)
			return;
		GTTTestScriptView view = GTTlipse.showScriptView();
		Iterator<AbstractNode> ite = result.iterator();
		while (ite.hasNext()) {
			EventNode en = (EventNode) ite.next();
			if (en == null)
				continue;
			
			if(en.getEvent().getArguments().find("X") != null)
				en.getEvent().getArguments().find("X").setValue("0");
			if(en.getEvent().getArguments().find("Y") != null)
				en.getEvent().getArguments().find("Y").setValue("0");
			
			IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
					.getSelection();
			if (selection.getFirstElement() instanceof TestScriptDocument) {
				TestScriptDocument doc = (TestScriptDocument) selection
						.getFirstElement();
				doc.getScript().add(en);
			} else if (selection.getFirstElement() instanceof FolderNode) {
				FolderNode folder = (FolderNode) selection.getFirstElement();
				folder.add(en);
			}
			
			// add Component to history
			view.addComponent(en.getComponent());
		}
		// set dirty
		GTTlipse.makeScriptViewDirty();
		m_TreeViewer.refresh();
	}

	public void setController(Controller _controller) {
		this._controller = _controller;
	}

}
