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
package gtt.editor.view.dialog;

import gtt.editor.view.ComponentView;
import gtt.eventmodel.IComponent;

import javax.swing.JPanel;

public class ComponentInfoView {
	private IComponent m_ComponentData = null;

	private ComponentView m_editPanel = null;

	public ComponentInfoView(IComponent c) {
		m_ComponentData = c;
		initPanel();
	}

	private void initPanel() {
		m_editPanel = new ComponentView();
		m_editPanel.setWinClassType(m_ComponentData.getWinType());
		m_editPanel.setWinTtile(m_ComponentData.getTitle());
		m_editPanel.setComponentClassType(m_ComponentData.getType());
		m_editPanel.setComponentName(m_ComponentData.getName());
		m_editPanel.setComponentText(m_ComponentData.getText());
		m_editPanel.setIndexInWindow(Integer.toString(m_ComponentData
				.getIndex()));
		m_editPanel.setIndexOfSameName(Integer.toString(m_ComponentData
				.getIndexOfSameName()));
	}

	public JPanel getPanel() {
		return m_editPanel.getView();
	}

	public IComponent getComponent() {
		return m_ComponentData;
	}

	public void accept() {
		String wintype = m_ComponentData.getType();
		try {
			Class.forName(m_editPanel.getWinClasstype());
			m_ComponentData.setWinType(m_editPanel.getWinClasstype());
		} catch (ClassNotFoundException e) {
			// 不正確的class type
			System.out.println(e);
			return;
		}

		try {
			Class.forName(m_editPanel.geComponentClasstype());
			m_ComponentData.setType(m_editPanel.geComponentClasstype());
		} catch (ClassNotFoundException e) {
			m_ComponentData.setType(wintype); // 恢復原本的win class type
			// 不正確的class type
			System.out.println(e);
			return;
		}

		// 以下應該都可以正常設值
		m_ComponentData.setName(m_editPanel.getComponentName());
//		m_ComponentData.setName(m_editPanel.getComponentName());
		m_ComponentData.setIndex(Integer.parseInt(m_editPanel
				.getIndexInWindow()));
		m_ComponentData.setIndexOfSameName(Integer.parseInt(m_editPanel
				.getIndexOfSameName()));
		m_ComponentData.setText(m_editPanel.getComponentText());
		m_ComponentData.setTitle(m_editPanel.getWinTitle());
	}

}
