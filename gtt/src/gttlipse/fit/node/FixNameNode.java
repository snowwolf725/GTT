package gttlipse.fit.node;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class FixNameNode extends AbstractMacroNode {
	String m_componentName;

	FixNameNode(String name, String componentName) {
		setName(name);
		m_componentName = componentName;
	}

	public FixNameNode() {
		setName("");
		m_componentName = "";
	}

	public void setComponentName(String name) {
		m_componentName = name;
	}

	public String getComponentName() {
		return m_componentName;
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		if(v instanceof IMacroFitVisitor)
			accept(((IMacroFitVisitor)v));
	}

	@Override
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	@Override
	public AbstractMacroNode clone() {
		return new FixNameNode(getName(), m_componentName);
	}
	
	public String toString() {
		return getName() + "(" + m_componentName + ")";
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_FIXNAME_NODE;
	}
}
