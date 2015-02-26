package gttlipse.fit.fixture;

import gtt.eventmodel.Arguments;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.table.FitTable;
import gttlipse.fit.view.GTTFitViewDefinition;

public class ActionFixture extends AbstractFitFixture {
	public ActionFixture(FitTable table, MacroEventCallerNode node,
			MacroDocument doc) {
		m_fitTable = table;
		m_macroEventCallerNode = node;
		m_macroDocument = doc;
	}

	// 設定macro參數內容
	private boolean configArgumentList(Arguments list, int tableOrder) {
		try {
			for (int j = 0; j < list.size(); j++)
				list.get(j).setValue(
						m_fitTable.getRowElement(tableOrder).get(
								GTTFitViewDefinition.FirstDataOfActionFixture
										+ j).getText());
			return true;
		} catch (NullPointerException e) {
			System.err.println("ActionFixture config argument Error!");
			return false;
		}
	}

	// 找出在actionfixture的table中，其order所對應之macro
	public MacroEventNode getMacroEventNode(int tableOrder) {
		// 先找出fit node中，指定的macro event node;
		MacroEventNode parentMacroEventNode = findMacroEventNode(m_macroEventCallerNode
				.getReferencePath());
		if (parentMacroEventNode != null) {
			if (m_fitTable.getRowCount() >= 0
					&& tableOrder <= m_fitTable.getRowCount()) {
				MacroEventNode targetMacroEventNode = null;
				String macroName = m_fitTable.getRowElement(tableOrder).get(
						GTTFitViewDefinition.MacroNameOfActionFixture)
						.getText();
				AbstractMacroNode[] children = parentMacroEventNode
						.getChildren();
				for (int i = 0; i < children.length; i++) {
					if (children[i] instanceof MacroEventCallerNode) {
						if (((MacroEventCallerNode) children[i]).getName()
								.compareTo(macroName) == 0) {
							targetMacroEventNode = findMacroEventNode(((MacroEventCallerNode) children[i])
									.getReferencePath());
							if (targetMacroEventNode != null
									&& configArgumentList(targetMacroEventNode
											.getArguments(), tableOrder)) {
								return targetMacroEventNode;
							}
							return null;
						}
					}
				}
			}
		}
		return null;
	}

	private MacroEventNode findMacroEventNode(String path) {
		AbstractMacroNode parentMacroEventNode = m_macroDocument.findByPath(path);
		
		if (parentMacroEventNode == null)
			return null;
		if (parentMacroEventNode instanceof MacroEventNode)
			return (MacroEventNode) parentMacroEventNode;
		return null;
	}
}
