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
package gtt.editor.view;

import gtt.util.swing.WidgetFactory;

import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class BoxTableView implements IView {

	private JScrollPane m_panel = new JScrollPane();
	private JPanel m_ParentPanel = null;
	private Box m_BoxTable = null;

	private static final int SCROLLBAR_SIZE = 18;

	private static final int DEFAULT_COLUMN_WIDTH = 70;

	private static final int DEFAULT_ROW_HEIGHT = 15;

	private static final int DEFAULT_MAX_HEIGHT = 120;

	private int m_MaxHeight = DEFAULT_MAX_HEIGHT;

	private int m_RowHeight = DEFAULT_ROW_HEIGHT;

	private int m_RowCount = 0;

	private int m_ColumnCount = 0;

	private List<Integer> m_ColumnWidth = null;

	public static BoxTableView createArgumentBoxView(JPanel macroPanel) {
		List<String> titleNameVector = new Vector<String>();
		titleNameVector.add("Type");
		titleNameVector.add("Name");
		titleNameVector.add("Value");

		List<Integer> widths = new Vector<Integer>();
		widths.add(80);
		widths.add(80);
		widths.add(140);

		return new BoxTableView(macroPanel, titleNameVector, widths, 20);
	}

	public static BoxTableView createArgumentBoxView() {
		return createArgumentBoxView(null);
	}

	/*
	 * �Τ@���}�C�x�s�G��box table�����
	 */
	private List<JComponent> m_CellValues = new Vector<JComponent>();

	public BoxTableView(List<String> titleName, List<Integer> columnWidth,
			int rowHeight) {
		// ��Υt�@��constructor
		this(null, titleName, null, rowHeight);
	}

	public BoxTableView(JPanel parentPanel, List<String> titleName,
			List<Integer> columnWidth, int rowHeight) {
		m_panel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		m_ParentPanel = parentPanel;
		m_RowHeight = rowHeight;

		if (columnWidth == null)
			initColumnWidthByName(titleName);
		else
			initColumnWidthByWidth(columnWidth);

		m_panel.setViewportView(createMainBox(titleName));
	}

	public boolean addRow(List<JComponent> aRow) {
		if (aRow == null)
			return false;

		if (aRow.size() != m_ColumnCount) {
			System.out.println("BoxTableView - Size doesn't match: " + aRow.size() + ":"
					+ m_ColumnCount);
			// size ���X�N����
			return false;
		}

		m_BoxTable.add(createRowData(aRow));
		m_RowCount++;

		updateCellSize();
		updateParentUI();
		return true;
	}

	public boolean addNewRow() {
		return addRow(createTableRow());
	}

	// GTML �� loosely-typed language
	// 2008/06/08 -zws
	public static final String TYPE = "Variant";

	private List<JComponent> createTableRow() {
		List<JComponent> v = new Vector<JComponent>();
		v.add(new JLabel(TYPE)); // type
		v.add(new JTextField()); // name
		v.add(new JTextField()); // value
		return v;
	}

	/*
	 * �NaROW�̪�����ഫBox�n�s�񪺤@�C��T zws 2006/10/20
	 */
	private Box createRowData(List<JComponent> theRow) {
		Box rowData = Box.createHorizontalBox();
		for (int col = 0; col < theRow.size(); ++col) {
			if (!(theRow.get(col) instanceof JComponent)) {
				System.out.println("[BoxTableView] Not a JComponent element");
				continue; // �J����~�A���ӭn���L
			}
			JComponent com = (JComponent) theRow.get(col);
			// �Ӥ���]�wname�A�i�H��GUI testing
			com.setName(m_panel.getName() + "_r" + m_RowCount + "_c" + col);

			WidgetFactory.setupBorder(com, 1);
			WidgetFactory.setupSize(com, m_ColumnWidth.get(col).intValue(),
					m_RowHeight);

			rowData.add(com);
			m_CellValues.add(com);
		}
		return rowData;
	}

	public boolean removeLastRow() {
		if (m_RowCount <= 0)
			return false;
		m_BoxTable.remove(m_BoxTable.getComponentCount() - 1);

		int iter = m_RowCount * m_ColumnCount - 1;
		for (int i = iter; i > iter - m_ColumnCount; --i) {
			m_CellValues.remove(i);
		}

		m_RowCount--;
		updateParentUI();
		return true;
	}

	public void removeAll() {
		m_RowCount = 0;

		// �U�C���O���Ъ�
		m_CellValues.clear();
		m_CellValues = new Vector<JComponent>();

		m_BoxTable.removeAll();

		updateParentUI();
	}

	public int getRowCount() {
		return m_RowCount;
	}

	public int getColumnSize() {
		return m_ColumnCount;
	}

	public void setMaxHeight(int h) {
		if (h < m_RowHeight + 10) // ����n + 10
			return;

		m_MaxHeight = h;
	}

	public boolean setValue(String value, int row, int column) {
		if (value == null)
			return false;

		if (!inRange(row, column))
			return false;

		JComponent com = getJComponent(row, column);
		if (com == null)
			return false;
		if (com instanceof JLabel) {
			((JLabel) com).setText(value);
			return true;
		}

		if (com instanceof JTextField) {
			((JTextField) com).setText(value);
			return true;
		}

		if (com instanceof JComboBox) {
			JComboBox cb = (JComboBox) com;
			for (int i = 0, size = cb.getItemCount(); i < size; ++i) {
				Object item = cb.getItemAt(i);
				if (item.toString().equals(value)) {
					cb.setSelectedIndex(i);
					return true;
				}
			}
		}

		// �L�k�]�wvalue
		return true;
	}

	// �C��Cell���O�@��JComponent
	private JComponent getJComponent(int row, int col) {
		return (JComponent) getCell(row, col);
	}

	private Object getCell(int row, int col) {
		if (!inRange(row, col))
			return null;

		return m_CellValues.get(row * m_ColumnCount + col);
	}

	public String getValue(int row, int col) {
		if (!inRange(row, col))
			return null;

		JComponent com = getJComponent(row, col);
		if (com instanceof JLabel) {
			return ((JLabel) com).getText();
		}

		if (com instanceof JTextField) {
			return ((JTextField) com).getText();
		}

		if (com instanceof JComboBox) {
			return ((JComboBox) com).getSelectedItem().toString();
		}

		if (com instanceof JCheckBox) {
			return ((JCheckBox) com).isSelected() == true ? "true" : "false";
		}

		return "[EMPTY]"; // no value
	}

	// �ˬd row, col �O�_�W�Xrow range, col range
	private boolean inRange(int row, int column) {
		return inRowRange(row) && inColumnRange(column);
	}

	// �ˬd row �O�_�W�Xrow range
	private boolean inRowRange(int row) {
		return (row >= 0 && row < m_RowCount);
	}

	// �ˬd col �O�_�W�Xcol range
	private boolean inColumnRange(int col) {
		return (col >= 0 && col < m_ColumnCount);
	}

	private Box createMainBox(List<String> titleName) {
		m_ColumnCount = titleName.size();

		Box mainBox = Box.createVerticalBox();
		mainBox.add(createTitleBox(titleName));

		m_BoxTable = Box.createVerticalBox();
		mainBox.add(m_BoxTable);

		updateCellSize();
		return mainBox;
	}

	private Box createTitleBox(List<String> titleName) {
		Box titleBox = Box.createHorizontalBox();
		for (int i = 0; i < titleName.size(); ++i) {
			JLabel label = WidgetFactory.createJLabel(titleName.get(i),
					m_ColumnWidth.get(i).intValue(), m_RowHeight, true);
			WidgetFactory.setupBorder(label, 2);
			titleBox.add(label);
		}
		return titleBox;
	}

	/*
	 * �]�w�C�@��colum���e��(width) zws 2006/10/20
	 */
	private void initColumnWidthByName(List<String> titleName) {
		m_ColumnWidth = new Vector<Integer>();
		for (int i = 0; i < titleName.size(); ++i)
			m_ColumnWidth.add(DEFAULT_COLUMN_WIDTH);
	}

	/*
	 * �]�w�C�@��colum���e��(width) zws 2006/10/20
	 */
	private void initColumnWidthByWidth(List<Integer> columnWidth) {
		// create by columnWidth
		m_ColumnWidth = new Vector<Integer>(columnWidth);
	}

	private void updateCellSize() {
		int totalHeight = calcTotalHeight();
		int totalWidth = calcTotalWidth();

		Dimension d = new Dimension(totalWidth, totalHeight);
		m_panel.setSize(d);
		m_panel.setPreferredSize(d);
		m_panel.setMinimumSize(d);
		m_panel.setMaximumSize(d);
	}

	private int calcTotalWidth() {
		int totalWidth = SCROLLBAR_SIZE;
		for (int i = 0; i < m_ColumnWidth.size(); ++i)
			totalWidth += m_ColumnWidth.get(i).intValue();
		return totalWidth;
	}

	private int calcTotalHeight() {
		int totalHeight = m_RowHeight * m_RowCount + m_RowHeight + 3;
		if (totalHeight > m_MaxHeight)
			return m_MaxHeight;

		return totalHeight;
	}

	private void updateParentUI() {
		if (m_ParentPanel != null)
			m_ParentPanel.updateUI();
	}

	public void setName(String name) {
		// �����O�@��JScrollPanel�A�ݭn�@�ӦW�r
		m_panel.setName(name);
		m_BoxTable.setName(name + "BoxTable");

		// �C�@�� cell��������]�ݭn�]�w�W�r
		initEachCellName();
	}

	/*
	 * �C�@�� cell��������]�ݭn�]�w�W�r
	 */
	private void initEachCellName() {
		String name = m_panel.getName();
		for (int row = 0; row < m_RowCount; ++row) {
			for (int col = 0; col < m_ColumnCount; ++col) {
				getJComponent(row, col).setName(name + "_r" + row + "_c" + col);
			}
		}
	}

	// from IView
	public JScrollPane getView() {
		return m_panel;
	}

	// from IView
	public String getViewName() {
		return m_panel.getName();
	}

	public static List<String> modifierItems() {
		List<String> items = new Vector<String>();
		items.add("None");
		items.add("Alt");
		items.add("Backspace");
		items.add("CapsLock");
		items.add("Ctrl");
		items.add("Delete");
		items.add("Down");
		items.add("End");
		items.add("Enter");
		items.add("Esc");
		items.add("F1");
		items.add("F2");
		items.add("F3");
		items.add("F4");
		items.add("F5");
		items.add("F6");
		items.add("F7");
		items.add("F8");
		items.add("F9");
		items.add("F10");
		items.add("F11");
		items.add("Home");
		items.add("Insert");
		items.add("Let");
		items.add("NumLk");
		items.add("Pause");
		items.add("PgUp");
		items.add("PgDn");
		items.add("PrtSc");
		items.add("ScrLk");
		items.add("Shift");
		items.add("Tab");
		items.add("Up");
		return items;
	}

}
