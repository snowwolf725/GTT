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
/**
 * 
 */
package gtt.oracle;

import gtt.eventmodel.EventModelFactory;
import gtt.util.refelection.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author SnowWolf
 * 
 *         created first in project gtt.util.refelection
 * 
 */
public class AssertableMethodLoader {

	private Map<String, List<Method>> mapClassToMethod = new HashMap<String, List<Method>>();

	private static String DEFINITION_FILE = EventModelFactory.getPluginPath()
			+ "descriptor/assertInfo.xml";

	public AssertableMethodLoader() {
		readAssertInfo();
	}

	public List<Method> getMethods(String classname) {
		return mapClassToMethod.get(classname);
	}

	private void readAssertInfo() {
		try {
			// init XML
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(DEFINITION_FILE));

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();
			if (node0 == null) {
				System.out.println("File: " + DEFINITION_FILE
						+ " isn't correct.");
				return;
			}
			/* Load Assert Info */
			for (int i = 0; i < node0.getChildNodes().getLength(); i++) {
				getTreeNodeFromDOM(node0.getChildNodes().item(i));
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	private void getTreeNodeFromDOM(org.w3c.dom.Node node) {
		if (isElementNode(node))
			processAsseertionNode((org.w3c.dom.Element) node);
	}

	/* for ViewAssertionNode element */
	/**
	 * 記錄每種Class Type 的所有可以進行Assert的函式列表
	 */
	private void processAsseertionNode(Element e) {
		String cls = e.getAttribute("componentClassType");

		List<Method> methods = new Vector<Method>();
		for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
			if (!isElementNode(e.getChildNodes().item(i)))
				continue;
			if (e.getChildNodes().item(i).getNodeName().compareTo("Assertion") != 0)
				continue;
			Element ec = (Element) e.getChildNodes().item(i);
			String methodname = ec.getAttribute("method");
			Method m = ReflectionUtil.getMethodFromFullString(cls, methodname);
			methods.add(m);
		}
		mapClassToMethod.put(cls, methods);
	}

}
