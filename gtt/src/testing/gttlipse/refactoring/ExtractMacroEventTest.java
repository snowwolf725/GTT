package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.ExtractMacroEvent;

import java.util.Vector;

import junit.framework.TestCase;

public class ExtractMacroEventTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCheckUsable() {
		// initial
		Vector<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		MacroComponentNode root = new MacroComponentNode();
		MacroEventNode event = new MacroEventNode();
		event.setName("EVENT");
		root.add(event);
		
		ComponentEventNode comEve1 = new ComponentEventNode();
		ComponentEventNode comEve2 = new ComponentEventNode();
		ComponentEventNode comEve3 = new ComponentEventNode();
		
		event.add(comEve1);
		event.add(comEve2);
		event.add(comEve3);
		
		nodes.add(comEve1);
		nodes.add(comEve3);
		
		ExtractMacroEvent refactor = new ExtractMacroEvent();
		
		// assert
		assertFalse(refactor.isValid(nodes, nodes.get(0).getParent().getParent(), "TEST"));
		nodes.clear();
		nodes.add(comEve1);
		nodes.add(comEve2);
		assertTrue(refactor.isValid(nodes, nodes.get(0).getParent().getParent(), "TEST"));
	}
	
	public void testExtractMacroEvent() {
		// initial
		Vector<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		MacroComponentNode root = new MacroComponentNode();
		MacroEventNode event = new MacroEventNode();
		event.setName("EVENT");
		event.setParent(null);
		root.add(event);
		Arguments list = new Arguments();
		list.add(Argument.create("TYPE", "name", "value"));
		event.setArguments(list);
		
		Arguments comList = new Arguments();
		comList.add(Argument.create("TYPE", "NAME", "@name"));
		ComponentEventNode comEve1 = new ComponentEventNode();
		comEve1.setArguments(comList);
		ComponentEventNode comEve2 = new ComponentEventNode();
		ComponentEventNode comEve3 = new ComponentEventNode();
		
		event.add(comEve1);
		event.add(comEve2);
		event.add(comEve3);
		
		nodes.add(comEve1);
		nodes.add(comEve2);
		
		ExtractMacroEvent refactor = new ExtractMacroEvent();
		
		// assert
		assertTrue(root.size() == 1);
		assertTrue(comEve1.getParent() == event);
		assertTrue(comEve2.getParent() == event);
		refactor.extractMacroEvent(nodes, "NEW EVENT");
		assertFalse(comEve1.getParent() == event);
		assertFalse(comEve2.getParent() == event);
		assertTrue(comEve3.getParent() == event);
		assertTrue(root.size() == 2);
		MacroEventNode newEvent = (MacroEventNode) root.get(1);
		assertTrue(newEvent.getName().equals("NEW EVENT"));
		assertTrue(comEve1.getParent() == newEvent);
		assertTrue(comEve2.getParent() == newEvent);
		Argument arg = newEvent.getArguments().get(0);
		assertTrue(arg.getName().equals("name"));
	}
}
