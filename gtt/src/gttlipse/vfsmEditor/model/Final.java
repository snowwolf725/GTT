package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

import org.eclipse.draw2d.geometry.Dimension;


public class Final extends State {
	public Final() {
		setName("Final");
		setDimension(new Dimension(15, 15));
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	public String getStateType() {
		return VFSMDef.PROP_FINAL;
	}

	@Override
	public boolean isResizeable() {
		return false;
	}

	public Final clone() {
		Final s = new Final();
		defaultCopy(s);
		return s;
	}
}
