package gttlipse;

import gtt.macro.MacroDocument;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.InvisibleRootNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.io.LoadConfig;
import gttlipse.scriptEditor.testScript.io.LoadScript;

import java.io.File;


public class TestProject {
	private static InvisibleRootNode m_scriptRoot = null;

	private static MacroDocument m_macroDocument = null;

	final static String DEFAULT_PROJECT_NAME = "(not set)";

	public TestProject() {
		initProject();
	}

	public static void initProject() {
		if (m_scriptRoot != null) {
			m_scriptRoot.clearChildren();
		}

		m_scriptRoot = new InvisibleRootNode();
		m_scriptRoot.addChild(new ProjectNode(DEFAULT_PROJECT_NAME));

		m_macroDocument = MacroDocument.create();
	}

	public static InvisibleRootNode getInstance() {
		if (m_scriptRoot == null)
			initProject();
		return m_scriptRoot;
	}

	public static ProjectNode getProject() {
		BaseNode projectnodes[] = getInstance().getChildren();
		return (ProjectNode) projectnodes[0];
	}

	public static MacroDocument getMacroDocument() {
		if (m_macroDocument == null)
			initProject();
		return m_macroDocument;
	}

	final public static String PROJECT_FILENAME = "GTTlipse.gtt";

	public static String getProjectFile() {
		return PROJECT_FILENAME;
	}

	public static void loadTestScript() {
		// use default project file
		loadTestScript(getProjectFile());
	}

	public static void loadTestScript(String gttfile) {
		LoadScript loader = new LoadScript();
		loader.readFile(gttfile, null);
	}
	
	// for GTT plug-in
	public static void loadTestScript(LoadScript loader, String gttfile) {
		loader.readFile(gttfile, null);
	}

	public static void updateScriptSync() {
		UpdateScript updater = new UpdateScript();
		updater.Sync();
	}

	public static GTTlipseConfig loadConfig() {
		// use default project file
		File file = new File(getProjectFile());
		if(file.exists())
			return loadConfig(getProjectFile());
		else
			return null;
	}

	public static GTTlipseConfig loadConfig(String gttfile) {
		LoadConfig configloader = new LoadConfig();
		configloader.readConfig(gttfile);
		return configloader.getConfig();
	}

}
