/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.command.CreateStateOnDiagramCommand;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramLayoutEditPolicy extends XYLayoutEditPolicy {

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
		if (request.getNewObject() instanceof State) {
			CreateStateOnDiagramCommand cmd = new CreateStateOnDiagramCommand();
			cmd.setDiagram((Diagram) getHost().getModel());
			cmd.setState((State) request.getNewObject());
			Rectangle rect = (Rectangle) getConstraintFor(request);
			cmd.setLocation(rect.getLocation());
			return cmd;
		}
		return null;
	}

	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}
}