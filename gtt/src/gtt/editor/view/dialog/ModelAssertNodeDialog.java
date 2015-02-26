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

import gtt.testscript.AbstractNode;
import gtt.testscript.ModelAssertNode;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ModelAssertNodeDialog implements INodeDialog {

	public void appear() {
		new _ImplementMANodeDialog(m_node).setVisible(true);
	}

	private ModelAssertNode m_node;

	public AbstractNode getNode() {
		return m_node;
	}

	public void setNode(Object node) {
		m_node = (ModelAssertNode) node;
	}

}


class _ImplementMANodeDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	ModelAssertPanel m_panel;

	ModelAssertNode m_originalMANode;

	public _ImplementMANodeDialog(ModelAssertNode node) {
		init();
		m_originalMANode = node; // 正本

		// 使用clone 讓panel修改到event node的複本即可
		m_panel = new ModelAssertPanel(node.clone());

		getContentPane().add(BorderLayout.CENTER, m_panel);
		initButtons();
		this.pack();
	}

	private void init() {
		this.setTitle("Model Assertion Node");
		this.setModal(true);
		this.setResizable(false);
		setSize(300, 350);
		WidgetFactory.placeCenter(this);
		getContentPane().setLayout(new BorderLayout());
	}

	private void initButtons() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(creaeOkButton());
		bottomPanel.add(createBtnCancel());
		getContentPane().add(BorderLayout.SOUTH, bottomPanel);
	}

	private JButton createBtnCancel() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new AbstractAction("cancelButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				// 使用者取消修改
				setVisible(false);
				dispose();
			}
		});
		return btnCancel;
	}

	private JButton creaeOkButton() {
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new AbstractAction("okButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				doAccept();
				setVisible(false);
				dispose();
			}
		});
		return btnOk;
	}

	private void doAccept() {
		// 使用者確定修改，要將複本更新回正本
		m_originalMANode.setClassPath(m_panel.getModelAssertNode()
				.getClassPath());
		m_originalMANode.setAssertMethod(m_panel.getModelAssertNode()
				.getAssertMethod());
	}

}