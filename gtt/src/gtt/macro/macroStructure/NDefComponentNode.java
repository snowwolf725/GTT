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
package gtt.macro.macroStructure;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class NDefComponentNode extends ComponentNode implements Cloneable {

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	private EventCoverage m_coverage;

	private void initCoverage() {
		m_coverage = new EventCoverage(super.getComponent());
	}

	public NDefComponentNode(IComponent data) {
		super(data);
		initCoverage();
	}

	public EventCoverage getEventCoverage() {
		return m_coverage;
	}

	public static NDefComponentNode create(IComponent d) {
		return new NDefComponentNode(d);
	}

	public static NDefComponentNode create() {
		return create(SwingComponent.createDefault());
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_NDEFCOMPONENT_NODE;
	}
}