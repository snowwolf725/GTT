package gttlipse.fit.node.processor;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.oracle.AssertionChecker;
import gttlipse.fit.FixtureDefinition;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.table.FitTable;
import gttlipse.fit.table.TableLoader;
import gttlipse.fit.table.TableRow;
import gttlipse.fit.table.TableSaver;

import org.eclipse.swt.SWT;


public class FitStateAssertionNodeProcessor extends EventTrigger{
	AssertionChecker m_componentAsserter;
	FitTable m_fitTable;
	String m_windowTitle;
	String m_windowType;
	String m_tablePath;

	public FitStateAssertionNodeProcessor(AssertionChecker asserter) {
		m_componentAsserter = asserter;
	}
	
	public boolean process(FitStateAssertionNode node) {
		if(node == null)
			return false;
		m_tablePath = node.getProjectRoot() + node.getTableFilePath();
		m_windowTitle = node.getWindowTitle();
		m_windowType = node.getWindowType();
		m_fitTable = TableLoader.read(m_tablePath, FixtureDefinition.RowFixture);
		return trigger();
	}
	
	public boolean trigger() {
		Assertion assertion = new Assertion();
		for(int i = 0; i < m_fitTable.getRowElement().size(); i++) {
			IComponent ic = createComponent(m_fitTable.getItem("Type", m_fitTable.getRowElement(i)), m_fitTable.getItem("Name", m_fitTable.getRowElement(i)), m_windowTitle, m_windowType); 
			assertion.setMethodName(m_fitTable.getItem("Method()", m_fitTable.getRowElement(i)));
			assertion.setValue(m_fitTable.getItem("Value", m_fitTable.getRowElement(i)));
			testResult(m_fitTable.getRowElement(i), m_componentAsserter.check(ic, assertion));
		}
		saveResult();
		return true;
	}
	
	private void testResult(TableRow row, boolean result) {
		for(int i = 0; i < row.getSize(); i++) {
			if(result)
				row.get(i).setColor(SWT.COLOR_DARK_GREEN);
			else
				row.get(i).setColor(SWT.COLOR_RED);
		}
	}
	
	private void saveResult() {
		TableSaver m_saver = new TableSaver();
		m_saver.save(m_fitTable, m_tablePath);
	}
}
