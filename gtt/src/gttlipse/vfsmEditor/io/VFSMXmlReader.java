package gttlipse.vfsmEditor.io;

import gtt.util.Pair;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.view.VFSMDiagram;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class VFSMXmlReader {

	private Document m_XmlDoc = null;
	private State m_VFSMRoot;
	private List<ConnectionTemp> m_ConnectionStore = new LinkedList<ConnectionTemp>();
	private List<ConnectionTemp> m_ProxyConnectionStore = new LinkedList<ConnectionTemp>();
	private List<Pair> m_ProxyStateList = new LinkedList<Pair>();

	private ConnectionTemp m_CurrentConnection;

	public VFSMDiagram getVFSMDiagram() {
		return m_Diagram;
	}

	private VFSMDiagram m_Diagram;

	public VFSMXmlReader() {
		m_Diagram = new VFSMDiagram();
		m_VFSMRoot = m_Diagram.getFSMRoot();
	}

	public org.w3c.dom.Node checkRoot(org.w3c.dom.Node node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node cur = node.getChildNodes().item(i);
			// �|���� ROOT Superstate
			if (cur.getNodeName().compareTo(VFSMDef.TYPE_SUPERSTATE) == 0)
				return cur;
		}
		return null;
	}

	public Element getElementByName(org.w3c.dom.Node node, String name) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node cur = node.getChildNodes().item(i);
			if (!isElementNode(cur))
				continue;
			Element e = (Element) cur;
			if (e.getAttribute(VFSMDef.TAG_NAME).equals(name))
				return e;
		}
		return null;
	}

	public boolean read(String filepath) {
		try {
			/* open file, path is file path */
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File myfile = new File(filepath);
			if(myfile.exists() == false){
				System.out.println("File:" + filepath + " isn't exist.");
				return false;
			}else if(myfile.length() == 0){
				System.out.println("File:" + filepath + " isn't corrent.");
				return false;
			}
			m_XmlDoc = builder.parse(myfile);

			org.w3c.dom.Node node0 = getXmlRootNode();
			if (node0 == null) {
				System.out.println("File:" + filepath + " isn't corrent.");
				return false;
			}
			if (!node0.getNodeName().equals(VFSMDef.TAG_VFSM)) {
				System.out.println(filepath + " isn't VFSM file.");
				return false;
			}

			// �|���� ROOT Superstate
			org.w3c.dom.Node root_ss = checkRoot(node0);
			if (root_ss == null)
				return false;
			build_vfsm(root_ss);

		} catch (Exception pce) {
			// System.err.println("VFSMXmlReader error: " + pce);
			pce.printStackTrace();
		}

		return (m_XmlDoc != null);
	}

	private void build_vfsm(org.w3c.dom.Node root_ss) {
		m_VFSMRoot.setName("ROOT");

		// root �U�N�|���@�� Main SuperStaet �Τ@�� FSM SuperState
		readMainPart(root_ss); // for main part
		readDeclarationPart(root_ss); // for declaration part

		// ��Ҧ�State���Ū������A�N�o�n���s�إ�Proxy��T
		rebuildProxy();
		// ���s�إ�connections��T
		rebuildConnections();
		// �A���s�]�wProxy�A�~��clone�쥿�T��real state���e
		// reSetupProxy();
		// ����~��إ� Proxy ����connection��T
		rebuildProxyConnections();
	}

	// �@��s�u
	private void rebuildConnections() {
		buildConnection(m_ConnectionStore.iterator());
	}

	// proxy �����s�u
	private void rebuildProxyConnections() {
		buildConnection(m_ProxyConnectionStore.iterator());
	}

	private void buildConnection(Iterator<ConnectionTemp> ite) {
		while (ite.hasNext()) {
			ConnectionTemp t = ite.next();
			// ���s�إ߳s�u - �n�ǤJ Main �� Declaration ��� root
			t.build(m_Diagram.getFSMMain().getMainState(), m_Diagram
					.getFSMDeclaration());
		}
	}

	private void rebuildProxy() {
		Iterator<Pair> ite = m_ProxyStateList.iterator();
		while (ite.hasNext()) {
			Pair p = ite.next();
			// ���s�إ߳s�u - �n�ǤJ Main �� Declaration ��� root
			State target = StateReferenceUtil.findTarget(m_Diagram.getFSMMain()
					.getMainState(), m_Diagram.getFSMDeclaration(),
					(String) p.second);
			if (target == null)
				continue;
			// let Proxy reference to the real state
			ProxySuperState ps = (ProxySuperState) p.first;
			AbstractSuperState ts = (AbstractSuperState) target;
			ps.setRealState(ts);
		}
	}

	private void readDeclarationPart(org.w3c.dom.Node root_ss) {
		// Ū�� FSM part �U����T
		Element fsm = getElementByName(root_ss, "FSM");
		if (fsm == null)
			return;

		readForGeneralState(fsm, m_Diagram.getFSMDeclaration());
	}

	private void readMainPart(org.w3c.dom.Node root_ss) {
		// Ū�� main parts �U����T
		Element main = getElementByName(root_ss, "Main");
		if (main == null)
			return;

		readForGeneralState(main, m_Diagram.getFSMMain().getMainState());
	}

	private void readMoreData(Element ele) {
		NodeList nodelist = ele.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
			getTreeNodeFromDOM(nodelist.item(i));
		}
	}

	private void readMoreDataForProxyReal(Element ele) {
		// �H��e proxy ��real state ��root
		State oldState = m_VFSMRoot;
		m_VFSMRoot = m_CurrentProxySuperState.getRealStateInstance();
		readMoreData(ele);
		m_VFSMRoot = oldState;
	}

	private void readMoreDataForProxyExtra(Element ele) {
		// �H��e proxy ��extra state ��root
		State oldState = m_VFSMRoot;
		m_VFSMRoot = m_CurrentProxySuperState.getExtraState();
		readMoreData(ele);
		m_VFSMRoot = oldState;
	}

	/* get the root node from doc */
	private Element getXmlRootNode() {
		return (Element) m_XmlDoc.getDocumentElement();
	}

	private boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	private void getTreeNodeFromDOM(org.w3c.dom.Node node) {
		if (node == null)
			return;
		if (!isElementNode(node))
			return;
		dispatch((org.w3c.dom.Element) node);
	}

	private void dispatch(Element e) {
		if (e.getNodeName().equals(VFSMDef.TYPE_INITIAL))
			processInitial(e);
		if (e.getNodeName().equals(VFSMDef.TYPE_FINAL))
			processFinal(e);
		if (e.getNodeName().equals(VFSMDef.TYPE_STATE))
			processState(e);
		if (e.getNodeName().equals(VFSMDef.TYPE_SUPERSTATE))
			processSuperState(e);
		if (e.getNodeName().equals(VFSMDef.TYPE_ANDSTATE))
			processAndSuperState(e);
		if (e.getNodeName().equals(VFSMDef.TYPE_PROXYSTATE))
			processProxySuperState(e);
		if (e.getNodeName().equals(VFSMDef.TAG_PROXY_REALSTATE))
			readMoreDataForProxyReal(e);
		if (e.getNodeName().equals(VFSMDef.TAG_PROXY_EXTRASTATE))
			readMoreDataForProxyExtra(e);
		// �إ߳�W�@���s�u
		if (e.getNodeName().equals(VFSMDef.TAG_TRANSITION))
			processSingleTransition(e);
		// �B�z�s�u
		if (e.getNodeName().equals(VFSMDef.TAG_TRANSITIONS))
			processTransitions(e);
		// �B�z�s�u�W���ƥ�
		if (e.getNodeName().equals(VFSMDef.TAG_EVENT))
			processTransitionEvent(e);
		if (e.getNodeName().equals(VFSMDef.TAG_BENDPOINT))
			processTransitionBendpoint(e);
	}

	// state �q�����ݩ�
	private void readForGeneralState(Element e, State state) {
		state.setName(e.getAttribute(VFSMDef.TAG_NAME));
		int w = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION_W));
		int h = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION_H));
		state.setDimension(w, h);

		int x = Integer.parseInt(e.getAttribute(VFSMDef.TAG_LOCATION_X));
		int y = Integer.parseInt(e.getAttribute(VFSMDef.TAG_LOCATION_Y));
		state.setLocation(x, y);

		boolean collapse = Boolean.parseBoolean(e
				.getAttribute(VFSMDef.TAG_COLLAPSE));
		state.setCollapsed(collapse);

		// �~��Ū�� transition/child state ��
		readForChildren(e, state);
	}

	// Ū�� children states
	private void readForChildren(Element e, State parent) {
		State oldState = m_VFSMRoot;
		m_VFSMRoot = parent;
		readMoreData(e);
		m_VFSMRoot = oldState;
	}

	private void processInitial(Element e) {
		Initial state = new Initial();
		m_VFSMRoot.addState(state);
		readForGeneralState(e, state);
	}

	private void processFinal(Element e) {
		Final state = new Final();
		m_VFSMRoot.addState(state);
		readForGeneralState(e, state);
	}

	private void processState(Element e) {
		State state = new State();
		m_VFSMRoot.addState(state);
		readForGeneralState(e, state);
	}

	private void processSuperState(Element e) {
		SuperState state = new SuperState();
		m_VFSMRoot.addState(state);
		readForGeneralState(e, state);
	}

	private void processAndSuperState(Element e) {
		AndSuperState state = new AndSuperState();
		m_VFSMRoot.addState(state);
		readForGeneralState(e, state);
	}

	ProxySuperState m_CurrentProxySuperState = null;

	private void processProxySuperState(Element e) {
		ProxySuperState state = new ProxySuperState();
		m_VFSMRoot.addState(state);

		ProxySuperState oldState = m_CurrentProxySuperState;
		m_CurrentProxySuperState = state;
		readForGeneralState(e, state);
		m_CurrentProxySuperState = oldState;

		// ���ȮɰO�U ProxyState�ΰѦҪ� target path
		Pair p = new Pair(state, e.getAttribute(VFSMDef.TAG_TARGET));
		m_ProxyStateList.add(p);
	}

	// ////////////////////////////////////////////////////////////////
	// �B�z�s�uConnection
	private void processTransitions(Element e) {
		readMoreData(e);
	}

	private void processSingleTransition(Element e) {
		// �C�@�� Element ���O�@�� Transition
		ConnectionTemp ct = new ConnectionTemp(m_VFSMRoot);
		ct.setAction(e.getAttribute(VFSMDef.TAG_ACTION));
		ct.setCondition(e.getAttribute(VFSMDef.TAG_CONDITION));
		ct.setTarget(e.getAttribute(VFSMDef.TAG_TARGET));

		storeConnectionTemp(ct);

		m_CurrentConnection = ct;
		// �B�z event list
		readMoreData(e);
	}

	private void storeConnectionTemp(ConnectionTemp ct) {
		// if (m_VFSMRoot.getName().equals("RealState"))
		// m_ProxyConnectionStore.add(ct);
		// else if (m_VFSMRoot.getName().equals("ExtraState"))
		// m_ProxyConnectionStore.add(ct);
		// else
		// if (m_VFSMRoot.getParent() == null)
		// m_ConnectionStore.add(ct);
		// if (m_VFSMRoot.getParent().getName().equals("RealState"))
		// m_ProxyConnectionStore.add(ct);
		if (m_CurrentProxySuperState != null) // proxy ���s�u�t�~�O��
			m_ProxyConnectionStore.add(ct);
		else
			m_ConnectionStore.add(ct);
	}

	private void processTransitionEvent(Element e) {
		// �C�@�� Element ���O�@�� AbstractNode �ƥ�
		m_CurrentConnection.addEvent(e.getAttribute(VFSMDef.TAG_REFPATH));
	}

	private void processTransitionBendpoint(Element e) {
		// �C�@�� Element ���O�@�� AbstractNode �ƥ�
		int w1 = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION1_W));
		int h1 = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION1_H));
		int w2 = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION2_W));
		int h2 = Integer.parseInt(e.getAttribute(VFSMDef.TAG_DIMENSION2_H));

		m_CurrentConnection.addBendpoint(w1, h1, w2, h2);
	}
}
