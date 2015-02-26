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
package gtt.tester.web;


/*
 * 這個界面定義出每一個Web event 的ID
 * 事實上沒有實做的必要，但是為了和swing架構相同故加入
 */
final public class WebTesterTag {
	// //////////////////////////////////////////////////////////////////
	// WebElement
	public static final int CLEAR = 10001;
	public static final int CLICK = 10002;
	public static final int INPUT = 10003;
	public static final int SET_SELECTED = 10004;
	public static final int SUBMIT  = 10005;
	
	// //////////////////////////////////////////////////////////////////
	// Select
	public static final int DESELECT_All  = 20001;
	public static final int DESELECT_BY_INDEX  = 20002;
	public static final int DESELECT_BY_VALUE  = 20003;
	public static final int DESELECT_BY_VISIBLE_TEXT = 20004;
	public static final int SELECT_BY_INDEX  = 20005;
	public static final int SELECT_BY_VALUE  = 20006;
	public static final int SELECT_BY_VISIBLE_TEXT = 20007;
	
	
	public final static String UNKNOWN_TYPE = "unknown type";
	
	
	
	final static public String forHTMLEvent(int eid) {
		switch (eid) {
		case CLEAR:
			return "CLEAR";
		case CLICK:
			return "CLICK";
		case INPUT:
			return "INPUT";
		case SET_SELECTED:
			return "SET_SELECTED";
		case SUBMIT:
			return "SUBMIT";
		case DESELECT_All:	
			return "DESELECT_All";
		case DESELECT_BY_INDEX:	
			return "DESELECT_BY_INDEX";	
		case DESELECT_BY_VALUE:	
			return "DESELECT_BY_VALUE";	
		case DESELECT_BY_VISIBLE_TEXT:	
			return "DESELECT_BY_VISIBLE_TEXT";	
		case SELECT_BY_INDEX:	
			return "SELECT_BY_INDEX";	
		case SELECT_BY_VALUE:	
			return "SELECT_BY_VALUE";		
		case SELECT_BY_VISIBLE_TEXT:	
			return "SELECT_BY_VISIBLE_TEXT";		
		}
		return UNKNOWN_TYPE;
	}
	
	
	
}