/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.parts.policies.StateDeleteEditPolicy;
import gttlipse.vfsmEditor.parts.policies.StateGraphicalNodeEditPolicy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.ReconnectRequest;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class NodePart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	final int ANCHOR_METHOD_CHOPBOX = 0;
	final int ANCHOR_METHOD_NEWADD = 1;
	int anchorType = ANCHOR_METHOD_CHOPBOX;

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Node.PROP_LOCATION))
			refreshVisuals();
		else if (evt.getPropertyName().equals(Node.PROP_NAME))
			refreshVisuals();
		else if (evt.getPropertyName().equals(Node.PROP_SIZE))
			refreshVisuals();
		else if (evt.getPropertyName().equals(Node.PROP_INPUTS))
			refreshTargetConnections();
		else if (evt.getPropertyName().equals(Node.PROP_OUTPUTS))
			refreshSourceConnections();
		else if (evt.getPropertyName().equals(SuperState.PROP_COLLAPSED)) {
			refreshSourceConnections();
			refreshTargetConnections();
		}
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		switch (anchorType) {
		case ANCHOR_METHOD_CHOPBOX:
			return new ChopboxAnchor(getFigure());
		case ANCHOR_METHOD_NEWADD:
			ConnectionPart con = (ConnectionPart) connection;
			BorderAnchor anchor = con.getSourceAnchor();
			if (anchor == null || anchor.getOwner() != getFigure()) {
				if (getModel() instanceof State
						|| getModel() instanceof AbstractSuperState)
					anchor = new RectangleBorderAnchor(getFigure());
				else if (getModel() instanceof Initial
						|| getModel() instanceof Final)
					anchor = new EllipseBorderAnchor(getFigure());
				else
					throw new IllegalArgumentException("unexpected model");

				Connection conModel = (Connection) con.getModel();
				anchor.setAngle(conModel.getSourceAngle());
				con.setSourceAnchor(anchor);
			}
			return anchor;
		}
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		switch (anchorType) {
		case ANCHOR_METHOD_CHOPBOX:
			return new ChopboxAnchor(getFigure());
		case ANCHOR_METHOD_NEWADD:
			if (request instanceof ReconnectRequest)
				return createAnchor((ReconnectRequest) request);

			return createAnchor();
		}
		return null;
	}

	private ConnectionAnchor createAnchor(ReconnectRequest r) {
		ConnectionPart con = (ConnectionPart) r.getConnectionEditPart();
		Connection conModel = (Connection) con.getModel();
		BorderAnchor anchor = con.getSourceAnchor();
		GraphicalEditPart part = (GraphicalEditPart) r.getTarget();
		if (anchor == null || anchor.getOwner() != part.getFigure()) {
			anchor = createAnchor();
			anchor.setAngle(conModel.getSourceAngle());
			con.setSourceAnchor(anchor);
		}

		Rectangle rect = Rectangle.SINGLETON;
		rect.setBounds(getFigure().getBounds());
		getFigure().translateToAbsolute(rect);

		Point ref = rect.getCenter();
		Point loc = r.getLocation();
		double dx = loc.x - ref.x;
		double dy = loc.y - ref.y;

		anchor.setAngle(Math.atan2(dy, dx));
		conModel.setSourceAngle(anchor.getAngle());
		return anchor;
	}

	private BorderAnchor createAnchor() {
		if (getModel() instanceof Initial)
			return new EllipseBorderAnchor(getFigure());
		if (getModel() instanceof Final)
			return new EllipseBorderAnchor(getFigure());

		if (getModel() instanceof State)
			return new RectangleBorderAnchor(getFigure());
		if (getModel() instanceof AbstractSuperState)
			return new RectangleBorderAnchor(getFigure());

		throw new IllegalArgumentException("unexpected model");
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		switch (anchorType) {
		case ANCHOR_METHOD_CHOPBOX:
			return new ChopboxAnchor(getFigure());
		case ANCHOR_METHOD_NEWADD:
			ConnectionPart con = (ConnectionPart) connection;
			BorderAnchor anchor = con.getSourceAnchor();
			if (anchor == null || anchor.getOwner() != getFigure()) {
				if (getModel() instanceof State
						|| getModel() instanceof AbstractSuperState)
					anchor = new RectangleBorderAnchor(getFigure());
				else if (getModel() instanceof Initial
						|| getModel() instanceof Final)
					anchor = new EllipseBorderAnchor(getFigure());
				else
					throw new IllegalArgumentException("unexpected model");

				Connection conModel = (Connection) con.getModel();
				anchor.setAngle(conModel.getSourceAngle());
				con.setSourceAnchor(anchor);
			}
			return anchor;
		}
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		switch (anchorType) {
		case ANCHOR_METHOD_CHOPBOX:
			return new ChopboxAnchor(getFigure());
		case ANCHOR_METHOD_NEWADD:
			if (request instanceof ReconnectRequest)
				return createAnchor((ReconnectRequest) request);

			return createAnchor();
		}
		return null;
	}

	public void setSelected(int value) {
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE) {
			// if the connection be selected, increase line width and change
			// color to red.
			getFigure().setForegroundColor(ColorConstants.red);
		} else if (value == EditPart.SELECTED_NONE) {
			// if not selected, reduce line width to 1 and change color back to
			// black.
			getFigure().setForegroundColor(ColorConstants.black);
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new StateDeleteEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new StateGraphicalNodeEditPolicy());
	}
	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshParentLayoutConstraint();
	}

	private void refreshParentLayoutConstraint() {
		if (getParent() == null)
			return;
		State state = (State) getModel();
		Point loc = state.getLocation();
		Dimension size = new Dimension(state.getDimension());
		Rectangle rectangle = new Rectangle(loc, size);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);
	}
	
}