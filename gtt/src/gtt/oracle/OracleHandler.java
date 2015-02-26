/**
 * 
 */
package gtt.oracle;

import gtt.eventmodel.IComponent;
import gtt.testscript.ViewAssertNode;

import java.awt.Frame;
import java.awt.Window;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.interpreter
 * 
 */
public class OracleHandler implements IOracleHandler {
	private OracleData m_OracleData;
	private ComponentCollector m_collector = null;
	private Window theMainWindow = null;
	private boolean bCollectOracle = false;

	public OracleHandler(ComponentCollector collector, Window window) {
		m_collector = collector;
		theMainWindow = window;
	}

	public OracleHandler(Window window) {
		m_collector = new ComponentCollector();
		m_collector.setCollect(true);
		theMainWindow = window;
	}

	public void setCollectOracle(boolean flag) {
		bCollectOracle = flag;
	}

	public boolean handle(OracleData data) {
		if (bCollectOracle) {
			// �C�@�� Oracle node �|�x�s������ViewAssertNode List
			return collectOracleInformation(data);
		} else {
			// else - ����Oracle Infomration
			return validOracleInformation(data);
		}

	}

	private List<ViewAssertNode> process(OracleData oracle) {
		if (oracle == null)
			return new Vector<ViewAssertNode>();

		m_OracleData = oracle;
		if (m_OracleData.getLevel() == IOracleHandler.Level.APPLICATION_LEVEL) {
			return forApplicationLevel();
		} else {
			return forWindowAndComponentLevel();
		}
	}

	// for Window and Component Oracle Level Information
	private List<ViewAssertNode> forWindowAndComponentLevel() {
		List<IComponent> win = new Vector<IComponent>();
		// �u�Ҽ{�ثe��@���@�� main window
		// Component Level �]�|�bMain Window�W��M
		m_collector.collect(win, theMainWindow);

		return OracleUtil.makeAssertList(win, m_OracleData);
	}

	// for Application Oracle Level Information
	private List<ViewAssertNode> forApplicationLevel() {
		Frame[] frames = Frame.getFrames();
		List<IComponent> wins = new Vector<IComponent>();
		for (Frame f : frames) {
			// ��C�@��frame ���i��@��collection
			m_collector.collect(wins, f);
			Window[] windows = f.getOwnedWindows();
			for (Window win : windows) {
				if (win.isVisible() == false)
					continue;
				// ��C�@��win ���i��@��collection
				m_collector.collect(wins, win);
			}
		}

		return OracleUtil.makeAssertList(wins, m_OracleData);
	}

	List<ViewAssertNode> m_OracleResult = new LinkedList<ViewAssertNode>();

	// �qoracle��T�����X�C�@��ViewAssertNode �A�@�@�i������
	private boolean validOracleInformation(OracleData oracle) {
		List<ViewAssertNode> assertlist = OracleDataIO.readOracle(oracle);

		Iterator<ViewAssertNode> ite = assertlist.iterator();
		// �C��oracle �O���@��failure�T��
		boolean bFail = true;
		m_OracleResult = new LinkedList<ViewAssertNode>();
		while (ite.hasNext()) {
			ViewAssertNode va = ite.next();
			if (verify(va) == false) {
				// �u�O���ҥ��Ѫ��`�I
				m_OracleResult.add(va); // �O���C�@��view assert
				bFail = false;
			}
		}

		if (m_OracleResult.size() > 0) {
			// �x�s��HTML�榡
			OracleDataIO.writeAsHtml(oracle, m_OracleResult);
		}
		return bFail;
	}

	// ����oracle ��T
	private boolean collectOracleInformation(OracleData oracle) {
		// ����Oracle - �Y�@�� ViewAssertNode
		List<ViewAssertNode> list = process(oracle);
		if (list == null || list.size() == 0)
			return false;
		System.out.println("collect oracle size: " + list.size());
		// �s�� - �ɦW��OracleData UUID
		OracleDataIO.writeOracle(list, oracle);
		return true;
	}

	private boolean verify(ViewAssertNode node) {
		if (node.getComponent().getType().indexOf("javax.swing.JFrame") >= 0)
			return true;

		AssertionChecker checker = new SwingChecker();
		if (checker.check(node.getComponent(), node.getAssertion()) == false) {
			// ���ҥ���
			node.setActualValue(checker.getActualValue());
			return false;
		}
		// ���ҥ��T
		node.setActualValue(null);
		return true;
	}

}
