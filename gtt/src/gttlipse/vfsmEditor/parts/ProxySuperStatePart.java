package gttlipse.vfsmEditor.parts;

import gttlipse.GTTlipse;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.figures.ProxySuperStateFigure;
import gttlipse.vfsmEditor.parts.policies.SuperStateLayoutEditPolicy;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageRegistry;


public class ProxySuperStatePart extends SuperStatePart {

	ImageRegistry image_registry = GTTlipse.getDefault().getImageRegistry();

	protected List<State> getModelChildren() {
		return ((ProxySuperState) this.getModel()).getAll();
	}

	protected IFigure createFigure() {
		return new ProxySuperStateFigure();
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new SuperStateLayoutEditPolicy());
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		ProxySuperStateFigure figure = (ProxySuperStateFigure) getFigure();
		figure.setName(((State) this.getModel()).getName());
		if (!getActualModel().getCollapsed()) {
			figure.setIcon(image_registry.get("file"));
		} else {
			figure.setIcon(image_registry.get("folder"));
		}
	}

	public IFigure getContentPane() {
		return ((ProxySuperStateFigure) getFigure()).getFigure();
	}

	protected ProxySuperState getActualModel() {
		return (ProxySuperState) getModel();
	}
}
