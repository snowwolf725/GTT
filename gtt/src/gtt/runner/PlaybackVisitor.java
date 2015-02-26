package gtt.runner;

import gtt.oracle.AssertionChecker;
import gtt.oracle.IOracleHandler;
import gtt.tester.macro.IMacroTester;
import gtt.tester.swing.ITester;
import gtt.testscript.BreakerNode;
import gtt.testscript.CommentNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.SleeperNode;
import gtt.testscript.ViewAssertNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.fit.node.ReferenceFitNode;

/**
 * Test Script Playback Visitor 2005/06/13
 * 
 * @author Z.W Shen
 * 
 */
public class PlaybackVisitor implements ITestScriptVisitor {

	public PlaybackVisitor() {
	}

	/**
	 * Collection datas in playback
	 */
	PlaybackResult m_Result = new PlaybackResult();

	public PlaybackResult getPlaybackResult() {
		return m_Result;
	}

	// �o�ƥ�
	ITester m_Tester;

	public void setTester(ITester tester) {
		m_Tester = tester;
	}

	// ��view assertion
	AssertionChecker m_Asserter;

	public void setAsserter(AssertionChecker a) {
		m_Asserter = a;
	}

	IMacroTester m_MacroTester;

	public void setMacroTester(IMacroTester tester) {
		m_MacroTester = tester;
		m_MacroTester.setTester(m_Tester); // �]�w Event Tester
	}

	public void setController(Controller controller) {
		m_controller = controller;
	}

	private Controller m_controller;

	public void visit(EventNode node) {
		m_Result.addEventNodeCount();
		if (m_Tester == null)
			return;
		try {
			boolean r = m_Tester.fire(node);
			// ���ѡA���~���ƥ[1
			if (r == false)
				m_Result.addEventNodeErrorCount();
		} catch (Exception exp) {
			// �o�Ͳ��`���p
			System.out.format("[PlaybackVisitor] %s\n", exp.getMessage());
			// event node ���~���ƥ[1
			m_Result.addEventNodeErrorCount();
		}

	}

	public void visit(ViewAssertNode node) {
		m_Result.addViewAsserNodeCount();
		if (m_Asserter != null
				&& m_Asserter.check(node.getComponent(), node.getAssertion()) == false) {
			// view assert ���~���ƥ[1
			m_Result.addViewAssertNodeErrorCount();
		}
	}

	public void visit(ModelAssertNode node) {
		// �Ȯɤ��䴩 ModelAssertNode
	}

	public void visit(FolderNode node) {
		// nothing to do
	}

	public void visit(LaunchNode node) {
		// ���s�Ұ�AUT
		if (m_controller == null)
			return;
		m_controller.loadAUTbyMain(node.getLaunchData());
		m_controller.showAppWindow();
	}

	public void visit(ReferenceMacroEventNode node) {
		// MacroEvent Counting
		m_Result.addMacroCount();

		if (m_MacroTester == null)
			return;

		// Macro Event �������A��浹 MacroTester �h��
		try {
			boolean r = m_MacroTester.fire(node);
			if (r == false) // ���ѡA���~�Ӽ�+1
				m_Result.addMacroErrorCount();
		} catch (Exception exp) {
			// �o�Ͳ��`���p ���~�Ӽ�+1
			System.out.println("[PlaybackVisitor] " + exp.toString());
			exp.printStackTrace();
			m_Result.addMacroErrorCount();
		}
	}

	IOracleHandler m_OracleHandler = null;

	public void setOracleHandler(IOracleHandler handler) {
		m_OracleHandler = handler;
	}

	public void visit(OracleNode node) {
		// Oracle �Ӽ�+1
		m_Result.addOracleCount();
		if (m_OracleHandler == null) {
			m_Result.addOracleFailCount();
			return;
		}

		// ���� oracle information�A���x�s�_��
		boolean result = m_OracleHandler.handle(node.getOracleData());
		if (result == false) {
			// oracle ����
			m_Result.addOracleFailCount();
		}
	}

	@Override
	public void visit(SleeperNode node) {
		try {
			Thread.sleep(node.getSleepTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(BreakerNode node) {
		// stop playback
	}

	@Override
	public void visit(CommentNode node) {

	}

	@Override
	public void visit(ReferenceFitNode node) {
		if (m_MacroTester == null)
			return;

		// Macro Event �������A��浹 IMacroFirer �h��
		try {
			boolean r = m_MacroTester.fire(node);
			if (r == false) // ���ѡA���~�Ӽ�+1
				m_Result.addMacroErrorCount();
		} catch (Exception exp) {
			// �o�Ͳ��`���p ���~�Ӽ�+1
			System.err.println(exp.toString());
			m_Result.addMacroErrorCount();
		}
	}
}
