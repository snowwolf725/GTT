package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.parts.figures.StateFigure;
import gttlipse.vfsmEditor.parts.policies.StateRenameEditPolicy;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;


public class StatePart extends NodePart {

	@Override
	protected IFigure createFigure() {
		return new StateFigure();
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// state 具有 DirectEdit 的能力
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new StateRenameEditPolicy());
	}

	@Override
	final public void activate() {
		super.activate();
		((State) getModel()).addPropertyChangeListener(this);

		if (getParent().getModel() instanceof AbstractSuperState)
			((AbstractSuperState) getParent().getModel())
					.addPropertyChangeListener(this);
		if (getParent().getModel() instanceof Diagram)
			((Diagram) getParent().getModel()).addPropertyChangeListener(this);

	}

	@Override
	final public void deactivate() {
		super.deactivate();
		((State) getModel()).removePropertyChangeListener(this);

		if (getParent().getModel() instanceof AbstractSuperState)
			((AbstractSuperState) getParent().getModel())
					.removePropertyChangeListener(this);
		if (getParent().getModel() instanceof Diagram)
			((Diagram) getParent().getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT))
			performDirectEditRequest();
		if (req.getType().equals(RequestConstants.REQ_OPEN))
			getActualModel().setCollapsed(!getActualModel().getCollapsed());
	}

	protected DirectEditManager m_DirectManager;

	private void performDirectEditRequest() {
		// in-place Edit
		if (m_DirectManager == null) {
			StateFigure figure = (StateFigure) getFigure();
			m_DirectManager = new StateDirectEditManager(this,
					TextCellEditor.class, new NodeCellEditorLocator(figure));
		}
		m_DirectManager.show();
	}

	private static final List<ConnectionBase> EMPTY_LIST = new LinkedList<ConnectionBase>();

	protected boolean checkModelCollapsed() {
		if (getActualModel().getCollapsed())
			return true;
		State parent = getActualModel().getParent();
		while (parent != null) {
			if (parent.getCollapsed() == true)
				return true;
			parent = parent.getParent();
		}
		return false;
	}

	@Override
	protected List<ConnectionBase> getModelSourceConnections() {
		// 判斷 parent part 是否 collapse
		if (checkModelCollapsed())
			return EMPTY_LIST;
		State st = (State) this.getModel();
		return st.getOutgoingConnections();
	}

	@Override
	protected List<ConnectionBase> getModelTargetConnections() {
		// 判斷 parent part 是否 collapse
		if (checkModelCollapsed())
			return EMPTY_LIST;
		State st = (State) this.getModel();
		return st.getIncomingConnections();
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		((StateFigure) getFigure()).setName(getActualModel().getName());
		if(!(getActualModel() instanceof SuperState)) {
			((StateFigure) getFigure()).setBackgroundColor(getActualModel().getBackgroundColor());
		}
	}

	protected State getActualModel() {
		return (State) getModel();
	}
}
