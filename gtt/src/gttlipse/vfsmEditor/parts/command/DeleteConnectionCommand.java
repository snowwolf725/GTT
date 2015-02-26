/*
 * Created on 2005-1-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteConnectionCommand extends Command {

	State m_source;
	State m_target;

	Connection m_connection;

	// Setters
	public void setConnection(Connection connection) {
		this.m_connection = connection;
		m_source = m_connection.getSource();
		m_target = m_connection.getTarget();
	}

	public void execute() {
		if (m_source != null)
			m_source.removeOutput(m_connection);
		if (m_target != null)
			m_target.removeInput(m_connection);
		m_connection.setSource(null);
		m_connection.setTarget(null);
	}

	public String getLabel() {
		return "Delete Connection";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (m_source != null) {
			m_connection.setSource(m_source);
			m_source.addOutput(m_connection);
		}
		
		if (m_target != null) {
			m_connection.setTarget(m_target);
			m_target.addInput(m_connection);
		}
	}
}