package gttlipse.fit.node.processor;

import gtt.eventmodel.IComponent;
import gtt.oracle.AssertionChecker;
import gttlipse.fit.node.FitAssertionNode;

public class FitAssertionNodeProcessor extends EventTrigger {

	AssertionChecker m_iAssert;
	FitAssertionNode m_assertionNode;
	ComponentNamePool m_componentNamePool;
	String[] m_componentNameList;

	public FitAssertionNodeProcessor(AssertionChecker asserter, ComponentNamePool pool) {
		m_iAssert = asserter;
		m_componentNamePool = pool;
	}

	public boolean process(FitAssertionNode node) {
		if(node == null)
			return false;
		m_assertionNode = node;
		String key = getNodeNameByPath(node.getGenerationKey());
		m_componentNameList = m_componentNamePool.getComponentNameList(key);
		return trigger();
	}

	@Override
	protected boolean trigger() {
		boolean result = true;
		StringPreprocessor preprocessor = new StringPreprocessor();
		String assertData = m_componentNameList[0];
		preprocessor.processEscapeCharacter(assertData);
		
		String[] assertDataGroup = assertData.split(",");
		preprocessor.processEscapeCharacter(assertDataGroup);

		for(int i = 0; i < m_componentNameList.length; i++) {
			IComponent ic = createComponent(m_assertionNode.getComponentType(), m_assertionNode.getAssertionDataVariable(), m_assertionNode.getWindowTitle(), m_assertionNode.getWindowType());
			if(assertDataGroup.length == 1) {
				m_assertionNode.getAssertion().setValue(assertDataGroup[0]);
				System.out.println("FitAssetionNodeProcessor(tempValue):" + assertDataGroup[0]);
			}
			else {
				m_assertionNode.getAssertion().setValue(assertDataGroup[i]);
				System.out.println("FitAssetionNodeProcessor(assertData):" + assertDataGroup[i]);
			}
			//if(m_iAssert.check(ic, m_assertionNode.getAssertion()) == true) {
			m_assertionNode.getAssertion().setValue(assertData);
			if(m_iAssert.check(ic, m_assertionNode.getAssertion()) == true) {
				System.out.println(m_componentNameList[i] + ":Pass");
				result = result && true;
			}
			else {
				System.err.println(m_componentNameList[i] + ":false");
				result = result && false;
			}
		}
		return result;
	}

	private String getNodeNameByPath(String path) {
		int begin = path.lastIndexOf("::");
		if(begin == -1)
			return path;
		String temp = path.substring(begin + 2);
		return temp;
	}
}
