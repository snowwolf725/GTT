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

import gtt.runner.LaunchData;
import gtt.testscript.visitor.ITestScriptVisitor;

public class LaunchNode extends AbstractNode {

	LaunchData launchData;

	public LaunchNode() {
		launchData = new LaunchData();
	}

	public LaunchNode(LaunchData info) {
		super();
		launchData = new LaunchData();
		
		launchData.setClassPath(info.getClasspath());
		launchData.setClassName(info.getClassName());
		launchData.setArgument(info.getArgument());
	}

	@Override
	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return launchData.getClassName();
	}

	@Override
	public String toDetailString() {
		return toString();
	}

	@Override
	public String toSimpleString() {
		return toString();
	}

	public LaunchNode clone() {
		LaunchNode node = new LaunchNode(launchData);
		return node;
	}

	public LaunchData getLaunchData() {
		return launchData;
	}

	public void setClassPath(String clspath) {
		launchData.setClassPath(clspath);
	}

	public void setClassName(String clsname) {
		launchData.setClassName(clsname);
	}

	public void setArgument(String argument) {
		launchData.setArgument(argument);
	}

	public String getClassPath() {
		return launchData.getClasspath();
	}

	public String getClassName() {
		return launchData.getClassName();
	}

	public String getArgument() {
		return launchData.getArgument();
	}

}
