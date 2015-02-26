package gttlipse.tabular.def;

public interface TableTag {
	// For Macro usage
	public static String VIEW_ASSERT_NODE = "Assert";
	public static String LAUNCH_NODE = "Launch";
	public static String SPLIT_DATA_NODE = "Split";
	public static String ARG_COMPONENT = "Argument";
	public static String ARG_ACTION = "List";
	
	// For Test Script usage
	public static String SETUP = "SetUp";
	public static String TEARDOWN = "TearDown";
	public static String AUT_INFO_NODE = "Start Application";
	public static String EVENT_NODE = "Test Event";
	public static String REF_MACRO_EVENT_NODE = "Call Macro Event";
	
	// Tag list for Component Node
	public static String COMPONENT_TAG_LIST[] = {
		VIEW_ASSERT_NODE,
	};
	
	// Tag list for normal usage
	public static String NORMAL_TAG_LIST[] = {
		SPLIT_DATA_NODE,
		LAUNCH_NODE
	};
}
