/**
 * 
 */
package gttlipse.scriptEditor.def;

/**
 * @author SnowWolf725
 * 
 * created first in project GTTlipse.scriptEditor.def
 * 
 */
public interface ActionType {
	public static int SEPARATOR = 0;

	public static int OPEN_NEW_FILE = 1;

	public static int OPEN_FILE = 2;

	public static int SAVE_FILE = 3;

	public static int SAVE_AS_FILE = 4;

	public static int STOP_RECORD = 5;

	public static int RECORD = 6;

	public static int SYNC = 7;
	
	public static int ADD_SOURCE_FOLDER_NODE = 8;

	public static int ADD_PACKAGE_NODE = 9;

	public static int ADD_CASE_NODE = 10;

	public static int ADD_METHOD_NODE = 11;

	public static int ADD_TEST_SCRIPT_DOCUMENT = 12;

	public static int ADD_FOLDER_NODE = 13;

	public static int ADD_ASSERT_NODE = 14;

	public static int ADD_EVENT_NODE = 15;
	
	public static int ADD_AUTINFO_NODE = 16;
	
	public static int ADD_ORACLE_NODE = 17;

	public static int DEL_NODE = 18;

	public static int EDIT_NODE = 19;

	public static int COPY_NODE = 20;

	public static int CUT_NODE = 21;

	public static int PASTE_NODE = 22;

	public static int UP_MOVE_NODE = 23;

	public static int DOWN_MOVE_NODE = 24;

	public static int GOTO_CODE = 25;
	
	public static int FILTER = 26;
	
	public static int ADD_ORACLE = 27;
	
	public static int COLLECT_COMP_INFO = 28;
	
	public static int REFEACTORING_RENAME = 29;

	public static int REFEACTORING_RENAME_WINDOW_TITLE = 30;
	
	public static int REPLAY = 31;
}
