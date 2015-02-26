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

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/*
 * 這個界面定義出每一個Swing event 的ID
 * 在runner時，是以event id來決定要發的event，而不是以event name來決定。
 */
final public class SwingTesterTag {
	public final long DEFAULT_MOUSE_SLEEP = 100L;

	// //////////////////////////////////////////////////////////////////
	// java.awt.Window
	// JDialog
	public static final int ACTIVATE = 1171;

	public static final int CLOSE = 1172;

	public static final int MOVE = 1173;

	public static final int RESIZE = 1174;

	// //////////////////////////////////////////////////////////////////
	// JFrame
	public static final int DEMAXIMIZE = 1181;

	public static final int MAXIMIZE = 1182;

	// //////////////////////////////////////////////////////////////////
	// JInternalFrame
	public static final int DEICONIFY = 1170;

	public static final int SCROLL_TO_FRAME = 1175;

	// public static final int ACTIVATE = 1171 ;
	// public static final int CLOSE = 1172 ;
	// public static final int MOVE = 1173 ;
	// public static final int RESIZE = 1174 ;
	// public static final int DEMAXIMIZE = 1181 ;
	// public static final int MAXIMIZE = 1182 ;

	// //////////////////////////////////////////////////////////////////
	// JComponent
	// JLabel
	// JProgressBar
	public static final int PRESS_KEY = 1000;

	public static final int PRESS_MOUSE = 1001;

	public static final int RELEASE_KEY = 1002;

	public static final int RELEASE_MOUSE = 1003;

	public static final int TYPE_KEY = 1004;

	public static final int CLICK_NO_BLOCK = 1005;

	public static final int CLICK_MOUSE = 1006;

	public static final int CLICK_MOUSE_NOXY = 1013;

	public static final int DRAGNDROP = 1007;

	public static final int DRAG_MOUSE = 1008;

	public static final int ENTER_MOUSE = 1009;

	public static final int EXIT_MOUSE = 1010;

	public static final int MOVE_MOUSE = 1011;

	public static final int SET_TIME_OUT = 1012;

	// //////////////////////////////////////////////////////////////////
	// AbstractButtonComponent
	// JRaiodButton
	// JCheckBox
	// JToggleButton
	public static final int PRESS = 900;

	public static final int PUSH = 901;

	public static final int PUSH_NO_BLOCK = 902;

	public static final int RELEASE = 903;

	// //////////////////////////////////////////////////////////////////
	// JTextComponent
	// JTextPane
	// JTextField
	public static final int CHANGE_CARET_POSITION = 1151;

	public static final int CLEAR_TEXT = 1152;

	public static final int ENTER_TEXT = 1153;

	// //////////////////////////////////////////////////////////////////
	// JComboBox
	// public static final int CLEAR_TEXT = 1152 ;
	// public static final int ENTER_TEXT = 1153 ; // Click + Clear + EnterText
	// public static final int TYPE_TEXT = 1159 ; // Click + EnterText
	public static final int SELECT_ITEM_BY_INDEX = 1024;

	public static final int SELECT_ITEM_BY_STRING = 1025;

	// //////////////////////////////////////////////////////////////////
	// 衝突
	// public static final int PRESS_MOUSE = 1154 ;
	// public static final int RELEASE_MOUSE = 1155 ;
	public static final int SCROLL_TO_POSITION = 1156;

	public static final int SELECT_TEXT = 1157;

	public static final int SELECT_TEXT_POS = 1158;

	public static final int TYPE_TEXT = 1159;

	public static final int TYPE_TEXT_NO_POS = 1160;

	// //////////////////////////////////////////////////////////////////
	// JTextArea
	public static final int CHANGE_CARET_POS_BY_ROW_COL = 1161;

	public static final int CHANGE_CARET_ROW = 1162;

	public static final int SELECT_TEXT_BY_ROW_COL = 1163;

	public static final int TYPE_TEXT_BY_ROW_COL = 1164;

	// //////////////////////////////////////////////////////////////////
	// JColorChooser
	public static final int ENTER_COLOR = 1011;

	// //////////////////////////////////////////////////////////////////
	// JFileChooser
	public static final int APPROVE = 1031;

	public static final int CANCEL = 1032;

	public static final int CHOOSE_FILE = 1033; // SelectFilr+Approve

	public static final int CLICK_ON_FILE_BY_INDEX = 1034;

	public static final int CLICK_ON_FILE_BY_NAME = 1035;

	public static final int ENTER_SUB_DIR = 1036;

	public static final int SELECT_FILE = 1037;

	public static final int SELECT_FILE_TYPE = 1038;

	public static final int SELECT_PATH_DIRECTORY = 1039;

	// //////////////////////////////////////////////////////////////////
	// JTree
	public static final int CALL_POPUP_ON_PATH = 1151;

	public static final int CLICK_FOR_EDIT = 1152;

	public static final int CLICK_ON_PATH = 1153;

	public static final int COLLAPSE_PATH = 1154;

	public static final int COLLAPSE_ROW = 1155;

	public static final int EXPAND_PATH = 1156;

	public static final int EXPAND_ROW = 1157;

	public static final int SCROLL_TO_PATH = 1160;

	public static final int SCROLL_TO_ROW = 1161;

	public static final int SELECT_PATH = 1162;

	public static final int SELECT_ROW = 1163;

	// //////////////////////////////////////////////////////////////////
	// JList
	// public static final int PRESS_MOUSE = 1001 ;
	// public static final int RELEASE_MOUSE = 1003 ;
	public static final int SELECT_ITEMS_BY_INDEX = 1053;

	public static final int SELECT_ITEMS_BY_TEXT = 1054;

	public static final int CLICK_ON_ITEM_BY_INDEX = 1055;

	public static final int CLICK_ON_ITEM_BY_TEXT = 1056;

	public static final int SCROLL_TO_ITEM_BY_INDEX = 1057;

	public static final int SCROLL_TO_ITEM_BY_TEXT = 1058;

	// //////////////////////////////////////////////////////////////////
	// JMenuBar
	public static final int CLOSE_SUBMENUS = 1061;

	// JMenu
	public static final int PUSH_MENU = 1062;

	public static final int PUSH_MENU_NO_BLOCK = 1063;

	public static final int SHOW_MENU_ITEM = 1064;

	// //////////////////////////////////////////////////////////////////
	// JPopupMenu
	public static final int CALL_POPUP = 1071;

	// public static final int PRESS_MOUSE = 1001 ;
	// public static final int RELEASE_MOUSE = 1003 ;
	// public static final int PUSH_MENU = 1062 ;
	// public static final int PUSH_MENU_NO_BLOCK = 1063 ;
	// public static final int SHOW_MENU_ITEM = 1064 ;

	// //////////////////////////////////////////////////////////////////
	// JSpinner
	public static final int SCROLL_TO_MAXIMUM = 1101;

	public static final int SCROLL_TO_MINIMUM = 1102;

	public static final int SCROLL_TO_STRING = 1103;

	// //////////////////////////////////////////////////////////////////
	// JScrollBar
	// JSlider
	// public static final int CLICK_MOUSE = 1081 ;
	// public static final int PRESS_MOUSE = 1082 ;
	// public static final int RELEASE_MOUSE = 1083 ;
	// public static final int SCROLL_TO_MAXIMUM = 1101 ;
	// public static final int SCROLL_TO_MINIMUM = 1102 ;
	public static final int SCROLL_TO_PERCENT = 1086;

	public static final int SCROLL_TO_VALUE = 1087;

	// //////////////////////////////////////////////////////////////////
	// JSrollPane
	public static final int SCROLL_TO_BOTTOM = 1091;

	public static final int SCROLL_TO_COMPONENT = 1092;

	public static final int SCROLL_TO_HORIZONTAL_VALUE = 1093;

	public static final int SCROLL_TO_LEFT = 1094;

	public static final int SCROLL_TO_RIGHT = 1095;

	public static final int SCROLL_TO_TOP = 1096;

	public static final int SCROLL_TO_VALUES = 1097;

	public static final int SCROLL_TO_VERTICAL_VALUE = 1098;

	// //////////////////////////////////////////////////////////////////
	// JSplitPane
	public static final int EXPAND_LEFT = 1111;

	public static final int EXPAND_RIGHT = 1112;

	public static final int MOVE_DIVIDER = 1113;

	public static final int MOVE_TO_MAXIMUM = 1114;

	public static final int MOVE_TO_MINIMUM = 1115;

	// //////////////////////////////////////////////////////////////////
	// JSplitPane
	// public static final int PRESS_MOUSE = 1121 ;
	// public static final int RELEASE_MOUSE = 1122 ;
	public static final int SELECT_PAGE = 1123;
	public static final int SELECT_PAGE_BY_INDEX = 1124;

	// //////////////////////////////////////////////////////////////////
	// JTable
	public static final int CALL_POPUP_ON_CELL = 1131;
	// public static final int CLICK_FOR_EDIT = 1132 ;
	public static final int CLICK_ON_CELL = 1133;
	// public static final int PRESS_MOUSE = 1001 ;
	// public static final int RELEASE_MOUSE = 1003 ;
	public static final int SCROLL_TO_CELL = 1136;
	public static final int SELECT_CELL = 1137;

	// //////////////////////////////////////////////////////////////////
	// JTableHeader
	public static final int MOVE_COLUMN = 1141;
	// public static final int PRESS_MOUSE = 1001 ;
	// public static final int RELEASE_MOUSE = 1003 ;
	public static final int SELECT_COLUMN = 1144;

	// //////////////////////////////////////////////////////////////////
	// othter
	public static final int DOUBLE_CLICK = 200;
	

	public static String forEvent(int eid) {
		switch (eid) {
		// JComponent
		case SwingTesterTag.CLICK_NO_BLOCK:
			return "CLICK_NO_BLOCK";
		case SwingTesterTag.CLICK_MOUSE:
			return "CLICK_MOUSE";
		case SwingTesterTag.DRAG_MOUSE:
			return "DRAG_MOUSE";
		case SwingTesterTag.DRAGNDROP:
			return "DRAGNDROP";
		case SwingTesterTag.ENTER_MOUSE:
			return "ENTER_MOUSE";
		case SwingTesterTag.EXIT_MOUSE:
			return "EXIT_MOUSE";
		case SwingTesterTag.MOVE_MOUSE:
			return "MOVE_MOUSE";
		case SwingTesterTag.PRESS_MOUSE:
			return "PRESS_MOUSE";
		case SwingTesterTag.PRESS_KEY:
			return "PRESS_KEY";
		case SwingTesterTag.RELEASE_KEY:
			return "RELEASE_KEY";
		case SwingTesterTag.RELEASE_MOUSE:
			return "RELEASE_MOUSE";
		case SwingTesterTag.TYPE_KEY:
			return "TYPE_KEY";
		case SwingTesterTag.SET_TIME_OUT:
			return "SET_TIME_OUT";
		case SwingTesterTag.PRESS:
			return "PRESS";
		case SwingTesterTag.PUSH:
			return "PUSH";
		case SwingTesterTag.PUSH_NO_BLOCK:
			return "PUSH_NO_BLOCK";
		case SwingTesterTag.RELEASE:
			return "RELEASE";
		case SwingTesterTag.ENTER_TEXT:
			return "ENTER_TEXT";
		case SwingTesterTag.TYPE_TEXT:
			return "TYPE_TEXT";
		case SwingTesterTag.SELECT_ITEM_BY_INDEX:
			return "SELECT_ITEM_BY_INDEX";
		case SwingTesterTag.SELECT_ITEM_BY_STRING:
			return "SELECT_ITEM_BY_TEXT";
		case SwingTesterTag.APPROVE:
			return "APPROVE";
		case SwingTesterTag.CANCEL:
			return "CANCEL";
		case SwingTesterTag.CHOOSE_FILE:
			return "CHOOSE_FILE";
		case SwingTesterTag.CLICK_ON_FILE_BY_INDEX:
			return "CLICK_FILE_INDEX";
		case SwingTesterTag.CLICK_ON_FILE_BY_NAME:
			return "CLICK_FILE_NAME";
		case SwingTesterTag.ENTER_SUB_DIR:
			return "ENTER_SUB_DIR";
		case SwingTesterTag.SELECT_FILE:
			return "SELECT_FILE";
		case SwingTesterTag.SELECT_FILE_TYPE:
			return "SELECT_FILTER";
		case SwingTesterTag.SELECT_PATH_DIRECTORY:
			return "SELECT_DIRECTORY";

			// JTreeComponent
		case SwingTesterTag.COLLAPSE_PATH:
			return "COLLAPSE_PATH";
		case SwingTesterTag.COLLAPSE_ROW:
			return "COLLAPSE_ROW";
			// JTextArea
		case SwingTesterTag.CHANGE_CARET_POS_BY_ROW_COL:
			return "CHANGE_CARET";
		case SwingTesterTag.CHANGE_CARET_ROW:
			return "CHANGE_CARET";
		case SwingTesterTag.SELECT_TEXT_BY_ROW_COL:
			return "SELECT_TEXT";
		case SwingTesterTag.TYPE_TEXT_BY_ROW_COL:
			return "TYPE_TEXT";

			// JTextComponent
		case SwingTesterTag.CHANGE_CARET_POSITION:
			return "CHANGE_CARET";
		case SwingTesterTag.SCROLL_TO_POSITION:
			return "SCROLL_TO_POS";
		case SwingTesterTag.SELECT_TEXT:
			return "SELECT_TEXT";
		case SwingTesterTag.SELECT_TEXT_POS:
			return "SELECT_TEXT_POS";
		case SwingTesterTag.TYPE_TEXT_NO_POS:
			return "TYPE_TEXT_NO_POS";
			// JList
		case SwingTesterTag.SELECT_ITEMS_BY_INDEX:
			return "SELECT_ITEM";
		case SwingTesterTag.SELECT_ITEMS_BY_TEXT:
			return "SELECT_ITEM";
		case SwingTesterTag.CLICK_ON_ITEM_BY_INDEX:
			return "CLICK_ON_ITEM";
		case SwingTesterTag.CLICK_ON_ITEM_BY_TEXT:
			return "CLICK_ON_ITEM";
		case SwingTesterTag.SCROLL_TO_ITEM_BY_INDEX:
			return "SCROLL_TO_ITEM";
		case SwingTesterTag.SCROLL_TO_ITEM_BY_TEXT:
			return "SCROLL_TO_ITEM";

			// JTableHeader
		case SwingTesterTag.MOVE_COLUMN:
			return "MOVE_COLUMN";
		case SwingTesterTag.SELECT_COLUMN:
			return "SELECT_COLUMN";
			// JTableComponent
		case SwingTesterTag.CALL_POPUP_ON_CELL:
			return "CALL_POPUP_ON_CELL";
		case SwingTesterTag.CLICK_FOR_EDIT:
			return "CLICK_FOR_EDIT";
		case SwingTesterTag.CLICK_ON_CELL:
			return "CLICK_ON_CELL";
		case SwingTesterTag.SCROLL_TO_CELL:
			return "SCROLL_TO_CELL";
		case SwingTesterTag.SELECT_CELL:
			return "SELECT_CELL";
			// JTable
		case SwingTesterTag.SELECT_PAGE:
			return "SELECT_PAGE";
		case SwingTesterTag.SELECT_PAGE_BY_INDEX:
			return "SELECT_PAGE";
			// Window
		case SwingTesterTag.ACTIVATE:
			return "ACTIVATE";
		case SwingTesterTag.CLOSE:
			return "CLOSE";
		case SwingTesterTag.MOVE:
			return "MOVE";
		case SwingTesterTag.RESIZE:
			return "RESIZE";
			// JSplitPane
		case SwingTesterTag.EXPAND_LEFT:
			return "EXPAND_LEFT";
		case SwingTesterTag.EXPAND_RIGHT:
			return "EXPAND_RIGHT";
		case SwingTesterTag.MOVE_DIVIDER:
			return "MOVE_DIVIDER";
		case SwingTesterTag.MOVE_TO_MAXIMUM:
			return "MOVE_TO_MAXIMUM";
		case SwingTesterTag.MOVE_TO_MINIMUM:
			return "MOVE_TO_MINIMUM";
		case SwingTesterTag.SCROLL_TO_STRING:
			return "SCROLL_TO_STRING";
			// JScrollPane
		case SwingTesterTag.SCROLL_TO_BOTTOM:
			return "SCROLL_TO_BOTTOM";
		case SwingTesterTag.SCROLL_TO_COMPONENT:
			return "SCROLL_TO_COMP";
		case SwingTesterTag.SCROLL_TO_HORIZONTAL_VALUE:
			return "SCROLL_TO_X";
		case SwingTesterTag.SCROLL_TO_LEFT:
			return "SCROLL_TO_LEFT";
		case SwingTesterTag.SCROLL_TO_RIGHT:
			return "SCROLL_TO_RIGHT";
		case SwingTesterTag.SCROLL_TO_TOP:
			return "SCROLL_TO_TOP";
		case SwingTesterTag.SCROLL_TO_VALUES:
			return "SCROLL_TO_XY";
		case SwingTesterTag.SCROLL_TO_VERTICAL_VALUE:
			return "SCROLL_TO_Y";

			// JScrollBar
		case SwingTesterTag.SCROLL_TO_MAXIMUM:
			return "SCROLL_TO_MAXIMUM";
		case SwingTesterTag.SCROLL_TO_MINIMUM:
			return "SCROLL_TO_MINIMUM";
		case SwingTesterTag.SCROLL_TO_PERCENT:
			return "SCROLL_TO_PERCENT";
		case SwingTesterTag.SCROLL_TO_VALUE:
			return "SCROLL_TO_VALUE";
		case SwingTesterTag.CALL_POPUP:
			return "CALL_POPUP";

			// JMenuComponent
		case SwingTesterTag.PUSH_MENU:
			return "PUSH_MENU";
		case SwingTesterTag.PUSH_MENU_NO_BLOCK:
			return "PUSH_MENU_NO_BLOCK";
		case SwingTesterTag.SHOW_MENU_ITEM:
			return "SHOW_MENU_ITEM";
			// JMenuBar
		case SwingTesterTag.CLOSE_SUBMENUS:
			return "CLOSE_SUBMENUS";
			// JFrame
		case SwingTesterTag.DEMAXIMIZE:
			return "DEMAXIMIZE";
		case SwingTesterTag.MAXIMIZE:
			return "MAXIMIZE";

			// JInternalFrame
		case SwingTesterTag.DEICONIFY:
			return "DEICONIFY";
		case SwingTesterTag.SCROLL_TO_FRAME:
			return "SCROLL_TO_FRAME";
		}
		return UNKNOWN_TYPE;
	}

	public final static String UNKNOWN_TYPE = "unknown type";

	public static String forKeyEvent(int eid) {
		switch (eid) {
		case KeyEvent.KEY_PRESSED:
			return "PRESS_KEY";
		case KeyEvent.KEY_RELEASED:
			return "RELEASE_KEY";
		case KeyEvent.KEY_TYPED:
			return "TYPE_KEY";
		case SwingTesterTag.TYPE_TEXT:
			return "TYPE_TEXT";
		}
		return UNKNOWN_TYPE;
	}

	final static public String forMouseEvent(int eid) {
		switch (eid) {
		case MouseEvent.MOUSE_PRESSED:
			return "PRESSE_MOUSE";
		case MouseEvent.MOUSE_RELEASED:
			return "RELEASE_MOUSE";
		case MouseEvent.MOUSE_CLICKED:
			return "CLICKED_MOUSE";
		case MouseEvent.MOUSE_ENTERED:
			return "ENTER_MOUSE";
		case MouseEvent.MOUSE_EXITED:
			return "EXIT_MOUSE";
		case MouseEvent.MOUSE_MOVED:
			return "MOVE_MOUSE";
		case MouseEvent.MOUSE_DRAGGED:
			return "DRAG_MOUSE";
		case SwingTesterTag.DOUBLE_CLICK:
			return "DOUBLE_CLICK";
		}
		return UNKNOWN_TYPE;
	}

	
	final static public String forHTMLEvent(int eid) {
		switch (eid) {
		case MouseEvent.MOUSE_PRESSED:
			return "PRESSE_MOUSE";
		case MouseEvent.MOUSE_RELEASED:
			return "RELEASE_MOUSE";
		case MouseEvent.MOUSE_CLICKED:
			return "CLICKED_MOUSE";
		case MouseEvent.MOUSE_ENTERED:
			return "ENTER_MOUSE";
		case MouseEvent.MOUSE_EXITED:
			return "EXIT_MOUSE";
		case MouseEvent.MOUSE_MOVED:
			return "MOVE_MOUSE";
		case MouseEvent.MOUSE_DRAGGED:
			return "DRAG_MOUSE";
		case SwingTesterTag.DOUBLE_CLICK:
			return "DOUBLE_CLICK";
		}
		return UNKNOWN_TYPE;
	}
	
	
	
}