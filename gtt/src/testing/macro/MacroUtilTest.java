package testing.macro;

import gtt.macro.MacroUtil;
import junit.framework.TestCase;

public class MacroUtilTest extends TestCase {
	
	MacroUtil macroUtil = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		macroUtil = new MacroUtil();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
//	public void testFindComponentNode() {
//		InvisibleRootNode iRoot = new InvisibleRootNode();
//		MacroComponentNode AUT = new MacroComponentNode();
//		MacroComponentNode mNode1 = new MacroComponentNode();
//		ComponentNode cNode1 = ComponentNode.create();
//		ComponentNode cNode2 = ComponentNode.create();
//		MacroComponentNode mNode2 = new MacroComponentNode();
//		ComponentEventNode cENode = new ComponentEventNode();
//		
//		AUT.setName("AUT");
//		mNode2.setName("mNode2");
//		mNode1.setName("mNode1");
//		cNode1.setName("cNode1");
//		cNode2.setName("cNode2");
//		cENode.setName("cENode");
//		
//		iRoot.add(AUT);
//		AUT.add(mNode1);
//		mNode1.add(mNode2);
//		mNode1.add(cNode1);
//		mNode2.add(cNode2);
//		mNode2.add(cENode);
//		
//		assertEquals("AUT::mNode1::mNode2::cNode2", cNode2.getPath().toString());
//		cENode.setComponentPath(cNode2.getPath().toString());
//		cENode.setParent(mNode2);
//		
//		assertEquals("cNode2", DefaultMacroFinder.findComponentNode(cENode).getName());
//		
//	}
}
