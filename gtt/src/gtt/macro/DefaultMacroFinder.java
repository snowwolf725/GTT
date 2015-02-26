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
package gtt.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroPath;

import java.util.Iterator;

public class DefaultMacroFinder implements MacroFinder {

	private static final Class<AbstractMacroNode> CANDIDATE_TYPE = AbstractMacroNode.class;

	private AbstractMacroNode m_MacroRoot;

	public DefaultMacroFinder(AbstractMacroNode macro) {
		m_MacroRoot = macro;
	}

	@Override
	public ComponentNode findComponentNodeByName(String name) {
		AbstractMacroNode foundNode = findByName(name, ComponentNode.class);
		return (ComponentNode)checkFoundNode(foundNode);
	}

	@Override
	public ComponentNode findComponentNodeByPath(String refpath) {
		AbstractMacroNode foundNode = findByPath(refpath, ComponentNode.class);
		return (ComponentNode)checkFoundNode(foundNode);
	}

	@Override
	public DynamicComponentNode findDynamicComponentNodeByName(String name) {
		AbstractMacroNode foundNode = findByName(name, DynamicComponentNode.class);
		return (DynamicComponentNode)checkFoundNode(foundNode);
	}
	
	@Override
	public DynamicComponentNode findDynamicComponentNodeByPath(String refpath) {
		AbstractMacroNode foundNode = findByPath(refpath, DynamicComponentNode.class);
		return (DynamicComponentNode)checkFoundNode(foundNode);
	}
	
	private AbstractMacroNode checkFoundNode(AbstractMacroNode foundNode) {
		if (foundNode == null)
			return null;
		if (!(foundNode instanceof ComponentNode)
				&& !(foundNode instanceof DynamicComponentNode))
			return null;
		return foundNode;
	}

	@Override
	public AbstractMacroNode findByName(String name) {
		return findByName(name, CANDIDATE_TYPE);
	}

	@Override
	public AbstractMacroNode findByName(String name, Class<?> expectedClassType) {
		if (isLegalName(name) == false)
			return null;
		if (isLegalNodeTypeAndHasRoot(expectedClassType) == false)
			return null;

		return findChild(m_MacroRoot, name, expectedClassType);
	}
	
	private boolean isLegalName(String name) {
		if (name == null || name == "")
			return false;

		return true;
	}
	@Override
	public AbstractMacroNode findByPath(String refpath) {
		return findByPath(refpath, CANDIDATE_TYPE);
	}

	@Override
	public AbstractMacroNode findByPath(String path, Class<?> expectedNodeType) {
		if (isLegalPath(path) == false)
			return null;
		if (isLegalNodeTypeAndHasRoot(expectedNodeType) == false)
			return null;

		return searchThroughPath(path, expectedNodeType);
	}

	private boolean isLegalNodeTypeAndHasRoot(Class<?> expectedClassType) {
		if (m_MacroRoot == null)
			return false;
		if (expectedClassType == null)
			return false;
		if (!CANDIDATE_TYPE.isAssignableFrom(expectedClassType))
			return false;

		return true;
	}


	private AbstractMacroNode findChild(AbstractMacroNode root, String name,
			Class<?> expectedNodeType) {
		if (root == null)
			return null;

		Iterator<AbstractMacroNode> ite = root.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode n = ite.next();
			if (!name.equals(n.getName()))
				continue;
			if (expectedNodeType.isAssignableFrom(n.getClass()))
				return n;
		}
		return null;
	}

	private boolean isLegalPath(String refpath) {
		if (refpath == null || refpath == "")
			return false;
		
		return true;
	}

	private AbstractMacroNode searchThroughPath(String path,
			Class<?> expectedNodeType) {
		String paths[] = path.split(MacroPath.PATH_SEPARATOR);

		if (!paths[0].equals(m_MacroRoot.getName()))
			return null;

		AbstractMacroNode nextRoot = m_MacroRoot;
		for (int i = 1; i < paths.length; i++) {
			if(i<paths.length-1)
				nextRoot = findChild(nextRoot, paths[i], CANDIDATE_TYPE);
			else
				nextRoot = findChild(nextRoot, paths[i], expectedNodeType);
				
			if (nextRoot == null)
				return null;
		}
		return nextRoot;
	}

	public static MacroComponentNode findRoot(AbstractMacroNode node) {
		AbstractMacroNode theRoot = node;
		while (theRoot.getParent() != null
				&& !(theRoot.getParent() instanceof InvisibleRootNode)) {
			theRoot = theRoot.getParent();
		}
		return (MacroComponentNode) theRoot;
	}

	public static MacroComponentNode findLocalRoot(AbstractMacroNode node) {
		try {
			if ((node.getParent() instanceof MacroComponentNode))
				return (MacroComponentNode) node.getParent();
			if ((node.getParent().getParent() instanceof MacroComponentNode))
				return (MacroComponentNode) node.getParent().getParent();
			return null;
		} catch (Exception ep) {
			return null;
		}
	}
}
