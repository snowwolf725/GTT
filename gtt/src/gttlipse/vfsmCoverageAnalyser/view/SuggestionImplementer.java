package gttlipse.vfsmCoverageAnalyser.view;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.tester.macro.MacroTester;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.util.LinkedList;


public class SuggestionImplementer {

	MacroTester _macroTester;
	
	public SuggestionImplementer(MacroDocument macroDoc) {
		_macroTester = new MacroTester(macroDoc);
	}
	
	public TestMethodNode transformEventList(LinkedList<Object> eventList) {
		TestMethodNode methodNode = new TestMethodNode();
		TestScriptDocument testScriptDoc = methodNode.addInteractionSequence();
		AbstractNode parent = testScriptDoc.getScript();
		
		for(int index = 0; index < eventList.size(); index++) {
			if(eventList.get(index) instanceof ComponentEventNode) {
				EventNode eventNode = _macroTester.transformComponentEvent((ComponentEventNode)eventList.get(index));
				testScriptDoc.insertNode(parent, eventNode);
			}
			else {
				System.out.println("It does not ComponentEventNode");
				return null;
			}
		}
		
		return methodNode;
	}
	
}
