/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.macro.io;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.visitor.IMacroStructureVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;


public class XmlMacroSaveVisitor implements IMacroStructureVisitor {

	protected org.w3c.dom.Document m_XmlDoc; /* XML Document */

	protected org.w3c.dom.Element m_MacroXmlRoot; /* Root of document */

	/**
	 * 在遞回跑Visitor時，每個子節點應該要加在父節點(即Folder node)下 此變數暫時用來記錄每個節點應該加入的父節點
	 */
	protected org.w3c.dom.Element m_XmlParent;

	// Constructor
	public XmlMacroSaveVisitor() {
		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		m_MacroXmlRoot = m_XmlDoc.createElement("GTT");
		m_XmlParent = m_XmlDoc.createElement("MacroStructure");
		m_MacroXmlRoot.appendChild(m_XmlParent);
	}

	// add by David Wu(2007-06-06)
	public XmlMacroSaveVisitor(org.w3c.dom.Document doc) {
		m_XmlDoc = doc;
		m_XmlParent = m_XmlDoc.createElement("MacroStructure");
	}

	// add by David Wu(2007-06-06)
	public org.w3c.dom.Element getMacroRoot() {
		return m_XmlParent;
	}

	public org.w3c.dom.Node getTestScriptXmlRootNode(String filepath) {
		try {
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File macrofile = new File(filepath);
			if (macrofile.exists() == false)
				return null;
			doc = builder.parse(macrofile);

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();

			// Parser parser = new org.apache.xerces.parsers.DOMParser();
			// parser.parse(filepath);
			//
			// org.w3c.dom.Document doc = parser.getDocument();
			// org.w3c.dom.Node node0 = doc.getChildNodes().item(0);

			if (node0.getNodeName().compareTo("TestScript") == 0)
				return node0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 將XML Document 實際寫到外部檔案中
	 */
	public boolean saveFile(String filepath) {
		try {
			// test-script
			org.w3c.dom.Node ts_root = getTestScriptXmlRootNode(filepath);
			if (ts_root != null) {
				m_MacroXmlRoot.insertBefore(m_XmlDoc.importNode(ts_root, true),
						m_MacroXmlRoot.getFirstChild());
			}

			// macro-structure
			m_XmlDoc.appendChild(m_MacroXmlRoot);

			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat());
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(m_XmlDoc.getDocumentElement());

			PrintWriter writer = new PrintWriter(new FileOutputStream(filepath));
			writer.print(stringOut.toString());
			writer.flush();
			writer.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private com.sun.org.apache.xml.internal.serialize.OutputFormat createXmlFormat() {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(m_XmlDoc);
		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}

	@Override
	public void visit(ComponentEventNode node) {
		if (node.getComponent() == null)
			return;

		org.w3c.dom.Element child = m_XmlDoc.createElement("ComponentEvent");

		if (node.getComponent()==null) {
			child.setAttribute("ReferencePath", node.getComponentPath());
		}
		else {
			child.setAttribute("ReferencePath", node.getComponent().getPath().toString());
		}

		// = = = = = = = = = = = = = = = = = = =
		org.w3c.dom.Element method = m_XmlDoc.createElement("Event");
		method.setAttribute("name", node.getEventType());
		method.setAttribute("ID", Integer.toString(node.getEventID()));
		child.appendChild(method);

		org.w3c.dom.Element dynamicComponent = m_XmlDoc.createElement("DynamicComponent");
		dynamicComponent.setAttribute("name", node.getDyType());
		dynamicComponent.setAttribute("value", node.getDyValue());
		dynamicComponent.setAttribute("index", String.valueOf(node.getDyIndex()));
		child.appendChild(dynamicComponent);
		
		// Save ArgumentList
		child.appendChild(createArgumentListXml(node));

		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		// When the dynamic component is not existed
		if (node.getComponent() == null) {
			return;
		}
		
		org.w3c.dom.Element child = m_XmlDoc.createElement("DynamicComponentEvent");
		child.setAttribute("ReferencePath", node.getComponent().getPath().toString());
		
		org.w3c.dom.Element event = m_XmlDoc.createElement("Event");
		event.setAttribute("name", node.getEventType());
		event.setAttribute("ID", Integer.toString(node.getEventID()));
		
		org.w3c.dom.Element argument = createArgumentListXml(node);
		
		child.appendChild(event);
		child.appendChild(argument);
		m_XmlParent.appendChild(child);
	}
	
	@Override
	public void visit(ComponentNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("SingleComponent");
		
		// = = = = = = = = = = = = = = = = = = =
		child.setAttribute("name", node.getName());
		child.setAttribute("winClassType", node.getWinType());
		child.setAttribute("winTitle", node.getTitle());
		child.setAttribute("comClassType", node.getType());
		child.setAttribute("comName", node.getComponentName());
		child.setAttribute("text", node.getText());
		child.setAttribute("indexInWindow", Integer.toString(node.getIndex()));
		child.setAttribute("indexOfSameName", Integer.toString(node.getIndexOfSameName()));

		org.w3c.dom.Element grandchild = m_XmlDoc.createElement("EventCover");
		EventCoverage c = node.getEventCoverage();
		Set<String> s = c.getNeedToCoverSet();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			String event = (String) ite.next();
			org.w3c.dom.Element tmp = m_XmlDoc.createElement("Event");
			tmp.setAttribute("name", event);
			Boolean cover = c.isCover(event);
			tmp.setAttribute("cover", cover.toString());
			grandchild.appendChild(tmp);
		}
		child.appendChild(grandchild);
		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(DynamicComponentNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("DynamicComponent");
		
		child.setAttribute("name", node.getName());
		child.setAttribute("source", node.getSource());
		
		org.w3c.dom.Element grandChild = m_XmlDoc.createElement("ComponentInfo");
		
		grandChild.setAttribute("winClassType", node.getWinType());
		grandChild.setAttribute("winTitle", node.getTitle());
		grandChild.setAttribute("comClassType", node.getType());
		grandChild.setAttribute("comName", node.getComponentName());
		grandChild.setAttribute("text", node.getText());
		grandChild.setAttribute("indexInWindow", Integer.toString(node.getIndex()));
		grandChild.setAttribute("indexOfSameName", Integer.toString(node.getIndexOfSameName()));
		
		child.appendChild(grandChild);
		m_XmlParent.appendChild(child);
	}
	
	@Override
	public void visit(MacroComponentNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("MacroComponent");
		
		// = = = = = = = = = = = = = = = = = = =
		child.setAttribute("name", node.getName());

		org.w3c.dom.Element grandchild = m_XmlDoc.createElement("EventCover");

		EventCoverage c = node.getEventCoverage();
		Set<String> s = c.getNeedToCoverSet();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			String event = (String) ite.next();
			org.w3c.dom.Element tmp = m_XmlDoc.createElement("Event");
			tmp.setAttribute("name", event);
			Boolean cover = c.isCover(event);
			tmp.setAttribute("cover", cover.toString());
			grandchild.appendChild(tmp);
		}
		child.appendChild(grandchild);
		m_XmlParent.appendChild(child);

		// for save children
		saveChildren(node, child);
	}

	@Override
	public void visit(MacroEventNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("MacroEvent");

		child.setAttribute("name", node.getName());
		child.setAttribute("precondition", node.getContract().getPreCondition());
		child.setAttribute("action", node.getContract().getAction());
		child.setAttribute("postcondition", node.getContract().getPostCondition());
		child.setAttribute("level", "" + node.getContract().getLevel());

		// uhsing 2010/12/27 (For customized table header)
		child.setAttribute("auto", String.valueOf(node.isAutoParsing()));
		
		// save ArgumentList
		child.appendChild(createArgumentListXml(node));
		m_XmlParent.appendChild(child);

		// for save children
		saveChildren(node, child);
	}

	protected org.w3c.dom.Element createArgumentListXml(IHaveArgument node) {
		org.w3c.dom.Element argSet = m_XmlDoc.createElement("ArgumentSet");
		
		Iterator<Argument> ite = node.getArguments().iterator();
		while (ite.hasNext()) {
			Argument arg_obj = (Argument) ite.next();
			org.w3c.dom.Element arg = m_XmlDoc.createElement("Argument");
			arg.setAttribute("name", arg_obj.getName());
			arg.setAttribute("type", arg_obj.getType());
			arg.setAttribute("value", arg_obj.getValue());
			argSet.appendChild(arg);
		}
		return argSet;
	}

	protected org.w3c.dom.Element createArgumentListXmlFromList(Arguments list) {
		org.w3c.dom.Element argSet = m_XmlDoc.createElement("ArgumentSet");
		
		Iterator<Argument> ite = list.iterator();
		while (ite.hasNext()) {
			Argument arg_obj = (Argument) ite.next();
			org.w3c.dom.Element arg = m_XmlDoc.createElement("Argument");
			arg.setAttribute("name", arg_obj.getName());
			arg.setAttribute("type", arg_obj.getType());
			arg.setAttribute("value", arg_obj.getValue());
			argSet.appendChild(arg);
		}
		return argSet;
	}

	protected void saveChildren(AbstractMacroNode node, org.w3c.dom.Element child) {
		org.w3c.dom.Element tempParent = m_XmlParent;
		
		m_XmlParent = child;
		for (int i = 0; i < node.size(); i++) {
			// 每一個child node都要做存檔動作 zws 2007/04/03
			((AbstractMacroNode) node.get(i)).accept(this);
		}
		// 換回parent
		m_XmlParent = tempParent;
	}

	@Override
	public void visit(ModelAssertNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("ModelAssert");
		
		child.setAttribute("URL", node.getClassUrl());
		child.setAttribute("Method", node.getMethod());

		m_XmlParent.appendChild(child);
	}

	/**
	 * SingleMacroEvent 主要能存入 comPath 及 arguments
	 */
	@Override
	public void visit(MacroEventCallerNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("MacroEventCaller");

		child.setAttribute("ReferencePath", node.getReferencePath());
		// Save ArgumentList
		child.appendChild(createArgumentListXml(node));
		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(ViewAssertNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("ViewAssert");
		
		if (node.getComponent() == null) {
			child.setAttribute("ReferencePath", node.getComponentPath());
		}
		else {
			child.setAttribute("ReferencePath", node.getComponent().getPath().toString());
		}
		
		org.w3c.dom.Element method = m_XmlDoc.createElement("AssertMethod");
		
		method.setAttribute("name", node.getAssertion().getMethodName());
		method.setAttribute("fullName", node.getAssertion().getFullMethodName());
		method.setAttribute("assertValue", node.getAssertion().getValue());
		method.setAttribute("message", node.getAssertion().getMessage());
		method.setAttribute("resultNum", String.valueOf(node.getAssertion().getExpectedSizeOfCheck()));
		child.appendChild(method);
		
		org.w3c.dom.Element dynamicComponent = m_XmlDoc.createElement("DynamicComponent");
		
		dynamicComponent.setAttribute("name", node.getDyType());
		dynamicComponent.setAttribute("value", node.getDyValue());
		dynamicComponent.setAttribute("index", String.valueOf(node.getDyIndex()));
		child.appendChild(dynamicComponent);

		// Save ArgumentList
		child.appendChild(createArgumentListXml(node));

		m_XmlParent.appendChild(child);
	}
	
	@Override
	public void visit(ExistenceAssertNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("ExistenceAssert");
		
		// = = = = = = = = = = = = = = = = = = =
		child.setAttribute("winClassType", node.getWinType());
		child.setAttribute("winTitle", node.getTitle());
		child.setAttribute("comClassType", node.getType());
		child.setAttribute("comName", node.getComponentName());
		child.setAttribute("text", node.getText());
		child.setAttribute("indexInWindow", Integer.toString(node.getIndex()));
		child.setAttribute("indexOfSameName", Integer.toString(node.getIndexOfSameName()));
		child.setAttribute("expectResult", String.valueOf(node.getExpectResult()));
		child.setAttribute("expectResultCount", String.valueOf(node.getExpectResultCount()));
		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(gtt.macro.macroStructure.NDefComponentNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("NDefSingleComponent");
		
		// = = = = = = = = = = = = = = = = = = =
		child.setAttribute("winClassType", node.getWinType());
		child.setAttribute("winTitle", node.getTitle());
		child.setAttribute("comClassType", node.getType());
		child.setAttribute("comName", node.getName());
		child.setAttribute("text", node.getText());
		child.setAttribute("indexInWindow", Integer.toString(node.getIndex()));
		child.setAttribute("indexOfSameName", Integer.toString(node.getIndexOfSameName()));
		child.setAttribute("test", "test");

		org.w3c.dom.Element grandchild = m_XmlDoc.createElement("EventCover");
		EventCoverage c = node.getEventCoverage();
		Set<String> s = c.getNeedToCoverSet();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			String event = (String) ite.next();
			org.w3c.dom.Element tmp = m_XmlDoc.createElement("Event");
			tmp.setAttribute("name", event);
			Boolean cover = c.isCover(event);
			tmp.setAttribute("cover", cover.toString());
			grandchild.appendChild(tmp);
		}
		child.appendChild(grandchild);
		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(SleeperNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("SleeperNode");
		
		x.setAttribute("time", "" + node.getSleepTime());
		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(BreakerNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("BreakerNode");
		
		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(CommentNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("CommentNode");
		
		x.setAttribute("comment", "" + node.getComment());
		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(OracleNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("OracleNode");
		
		x.setAttribute("name", node.getName());
		x.setAttribute("Type", node.getOracleData().getLevel().ordinal() + "");
		x.setAttribute("EventType", node.getOracleData().getType().ordinal() + "");
		x.setAttribute("UUID", node.getOracleData().getUUID().toString());
		x.setAttribute("subdir", node.getOracleData().getSubDir());
		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(LaunchNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("LaunchNode");
		
		x.setAttribute("LaunchClassName",  node.getClassName());
		x.setAttribute("LaunchClassPath",  node.getClassPath());
		x.setAttribute("LaunchArgument",  node.getArgument());
		m_XmlParent.appendChild(x);		
	}

	@Override
	public void visit(SplitDataNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("SplitDataNode");
		
		child.setAttribute("name", node.getName());
		child.setAttribute("target", node.getTarget());
		
		org.w3c.dom.Element grandChild = m_XmlDoc.createElement("SplitWhat");
		
		grandChild.setAttribute("data", node.getData());
		grandChild.setAttribute("separator", node.getSeparator());
		
		child.appendChild(grandChild);
		m_XmlParent.appendChild(child);
	}

	@Override
	public void visit(IncludeNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("IncludeNode");
		
		child.setAttribute("includeMacroPath", node.getIncludeMacroComPath());
		child.setAttribute("includeMacroName", node.getIncludeMacroComName());
		
		m_XmlParent.appendChild(child);
	}
	
	@Override
	public void visit(SystemNode node) {
		org.w3c.dom.Element child = m_XmlDoc.createElement("SystemNode");
		
		// = = = = = = = = = = = = = = = = = = =
		child.setAttribute("name", node.getName());

		org.w3c.dom.Element grandchild = m_XmlDoc.createElement("EventCover");

		EventCoverage c = node.getEventCoverage();
		Set<String> s = c.getNeedToCoverSet();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			String event = (String) ite.next();
			org.w3c.dom.Element tmp = m_XmlDoc.createElement("Event");
			tmp.setAttribute("name", event);
			Boolean cover = c.isCover(event);
			tmp.setAttribute("cover", cover.toString());
			grandchild.appendChild(tmp);
		}
		child.appendChild(grandchild);
		m_XmlParent.appendChild(child);

		// for save children
		saveChildren(node, child);
	}	
}
