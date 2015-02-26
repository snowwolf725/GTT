package gttlipse.vfsmEditor.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

public class ProxySuperState extends AbstractSuperState {
	// the reference statechart
	private AbstractSuperState m_realState;
	// the instance of reference statechart
	private AbstractSuperState m_realStateInstance = new SuperState();
	// extra states
	private AbstractSuperState m_extraState = new SuperState();

	public AbstractSuperState getRealState() {
		return m_realState;
	}

	public AbstractSuperState getRealStateInstance() {
		return m_realStateInstance;
	}

	// 參照到另一個 super state
	public void setRealState(AbstractSuperState rs) {
		m_realState = rs;
		// 先只 clone states 就行
		if (m_realStateInstance.size() == 0)
			m_realStateInstance = m_realState.clone();
		else {
			// 已有任何children ，則應該做sync
			sync();
		}
		m_realStateInstance.setParent(this);
		// 包括更新 dimension
		updateDimension();
	}

	private void updateDimension() {
		if (getCollapsed())
			return; // 褶起來就不用算了
		
		List<State> newList = new LinkedList<State>();
		newList.addAll(m_realStateInstance.getAll());
		newList.addAll(m_extraState.getAll());
		Dimension d = PlaneLayout.calDimension(newList);

		setDimension(d.width, d.height);
	}

	
	public AbstractSuperState getExtraState() {
		return m_extraState;
	}

	public ProxySuperState(AbstractSuperState ss) {
		m_realState = ss;
		m_realStateInstance = m_realState.clone();
		m_realStateInstance.setParent(this);
		setCollapsed(false);
		setName("SS");
		// 包括更新 dimension
		updateDimension();
	}

	public ProxySuperState() {
		m_realState = new SuperState();
		m_realStateInstance = new SuperState();

		m_realStateInstance.setParent(this);
		m_realStateInstance.setName("RealState");

		m_extraState.setParent(this);
		m_extraState.setName("ExtraState");

		setName("SS");
		setCollapsed(false);
	}

	private void sync() {
		// 先同步 realStateInstance 跟 realState的內容
		SyncUtil.sync(m_realStateInstance, m_realState);
	}


	@Override
	public List<State> getAll() {
		sync();
		List<State> newList = new LinkedList<State>();
		newList.addAll(m_realStateInstance.getAll());
		newList.addAll(m_extraState.getAll());
		for (int i = 0; i < newList.size(); i++)
			newList.get(i).setParent(this);
		return newList;
	}

	public Dimension getDimension() {
		/* if collapse */
		if (getCollapsed())
			return COLLAPSE_SIZE;

		/* if not collapse */
		return super.getDimension();
	}

	@Override
	public void addState(int index, State st) {
		checkStateName(st);
		m_extraState.addState(index, st);
		st.setParent(this);

		refreshLayout();
	}

	@Override
	public void addState(State st) {
		addState(m_extraState.size(), st);
	}

	@Override
	public boolean checkChildrenByType(String type) {
		return m_realStateInstance.checkChildrenByType(type);
	}

	@Override
	public void clear() {
		m_extraState.clear();
	}

	public boolean getCollapsed() {
		return m_Collapsed;
	}

	@Override
	public boolean moveChildrenFront(State child) {
		return m_realStateInstance.moveChildrenFront(child);
	}

	@Override
	public boolean moveChildrenRear(State child) {
		return m_realStateInstance.moveChildrenRear(child);
	}

	@Override
	public void removeState(State st) {
		m_extraState.removeState(st);
		st.setParent(null);

		fireStructureChange(PROP_STRUCTURE, st);
	}

	@Override
	public void setCollapsed(boolean collapsed) {
		m_Collapsed = collapsed;
		m_realStateInstance.setCollapsed(collapsed);
		m_extraState.setCollapsed(collapsed);
		
		fireStructureChange(PROP_COLLAPSED, this.m_Collapsed);
		refreshLayout();
	}

	@Override
	public void setAllState(List<State> states) {
		// m_extraState.setAllState(states);
	}

	@Override
	public int size() {
		return m_realStateInstance.size() + m_extraState.size();
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	@Override
	public State getChildrenByName(String name) {
		List<State> nodes = getAll();
		for (int i = 0; i < nodes.size(); i++)
			if (nodes.get(i).getName().equals(name)) {
				nodes.get(i).setParent(this);
				return nodes.get(i);
			}
		return null;

	}

	@Override
	public State getChildrenByType(String type) {
		List<State> nodes = getAll();
		for (int i = 0; i < nodes.size(); i++)
			if (nodes.get(i).getStateType().equals(type))
				return nodes.get(i);
		return null;
	}

	@Override
	public List<AbstractSuperState> getAndList() {
		return m_realStateInstance.getAndList();
	}

	@Override
	public List<AbstractSuperState> getSSList() {
		return m_realStateInstance.getSSList();
	}

	@Override
	public boolean isMoveable() {
		return true;
	}

	@Override
	public boolean isResizeable() {
		return true;
	}

	public ProxySuperState clone() {
		ProxySuperState s = new ProxySuperState();
		defaultCopy(s);

		s.setRealState(m_realState);
		s.m_realStateInstance = m_realStateInstance.clone();
		s.m_extraState = m_extraState.clone();

		return s;
	}
}
