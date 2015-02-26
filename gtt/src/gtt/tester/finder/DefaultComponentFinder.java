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
package gtt.tester.finder;

import gtt.eventmodel.IComponent;

import java.awt.Component;
import java.awt.Window;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.ComponentSearcher;
import org.netbeans.jemmy.TimeoutExpiredException;
import org.netbeans.jemmy.operators.WindowOperator;

public class DefaultComponentFinder implements ComponentFinder {

	private final static DefaultComponentFinder m_Instance = new DefaultComponentFinder();

	private void logging(String msg) {
		System.out.println("[ComponentFinder] " + msg);
	}

	// Singleton Pattern
	public static DefaultComponentFinder instance() {
		return m_Instance;
	}

	IComponent component;

	public Component find(IComponent com) {
		if (com == null) {
			logging("[" + com + "] cannot be found.");
			return null; // can't found
		}

		component = com;

		return findComponent();
	}

	private Component findComponent() {
		Window window = findVisibleWindow();
		if (window == null) {
			logging("no visible window");
			return null;
		}

		try {
			// component 本身就是 Dialog/Frame/JWindow 等
			if (Window.class.isAssignableFrom(Class.forName(component.getType())))
				return window;

			// 否則, 在window上找元件
			return findComponentInWindow(window);
		} catch (ClassNotFoundException e) {
			logging("ComponentFinder " + e.getMessage());
		}
		return null;
	}

	private Window findVisibleWindow() {
		Window window = findWindow();

		// 沒有window，就沒有component
		if (window == null || !window.isShowing() || !window.isVisible()) {
			return null;
		}
		return window;
	}

	private Window findWindow() {
//		if (component.getType().indexOf("JFileChooser") != -1) {
//			// Component 本身是JFileChooser
//			int ct = 0;
//			while (ct < 3) {
//				// JFileChooser 有比較多的時間跟次數來找
//				// 停一點時間再找，會比較找的到 -zwshen 2010/01/07
//				try {
//					Thread.sleep(50 + 50 * ct);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				JDialog d = JFileChooserOperator.findJFileChooserDialog();
//				if (d != null)
//					return d;
//				ct++;
//			}
//		}

		// 使用自訂WindowChooser 來尋找 Window
		return WindowOperator.findWindow(DefaultWindowChooser.create(component));

	}

	private Component findComponentInWindow(Window window)
			throws TimeoutExpiredException {
		// 自訂Chooser，用來做尋找component的過瀘器
		ComponentChooser chooser = DefaultComponentChooser.create(component);

		// 建立search物件，在window中找元件
		ComponentSearcher searcher = new ComponentSearcher(window);

		// 有name時，就以name為第一優先搜尋條件
		if (component.getName() != null && !component.getName().equals("")) {
			// 當遇到同名的元件時，會以IndexOfSameName來做進一步判斷
			return searcher.findComponent(chooser, component.getIndexOfSameName());
		}

		// 加上 index 的判斷
		return searcher.findComponent(chooser, component.getIndex());
	}
}
