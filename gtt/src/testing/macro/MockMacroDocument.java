package testing.macro;

import gtt.macro.macroStructure.MacroComponentNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MockMacroDocument {

	public Element getDOMFromData(Document XMLDoc) {
		return null;
	}

	private boolean correct = false;

	private MacroComponentNode tree_root;

	public boolean isCorrect() {
		return correct;
	}

	public MacroComponentNode getTreeNode() {
		return tree_root;
	}

/*	// 將XML的DOM格式存回成資料
	public void setDOMToData(org.w3c.dom.Element element) {
		correct = true;
		if (!element.getNodeName().equals("MacroDocument")) {
			correct = false;
			return;
		}
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			org.w3c.dom.Element aChild = DataReadableUtility
					.tranToElementNode(element.getChildNodes().item(i));
			if (aChild == null)
				continue;

			if (aChild.getNodeName().equals("MacroTree")) {
				// 應該只有一個 macro tree
				loadMacroTree(aChild);
				break;
			}
		}
	}

	// 將XML的DOM格式存回成資料
	public void loadMacroTree(org.w3c.dom.Element element) {
		if (!element.getNodeName().equals("MacroTree"))
			return;

		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			org.w3c.dom.Element aChild = DataReadableUtility
					.tranToElementNode(element.getChildNodes().item(i));
			if (aChild == null)
				continue;
			if (!aChild.getNodeName().equals("MacroComponent"))
				continue;

			SwingMacroNode tree = new SwingMacroNode(new MacroComponentNode());
			tree.setDOMToData(aChild);

			tree_root = (MacroComponentNode) tree.getUserObject();
		}

	}*/
}
