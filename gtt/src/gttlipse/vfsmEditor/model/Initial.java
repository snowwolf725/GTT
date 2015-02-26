package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

import org.eclipse.draw2d.geometry.Dimension;


public class Initial extends State {
	public Initial() {
		setName("Initial");
		setDimension(new Dimension(15, 15));
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	public String getStateType() {
		return VFSMDef.PROP_INITIAL;
	}

	@Override
	public boolean isResizeable() {
		return false;
	}
	
	public Initial clone() {
		Initial s = new Initial();
		defaultCopy(s);
		return s;
	}
}
