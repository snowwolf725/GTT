/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.command.CreateConnectionCommand;
import gttlipse.vfsmEditor.parts.command.ReconnectSourceCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;



/**
 * @author zhanghao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StateGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
    	// 開始建立連線
        CreateConnectionCommand command = new CreateConnectionCommand();
        command.setSource((State) getHost().getModel());
        request.setStartCommand(command);
        return command;
    }
	
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
    	// 建立連線完成
        CreateConnectionCommand command = (CreateConnectionCommand) request.getStartCommand();
        command.setTarget((State) getHost().getModel());
        return command;
    }

    protected Command getReconnectSourceCommand(ReconnectRequest request) {
    	// 重新連接 source
		ReconnectSourceCommand cmd = new ReconnectSourceCommand();
		cmd.setConnection((Connection)request.getConnectionEditPart().getModel());
		cmd.setSource((State)getHost().getModel());
		return cmd;
    }

    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        return null;
    }
}