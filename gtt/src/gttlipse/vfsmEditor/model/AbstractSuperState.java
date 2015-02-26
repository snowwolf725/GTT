package gttlipse.vfsmEditor.model;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

public abstract class AbstractSuperState extends State {

	final protected Dimension COLLAPSE_SIZE = new Dimension(80, 20);
	public static String PROP_STRUCTURE = "NODE";
	public static String PROP_COLLAPSED = "COLLAPSED";
	protected boolean m_Collapsed = true;
//	protected boolean canCollapse = true;
	protected boolean moveable = true;

	/* add Node with index */
	public abstract void addState(int index, State state);

	/* this method can add a AND composite with its sub and sub states */
	// public abstract void addAndComposite(AndSuperState and);
	/* remove Node */
	public abstract void removeState(State node);

	/* set compositeNode */
	public abstract void setAllState(List<State> states);

	/* clear compositeNode */
	public abstract void clear();

	/* set canCollapse flag to false */
//	public abstract void noCollapsed();

	/* move up child */
	public abstract boolean moveChildrenFront(State child);

	/* move down child */
	public abstract boolean moveChildrenRear(State child);

	/* get all of AndSuperState from compositeNode */
	public abstract List<AbstractSuperState> getAndList();

	/* get all of SuperState from compositeNode */
	public abstract List<AbstractSuperState> getSSList();

	/* clone */
	public abstract AbstractSuperState clone();
	
	public AbstractSuperState cloneWithoutConnection() {
		return null;
	}

	@Override
	public boolean isMoveable() {
		return false;
	}

	public boolean isResizeable() {
		return getCollapsed();
	}

	/* recomputed AndSuperState layout */
	public void refreshLayout() {
		// if (isCollapsed())
		// return; // 褶起來，就不用重新算layout
		// if (getAll() == null)
		// return;

		PlaneLayout layout = new PlaneLayout();
		layout.layoutAndState(getAndList(), getDimension());
		// calDimension();

		fireStructureChange(PROP_STRUCTURE, getAll());
	}

	final public void checkStateName(State newState) {
		// 檢查 State Name 是否有重複
		// 若重複則要更新名字
		String basicName = newState.getName();
		String newName = basicName;
		int ct = 1;
		while (getChildrenByName(newName) != null) {
			newName = basicName + "" + ct;
			ct++;
		}
		newState.setName(newName);
	}

	public State getChildrenByName(String name) {
		List<State> list = getAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getName().equals(name))
				return list.get(i);

		return null;
	}

	public boolean checkChildrenByType(String type) {
		return getChildrenByType(type) != null;
	}

	public State getChildrenByType(String type) {
		List<State> list = getAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getStateType().equals(type))
				return list.get(i);
		return null;
	}
}