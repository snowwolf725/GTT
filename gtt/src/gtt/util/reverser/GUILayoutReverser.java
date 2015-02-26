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

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * Swing GUI 元件逆向解析器
 * 
 * @author zwshen
 * 
 */
public class GUILayoutReverser implements IGUIReverser {

	AbstractMacroNode m_MacroRoot = MacroComponentNode.create("GUI Hierarchy");

	public AbstractMacroNode getRoot() {
		return m_MacroRoot;
	}

	public void reverse(Object obj) {
		// initialize
		m_MacroRoot = MacroComponentNode.create("GUI Hierarchy");

		try {
			reverseComponent((Component) obj);
		} catch (Exception nep) {
			System.err.println(nep.toString());
		}
	}

	/***************************************************************************
	 * 培析 AWT Component
	 * 
	 * @param comp
	 */
	private void reverseComponent(Component comp) // 20040212
	{
		if (comp == null)
			return;

		if (comp instanceof Container) {
			reverseAWTContainer((Container) comp);
		} else {
			storeComponentNode(comp);
		}
	}

	/***************************************************************************
	 * 培析 AWT Container
	 * 
	 * @param cont
	 */
	private void reverseAWTContainer(Container cont) {
		if (cont instanceof JComponent) {
			// 以 JComponent 來處理
			reverseSwingJComponent((JComponent) cont);
		} else {
			// 以 AWT Container 來處理
			reverseContainer(cont);
		}
	}

	/***************************************************************************
	 * 培析 JComponent
	 * 
	 * @param comp
	 */
	private void reverseSwingJComponent(JComponent comp) {

		if (comp instanceof JMenu) {
			reverseSwingJMenu((JMenu) comp);
		} else if (comp.getComponentCount() > 0) {
			// 有子節點，要當做MacroComponentNode
			reverseContainer(comp);
		} else {
			// 當做 ComponentNode
			storeComponentNode(comp);
		}
	}

	/***************************************************************************
	 * 培析 Swing JMenu
	 * 
	 * @param menu
	 */
	private void reverseSwingJMenu(JMenu menu) {

		// JPopupMenu pop = menu.getPopupMenu();

		// 產生MacroComponent
		String name = menu.getName();
		if (name == null || name == "") {
			name = menu.getClass().toString().replace("class ", "");
		}
		AbstractMacroNode menuNode = MacroComponentNode.create(name);
		m_MacroRoot.add(menuNode);
		AbstractMacroNode parentNode = m_MacroRoot;
		m_MacroRoot = menuNode;

		// menu 本身也要放一個 ComponentNode
		storeComponentNode(menu);

		Component[] children = menu.getMenuComponents();

		for (int i = 0; i < children.length; i++) {
			// 遞迴培析每個menuitem元件
			reverseComponent(children[i]);
		}

		// 換回 parent
		m_MacroRoot = parentNode;
	}

	/***************************************************************************
	 * 培析 AWT Container，並建立MacroComponent
	 * 
	 * @param cont
	 */
	private void reverseContainer(Container cont) {
		if (cont == null)
			return;

		// 產生MacroComponent
		String name = cont.getName();
		if (name == null || name == "") {
			name = cont.getClass().toString().replace("class ", "");
		}

		AbstractMacroNode subNode = MacroComponentNode.create(name);
		m_MacroRoot.add(subNode);

		AbstractMacroNode parentNode = m_MacroRoot;
		m_MacroRoot = subNode;

		// container 本身也是一個component node
		storeComponentNode(cont);

		Component[] children = cont.getComponents();

		for (int i = 0; i < children.length; i++) {
			// 遞迴培析每個子元件
			reverseComponent(children[i]);
		}

		// 換回 parent
		m_MacroRoot = parentNode;
	}

	/***************************************************************************
	 * 轉換成 ComponentNode
	 * 
	 * @param comp
	 */
	private void storeComponentNode(Component comp) {

		// if( !(comp instanceof JComponent)) return ;
		// 產生 IComponent
		String type = comp.getClass().toString().replace("class ", "");

		int idx = type.indexOf("$");
		if (idx != -1) {
			type = type.substring(0, idx);
		}

		IComponent ic = EventModelFactory.getDefault().getComponent(type);
		if (ic == null)
			return;

		ic.setText("");
		ic.setIndexOfSameName(0);
		ic.setIndex(0);
		ic.setName(comp.getName());

		// 使用ComponentNode 記錄基礎元件
		AbstractMacroNode comp_node = ComponentNode.create(ic);
		m_MacroRoot.add(comp_node);
	}
}
