package gtt.runner;

public class PlaybackResult {
	private int m_eventCount;

	public void addEventNodeCount() {
		m_eventCount++;
	}

	public int getEventNodeCount() {
		return m_eventCount;
	}

	private int m_eventErrorCount;

	public void addEventNodeErrorCount() {
		m_eventErrorCount++;
		m_hasError = true;
	}

	public int getEventNodeErrorCount() {
		return m_eventErrorCount;
	}

	private int m_viewCount;

	public void addViewAsserNodeCount() {
		m_viewCount++;
	}

	public void addViewAssertNodeErrorCount() {
		m_viewErrorCount++;
		m_hasError = true;
	}

	private int m_viewErrorCount;

	public int getViewAssertNodeCount() {
		return m_viewCount;
	}

	public int getViewAssertNodeErrorCount() {
		return m_viewErrorCount;
	}

	private int m_modelCount;

	public void addModelAssertNodeCount() {
		m_modelCount++;
	}

	// ////////////////////////////////////////
	// MacroEvent
	private int m_macroCount;

	public void addMacroCount() {
		m_macroCount++;
	}

	public int getMacroCount() {
		return m_macroCount;
	}

	public int getModelAssertNodeCount() {
		return m_modelCount;
	}

	// ////////////////////////////////////////

	// ////////////////////////////////////////
	// MacroEvent Error counting
	private int m_macroErrorCount;

	public void addMacroErrorCount() {
		m_macroErrorCount++;
		m_hasError = true;
	}

	public int getMacroErrorCount() {
		return m_macroErrorCount;
	}

	// ////////////////////////////////////////

	public String result() {
		StringBuilder result = new StringBuilder("");

		result.append("<br>Event Nodes : " + getEventNodeCount());
		if (getEventNodeErrorCount() == 0) {
			result.append(" <font color=GREEN>OK!</font>");
		} else {
			result.append(" <font color=RED>" + getEventNodeErrorCount()
					+ "</font>");
		}

		// Macro Event Count
		result.append("<br>MacroEvent Nodes : " + getMacroCount());
		if (getMacroErrorCount() == 0) {
			result.append(" <font color=GREEN>OK!</font>");
		} else {
			result.append(" <font color=RED>" + getMacroErrorCount()
					+ "</font>");
		}
		// View Assertion
		result.append("<br>View Asserts: " + getViewAssertNodeCount());
		if (getViewAssertNodeErrorCount() == 0) {
			result.append(" <font color=GREEN>OK!</font>");
		} else {
			result.append(" <font color=RED>failure "
					+ getViewAssertNodeErrorCount() + "</font>");
		}
		result.append("<br>Model Asserts: " + getModelAssertNodeCount());

		// Oracle
		result.append("<br>Test Oracles: " + getOracleCount());
		if (getOracleFailCount() == 0) {
			result.append(" <font color=GREEN>OK!</font>");
		} else {
			result.append(" <font color=RED>failure " + getOracleFailCount()
					+ "</font>");
		}

		return result.toString();
	}

	private boolean m_hasError = false;

	public boolean hasError() {
		return m_hasError;
	}

	public void setHasError(boolean flag) {
		m_hasError = flag;
	}

	public PlaybackResult() {

	}

	// ////////////////////////////////////////
	// Oracle
	private int m_OracleCount;
	private int m_OracleErrorCount;

	public void addOracleCount() {
		m_OracleCount++;
	}

	public int getOracleCount() {
		return m_OracleCount;
	}

	public void addOracleFailCount() {
		m_OracleErrorCount++;
	}

	public int getOracleFailCount() {
		return m_OracleErrorCount;
	}

}
