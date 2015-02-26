/**
 * Copied from GEF Logic example.
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.ConnectionBase;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;


public class BendpointCommand extends Command {

	protected int index;
	protected ConnectionBase m_connection;
	protected Dimension d1, d2;

	public void setConnection(ConnectionBase connection) {
		this.m_connection = connection;
	}

	public void redo() {
		execute();
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setIndex(int i) {
		index = i;
	}

}
