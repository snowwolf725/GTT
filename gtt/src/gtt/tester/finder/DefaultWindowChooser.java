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
package gtt.tester.finder;

import gtt.eventmodel.IComponent;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;

import org.netbeans.jemmy.ComponentChooser;

public class DefaultWindowChooser implements ComponentChooser {

	public static DefaultWindowChooser create(IComponent comp) {
		return new DefaultWindowChooser(comp);
	}

	IComponent component; // 記錄元件的七個資訊

	private DefaultWindowChooser(IComponent comp) {
		component = comp;
	}

	public boolean checkComponent(Component c) {
		if (component == null)
			return false; // can't find component

		if (c instanceof Frame) {
			// Frame 有 title
			Frame frame = (Frame) c;
			// 用title及name 來找
			boolean r = isEquals(component.getTitle(), frame.getTitle())
					|| isEquals(component.getName(), frame.getName());
			if (r == true)
				return true;
		}

		if (c instanceof Dialog) {
			// Dialog 有 title
			Dialog dialog = (Dialog) c;
			// 用title及name 來找
			boolean r = isEquals(component.getTitle(), dialog.getTitle())
					|| isEquals(component.getName(), dialog.getName());
			if (r == true)
				return true;
		}

		// 其它就用 name 來找
		return isEquals(component.getName(), c.getName());
	}

	final public String DESC = getClass().toString() + "[GTT]";

	public String getDescription() {
		return DESC;
	}

	private boolean isEquals(String s1, String s2) {
		if (s1 == null)
			return false;
		if (s2 == null)
			return false;
		return s1.equals(s2);
	}
}