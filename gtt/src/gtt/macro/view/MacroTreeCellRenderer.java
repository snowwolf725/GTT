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
 * Created on 2005/7/7 by ­õ»Ê
 */
package gtt.macro.view;

import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.util.swing.WidgetFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MacroTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private static final String PATH = System.getProperty("user.dir")
			+ "/images/";

	private JPanel m_CellPanel = new JPanel();

	private static final ImageIcon m_Icon_MacroComponent = new ImageIcon(PATH
			+ "node2_macro.gif");
	private static final ImageIcon m_Icon_SingleComponent = new ImageIcon(PATH
			+ "node2_component.gif");
	private static final ImageIcon m_Icon_EventList = new ImageIcon(PATH
			+ "node2_eventList.gif");
	private static final ImageIcon m_Icon_MacroEvent = new ImageIcon(PATH
			+ "node2_macroEvent.gif");
	private static final ImageIcon m_Icon_ComponentEvent = new ImageIcon(PATH
			+ "node2_componentEvent.gif");
	private static final ImageIcon m_Icon_ComponentAssert = new ImageIcon(PATH
			+ "node2_componentAssert.gif");
	private static final ImageIcon m_Icon_JUnitAssert = new ImageIcon(PATH
			+ "node2_JUnitAssert.gif");

	private static final ImageIcon m_Icon_Sleeper = new ImageIcon(PATH
			+ "sleeper.GIF");
	private static final ImageIcon m_Icon_Comment = new ImageIcon(PATH
			+ "comment.gif");
	private static final ImageIcon m_Icon_Breaker = new ImageIcon(PATH
			+ "break.gif");

	private static final ImageIcon m_Icon_Oracle = new ImageIcon(PATH
			+ "TestOracle.png");

	private JButton m_IconButton = null;

	public MacroTreeCellRenderer() {
		super();
		m_CellPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		m_IconButton = WidgetFactory.createJButton("", 16, 16);
		m_IconButton.setBackground(Color.white);
		m_IconButton.setBorderPainted(false);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// m_CellPanel.setBackground(Color.white);
		// m_CellPanel.setForeground(Color.black);

		Component comp = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof MacroComponentNode) {
			setIcon(m_Icon_MacroComponent);
		} else if (node.getUserObject() instanceof ComponentNode) {
			setIcon(m_Icon_SingleComponent);
		} else if (node.getUserObject() instanceof MacroEventNode) {
			setIcon(m_Icon_EventList);
		} else if (node.getUserObject() instanceof MacroEventCallerNode) {
			setIcon(m_Icon_MacroEvent);
		} else if (node.getUserObject() instanceof ComponentEventNode) {
			setIcon(m_Icon_ComponentEvent);
		} else if (node.getUserObject() instanceof ViewAssertNode) {
			setIcon(m_Icon_ComponentAssert);
		} else if (node.getUserObject() instanceof ModelAssertNode) {
			setIcon(m_Icon_JUnitAssert);
		} else if (node.getUserObject() instanceof SleeperNode) {
			setIcon(m_Icon_Sleeper);
		} else if (node.getUserObject() instanceof CommentNode) {
			setIcon(m_Icon_Comment);
		} else if (node.getUserObject() instanceof BreakerNode) {
			setIcon(m_Icon_Breaker);
		} else if (node.getUserObject() instanceof OracleNode) {
			setIcon(m_Icon_Oracle);
		} else {
			setIcon(m_Icon_MacroComponent);
		}
		return comp;
	}
}
