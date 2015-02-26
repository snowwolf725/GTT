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
package gtt.editor.view.dialog;

import gtt.editor.view.BoxTableView;
import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.testscript.EventNode;
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

class EventNodePanel extends JPanel {

	private static IEventModel m_ComponentModel = EventModelFactory
			.getDefault();

	/**
	 * default serial version uid zws 2006/10/20
	 */
	private static final long serialVersionUID = 1L;

	private final static int DATAUI_WIDTH_NAME = 120;

	private final static int DATAUI_WIDTH_TYPE = 70;

	private final static int DATAUI_WIDTH_VALUE = 100;

	private JComboBox m_ComboBoxEventType = new JComboBox();

	private BoxTableView m_BoxTable = null;

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	private List<String> m_Events = null;

	private void loadEventInformation() {
		List<IEvent> events = m_ComponentModel.getEvents(m_EventNode
				.getComponent());
		Iterator<IEvent> eite = events.iterator();
		m_Events = new Vector<String>();
		while (eite.hasNext()) {
			IEvent e = (IEvent) eite.next();
			m_Events.add(e.getName());
		}

	}

	private ComboBoxModel createtComboBoxModel() {
		return new DefaultComboBoxModel(m_Events.toArray());
	}

	private EventNode m_EventNode;

	public EventNode getEventNode() {
		return m_EventNode;
	}

	public EventNodePanel(EventNode node) {
		// 實際修改到的event node
		m_EventNode = node;

		loadEventInformation();
		m_ComboBoxEventType.setModel(createtComboBoxModel());

		initMainLayout();
		initAction();
		setupName();

		doReset();
	}

	private void initMainLayout() {
		setLayout(new BorderLayout());
		Box detailBox = Box.createVerticalBox();
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createTypePartBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(creatArgumentPartBox());
		detailBox.add(Box.createVerticalGlue());
		add(BorderLayout.CENTER, detailBox);
		add(BorderLayout.SOUTH, createButtonPartBox());
	}

	private void setupName() {
		// 替每個元件命名，才能做gui testing
		m_ComboBoxEventType.setName("CEEditor_Event");
		m_BoxTable.setName("CEEditor_Argument");
		m_ButtonApply.setName("CEEditor_Apply");
		m_ButtonReset.setName("CEEditor_Reset");
	}

	/**
	 * @param detailBox
	 */
	private Box createTypePartBox() {
		Box typeBox = Box.createHorizontalBox();
		typeBox.add(Box.createHorizontalStrut(5));
		JLabel labelType = WidgetFactory.createJLabel("Event :", 70, 14, false);
		typeBox.add(labelType);
		typeBox.add(Box.createHorizontalStrut(5));
		typeBox.add(m_ComboBoxEventType);
		typeBox.add(Box.createHorizontalGlue());
		return typeBox;
	}

	/**
	 * @param detailBox
	 */
	private Box creatArgumentPartBox() {
		Box dataBox = Box.createHorizontalBox();
		dataBox.add(Box.createHorizontalStrut(5));
		JLabel labelData = WidgetFactory.createJLabel("Arguments ", 70, 20,
				false);
		dataBox.add(labelData);
		dataBox.add(Box.createHorizontalStrut(5));

		m_BoxTable = new BoxTableView(getTitleNames(), getWidths(), 20);
		dataBox.add(m_BoxTable.getView());
		dataBox.add(Box.createHorizontalGlue());
		return dataBox;
	}

	private List<Integer> getWidths() {
		List<Integer> widthVector = new Vector<Integer>();
		widthVector.add(DATAUI_WIDTH_TYPE);
		widthVector.add(DATAUI_WIDTH_NAME);
		widthVector.add(DATAUI_WIDTH_VALUE);
		return widthVector;
	}

	private List<String> getTitleNames() {
		List<String> titleNameVector = new Vector<String>();
		titleNameVector.add("Type");
		titleNameVector.add("Name");
		titleNameVector.add("Value");
		return titleNameVector;
	}

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

	private void initAction() {
		m_ComboBoxEventType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doEventSelectionChanged();
			}
		});
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAccept();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doReset();
			}
		});
	}

	/**
	 * 當使用者從Event listing中選擇一種Event，需要更新UI
	 */
	protected void doEventSelectionChanged() {
		// 2. update BoxTable UI
		removeAll();
	}

	public void removeAll() {
		m_BoxTable.removeAll();
		IEvent event = selectedEvent();
		Iterator<Argument> ite = event.getArguments().iterator();
		while (ite.hasNext()) {
			// 產生一列(row)資訊，含type, name, editing component
			List<JComponent> row = createBoxRow((Argument) ite.next());

			m_BoxTable.addRow(row);
		}

		// 更新UI上的資訊
		m_BoxTable.getView().updateUI();
	}

	// 產生一列(row)的資訊，可放入 BoxTable 中
	private List<JComponent> createBoxRow(Argument arg) {
		List<JComponent> row = new Vector<JComponent>();
		// 含type, name, editing component
		row.add(new JLabel(arg.getType()));
		row.add(new JLabel(arg.getName()));
		row.add(createEditingComponent(arg.getType()));
		return row;
	}

	// create a editing component
	private JComponent createEditingComponent(String type) {
		if (type.equals("MouseButton"))
			// 有東西可以列舉
			return new JComboBox(createMouseButtonEnumItem());

		if (type.equals("Modifier"))
			// 有東西可以列舉
			return new JComboBox(BoxTableView.modifierItems().toArray());

		// 其它情況下，就使用文字框
		return new JTextField();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector createMouseButtonEnumItem() {
		Vector items = new Vector();
		items.add("Left");
		items.add("Middle");
		items.add("Right");
		return items;
	}

	/*
	 * 取得使用者在combobox 中選到的IEvent
	 */
	private IEvent selectedEvent() {
		String event_name = (String) m_ComboBoxEventType.getSelectedItem();
		List<IEvent> events = m_ComponentModel.getEvents(m_EventNode
				.getComponent());
		Iterator<IEvent> eite = events.iterator();
		while (eite.hasNext()) {
			IEvent e = (IEvent) eite.next();
			if (e.getName().equals(event_name))
				return e;
		}
		return null;
	}

	/**
	 * 從EventNode 中取得資料來更新UI的顯示
	 */
	private void doReset() {
		if (m_EventNode == null)
			return;

		resetEventSelected();
		resetArgumentUI();

		updateUI();
	}

	// 從原本的IEvent 來更新 BoxTable上的參數值
	private void resetArgumentUI() {
		int rct = m_BoxTable.getRowCount();
		for (int i = 0; i < rct; ++i) {
			String argName = m_BoxTable.getValue(i, 1);
			String argValue;
			try {
				argValue = m_EventNode.getEvent().getArguments().find(
						argName).getValue();
			} catch (NullPointerException nep) {
				argValue = "";
			}
			m_BoxTable.setValue(argValue, i, 2);
		}
	}

	private void resetEventSelected() {
		for (int i = 0; i < m_Events.size(); ++i) {
			if (m_Events.get(i).equals(m_EventNode.getEvent().getName())) {
				m_ComboBoxEventType.setSelectedIndex(i);
				break; // exit for
			}
		}
	}

	// 將boxtable上的資料寫回 event
	private void doAccept() {
		if (m_EventNode == null)
			return;

		// 產生複本
		IEvent e = selectedEvent().clone();

		// 更新argument 值
		for (int i = 0; i < m_BoxTable.getRowCount(); ++i) {
			String name = m_BoxTable.getValue(i, 1);
			String value = m_BoxTable.getValue(i, 2);

			try {
				e.getArguments().find(name).setValue(value);
			} catch (NullPointerException nep) {
				// nothing to recover
			}
		}

		m_EventNode.setEvent(e);
	}

}
