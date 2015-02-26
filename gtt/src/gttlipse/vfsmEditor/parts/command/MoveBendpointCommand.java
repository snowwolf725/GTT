package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.ConnectionBendpoint;

import org.eclipse.draw2d.geometry.Dimension;


public class MoveBendpointCommand extends BendpointCommand {
	private Dimension oldDim1;
	private Dimension oldDim2;

	public void setOldRelativeDimensions(Dimension d1, Dimension d2) {
		this.oldDim1 = d1;
		this.oldDim2 = d2;
	}

	public void execute() {
		//Remember old location
		ConnectionBendpoint cbp=(ConnectionBendpoint)m_connection.getBendpoints().get(index);
		setOldRelativeDimensions(cbp.firstRelativeDim(), cbp.secondRelativeDim());
		//Set new location
		m_connection.setBendpointRelativeDimensions(index,d1,d2);
	}

	public void undo() {
		ConnectionBendpoint cbp=(ConnectionBendpoint)m_connection.getBendpoints().get(index);
		cbp.setDimension(oldDim1, oldDim2);
	}

}
