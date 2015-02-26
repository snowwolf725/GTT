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
			// 每一個 Oracle node 會儲存對應的ViewAssertNode List
			return collectOracleInformation(data);
		} else {
			// else - 驗證Oracle Infomration
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
		// 只考慮目前單一的一個 main window
		// Component Level 也會在Main Window上找尋
		m_collector.collect(win, theMainWindow);

		return OracleUtil.makeAssertList(win, m_OracleData);
	}

	// for Application Oracle Level Information
	private List<ViewAssertNode> forApplicationLevel() {
		Frame[] frames = Frame.getFrames();
		List<IComponent> wins = new Vector<IComponent>();
		for (Frame f : frames) {
			// 對每一個frame 都進行一次collection
			m_collector.collect(wins, f);
			Window[] windows = f.getOwnedWindows();
			for (Window win : windows) {
				if (win.isVisible() == false)
					continue;
				// 對每一個win 都進行一次collection
				m_collector.collect(wins, win);
			}
		}

		return OracleUtil.makeAssertList(wins, m_OracleData);
	}

	List<ViewAssertNode> m_OracleResult = new LinkedList<ViewAssertNode>();

	// 從oracle資訊中取出每一個ViewAssertNode ，一一進行驗證
	private boolean validOracleInformation(OracleData oracle) {
		List<ViewAssertNode> assertlist = OracleDataIO.readOracle(oracle);

		Iterator<ViewAssertNode> ite = assertlist.iterator();
		// 每份oracle 記錄一份failure訊息
		boolean bFail = true;
		m_OracleResult = new LinkedList<ViewAssertNode>();
		while (ite.hasNext()) {
			ViewAssertNode va = ite.next();
			if (verify(va) == false) {
				// 只記驗證失敗的節點
				m_OracleResult.add(va); // 記錄每一個view assert
				bFail = false;
			}
		}

		if (m_OracleResult.size() > 0) {
			// 儲存成HTML格式
			OracleDataIO.writeAsHtml(oracle, m_OracleResult);
		}
		return bFail;
	}

	// 收集oracle 資訊
	private boolean collectOracleInformation(OracleData oracle) {
		// 產生Oracle - 即一堆 ViewAssertNode
		List<ViewAssertNode> list = process(oracle);
		if (list == null || list.size() == 0)
			return false;
		System.out.println("collect oracle size: " + list.size());
		// 存檔 - 檔名用OracleData UUID
		OracleDataIO.writeOracle(list, oracle);
		return true;
	}

	private boolean verify(ViewAssertNode node) {
		if (node.getComponent().getType().indexOf("javax.swing.JFrame") >= 0)
			return true;

		AssertionChecker checker = new SwingChecker();
		if (checker.check(node.getComponent(), node.getAssertion()) == false) {
			// 驗證失敗
			node.setActualValue(checker.getActualValue());
			return false;
		}
		// 驗證正確
		node.setActualValue(null);
		return true;
	}

}
