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

import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.ViewAssertNode;

// 不儲存Event資訊 -zwhsen 2008/05/19
public class XmlMacroSaveNoEventVisitor extends XmlMacroSaveVisitor {

	// Constructor
	public XmlMacroSaveNoEventVisitor() {
		super();
	}

	public XmlMacroSaveNoEventVisitor(org.w3c.dom.Document doc) {
		super(doc);
	}

	public void visit(ComponentEventNode node) {
		// needn't save event
	}

	public void visit(MacroEventNode node) {
		// needn't save event
	}

	public void visit(ModelAssertNode node) {
		// needn't save event
	}

	public void visit(MacroEventCallerNode node) {
		// needn't save event
	}

	public void visit(ViewAssertNode node) {
		// needn't save event
	}

}
