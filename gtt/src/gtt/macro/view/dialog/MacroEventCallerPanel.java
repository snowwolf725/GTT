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
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.view.IMacroTree;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author 哲銘 這個class是節點編輯介面的class,用來編輯一個macro事件節點
 */
class MacroEventCallerPanel extends JPanel implements IDataNodePanel {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

//	private final static String LABEL_NAME = "Name:";
//
//	private final static String LABEL_DESCRIPT = "Event Node of Macro Component";

	private final static String LABEL_EVENT = "Type:";

	private final static String LABEL_ARGUMENT = "Argument:";

	private final static String BUTTON_APPLY = "Apply";

	private final static String BUTTON_RESET = "Reset";

	public final static int ARGUI_WIDTH_NAME = 100;

	public final static int ARGUI_WIDTH_TYPE = 100;

	public final static int ARGUI_WIDTH_VALUE = 100;

	private JTree m_hereMacroTree;

	private MacroEventCallerNode m_DataNode = null;

	private List<MacroEventNode> m_MacroEvents = null;

	private JLabel m_LabelNameContext = new JLabel();

	private JComboBox m_cbMacroEvent = new JComboBox();

	private BoxTableView m_BoxTable = null;

	private JButton m_btnApply = new JButton();

	private JButton m_btnReset = new JButton();

	public boolean isAccept(AbstractMacroNode data, JTree hereTree,
			IMacroTree outTree, DefaultMutableTreeNode hereMacroEvent) {

		if (data == null)
			return false;

		if (!(data instanceof MacroEventCallerNode))
			return false;

		m_hereMacroTree = hereTree;
		m_DataNode = (MacroEventCallerNode) data;

		// 取得可以使用的 MacroEvent，必須要提供正確的 Macro root
		// zws 2007/07/10
		AbstractMacroNode outRoot = (AbstractMacroNode) outTree.getModelRoot()
				.getUserObject();
		// reference 的parent 一定是 MacroComponentNode
		MacroComponentNode mc = (MacroComponentNode) m_DataNode.getReference(
				outRoot).getParent();

		// 取得 MacroComponent 時，就能列舉出所有可選擇的 MacroEvent
		m_MacroEvents = mc.getMacroEvents();

		resetUI();
		return true;
	}

	private void resetUI() {
		resetEventTypeComboBox();
		if (m_DataNode.getEventName().equals("")) {
			// 沒有選到macro event
			selectMacroEvent();
			updateNameView();
		} else {
			updateView();
		}
		updateUI();
	}

	private void resetEventTypeComboBox() {
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel(m_MacroEvents
				.toArray());
		m_cbMacroEvent.setModel(comboBoxModel);
	}

	public MacroEventCallerPanel() {
		// m_EventMaintenance=eventMaintenance;
		setLayout(new BorderLayout());
		// ===initial part===
		Box detailBox = Box.createVerticalBox();
//		initNamePart(detailBox);
//		detailBox.add(Box.createVerticalStrut(5));
		initEventPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initArgPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initButtonPart(detailBox);
		detailBox.add(Box.createVerticalStrut(200));
		// ==================
		add(BorderLayout.CENTER, detailBox);

		initAction();
		initName();
	}

	private void initName() {
		m_LabelNameContext.setName("AMEEditor_LabelName");
		m_cbMacroEvent.setName("AMEEditor_Event");
		m_BoxTable.setName("AMEEditor_Argument");
		m_btnApply.setName("AMEEditor_Apply");
		m_btnReset.setName("AMEEditor_Reset");
	}

//	/**
//	 * @param detailBox
//	 */
//	private void initNamePart(Box detailBox) {
//		Box nameBox = Box.createHorizontalBox();
//		nameBox.add(Box.createHorizontalStrut(5));
//		JLabel labelName = WidgetFactory
//				.createJLabel(LABEL_NAME, 70, 20, false);
//		nameBox.add(labelName);
//		nameBox.add(Box.createHorizontalStrut(5));
//		Box contextBox = Box.createVerticalBox();
//		JLabel labelNameDescript = WidgetFactory.createJLabel(LABEL_DESCRIPT,
//				330, 20, false);
//		contextBox.add(labelNameDescript);
//		m_LabelNameContext = WidgetFactory.createJLabel("", 330, 20, false);
//		contextBox.add(m_LabelNameContext);
//		nameBox.add(contextBox);
//		nameBox.add(Box.createHorizontalGlue());
//		detailBox.add(nameBox);
//	}

	/**
	 * @param detailBox
	 */
	private void initEventPart(Box detailBox) {
		Box typeBox = Box.createHorizontalBox();
		typeBox.add(Box.createHorizontalStrut(5));
		JLabel labelType = WidgetFactory.createJLabel(LABEL_EVENT, 70, 20,
				false);
		typeBox.add(labelType);
		typeBox.add(Box.createHorizontalStrut(5));
		typeBox.add(m_cbMacroEvent);
		typeBox.add(Box.createHorizontalGlue());
		detailBox.add(typeBox);
	}

	/**
	 * @param detailBox
	 */
	private void initArgPart(Box detailBox) {
		Box dataBox = Box.createHorizontalBox();
		dataBox.add(Box.createHorizontalStrut(5));
		JLabel labelData = WidgetFactory.createJLabel(LABEL_ARGUMENT, 70, 20,
				false);
		dataBox.add(labelData);
		dataBox.add(Box.createHorizontalStrut(5));

		m_BoxTable = new BoxTableView(getTitleNames(), getWidths(), 20);
		dataBox.add(m_BoxTable.getView());
		// UpdateDataValueUIForType(70,70,70);
		dataBox.add(Box.createHorizontalGlue());
		detailBox.add(dataBox);
	}

	private List<Integer> getWidths() {
		List<Integer> widthVector = new Vector<Integer>();
		widthVector.add(ARGUI_WIDTH_TYPE);
		widthVector.add(ARGUI_WIDTH_NAME);
		widthVector.add(ARGUI_WIDTH_VALUE);
		return widthVector;
	}

	private Vector<String> getTitleNames() {
		Vector<String> titleNameVector = new Vector<String>();
		titleNameVector.add("Type");
		titleNameVector.add("Name");
		titleNameVector.add("Value");
		return titleNameVector;
	}

	/**
	 * @param detailBox
	 */
	private void initButtonPart(Box detailBox) {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(75));
		WidgetFactory.setupJButton(m_btnApply, BUTTON_APPLY, 80, 20);
		buttonBox.add(m_btnApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_btnReset, BUTTON_RESET, 80, 20);
		buttonBox.add(m_btnReset);
		buttonBox.add(Box.createHorizontalGlue());
		detailBox.add(buttonBox);
	}

	private void initAction() {
		m_cbMacroEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMacroEvent();
			}
		});
		m_btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataToNode();
			}
		});
		m_btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateView();
			}
		});
	}

	private void updateNameView() {
		if (m_DataNode == null)
			return;

		m_LabelNameContext.setText(m_DataNode.getName());
		m_LabelNameContext.updateUI();
	}

	private void selectMacroEvent() {
		m_BoxTable.removeAll();
		if (m_MacroEvents == null || m_MacroEvents.size() < 1)
			return;

		MacroEventNode me = selectedEvent();
		// setup reference path
		m_DataNode.setReferencePath(me.getPath().toString());

		// update arguments ui
		updateArgumentPart(me);

		updateUI();
	}

	private MacroEventNode selectedEvent() {
		int index = m_cbMacroEvent.getSelectedIndex();
		return m_MacroEvents.get(index);
	}

	private void updateArgumentPart(MacroEventNode me) {
		Iterator<Argument> ite = me.getArguments().iterator();
		while (ite.hasNext()) {
			Argument arg = ite.next();
			List<JComponent> argUIs = new Vector<JComponent>();
			// label for type
			argUIs.add(new JLabel(arg.getType()));
			// label for name
			argUIs.add(new JLabel(arg.getName()));
			// 以 JTextField 來編輯 value
			argUIs.add(new JTextField(arg.getValue()));

			m_BoxTable.addRow(argUIs);
		}
	}

	private void updateView() {
		updateNameView();
		if (m_DataNode.getEventName().equals(""))
			return;

		// 在combobox上選定所呼叫的 Macro event
		String nameOfMacroEvent = m_DataNode.getEventName();
		int idxOfEvent = -1;
		for (int idx = 0; idx < m_cbMacroEvent.getItemCount(); idx++) {
			// 檢查 event name 是不是相同
			String e = m_cbMacroEvent.getItemAt(idx).toString();
			// 檢查 arguments 個數是否一樣
			int startOfArgument = e.indexOf("(");
			String namePart = e.substring(0, startOfArgument);
			if (!nameOfMacroEvent.toLowerCase().equals(namePart.toLowerCase()))
				continue;
			
			idxOfEvent = idx;
			break;
			
			// 不比較參數
//			int endOfArgument = e.indexOf(")");
//			String arguments = e.substring(startOfArgument, endOfArgument);
//			int arg_size = arguments.split(",").length;
//			
//			if (arg_size == m_DataNode.getArgumentList().size()) {
//				idxOfEvent = idx;
//				break;
//			}
		}

		if (idxOfEvent == -1)
			idxOfEvent = 0; // 都沒有符合的，就選擇第一個

		m_cbMacroEvent.setSelectedIndex(idxOfEvent);

		// 取出值來，放到boxtable中
		Iterator<Argument> ite = m_DataNode.getArguments().iterator();
		int ct = 0;
		while (ite.hasNext()) {
			Argument arg = ite.next();
			m_BoxTable.setValue(arg.getValue(), ct, 2);
			ct++;
		}
		updateUI();
	}

	private void updateDataToNode() {
		m_DataNode.getArguments().clear();
		for (int i = 0; i < m_BoxTable.getRowCount(); ++i) {
			String type = m_BoxTable.getValue(i, 0);
			String name = m_BoxTable.getValue(i, 1);
			String value = m_BoxTable.getValue(i, 2);
			Argument arg = Argument.create(type, name, value);
			m_DataNode.getArguments().add(arg);
		}
		m_hereMacroTree.updateUI();
	}
}
