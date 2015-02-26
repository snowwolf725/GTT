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
package gtt.editor.view;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

public class TabbedView implements IView {

	private JTabbedPane m_TabbedPane;
	private List<IView> m_ViewList;
	private static final String VIEW_NAME = "TabbedView";

	public TabbedView() {
		m_TabbedPane = new JTabbedPane();
		m_TabbedPane.setName(VIEW_NAME);
		m_ViewList = new LinkedList<IView>();
	}

	public void addTab(IView view) {
		m_ViewList.add(view);
		m_TabbedPane.addTab(view.getViewName(), (JComponent)view.getView());
	}

	public JComponent getView() {
		return m_TabbedPane;
	}

	public String getViewName() {
		return VIEW_NAME;
	}

}
