/*
 *  create by jason 
 *  2008/04/28
 *  
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.command.CreateStateOnSSCommand;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;


public class SuperStateLayoutEditPolicy extends XYLayoutEditPolicy {

	class EllipseResizableEditPolicy extends ResizableEditPolicy {
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new EllipseResizableEditPolicy();
	}

	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return ChangeLayoutPolicy.layoutConstraintCommand(child, constraint);
	}

	protected Command getCreateCommand(CreateRequest request) {
		if (!(request.getNewObject() instanceof State))
			return null;

		CreateStateOnSSCommand cmd = new CreateStateOnSSCommand();
		cmd.setParentState((AbstractSuperState) getHost().getModel());
		cmd.setState((State) request.getNewObject());

		Rectangle constraint = (Rectangle) getConstraintFor(request);
		cmd.setLocation(constraint.getLocation());
		return cmd;
	}

	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}
}