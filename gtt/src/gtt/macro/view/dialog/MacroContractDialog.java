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
 * Created on 2005/4/7
 */
package gtt.macro.view.dialog;

import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.view.IMacroTree;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MacroContractDialog extends JDialog {
	/**
	 * default serial version UID
	 */
	static final long serialVersionUID = 1L;

	private JTextArea m_Level = new JTextArea(1, 50);
	private JTextArea m_PreCondition = new JTextArea(8, 50);
	private JTextArea m_Action = new JTextArea(8, 50);
	private JTextArea m_PostCondition = new JTextArea(8, 50);

	protected JButton m_btnOK = new JButton("Ok");

	private JButton m_btnCancel = new JButton("Cancel");

	private MacroEventNode m_MacroEvent = null;

	private IMacroTree m_MacroTree = null;

	public MacroContractDialog(IMacroTree mtree, MacroEventNode me) {
		m_MacroTree = mtree;
		m_MacroEvent = me;
		
		m_PreCondition.setLineWrap(true);
		m_PreCondition.setWrapStyleWord(true);
		m_Action.setLineWrap(true);
		m_Action.setWrapStyleWord(true);
		m_PostCondition.setLineWrap(true);
		m_PostCondition.setWrapStyleWord(true);

		initDialog();
	}

	private void initDialog() {
		setTitle("Contract Editor - " + m_MacroEvent.toString());

		setSize(590, 650);
		WidgetFactory.placeCenter(this);
		setResizable(true);

		initLayout();
		initDialogAction();
		initDialogName();

		// setup contract
		m_PreCondition.setText(m_MacroEvent.getContract().getPreCondition());
		m_PostCondition.setText(m_MacroEvent.getContract().getPostCondition());
		m_Action.setText(m_MacroEvent.getContract().getAction());
		m_Level.setText("" + m_MacroEvent.getContract().getLevel());
		m_Level.setEditable(false);
	}

	private void initLayout() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(BorderLayout.CENTER, createMainInfoPanel());
		mainPanel.add(BorderLayout.SOUTH, createMainButtonPanel());
		getContentPane().add(BorderLayout.CENTER, mainPanel);
	}

	private void initDialogName() {
		m_PreCondition.setName("TextArea_PreCondition");
		m_Action.setName("TextArea_Action");
		m_PostCondition.setName("TextArea_PostCondtion");
		m_Level.setName("TextArea_Level");

		m_btnOK.setName("MEEditor_OK");
		m_btnCancel.setName("MEEditor_Cancel");
		// m_hereMacroEventTree.setName("MEEditor_EventTree");
	}

	@SuppressWarnings("serial")
	private void initDialogAction() {
		m_btnOK.addActionListener(new AbstractAction("finishButton") {
			public void actionPerformed(ActionEvent e) {
				doOK();
			}
		});

		m_btnCancel.addActionListener(new AbstractAction("cancelButton") {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});

	}

	private JPanel createMainInfoPanel() {
		JPanel p0 = new JPanel();
		p0.setBorder(new TitledBorder(new EtchedBorder(), "Level"));
		p0.add(BorderLayout.CENTER, m_Level);

		JScrollPane p1 = new JScrollPane(m_PreCondition);
		p1.setBorder(new TitledBorder(new EtchedBorder(), "Pre-Condition"));
//		p1.add();

		JScrollPane p2 = new JScrollPane(m_Action);
		p2.setBorder(new TitledBorder(new EtchedBorder(), "Action"));
//		p2.add();

		JScrollPane p3 = new JScrollPane(m_PostCondition);
		p3.setBorder(new TitledBorder(new EtchedBorder(), "Post-Condition"));
//		p3.add();

		JPanel panel = new JPanel(new FlowLayout());
		// panel.setLayout();
		panel.add(p0);
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		// panel.add(BorderLayout.NORTH, p1);
		// panel.add(BorderLayout.CENTER, p2);
		// panel.add(BorderLayout.SOUTH, p3);
		// panel.add(m_PanelSelector.getMainPanel());
		return panel;
	}

	private JPanel createMainButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		btnPanel.add(BorderLayout.WEST, m_btnOK);
		btnPanel.add(BorderLayout.EAST, m_btnCancel);
		buttonPanel.add(BorderLayout.EAST, btnPanel);

		return buttonPanel;
	}

	private void doOK() {
		m_MacroEvent.getContract().setPreCondition(m_PreCondition.getText());
		m_MacroEvent.getContract().setAction(m_Action.getText());
		m_MacroEvent.getContract().setPostCondition(m_PostCondition.getText());
//		m_MacroEvent.getContract()
//				.setLevel(Integer.parseInt(m_Level.getText()));

		m_MacroTree.updateUI();
		this.dispose();
	}

	private void doCancel() {
		this.dispose();
	}

}
