package gttlipse.vfsmEditor.io;

import gtt.testscript.AbstractNode;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.ConnectionBendpoint;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.IVFSMVisitor;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

import java.util.Iterator;
import java.util.List;


public class VFSMSaveVisitor implements IVFSMVisitor {

	org.w3c.dom.Document m_XmlDoc;
	org.w3c.dom.Element m_xmlRoot;

	public VFSMSaveVisitor() {
		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		m_xmlRoot = m_XmlDoc.createElement(VFSMDef.TAG_VFSM);
		m_XmlDoc.appendChild(m_xmlRoot);
	}

	public org.w3c.dom.Document getXmlDocument() {
		return m_XmlDoc;
	}

	@Override
	public void visit(Initial node) {
		org.w3c.dom.Element xml = createGeneralState(node, VFSMDef.TYPE_INITIAL);
		m_xmlRoot.appendChild(xml);
	}

	@Override
	public void visit(Final node) {
		org.w3c.dom.Element xml = createGeneralState(node, VFSMDef.TYPE_FINAL);
		m_xmlRoot.appendChild(xml);
	}

	@Override
	public void visit(State node) {
		org.w3c.dom.Element xml = createGeneralState(node, VFSMDef.TYPE_STATE);
		m_xmlRoot.appendChild(xml);
	}

	@Override
	public void visit(SuperState node) {
		org.w3c.dom.Element xml = createHasChildrenState(node,
				VFSMDef.TYPE_SUPERSTATE, node.getAll());
		m_xmlRoot.appendChild(xml);
	}

	public void visit(AndSuperState node) {
		org.w3c.dom.Element xml = createHasChildrenState(node,
				VFSMDef.TYPE_ANDSTATE, node.getAll());
		m_xmlRoot.appendChild(xml);
	}

	@Override
	public void visit(ProxySuperState node) {
		// proxy �n�x�s extra states �ήi�}�᪺ proxy state
		org.w3c.dom.Element xml = createGeneralState(node,
				VFSMDef.TYPE_PROXYSTATE);
		// �Ѩ쪺 Real State �� fullName
		xml.setAttribute(VFSMDef.TAG_TARGET, node.getRealState().fullName());

		// applying visitor for each child
		// real part
		org.w3c.dom.Element realXml = m_XmlDoc
				.createElement(VFSMDef.TAG_PROXY_REALSTATE);
		visitChildren(node.getRealStateInstance().getAll(), realXml);
		xml.appendChild(realXml);
		
		// extra part
		org.w3c.dom.Element extraXml = m_XmlDoc
				.createElement(VFSMDef.TAG_PROXY_EXTRASTATE);
		visitChildren(node.getExtraState().getAll(), extraXml);
		xml.appendChild(extraXml);

		m_xmlRoot.appendChild(xml);
	}

	// ///////////////////////////////////////////////////////////////////////
	// private methods
	private org.w3c.dom.Element createGeneralState(State node, String tag) {
		org.w3c.dom.Element xml = m_XmlDoc.createElement(tag);
		xml.setAttribute(VFSMDef.TAG_NAME, node.getName());
		xml.setAttribute(VFSMDef.TAG_LOCATION_X, "" + node.getLocation().x);
		xml.setAttribute(VFSMDef.TAG_LOCATION_Y, "" + node.getLocation().y);
		xml.setAttribute(VFSMDef.TAG_DIMENSION_W, ""
				+ node.getDimension().width);
		xml.setAttribute(VFSMDef.TAG_DIMENSION_H, ""
				+ node.getDimension().height);
		xml.setAttribute(VFSMDef.TAG_COLLAPSE, ""
				+ node.getCollapsed());
		// �[�J transitions
		xml.appendChild(createTransitionsXml(node));

		return xml;
	}

	private org.w3c.dom.Element createHasChildrenState(State node, String tag,
			List<State> states) {
		// general state
		org.w3c.dom.Element xml = createGeneralState(node, tag);
		// applying visitor for each child
		visitChildren(states, xml);
		return xml;
	}

	private void visitChildren(List<State> nodes, org.w3c.dom.Element xml) {
		// �O���ثe�� root
		org.w3c.dom.Element old_root = m_xmlRoot;
		// �� xml �����s��root
		m_xmlRoot = xml;
		for (int i = 0; i < nodes.size(); i++) {
			// �C��child ���nvisit �L
			nodes.get(i).accept(this);
		}
		// ���^�ª� xml root
		m_xmlRoot = old_root;
	}

	// �w�� node �s�X�h���Ҧ� Transitions ���s��
	private org.w3c.dom.Element createTransitionsXml(State node) {
		// Transition �����X
		org.w3c.dom.Element xml = m_XmlDoc
				.createElement(VFSMDef.TAG_TRANSITIONS);
		Iterator<ConnectionBase> cite = node.getOutgoingConnections()
				.iterator();
		while (cite.hasNext()) {
			// �C���X�h���䳣�n�x�s�@�� transition XML �`�I
			org.w3c.dom.Element tran = createSingleTransitionXml(cite.next());
			// �C�� tran ����J�� transitions ��
			xml.appendChild(tran);
		}
		return xml;
	}

	// �w��@�� transition ���s��
	private org.w3c.dom.Element createSingleTransitionXml(ConnectionBase trans) {
		org.w3c.dom.Element xml = m_XmlDoc
				.createElement(VFSMDef.TAG_TRANSITION);
		// 1. �C�� transition �n�x�s Condition
		xml.setAttribute(VFSMDef.TAG_CONDITION, trans.getCondition());
		// 2. �C�� transition �n�x�s target �ئa�`�I
		xml.setAttribute(VFSMDef.TAG_TARGET, trans.getTarget().fullName());
		// 3. �C�� transition �n�x�s action
		xml.setAttribute(VFSMDef.TAG_ACTION, trans.getAction());

		// 5. �C�� transition ���n�x�s Macro�ƥ�
		Iterator<AbstractNode> eite = trans.getEventList().iterator();
		while (eite.hasNext()) {
			// �C��MacroEvent����J�� transition ��
			org.w3c.dom.Element event = m_XmlDoc
					.createElement(VFSMDef.TAG_EVENT);
			event.setAttribute(VFSMDef.TAG_REFPATH, eite.next().toString());
			xml.appendChild(event);
		}

		// 6. �C�� Transition �� bend points
		Iterator<ConnectionBendpoint> bite = trans.getBendpoints().iterator();
		while (bite.hasNext()) {
			ConnectionBendpoint cb = bite.next();
			org.w3c.dom.Element tag = m_XmlDoc
					.createElement(VFSMDef.TAG_BENDPOINT);
			// d1
			tag.setAttribute(VFSMDef.TAG_DIMENSION1_W, "" + cb.getD1().width);
			tag.setAttribute(VFSMDef.TAG_DIMENSION1_H, "" + cb.getD1().height);
			// d2
			tag.setAttribute(VFSMDef.TAG_DIMENSION2_W, "" + cb.getD2().width);
			tag.setAttribute(VFSMDef.TAG_DIMENSION2_H, "" + cb.getD2().height);

			xml.appendChild(tag);
		}

		return xml;
	}
}
