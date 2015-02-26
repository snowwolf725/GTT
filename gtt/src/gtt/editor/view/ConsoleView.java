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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class ConsoleView implements IView {

	public static final String DEFAULT_VIEW_NAME = "Output";
	private JScrollPane m_ScrollPane;
	private JTextArea m_TextArea;
	private List<String> m_Text;
	private String m_name = DEFAULT_VIEW_NAME;
	private JButton m_btnClear;

	public ConsoleView(String name) {
		m_name = name;
		init();
	}

	public ConsoleView() {
		init();
	}

	private void init() {
		m_TextArea = new JTextArea();
		m_TextArea.setName(m_name + "TextArea");
		m_TextArea.setEditable(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createToolBar(), BorderLayout.NORTH);
		panel.add(m_TextArea, BorderLayout.CENTER);

		m_ScrollPane = new JScrollPane(panel);
		m_ScrollPane.setAutoscrolls(true);

		m_Text = new LinkedList<String>();
		bind();
	}

	private JToolBar createToolBar() {
		m_btnClear = new JButton();
		m_btnClear.setName("btnClear");
		m_btnClear.setToolTipText("Clear");
		m_btnClear.setIcon(new ImageIcon("images/del.jpg", "X"));
		m_btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearText();
			}
		});
		JToolBar toolBar = new JToolBar("Output toolbar");
		toolBar.setName("Output toolbar");
		toolBar.add(m_btnClear);
		return toolBar;
	}

	private void bind() {
		Iterator<String> ite = m_Text.iterator();
		StringBuilder sb = new StringBuilder();
		while (ite.hasNext()) {
			String s = ite.next();
			sb.append(s + "\n");
		}

		m_TextArea.setText(sb.toString());
		m_ScrollPane.updateUI();
	}

	public void addText(String text) {
		m_Text.add(text);
		bind();
	}

	public void clearText() {
		m_Text.clear();
		m_TextArea.setText("");
	}

	public JComponent getView() {
		return m_ScrollPane;
	}

	public String getViewName() {
		return m_name;
	}

}
