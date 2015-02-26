/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateStateOnSSCommand extends Command {
	protected Diagram m_diagram = null;

	protected AbstractSuperState m_SuperState = null;

	protected State m_newState;

	protected Point m_Location;

	public void setParentState(AbstractSuperState s) {
		m_SuperState = s;
	}

	public void setState(State s) {
		m_newState = s;
	}

	public void setLocation(Point location) {
		m_Location = location;
	}

	public void execute() {
		if (m_Location != null)
			m_newState.setLocation(m_Location);

		if (m_SuperState == null)
			return;

		m_SuperState.addState(m_newState);
	}

	public String getLabel() {
		return "Create Node";
	}

	public void redo() {
		this.execute();
	}

	public void undo() {
		m_diagram.removeNode(m_newState);
	}
}