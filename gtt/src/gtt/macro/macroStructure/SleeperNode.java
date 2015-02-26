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

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

/**
 * @author SnowWolf
 *
 *         created first in project GTTlipse.scriptEditor.TestScript
 */
public class SleeperNode extends AbstractMacroNode {

	private long sleep_time = 100l; // 100ms

	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return sleep_time + " ms";
	}

	public AbstractMacroNode clone() {
		return new SleeperNode(sleep_time);
	}

	public SleeperNode(long time) {
		super();
		this.sleep_time = time;
	}

	public void setSleepTime(long time) {
		this.sleep_time = time;
	}

	public long getSleepTime() {
		return sleep_time;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_SLEEP_NODE;
	}

}
