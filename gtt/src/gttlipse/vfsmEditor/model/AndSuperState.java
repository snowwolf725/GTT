package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

import org.eclipse.draw2d.geometry.Dimension;


public class AndSuperState extends SuperState {
	public AndSuperState() {
		super();
		setName("AND");
		setDimension(new Dimension(200, 160));
	}

	public String getStateType() {
		return VFSMDef.TYPE_ANDSTATE;
	}

	/* set collapse flag */
	public void setCollapsed(boolean collapsed) {
		m_Collapsed = collapsed;
		setChildrenCollapsed(m_Collapsed);
		fireStructureChange(PROP_COLLAPSED, m_Collapsed);
	}

	public boolean getCollapsed() {
		return m_Collapsed;
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	public void addState(State st) {
		// AndSuperState 本身不能再加 AndSuperState
		if (st instanceof AndSuperState) {
			if (getParent() != null) {
				getParent().addState(st);
				return;
			}
		}
		super.addState(st);
	}

	public AndSuperState clone() {
		AndSuperState s = new AndSuperState();
		defaultCopy(s);
		cloneChildren(this, s);
		ConnectionBase.cloneConnections(this, s);
		return s;
	}

	@Override
	public boolean isMoveable() {
		return true;
	}

	@Override
	public boolean isResizeable() {
		return true;
	}

	public Dimension getDimension() {
		/* if collapse */
		if (getCollapsed())
			return COLLAPSE_SIZE;

		/* if not collapse */
		updateDimension();
		return m_Size;
	}

	private void updateDimension() {
		if (getCollapsed())
			return; // 褶起來就不用算了

		Dimension d = PlaneLayout.calDimension(getAll());
		setDimension(d.width, d.height);
	}
}
