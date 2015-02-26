package gttlipse.macro.action;

public interface MacroAction {
	// 檔案操作
	public static int NEW_FILE = 0;
	public static int OPEN_FILE = 1;
	public static int SAVE_FILE = 2;
	public static int SAVE_FILE_AS = 3;

	// 節點操作
	public static int CUT_MACRO_NODE = 4;
	public static int COPY_MACRO_NODE = 5;
	public static int PASTE_MACRO_NODE = 6;
	public static int DELETE_MACRO_NODE = 7;
	public static int UP_MACRO_NODE = 8;
	public static int DOWN_MACRO_NODE = 9;

	// 編輯節點
	public static int EDIT_MACRO_NODE = 10;
	public static int INSERT_COMPONENT_NODE = 11;
	public static int INSERT_MACRO_COMPONENT_NODE = 12;
	public static int INSERT_MACRO_EVENT_NODE = 13;
	public static int INSERT_COMPONENT_EVENT_NODE = 14;
	public static int INSERT_SINGLE_MACRO_EVENT_NODE = 15;
	public static int INSERT_VIEW_ASSERT_NODE = 16;
	public static int INSERT_EXISTENCE_ASSERT_NODE = 161;

	public static int INSERT_MACRO_EVENT_TO_TEST_SCRIPT = 17;
	public static int COPY_TEST_SCRIPT_TO_MACRO = 18;

	public static int CREATE_METS = 19;

	public static int REFRESH = 20;
	public static int SAVE_MFSM = 21;

	// 加入用來處理EventTrigger node的參數
	public static int INSERT_EVENT_TRIGGER_NODE = 22;
	// 加入用來處理fit table assertion node
	public static int INSERT_FIT_TABLE_ASSERTION_NODE = 23;
	// 加入用來處理fit node
	public static int INSERT_FIT_NODE = 24;
	// 加入用來處理generational node
	public static int INSERT_SPLIT_DATA_AS_NAME_NODE = 25;
	public static int INSERT_GENERATE_ORDER_NAME_NODE = 26;
	public static int INSERT_FIX_NAME_NODE = 27;
	// 加入用來處理fit assertion node
	public static int INSERT_FIT_ASSERTION_NODE = 28;
	
	// Refactoring
	public static int REFACTORING_RENAME = 29;
	public static int REFACTORING_ADD_PARAMETER = 30;
	public static int REFACTORING_REMOVE_PARAMETER = 31;
	public static int REFACTORING_RENAME_PARAMETER = 32;
	public static int REFACTORING_EXTRACT_MACRO_EVENT = 33;
	public static int REFACTORING_INLINE_MACRO_EVENT = 34;
	public static int REFACTORING_MOVE_MACRO_EVENT = 35;
	public static int REFACTORING_EXTRACT_MACRO_COMPONENT = 36;
	public static int REFACTORING_RENAME_WINDOW_TITLE = 37;
	public static int REFACTORING_MOVE_COMPONENT = 38;
	public static int REFACTORING_MOVE_MACRO_COMPONENT = 39;
	public static int REFACTORING_REMOVE_MIDDLE_MAN = 40;
	public static int REFACTORING_INLINE_MACRO_COMPONENT = 41;
	public static int REFACTORING_HIDE_DELEGATE = 42;
	public static int REFACTORING_EXTRACT_PARAMETER = 43;
	public static int REFACTORING_INLINE_PARAMETER = 44;
	
	// Control view focus on point
	public static int FOCUS_ON_POINT = 50;
	
	// Detect
	public static int DETECT_OUTER_USAGE = 51;
	public static int DETECT_LONGMACROEVENT = 52;
	public static int DETECT_LONGMACROCOMP = 53;
	public static int DETECT_LONGARG = 54;
	public static int DETECT_DUPLICATEEVENT = 55;
	public static int DETECT_SHOTGUNSURGERYUSAGE = 56;
	public static int DETECT_LACKENCAPSULATION = 57;
	public static int DETECT_MIDDLEMAN = 58;
	public static int DETECT_HIERARCHY = 59;
	public static int DETECT_FEATUREENVY = 60;
	public static int DETECT_ALLSMELL = 61;
	public static int LOCATE_BADSMELL = 65;
	public static int CLEAR_BADSMELL = 66;
	public static int CLEAR_LOADTESTINGRESULT = 67;
	
	// Replay
	public static int REPLAY = 70;
	public static int MULTI_USER_REPLAY = 71;	
//	public static int GENERATE_TO_MACRO_COMPONENT = 72;
	
	// uhsing 2011.03.16
	public static int INSERT_SPLIT_DATA_NODE = 80;
	// uhsing 2011.03.22
	public static int INSERT_DYNAMIC_COMPONENT_NODE = 81;
	// uhsing 2011.03.28
	public static int INSERT_DYNAMIC_COMPONENT_EVENT_NODE = 82;
	
	// Statistic
	public static int STAT_NODES = 90;
	public static int STAT_SEARCH_COST = 91;
	
	public static int INSERT_LAUNCH_NODE = 101;
	public static int INSERT_INCLUDE_NODE = 102;	
	
	public static int[] actionList = {
			MacroAction.NEW_FILE,
			MacroAction.OPEN_FILE,
			MacroAction.SAVE_FILE,
			MacroAction.SAVE_FILE_AS,
			MacroAction.CUT_MACRO_NODE,
			MacroAction.COPY_MACRO_NODE,
			MacroAction.PASTE_MACRO_NODE,
			MacroAction.DELETE_MACRO_NODE,
			MacroAction.UP_MACRO_NODE,
			MacroAction.DOWN_MACRO_NODE,
			MacroAction.EDIT_MACRO_NODE,
			MacroAction.INSERT_COMPONENT_NODE,
			MacroAction.INSERT_MACRO_COMPONENT_NODE,
			MacroAction.INSERT_MACRO_EVENT_NODE,
			MacroAction.INSERT_COMPONENT_EVENT_NODE,
			MacroAction.INSERT_SINGLE_MACRO_EVENT_NODE,
			MacroAction.INSERT_VIEW_ASSERT_NODE,
			MacroAction.INSERT_EXISTENCE_ASSERT_NODE,
			MacroAction.INSERT_LAUNCH_NODE,
			MacroAction.INSERT_INCLUDE_NODE,			
			MacroAction.INSERT_MACRO_EVENT_TO_TEST_SCRIPT,
			MacroAction.COPY_TEST_SCRIPT_TO_MACRO,
			MacroAction.CREATE_METS,
			MacroAction.REFRESH,
			MacroAction.SAVE_MFSM,
			MacroAction.INSERT_EVENT_TRIGGER_NODE,
			MacroAction.INSERT_FIT_TABLE_ASSERTION_NODE,
			MacroAction.INSERT_FIT_NODE,
			MacroAction.INSERT_SPLIT_DATA_AS_NAME_NODE,
			MacroAction.INSERT_GENERATE_ORDER_NAME_NODE,
			MacroAction.INSERT_FIX_NAME_NODE,
			MacroAction.INSERT_FIT_ASSERTION_NODE,
			MacroAction.REFACTORING_RENAME,
			MacroAction.REFACTORING_ADD_PARAMETER,
			MacroAction.REFACTORING_REMOVE_PARAMETER,
			MacroAction.REFACTORING_RENAME_PARAMETER,
			MacroAction.REFACTORING_EXTRACT_MACRO_EVENT,
			MacroAction.REFACTORING_INLINE_MACRO_EVENT,
			MacroAction.REFACTORING_MOVE_MACRO_EVENT,
			MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT,
			MacroAction.REFACTORING_RENAME_WINDOW_TITLE,
			MacroAction.REFACTORING_MOVE_COMPONENT,
			MacroAction.REFACTORING_MOVE_MACRO_COMPONENT,
			MacroAction.REFACTORING_REMOVE_MIDDLE_MAN,
			MacroAction.REPLAY,
			MacroAction.MULTI_USER_REPLAY,
			MacroAction.DETECT_OUTER_USAGE,
			MacroAction.DETECT_LONGMACROEVENT,
			MacroAction.DETECT_LONGMACROCOMP,
			MacroAction.DETECT_LONGARG,
			MacroAction.DETECT_DUPLICATEEVENT,
			MacroAction.DETECT_SHOTGUNSURGERYUSAGE,
			MacroAction.DETECT_LACKENCAPSULATION,
			MacroAction.DETECT_LACKENCAPSULATION,
			MacroAction.DETECT_MIDDLEMAN,
			MacroAction.DETECT_HIERARCHY,
			MacroAction.DETECT_FEATUREENVY,
			MacroAction.DETECT_ALLSMELL,
			MacroAction.LOCATE_BADSMELL,
			MacroAction.CLEAR_BADSMELL,
			MacroAction.CLEAR_LOADTESTINGRESULT,
//			MacroAction.GENERATE_TO_MACRO_COMPONENT,
			MacroAction.REFACTORING_INLINE_MACRO_COMPONENT,
			MacroAction.REFACTORING_HIDE_DELEGATE,
			MacroAction.REFACTORING_EXTRACT_PARAMETER,
			MacroAction.REFACTORING_INLINE_PARAMETER,
			MacroAction.FOCUS_ON_POINT,
			MacroAction.INSERT_SPLIT_DATA_NODE,
			MacroAction.STAT_NODES,
			MacroAction.STAT_SEARCH_COST,
			MacroAction.INSERT_DYNAMIC_COMPONENT_NODE,
			MacroAction.INSERT_DYNAMIC_COMPONENT_EVENT_NODE
	};
}
