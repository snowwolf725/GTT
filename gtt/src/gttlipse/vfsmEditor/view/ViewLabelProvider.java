package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class ViewLabelProvider extends LabelProvider {
	private ImageRegistry m_ImageRegistry = null;

	ViewLabelProvider(ImageRegistry registry) {
		super();
		m_ImageRegistry = registry;
	}

	public String getText(Object obj) {
		if (obj instanceof Node) {
			State node = (State) obj;
			return node.getName();
		}
		if (obj instanceof Connection) {
			// Connection conn = (Connection) obj;
			return VFSMDef.FSM_OUTPUT;
		}

		return null;
	}

	public Image getImage(Object obj) {

		// if(!(obj instanceof Node))
		// return null;
		//		
		// Node node = (Node) obj;
		// node.getType();
		// return m_ImageRegistry.get(node.getType());

		if (obj instanceof Initial) {
			return m_ImageRegistry.get("initial");
		}

		if (obj instanceof Final) {
			return m_ImageRegistry.get("final");
		}

		if (obj instanceof AndSuperState) {
			return m_ImageRegistry.get("andsuperstate");
		}
		if (obj instanceof ProxySuperState) {
			return m_ImageRegistry.get("instance");
		}
		if (obj instanceof AbstractSuperState) {
			AbstractSuperState temp = (AbstractSuperState) obj;
			if (temp.getName() == "Diagram")
				return m_ImageRegistry.get("diagram");
			if (temp.getName() == VFSMDef.FSM_FSM)
				return m_ImageRegistry.get("declaration");
			if (temp.getName() == VFSMDef.FSM_MAIN)
				return m_ImageRegistry.get("main");
			return m_ImageRegistry.get("superstate");
		}

		if (obj instanceof Connection) {
			return m_ImageRegistry.get("output");
		}

		if (obj instanceof State) {
			return m_ImageRegistry.get("state");
		}

		return null;
	}

}
