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

import gtt.editor.view.BoxTableView;
import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.view.IMacroTree;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

class ComponentEventPanel extends JPanel implements IDataNodePanel {
	/**
	 * default serial version uid zws 2006/10/20
	 */
	private static final long serialVersionUID = 1L;

	private IEventModel m_ComponentModel = EventModelFactory.getDefault();

	private JTree m_curMacroEventTree = null;

	private ComponentEventNode m_DataNode = null;

	private List<IEvent> m_Events = null;

	public void setup(AbstractMacroNode node) {
		acceptData(node, null);
	}

	public boolean isAccept(AbstractMacroNode node, JTree curMacroEventTree,
			IMacroTree outerMacroJTree, DefaultMutableTreeNode curMacroEventRoot) {
		return acceptData(node, curMacroEventTree);
	}

	private boolean acceptData(AbstractMacroNode node, JTree curMacroEventTree) {
		if (node == null)
			return false;
		// 只編輯 ComponentEventNode
		if (!(node instanceof ComponentEventNode))
			return false;
		m_DataNode = (ComponentEventNode) node;
		m_curMacroEventTree = curMacroEventTree;
		// 從swing model中讀取資訊
		loadingEventInformation();
		resetUI();
		return true;
	}

	private void loadingEventInformation() {
		if (m_ComponentModel == null)
			return;
		m_Events = m_ComponentModel.getEvents(m_ComponentModel
				.getComponent(m_DataNode.getComponent().getType()));
	}

	private void resetUI() {
		// 列出 IEvent list ，讓使用者可以選擇event
		m_ComboBoxEventType.setModel(new DefaultComboBoxModel(m_Events
				.toArray()));
		if (m_DataNode.getArguments().size() == 0) {
			// 更新參數
			updateArgumentBoxTableUI();
		}
		updateDataUI();
		updateUI();
	}

	// private JLabel m_LabelNameContext = new JLabel();

	private JComboBox m_ComboBoxEventType = new JComboBox();

	private BoxTableView m_ArgumentBoxTable = null;

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	public ComponentEventPanel() {
		setLayout(new BorderLayout());
		// ===initial part===
		Box detailBox = Box.createVerticalBox();
		detailBox.add(createTypePartBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createDataPartBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createButtonPartBox());
		// ==================
		add(BorderLayout.CENTER, detailBox);

		initAction();
		initName();
	}

	private void initName() {
		// m_LabelNameContext.setName("CEEditor_LabelName");
		m_ComboBoxEventType.setName("CEEditor_Event");
		m_ArgumentBoxTable.setName("CEEditor_Argument");
		m_ButtonApply.setName("CEEditor_Apply");
		m_ButtonReset.setName("CEEditor_Reset");
	}

	// /**
	// * @param detailBox
	// */
	// private Box createNamePartBox() {
	// Box nameBox = Box.createHorizontalBox();
	// nameBox.add(Box.createHorizontalStrut(5));
	// JLabel labelName = WidgetFactory.createJLabel("Name", 70, 20, false);
	// nameBox.add(labelName);
	// nameBox.add(Box.createHorizontalStrut(5));
	// Box contextBox = Box.createVerticalBox();
	// m_LabelNameContext = WidgetFactory.createJLabel("", 330, 20, false);
	// contextBox.add(m_LabelNameContext);
	// nameBox.add(contextBox);
	// nameBox.add(Box.createHorizontalGlue());
	// return nameBox;
	// }

	/**
	 * @param detailBox
	 */
	private Box createButtonPartBox() {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(75));
		WidgetFactory.setupJButton(m_ButtonApply, "Apply", 80, 20);
		buttonBox.add(m_ButtonApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, "Reset", 80, 20);
		buttonBox.add(m_ButtonReset);
		buttonBox.add(Box.createHorizontalGlue());
		return buttonBox;
	}

	/**
	 * @param detailBox
	 */
	private Box createDataPartBox() {
		Box dataBox = Box.createHorizontalBox();
		dataBox.add(Box.createHorizontalStrut(5));
		JLabel lbl = WidgetFactory.createJLabel("Argument", 70, 20, false);
		dataBox.add(lbl);
		dataBox.add(Box.createHorizontalStrut(5));

		m_ArgumentBoxTable = BoxTableView.createArgumentBoxView();
		dataBox.add(m_ArgumentBoxTable.getView());
		dataBox.add(Box.createHorizontalGlue());
		return dataBox;
	}

	/**
	 * @param detailBox
	 */
	private Box createTypePartBox() {
		Box typeBox = Box.createHorizontalBox();
		typeBox.add(Box.createHorizontalStrut(5));
		JLabel labelType = WidgetFactory.createJLabel("Event", 70, 20, false);
		typeBox.add(labelType);
		typeBox.add(Box.createHorizontalStrut(5));
		typeBox.add(m_ComboBoxEventType);

		typeBox.add(Box.createHorizontalGlue());
		return typeBox;
	}

	private void initAction() {
		m_ComboBoxEventType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentBoxTableUI();
			}
		});

		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataToNode();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataUI();
			}
		});
	}

	private void updateArgumentBoxTableUI() {
		m_ArgumentBoxTable.removeAll();
		IEvent ie = selectedIEvent();
		Iterator<Argument> ite = ie.getArguments().iterator();
		while (ite.hasNext()) {
			m_ArgumentBoxTable.addRow(createArgumentRow(ite.next()));
		}

		// 更新UI上的資訊
		m_ArgumentBoxTable.getView().updateUI();
	}

	// 產生一列(row)的資訊，可放入 BoxTable 中
	private List<JComponent> createArgumentRow(Argument arg) {
		List<JComponent> row = new Vector<JComponent>();
		// 含type, name, editing component
		row.add(new JLabel(arg.getType()));
		row.add(new JLabel(arg.getName()));
		// value
		row.add(new JTextField(arg.getValue()));

		return row;
	}

	private IEvent selectedIEvent() {
		int idx = m_ComboBoxEventType.getSelectedIndex();
		if (idx < 0)
			return null; // no item be selected

		return (IEvent) m_Events.get(idx).clone();
	}

	/**
	 * 從data node 中取得資料來更新UI的顯示
	 */
	private void updateDataUI() {
		updateSelectedIEvent();

		int rct = m_ArgumentBoxTable.getRowCount();
		for (int i = 0; i < rct; ++i) {
			String name = m_ArgumentBoxTable.getValue(i, 1);
			String value = m_DataNode.getArguments().getValue(name);
			m_ArgumentBoxTable.setValue(value, i, 2);
		}

		updateUI();
	}

	private void updateSelectedIEvent() {
		for (int i = 0; i < m_Events.size(); ++i) {
			IEvent ie = (IEvent) m_Events.get(i);
			if (ie.getName().equals(m_DataNode.getEventType())) {
				m_ComboBoxEventType.setSelectedIndex(i);
				break;
			}
		}
	}

	public void accept() {
		updateDataToNode();
	}

	private void updateDataToNode() {
		m_DataNode.setEvent(selectedIEvent().getName(), selectedIEvent()
				.getEventId());
		// update arguments
		m_DataNode.getArguments().clear();
		for (int i = 0; i < m_ArgumentBoxTable.getRowCount(); ++i) {
			String type = m_ArgumentBoxTable.getValue(i, 0);
			String name = m_ArgumentBoxTable.getValue(i, 1);
			String value = m_ArgumentBoxTable.getValue(i, 2);
			m_DataNode.getArguments()
					.add(Argument.create(type, name, value));
		}
		// update MacroTree ui
		if (m_curMacroEventTree != null)
			m_curMacroEventTree.updateUI();
	}
}
