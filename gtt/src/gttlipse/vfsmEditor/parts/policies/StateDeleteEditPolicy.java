/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.command.DeleteDiagramCommand;
import gttlipse.vfsmEditor.parts.command.DeleteStateCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class StateDeleteEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest deleteRequest) {

		if (getHost().getParent().getModel() instanceof Diagram) {
			DeleteDiagramCommand delCmd = new DeleteDiagramCommand();
			delCmd.setDiagram((Diagram) getHost().getParent().getModel());
			delCmd.setNode((State) getHost().getModel());
			return delCmd;
		}

		if (getHost().getParent().getModel() instanceof AbstractSuperState) {
			DeleteStateCommand delCmd = new DeleteStateCommand();
			delCmd.setSuperState((AbstractSuperState) getHost().getParent()
					.getModel());
			delCmd.setNode((State) getHost().getModel());
			return delCmd;
		}

		return null;
	}
}
