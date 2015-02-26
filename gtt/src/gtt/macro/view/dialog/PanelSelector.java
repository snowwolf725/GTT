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
/*
 * Created on 2005/4/13
 */
package gtt.macro.view.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.view.IMacroTree;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author 哲銘 這個class負責控制細節編輯介面(detail editor)的狀態切換
 *
 */
public class PanelSelector {
	private JPanel m_MainPanel = new JPanel(new BorderLayout());

	private List<IDataNodePanel> m_Contains = new LinkedList<IDataNodePanel>();

	public PanelSelector() {
		// sub panels
		m_Contains.add(new ViewAssertPanel());
		m_Contains.add(new ComponentEventPanel());
		m_Contains.add(new MacroEventCallerPanel());
		m_Contains.add(new ModelAssertPanel());
		m_Contains.add(new CommentPanel());
		m_Contains.add(new SleeperPanel());
	}

	public JPanel getMainPanel() {
		return m_MainPanel;
	}

	// 空的panel ，不做任何事
	private static JPanel EMPTY_PANEL = new JPanel();

	public boolean select(AbstractMacroNode data, JTree hereMacroTree,
			IMacroTree outMacroTree, DefaultMutableTreeNode hereMacroEvent) {
		m_MainPanel.removeAll();

		try {
			Iterator<IDataNodePanel> ite = m_Contains.iterator();
			while (ite.hasNext()) {
				// 從Container中，找尋一個可以接受此node的panel
				IDataNodePanel thePanel = ite.next();
				if (thePanel.isAccept(data, hereMacroTree, outMacroTree,
						hereMacroEvent) == true) {
					// 找到，替換掉panel
					m_MainPanel.add(BorderLayout.CENTER, (JPanel) thePanel);
					m_MainPanel.updateUI();
					return true;
				}
			}
		} catch (Exception nep) {
			// something happen
			System.out.println(nep.getMessage());
		}

		// 沒找到，則使用一個空panel
		m_MainPanel.add(BorderLayout.CENTER, EMPTY_PANEL);
		m_MainPanel.updateUI();
		return false;
	}

}
