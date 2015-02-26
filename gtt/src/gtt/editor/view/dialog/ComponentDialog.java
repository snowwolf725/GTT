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
 * Created on 2005/3/15
 */
package gtt.editor.view.dialog;

import gtt.eventmodel.IComponent;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.ViewAssertNode;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author zwshen 2006/11/30
 * 
 */

public class ComponentDialog implements INodeDialog {

	public ComponentDialog() {
	}

	public void appear() {
		if (m_node instanceof EventNode) {
			new _ImplementDialog(((EventNode) m_node).getComponent())
					.setVisible(true);
		} else if (m_node instanceof ViewAssertNode) {
			new _ImplementDialog(((ViewAssertNode) m_node).getComponent())
					.setVisible(true);
		}
	}

	private AbstractNode m_node;

	public AbstractNode getNode() {
		return m_node;
	}

	public void setNode(Object node) {
		m_node = (AbstractNode)node;
	}

}

/*
 * 實作的細節，使用到 Swing JDIalog
 */
class _ImplementDialog extends JDialog {
	/**
	 * default serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private final static String TITLE = "Information Dialog";

	ComponentInfoView view;

	public _ImplementDialog(IComponent comData) {
		view = new ComponentInfoView(comData);
		
		init();
		initButtons();
	}


	private void initButtons() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(creaeOkButton());
		bottomPanel.add(createBtnCancel());
		getContentPane().add(BorderLayout.SOUTH, bottomPanel);
	}

	private JButton createBtnCancel() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new AbstractAction("cancelButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		return btnCancel;
	}

	private JButton creaeOkButton() {
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new AbstractAction("okButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				view.accept();
				setVisible(false);
				dispose();
			}
		});
		return btnOk;
	}

	private void init() {
		this.setTitle(TITLE);
		this.setModal(true);
		this.setResizable(false);
		setSize(300, 400);
		WidgetFactory.placeCenter(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, new JScrollPane(view.getPanel()));
	}


}
