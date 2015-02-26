package testing.testassert;

import gtt.oracle.AssertMethod;

import java.lang.reflect.Method;
import java.util.Iterator;

import junit.framework.TestCase;

public class AssertMethodTest extends TestCase {

	AssertMethod m_assert;

	protected void setUp() throws Exception {
		super.setUp();
		m_assert = new AssertMethod();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_assert = null;
	}

	private boolean checkIteratorSize(Iterator<Method> ite, int size) {
		int ct = 0;
		while(ite.hasNext()) {
			ite.next();
			ct++;
		}
		return (ct==size);

	}
	public void testGetIterator() {
		m_assert.loadingMethods(test1.class);
		assertNotNull(m_assert.iteratorOfGetMethod());
		checkIteratorSize( m_assert.iteratorOfGetMethod(), 3);
		assertNotNull(m_assert.iteratorOfIsMethod());
		checkIteratorSize( m_assert.iteratorOfIsMethod(), 2);
		assertNotNull(m_assert.iteratorOfOtherMethod());
		checkIteratorSize( m_assert.iteratorOfOtherMethod(), 4);
	}

	class test1 {
		public void getMethod1() {};
		public void getMethod2() {};
		public void getMethod3() {};
		public void isMethod1() {};
		public void isMethod2() {};
		public void otherMethod1() {};
		public void otherMethod2() {};
		public void otherMethod3() {};
		public void otherMethod4() {};
	}

}
