/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteStateCommand extends Command {
	private State m_Node;

	private AbstractSuperState superstate;

	private int index;

	public void setSuperState(AbstractSuperState superstate) {
		this.superstate = superstate;
	}

	public void setNode(State node) {
		m_Node = node;
	}

	// ------------------------------------------------------------------------
	// Overridden from Command

	public void execute() {
		if (superstate == null)
			return;
		index = superstate.getAll().indexOf(m_Node);
		superstate.removeState(m_Node);
	}

	public String getLabel() {
		return "Delete Node";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (superstate == null)
			return;
		superstate.addState(index, m_Node);
	}
}