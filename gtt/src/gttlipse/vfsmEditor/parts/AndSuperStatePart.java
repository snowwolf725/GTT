package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.figures.AndSuperStateFigure;
import gttlipse.vfsmEditor.parts.policies.AndSuperStateLayoutEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;


public class AndSuperStatePart extends SuperStatePart {

	protected IFigure createFigure() {
		AndSuperStateFigure figure = new AndSuperStateFigure();
		return figure;
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		AndSuperStateFigure figure = (AndSuperStateFigure) getFigure();
		figure.setName(((State) this.getModel()).getName());
		figure.setIcon(image_registry.get("none"));
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new AndSuperStateLayoutEditPolicy());
	}
}
