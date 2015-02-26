package gttlipse.tabular.def;

import gttlipse.tabular.view.SWTResourceManager;

import org.eclipse.swt.graphics.Color;


public interface TableConstants {
	// Table row and column definition
	public static int DEFAULT_COLUMN_COUNT = 7;
	public static int DEFAULT_ROW_COUNT = 1;
	public static int ROW_HEIGHT = 22;
	public static int COLUMN_WIDTH = 30;
	public static int HEADER_ROW_HEIGHT = 26;
	public static int HEADER_COLUMN_COUNT = 0;
	public static int HEADER_ROW_COUNT = 0;
	public static int HEADER_FONT_SIZE = 14;
	public static int FIRST_HEADER_ROW = 0;
	public static int FIRST_EVENT_ROW = 2;
	
	// Default header
	public static String[] DEFAULT_MACRO_HEADER = {"Macro Event Name", "Component Name", "Action"};
	public static String[] DEFAULT_SCRIPT_HEADER = {"Method Name", "Component Name", "Action"};
	
	// Font and font size for cell renderer
	public static String FONT = "Calibri";
	public static int FONT_SIZE = 13;
	
	// Color for cell renderer
	public static Color COLOR_BLACK = SWTResourceManager.getColor(0, 0, 0);
	public static Color COLOR_WHITE = SWTResourceManager.getColor(255, 255, 255);
	public static Color COLOR_HEADER_ROW = SWTResourceManager.getColor(198, 217, 241);
	public static Color COLOR_ARG_ROW = SWTResourceManager.getColor(46, 37, 225);
	public static Color COLOR_ERROR_ROW = SWTResourceManager.getColor(239, 202, 201);
	
	// Editors' ID
	public static String MACRO_EDITOR_ID = "GTTlipse.tabular.editors.MacroTabularEditor";
	public static String SCRIPT_EDITOR_ID = "GTTlipse.tabular.editors.ScriptTabularEditor";
	
	// Special test method name
	public static String GET_METHOD_NAME = "getMethodName";
	public static String SETUP = "setUp";
	public static String TEARDOWN = "tearDown";
	
	// Additional header string for assertion
	public static String ASSERTION_VALUE = "Assertion Value";
	public static String ASSERTION_METHOD = "Assertion Method";
	
	// Table header indicator
	public static String HEADER_INDICATOR = "Component Name";
	public static String MACRO_HEADER_INDICATOR = "Macro Event Name";
	public static String SCRIPT_HEADER_INDICATOR = "Method Name";
	
	// The header index of the table
	public static int MACRO_EVENT_NAME = 0;
	public static int COMPONENT_NAME = 1;
	public static int ACTION_NAME = 2;
	public static int TEST_METHOD_NAME = 0;
	
	// The content index of the table(Macro usage)
	public static int MACRO_ASSERT_VALUE = 3;
	public static int COMPONENT_METHOD = 4;
	public static int EVENT_ARGUMENT_START = 3;
	public static int METHOD_ARGUMENT_START = 5;
	public static int SPLIT_DATA = 3;
	public static int SPLIT_SEPARATOR = 4;
	public static int SPLIT_TARGET = 5;
	
	// The content index of the table(Test Script usage)
	public static int REF_MACRO_EVENT_PATH = 1;
	public static int CLASS_NAME = 1;
	public static int CLASS_PATH = 3;
	public static int WINDOW_TITLE = 3;
	public static int COMPONENT_TYPE = 4;
	public static int METHOD_NAME = 5;
	public static int SCRIPT_ASSERT_VALUE = 6;
}
