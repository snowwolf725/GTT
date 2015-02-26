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
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.runner.LaunchData;
import gttlipse.GTTlipseConfig;
import gttlipse.macro.dialog.EditDialogFactory;

public class LaunchNode extends AbstractMacroNode {

	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	LaunchData launchData;

	public String toString() {
		if (GTTlipseConfig.testingOnSwingPlatform())
			return launchData.getClassName();

		if (launchData.getArgument().equalsIgnoreCase("Loading URL"))
			return launchData.getArgument() + " " + launchData.getClasspath();
		if (launchData.getArgument().equalsIgnoreCase("Change Browser"))
			return launchData.getArgument() + " to "
					+ launchData.getClasspath();
		if (launchData.getArgument().equalsIgnoreCase("AJAX waiting time"))
			return launchData.getArgument() + ": " + launchData.getClasspath() + " secs";
		return launchData.getArgument();
	}

	public LaunchNode clone() {
		LaunchNode node = new LaunchNode(launchData);
		return node;
	}

	public LaunchNode() {
		launchData = new LaunchData();
	}

	public LaunchNode(LaunchData data) {
		super();
		launchData = new LaunchData();

		launchData.setClassPath(data.getClasspath());
		launchData.setClassName(data.getClassName());
		launchData.setArgument(data.getArgument());
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

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_LAUNCH_NODE;
	}

}
