package testing.eventmodel;

import gtt.eventmodel.Assertion;
import gtt.oracle.SwingChecker;

import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import junit.framework.TestCase;

public class SwingAsserterTest extends TestCase {

	SwingChecker m_Asserter;

	protected void setUp() throws Exception {
		super.setUp();
		m_Asserter = new SwingChecker();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_Asserter = null;
	}

	private Method getMethodWithoutArgument(JComponent c, String name) {
		try {
			Class<?> cc = c.getClass();
			return cc.getMethod(name, new Class[] {});
		} catch (Exception e) {
			// doesn't find any method
		}
		return null;
	}

	public void testJTextField() {
		JTextField c = new JTextField("444.");
		Assertion a = new Assertion();

		a.setMethod(getMethodWithoutArgument(c, "getText"));
		a.setMessage("It should be 444.");

		a.setValue("abcd.");
		assertFalse(m_Asserter.checkAcutalObject(c, a));
		a.setValue("444.");
		assertTrue(m_Asserter.checkAcutalObject(c, a));
	}

	public void testJButton() {
		JButton c = new JButton("ok");
		Assertion a = new Assertion();

		a.setMethod(getMethodWithoutArgument(new JButton("ok"), "getText"));
		a.setMessage("It should be ok.");

		a.setValue("abcd.");
		assertFalse(m_Asserter.checkAcutalObject(c, a));
		a.setValue("ok");
		assertTrue(m_Asserter.checkAcutalObject(c, a));
	}

	public void testAssertion() {
		JTextField c = new JTextField("123.");
		Assertion a = new Assertion();
		a.setMethod(getMethodWithoutArgument(c, "getText"));
		a.setValue(null);
		assertTrue(m_Asserter.checkAcutalObject(c, a));
		a.setValue("");
		assertFalse(m_Asserter.checkAcutalObject(c, a));
		a.setValue("123.");
		assertTrue(m_Asserter.checkAcutalObject(c, a));
	}
}
