package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.parts.command.BendpointCommand;
import gttlipse.vfsmEditor.parts.command.CreateBendpointCommand;
import gttlipse.vfsmEditor.parts.command.DeleteBendpointCommand;
import gttlipse.vfsmEditor.parts.command.MoveBendpointCommand;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;


public class ConnectionBendPointEditPolicy extends BendpointEditPolicy {

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendpointCommand cmd = new CreateBendpointCommand();
		Point p = request.getLocation();
		Connection conn = getConnection();

		conn.translateToRelative(p);

		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		cmd.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		cmd.setConnection((gttlipse.vfsmEditor.model.Connection) request.getSource()
				.getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		BendpointCommand cmd = new DeleteBendpointCommand();
		cmd.setConnection((ConnectionBase) request.getSource().getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand cmd = new MoveBendpointCommand();
		Point p = request.getLocation();
		Connection conn = getConnection();

		conn.translateToRelative(p);

		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		cmd.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		cmd.setConnection((gttlipse.vfsmEditor.model.Connection) request.getSource()
				.getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

}
