package gttlipse.fit.node;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class SplitDataAsNameNode extends AbstractMacroNode {
	String m_data;
	String m_variable;

	public SplitDataAsNameNode() {
		this("", "", "");
	}

	private SplitDataAsNameNode(String name, String data, String variable) {
		setName(name);
		m_data = data;
		m_variable = variable;
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		if(v instanceof IMacroFitVisitor)
			accept(((IMacroFitVisitor)v));
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public AbstractMacroNode clone() {
		return new SplitDataAsNameNode(getName(), m_data, m_variable);
	}

	public void setVariable(String v) {
		m_variable = v;
	}

	public String getVariable() {
		return m_variable;
	}

	public void setData(String data) {
		m_data = data;
	}

	public String getData() {
		return m_data;
	}

	public String toString() {
		return getName() + "(" + m_variable + ")";
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_SPLITEDATA_ASNAME_NODE;
	}
}
