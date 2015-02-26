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
/*
 * Created on 2005/3/16
 */
package gtt.macro;

import gtt.eventmodel.Argument;
import gtt.macro.io.MacroXmlReader;
import gtt.macro.io.XmlMacroSaveVisitor;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.macroStructure.SystemNode;

import java.io.InputStream;
import java.util.ArrayList;

public class MacroDocument {

	private AbstractMacroNode m_macroScript;
	private AbstractMacroNode m_systemNode = null;

	public MacroDocument() {
		m_macroScript = MacroComponentNode.create("AUT");
		if(!isHaveSystemNode()) {
			initSystemNode();
			if(m_systemNode != null)
				m_macroScript.add(m_systemNode);
		}
	}

	public MacroDocument(AbstractMacroNode root) {
		m_macroScript = root;
		if(!isHaveSystemNode()) {
			initSystemNode();
			if(m_systemNode != null)
				m_macroScript.add(m_systemNode);
		}
	}

	public AbstractMacroNode getMacroScript() {
		return m_macroScript;
	}

	public MacroDocument clone() {
		MacroDocument doc = new MacroDocument(m_macroScript.clone());
		return doc;
	}

	public static MacroDocument create() {
		return new MacroDocument();
	}

	public static MacroDocument createFromFile(String filepath) {
		MacroDocument doc = new MacroDocument();
		doc.openFile(filepath);
		return doc;
	}

	public void saveFileWithoutEvent(String filepath) {
		XmlMacroSaveVisitor v = new XmlMacroSaveVisitor();
		m_macroScript.accept(v);
		v.saveFile(filepath);
	}
	
	public boolean openFile(String filepath) {
		MacroXmlReader reader = new MacroXmlReader();
		return openFile(reader, filepath, null);
	}
	
	public boolean openFile(InputStream in) {
		MacroXmlReader reader = new MacroXmlReader();
		return openFile(reader, "", in);
	}
	
	public boolean openFile(MacroXmlReader reader, String filepath, InputStream in) {
		AbstractMacroNode newRoot = reader.read(filepath, in);
		if (newRoot == null)
			return false;

		m_macroScript = newRoot;
		if(!isHaveSystemNode()) {
			initSystemNode();
			if(m_systemNode != null)
				m_macroScript.add(m_systemNode);
		}		

		return true;
	}

	public void saveFile(String filepath) {
		XmlMacroSaveVisitor v = new XmlMacroSaveVisitor();
		m_macroScript.accept(v);
		v.saveFile(filepath);
	}

	public AbstractMacroNode findByPath(String path) {
		return new DefaultMacroFinder(m_macroScript).findByPath(path);
	}
	
	public void initSystemNode() {
		//add System node
		MacroNodeFactory factory = new MacroNodeFactory();		
		m_systemNode = factory.createSystemNode("SYSTEM");
		//get events list
		ArrayList<String> eventNameList = ((SystemNode) m_systemNode).getEventList();
		//set events
		for(int i = 0; i < eventNameList.size(); i++) {
			AbstractMacroNode macroEventNode = factory.createMacroEventNode();
			macroEventNode.setName(eventNameList.get(i));
			ArrayList<Argument> eventArgList = ((SystemNode) m_systemNode).getEventArgList(eventNameList.get(i));
			((MacroEventNode) macroEventNode).getArguments().clear();
			for(int j = 0; j < eventArgList.size(); j++) {
				((MacroEventNode) macroEventNode).getArguments().add(eventArgList.get(j));
			}
			((MacroEventNode) macroEventNode).setAutoState(true);
			m_systemNode.add(macroEventNode);
		}
	}

	private boolean isHaveSystemNode() {
		if(m_macroScript != null) {
			if(m_macroScript.getChildren().length==0)
				return false;
			for(int i = 0; i < m_macroScript.getChildren().length; i++) {
				if(m_macroScript.getChildren()[i].getName().equalsIgnoreCase("SYSTEM"))
					return true;
			}
		}
		return false;
	}
}
