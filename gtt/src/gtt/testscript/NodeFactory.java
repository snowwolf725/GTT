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
package gtt.testscript;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gttlipse.fit.node.ReferenceFitNode;

/**
 * Object Factory
 *
 * @author zwshen
 *
 */
public class NodeFactory {

	public FolderNode createFolderNode(String name) {
		return new FolderNode(name);
	}

	public EventNode createEventNode(IComponent comp, IEvent event) {
		return new EventNode(comp, event);
	}

	public ViewAssertNode createViewAssertNode(IComponent comp, Assertion a) {
		return new ViewAssertNode(comp, a);
	}

	public ModelAssertNode createModelAssertNode() {
		return new ModelAssertNode();
	}

	public LaunchNode createLaunchNode(String classname, String classpath) {
		LaunchNode node = new LaunchNode();
		node.getLaunchData().setClassPath(classpath);
		node.getLaunchData().setClassName(classname);
		return node;
	}

	public ReferenceMacroEventNode createReferenceMacroEventNode(String path) {
		return new ReferenceMacroEventNode(path);
	}

	public OracleNode createOracleNode() {
		return new OracleNode();
	}

	public SleeperNode createSleeperNode(long time) {
		return new SleeperNode(time);
	}

	public CommentNode createCommentNode(String comment) {
		return new CommentNode(comment);
	}

	public BreakerNode createBreakerNode() {
		return new BreakerNode();
	}
	
	public ReferenceFitNode createReferenceFitNode(String path) {
		return new ReferenceFitNode(path);
	}
}
