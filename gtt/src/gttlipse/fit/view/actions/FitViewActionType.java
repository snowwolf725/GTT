package gttlipse.fit.view.actions;

public class FitViewActionType {
	final static public int NEW_FILE = 0;
	final static public int SAVE_FILE = 1;
//	final static public int TABLE_LOAD = 2;
	final static public int REFRESH_FILE = 3;
	final static public int TABLE_COL_ADD = 4;
	final static public int TABLE_COL_REMOVE = 5;
	final static public int TABLE_ROW_ADD = 6;
	final static public int TABLE_ROW_REMOVE = 7;
//	final static public int TABLE_COL_LEFT = 8;
//	final static public int TABLE_COL_RIGHT = 9;
//	final static public int TABLE_ROW_UP = 10;
//	final static public int TABLE_ROW_DOWN = 11;
	
	public static int[] actionList = {
		FitViewActionType.NEW_FILE,
		FitViewActionType.SAVE_FILE,
//		FitViewActionType.TABLE_LOAD,
		FitViewActionType.REFRESH_FILE,
		FitViewActionType.TABLE_COL_ADD,
		FitViewActionType.TABLE_COL_REMOVE,
		FitViewActionType.TABLE_ROW_ADD,
		FitViewActionType.TABLE_ROW_REMOVE,
//		FitViewActionType.TABLE_COL_LEFT,
//		FitViewActionType.TABLE_COL_RIGHT,
//		FitViewActionType.TABLE_ROW_UP,
//		FitViewActionType.TABLE_ROW_DOWN
	};
}
