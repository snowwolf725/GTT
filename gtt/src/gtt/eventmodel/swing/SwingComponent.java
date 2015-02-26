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
package gtt.eventmodel.swing;

import gtt.eventmodel.IComponent;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * 用來儲存一個Swing元件的資訊，在GTT中，一個元件共有七項資訊要記錄。
 * 
 * @author zwshen
 * 
 */
public class SwingComponent implements IComponent, Cloneable {
	public final static String DEFAULT_TYPE = "javax.swing.JComponent";

	public final static String DEFAULT_WIN_TYPE = "javax.swing.JFrame";

	public final static String DEFAULT_TITLE = "";

	public final static String DEFAULT_NAME = "";

	public final static String DEFAULT_TEXT = "";

	public final static int DEFAULT_INDEX = 0;

	public final static int DEFAULT_INDEX_OF_SAME_NAME = 0;

	// public final static String DEFAULT_PACKAGE = "JComponent";
	/**
	 * 1. Component type
	 */
	private String m_Type = DEFAULT_TYPE; // JButton, JFileChooser, etc...,

	public String getType() {
		return m_Type;
	}

	public void setType(String type) {
		m_Type = type;
		if (m_Type == null)
			m_Type = DEFAULT_TYPE;
	}

	/**
	 * 2. Window Type
	 */
	private String m_WinType = DEFAULT_WIN_TYPE;

	public String getWinType() {
		return m_WinType;
	}

	public void setWinType(String type) {
		m_WinType = type;
	}

	/**
	 * 3. Window Title
	 */
	private String m_Title = DEFAULT_TITLE;

	public String getTitle() {
		return m_Title;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	/**
	 * 4. Component Name
	 */
	private String m_Name = DEFAULT_NAME;

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
		if (m_Name == null)
			m_Name = ""; // 避免用到null
	}

	/**
	 * 5. Text(Label, Caption)
	 */

	private String m_Text = DEFAULT_TEXT;

	public String getText() {
		return m_Text;
	}

	public void setText(String text) {
		m_Text = text;
		if (m_Text == null)
			m_Text = ""; // 避免用到null
	}

	/**
	 * 6. the index of a component in a window
	 */
	private int m_Index = DEFAULT_INDEX;

	public int getIndex() {
		return m_Index;
	}

	public void setIndex(int idx) {
		m_Index = idx;
	}

	/**
	 * 7. the index of the same name component in window
	 */

	private int m_IndexOfSameName = DEFAULT_INDEX_OF_SAME_NAME;

	public int getIndexOfSameName() {
		return m_IndexOfSameName;
	}

	public void setIndexOfSameName(int idx) {
		m_IndexOfSameName = idx;
	}

	/**
	 * Prototype pattern 用來產生同樣的Swing Component
	 */
	public SwingComponent clone() {
		SwingComponent result = new SwingComponent();
		result.setType(m_Type);
		result.setWinType(m_WinType);
		result.setTitle(m_Title);
		result.setText(m_Text);
		result.setName(m_Name);
		result.setIndex(m_Index);
		result.setIndexOfSameName(m_IndexOfSameName);

		return result;
	}

	public static SwingComponent createComponent(String type, String winType,
			String title, String name, String text, int index, int indexOfName) {
		SwingComponent c = new SwingComponent();
		c.setType(type);
		c.setWinType(winType);
		c.setTitle(title);
		c.setName(name);
		c.setText(text);
		c.setIndex(index);
		c.setIndexOfSameName(indexOfName);
		return c;
	}

	public static SwingComponent create(String type) {
		return createComponent(type, DEFAULT_WIN_TYPE, DEFAULT_TITLE,
				DEFAULT_NAME, DEFAULT_TEXT, DEFAULT_INDEX,
				DEFAULT_INDEX_OF_SAME_NAME);
	}

	public static SwingComponent create(String type, String name) {
		return createComponent(type, DEFAULT_WIN_TYPE, DEFAULT_TITLE, name,
				DEFAULT_TEXT, DEFAULT_INDEX, DEFAULT_INDEX_OF_SAME_NAME);
	}

	public static SwingComponent createDefault() {
		return new SwingComponent();
	}

//	// 使用 awt.component 來建立 IComponent
//	public static SwingComponent createByComponent(Component comp) {
//		return new SwingComponent(comp);
//	}

	private void initTextInformation(Component comp) {
		if (comp instanceof JLabel) {
			m_Text = ((JLabel) comp).getText();
		} else if (comp instanceof JTextComponent) {
			m_Text = ((JTextComponent) comp).getText();
		} else if (comp instanceof JButton) {
			m_Text = ((JButton) comp).getText();
		} else if (comp instanceof AbstractButton) {
			m_Text = ((AbstractButton) comp).getText();
		} else if (comp instanceof JList) {
			if (!((JList) comp).isSelectionEmpty())
				m_Text = ((JList) comp).getSelectedValue().toString();
		} else if (comp instanceof JTabbedPane) {
			if (((JTabbedPane) comp).getSelectedIndex() != -1)
				m_Text = ((JTabbedPane) comp).getTitleAt(((JTabbedPane) comp)
						.getSelectedIndex());
		} else if (comp instanceof JComboBox) {
			m_Text = ((JComboBox) comp).getSelectedItem() != null ? ((JComboBox) comp)
					.getSelectedItem().toString()
					: null;
		}
	}

	private void initWindowInformation(Component comp) {
		Window window = null;

		if (comp instanceof Window)
			window = (Window) comp;
		else
			window = SwingUtilities.getWindowAncestor(comp);

		if (window instanceof Frame) {
			m_WinType = Frame.class.toString();
			m_Title = ((Frame) window).getTitle();
		} else if (window instanceof Dialog) {
			m_WinType = Dialog.class.toString();
			m_Title = ((Dialog) window).getTitle();
		} else if (window instanceof JWindow) {
			// JWindow 類的沒有title
			m_WinType = JWindow.class.toString();
		}
	}

	private SwingComponent(Component comp) {
		m_Name = comp.getName();
		m_Type = comp.getClass().getName();

		// 暫不考慮 index 的問題
		m_Index = 0;
		m_IndexOfSameName = 0;

		initWindowInformation(comp);
		initTextInformation(comp);
	}

	private SwingComponent() {
		m_Type = SwingComponent.DEFAULT_TYPE;
	}

	public String toString() {
		String type = null;
		if (m_Type.lastIndexOf('.') > 0)
			type = m_Type.substring(m_Type.lastIndexOf('.') + 1);
		else
			type = m_Type;

		if (m_Name.equals(""))
			return type;

		return m_Name + ":" + type;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof IComponent))
			return false;

		if (obj == this)
			return true;

		SwingComponent comp = (SwingComponent) obj;
		// 最後，name 要一樣
		return this.m_Name.equals(comp.getName());
	}

	public boolean match(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof IComponent))
			return false;

		SwingComponent component = (SwingComponent) obj;

		// Three information match
		if (!getName().equals(component.getName()))
			return false;

		if (!getType().equals(component.getType()))
			return false;

		if (!getWinType().equals(component.getWinType()))
			return false;

		return true;
	}
}
