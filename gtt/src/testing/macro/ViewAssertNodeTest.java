package testing.macro;

import gtt.macro.macroStructure.ViewAssertNode;
import junit.framework.TestCase;

public class ViewAssertNodeTest extends TestCase {

	ViewAssertNode node;

	String name = "result";
//	String type = "java.swing.JTextField";

	protected void setUp() throws Exception {
		super.setUp();
		node = new ViewAssertNode(name);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testCreate() {
		assertEquals(node.getArguments().types().size(), 0);
		assertEquals(node.getPath().list().size(), 1);
		assertEquals(node.getAssertion().getMethodName(), "");
		assertEquals(node.getAssertion().getMessage(), "");
		assertEquals(node.getAssertion().getValue(), "");
	}

	public void testCreate2() {
		ViewAssertNode node2 = new ViewAssertNode();
		assertEquals(node2.getArguments().types().size(), 0);
		assertEquals(node2.getPath().list().size(), 1);
		assertEquals(node2.getAssertion().getMethodName(), "");
		assertEquals(node2.getAssertion().getMessage(), "");
		assertEquals(node2.getAssertion().getValue(), "");
//		assertFalse(node2.isLocalComponentPath());
	}

	public void testClone() {
		assertEquals(node.getArguments().types().size(), 0);
		assertEquals(node.getPath().list().size(), 1);
		assertEquals(node.getAssertion().getMethodName(), "");
		assertEquals(node.getAssertion().getMessage(), "");
		assertEquals(node.getAssertion().getValue(), "");

		// 要考慮到macro裡的元件
//		MacroComponent mc = new MacroComponent("root");
		
		ViewAssertNode node2 = node.clone();
		assertEquals(node2.getArguments().types().size(), node.getArguments().types()
				.size());
		assertEquals(node2.getPath().list().size(), node.getPath()
				.list().size());
		assertEquals(node2.getAssertion().getMethodName(), node.getAssertion().getMethodName());
		assertEquals(node2.getAssertion().getMessage(), node.getAssertion().getMessage());
		assertEquals(node2.getAssertion().getValue(), node.getAssertion().getValue());
	}

	public void testCopyCreate() {
		assertEquals(node.getArguments().types().size(), 0);
		assertEquals(node.getPath().list().size(), 1);
		assertEquals(node.getAssertion().getMethodName(), "");
		assertEquals(node.getAssertion().getMessage(), "");
		assertEquals(node.getAssertion().getValue(), "");

		ViewAssertNode node2 = node.clone();
		assertEquals(node2.getArguments().types().size(), node.getArguments().types()
				.size());
		assertEquals(node2.getPath().list().size(), node.getPath()
				.list().size());
		assertEquals(node2.getAssertion().getMethodName(), node.getAssertion().getMethodName());
		assertEquals(node2.getAssertion().getMessage(), node.getAssertion().getMessage());
		assertEquals(node2.getAssertion().getValue(), node.getAssertion().getValue());
	}

	public void testSetter() {
		assertEquals(node.getAssertion().getMethodName(), "");
		assertEquals(node.getAssertion().getMessage(), "");
		assertEquals(node.getAssertion().getValue(), "");

		node.getAssertion().setMessage("msg");
		node.getAssertion().setMethodName("method");
		node.getAssertion().setValue("value");
		assertEquals(node.getAssertion().getMethodName(), "method");
		assertEquals(node.getAssertion().getMessage(), "msg");
		assertEquals(node.getAssertion().getValue(), "value");
	}

//	public void testGetComponentType() {
//		assertEquals(node.getReference().getType(), type);
//	}
//
//	public void testGetComponentName() {
//		assertEquals(node.getReference().getName(), name);
//	}


}
