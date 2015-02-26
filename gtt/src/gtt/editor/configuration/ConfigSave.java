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
package gtt.editor.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript.io
 * 
 */
public class ConfigSave {
	protected org.w3c.dom.Document m_XmlDoc; /* XML Document */

	protected org.w3c.dom.Element m_ConfigXmlRoot; /* Root of document */

	protected org.w3c.dom.Element m_XmlParent;

	protected IConfiguration m_Config;

	public ConfigSave(IConfiguration config) {
		m_Config = config;

		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		m_ConfigXmlRoot = m_XmlDoc.createElement("GTT");
		m_XmlParent = m_XmlDoc.createElement("Configuration");
		m_ConfigXmlRoot.appendChild(m_XmlParent);
	}

	// add by David Wu(2007-06-06)
	public ConfigSave(IConfiguration config, org.w3c.dom.Document doc) {
		m_Config = config;
		m_XmlDoc = doc;
		m_XmlParent = m_XmlDoc.createElement("Configuration");
	}

	// add by David Wu(2007-06-06)
	public org.w3c.dom.Element getConfigRoot() {
		return m_XmlParent;
	}

	// change by David Wu(2007-06-06)
	public void buildConfig() {
		// sleep time
		org.w3c.dom.Element sensitivity = m_XmlDoc.createElement("Sensitivity");
		sensitivity.setAttribute("time", "" + m_Config.getSensitivityTime());
		m_XmlParent.appendChild(sensitivity);

		// sleep time
		org.w3c.dom.Element sleeptime = m_XmlDoc.createElement("SleepTime");
		sleeptime.setAttribute("time", "" + m_Config.getSleepTime());
		m_XmlParent.appendChild(sleeptime);

		// sleep time
		org.w3c.dom.Element abs = m_XmlDoc.createElement("AbstractionLevel");
		abs.setAttribute("level", "" + m_Config.getAbstractionLevel());
		m_XmlParent.appendChild(abs);

		// Replay Mode
		org.w3c.dom.Element mode = m_XmlDoc.createElement("ReplayMode");
		mode.setAttribute("mode", "" + m_Config.getReplayMode());
		m_XmlParent.appendChild(mode);

		// Window title
		org.w3c.dom.Element wintitle = m_XmlDoc.createElement("WindowTitle");
		wintitle.setAttribute("title", m_Config.getWindowTitle());
		m_XmlParent.appendChild(wintitle);

		// Window class
		org.w3c.dom.Element winclass = m_XmlDoc.createElement("WindowClass");
		winclass.setAttribute("class", m_Config.getWindowClass());
		m_XmlParent.appendChild(winclass);

		// AUT path
		org.w3c.dom.Element autpath = m_XmlDoc.createElement("AUTFilePath");
		autpath.setAttribute("path", m_Config.getAUTFilePath());
		m_XmlParent.appendChild(autpath);

		// AUT argument
		org.w3c.dom.Element autargs = m_XmlDoc.createElement("AUTArgument");
		autargs.setAttribute("argument", m_Config.getAUTArgument());
		m_XmlParent.appendChild(autargs);

		// ClassPath
		org.w3c.dom.Element clspath = m_XmlDoc.createElement("ClassPath");
		clspath.setAttribute("path", m_Config.getClassPath());
		m_XmlParent.appendChild(clspath);

		// Invoke Method
		org.w3c.dom.Element invoke = m_XmlDoc.createElement("Invoke");
		invoke.setAttribute("method", m_Config.getInvokeMethod().toString());
		m_XmlParent.appendChild(invoke);

		// Oracle Collection
		org.w3c.dom.Element oracle = m_XmlDoc.createElement("Oracle");
		oracle.setAttribute("value", Boolean.toString(m_Config
				.getCollectOracle()));
		m_XmlParent.appendChild(oracle);
	}

	public boolean save(String filepath) {
		try {
			// test-script
			org.w3c.dom.Node ts_root = getXmlRootNode(filepath, "TestScript");
			if (ts_root != null) {
				m_ConfigXmlRoot.insertBefore(
						m_XmlDoc.importNode(ts_root, true), m_ConfigXmlRoot
								.getFirstChild());
			}

			// macro-structure
			ts_root = getXmlRootNode(filepath, "MacroStructure");
			if (ts_root != null) {
				m_ConfigXmlRoot.insertBefore(
						m_XmlDoc.importNode(ts_root, true), m_ConfigXmlRoot
								.getFirstChild());
			}

			buildConfig();

			/* config */
			m_XmlDoc.appendChild(m_ConfigXmlRoot);

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

	public org.w3c.dom.Node getXmlRootNode(String filepath, String tageName) {
		try {
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(filepath));

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();
			org.w3c.dom.Node childnode = node0.getFirstChild();
			do {
				childnode = childnode.getNextSibling();
				if (childnode == null)
					return null;
				if (childnode.getNodeName().compareTo(tageName) == 0)
					return childnode;
			} while (childnode != null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
