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
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.AbstractNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.util.refelection.FileClassLoader;
import gtt.util.refelection.ReflectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 從外部 xml 檔案讀取進來，展開一個 Test Script Tree
 * 
 * @author Z.W Shen
 * 
 */
public class TestScriptXmlReader implements IXmlFileReader {

	private static IEventModel m_SwingModel = EventModelFactory.getDefault();

	private static NodeFactory m_NodeFactory = new NodeFactory();

	public org.w3c.dom.Node check(org.w3c.dom.Node gtt_node) {
		// 取得 GTT/TestScript 的部份
		for (int i = 0; i < gtt_node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node cur = gtt_node.getChildNodes().item(i);
			// xml format 初步檢查
			if (cur.getNodeName().equals("TestScript"))
				return cur;
		}

		// no such format
		return null;
	}

	public AbstractNode read(String filename) {
		try {
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			try {
				doc = builder.parse(new File(filename));
			} catch (FileNotFoundException fnfe) {
				// 沒有這個檔案
				return null;
			}

			org.w3c.dom.Node node0 = doc.getFirstChild();

			// xml format 初步檢查
			if (node0.getNodeName().compareTo("GTT") != 0) {
				System.err.println("File:" + filename + " isn't GTT xml.");
				return null;
			}

			org.w3c.dom.Node node00 = check(node0);

			if (node00 == null) {
				System.err.println("File:" + filename
						+ " doesn't have the TestScript part.");
				return null;
			}

			for (int i = 0, length = node00.getChildNodes().getLength(); i < length; i++) {
				AbstractNode newChild = parse(node00.getChildNodes().item(i));
				if (newChild != null)
					return newChild; /* 找到一個可用的子樹即可，因為要避開重覆建立的 root 節點 */
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("XmlFileLoader load file [ " + filename
					+ " ] error.");
		}
		// default
		return m_NodeFactory.createFolderNode("root"); // empty tree with 1
		// root
	}

	private static boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	private AbstractNode parse(Node node) {
		if (isElementNode(node))
			return dispatch((org.w3c.dom.Element) node);
		return null;
	}

	private AbstractNode dispatch(Element e) {
		/* 四種Test Script所需要的節點 */
		if (e.getNodeName().equals("EventNode"))
			return processEventNode(e);
		if (e.getNodeName().compareTo("ViewAssertNode") == 0)
			return processViewAsseertionNode(e);
		if (e.getNodeName().compareTo("ModelAssertNode") == 0)
			return processModelAssertionNode(e);
		if (e.getNodeName().compareTo("FolderNode") == 0)
			return processFolderNode(e);
		if (e.getNodeName().compareTo("LoadAUTNode") == 0)
			return processLoadAUTNode(e);
		if (e.getNodeName().compareTo("ReferenceMacroEventNode") == 0)
			return processReferenceMacroEventNode(e);
		if (e.getNodeName().compareTo("SleeperNode") == 0)
			return processSleeperNode(e);
		if (e.getNodeName().compareTo("BreakerNode") == 0)
			return processBreakerNode(e);
		if (e.getNodeName().compareTo("CommentNode") == 0)
			return processCommentNode(e);
		if (e.getNodeName().compareTo("ReferenceFitNode") == 0)
			return processReferenceFitNode(e);
		if (e.getNodeName().equals("OracleNode"))
			return processOracleNode(e);
		return null;
	}

	/* 讀取 SwingCompoent 的資訊 */
	private IComponent getComponent(Element e) throws ClassNotFoundException {

		IComponent c = m_SwingModel.getComponent(e
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
			assertion.setCompareOperator(e.getAttribute("CompareOperator"));
		} catch (NullPointerException npe) {
			System.out.println("getAssertion op fail");
			assertion.setCompareOperator(Assertion.CompareOperator.EqualTo);
		}
		try {
			assertion.setMethod(ReflectionUtil.getMethod(Class.forName(c
					.getType()), e.getAttribute("method")));
		} catch (ClassNotFoundException e1) {
			System.out.println("ClassNotFoundException:" + e1);
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

	/* for EventNode element */
	private AbstractNode processEventNode(Element e) {
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
				return null;
			return m_NodeFactory.createEventNode(sc, event);
		} catch (Exception exc) {
			System.out.println("processEventNode " + exc);
		}
		return null; // exception
	}

	/* for ViewAssertionNode element */
	private AbstractNode processViewAsseertionNode(Element e) {
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
				return null;
			return m_NodeFactory.createViewAssertNode(com, assertion);
		} catch (Exception exc) {
			System.out.println("processViewAsseertionNode " + exc);
		}
		return null; // exception
	}

	/* for ModelAssertionNode element */
	private AbstractNode processModelAssertionNode(Element e) {
		ModelAssertNode ma = (ModelAssertNode) m_NodeFactory
				.createModelAssertNode();
		ma.setClassPath(e.getAttribute("filepath"));

		if (ma.getClassPath() == null || ma.getClassPath() == "")
			return ma;

		// 使用Reflection 取得 method
		try {
			Class<?> c = null;
			c = new FileClassLoader().loadFile(ma.getClassPath() + ".class");
			if (c != null) {
				ma.setAssertMethod(ReflectionUtil.getMethod(c, e
						.getAttribute("method")));
			}
		} catch (ClassNotFoundException e1) {
			System.out.println(e1);
		}
		// ma.setAssertMethod( e.getAttribute("method") );

		return ma;
	}

	/* for FolderNode element */
	private AbstractNode processFolderNode(Element e) {
		AbstractNode folder = m_NodeFactory.createFolderNode(e
				.getAttribute("name"));
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			AbstractNode newChild = parse(e.getChildNodes().item(i));
			if (newChild != null)
				folder.add(newChild);
		}
		return folder;
	}

	/* for ReferenceMacroEventNode element */
	private AbstractNode processReferenceMacroEventNode(Element e) {
		AbstractNode node = m_NodeFactory.createReferenceMacroEventNode(e
				.getAttribute("path"));
		return node;
	}

	/* for LoadAUTNode element */
	private AbstractNode processLoadAUTNode(Element e) {
		LaunchNode node = m_NodeFactory.createLaunchNode(
				e.getAttribute("name"), e.getAttribute("AUT_File"));
		node.setArgument(e.getAttribute("AUT_ARG"));
		return node;
	}

	private AbstractNode processSleeperNode(Element e) {
		return m_NodeFactory.createSleeperNode(Long.parseLong(e
				.getAttribute("time")));
	}

	private AbstractNode processBreakerNode(Element e) {
		return m_NodeFactory.createBreakerNode();
	}

	private AbstractNode processCommentNode(Element e) {
		return m_NodeFactory.createCommentNode(e.getAttribute("comment"));
	}

	private AbstractNode processReferenceFitNode(Element e) {
		return m_NodeFactory.createReferenceFitNode(e.getAttribute("path"));
	}

	private OracleNode processOracleNode(Element e) {
		OracleNode node = m_NodeFactory.createOracleNode();
		node.setName( e.getAttribute("name"));
		node.getOracleData().setLevel(Integer.parseInt(e.getAttribute("Type")));
		node.getOracleData().setType(Integer.parseInt(e.getAttribute("EventType")));
		if(e.getAttribute("UUID") != null && e.getAttribute("UUID") != "" ) 
			node.getOracleData().setUUID(UUID.fromString(e.getAttribute("UUID")));

//		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
//			AbstractNode newChild = parse(e.getChildNodes().item(i));
//			if (newChild != null)
//				node.add(newChild);
//		}
		return node;
	}
}
