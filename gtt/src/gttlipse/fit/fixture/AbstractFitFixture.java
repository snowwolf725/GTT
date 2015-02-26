package gttlipse.fit.fixture;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.table.FitTable;

public abstract class AbstractFitFixture {
	FitTable m_fitTable;
	MacroEventCallerNode m_macroEventCallerNode;
	MacroDocument m_macroDocument;

	public String getFixtureType() {
		return m_fitTable.getFixtureType();
	}

	public String getTableName() {
		return m_fitTable.getTableName();
	}

	abstract public MacroEventNode getMacroEventNode(int tableOlder);
}
