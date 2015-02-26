package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.figures.FinalFigure;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;


public class FinalPart extends StatePart {
	protected DirectEditManager manager;

	protected FinalFigure createFigure() {
		return new FinalFigure();
	}

	protected void refreshVisuals() {
		State node = getActualModel();
		Rectangle rectangle = new Rectangle(node.getLocation(), node
				.getDimension());
		((FinalFigure) this.getFigure())
				.setName(getActualModel().getName());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);
	}
}
