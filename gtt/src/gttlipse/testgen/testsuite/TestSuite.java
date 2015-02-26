package gttlipse.testgen.testsuite;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;

import java.util.List;
import java.util.Vector;

public class TestSuite {
	private List<Object[]> m_tests = new Vector<Object[]>();

	public TestSuite() {
	}

	public void addTestCase(List<Object> testcase) {
		m_tests.add(testcase.toArray());
	}

	public Object[] getTestCase(int index) {
		return m_tests.get(index);
	}

	public int size() {
		return m_tests.size();
	}

	int cal(MacroEventCallerNode node) {
//		if(node.getReference()==null)
//			return 0;
//		System.out.println("cal MEC " + node.getReferencePathString());
		return cal((MacroEventNode) node.getReference());
	}

	int cal(MacroEventNode node) {
//		System.out.println("cal " + node.getName());
		int r = 0;
		for(int i=0;i<node.size();i++) {
			AbstractMacroNode n = node.get(i);
			if(n instanceof MacroEventCallerNode)
				r += cal((MacroEventCallerNode)n);
			else 
				r++; // ¨Æ¥ó+1
		}

		return r;
	}

	int calSize(AbstractMacroNode node) {
		if (node instanceof MacroEventCallerNode)
			return cal((MacroEventCallerNode) node);
		if (node instanceof MacroEventNode)
			return cal((MacroEventNode) node);

		return 0;
	}
	
	public int getTotalSize() {
		int i = 0, total = 0;
		for (i = 0; i < m_tests.size(); i++) {
			Object[] t = m_tests.get(i);
			for (int j = 0; j < t.length; j++) {
				total += calSize((AbstractMacroNode) t[j]);
			}

		}

		return total;
	}

	public int getAverageLength() {
		return getTotalSize() / m_tests.size();
	}
}
