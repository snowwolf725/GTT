/**
 * 
 */
package gttlipse.scriptEditor.def;

/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.def
 * 
 */
public interface TestScriptTage {
	final public static String PROJECT_NODE = "TestProjectNode";

	final public static String PACKAGE_NODE = "TestPackage";

	final public static String CLASS_NODE = "TestCaseNode";

	final public static String METHOD_NODE = "TestMethodNode";

	final public static String TEST_SCRIPT_DOCUMENT = "TestScriptDocument";
	
	final public static String TEST_SCRIPT_DOCUMENT_FAIL = "TestScriptDocument_Fail";

	final public static String FOLDER_NODE = "TestFolder";

	final public static String EVENT_NODE = "EventNode";
	
	final public static String EVENT_NODE_FAIL = "EventNode_Fail";

	final public static String ASSERT_NODE = "TestAssertNode";
	
	final public static String ASSERT_NODE_FAIL = "AssertNode_Fail";
	
	final public static String AUT_INFO_NODE = "AUTInfoNode";
	
	final public static String SOURCE_FOLDER_NODE = "SourceFolderNode";
	
	final public static String REFERENCE_MACRO_EVENT_NODE = "ReferenceMacroEventNode";
	
	final public static String REFERENCE_MACRO_EVENT_NODE_FAIL = "ReferenceMacroEventNode_Fail";
	
	final public static String REFERENCE_FIT_NODE = "RferenceFitNode";
	
	final public static String ORACLE_NODE = "OracleNode";
	
	final public static String ORACLE_NODE_FAIL = "OracleNode_Fail";

	final public static String ABOUT = "About";

	final public static String DEL = "Del";

	final public static String EDIT = "Edit";

	final public static String COPY = "Copy";

	final public static String CUT = "Cut";

	final public static String PASTE = "Paste";

	final public static String UP = "up";

	final public static String DOWN = "Down";
	
	final public static String GTTLIPSEREPORTFILE = "GTTlipse.rep";
	
	final public static String TESTRESULTFILE = "GUITestResult.txt";
	
	final public static String MFSMFILE = "MFSM.txt";
	final public static String MFSM_GRAPH_FILE = "MFSM.jpg";
	
	final public static String GTTLIPSEIMGFOLDER = "images/";
	
	final public static String TESTSCRIPTDOC_PATTERN = "^runner\\.GTTTestScript.*?;";
	
	final public static String TESTSCRIPTDOC_REPLACEPATTERN = "runner\\.GTTTestScript\\(this\\.getMethodName\\(\\),\"";
	
	final public static String OK = "OK";
	
	final public static String ERROR = "ERROR";
	
	final public static String REPLAY = "Replay";

}
