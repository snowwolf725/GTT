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
package gtt.macro.view.dialog;

import gtt.editor.view.dialog.INodeDialog;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ComponentEventDialog implements INodeDialog {

	ComponentEventNode m_node;

	public ComponentEventDialog() {
	}

	public void appear() {
		new _ImplementEventNodeDialog(m_node).setVisible(true);
	}

	public AbstractMacroNode getNode() {
		return m_node;
	}

	public void setNode(Object node) {
		m_node = (ComponentEventNode) node;
	}
}

class _ImplementEventNodeDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private ComponentEventPanel m_Panel;

	public _ImplementEventNodeDialog(ComponentEventNode node) {
		this.setTitle("Component Event Node");
		this.setModal(true);
		this.setResizable(false);
		m_Panel = new ComponentEventPanel();
		m_Panel.setup(node);

		setSize(350, 440);
		WidgetFactory.placeCenter(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, m_Panel);
		initButtons();
		this.pack();
	}

	private void initButtons() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(creaeOkButton());
		bottomPanel.add(createBtnCancel());
		getContentPane().add(BorderLayout.SOUTH, bottomPanel);
	}

	private JButton createBtnCancel() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new AbstractAction("btnCancel") {
			private static final long serialVersionUID = -6375216328301226447L;
			public void actionPerformed(ActionEvent e) {
				// 使用者取消修改
				dispose();
			}
		});
		return btnCancel;
	}

	private void accept() {
		m_Panel.accept();
	}

	private JButton creaeOkButton() {
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new AbstractAction("btnOk") {
			private static final long serialVersionUID = 6770104344302110670L;
			public void actionPerformed(ActionEvent e) {
				accept();
				dispose();
			}

		});
		return btnOk;
	}

}