/**
 * 
 */
package gttlipse.scriptEditor.testScript.io;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.AbstractNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.TestProject;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.action
 * 
 */
public class LoadScript {
	private gttlipse.scriptEditor.testScript.CompositeNode parent = null;
	private gttlipse.scriptEditor.testScript.TestMethodNode m_methodnode = null;
	private AbstractNode docparent = null;
	private static NodeFactory m_NodeFactory = new NodeFactory();
	private IEventModel m_EventModel = null;
	
	public LoadScript() {
		m_EventModel = EventModelFactory.getDefault();
	}
	
	public LoadScript(IEventModel model){
		m_EventModel = model;
	}
		
	// for GTT plug-in
	public int specificTestPlatformID(){ 
		return 1;
	}

	public void readFile(String filename, InputStream is) {
		try {

			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			if(is == null)
				doc = builder.parse(new File(filename));
			else
				doc = builder.parse(is);

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();
			if (node0 == null) {
				System.out.println("File:" + filename + " isn't correct.");
				return;
			}
			// TestProject
			org.w3c.dom.Node node1 = ((Element) node0).getElementsByTagName(
					"ProjectNode").item(0);
			if (node1 == null) {
				System.out.println("File:" + filename + " isn't correct.");
				return;
			}

			ProjectNode projectnode = TestProject.getProject();
			projectnode.clearChildren();
			Element e = (org.w3c.dom.Element) node1;
			projectnode.setName(e.getAttribute("name"));

			for (int i = 0; i < node1.getChildNodes().getLength(); i++) {
				parent = projectnode;
				getTreeNodeFromDOM(node1.getChildNodes().item(i));
			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	private void getTreeNodeFromDOM(org.w3c.dom.Node node) {
		if (isElementNode(node))
			dispatch((org.w3c.dom.Element) node);
	}

	private void dispatch(Element e) {
		/* Test Script所需要的節點 */
		if (e.getNodeName().compareTo("SourceFolderNode") == 0)
			processSourceFolderNode(e);
		else if (e.getNodeName().compareTo("PackageNode") == 0)
			processPackageNode(e);
		else if (e.getNodeName().compareTo("ClassNode") == 0)
			processCaseNode(e);
		else if (e.getNodeName().compareTo("MethodNode") == 0)
			processMethodNode(e);
		else if (e.getNodeName().compareTo("TestScriptDocument") == 0)
			processDoc(e);
		else if (e.getNodeName().compareTo("FolderNode") == 0)
			processFolderNode(e);
		else if (e.getNodeName().compareTo("EventNode") == 0)
			processEventNode(e);
		else if (e.getNodeName().compareTo("ViewAssertNode") == 0)
			processViewAsseertionNode(e);
		else if (e.getNodeName().compareTo("LoadAUTNode") == 0)
			processAUTInfoNode(e);
		else if (e.getNodeName().compareTo("ReferenceMacroEventNode") == 0)
			processReferenceMacroEventNode(e);
		else if (e.getNodeName().compareTo("OracleNode") == 0)
			processOracleNodeNode(e);
		else if (e.getNodeName().compareTo("ReferenceFitNode") == 0)
			processReferenceFitNode(e);
	}

	/* for PackageNode element */
	private void processSourceFolderNode(Element e) {
		SourceFolderNode folder = new SourceFolderNode(e.getAttribute("name"));
		parent.addChild(folder);
		gttlipse.scriptEditor.testScript.CompositeNode tmp = parent;
		parent = folder;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
		parent = tmp;
	}

	/* for PackageNode element */
	private void processPackageNode(Element e) {
		PackageNode packagenode = new PackageNode(e.getAttribute("name"));
		parent.addChild(packagenode);
		gttlipse.scriptEditor.testScript.CompositeNode tmp = parent;
		parent = packagenode;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
		parent = tmp;
	}

	/* for CaseNode element */
	private void processCaseNode(Element e) {
		TestCaseNode classnode = new TestCaseNode(e.getAttribute("name"));
		parent.addChild(classnode);
		gttlipse.scriptEditor.testScript.CompositeNode tmp = parent;
		parent = classnode;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
		parent = tmp;
	}

	/* for MethodNode element */
	private void processMethodNode(Element e) {
		TestMethodNode methodnode = new TestMethodNode(e.getAttribute("name"));
		parent.addChild(methodnode);
		m_methodnode = methodnode;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
	}

	/* for ScriptDocument element */
	private void processDoc(Element e) {
		NodeFactory factory = new NodeFactory();
		AbstractNode root = factory.createFolderNode("Root");
		TestScriptDocument child = new TestScriptDocument(root);
		child.setName(e.getAttribute("name"));
		m_methodnode.addDocument(child);

		docparent = child.getScript();
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
	}

	/* for FolderNode element */
	private void processFolderNode(Element e) {
		AbstractNode folderNode = m_NodeFactory.createFolderNode(e
				.getAttribute("name"));
		docparent.add(folderNode);
		AbstractNode tmp = docparent;
		docparent = folderNode;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
		docparent = tmp;
	}

	/* for EventNode element */
	private void processEventNode(Element e) {
		try {
			IComponent sc = null;
			IEvent event = null;
			for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
				if (!isElementNode(e.getChildNodes().item(i)))
					continue;
				if (e.getChildNodes().item(i).getNodeName().compareTo(
						"SwingComponent") == 0)
					sc = getComponent((Element) e.getChildNodes().item(i));
				else if (e.getChildNodes().item(i).getNodeName().compareTo(
						"Event") == 0)
					event = getEvent((Element) e.getChildNodes().item(i));
			}
			if (sc == null || event == null)
				return;
			AbstractNode node = m_NodeFactory.createEventNode(sc, event);
			docparent.add(node);
		} catch (Exception exc) {
			System.out.println(exc);
		}
	}

	/* for ViewAssertionNode element */
	private void processViewAsseertionNode(Element e) {
		try {
			IComponent com = null;
			Assertion assertion = null;
			for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
				if (!isElementNode(e.getChildNodes().item(i)))
					continue;
				if (e.getChildNodes().item(i).getNodeName().compareTo(
						"SwingComponent") == 0)
					com = getComponent((Element) e.getChildNodes().item(i));
				else if (e.getChildNodes().item(i).getNodeName().compareTo(
						"Assertion") == 0)
					assertion = getAssertion((Element) e.getChildNodes()
							.item(i), com);
			}
			if (com == null || assertion == null)
				return;
			AbstractNode node = m_NodeFactory.createViewAssertNode(com,
					assertion);
			docparent.add(node);
		} catch (Exception exc) {
			System.out.println(exc);
		}
	}

	private void processAUTInfoNode(Element e) {
		try {
			NodeFactory factory = new NodeFactory();
			LaunchNode node = (LaunchNode) factory.createLaunchNode(e
					.getAttribute("name"), e.getAttribute(ATTRIBUTE_AUT_FILE));
			node.setArgument(e.getAttribute(ATTRIBUTE_AUT_ARG));
			node.setClassPath(e.getAttribute(ATTRIBUTE_AUT_CLASSPATH));
			docparent.add(node);
		} catch (Exception exc) {
			System.out.println(exc);
		}
	}

	/* 讀取 SwingCompoent 的資訊 */
	private IComponent getComponent(Element e) throws ClassNotFoundException {
		IComponent c = m_EventModel.getComponent(e
				.getAttribute(ATTRIBUTE_ComponentClassType));
		String winType = e.getAttribute(ATTRIBUTE_WindowClassType);
		String winTitle = e.getAttribute(ATTRIBUTE_WindowTitle);
		String comName = e.getAttribute(ATTRIBUTE_ComponentName);
		String text = e.getAttribute(ATTRIBUTE_Text);
		String indexWin = e.getAttribute(ATTRIBUTE_IndexInWindow);
		String indexSName = e.getAttribute(ATTRIBUTE_IndexOfSameName);

		c.setWinType(winType);
		c.setTitle(winTitle);
		c.setName(comName);
		c.setText(text);
		c.setIndex(Integer.parseInt(indexWin));
		c.setIndexOfSameName(Integer.parseInt(indexSName));

		return c;
	}

	/* 讀取 SwingEvent 的資訊 */
	private IEvent getEvent(Element e) {

		IEvent event = SwingEvent.create(
				Integer.parseInt(e.getAttribute("id")), e.getAttribute("name"));

		for (int i = 0, counts = e.getChildNodes().getLength(); i < counts; i++) {
			if (!isElementNode(e.getChildNodes().item(i)))
				continue;
			Element arg = (Element) e.getChildNodes().item(i);
			event.getArguments().add(
					Argument.create(arg.getAttribute("type"), arg
							.getAttribute("name"), arg.getAttribute("value")));
		}
		return event;
	}

	/* 讀取 Assertion 的資訊 */
	private Assertion getAssertion(Element e, IComponent c) {
		Assertion assertion = new Assertion();
		assertion.setValue(e.getAttribute("AssertValue"));
		assertion.setMessage(e.getAttribute("AssertMessage"));
		try {
			assertion.setMethod(ReflectionUtil.getMethod(Class.forName(c
					.getType()), e.getAttribute("method")));
		} catch (ClassNotFoundException e1) {
			System.out.println(e1);
			return null;
		}

		for (int i = 0, counts = e.getChildNodes().getLength(); i < counts; i++) {
			if (!isElementNode(e.getChildNodes().item(i)))
				continue;
			Element arg = (Element) e.getChildNodes().item(i);
			assertion.getArguments().add(
					Argument.create(arg.getAttribute("type"), arg
							.getAttribute("type"), arg.getAttribute("value")));
		}
		return assertion;
	}

	private void processReferenceMacroEventNode(Element e) {
		ReferenceMacroEventNode node = (ReferenceMacroEventNode) m_NodeFactory
				.createReferenceMacroEventNode(e.getAttribute("path"));
		docparent.add(node);
	}

	/* 讀取Reference Fit的資訊 */
	private void processReferenceFitNode(Element e) {
		ReferenceFitNode node = (ReferenceFitNode) m_NodeFactory
				.createReferenceFitNode(e.getAttribute("path"));
		docparent.add(node);
	}

	private void processOracleNodeNode(Element e) {
		OracleNode node = m_NodeFactory.createOracleNode();
		node.getOracleData().setLevel(Integer.parseInt(e.getAttribute("Type")));
		node.getOracleData().setType(Integer.parseInt(e.getAttribute("EventType")));
		if (e.getAttribute("UUID") != null)
			node.getOracleData().setUUID(UUID.fromString(e.getAttribute("UUID")));

		docparent.add(node);
		AbstractNode tmp = docparent;
		docparent = node;
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			getTreeNodeFromDOM(e.getChildNodes().item(i));
		}
		docparent = tmp;
	}

	// 可以套 typesafe enum pattern 06/05
	public final static String ATTRIBUTE_BranchName = "name";

	public final static String ATTRIBUTE_WindowClassType = "windowClassType";

	public final static String ATTRIBUTE_WindowTitle = "windowTitle";

	public final static String ATTRIBUTE_ComponentClassType = "componentClassType";

	public final static String ATTRIBUTE_ComponentName = "componentName";

	public final static String ATTRIBUTE_Text = "text";

	public final static String ATTRIBUTE_IndexInWindow = "indexInWindow";

	public final static String ATTRIBUTE_IndexOfSameName = "indexOfSameName";

	public final static String ATTRIBUTE_AUT_FILE = "AUT_File";

	public final static String ATTRIBUTE_AUT_CLASSPATH = "AUT_ClassPath";

	public final static String ATTRIBUTE_AUT_ARG = "AUT_ARG";
}
