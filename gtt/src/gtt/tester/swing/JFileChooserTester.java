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
package gtt.tester.swing;

import gtt.eventmodel.IEvent;

import javax.swing.JFileChooser;

import org.netbeans.jemmy.operators.JFileChooserOperator;

public class JFileChooserTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JFileChooser))
			return false;

		return forJFileChooserOperator(info, comp);
	}

	private boolean forJFileChooserOperator(IEvent info, Object comp) {
		JFileChooserOperator op = new JFileChooserOperator((JFileChooser) comp);
		op.setApproveButtonText("approve");

		int eid = info.getEventId();
		if (eid == SwingTesterTag.APPROVE) {
			// file chooser ¤Wªºopen Áä
			op.approve();
			return true;
		}
		if (eid == SwingTesterTag.CANCEL) {
			op.cancelSelection();
			return true;
		}
		if (eid == SwingTesterTag.CHOOSE_FILE) {
			// choose file
			String file = info.getArguments().getValue("SelectedFile");
			op.chooseFile(file);
			return true;
		}
		if (eid == SwingTesterTag.CLICK_ON_FILE_BY_INDEX) {
			// something wrong, zws 2007/05/22
			String _selectedFile = info.getArguments().getValue(
					"SelectedFile");
			op.clickOnFile(_selectedFile);
			return true;

		}
		if (eid == SwingTesterTag.CLICK_ON_FILE_BY_NAME) {
			String _selectedFile = info.getArguments().getValue(
					"SelectedFile");
			op.clickOnFile(_selectedFile);
			return true;
		}
		if (eid == SwingTesterTag.ENTER_SUB_DIR) {
			String dir = info.getArguments().getValue("SelectedDir");
			op.enterSubDir(dir);
			return true;
		}
		// select By Click
		if (eid == SwingTesterTag.SELECT_FILE) {
			String _selectedFile = info.getArguments().getValue(
					"SelectedFile");
			op.selectFile(_selectedFile);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_FILE_TYPE) {
			String filter = info.getArguments().getValue("Filter");
			op.selectFileType(filter);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_PATH_DIRECTORY) {
			String dir = info.getArguments().getValue("SelectDir");
			op.selectPathDirectory(dir);
			return true;
		}

		return false;
	}

}
