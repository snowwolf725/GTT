/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteDiagramCommand extends Command {
	private Diagram diagram;

	private State m_Node;

	private int index;

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}

	public void setNode(State node) {
		m_Node = node;
	}

	// ------------------------------------------------------------------------
	// Overridden from Command

	public void execute() {
		if (diagram == null)
			return;
		index = diagram.getMainState().getAll().indexOf(m_Node);
		diagram.removeNode(m_Node);
	}

	public String getLabel() {
		return "Delete Node";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (diagram == null)
			return;
		diagram.getMainState().addState(index, m_Node);
	}
}