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
 * Created on 2005/4/11
 */
package gtt.macro.macroStructure;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.MacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroPath;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class MacroEventCallerNode extends AbstractMacroNode implements
		IHaveArgument {

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public MacroEventCallerNode(String path) {
		setReferencePath(path);
	}

	public MacroEventCallerNode(MacroPath path) {
		setReferencePath(path.toString());
	}

	// copy construtor
	public MacroEventCallerNode(MacroEventCallerNode node) {
		setReferencePath(node.getReferencePath());
		setArguments(node.getArguments().clone());
	}

	// clone
	public MacroEventCallerNode clone() {
		return new MacroEventCallerNode(this);
	}

	public String getComponentName() {
		String[] paths = m_referencePath.split(MacroPath.PATH_SEPARATOR);
		if (paths.length < 2)
			return "";
		// 倒數第二個是 MacroComponentNode Name
		return paths[paths.length - 2];
	}

	public String getEventName() {
		String[] paths = m_referencePath.split(MacroPath.PATH_SEPARATOR);
		if (paths.length < 2)
			return "";
		// 最後一個是 MacroEventNode Name
		return paths[paths.length - 1];
	}

	public String toString() {
		return getComponentName() + "." + getEventName() + m_ArgList.toString();
	}

	private Arguments m_ArgList = new Arguments();

	@Override
	public Arguments getArguments() {
		return m_ArgList;
	}

	@Override
	public void setArguments(Arguments list) {
		m_ArgList = list.clone();
	}

	protected void update() {
		m_Name = getEventName();
	}

	protected Class<?> internalType() {
		// 半路上的節點的type - 預設 AbstractMacroNode
		return MacroComponentNode.class;
	}

	protected Class<?> leafType() {
		// 最後節點的type - 預設是 AbstractMacroNode
		return MacroEventNode.class;
	}

	protected String m_referencePath;

	public final String getReferencePath() {
		return m_referencePath;
	}

	public final void setReferencePath(String path) {
		m_referencePath = path;
		update();
	}

	public final AbstractMacroNode getReference() {
		if (m_referencePath == null)
			return null;
		if (m_referencePath == "")
			return null;

		// 找到 root，再委託至另一個function 去找出真正參考的節點
		return getReference(findRoot());
	}

	// 延著Macro Tree 往上找到root
	private AbstractMacroNode findRoot() {
		MacroComponentNode theRoot = DefaultMacroFinder.findRoot(this);

		if (theRoot != null)
			return theRoot; // ok, find it!!

		return null; // not found
	}

	// 從外界提供的 Root 尋找 reference path 所指的對象
	public final AbstractMacroNode getReference(AbstractMacroNode root) {
		if (root == null)
			return null;
		if (m_referencePath == null)
			return null;
		if (m_referencePath == "")
			return null;

		AbstractMacroNode ref = root;
		int startPath = 1;

		String[] paths = m_referencePath.split(MacroPath.PATH_SEPARATOR);

		// // root name 要一致
		// if (paths.length == 1 && paths[0].equals(ref.getName())) {
		// return ref; // 直接找到Root
		// }
		
//		//原本判斷是否是 System node
//		if (paths[0].equalsIgnoreCase("SYSTEM")) {
//			ref = ref.getParent();
//			startPath = 0;
//		}

		for (int i = startPath; i < paths.length; i++) {
			AbstractMacroNode child = null;

			MacroFinder finder = new DefaultMacroFinder(ref);
			if (i == paths.length - 1) {
				// 半路上 的節點
				child = finder.findByName(paths[i], leafType());
			} else {
				// 最後的節點
				child = finder.findByName(paths[i], internalType());
			}

			if (child == null)
				return null;
			ref = child;
		}
		return ref;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_MACRO_EVENT_CALLER_NODE;
	}

}
