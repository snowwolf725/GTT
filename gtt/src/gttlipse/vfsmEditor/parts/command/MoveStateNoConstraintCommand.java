/*
 *  create by jason 
 *  2008/04/28
 *  
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.State;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;


public class MoveStateNoConstraintCommand extends Command {
	private State m_state;
	private Point oldPos;
	private Point newPos;
	private Dimension oldSize;
	private Dimension newSize;

	public void setLocation(Point p) {
		oldPos = m_state.getLocation();
		newPos = p;
	}

	public void setDimension(Dimension d) {
		oldSize = this.m_state.getDimension();
		newSize = d;
	}

	public void setState(State s) {
		m_state = s;
	}

	public void execute() {
		moveState();
		resizeState();
	}

	private void moveState() {
		if (!m_state.isMoveable())
			return;
		m_state.setLocation(newPos);
	}

	private void resizeState() {
		if (!m_state.isResizeable())
			return;
		if(m_state.getCollapsed())
			return;	// 褶起來就不用resizse
		m_state.setDimension(newSize);
	}

	public String getLable() {
		return "Move/Resize Node";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		m_state.setLocation(oldPos);
		m_state.setDimension(oldSize);
	}
}
