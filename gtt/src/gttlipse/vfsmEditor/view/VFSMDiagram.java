package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.io.VFSMXmlReader;
import gttlipse.vfsmEditor.io.VFSMXmlSave;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

import org.eclipse.draw2d.geometry.Dimension;


public class VFSMDiagram implements IVFSMDagram {

	// invisible root
	private AbstractSuperState m_FSMRoot;

	public AbstractSuperState getFSMRoot() {
		return m_FSMRoot;
	}

	// main part
	private Diagram m_FSMMain;

	public Diagram getFSMMain() {
		return m_FSMMain;
	}

	// declaration part
	private AbstractSuperState m_FSMDeclaration;

	public AbstractSuperState getFSMDeclaration() {
		return m_FSMDeclaration;
	}

	private int m_ChildCount = 0;

	public String addDeclarationChild(String name) {
		AbstractSuperState child = createDeclarationChild(name);
		return addDeclarationChild(child);
	}

	public String addDeclarationChild(State state) {
		state.setDeclaration(true);
		m_FSMDeclaration.addState(state);
		m_ChildCount++; // count of children
		// 加入 state 有可能會變成新的name
		return state.getName();
	}

	// factory for createDefaultSuperState
	private AbstractSuperState createDeclarationChild(String name) {
		AbstractSuperState state = new SuperState();
		state.setName(name);
		state.setDimension(new Dimension(800, 800));
		state.setCollapsed(false);
		state.setDeclaration(true);
		return state;
	}

	public VFSMDiagram() {
		initialize();
	}

	private void initialize() {
		// main 區
		m_FSMMain = new Diagram();
//		m_FSMMain.getMainState().noCollapsed();
		m_FSMMain.getMainState().setCollapsed(false);

		// declaration 區
		m_FSMDeclaration = new SuperState();
		m_FSMDeclaration.setName(VFSMDef.FSM_FSM);

		// 看不見的 FSM Root
		m_FSMRoot = new SuperState();
		m_FSMRoot.setName("ROOT");
		// 加入 Main Diagram
		m_FSMRoot.addState(m_FSMMain.getMainState());
		// 加入 declaration
		m_FSMRoot.addState(m_FSMDeclaration);
		m_FSMRoot.setName("ROOT");
	}

	@Override
	public void read(String filename) {
		VFSMXmlReader reader = new VFSMXmlReader();
		reader.read(filename);

		VFSMDiagram newDiagram = reader.getVFSMDiagram();
		m_FSMRoot = newDiagram.m_FSMRoot;
		m_FSMMain = newDiagram.m_FSMMain;
		m_FSMDeclaration = newDiagram.m_FSMDeclaration;
	}

	@Override
	public void save(String filename) {
		// 利用 Saver 存檔
		VFSMXmlSave xmlSave = new VFSMXmlSave();
		xmlSave.doSave(m_FSMRoot, filename);
	}

	@Override
	public void removeDeclarationChild(State node) {
		// 移掉 declaration 中的 node
		m_FSMDeclaration.removeState(node);

		// Main 中也要 移掉該 declaration 中的 node
		m_FSMMain.removeNode(node);
	}

	@Override
	public Diagram createDiagram() {
		State st = m_FSMMain.getMainState();
		st.setDimension(800, 800);
		st.setLocation(0, 0);
		return createDiagram(st);
	}

	@Override
	public Diagram createDiagram(String name) {
		State child = findDeclarationChild(name);
		if (child == null)
			return new Diagram();
		child.setLocation(0, 0);

		return createDiagram(child);
	}

	private State findDeclarationChild(String name) {
		return m_FSMDeclaration.getChildrenByName(name);
	}

	private Diagram createDiagram(State node) {
		Diagram newDiagram = new Diagram();
		newDiagram.getMainState().addState(node);
		return newDiagram;
	}
}
