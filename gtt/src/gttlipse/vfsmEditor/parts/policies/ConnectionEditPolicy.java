/*
 * Created on 2005-1-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.parts.command.DeleteConnectionCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ConnectionEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Connection conn = (Connection) getHost().getModel();
		DeleteConnectionCommand cmd = new DeleteConnectionCommand();
		cmd.setConnection(conn);
		return cmd;
	}
}
