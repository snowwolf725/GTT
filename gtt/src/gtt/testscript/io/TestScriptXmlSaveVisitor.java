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
package gtt.testscript.io;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.testscript.AbstractNode;
import gtt.testscript.BreakerNode;
import gtt.testscript.CommentNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.SleeperNode;
import gtt.testscript.ViewAssertNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.fit.node.ReferenceFitNode;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;


/**
 * Visitor pattern XML File Save
 * 
 * @author Z.W Shen 2005/06/02
 */
public class TestScriptXmlSaveVisitor implements ITestScriptVisitor {
	protected org.w3c.dom.Document m_XmlDoc; /* XML Document */

	protected org.w3c.dom.Element m_XmlRoot; /* Root of document */

	/**
	 * 在遞回跑Visitor時，每個子節點應該要加在父節點(即Folder node)下 此變數暫時用來記錄每個節點應該加入的父節點
	 */
	protected org.w3c.dom.Element m_XmlParent;

	// Constructor
	public TestScriptXmlSaveVisitor() {
		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		init();
	}

	// add by David Wu(2007-06-06)
	public TestScriptXmlSaveVisitor(org.w3c.dom.Document doc) {
		m_XmlDoc = doc;
		init();
	}

	private void init() {
		m_XmlRoot = m_XmlDoc.createElement("TestScript");
		m_XmlParent = m_XmlRoot;
	}

	// add by David Wu(2007-06-06)
	public org.w3c.dom.Element getScriptRoot() {
		return m_XmlRoot;
	}

	/**
	 * 將XML Document 實際寫到外部檔案中
	 */
	public boolean saveFile(String filepath) {
		try {
			// Serialize DOM
			m_XmlDoc.appendChild(m_XmlRoot);

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
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(
				m_XmlDoc);

		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}

	/**
	 * for EventNode
	 */
	public void visit(EventNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("EventNode");
		// x.setAttribute("name", node.getName() );

		processSwingComponentPart(x, node.getComponent());
		processEventPart(x, node.getEvent());

		m_XmlParent.appendChild(x);
	}

	/**
	 * for ViewAssertionNode
	 */
	public void visit(ViewAssertNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("ViewAssertNode");
		// x.setAttribute("name", node.getName() );

		processSwingComponentPart(x, node.getComponent());
		processAssertionPart(x, node.getAssertion());

		m_XmlParent.appendChild(x);
	}

	/**
	 * for ModelAssertionNode
	 */
	public void visit(ModelAssertNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("ModelAssertNode");
		// x.setAttribute("name", node.getName() );
		x.setAttribute("filepath", node.getClassPath());
		try {
			x.setAttribute("method", node.getAssertMethod().toString());
		} catch (NullPointerException npe) {
			x.setAttribute("method", "");
		}

		m_XmlParent.appendChild(x);
	}

	public void visit(FolderNode node) {
		// Folder node 會呼叫每一個child node 進行 visitor
		// 因此本身為這些child nodes的parent
		// 更換parent
		org.w3c.dom.Element tempParent = m_XmlParent;
		m_XmlParent = m_XmlDoc.createElement("FolderNode");
		m_XmlParent.setAttribute("name", node.getName());
		tempParent.appendChild(m_XmlParent);

		Iterator<AbstractNode> iterator = node.iterator();
		while (iterator.hasNext()) {
			((AbstractNode) iterator.next()).accept(this);
		}

		// 換回原本的 parent
		m_XmlParent = tempParent;
	}

	public void visit(ReferenceMacroEventNode node) {
		org.w3c.dom.Element x = m_XmlDoc
				.createElement("ReferenceMacroEventNode");
		x.setAttribute("path", node.getRefPath());
		m_XmlParent.appendChild(x);
	}

	private void processSwingComponentPart(org.w3c.dom.Element x,
			IComponent component) {
		// SwingComponent 資訊也佔一個 xml element
		org.w3c.dom.Element xc = m_XmlDoc.createElement("SwingComponent");
		xc.setAttribute(ATTRIBUTE_WindowClassType, component.getWinType());
		xc.setAttribute(ATTRIBUTE_WindowTitle, component.getTitle());
		xc.setAttribute(ATTRIBUTE_ComponentClassType, component.getType());
		xc.setAttribute(ATTRIBUTE_ComponentName, component.getName());
		xc.setAttribute(ATTRIBUTE_IndexInWindow, Integer.toString(component
				.getIndex()));
		xc.setAttribute(ATTRIBUTE_IndexOfSameName, Integer.toString(component
				.getIndexOfSameName()));
		xc.setAttribute(ATTRIBUTE_Text, component.getText());
		// xc.setAttribute("name", name );
		x.appendChild(xc);
	}

	private void processEventPart(org.w3c.dom.Element x, IEvent e) {
		// EventInfo 資訊也佔一個 xml element
		org.w3c.dom.Element xe = m_XmlDoc.createElement("Event");
		xe.setAttribute("name", e.getName());
		xe.setAttribute("id", Integer.toString(e.getEventId()));
		// 每個 argument of event 也佔一個 xml element
		Iterator<Argument> ite = e.getArguments().iterator();
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			org.w3c.dom.Element xarg = m_XmlDoc.createElement("Argument");
			xarg.setAttribute("type", a.getType());
			xarg.setAttribute("name", a.getName());
			xarg.setAttribute("value", a.getValue());
			xe.appendChild(xarg);
		}
		x.appendChild(xe);
	}

	private void processAssertionPart(org.w3c.dom.Element parent, Assertion v) {
		org.w3c.dom.Element element = m_XmlDoc.createElement("Assertion");

		element.setAttribute("AssertValue", v.getValue());
		element.setAttribute("AssertMessage", v.getMessage());
		element.setAttribute("CompareOperator", Assertion.getOperatorString(v
				.getCompareOperator()));
		try {
			element.setAttribute("method", v.getMethod().toString());
		} catch (NullPointerException npe) {
			element.setAttribute("method", "");
		}

		// 每個 argument of event 也佔一個 xml element
		Iterator<Argument> ite = v.getArguments().iterator();
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			org.w3c.dom.Element xarg = m_XmlDoc.createElement("Argument");
			xarg.setAttribute("type", a.getType());
			xarg.setAttribute("name", a.getName());
			xarg.setAttribute("value", a.getValue());
			element.appendChild(xarg);
		}
		parent.appendChild(element);
	}

	public void visit(LaunchNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("LoadAUTNode");

		x.setAttribute("name", node.getClassName());
		x.setAttribute(ATTRIBUTE_AUT_CLASSNAME, node.getClassName());
		x.setAttribute(ATTRIBUTE_AUT_ARG, node.getArgument());
		x.setAttribute(ATTRIBUTE_AUT_CLASSPATH, node.getClassPath());

		m_XmlParent.appendChild(x);
	}

	public void visit(OracleNode node) {
		org.w3c.dom.Element tempParent = m_XmlParent;
		org.w3c.dom.Element x = m_XmlDoc.createElement("OracleNode");
		x.setAttribute("name", node.getName());
		x.setAttribute("Type", node.getOracleData().getLevel().ordinal() + "");
		x.setAttribute("EventType", node.getOracleData().getType().ordinal()
				+ "");
		x.setAttribute("UUID", node.getOracleData().getUUID().toString());
		m_XmlParent = x;
		Iterator<AbstractNode> ite = node.iterator();
		while (ite.hasNext()) {
			ViewAssertNode assertnode = (ViewAssertNode) ite.next();
			visit(assertnode);
		}
		tempParent.appendChild(x);

		// 換回原本的 parent
		m_XmlParent = tempParent;
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

	public final static String ATTRIBUTE_AUT_CLASSNAME = "AUT_File";

	public final static String ATTRIBUTE_AUT_CLASSPATH = "AUT_ClassPath";

	public final static String ATTRIBUTE_AUT_ARG = "AUT_ARG";

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
	public void visit(ReferenceFitNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("ReferenceFitNode");
		x.setAttribute("path", node.getReferencePath());
		m_XmlParent.appendChild(x);

	}

}
