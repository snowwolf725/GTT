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
package gtt.tester.swing;

import gtt.eventmodel.IEvent;

import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.netbeans.jemmy.operators.JTreeOperator;
import org.netbeans.jemmy.operators.Operator.DefaultStringComparator;

public class JTreeTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JTree))
			return false;

		JTreeOperator treeOperator = new JTreeOperator((JTree) comp);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.CALL_POPUP_ON_PATH) {
			TreePath treePath = findTreePath(treeOperator, info);
			treeOperator.callPopupOnPath(treePath);
			return true;
		}
		if (eid == SwingTesterTag.CLICK_FOR_EDIT) {
			TreePath treePath = findTreePath(treeOperator, info);
			treeOperator.clickForEdit(treePath);
			return true;
		}
		if (eid == SwingTesterTag.CLICK_ON_PATH) {
			TreePath treePath = findTreePath(treeOperator, info);
			int ck = Integer.valueOf(info.getArguments().getValue(
					"ClickCount"));
			treeOperator.clickOnPath(treePath, ck);
			return true;
		}
		if (eid == SwingTesterTag.COLLAPSE_PATH) {
			TreePath treePath = findTreePath(treeOperator, info);
			treeOperator.collapsePath(treePath);
			return true;
		}
		if (eid == SwingTesterTag.COLLAPSE_ROW) {
			int row = findTreeRow(info);
			treeOperator.doCollapseRow(row);
			return true;
		}
		if (eid == SwingTesterTag.EXPAND_PATH) {
			TreePath treePath = findTreePath(treeOperator, info);
			treeOperator.doExpandPath(treePath);
			return true;
		}
		if (eid == SwingTesterTag.EXPAND_ROW) {
			int row = findTreeRow(info);
			treeOperator.doExpandRow(row);
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_PATH) {
			int row = findTreeRow(info);
			treeOperator.scrollToRow(row);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_PATH) {
			TreePath treePath = findTreePath(treeOperator, info);
			treeOperator.selectPath(treePath);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_ROW) {
			int row = findTreeRow(info);
			treeOperator.selectRow(row);
			return true;
		}

		return false;
	}

	private TreePath findTreePath(JTreeOperator treeOperator, IEvent info) {
		LinkedList<String> paths = toTreePathList(info.getArguments()
				.getValue("TreePath"));
		Boolean isCompareExactly = Boolean.valueOf(info.getArguments()
				.getValue("IsCompareExactly"));
		return findTreePath(treeOperator, paths, isCompareExactly);
	}

	private TreePath findTreePath(JTreeOperator treeOperator,
			LinkedList<String> paths, boolean isCompareExactly) {

		try {
			return treeOperator.findPath((String[]) paths.toArray(),
					new DefaultStringComparator(isCompareExactly, false));
		} catch (org.netbeans.jemmy.TimeoutExpiredException te) {
			System.err.println("[[ERROR! JTreeComponent findTreePath fail.");
		}
		return null;
	}

	private LinkedList<String> toTreePathList(String treepath) {
		LinkedList<String> paths = new LinkedList<String>();
		// tree path 可以使用 ' ' '.' '\t' ':' '\n' 做分隔
		StringTokenizer st = new StringTokenizer(treepath, " .\t:\n");
		while (st.hasMoreTokens()) {
			paths.add((String) st.nextToken());
		}
		return paths;
	}

	private int findTreeRow(IEvent info) {
		LinkedList<String> paths = toTreePathList(info.getArguments()
				.getValue("TreePath"));
		return Integer.parseInt(paths.get(0));
	}

}
