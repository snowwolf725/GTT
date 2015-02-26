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
package gtt.util.reverser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/**
 * Swing GUI 元件逆向解析器
 * 
 * @author zwshen
 * 
 */
public class GUISimpleReverser implements IGUIReverser {

	public void reverse(Object obj) {
		listing(reverse_swing((Component) obj));
	}

	// swing 元件的逆向工程
	public Hashtable<String, Vector<Component>> reverse_swing(Component root) // 20040212
	{
		Hashtable<String, Vector<Component>> table = new Hashtable<String, Vector<Component>>();
		if (root == null)
			return table; // empty hash

		Window win = SwingUtilities.getWindowAncestor(root);
		store(table, win); // 存起來

		store(table, root);

		if (root instanceof Container) {
			// 是container時，就要做deep search
			reverseContainer((Container) root, table);
		}

		return table;
	}

	public void listing(Hashtable<String, Vector<Component>> table) {
		if (table == null)
			return;
		Enumeration<String> e = table.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			System.out.println(key + " : ");
			Vector<Component> v = (Vector<Component>) table.get(key);
			for (int i = 0; i < v.size(); i++) {
				Component c = (Component) v.get(i);
				System.out.println("\t" + c.getName());
			}
		}
	}

	private void store(Hashtable<String, Vector<Component>> table, Component comp) {
		if (comp == null)
			return; // 不需要存
		store(table, clssify(comp), comp);
	}

	private void reverseContainer(Container con, Hashtable<String, Vector<Component>> table) {
		if (con == null || table == null)
			return;

		Component[] children = con.getComponents();

		for (int i = 0; i < children.length; i++) {
			store(table, clssify(children[i]), children[i]);

			if (children[i] instanceof Container) {
				reverseContainer((Container) children[i], table);
			}
		}
	}

	/**
	 * 
	 * @param table
	 * @param key
	 *            swing type of component
	 * @param comp
	 *            component which would be stored
	 */
	private void store(Hashtable<String, Vector<Component>> table, String key, Component comp) {
		if (table == null || key == null || comp == null)
			return;

		Vector<Component> v;
		if (!table.containsKey(key)) {
			// 不存在這個key，就產生新的vector容器，來儲存component
			v = new Vector<Component>();
			table.put(key, v);
		} else {
			// 繼續往vector 裡面放
			v = (Vector<Component>) table.get(key);
		}

		// 存到 vector 中
		v.add(comp);
	}

	private static String clssify(Component comp) {
		if (comp instanceof JComboBox)
			return "JComboBox";
		if (comp instanceof JLabel)
			return "JLabel";
		if (comp instanceof JList)
			return "JList";
		if (comp instanceof JMenu)
			return "JMenu";
		if (comp instanceof JMenuBar)
			return "JMenuBar";
		if (comp instanceof JOptionPane)
			return "JOptionPane";
		if (comp instanceof JPanel)
			return "JPanel";
		if (comp instanceof JPopupMenu)
			return "JPopupMenu";
		if (comp instanceof JProgressBar)
			return "JProgressBar";
		if (comp instanceof JScrollBar)
			return "JScrollBar";
		if (comp instanceof JScrollPane)
			return "JScrollPane";
		if (comp instanceof JSeparator)
			return "JSeparator";
		if (comp instanceof JSlider)
			return "JSlider";
		if (comp instanceof JSplitPane)
			return "JSplitPane";
		if (comp instanceof JTabbedPane)
			return "JTabbedPane";
		if (comp instanceof JTable)
			return "JTable";
		if (comp instanceof JTableHeader)
			return "JTableHeader";
		if (comp instanceof JToolBar)
			return "JToolBar";
		if (comp instanceof JTree)
			return "JTree";
		if (comp instanceof JViewport)
			return "JViewport";
		if (comp instanceof JFrame)
			return "JFrame";
		if (comp instanceof JDialog)
			return "JDialog";
		if (comp instanceof Frame)
			return "Frame";
		if (comp instanceof Dialog)
			return "Dialog";
		if (comp instanceof JButton)
			return "JButton";
		if (comp instanceof JColorChooser)
			return "JColorChooser";
		if (comp instanceof JEditorPane)
			return "JEditorPane";
		if (comp instanceof JInternalFrame)
			return "JInternalFrame";
		if (comp instanceof JPasswordField)
			return "JPasswordField";
		if (comp instanceof JRadioButton)
			return "JRadioButton";
		if (comp instanceof JSpinner)
			return "JSpinner";
		if (comp instanceof JTextArea)
			return "JTextArea";
		if (comp instanceof JTextField)
			return "JTextField";
		if (comp instanceof AbstractButton)
			return "AbstractButton";
		if (comp instanceof JTextComponent)
			return "JTextComponent";

		return comp.getClass().toString();
	}

}
