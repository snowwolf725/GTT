package gtt.runner;

import gtt.editor.configuration.IConfiguration;
import gtt.editor.presenter.ITestScriptPresenter;
import gtt.editor.view.TestResultData;
import gtt.editor.view.TestResultView;
import gtt.editor.view.TreeNodeData;
import gtt.macro.IMacroPresenter;
import gtt.oracle.OracleHandler;
import gtt.oracle.SwingChecker;
import gtt.tester.macro.IMacroTester;
import gtt.tester.macro.MacroTester;
import gtt.tester.swing.ITester;
import gtt.tester.swing.SwingTester;
import gtt.testscript.BreakerNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

//playback thread
public class PlaybackThread extends Thread {

	public static PlaybackThread create(final ITestScriptPresenter sp,
			final IMacroPresenter mp, final IConfiguration config,
			final Controller controller) {
		return new PlaybackThread(sp, mp, config, controller);
	}

	private ITestScriptPresenter m_ScriptPresenter; // for test script
	private IMacroPresenter m_MacroPresenter; // for macro script
	private IConfiguration m_config; // for global configuration
	private Controller m_controller; // for AUT handling

	PlaybackThread(final ITestScriptPresenter sp, final IMacroPresenter mp,
			final IConfiguration config, final Controller controller) {
		m_ScriptPresenter = sp;
		m_MacroPresenter = mp;
		m_config = config;
		m_controller = controller;
	}

	/* �Hhtml ���榡��� playback ���᪺�έp���G */
	private void showResult(PlaybackResult result) {
		StringBuilder msg = new StringBuilder("<html>");
		msg.append("<center>");
		if (m_config.getCollectOracle() == true) {
			// ����Oracle�w��������
			msg
					.append("<font color=GREEN>Collect oracle information finish.</font>");
			msg.append(result.result());
		} else {
			msg.append("<h3><FONT color=BLUE>Playback finish!!</font><hr>");
			// ���յ��G
			msg.append(result.result());
		}
		msg.append("</center><hr></html>");
		m_ScriptPresenter.getView().showMessage(msg.toString());
	}

	public List<TestResultData> m_ResultDataList;

	public void run() {
		isRunning = true;

		PlaybackVisitor visitor = createPlaybackVisitor();
		m_ResultDataList = new LinkedList<TestResultData>();

		try {
			// ���J AUT
			m_controller.loadAUT(m_config);

			Iterator<TreeNode> ite = m_ScriptPresenter.treeIterator();
			while (isRunning && ite.hasNext()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ite
						.next();
				// ����@��node�ɡATestScript�e���W�n���o��node
				m_ScriptPresenter.selectTreeNode(node);

				TreeNodeData data = ((TreeNodeData) node.getUserObject());
				// ���`��
				data.setColor(TreeNodeData.Color.NORMAL);
				if (data.getData() instanceof BreakerNode) {
					// �J�� breaker �N�Ȱ�
					data.setColor(TreeNodeData.Color.PLAY);
					break;
				}
				// �ϥ� Visitor �ӫ��Xdata�̪�script node
				data.getData().accept(visitor);
				// �ˬd�O�_�����~
				checkErrorHappening(visitor.getPlaybackResult(), node);
			}
		} catch (Throwable tt) {
			System.out.println("GTT PlaybackThread exception.");
			tt.printStackTrace();
		}

		System.out.println("playback finished.");
		isRunning = false;
		m_ScriptPresenter.getView().updateUI();
		m_TestResultView.setErrorData(m_ResultDataList);
		// ���񧹲��A��ܵ��G
		showResult(visitor.getPlaybackResult());
	}

	TestResultView m_TestResultView;

	public void setTestResultView(TestResultView v) {
		m_TestResultView = v;
	}

	private boolean checkErrorHappening(PlaybackResult result,
			DefaultMutableTreeNode node) {
		TreeNodeData data = ((TreeNodeData) node.getUserObject());

		if (!result.hasError()) {
			// �S��error �o��
			data.setColor(TreeNodeData.Color.PLAY);
			return false;
		}

		// ���o��error
		data.setColor(TreeNodeData.Color.ERROR);

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		while (parent != null) {
			// node �W��parents ���ݭn�G���O
			TreeNodeData td = (TreeNodeData) parent.getUserObject();
			td.setColor(TreeNodeData.Color.ERROR);
			parent = (DefaultMutableTreeNode) parent.getParent();
		}
		// if (data.getData() instanceof ViewAssertNode) {
		// ViewAssertNode vd = (ViewAssertNode) data.getData();
		// TestResultData rd = new TestResultData(data.getData().toString(),
		// vd.getAssertion().getMessage(), node.getPath());
		// m_ResultDataList.add(rd);
		// } else {
		m_ResultDataList.add(new TestResultData(node));
		result.setHasError(false);
		return true;
	}

	private PlaybackVisitor createPlaybackVisitor() {
		PlaybackVisitor visitor = new PlaybackVisitor();
		// Swing Event ���ƥ�o�e����
		ITester swingtester = new SwingTester();
		swingtester.setSleepTime(m_config.getSleepTime());
		visitor.setTester(swingtester);
		// View Assertion ����
		visitor.setAsserter(new SwingChecker());

		// macro event ���ƥ�o�e����
		IMacroTester macroTester = new MacroTester(m_MacroPresenter.getModel());
		macroTester.setTester(new SwingTester());

		visitor.setMacroTester(macroTester);
		// macro event ���ƥ�o�e����
		visitor.setController(m_controller);

		// �]�wOracle Handler
		OracleHandler handler = new OracleHandler(m_controller
				.getAppMainWindow());
		// �O�_�n����������oracle
		handler.setCollectOracle(m_config.getCollectOracle());

		visitor.setOracleHandler(handler);
		macroTester.setOracleHandler(handler);
		return visitor;
	}

	public boolean isRunning = true;

	public boolean isRun() {
		return isRunning;
	}

	public void terminate() {
		isRunning = false;
		interrupt();
	}
}
