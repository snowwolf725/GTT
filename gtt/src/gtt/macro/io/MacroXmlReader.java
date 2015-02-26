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
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IHaveArgument;
import gtt.eventmodel.swing.SwingEvent;
import gtt.eventmodel.swing.SwingModel;
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
import gtt.macro.macroStructure.MacroContract;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class MacroXmlReader {
	
	// for GTT plug-in
	public int specificTestPlatformID(){ 
		return 1;
	}

	public org.w3c.dom.Node check(org.w3c.dom.Node gtt_node) {
		// 取得 GTT/MacroStructure 的部份 (at item 3)
		for (int i = 0; i < gtt_node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node cur = gtt_node.getChildNodes().item(i);
			// xml format 初步檢查
			if (cur.getNodeName().compareTo("MacroStructure") == 0)
				return cur;
		}

		return null;
	}

	public AbstractMacroNode read(String filename, InputStream in) {
		try {
			return doRead(filename, in);
		} catch (Exception e) {
			System.out.println("XmlFileLoader load file [ " + filename
					+ " ] error.");
			e.printStackTrace();
		}

		return MacroEventNode.createDefault();
	}

	private AbstractMacroNode doRead(String filename, InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		org.w3c.dom.Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		if (filename != "") {
			try {
				doc = builder.parse(new File(filename));
			} catch (FileNotFoundException fnfe) {
				// 沒有這個檔案
				return null;
			}
		} else {
			try {
				doc = builder.parse(in);
			} catch (FileNotFoundException fnfe) {
				// 沒有這個檔案
				return null;
			}
		}

		org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();

		// xml format 初步檢查
		if (node0.getNodeName().compareTo("GTT") != 0) {
			System.out.println("File: " + filename + " isn't GTT xml.");
			return null;
		}

		org.w3c.dom.Node macro_node = check(node0);
		if (macro_node == null) {
			System.out.println("File: " + filename
					+ " hasn't MacroStructure part.");
			return null;
		}

		for (int i = 0, length = macro_node.getChildNodes().getLength(); i < length; i++) {
			AbstractMacroNode newChild = parseAndDispatch(macro_node.getChildNodes().item(
					i));
			/* 找到一個可用的子樹即可，因為要避開重覆建立的 root 節點 */
			if (newChild != null) {
				return newChild;
			}
		}
		return null;
	}

	private AbstractMacroNode parseAndDispatch(Node node) {
		if (!isElementNode(node))
			return null;

		return dispatch((org.w3c.dom.Element) node);
	}

	private AbstractMacroNode dispatch(Element e) {
		if (e.getNodeName().compareTo("MacroEvent") == 0)
			return parseMacroEvent(e);
		if (e.getNodeName().compareTo("MacroEventCondition") == 0)
			return parseMacroEvent(e);
		if (e.getNodeName().equals("SingleMacroEvent")
				|| e.getNodeName().equals("MacroEventCaller"))
			return parseMacroEventCaller(e);
		if (e.getNodeName().compareTo("MacroComponent") == 0)
			return parseMacroComponent(e);
		if (e.getNodeName().compareTo("SystemNode") == 0)
			return parseMacroComponent(e);		
		if (e.getNodeName().compareTo("SingleComponent") == 0)
			return parseComponentNode(e);
		if (e.getNodeName().compareTo("DynamicComponent") == 0)
			return parseDynamicComponentNode(e);
		if (e.getNodeName().equals("SingleComponentEvent")
				|| e.getNodeName().equals("ComponentEvent"))
			return parseComponentEvent(e);
		if (e.getNodeName().compareTo("DynamicComponentEvent") == 0)
			return parseDynamicComponentEvent(e);
		if (e.getNodeName().compareTo("ViewAssert") == 0)
			return parseViewAssert(e);
		if (e.getNodeName().compareTo("ExistenceAssert") == 0)
			return parseExistenceAssertNode(e);
		if (e.getNodeName().compareTo("ModelAssert") == 0)
			return parseModelAssert(e);
		if (e.getNodeName().compareTo("NDefSingleComponent") == 0)
			return parseNDefSingleComponent(e);
		if (e.getNodeName().compareTo("SleeperNode") == 0)
			return processSleeperNode(e);
		if (e.getNodeName().compareTo("BreakerNode") == 0)
			return processBreakerNode(e);
		if (e.getNodeName().compareTo("CommentNode") == 0)
			return processCommentNode(e);
		if (e.getNodeName().compareTo("ConversionNode") == 0)
			return processEventTriggerNode(e);
		if (e.getNodeName().compareTo("FitTableAssertionNode") == 0)
			return processFitStateAssertionNode(e);
		if (e.getNodeName().compareTo("FitNode") == 0)
			return processFitNode(e);
		if (e.getNodeName().compareTo("GenerationalTypeINode") == 0)
			return processSplitDataAsNameNode(e);
		if (e.getNodeName().compareTo("GenerationalTypeIINode") == 0)
			return processGenerateOrderNameNode(e);
		if (e.getNodeName().compareTo("GenerationalTypeIIINode") == 0)
			return processFixNameNode(e);
		if (e.getNodeName().compareTo("FitAssertionNode") == 0)
			return processFitAssertionNode(e);
		if (e.getNodeName().compareTo("OracleNode") == 0)
			return processOracleNode(e);
		if (e.getNodeName().compareTo("LaunchNode") == 0)
			return processLaunchNode(e);
		if (e.getNodeName().compareTo("IncludeNode") == 0)
			return processIncludeNode(e);		
		if (e.getNodeName().compareTo("SplitDataNode") == 0)
			return parseSplitDataNode(e);
		return null;
	}

	private AbstractMacroNode parseMacroEvent(Element e) {
		MacroEventNode node = new MacroEventNode();
		node.setName(e.getAttribute("name"));
		node.setContract(readContract(e));
		
		// uhsing 2010/12/27 (For customized table header)
		node.setAutoState(readAutoState(e.getAttribute("auto")));
		
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			// 考慮 ArgumentSet 的部份
			if (aChild.getNodeName().equals("ArgumentSet"))
				readArgumentList(node, aChild);

			// 其它子節點
			AbstractMacroNode childNode = parseAndDispatch(e.getChildNodes().item(i));
			if (childNode == null)
				continue;
			node.add(childNode);
		}

		return node;
	}

	private MacroContract readContract(Element e) {
		MacroContract contract = new MacroContract();
		contract.setPreCondition(e.getAttribute("precondition"));
		contract.setAction(e.getAttribute("action"));
		contract.setPostCondition(e.getAttribute("postcondition"));
		try {
			contract.setLevel(Integer.parseInt(e.getAttribute("level")));
		} catch (Exception ep) {
			contract.setLevel(1);
		}
		return contract;
	}

	private boolean readAutoState(String state) {
		if (state.equals("true")) {
			return true;
		}
		else if (state.equals("false")) {
			return false;
		}
		else {
			// Unknown state of String is always returned false
			return false;
		}
	}
	
	private void readArgumentList(IHaveArgument node, Element aChild) {
		for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
			Element aArg = transToElementNode(aChild.getChildNodes().item(j));
			if (aArg == null)
				continue;
			if (aArg.getNodeName().equals("Argument")) {
				Argument arg = Argument.create(aArg.getAttribute("type"), aArg
						.getAttribute("name"), aArg.getAttribute("value"));
				node.getArguments().add(arg);
			}

		}
	}

	private AbstractMacroNode parseMacroEventCaller(Element e) {
		String path = e.getAttribute("ReferencePath");

		MacroEventCallerNode node = new MacroEventCallerNode(path);

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			if (aChild.getNodeName().equals("ArgumentSet"))
				readArgumentList(node, aChild);
		}

		return node;
	}

	private AbstractMacroNode parseMacroComponent(Element e) {
		MacroComponentNode node = new MacroComponentNode();
		node.setName(e.getAttribute("name"));
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			// 其它子節點
			AbstractMacroNode moreChildren = parseAndDispatch(e.getChildNodes().item(i));
			if (moreChildren == null)
				continue;
			node.add(moreChildren);
		}

		node.getEventCoverage().setup();
		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element child = transToElementNode(e.getChildNodes().item(i));
			if (child == null)
				continue;
			if (child.getNodeName().equals("EventCover") == false)
				continue;
			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				Element aChild = transToElementNode(child.getChildNodes()
						.item(j));
				if (aChild == null)
					continue;
				if (aChild.getNodeName().equals("Event") == false)
					continue;

				String nodename = aChild.getAttribute("name");
				node.getEventCoverage().addNeedToCover(nodename, true);

				boolean bCover = Boolean.parseBoolean(aChild
						.getAttribute("cover"));
				node.getEventCoverage().setCover(nodename, bCover);
			}
		}

		return node;
	}

	private AbstractMacroNode parseComponentNode(Element e) {
		ComponentNode node = ComponentNode.create();

		node.setComponentName(e.getAttribute("comName"));

		if (e.hasAttribute("name")) {
			node.setName(e.getAttribute("name"));
		} else {
			node.setName(node.getComponentName());
		}

		node.setWinType(e.getAttribute("winClassType"));
		node.setTitle(e.getAttribute("winTitle"));
		node.setType(e.getAttribute("comClassType"));
		node.setText(e.getAttribute("text"));
		node.setIndex(Integer.parseInt(e.getAttribute("indexInWindow")));
		node.setIndexOfSameName(Integer.parseInt(e.getAttribute("indexOfSameName")));

		node.getEventCoverage().setup();

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element child = transToElementNode(e.getChildNodes().item(i));
			if (child == null)
				continue;
			if (child.getNodeName().equals("EventCover") == false)
				continue;
			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				Element aChild = transToElementNode(child.getChildNodes().item(j));
				if (aChild == null)
					continue;
				if (aChild.getNodeName().equals("Event") == false)
					continue;

				String nodename = aChild.getAttribute("name");
				node.getEventCoverage().addNeedToCover(nodename, true);

				boolean bCover = Boolean.parseBoolean(aChild
						.getAttribute("cover"));
				node.getEventCoverage().setCover(nodename, bCover);
			}
		}

		return node;
	}

	private AbstractMacroNode parseDynamicComponentNode(Element e) {
		DynamicComponentNode node = DynamicComponentNode.create();
		
		node.setName(e.getAttribute("name"));
		node.setSource(e.getAttribute("source"));
		
		for(int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			
			if (aChild == null) {
				continue;
			}
			
			if (aChild.getNodeName().equals("ComponentInfo")) {
				node.setComponentName(aChild.getAttribute("comName"));
				node.setWinType(aChild.getAttribute("winClassType"));
				node.setTitle(aChild.getAttribute("winTitle"));
				node.setType(aChild.getAttribute("comClassType"));
				node.setText(aChild.getAttribute("text"));
				node.setIndex(Integer.parseInt(aChild.getAttribute("indexInWindow")));
				node.setIndexOfSameName(Integer.parseInt(aChild.getAttribute("indexOfSameName")));
			}
		}
		
		return node;
	}
	
	private AbstractMacroNode parseNDefSingleComponent(Element e) {
		NDefComponentNode node = NDefComponentNode.create();

		node.setWinType(e.getAttribute("winClassType"));
		node.setTitle(e.getAttribute("winTitle"));
		node.setType(e.getAttribute("comClassType"));
		node.setName(e.getAttribute("comName"));
		node.setText(e.getAttribute("text"));
		node.setIndex(Integer.parseInt(e.getAttribute("indexInWindow")));
		node.setIndexOfSameName(Integer.parseInt(e.getAttribute("indexOfSameName")));

		node.getEventCoverage().setup();

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element child = transToElementNode(e.getChildNodes().item(i));
			if (child == null)
				continue;
			if (child.getNodeName().equals("EventCover") == false)
				continue;
			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				Element aChild = transToElementNode(child.getChildNodes().item(j));
				if (aChild == null)
					continue;
				if (aChild.getNodeName().equals("Event") == false)
					continue;

				String nodename = aChild.getAttribute("name");
				node.getEventCoverage().addNeedToCover(nodename, true);

				boolean bCover = Boolean.parseBoolean(aChild
						.getAttribute("cover"));
				node.getEventCoverage().setCover(nodename, bCover);
			}
		}

		return node;
	}

	private AbstractMacroNode parseComponentEvent(Element e) {
		String path = e.getAttribute("ReferencePath");
		ComponentEventNode node;
		if (path != "") {
			// 新版本-用path 來找component
			node = new ComponentEventNode(path);
		} else {
			// 過渡版本 - 用type/name 來找component
			// String type = e.getAttribute("component_type");
			String name = e.getAttribute("component_name");
			node = new ComponentEventNode(name); // 用name 當path 即可
		}

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			if (aChild.getNodeName().equals("Event")) {
				node.setEvent(aChild.getAttribute("name"), Integer
						.parseInt(aChild.getAttribute("ID")));
				continue;
			}

			if (aChild.getNodeName().equals("DynamicComponent")) {
				node.setDyType(aChild.getAttribute("name"));
				node.setDyValue(aChild.getAttribute("value"));
				node.setDyIndex(Integer.valueOf(aChild.getAttribute("index")));
				continue;
			}

			if (aChild.getNodeName().equals("ArgumentSet")) {
				for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
					Element aArg = transToElementNode(aChild.getChildNodes()
							.item(j));
					if (aArg == null)
						continue;
					if (aArg.getNodeName().equals("Argument")) {
						Argument a = Argument.create(aArg.getAttribute("type"),
								aArg.getAttribute("name"), aArg
										.getAttribute("value"));
						node.getArguments().add(a);
					}
				}
				continue;
			}
		}

		return node;
	}

	private AbstractMacroNode parseDynamicComponentEvent(Element e) {
		String path = e.getAttribute("ReferencePath");
		DynamicComponentEventNode node = null;
		
		if (!path.equals("")) {
			node = new DynamicComponentEventNode(path);
		}
		else {
			node = new DynamicComponentEventNode();
		}

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			
			if (aChild == null) {
				continue;
			}
			
			if (aChild.getNodeName().equals("Event")) {
				node.setEvent(aChild.getAttribute("name"), 
						Integer.parseInt(aChild.getAttribute("ID")));
				continue;
			}

			if (aChild.getNodeName().equals("ArgumentSet")) {
				for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
					Element aArg = transToElementNode(aChild.getChildNodes().item(j));
					
					if (aArg == null) {
						continue;
					}
					
					if (aArg.getNodeName().equals("Argument")) {
						Argument a = Argument.create(aArg.getAttribute("type"),
								aArg.getAttribute("name"), aArg.getAttribute("value"));
						node.getArguments().add(a);
					}
				}
				continue;
			}
		}

		return node;
	}
	
	private AbstractMacroNode parseViewAssert(Element e) {
		String path = e.getAttribute("ReferencePath");

		ViewAssertNode node;
		if (path != "") {
			// 新版本-用path 來找component
			node = new ViewAssertNode(path);
		} else {
			// String type = e.getAttribute("component_type");
			String name = e.getAttribute("component_name");
			// 用name 當local path即可
			node = new ViewAssertNode(name);
		}

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;

			if (aChild.getNodeName().equals("AssertMethod")) {
				node.getAssertion().setMethodName(aChild.getAttribute("name"));
				node.getAssertion().setFullMethodName(aChild.getAttribute("fullName"));
				node.getAssertion().setValue(aChild.getAttribute("assertValue"));
				node.getAssertion().setMessage(aChild.getAttribute("message"));
				if (aChild.hasAttribute("resultNum"))// 避免影響之前版本，讀取預期結果數目會先判斷是否存在
					node.getAssertion().setExpectedSizeOfCheck(
							Integer.parseInt(aChild.getAttribute("resultNum")));
				continue;
			}

			if (aChild.getNodeName().equals("ArgumentSet")) {
				for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
					Element aArg = transToElementNode(aChild.getChildNodes()
							.item(j));

					if (aArg == null)
						continue;

					if (aArg.getNodeName().equals("Argument")) {
						Argument arg = Argument.create(aArg
								.getAttribute("type"), aArg
								.getAttribute("name"), aArg
								.getAttribute("value"));
						node.getArguments().add(arg);
					}
				}
				continue;
			}

			if (aChild.getNodeName().equals("DynamicComponent")) {
				node.setDyType(aChild.getAttribute("name"));
				node.setDyValue(aChild.getAttribute("value"));
				node.setDyIndex(Integer.valueOf(aChild.getAttribute("index")));
				continue;
			}
		}

		return node;
	}
	
	private static AbstractMacroNode parseExistenceAssertNode(Element e) {
		ExistenceAssertNode node = new MacroNodeFactory().createExistenceAssertNode();
		node.setComponentName(e.getAttribute("comName"));
		node.setWinType(e.getAttribute("winClassType"));
		node.setTitle(e.getAttribute("winTitle"));
		node.setType(e.getAttribute("comClassType"));
		node.setText(e.getAttribute("text"));
		node.setIndex(Integer.parseInt(e.getAttribute("indexInWindow")));
		node.setIndexOfSameName(Integer.parseInt(e.getAttribute("indexOfSameName")));
		node.setExpectResult(Boolean.valueOf(e.getAttribute("expectResult")));
		node.setExpectResultCount(Integer.valueOf(e.getAttribute("expectResultCount")));
		return node;
	}
	
	private AbstractMacroNode parseModelAssert(Element e) {
		ModelAssertNode node = new ModelAssertNode();
		node.setInfo(e.getAttribute("URL"), e.getAttribute("Method"));
		return node;
	}

	private AbstractMacroNode parseSplitDataNode(Element e) {
		String name = e.getAttribute("name");
		String target = e.getAttribute("target");
		
		// Use the specific name and target to create a SplitDataNode
		SplitDataNode node = new MacroNodeFactory().createSplitDataNode(name, target);
		
		for(int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			
			if (aChild == null) {
				continue;
			}
			
			if (aChild.getNodeName().equals("SplitWhat")) {
				node.setData(aChild.getAttribute("data"));
				node.setSeparator(aChild.getAttribute("separator"));
			}
		}
		
		return node;
	}
	
	private AbstractMacroNode processSleeperNode(Element e) {
		return new SleeperNode(Long.parseLong(e.getAttribute("time")));
	}

	private AbstractMacroNode processBreakerNode(Element e) {
		return new BreakerNode();
	}

	private AbstractMacroNode processCommentNode(Element e) {
		return new CommentNode(e.getAttribute("comment"));
	}

	private AbstractMacroNode processOracleNode(Element e) {
		OracleNode n = new OracleNode();
		n.setName(e.getAttribute("name"));
		n.getOracleData().setLevel(Integer.parseInt(e.getAttribute("Type")));
		n.getOracleData()
				.setType(Integer.parseInt(e.getAttribute("EventType")));
		n.getOracleData().setSubDir(e.getAttribute("subdir"));
		if (e.getAttribute("UUID") != null)
			n.getOracleData().setUUID(UUID.fromString(e.getAttribute("UUID")));

		return n;
	}

	private AbstractMacroNode processLaunchNode(Element e) {
		LaunchNode n = new LaunchNode();
		n.setClassName(e.getAttribute("LaunchClassName"));
		n.setClassPath(e.getAttribute("LaunchClassPath"));
		n.setArgument(e.getAttribute("LaunchArgument"));
		return n;
	}
	
	private AbstractMacroNode processIncludeNode(Element e) {
		IncludeNode n = new IncludeNode();
		n.setIncludeMacroComPath(e.getAttribute("includeMacroPath"));
		n.setIncludeMacroComName(e.getAttribute("includeMacroName"));
		return n;
	}	

	private AbstractMacroNode processEventTriggerNode(Element e) {
		IComponent ic = new SwingModel().createDefaultComponent();
		ic.setType(e.getAttribute("ComponentType"));
		ic.setTitle(e.getAttribute("WindowTitle"));
		ic.setWinType(e.getAttribute("WindowType"));
		List<IEvent> eventList = new ArrayList<IEvent>();
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			// 考慮 EventList部份
			if (aChild.getNodeName().equals("EventList"))
				readEventList(eventList, aChild);
		}

		return new EventTriggerNode(e.getAttribute("data"), e
				.getAttribute("GenerationKey"), ic, eventList);
	}

	private void readArgumentListToList(Arguments list, Element aChild) {
		for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
			Element aArg = transToElementNode(aChild.getChildNodes().item(j));
			if (aArg == null)
				continue;
			if (aArg.getNodeName().equals("Argument")) {
				Argument arg = Argument.create(aArg.getAttribute("type"), aArg
						.getAttribute("name"), aArg.getAttribute("value"));
				list.add(arg);
			}
		}
	}

	private void readEventList(List<IEvent> eventList, Element aChild) {
		for (int j = 0; j < aChild.getChildNodes().getLength(); j++) {
			Element event = transToElementNode(aChild.getChildNodes().item(j));
			if (event == null)
				continue;
			if (event.getNodeName().equals("Event"))
				eventList.add(readEvent(event));
		}
	}

	private IEvent readEvent(Element event) {
		IEvent ie = SwingEvent
				.create(Integer.parseInt(event.getAttribute("ID")), event
						.getAttribute("name"));
		Arguments argList = new Arguments();
		for (int j = 0; j < event.getChildNodes().getLength(); j++) {
			Element arglist = transToElementNode(event.getChildNodes().item(j));
			if (arglist == null)
				continue;
			if (arglist.getNodeName().equals("ArgumentSet")) {
				readArgumentListToList(argList, arglist);
				ie.setArguments(argList);
			}
		}
		return ie;
	}

	private AbstractMacroNode processFitStateAssertionNode(Element e) {
		FitStateAssertionNode node = new FitStateAssertionNode();
		node.setProjectPath(e.getAttribute("project"));
		node.setName(e.getAttribute("name"));
		node.setWindowTitle(e.getAttribute("windowTitle"));
		node.setWindowType(e.getAttribute("windowType"));
		node.setFitTableSource(new File(e.getAttribute("tablePath")));

		return node;
	}

	private AbstractMacroNode processFitNode(Element e) {
		FitNode node = new FitNode();

		node.setProjectPath(e.getAttribute("project"));
		node.setName(e.getAttribute("name"));
		node.setFixtureType(e.getAttribute("fixtureType"));
		node.setMacroEventCallerNode(new MacroEventCallerNode(e
				.getAttribute("macro")));
		node.setFitTableSource(new File(e.getAttribute("tablePath")));

		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;

			// 其它子節點
			AbstractMacroNode childNode = parseAndDispatch(e.getChildNodes().item(i));
			if (childNode == null)
				continue;
			node.add(childNode);
		}

		return node;
	}

	private AbstractMacroNode processSplitDataAsNameNode(Element e) {
		SplitDataAsNameNode node = new SplitDataAsNameNode();

		node.setVariable(e.getAttribute("variableNameOfData"));
		node.setName(e.getAttribute("name"));

		return node;
	}

	private AbstractMacroNode processGenerateOrderNameNode(Element e) {
		GenerateOrderNameNode node = new GenerateOrderNameNode();

		node.setVariableNameOfStart(e.getAttribute("variableNameOfStart"));
		node.setVariableNameOfEnd(e.getAttribute("variableNameOfEnd"));
		node.setPrefix(e.getAttribute("prefix"));
		node.setSuffix(e.getAttribute("suffix"));
		node.setName(e.getAttribute("name"));

		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			// 考慮 ArgumentSet 的部份
			if (aChild.getNodeName().equals("ArgumentSet"))
				readArgumentList(node, aChild);
		}

		return node;
	}

	private AbstractMacroNode processFixNameNode(Element e) {
		FixNameNode node = new FixNameNode();

		node.setComponentName(e.getAttribute("componentName"));
		node.setName(e.getAttribute("name"));

		return node;
	}

	private AbstractMacroNode processFitAssertionNode(Element e) {
		FitAssertionNode node = new FitAssertionNode();

		node.setComponentType(e.getAttribute("componentType"));
		node.setWindowType(e.getAttribute("wiindowType"));
		node.setWindowTitle(e.getAttribute("wiindowTitle"));
		node.setAssertionDataVariable(e.getAttribute("assertionDataVariable"));
		node.setGenerationKey(e.getAttribute("generationKey"));

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Element aChild = transToElementNode(e.getChildNodes().item(i));
			if (aChild == null)
				continue;
			if (aChild.getNodeName().equals("AssertMethod")) {
				node.getAssertion().setMethodName(aChild.getAttribute("name"));
				node.getAssertion().setMessage(aChild.getAttribute("message"));
				continue;
			}
		}

		return node;
	}

	private org.w3c.dom.Element transToElementNode(org.w3c.dom.Node node) {
		if (isElementNode(node))
			return (org.w3c.dom.Element) node;
		return null;
	}

	private boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}
}
