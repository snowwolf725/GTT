package gttlipse.fit.fixture;

import gtt.eventmodel.Arguments;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.table.FitTable;

public class ColumnFixture extends AbstractFitFixture {
	public ColumnFixture(FitTable table, MacroEventCallerNode node,
			MacroDocument doc) {
		m_fitTable = table;
		m_macroEventCallerNode = node;
		m_macroDocument = doc;
	}

	private boolean configArgumentList(Arguments list, int tableOrder) {
		try {
			for (int j = 0; j < list.size(); j++)
				list.get(j).setValue(
						m_fitTable.getItem(list.get(j).getName(), m_fitTable
								.getRowElement(tableOrder)));
			return true;
		} catch (NullPointerException e) {
			System.err.println("ColumnFixture config argument Error!");
			return false;
		}
	}

	// ColumnFixture and RowFixture ぃ穦table
	// olderτ跑传macro常琌肚fitnodeい﹚macro穦跑传把计
	public MacroEventNode getMacroEventNode(int tableOrder) {
		AbstractMacroNode macroEventNode = m_macroDocument
				.findByPath(m_macroEventCallerNode.getReferencePath());
		if (macroEventNode == null)
			return null;

		if (macroEventNode instanceof MacroEventNode) {
			if (configArgumentList(((MacroEventNode) macroEventNode)
					.getArguments(), tableOrder))
				return (MacroEventNode) macroEventNode;
			else
				return null;
		}
		return null;
	}
}
