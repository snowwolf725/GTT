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
 * Created on 2005/5/19
 */
package gtt.macro.view.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.view.IMacroTree;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

class SleeperPanel extends JPanel implements IDataNodePanel {
	private static final long serialVersionUID = 1L;

	private JTree m_EventListTreeView = null;
	private SleeperNode m_DataNode = null;

	public boolean isAccept(AbstractMacroNode node, JTree eventListTreeView,
			IMacroTree macroTree, DefaultMutableTreeNode parentMacroNode) {
		if (node == null)
			return false;
		if (!(node instanceof SleeperNode))
			return false;

		m_EventListTreeView = eventListTreeView;
		m_DataNode = (SleeperNode) node;

		updateDataUIForNode();
		return true;
	}

	// private JLabel m_LabelNameContext=new JLabel();
	private JTextField m_SleepTimeTextField = new JTextField();

	private JButton m_ButtonClassUrlBrowser = new JButton();

	private JComboBox m_ComboBoxMethod = new JComboBox();

	private JButton m_ButtonRefresh = new JButton();

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	public SleeperPanel() {
		setLayout(new BorderLayout());
		Box detailBox = Box.createVerticalBox();
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(initSleepTimePart());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(initButtonPart());
		detailBox.add(Box.createVerticalGlue());
		// ==================
		add(BorderLayout.CENTER, detailBox);

		initAction();
		initName();
	}

	private void initName() {
		m_SleepTimeTextField.setName("CFAEditor_UrlText");
		m_ButtonClassUrlBrowser.setName("CFAEditor_Browser");
		m_ComboBoxMethod.setName("CFAEditor_Method");
		m_ButtonRefresh.setName("CFAEditor_Refresh");
		m_ButtonApply.setName("CFAEditor_Apply");
		m_ButtonReset.setName("CFAEditor_Reset");
	}

	private Box initSleepTimePart() {
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(5));
		JLabel labelName = 	WidgetFactory.createJLabel( "Sleep Time", 80, 20, false);
		box.add(labelName);
		box.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJTextComponent(m_SleepTimeTextField, "", 330, 20);
		box.add(m_SleepTimeTextField);
		box.add(Box.createHorizontalGlue());
		return box;
	}

	private Box initButtonPart() {
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonApply, "Apply", 80, 20);
		box.add(m_ButtonApply);
		box.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, "Reset", 80, 20);
		box.add(m_ButtonReset);
		box.add(Box.createHorizontalGlue());
		return box;
	}

	public void initAction() {
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataToNode();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataUIForNode();
			}
		});
	}

	protected void updateDataToNode() {
		String time = m_SleepTimeTextField.getText();
		m_DataNode.setSleepTime(Long.parseLong(time));
		m_EventListTreeView.updateUI();
	}

	protected void updateDataUIForNode() {
		if (m_DataNode == null)
			return;
		m_SleepTimeTextField.setText(""+m_DataNode.getSleepTime());
		updateUI();
	}
}
