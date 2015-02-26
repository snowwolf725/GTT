package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;


public class SuperState extends AbstractSuperState {
	private static final long serialVersionUID = 1L;
	protected List<State> m_Composite = new ArrayList<State>();
	private final static String DEFAULT_NAME = "XOR";

	public SuperState() {
		setName(DEFAULT_NAME);
		setDimension(new Dimension(160, 160));
		setDeclaration(true);
		setCollapsed(false);
	}

	public String getStateType() {
		return VFSMDef.PROP_SUPERSTATE;
	}

	/* return SuperState size */
	public Dimension getDimension() {
		/* if not collapse */
		if (!getCollapsed())
			return super.getDimension();

		/* if collapse */
		return COLLAPSE_SIZE;
	}

	/* return SuperState children's number */
	public int size() {
		return m_Composite.size();
	}

	/* add Node with index */
	public void addState(int index, State newState) {
		checkStateName(newState);

		m_Composite.add(index, newState);

		newState.setParent(this);
		fireStructureChange(PROP_STRUCTURE, newState);
		refreshLayout();
	}

	/* add Node without index */
	public void addState(State newState) {
		if (newState instanceof AndSuperState) {
			// 如果新增的是 AndSuperState，要做不一樣的處理
			if (((AndSuperState) newState).getAll().size() == 0) {
				/* call andAndSuperState method to add node */
				addAndSuperState((AndSuperState) newState);
				return;
			}
		}

		if (newState instanceof Initial || newState instanceof Final) {
			// 不能有一個以上的Inital或Final狀態
			if (checkChildrenByType(newState.getStateType()))
				return;
		}

		addState(size(), newState);
	}

	/* add AndSuperState to compositeNode */
	private void addAndSuperState(AndSuperState state) {
		/* if AND number is zero */
		if (getAndList().size() == 0 && state.size() != 0) {
			addNewAndSuperState();
		} else {
			/* if isn't first AND to add */
			addState(size(), state);
		}
	}

	private void addNewAndSuperState() {
		/* move all node to inner AndSuperState */
		AndSuperState andState1 = new AndSuperState();
		for (int j = 0; j < m_Composite.size(); j++) {
			andState1.addState(m_Composite.get(j));
		}

		/* clear compositeNode */
		m_Composite.clear();

		/* add AndSuperState to compositeNode */
		addState(size(), andState1);

		// /* add another AndSuperState to compositeNode */
		// AndSuperState andState2 = new AndSuperState();
		// andState2.setName(andState1.getName() + 1);
		// addState(size(), andState2);
	}

	/* get all of AndSuperState from compositeNode */
	public List<AbstractSuperState> getAndList() {
		List<AbstractSuperState> list = new ArrayList<AbstractSuperState>();
		for (int i = 0; i < m_Composite.size(); i++) {
			if (m_Composite.get(i) instanceof AndSuperState)
				list.add((AndSuperState) m_Composite.get(i));
		}
		return list;
	}

	/* get all of SuperState from compositeNode */
	public List<AbstractSuperState> getSSList() {
		List<AbstractSuperState> list = new ArrayList<AbstractSuperState>();
		for (int i = 0; i < m_Composite.size(); i++) {
			if (m_Composite.get(i) instanceof SuperState) {
				list.add((SuperState) m_Composite.get(i));
			}
		}
		return list;
	}

	/* remove Node */
	public void removeState(State state) {
		m_Composite.remove(state);
		state.setParent(null);

		// delete all related incoming connection
		for (int i = 0; i < state.getIncomingConnections().size(); i++) {
			state.getIncomingConnections().get(i).removeConnection(
					state.getIncomingConnections().get(i).getSource(), state);
		}
		// delete all related outgoing connection
		for (int j = 0; j < state.getOutgoingConnections().size(); j++) {
			state.getOutgoingConnections().get(j).removeConnection(state,
					state.getOutgoingConnections().get(j).getTarget());
		}

		/* when remove node, check AndList and remove Single AND */
		checkRemoveSingleAndSuperState();

		fireStructureChange(PROP_STRUCTURE, state);
		refreshLayout();
	}

	/* we don't need single AND on SuperState */
	private void checkRemoveSingleAndSuperState() {
		// if AND # equal to one
		if (getAndList().size() != 1)
			return;

		// add all nodes to compositeNode
		AbstractSuperState andState = getAndList().get(0);
		for (int i = 0; i < andState.size(); i++)
			m_Composite.add(andState.getAll().get(i));

		// remove AND
		removeState(andState);
	}

	/* get compositeNode */
	public List<State> getAll() {
		return m_Composite;
	}

	/* set compositeNode */
	public void setAllState(List<State> nodes) {
		m_Composite = nodes;
	}

	/* clear compositeNode */
	public void clear() {
		m_Composite.clear();
	}

	/* get collapse flag */
	public boolean getCollapsed() {
		return false;
	}

	/* set collapse flag */
	public void setCollapsed(boolean collapsed) {
		// m_Collapsed = false;
		// fireStructureChange(PROP_COLLAPSED, this.m_Collapsed);
		setChildrenCollapsed(collapsed);
	}

	protected void setChildrenCollapsed(boolean flag) {
		Iterator<State> ite = m_Composite.iterator();
		while (ite.hasNext())
			ite.next().setCollapsed(flag);
	}

	/* move up child */
	public boolean moveChildrenFront(State child) {
		if (m_Composite.get(0).equals(child))
			return false;

		for (int i = 1; i < m_Composite.size(); i++) {
			if (m_Composite.get(i).equals(child)) {
				State temp = m_Composite.get(i - 1);
				m_Composite.set(i - 1, m_Composite.get(i));
				m_Composite.set(i, temp);
				break;
			}
		}
		return true;
	}

	/* move down child */
	public boolean moveChildrenRear(State child) {
		if (m_Composite.get(0).equals(child))
			return false;

		for (int i = 1; i < m_Composite.size(); i++) {
			if (m_Composite.get(i).equals(child)) {
				State temp = m_Composite.get(i + 1);
				m_Composite.set(i + 1, m_Composite.get(i));
				m_Composite.set(i, temp);
				break;
			}
		}
		return true;
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	@Override
	public AbstractSuperState clone() {
		AbstractSuperState newSS = cloneWithoutConnection();
		ConnectionBase.cloneConnections(this, newSS);
		return newSS;
	}

	public AbstractSuperState cloneWithoutConnection() {
		AbstractSuperState newSS = new SuperState();
		newSS.setCollapsed(m_Collapsed);
		defaultCopy(newSS);
		cloneChildren(this, newSS);
		return newSS;
	}

	protected static void cloneChildren(AbstractSuperState src,
			AbstractSuperState newSS) {
		int maxW = 80;
		int maxH = 20;
		/* recursive create superstate */
		Iterator<State> ite = src.getAll().iterator();
		while (ite.hasNext()) {
			State child = ite.next();
			newSS.addState(child.clone());

			Dimension box = PlaneLayout.getBoundingBox(child);
			if (box.width > maxW)
				maxW = box.width;
			if (box.height > maxH)
				maxH = box.height;
		}
		List<AbstractSuperState> andList = newSS.getAndList();
		for (int i = 0; i < andList.size(); i++) {
			andList.get(i).setDimension(maxW, maxH + PlaneLayout.LABEL_SIZE);
			andList.get(i).setLocation((maxW * i) + PlaneLayout.BOUND_SIZE, 0);
		}

		newSS.setDimension(maxW * (andList.size())
				+ (PlaneLayout.BOUND_SIZE * 2), maxH
				+ (PlaneLayout.BOUND_SIZE * 2) + PlaneLayout.LABEL_SIZE);
	}

}
