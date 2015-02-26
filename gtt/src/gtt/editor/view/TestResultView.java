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

import gtt.editor.presenter.ITestScriptPresenter;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeNode;

class TestResultTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	//String titles[] = new String[] { "#", "Error", "Message", "Path" };
	String titles[] = new String[] { "#", "Error", "Description", };
	List<TestResultData> data_list = new LinkedList<TestResultData>();

	@Override
	public int getColumnCount() {
		return titles.length;
	}

	@Override
	public String getColumnName(int c) {
		return titles[c];
	}

	@Override
	public Class<String> getColumnClass(int c) {
		return String.class;
	}

	@Override
	public int getRowCount() {
		return data_list.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	public Object getSelectedValue(int index) {
		return data_list.get(index).getNode();
	}

	Object data[][] = null;

	public void setData(List<TestResultData> list) {
		data_list = list;
		data = new Object[list.size()][titles.length];
		for (int i = 0; i < list.size(); i++) {
			data[i][0] = "" + (i + 1);
			TestResultData rd = list.get(i);
			data[i][1] = rd.getAction();
			data[i][2] = rd.getMessage();
//			data[i][3] = rd.getPathString();
		}
		this.fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}

public class TestResultView implements IView {
	public static final String DEFAULT_VIEW_NAME = "Test Result";
	private JScrollPane m_panel;
	private JTable m_Table;

	private ITestScriptPresenter m_tsPresenter;

	public TestResultView(ITestScriptPresenter p) {
		m_tsPresenter = p;
		TestResultTableModel tm = new TestResultTableModel();
		m_Table = new JTable(tm);
		m_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rsm = m_Table.getSelectionModel();
		rsm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				TestResultTableModel m = (TestResultTableModel) m_Table
						.getModel();
				Object obj = m.getSelectedValue(m_Table.getSelectedRow());
				m_tsPresenter.selectTreeNode((TreeNode) obj);
			}
		});
		m_panel = new JScrollPane(m_Table);
	}

	public synchronized void setErrorData(List<TestResultData> list) {
		((TestResultTableModel) m_Table.getModel()).setData(list);
	}

	public String getViewName() {
		return DEFAULT_VIEW_NAME;
	}

	@Override
	public Object getView() {
		return m_panel;
	}

}
