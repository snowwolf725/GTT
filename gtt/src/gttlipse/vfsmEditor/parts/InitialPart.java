package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.figures.InitialFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;


public class InitialPart extends StatePart {
	protected IFigure createFigure() {
		return new InitialFigure();
	}

	protected void refreshVisuals() {
		State node = getActualModel();
		Rectangle rectangle = new Rectangle(node.getLocation(), node
				.getDimension());
		((InitialFigure) this.getFigure()).setName(((State) getModel())
				.getName());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);
	}
}
