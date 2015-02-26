package gttlipse.scriptEditor.views;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class ScriptViewLabelProvider extends LabelProvider {
	
	private ImageRegistry m_image_registry=null;
	
	private Set<Object> m_failnodes;
	
	private Vector<Object> m_coverageTable;
	
	ScriptViewLabelProvider(ImageRegistry image_registry,Set<Object> _failnodes) {
		super();
		m_image_registry = image_registry;
		m_failnodes = _failnodes;
		m_coverageTable = new Vector<Object>();
	}

	public String getText(Object obj) {
		if(obj instanceof BaseNode)
			return ((BaseNode)obj).getName();
		else if (obj instanceof TestScriptDocument)
			return ((TestScriptDocument)obj).getName();
		else if(obj instanceof AbstractNode)
			return ((AbstractNode)obj).toString();
		else return obj.toString();
	}

	public Image getImage(Object obj) {
		/*
		 *  Author : YuChiu Ku
		 *  In order to mark some special
		 */
		if(m_coverageTable.contains(obj)) {
			return null;
		}
		
		
		if (obj instanceof ProjectNode)
			return m_image_registry.get(TestScriptTage.PROJECT_NODE);
		else if (obj instanceof SourceFolderNode)
			return m_image_registry.get(TestScriptTage.SOURCE_FOLDER_NODE);
		else if (obj instanceof PackageNode)
			return m_image_registry.get(TestScriptTage.PACKAGE_NODE);
		else if (obj instanceof TestCaseNode)
			return m_image_registry.get(TestScriptTage.CLASS_NODE);
		else if (obj instanceof TestMethodNode)
			return m_image_registry.get(TestScriptTage.METHOD_NODE);
		else if (obj instanceof TestScriptDocument){
			boolean result = m_failnodes.contains(obj);
			if(result == true)
				return m_image_registry.get(TestScriptTage.TEST_SCRIPT_DOCUMENT_FAIL);
			else
				return m_image_registry.get(TestScriptTage.TEST_SCRIPT_DOCUMENT);
		}
		else if (obj instanceof FolderNode)
			return m_image_registry.get(TestScriptTage.FOLDER_NODE);
		else if (obj instanceof EventNode){
			boolean result = m_failnodes.contains(obj);
			if(result == true)
				return m_image_registry.get(TestScriptTage.EVENT_NODE_FAIL);
			else
				return m_image_registry.get(TestScriptTage.EVENT_NODE);
		}
		else if (obj instanceof ViewAssertNode){
			boolean result = m_failnodes.contains(obj);
			if(result == true)
				return m_image_registry.get(TestScriptTage.ASSERT_NODE_FAIL);
			else
				return m_image_registry.get(TestScriptTage.ASSERT_NODE);
		}
		else if (obj instanceof LaunchNode)
			return m_image_registry.get(TestScriptTage.AUT_INFO_NODE);
		
		else if (obj instanceof ReferenceMacroEventNode) {
			boolean result = m_failnodes.contains(obj);
			if(result == true)
				return m_image_registry.get(TestScriptTage.REFERENCE_MACRO_EVENT_NODE_FAIL);
			else
				return m_image_registry.get(TestScriptTage.REFERENCE_MACRO_EVENT_NODE);
		}
			
		else if (obj instanceof OracleNode){
			boolean result = m_failnodes.contains(obj);
			if(result == true)
				return m_image_registry.get(TestScriptTage.ORACLE_NODE_FAIL);
			else
				return m_image_registry.get(TestScriptTage.ORACLE_NODE);
		}
		else if (obj instanceof ReferenceFitNode) {
			return m_image_registry.get(TestScriptTage.REFERENCE_FIT_NODE);
		}
		
		else return null;
	}
}