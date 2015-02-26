package gtt.runner;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.MacroCountingVisitor;
import gtt.oracle.OracleHandler;
import gtt.oracle.SwingChecker;
import gtt.tester.macro.IMacroTester;
import gtt.tester.macro.MacroTester;
import gtt.tester.swing.ITester;
import gtt.tester.swing.SwingTester;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.visitor.TestScriptCountingVisitor;

import java.awt.Window;
import java.util.Iterator;

//playback thread
public class PlaybackStandalong {

	final TestScriptDocument doc_ts;
	final MacroDocument doc_macro;

	// factory method: 利用 .gtt 檔案來產生播放物件
	public static PlaybackStandalong create(String filename) {
		TestScriptDocument ts = TestScriptDocument.create();
		MacroDocument ms = MacroDocument.createFromFile(filename);

		if (ts.openFile(filename) == false)
			return null;
		return new PlaybackStandalong(ts, ms);
	}

	public PlaybackStandalong(TestScriptDocument ts, MacroDocument macro) {
		this.doc_ts = ts;
		this.doc_macro = macro;
	}

	public String countNodes() {
		StringBuilder result = new StringBuilder("");

		AbstractNode aNode = doc_ts.getScript();
		if (aNode != null) {
			TestScriptCountingVisitor tcv = new TestScriptCountingVisitor();
			aNode.accept(tcv);
			result.append(tcv.toString());
		}

		AbstractMacroNode mNode = doc_macro.getMacroScript();
		if (mNode != null) {
			MacroCountingVisitor mcv = new MacroCountingVisitor();
			mNode.accept(mcv);
			result.append(mcv.toString());
		}

		return result.toString();
	}

	public void run() throws Exception {
		run(0); // default 0ms
	}

	/**
	 * 須由外界啟動 AUT，執行測試腳本
	 * 
	 * @throws Exception
	 */
	public void run(long sleeptime) throws Exception {
		PlaybackVisitor visitor = new PlaybackVisitor();

		// Swing Event 的事件發送機制
		ITester swingtester = new SwingTester();
		swingtester.setSleepTime(sleeptime);
		visitor.setTester(swingtester);
		// View Assertion 機制
		visitor.setAsserter(new SwingChecker());

		// macro event 的事件發送機制
		IMacroTester macroTester = new MacroTester(doc_macro);
		visitor.setMacroTester(macroTester);

		Iterator<AbstractNode> ite = doc_ts.iterator();
		while (ite.hasNext()) {
			AbstractNode node = ite.next();
			node.accept(visitor);
		}
	}

	public boolean runMacro(String path) throws Exception {
		return runMacro(path, 100); // default 100 ms
	}

	/**
	 * 須由外界啟動 AUT，可指定巨集腳本
	 * 
	 * @throws Exception
	 */
	public boolean runMacro(String path, long sleeptime) throws Exception {
		// 預設是 oracle collecting 模式
		return runMacro(path, sleeptime, true);
	}

	private int m_ErrorType = IMacroTester.NO_ERROR;

	public int getErrorType() {
		return m_ErrorType;
	}

	public boolean runMacro(String path, long sleeptime, boolean bCollecOracle)
			throws Exception {
		// macro event 的事件發送機制
		IMacroTester m_MacroTester = new MacroTester(doc_macro);
		// Swing Event 的事件發送機制
		ITester swingtester = new SwingTester();
		swingtester.setSleepTime(sleeptime);
		m_MacroTester.setTester(swingtester);

		if (m_Window != null) {
			OracleHandler h = new OracleHandler(m_Window);
			h.setCollectOracle(bCollecOracle);
			m_MacroTester.setOracleHandler(h);
		}

		NodeFactory factory = new NodeFactory();
		ReferenceMacroEventNode node = factory
				.createReferenceMacroEventNode(path);

		boolean r = m_MacroTester.fire(node);
		m_ErrorType = m_MacroTester.getErrorType();

		return r;
	}

	private Window m_Window = null;

	public void setMainWindow(Window w) {
		m_Window = w;
	}
}
