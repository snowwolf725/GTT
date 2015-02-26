package gttlipse.tabular.util;

import java.util.List;

import gtt.macro.MacroUtil;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.macro.view.MacroPresenter;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.tabular.def.TableConstants;

import org.eclipse.jface.viewers.TreeViewer;


public class TreeStructureUtil {

	public static Class<AbstractMacroNode> CANDIDATE_TYPE = AbstractMacroNode.class;
	
	public static void refresh(AbstractMacroNode node) {
		TreeViewer viewer = GTTlipse.findMacroView().getViewer();
		
		viewer.refresh();
		viewer.setExpandedState(node, true);
	}
	
	public static void removeChildren(TestCaseNode node) {
		BaseNode[] children = node.getChildren();
		
		for(int i = 0; i < children.length; i++) {
			String methodName = children[i].getName();
			
			// Except special name such as 'getMethodName'
			if (methodName.equals(TableConstants.GET_METHOD_NAME)) {
				continue;
			}
			
			// Get the first test script documentation
			TestMethodNode methodNode = (TestMethodNode)children[i];
			TestScriptDocument doc = methodNode.getDocAt(0);
			
			// Remove all child nodes of documentation
			if (doc != null) {
				while(doc.getScript().hasChildren()) {
					doc.getScript().remove(0);
				}
			}
		}
	}
	
	public static void replace(AbstractMacroNode before, AbstractMacroNode after) {
		if ((before == null) || (after == null)) {
			return;
		}
		
		// Calculate the index of the node(before)
		int index = 0;
		AbstractMacroNode parent = before.getParent();
		AbstractMacroNode[] children = parent.getChildren();
		for(int i = 0; i < children.length; i++) {
			if (before == children[i]) {
				index = i;
				break;
			}
		}
		
		// Remove the node from the macro script
		MacroUtil.removeNode(before);
		
		// Insert the node to the macro script
		parent.add(after, index);
	}
	
	public static AbstractMacroNode findNode(AbstractMacroNode node, String name) {
		return findNode(node, name, CANDIDATE_TYPE);
	}
	
	public static AbstractMacroNode findNode(AbstractMacroNode node, String name, Class<?> expectedType) {
		if (node.getName().equals(name)) {
			if (expectedType.isAssignableFrom(node.getClass())) {
				return node;
			}
		}
		else {
			AbstractMacroNode[] children = node.getChildren();
			for(int i = 0; i < children.length; i++) {
				AbstractMacroNode foundNode = findNode(children[i], name, expectedType);
				if (foundNode != null) {
					return foundNode;
				}
			}
		}
		return null;
	}
	
	public static TestCaseNode findTestCaseNode(CompositeNode root, String name) {
		TestCaseNode testCase = null;
		
		// Source folder node
		SourceFolderNode folder = (SourceFolderNode)root.getChildrenByName("gtt");
		if (folder == null) {
			return testCase;
		}
		
		// Package node
		PackageNode packageNode = (PackageNode)folder.getChildrenByName("gttMacroScript");
		if (packageNode == null) {
			return testCase;
		}
		
		// Test case node
		testCase = (TestCaseNode)packageNode.getChildrenByName(name);
		
		return testCase;
	}
	
	public static void replayMacroScript(AbstractMacroNode node) {
		// Check the TestScriptDocument is existed or not
		if (GTTlipse.showScriptView().getMacroScriptEntryPoint() == false) {
			return;
		}
		
		if (node instanceof MacroEventNode) {
			MacroPresenter.insertScript(node);
		}
		else if (node instanceof MacroComponentNode) {
			List<MacroEventNode> nodes = ((MacroComponentNode)node).getMacroEvents();
			
			for(MacroEventNode n : nodes) {
				MacroPresenter.insertScript(n);
			}
		}
		else {
			// Unknown type, ignore it
			return;
		}
		
		MacroPresenter.replay();
	}
}
