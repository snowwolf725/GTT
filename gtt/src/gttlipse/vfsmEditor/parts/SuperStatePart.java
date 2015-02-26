package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.figures.StateFigure;
import gttlipse.vfsmEditor.parts.figures.SuperStateFigure;
import gttlipse.vfsmEditor.parts.policies.SuperStateLayoutEditPolicy;
import gttlipse.vfsmEditor.view.IVFSMEditor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageRegistry;


public class SuperStatePart extends StatePart {

	ImageRegistry image_registry = IVFSMEditor.IMAGE_REGISTRY;

	protected List<State> getModelChildren() {
		return getActualModel().getAll();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (AbstractSuperState.PROP_COLLAPSED.equals(evt.getPropertyName())) {
			// do collapse
			SuperStateFigure figure = ((SuperStateFigure) getFigure());
			if (getActualModel().getCollapsed()) {
				// ���_�ӡA�N������� content
			} else {
				try {
					figure.remove(getContentPane());
				} catch (Exception ep) {
					// not a child
				}
				figure.add(getContentPane());
			}
			refreshVisuals();
			refreshSourceConnections();
			refreshTargetConnections();
			return;
		}

		if (AbstractSuperState.PROP_STRUCTURE.equals(evt.getPropertyName())) {
			refreshChildren();
			return;
		}

		// ��o�� parent
		super.propertyChange(evt);
	}

	protected IFigure createFigure() {
		return new SuperStateFigure();
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		// �h�[�W Layout policy
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new SuperStateLayoutEditPolicy());
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		StateFigure figure = (StateFigure) getFigure();
		figure.setName(((State) this.getModel()).getName());
		if (getActualModel().getCollapsed())
			// ���_�ӴN��ܡ@folder icon
			figure.setIcon(image_registry.get("folder"));
		else {
			// �i�}�N��ܡ@file icon
			figure.setIcon(image_registry.get("file"));
		}
	}

	public IFigure getContentPane() {
		return ((SuperStateFigure) getFigure()).getFigure();
	}

	protected AbstractSuperState getActualModel() {
		return (AbstractSuperState) getModel();
	}

	protected List<ConnectionBase> getModelSourceConnections() {
		// ���_�ӡA�N�u�n�^�Ǧۨ�state���s�u
		if (!checkModelCollapsed())
			return getActualModel().getOutgoingConnections();

		List<ConnectionBase> list = new ArrayList<ConnectionBase>();
		list.addAll(getActualModel().getOutgoingConnections());
		Iterator<State> iter = getActualModel().getAll().iterator();
		while (iter.hasNext()) {
			State s = iter.next();
			list.addAll(s.getOutgoingConnections());
		}
		return list;
	}

	protected List<ConnectionBase> getModelTargetConnections() {
		// ���_�ӡA�N�u�n�^�Ǧۨ�state���s�u
		if (!checkModelCollapsed())
			return getActualModel().getIncomingConnections();

		List<ConnectionBase> list = new ArrayList<ConnectionBase>();
		list.addAll(getActualModel().getIncomingConnections());
		Iterator<State> iter = getActualModel().getAll().iterator();
		while (iter.hasNext()) {
			State s = iter.next();
			list.addAll(s.getIncomingConnections());
		}
		return list;
	}
}
