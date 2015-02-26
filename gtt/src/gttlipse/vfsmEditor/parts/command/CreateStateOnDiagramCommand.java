/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

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
public class CreateStateOnDiagramCommand extends Command {
	protected Diagram m_diagram = null;

	protected State m_newState;

	protected Point m_Location;

	// setters
	public void setDiagram(Diagram diagram) {
		this.m_diagram = diagram;
	}

	public void setState(State node) {
		m_newState = node;
	}

	public void setLocation(Point location) {
		m_Location = location;
	}

	public void execute() {
		if (m_Location != null)
			m_newState.setLocation(m_Location);
		
		if (m_diagram == null)
			return;

		m_diagram.getMainState().addState(m_newState);
	}

	public String getLabel() {
		return "Create Node (on Diagram)";
	}

	public void redo() {
		this.execute();
	}

	public void undo() {
		m_diagram.removeNode(m_newState);
	}
}