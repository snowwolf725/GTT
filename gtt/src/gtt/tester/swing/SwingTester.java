/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.tester.swing;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.logger.Logger;
import gtt.runner.RunnerUtil;
import gtt.tester.finder.DefaultComponentFinder;
import gtt.testscript.EventNode;

import java.awt.Component;
import java.awt.Window;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/*
 * 使用 Jemmy 模組來發事件
 * 使用 Event ID 來判斷要發送的事件類型
 * zws 2007/05/22
 */

/// Note 070522 : 每個event都含有sleep time 的參數，但是還沒有被放進播放中
public class SwingTester implements ITester {

	static {
		// 初始化 jemmy module
		RunnerUtil.initJemmyModule();
	}

	public SwingTester() {
		registrTester();
	}

	private HashMap<Class<?>, IComponentTester> m_Tester = new HashMap<Class<?>, IComponentTester>();

	public void addTester(Class<?> cls, IComponentTester tester) {
		m_Tester.put(cls, tester);
	}

	public IComponentTester mappedTester(Object obj) {
		// 先找出Class可以直接對應的Tester
		IComponentTester tester = m_Tester.get(obj.getClass());
		if (tester != null) {
			return tester;
		}

		Set<Class<?>> keys = m_Tester.keySet();
		Iterator<Class<?>> ite = keys.iterator();
		while (ite.hasNext()) {
			// 否則，則找出sub-class可對應的Tester
			Class<?> cls = ite.next();
			if (cls.isAssignableFrom(obj.getClass())) {
				return m_Tester.get(cls);
			}
		}
		// 預設也會有一個 tester
		return DEFAULT_TESTER;
	}

	private void registrTester() {
		// 註冊各個class 的tester
//		addTester(JComponent.class, new JComponentTester());
		addTester(AbstractButton.class, new AbstractButtonTester());
		addTester(JButton.class, new AbstractButtonTester());
		addTester(JCheckBox.class, new AbstractButtonTester());
		addTester(JMenuItem.class, new AbstractButtonTester());
		addTester(JRadioButton.class, new AbstractButtonTester());

		addTester(JTextComponent.class, new JTextComponentTester());
		addTester(JTextField.class, new JTextComponentTester());
		addTester(JTextPane.class, new JTextComponentTester());
		addTester(JTextArea.class, new JTextAreaTester());

		addTester(JColorChooser.class, new JColorChooserTester());
		addTester(Window.class, new WindowTester());
		addTester(JWindow.class, new WindowTester());
		addTester(JFrame.class, new JFrameTester());
		addTester(JTree.class, new JTreeTester());
		addTester(JList.class, new JListTester());
		addTester(JInternalFrame.class, new JInternalFrameTester());

		addTester(JMenu.class, new JMenuTester());
		addTester(JMenuBar.class, new JMenuBarTester());

		addTester(JPopupMenu.class, new JPopupMenuTester());
		addTester(JSpinner.class, new JSpinnerTester());
		addTester(JScrollBar.class, new JScrollBarTester());
		addTester(JSlider.class, new JSliderTester());
		addTester(JScrollPane.class, new JScrollPaneTester());
		addTester(JComboBox.class, new JComboBoxTester());
		addTester(JSplitPane.class, new JSplitPaneTester());
		addTester(JTabbedPane.class, new JTabbedPaneTester());
		addTester(JFileChooser.class, new JFileChooserTester());
		addTester(JTable.class, new JTableTester());
		addTester(JTableHeader.class, new JTableHeaderTester());
	}

	private void logging(String msg) {
		// 應該使用 logging 機制，會比較乾淨
		// zws 2007/01/04
		String prefix = "SwingTester";
		Logger.getSimpleLogger().log(prefix + "-" + msg);
	}

	private java.awt.Component findComponent(IComponent info) {
		java.awt.Component component = DefaultComponentFinder.instance().find(info);
		if (component == null) {
			logging("Could not find \"" + info + "\".");
			return null;
		}
		if (!(component instanceof Component)) { // 改變JComponent的轉型為Component
			logging("\"" + info + "\" isn't a java.awt.Component");
			return null;
		}
		return component;
	}

	public synchronized boolean fire(EventNode node) {
		java.awt.Component com = findComponent(node.getComponent());
		if (com == null)
			return false;

		try {
			dispatchFireEvent(node.getEvent(), com);
			if (m_GlobalSleepTime > 0)
				Thread.sleep(m_GlobalSleepTime);
		} catch (Exception exp) {
			logging("[error] " + exp.toString());
			return false; // 這個事件無法成功發送
		}
		// 至此一切正常
		return true;
	}

	private final static JComponentTester DEFAULT_TESTER = new JComponentTester();

	protected void dispatchFireEvent(IEvent event, Component comp) {
		// 找出 Component對應的 Tester
		IComponentTester tester = mappedTester(comp);

		try {
			boolean r = tester.fireEvent(event, comp);

			// 發成功的事件，要考慮sleep time
			if (r == true) {
				sleep(event);
			}
		} catch (NullPointerException nep) {
			nep.printStackTrace();
		}
	}

	private void sleep(IEvent info) {
		if (info.getArguments().getValue("SleepTime") == null)
			return;
		String sleepTime = info.getArguments().getValue("SleepTime");
		try {
			int time = Integer.parseInt(sleepTime);
			Thread.sleep(time);
		} catch (Exception e) {
			// nothing to do
		}
	}

	private long m_GlobalSleepTime = DEFAULT_SLEEP_TIME;

	public void setSleepTime(long st) {
		if (st < 0)
			st = 0;
		m_GlobalSleepTime = st;
	}
}
