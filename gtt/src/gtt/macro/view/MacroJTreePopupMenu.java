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
package gtt.macro.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author ­õ»Ê Applied Command (separate MacroDocument) zws 2006/11/01
 * 
 */
public class MacroJTreePopupMenu extends JPopupMenu {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	JMenuItem m_upgradeItem = new JMenuItem("Move up"),
			m_downgradeItem = new JMenuItem("Move down"),
			m_renameItem = new JMenuItem("Rename"), m_editItem = new JMenuItem(
					"Edit"), m_copyItem = new JMenuItem("Copy"),
			m_cutItem = new JMenuItem("Cut"), m_pasteItem = new JMenuItem(
					"Paste"), m_deleteItem = new JMenuItem("Delete");

	JMenuItem m_editContractItem = new JMenuItem("Edit Contract");
	JMenuItem m_runItem = new JMenuItem("Run");

	public MacroJTreePopupMenu() {
		initLayout();
		initAction();
		initName();
	}

	private void initName() {
		m_upgradeItem.setName("MoveupItem");
		m_downgradeItem.setName("MovedownItem");
		m_renameItem.setName("RenameItem");
		m_copyItem.setName("CopyItem");
		m_cutItem.setName("CutItem");
		m_pasteItem.setName("PasteItem");
		m_deleteItem.setName("DeleteItem");
		m_runItem.setName("RunItem");
	}

	private void initLayout() {

		this.add(m_runItem);
		this.addSeparator();
		this.add(m_editItem);
		this.add(m_editContractItem);
		this.add(m_renameItem);
		this.add(m_deleteItem);
		this.addSeparator();
		this.add(m_upgradeItem);
		this.add(m_downgradeItem);
		this.addSeparator();
		this.add(m_cutItem);
		this.add(m_copyItem);
		this.add(m_pasteItem);
	}

	// command
	ICommand up_cmd;
	ICommand down_cmd;
	ICommand rename_cmd;
	ICommand copy_cmd;
	ICommand cut_cmd;
	ICommand paste_cmd;
	ICommand delete_cmd;

	ICommand edit_cmd;
	ICommand editContract_cmd;
	ICommand run_cmd;

	private void initAction() {

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Move up")
					up_cmd.execute();
				else if (e.getActionCommand() == "Move down")
					down_cmd.execute();
				else if (e.getActionCommand() == "Rename")
					rename_cmd.execute();
				else if (e.getActionCommand() == "Copy")
					copy_cmd.execute();
				else if (e.getActionCommand() == "Cut")
					cut_cmd.execute();
				else if (e.getActionCommand() == "Paste")
					paste_cmd.execute();
				else if (e.getActionCommand() == "Delete")
					delete_cmd.execute();
				else if (e.getActionCommand() == "Edit")
					edit_cmd.execute();
				else if (e.getActionCommand() == "Edit Contract")
					editContract_cmd.execute();
				else if (e.getActionCommand() == "Run")
					run_cmd.execute();
			}
		};

		m_upgradeItem.addActionListener(al);
		m_downgradeItem.addActionListener(al);
		m_renameItem.addActionListener(al);
		m_cutItem.addActionListener(al);
		m_copyItem.addActionListener(al);
		m_pasteItem.addActionListener(al);
		m_deleteItem.addActionListener(al);

		m_editItem.addActionListener(al);
		m_editContractItem.addActionListener(al);
		m_runItem.addActionListener(al);
	}

	public void setCopyCmd(ICommand copy_cmd) {
		this.copy_cmd = copy_cmd;
	}

	public void setCutCmd(ICommand cut_cmd) {
		this.cut_cmd = cut_cmd;
	}

	public void setDeleteCmd(ICommand delete_cmd) {
		this.delete_cmd = delete_cmd;
	}

	public void setDownCmd(ICommand down_cmd) {
		this.down_cmd = down_cmd;
	}

	public void setPasteCmd(ICommand paste_cmd) {
		this.paste_cmd = paste_cmd;
	}

	public void setRenameCmd(ICommand setname_cmd) {
		this.rename_cmd = setname_cmd;
	}

	public void setUpCmd(ICommand up_cmd) {
		this.up_cmd = up_cmd;
	}

	public void setEditCmd(ICommand cmd) {
		this.edit_cmd = cmd;
	}

	public void setEditContractCmd(ICommand cmd) {
		this.editContract_cmd = cmd;
	}

	public void setRunCmd(ICommand cmd) {
		this.run_cmd = cmd;
	}
}
