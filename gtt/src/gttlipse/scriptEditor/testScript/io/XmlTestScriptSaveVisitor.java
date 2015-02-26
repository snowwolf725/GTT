/**
 * 
 */
package gttlipse.scriptEditor.testScript.io;

import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gtt.testscript.io.TestScriptXmlSaveVisitor;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

/**
 * @author SnowWolf725
 * 
 * created first in project GTTlipse.scriptEditor.TestScript.visitor
 * 
 */
public class XmlTestScriptSaveVisitor extends TestScriptXmlSaveVisitor
		implements IGTTlipseScriptVisitor {

	public XmlTestScriptSaveVisitor()
	{
	}
	
	public XmlTestScriptSaveVisitor( org.w3c.dom.Document doc )
	{
		super( doc );
	}
	
	// for GTT plug-in
	public void setDocument(org.w3c.dom.Document doc) {
		m_XmlDoc = doc;
		m_XmlRoot = m_XmlDoc.createElement("TestScript");
		m_XmlParent = m_XmlRoot;
	}
	
	// for GTT plug-in
	public int specificTestPlatformID(){ 
		return 1;
	}
	
	public void visit(ProjectNode node) {
		// TestProjectNode node 穦㊣–child node 秈︽ visitor
		// セō硂ㄇchild nodesparent
		// 传parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("ProjectNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		for (int i = 0; i < node.size(); i++) {
			if (node.getChildrenAt(i) instanceof SourceFolderNode) {
				SourceFolderNode foldernode = (SourceFolderNode) node.getChildrenAt(i);
				foldernode.accept(this);
			}
		}

		// 传セ parent
		m_XmlParent = tempParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GTTlipse.scriptEditor.TestScript.visitor.IGTTlipseScriptVisitor#visit(GTTlipse.scriptEditor.TestScript.TestPackageNode)
	 */
	public void visit(PackageNode node) {
		// TestPackageNode node 穦㊣–child node 秈︽ visitor
		// セō硂ㄇchild nodesparent
		// 传parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("PackageNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		for (int i = 0; i < node.size(); i++) {
			if (node.getChildrenAt(i) instanceof PackageNode) {
				PackageNode packagenode = (PackageNode) node
						.getChildrenAt(i);
				packagenode.accept(this);
			} else if (node.getChildrenAt(i) instanceof TestCaseNode) {
				TestCaseNode classnode = (TestCaseNode) node.getChildrenAt(i);
				classnode.accept(this);
			}
		}

		// 传セ parent
		m_XmlParent = tempParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GTTlipse.scriptEditor.TestScript.visitor.IGTTlipseScriptVisitor#visit(GTTlipse.scriptEditor.TestScript.TestCaseNode)
	 */
	public void visit(TestCaseNode node) {
		// TestCaseNode node 穦㊣–child node 秈︽ visitor
		// セō硂ㄇchild nodesparent
		// 传parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("ClassNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		for (int i = 0; i < node.size(); i++) {
			TestMethodNode method = (TestMethodNode) node.getChildrenAt(i);
			method.accept(this);
		}

		// 传セ parent
		m_XmlParent = tempParent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GTTlipse.scriptEditor.TestScript.visitor.IGTTlipseScriptVisitor#visit(GTTlipse.scriptEditor.TestScript.TestMethodNode)
	 */
	public void visit(TestMethodNode node) {
		// TestCaseNode node 穦㊣–child node 秈︽ visitor
		// セō硂ㄇchild nodesparent
		// 传parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("MethodNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		for (int i = 0; i < node.size(); i++) {
			TestScriptDocument doc = node.getDocAt(i);
			processTestScript(doc);
		}

		// 传セ parent
		m_XmlParent = tempParent;
	}

	/**
	 * TestScriptDocument ぃ惠璶ㄏノ visit change to visit refactory by zwshen
	 * 2007/02/08
	 * 
	 * @param node
	 */
	private void processTestScript(TestScriptDocument node) {
		// TestScriptDocument node 穦㊣–child node 秈︽ visitor
		// セō硂ㄇchild nodesparent
		// 传parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("TestScriptDocument");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		if (node.getScript().getChildren() == null) {
			// 传セ parent
			m_XmlParent = tempParent;
			return;
		}

		for (int i = 0; i < node.getScript().getChildren().length; i++) {
			if (node.getScript().get(i) instanceof FolderNode)
				((FolderNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof EventNode)
				((EventNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof ModelAssertNode)
				((ModelAssertNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof ViewAssertNode)
				((ViewAssertNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof LaunchNode)
				((LaunchNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof ReferenceMacroEventNode)
				((ReferenceMacroEventNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof OracleNode)
				((OracleNode) node.getScript().get(i)).accept(this);
			else if (node.getScript().get(i) instanceof ReferenceFitNode)
				((ReferenceFitNode)node.getScript().get(i)).accept(this);
		}

		// 传セ parent
		m_XmlParent = tempParent;
	}

	/* (non-Javadoc)
	 * @see GTTlipse.scriptEditor.TestScript.visitor.IGTTlipseScriptVisitor#visit(GTTlipse.scriptEditor.TestScript.SourceFolderNode)
	 */
	public void visit(SourceFolderNode node) {
		// SourceFolderNode node 穦㊣– child node 秈︽ visitor
		// セō硂ㄇ child nodes  parent
		// 传 parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("SourceFolderNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		for (int i = 0; i < node.size(); i++) {
			if (node.getChildrenAt(i) instanceof PackageNode) {
				PackageNode packagenode = (PackageNode) node.getChildrenAt(i);
				packagenode.accept(this);
			} else if (node.getChildrenAt(i) instanceof TestCaseNode) {
				TestCaseNode classnode = (TestCaseNode) node.getChildrenAt(i);
				classnode.accept(this);
			}
		}

		// 传セ parent
		m_XmlParent = tempParent;		
	}

}
