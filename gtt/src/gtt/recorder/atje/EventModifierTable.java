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

/**
 * Extract Class from InputEventData Create by zws 2006/04/02
 */
public class EventModifierTable {
	/* change to protected zws 2006/04/02 */
	protected static final String[] MID = { "12", "18", "8", "20", "17", "127",
			"40", "35", "10", "27", "112", "113", "114", "115", "116", "117",
			"118", "119", "120", "121", "122", "123", "36", "155", "37", "144",
			"19", "33", "34", "154", "39", "145", "16", "9", "38" };

	/* change to protected zws 2006/04/02 */
	protected static final String[] MNAME = { "None", "Alt", "Backspace",
			"CapsLock", "Ctrl", "Delete", "Down", "End", "Enter", "Esc", "F1",
			"F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11",
			"F12", "Home", "Insert", "Left", "NumLk", "Pause", "PgUp", "PgDn",
			"PrtSc", "Right", "ScrLk", "Shift", "Tab", "Up" };

	/*
	 * Extract Method from getData zws 2006/04/02
	 */
	public static int indexOfModifier(int modid) {
		for (int idx = 0; idx < MID.length; ++idx) {
			if (MID[idx].equals(Integer.toString(modid)))
				return idx;
		}
		return 0;
	}

	public static String getName(int idx) {
		return MNAME[idx];
	}

	public static String getId(int idx) {
		return MID[idx];

	}

	public static String getModifier(int idx) {
		return getName(idx) + ":" + getId(idx);
	}

}
