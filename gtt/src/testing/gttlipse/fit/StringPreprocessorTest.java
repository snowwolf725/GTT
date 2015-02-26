package testing.gttlipse.fit;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import junit.framework.TestCase;
import gttlipse.fit.node.processor.StringPreprocessor;

public class StringPreprocessorTest extends TestCase {
	StringPreprocessor m_processor;
	Arguments m_argumentList;
	protected void setUp() throws Exception {
		super.setUp();

		m_processor = new StringPreprocessor();
		m_argumentList = new Arguments();
		m_argumentList.add(Argument.create("String", "X", "1"));
		m_argumentList.add(Argument.create("String", "XX", "2"));
		m_processor.setArguments(m_argumentList);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testProcess() {
		assertTrue(m_processor.process("abc#X#").compareTo("abc1") == 0);
		assertTrue(m_processor.process("abc#XX#").compareTo("abc2") == 0);
	}
}
