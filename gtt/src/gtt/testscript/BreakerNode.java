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
package gtt.testscript;

import gtt.testscript.visitor.ITestScriptVisitor;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript
 */
public class BreakerNode extends AbstractNode {
	@Override
	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "Break";
	}

	@Override
	public String toDetailString() {
		return toString();
	}

	@Override
	public String toSimpleString() {
		return toString();
	}

	public AbstractNode clone() {
		return new BreakerNode();
	}

	public BreakerNode() {

	}

}
