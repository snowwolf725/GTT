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
 * Created on 2005/3/15
 */
package gtt.macro.view.dialog;

import gtt.editor.view.ComponentView;
import gtt.eventmodel.IComponent;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ComponentInfoDialog extends JDialog {
	/**
	 * default serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private IComponent m_ComponentData = null;

	private ComponentView m_editPanel = new ComponentView();

	public ComponentInfoDialog(IComponent comData) {
	}

	public ComponentInfoDialog(JFrame owner, String title, boolean modal,
			IComponent comData) {
		super(owner, title, modal);
		m_ComponentData = comData;
		init();
		initPanel();
		initButtons();
	}

	private void initPanel() {
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

	private void initButtons() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(creaeOkButton());
		bottomPanel.add(createBtnCancel());
		getContentPane().add(BorderLayout.SOUTH, bottomPanel);
	}

	private JButton createBtnCancel() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new AbstractAction("cancelButton") {
			private static final long serialVersionUID = 8314611518283159227L;

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		return btnCancel;
	}

	private JButton creaeOkButton() {
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new AbstractAction("okButton") {
			private static final long serialVersionUID = 6958802078999774383L;

			public void actionPerformed(ActionEvent e) {
				doAccept();
				setVisible(false);
				dispose();
			}
		});
		return btnOk;
	}

	private void init() {
		setSize(300, 400);
		WidgetFactory.placeCenter(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER,
				new JScrollPane(m_editPanel.getView()));
	}

	private void doAccept() {

		m_ComponentData.setName(m_editPanel.getComponentName());
		m_ComponentData.setIndex(Integer.parseInt(m_editPanel
				.getIndexInWindow()));
		m_ComponentData.setIndexOfSameName(Integer.parseInt(m_editPanel
				.getIndexOfSameName()));
		m_ComponentData.setText(m_editPanel.getComponentText());
		m_ComponentData.setTitle(m_editPanel.getWinTitle());

		try {
			m_ComponentData.setWinType(Class.forName(
					m_editPanel.getWinClasstype()).getName());
			m_ComponentData.setType(Class.forName(
					m_editPanel.geComponentClasstype()).getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// ComponentData d = ComponentData.getDefaultComponentData();
	// new ComponentInfoDialog(null, "test", true, d).setVisible(true);
	// System.out.println(d);
	// }
}
