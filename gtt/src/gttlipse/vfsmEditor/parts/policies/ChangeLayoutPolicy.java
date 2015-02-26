package gttlipse.vfsmEditor.parts.policies;

import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.parts.StatePart;
import gttlipse.vfsmEditor.parts.command.MoveStateHasConstraintCommand;
import gttlipse.vfsmEditor.parts.command.MoveStateNoConstraintCommand;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;


public class ChangeLayoutPolicy {
	public static Command layoutConstraintCommand(EditPart child,
			Object constraint) {
		if (!(child instanceof StatePart))
			return null;
		if (!(constraint instanceof Rectangle))
			return null;
		// rectangle constraint 0: no constraint, 1: constraint.
		final int RECT_NO_CONSTRAINT = 0;
		final int RECT_CONSTRAINT = 1;
		final int rectConstraint = RECT_NO_CONSTRAINT;
		switch (rectConstraint) {
		case RECT_NO_CONSTRAINT:
			return forNoConstraint((State) child.getModel(),
					(Rectangle) constraint);
		case RECT_CONSTRAINT:
			return forHasConstraint((State) child.getModel(),
					(Rectangle) constraint);
		}
		return null;
	}

	private static MoveStateNoConstraintCommand forNoConstraint(State s,
			Rectangle rect) {
		if (!s.isMoveable() && s.isResizeable())
			return null;
		MoveStateNoConstraintCommand cmd = new MoveStateNoConstraintCommand();
		cmd.setState(s);
		cmd.setLocation(rect.getLocation());
		cmd.setDimension(rect.getSize());
		return cmd;
	}

	private static Command forHasConstraint(State s, Rectangle rect) {
		if (!s.isMoveable() && s.isResizeable())
			return null;
		if (s instanceof SuperState)
			return forNoConstraint(s, rect);
		MoveStateHasConstraintCommand cmd = new MoveStateHasConstraintCommand();
		cmd.setState(s);
		cmd.setLocation(rect.getLocation());
		return cmd;
	}

}
