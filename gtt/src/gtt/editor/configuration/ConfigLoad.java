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
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.TestScript.io
 * 
 */
public class ConfigLoad {

	private IConfiguration m_Config;

	public ConfigLoad(IConfiguration c) {
		m_Config = c;
	}

	public boolean load(String filename) {
		try {
			// init XML
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			try {
				doc = builder.parse(new File(filename));
			} catch (FileNotFoundException fnfe) {
				// no such file
				return false;
			}

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();
			if (node0 == null) {
				System.out.println("File:" + filename + " isn't correct.");
				return false;
			}
			/* Load Configure */
			org.w3c.dom.Node node1 = ((Element) node0).getElementsByTagName(
					"Configuration").item(0);
			if (node1 == null) {
				System.out.println("File:" + filename
						+ " hasn't Configuration information.");
				return false;
			}

			m_Config.setAbstractionLevel(Integer.parseInt(loadValue(node1,
					"AbstractionLevel", "level")));
			m_Config.setSensitivityTime(Integer.parseInt(loadValue(node1,
					"Sensitivity", "time")));
			m_Config.setSleepTime(Integer.parseInt(loadValue(node1,
					"SleepTime", "time")));
			m_Config.setReplayMode(Integer.parseInt(loadValue(node1,
					"ReplayMode", "mode")));
			m_Config.setWindowTitle(loadValue(node1, "WindowTitle", "title"));
			m_Config.setWindowClass(loadValue(node1, "WindowClass", "class"));
			m_Config.setAUTFilePath(loadValue(node1, "AUTFilePath", "path"));
			m_Config
					.setAUTArgument(loadValue(node1, "AUTArgument", "argument"));
			m_Config.setClassPath(loadValue(node1, "ClassPath", "path"));
			m_Config.setInvokeMethod((loadValue(node1, "Invoke", "method")));
			m_Config.setCollectOracle(Boolean.parseBoolean((loadValue(node1, "Oracle", "value"))));
			return true;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String loadValue(org.w3c.dom.Node node1, String name, String attr) {
		org.w3c.dom.Node n = ((Element) node1).getElementsByTagName(name).item(
				0);
		if (n != null) {
			Element e = (org.w3c.dom.Element) n;
			return e.getAttribute(attr);
		}
		return "";
	}
}
