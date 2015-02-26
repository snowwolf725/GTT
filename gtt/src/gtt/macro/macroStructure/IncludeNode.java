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
package gtt.macro.macroStructure;

import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class IncludeNode extends AbstractMacroNode {
	
	private String _macroComPath = null; 
	private String _macroComName = "";

	public IncludeNode() {
		
	}
	
	public IncludeNode(MacroPath includeMacroComPath, String includeMacroComName) {
		setIncludeMacroComPath(includeMacroComPath.toString());
		setIncludeMacroComName(includeMacroComName);
	}
	
	public IncludeNode(String includeMacroComPath, String includeMacroComName) {
		setIncludeMacroComPath(includeMacroComPath);
		setIncludeMacroComName(includeMacroComName);
	}	
	
	private IncludeNode(IncludeNode node) {
		_macroComPath = node.getIncludeMacroComPath();
		_macroComName = node.getIncludeMacroComName();
	}
	
	public void setIncludeMacroComPath(String path) {
		_macroComPath = path;
	}
	
	public String getIncludeMacroComPath() {
		return _macroComPath;
	}
	
	public void setIncludeMacroComName(String name) {
		_macroComName = name;
	}
	
	public String getIncludeMacroComName() {
		return _macroComName;
	}	
	
	public String getName() {
		return this.toString();
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}
	
	@Override
	public IncludeNode clone() {
		return new IncludeNode(this);
	}
	
	@Override
	public String toString() {
		return "Include:" + getIncludeMacroComName();
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_INCLUDE_NODE;
	}	

}
