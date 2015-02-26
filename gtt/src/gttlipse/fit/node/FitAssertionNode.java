package gttlipse.fit.node;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class FitAssertionNode extends AbstractMacroNode implements IHaveArgument {
	Assertion m_assertion;
	String m_generationKey;
	String m_componentType;
	String m_windowTitle;
	String m_windowType;
	String m_assertionData;
	String m_assertionDataVariable;
	Arguments m_argmentList;

	public FitAssertionNode(){
		m_componentType = "";
		m_generationKey = "";
		m_windowType = "";
		m_windowTitle = "";
		m_assertionData = "";
		m_assertionDataVariable = "";
		m_argmentList = new Arguments();
		m_assertion = new Assertion();
	}

	public FitAssertionNode(String dataVariable, String componentType, String generationKey, String windowType, String windowTitle, Assertion assertion){
		m_assertionDataVariable = dataVariable;
		m_componentType = componentType;
		m_generationKey = generationKey;
		m_windowType = windowType;
		m_windowTitle = windowTitle;
		m_assertion = assertion;
		m_argmentList = new Arguments();
	}

	public Assertion getAssertion() {
		return m_assertion;
	}

	public void setAssertion(Assertion assertion) {
		m_assertion = assertion;
	}

	public void setAssertionData(String data){
		m_assertionData = data;
	}
	
	public String getAssertionData(){
		return m_assertionData;
	}

	public void setAssertionDataVariable(String data){
		m_assertionDataVariable = data;
	}

	public String getAssertionDataVariable(){
		return m_assertionDataVariable;
	}

	public void setGenerationKey(String key){
		m_generationKey = key;
	}
	
	public String getGenerationKey(){
		return m_generationKey;
	}

	public void setWindowTitle(String title) {
		m_windowTitle = title;
	}

	public String getWindowTitle() {
		return m_windowTitle;
	}

	public void setWindowType(String type) {
		m_windowType = type;
	}

	public String getWindowType() {
		return m_windowType;
	}

	public void setComponentType(String componentType){
		m_componentType = componentType;
	}
	
	public String getComponentType(){
		return m_componentType;
	}

	public Arguments getArguments(){
		return m_argmentList;
	}

	public void setArguments(Arguments list){
		m_argmentList = list;
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
		return new FitAssertionNode(m_assertionDataVariable, m_componentType,m_generationKey, m_windowType, m_windowTitle, m_assertion.clone());
	}
	
	public String toString() {
		return "Assert(" + m_assertionDataVariable + ")";
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_FIT_ASSERTION_NODE;
	}

}
