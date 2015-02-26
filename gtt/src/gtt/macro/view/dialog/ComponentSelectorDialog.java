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
 * Created on 2005/3/22
 */
package gtt.macro.view.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.view.MacroTreeCellRenderer;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentSelectorDialog extends JDialog {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private JPanel m_Panel_SelectComponent = null;

	private JButton m_btnOk = null;

	private JButton m_btnCancel = null;

	private JTree m_ComponentTree = null;

	private AbstractMacroNode m_SelectedComponent = null;

	private DefaultMutableTreeNode m_TreeRoot = null;

	final static String TITLE = "Component Selector";

	private MacroComponentNode m_MacroComponent;

	public ComponentSelectorDialog(MacroComponentNode mc) {
		this.setModal(true);
		this.setTitle(TITLE);

		m_MacroComponent = mc;

		initDialog();
		initAction();
		initName();
	}

	private void initName() {
		setName("ComSelector");
		m_ComponentTree.setName("ComSelector_ComTree");
		m_btnOk.setName("ComSelector_OK");
		m_btnCancel.setName("ComSelector_Cancel");
	}

	private void initTreeRoot() {
		/*
		 * 建立一個 JTree，列出MacroComponent及ComponentNode，讓使用者 可以選擇要使用的Component.
		 */
		m_TreeRoot = new DefaultMutableTreeNode("Available");

		// 加上Root MacroComponent
		m_TreeRoot.add(new DefaultMutableTreeNode(m_MacroComponent));

		// 加上 Sub primitive component
		for (int i = 0; i < m_MacroComponent.size(); i++) {
			// ComponentNode
			if (m_MacroComponent.get(i) instanceof ComponentNode) {
				m_TreeRoot.add(new DefaultMutableTreeNode(m_MacroComponent
						.get(i)));
				continue;
			}
			// MacroComponentNode
			if (m_MacroComponent.get(i) instanceof MacroComponentNode) {
				MacroComponentNode mc = (MacroComponentNode) m_MacroComponent
						.get(i);
				// 要有 MacroEvent 才列入考慮
				if (mc.getMacroEvents().size() <= 0)
					continue;
				m_TreeRoot.add(new DefaultMutableTreeNode(m_MacroComponent
						.get(i)));
				continue;
			}
		}
	}

	private void initDialog() {
		setSize(250, 350);
		WidgetFactory.placeCenter(this);

		JPanel centerPanel = new JPanel(new BorderLayout());

		initTreePanel();
		centerPanel.add(m_Panel_SelectComponent, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		m_btnOk = new JButton("Ok");
		m_btnCancel = new JButton("Cancel");
		bottomPanel.add(m_btnOk);
		bottomPanel.add(m_btnCancel);
		centerPanel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(centerPanel);
	}

	private void initAction() {
		m_btnOk.addActionListener(new AbstractAction("okButton") {
			private static final long serialVersionUID = 4736065992108521053L;

			public void actionPerformed(ActionEvent e) {
				doFinish();
			}
		});
		m_btnCancel.addActionListener(new AbstractAction("cancelButton") {
			private static final long serialVersionUID = 1451568773670025639L;

			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
	}

	private void initComponentTree() {
		initTreeRoot();

		m_ComponentTree = new JTree(m_TreeRoot);
		m_ComponentTree.setCellRenderer(new MacroTreeCellRenderer());

		m_ComponentTree.updateUI();

		m_Panel_SelectComponent.updateUI();
	}

	private void initTreePanel() {
		m_Panel_SelectComponent = new JPanel(new BorderLayout());

		initComponentTree();

		m_Panel_SelectComponent.add(new JScrollPane(m_ComponentTree),
				BorderLayout.CENTER);
	}

	public AbstractMacroNode selectedComponent() {
		return m_SelectedComponent;
	}

	private void doFinish() {
		try {
			m_SelectedComponent = (AbstractMacroNode) ((DefaultMutableTreeNode) m_ComponentTree
					.getLastSelectedPathComponent()).getUserObject();
		} catch (ClassCastException cce) {
			// 沒有可選擇的component node
		}
		setVisible(false);
	}

	private void doCancel() {
		m_SelectedComponent = null;
		setVisible(false);
	}
}
