/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.command.RenameNodeCommand;
import gttlipse.vfsmEditor.parts.figures.StateFigure;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class StateRenameEditPolicy extends DirectEditPolicy {

	protected Command getDirectEditCommand(DirectEditRequest request) {
		RenameNodeCommand cmd = new RenameNodeCommand();
		cmd.setState((State) getHost().getModel());
		cmd.setName((String) request.getCellEditor().getValue());
		return cmd;
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((StateFigure) getHostFigure()).setName(value);
	}
}
