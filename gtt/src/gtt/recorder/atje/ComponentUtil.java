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
package gtt.recorder.atje;

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

public class ComponentUtil {

	static void initTextInformation(TemporaryComponentData data, Component comp) {
		String _text = "";
		if (comp instanceof JLabel) {
			_text = ((JLabel) comp).getText();
		} else if (comp instanceof JTextComponent) {
			_text = ((JTextComponent) comp).getText();
		} else if (comp instanceof JButton) {
			_text = ((JButton) comp).getText();
		} else if (comp instanceof AbstractButton) {
			_text = ((AbstractButton) comp).getText();
		} else if (comp instanceof JList) {
			if (!((JList) comp).isSelectionEmpty())
				_text = ((JList) comp).getSelectedValue().toString();
		} else if (comp instanceof JTabbedPane) {
			if (((JTabbedPane) comp).getSelectedIndex() != -1) // 20040226 ,art
				_text = ((JTabbedPane) comp).getTitleAt(((JTabbedPane) comp)
						.getSelectedIndex());
		} else if (comp instanceof JComboBox) {
			_text = ((JComboBox) comp).getSelectedItem() != null ? ((JComboBox) comp)
					.getSelectedItem().toString()
					: null;
		}

		data.setText(_text);
	}

	static void initWindowInformation(TemporaryComponentData data,
			Component comp) {
		Window window = null;
		if (comp instanceof Window)
			window = (Window) comp;
		else
			window = SwingUtilities.getWindowAncestor(comp);
		
		if (window == null)
			return;
		
		Class<?> type = null;
		String title = "";
		if (window instanceof Frame) {
			type = Frame.class;
			title = ((Frame) window).getTitle();
		} else if (window instanceof Dialog) {
			type = Dialog.class;
			title = ((Dialog) window).getTitle();
		} else if (window instanceof JWindow) {
			// JWindow 類的沒有title
			type = JWindow.class;
		}

		data.setWinType(type);
		data.setTitle(title);
	}

}
