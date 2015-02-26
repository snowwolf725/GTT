package testing.testscript;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.testscript.ViewAssertNode;
import gtt.util.refelection.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

public class ViewAssertNodeTest extends TestCase {

	ViewAssertNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new ViewAssertNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	class MyMockVisitor extends MockVisitor {

		boolean m_hasVisit = false;

		public boolean hasVisit() {
			return m_hasVisit;
		}

		public void visit(ViewAssertNode node) {
			m_hasVisit = true;
		}

	}

	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}

	public void testIsContainer() {
		assertFalse(node.isContainer());
	}

	public void testSetter() {
		Class<?> c = node.getClass();
		assertEquals(c.getName(), "gtt.testscript.ViewAssertNode");

		Method[] mm = c.getDeclaredMethods();
		Assertion a = new Assertion();
		a.setMethod(mm[0]);
		assertEquals(a.getMethod(), mm[0]);
		assertFalse(a.getMethod().equals(mm[1]));

		a.setMessage("message");
		assertEquals(a.getMessage(), "message");
		assertFalse(a.getMessage().equals("abcdef"));
		assertFalse(a.getMessage().equals("abcdef"));

		assertTrue(node.getAssertion() == null);
		node.setAssertion(a);
		assertEquals(node.getAssertion(), a);

	}

	public void testSetComponent() {
		assertTrue(node.getComponent() == null);
		IComponent com = new myComponent();
		node.setComponent(com);
		assertTrue(node.getComponent() == com);
	}

	public void testCreation() {
		IComponent com = new myComponent();
		ViewAssertNode va = new ViewAssertNode(com);
		assertTrue(va.getComponent() == com);
	}

	public void testToString() {
		IComponent com = new myComponent();
		node.setComponent(com);
		assertEquals(node.toString(), "test");

		// 含assertion
		Assertion a = new Assertion();
		node.setAssertion(a);

		assertEquals(node.toString(), "test" + "." + a.toString());
	}

	public void testToSimpleString() {
		IComponent com = new myComponent();
		node.setComponent(com);
		assertEquals(node.toSimpleString(), "test");

		// 含assertion
		Assertion a = new Assertion();
		node.setAssertion(a);

		assertEquals(node.toSimpleString(), "test" + "." + a.toString());
	}

	public void testToDetailString() {
		IComponent com = new myComponent();
		node.setComponent(com);
		assertEquals(node.toDetailString(), "test");

		// 含assertion
		Assertion a = new Assertion();
		node.setAssertion(a);

		assertEquals(node.toDetailString(), "test" + "." + a.toString());
	}

	public void testClone() {
		IComponent com = new myComponent();
		node.setComponent(com);
		Assertion a = new Assertion();
		node.setAssertion(a);

		ViewAssertNode va = node.clone();
		assertEquals(node.getComponent(), va.getComponent());
		assertEquals(node.getAssertion(), va.getAssertion());
	}

	class myComponent implements IComponent {

		public IComponent clone() {
			return this;
		}

		public int getIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getIndexOfSameName() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return "test";
		}

		public String getText() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getType() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getWinType() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setIndex(int idx) {
			// TODO Auto-generated method stub

		}

		public void setIndexOfSameName(int idx) {
			// TODO Auto-generated method stub

		}

		public void setName(String name) {
			// TODO Auto-generated method stub

		}

		public void setText(String text) {
			// TODO Auto-generated method stub

		}

		public void setTitle(String title) {
			// TODO Auto-generated method stub

		}

		public void setType(String type) {
			// TODO Auto-generated method stub

		}

		public void setWinType(String type) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean match(Object obj) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public void testViewAssertClone() {
		Class<?> c = node.getClass();
		assertEquals(c.getName(), "gtt.testscript.ViewAssertNode");

		Method[] mm = c.getDeclaredMethods();
		Assertion a = new Assertion();
		a.setMethod(mm[0]);
		assertEquals(a.getMethod(), mm[0]);
		assertFalse(a.getMethod().equals(mm[1]));

		a.setMessage("message");
		assertEquals(a.getMessage(), "message");
		assertFalse(a.getMessage().equals("abcdef"));
		assertFalse(a.getMessage().equals("abcdef"));

		Assertion b = a.clone();

		assertEquals(a.getMethod(), b.getMethod());
		assertSame(a.getMethod(), b.getMethod());
		assertEquals(a.getMessage(), b.getMessage());
		assertEquals(a.getValue(), b.getValue());
		assertEquals(a.getArguments(), b.getArguments());

	}

	public void testAssertionMethod() {
		Class<?> c = node.getClass();
		assertEquals(c.getName(), "gtt.testscript.ViewAssertNode");

		List<Method> mm = ReflectionUtil.getMethodsStartsWith(c, "setComponent");
		assertEquals(mm.size(), 1);

		Method m = (Method) mm.get(0);
		assertEquals(m.getName(), "setComponent");
		String mname = ReflectionUtil.getStringOfMethod(m);
		int argStartIdx = mname.indexOf(m.getName()) + m.getName().length();
		int argEndIdx = mname.indexOf(":"); // 去掉return type

		StringTokenizer st = new StringTokenizer(mname.substring(argStartIdx,
				argEndIdx), ",( )");
		assertTrue(st.hasMoreTokens()); // 至少要有一個 IComponent token
		assertEquals(st.nextToken(), "IComponent");
		assertFalse(st.hasMoreTokens()); // 用完了
	}

}
