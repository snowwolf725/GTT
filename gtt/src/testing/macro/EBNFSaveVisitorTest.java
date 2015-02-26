package testing.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion.CompareOperator;
import gtt.macro.io.EBNFSaveVisitor;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import junit.framework.TestCase;

public class EBNFSaveVisitorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInitialize() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();
		assertNotNull(v.getStringBuffer());
		assertEquals("", v.getStringBuffer().toString());
	}

	public void testSimpleMacroComponent() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");

		mc.accept(v);

		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		assertEquals("Component name {}\n", v.getStringBuffer().toString());
	}

	public void testMultipleMacroComponent() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");
		MacroComponentNode c1 = MacroComponentNode.create("child1");
		MacroComponentNode c2 = MacroComponentNode.create("child2");
		MacroComponentNode c3 = MacroComponentNode.create("child3");
		mc.add(c1);
		c1.add(c2);
		c2.add(c3);

		mc.accept(v);

		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\tComponent child1 {\n");
		bf.append("\t\tComponent child2 {\n");
		bf.append("\t\t\tComponent child3 {}\n");
		bf.append("\t\t}\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testWithMacroEvent() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");
		MacroEventNode me1 = new MacroEventNode("me1");
		mc.add(me1);

		MacroEventNode me2 = new MacroEventNode("me2");
		me2.getArguments().add(Argument.create("type1", "name1"));
		mc.add(me2);

		MacroEventNode me3 = new MacroEventNode("me3");
		me3.getArguments().add(Argument.create("int", "a"));
		me3.getArguments().add(Argument.create("string", "b"));
		mc.add(me3);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me1() {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me2(name1) {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me3(a, b) {}\n");
		bf.append("}\n");

		assertEquals(v.getStringBuffer().toString(), bf.toString());
	}

	public void testWithEventNode() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();
		
		MacroComponentNode mc = MacroComponentNode.create("name");

		// component part 
		ComponentNode c1 = ComponentNode.create("t1", "n1");
		ComponentNode c2 = ComponentNode.create("t2", "n2");
		mc.add(c1);
		mc.add(c2);

		
		// macro event part
		MacroEventNode me = new MacroEventNode("me1");
		mc.add(me);

		
		ComponentEventNode ce = new ComponentEventNode(c1);
		ce.setEvent("push", 0);
		me.add(ce);

		ce = new ComponentEventNode(c2);
		ce.setEvent("select", 0);
		ce.getArguments().add(Argument.create("", "", "v1"));
		ce.getArguments().add(Argument.create("", "", "v2"));
		me.add(ce);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\tWidget n1(\"t1\", \"n1\");\n");
		bf.append("\tWidget n2(\"t2\", \"n2\");\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me1() {\n");
		bf.append("\t\tn1.push();\n");
		bf.append("\t\tn2.select(\"v1\", \"v2\");\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testWithAssertNode() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");
		ComponentNode c1 = ComponentNode.create();
		c1.setName("n1");
		c1.setType("t1");
		mc.add(c1);

		MacroEventNode me = new MacroEventNode("me1");
		mc.add(me);

		ViewAssertNode va = new ViewAssertNode( "n1");
		va.getAssertion().setMethodName("getText");
		// va.getAssert().setMethod(new Method()"getText()");
		va.getAssertion().setCompareOperator(CompareOperator.EqualTo);
		va.getAssertion().setValue("v");
		me.add(va);

		va = new ViewAssertNode( "n1");
		va.getAssertion().setMethodName("get");
		va.getAssertion().setCompareOperator(CompareOperator.EqualTo);
		va.getAssertion().setValue("v");
		va.getArguments().add(Argument.create("", "", "a"));
		va.getArguments().add(Argument.create("", "", "b"));
		me.add(va);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\tWidget n1(\"t1\", \"n1\");\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me1() {\n");
		bf.append("\t\tAssert(n1.getText()==\"v\");\n");
		bf.append("\t\tAssert(n1.get(\"a\", \"b\")==\"v\");\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testWithComponentNode() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");

		ComponentNode c1 = ComponentNode.create();
		c1.setName("button1");
		c1.setType("JButton");
		mc.add(c1);

		c1 = ComponentNode.create();
		c1.setName("n1");
		c1.setType("Type");
		mc.add(c1);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\tWidget button1(\"JButton\", \"button1\");\n");
		bf.append("\tWidget n1(\"Type\", \"n1\");\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testWithDeepComponentNode() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("name");
		MacroComponentNode mc2 = MacroComponentNode.create("mc2");

		mc.add(mc2);

		ComponentNode c1 = ComponentNode.create();
		c1.setName("button1");
		c1.setType("JButton");
		mc2.add(c1);

		c1 = ComponentNode.create();
		c1.setName("n1");
		c1.setType("Type");
		mc2.add(c1);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component name {\n");
		bf.append("\tComponent mc2 {\n");
		bf.append("\t\tWidget button1(\"JButton\", \"button1\");\n");
		bf.append("\t\tWidget n1(\"Type\", \"n1\");\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testMacroEventCaller() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("mc");
		mc.add(new MacroEventNode("me1"));
		mc.add(new MacroEventNode("me2"));
		mc.add(new MacroEventNode("me3"));

		MacroEventNode testcase = new MacroEventNode("testcase");
		mc.add(testcase);

		MacroEventCallerNode c;
		c = new MacroEventCallerNode("mc::me1");
		testcase.add(c);
		c = new MacroEventCallerNode("mc::me1");
		testcase.add(c);
		c = new MacroEventCallerNode("mc::me1");
		testcase.add(c);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component mc {\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me1() {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me2() {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me3() {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent testcase() {\n");
		bf.append("\t\tmc.me1();\n");
		bf.append("\t\tmc.me1();\n");
		bf.append("\t\tmc.me1();\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

	public void testMacroEventCaller_withArgs() {
		EBNFSaveVisitor v = new EBNFSaveVisitor();

		MacroComponentNode mc = MacroComponentNode.create("mc");

		MacroEventNode me1 = new MacroEventNode("me1");
		me1.getArguments().add(Argument.create("t1", "n1", "v1"));
		me1.getArguments().add(Argument.create("t2", "n2", "v2"));
		mc.add(me1);

		MacroEventNode testcase = new MacroEventNode("testcase");
		mc.add(testcase);

		MacroEventCallerNode c;
		c = new MacroEventCallerNode("mc::me1");
		c.getArguments().add(Argument.create("t1", "n1", "vv1"));
		c.getArguments().add(Argument.create("t2", "n2", "vv2"));
		testcase.add(c);

		c = new MacroEventCallerNode("mc::me1");
		c.getArguments().add(Argument.create("t1", "n1", "vv3"));
		c.getArguments().add(Argument.create("t2", "n2", "vv4"));
		testcase.add(c);

		c = new MacroEventCallerNode("mc::me1");
		c.getArguments().add(Argument.create("t1", "n1", "vv5"));
		c.getArguments().add(Argument.create("t2", "n2", "vv6"));
		testcase.add(c);

		mc.accept(v);
		assertNotNull(v.getStringBuffer());
		assertFalse(v.getStringBuffer().toString().equals(""));

		StringBuilder bf = new StringBuilder();
		bf.append("Component mc {\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent me1(n1, n2) {}\n");
		bf.append("\t//@level 1\n");
		bf.append("\tEvent testcase() {\n");
		bf.append("\t\tmc.me1(\"vv1\", \"vv2\");\n");
		bf.append("\t\tmc.me1(\"vv3\", \"vv4\");\n");
		bf.append("\t\tmc.me1(\"vv5\", \"vv6\");\n");
		bf.append("\t}\n");
		bf.append("}\n");

		assertEquals(bf.toString(), v.getStringBuffer().toString());
	}

}
