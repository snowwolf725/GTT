package gttlipse.fit.node;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class GenerateOrderNameNode extends AbstractMacroNode implements IHaveArgument {
	String m_start;
	String m_end;
	String m_prefix;
	String m_suffix;
	String m_variableNameOfStart;
	String m_variableNameOfEnd;
	Arguments m_argumentList;
	
	public GenerateOrderNameNode() {
		setName("");
		m_prefix = "";
		m_suffix = "";
		m_variableNameOfStart = "";
		m_variableNameOfEnd = "";
		m_argumentList = new Arguments();
	}

	private GenerateOrderNameNode(String name,String prefix, String suffix, String start, String end, Arguments list) {
		setName(name);
		m_prefix = prefix;
		m_suffix = suffix;
		m_variableNameOfStart = start;
		m_variableNameOfEnd = end;
		m_argumentList = list;
		
	}

	public void setVariableNameOfStart(String name) {
		m_variableNameOfStart = name;
	}

	public String getVariableNameOfStart() {
		return m_variableNameOfStart;
	}

	public void setVariableNameOfEnd(String name) {
		m_variableNameOfEnd = name;
	}

	public String getVariableNameOfEnd() {
		return m_variableNameOfEnd;
	}

	public void setStart(String start) {
		m_start = start;
	}

	public String getStart() {
		return m_start;
	}

	public void setEnd(String end) {
		m_end = end;
	}

	public String getEnd() {
		return m_end;
	}

	public void setPrefix(String prefix) {
		m_prefix = prefix;
	}

	public String getPrefix() {
		return m_prefix;
	}

	public void setSuffix(String suffix) {
		m_suffix = suffix;
	}

	public String getSuffix() {
		return m_suffix;
	}

	public String toString() {
		return getName() + "(" + m_prefix + m_variableNameOfStart + " to " + m_variableNameOfEnd + m_suffix + ")";
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
		return new GenerateOrderNameNode(getName(),m_prefix, m_suffix, m_variableNameOfStart, m_variableNameOfEnd, m_argumentList.clone());
	}

	@Override
	public Arguments getArguments() {
		return m_argumentList;
	}

	@Override
	public void setArguments(Arguments list) {
		m_argumentList = list;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_GENERATE_ORDERNAME_NODE;
	}

}
