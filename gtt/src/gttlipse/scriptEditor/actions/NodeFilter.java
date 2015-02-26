/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.TestProject;
import gttlipse.scriptEditor.def.FilterType;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class NodeFilter {

	public void setDisplayNode(int type) {
		clearFilter(TestProject.getProject());
		if (type == FilterType.DISPLAYTESTSCRIPT) {
			// 只顯示有含 TestScript 的 Node
			normalize(TestProject.getProject());
		} else if (type == FilterType.DISPLAYNONETESTSCRIPT) {
			/* 只顯示不含 TestScript 的 Node */
			// createFilter(project,false);
		}

	}

	public boolean normalize(BaseNode node) {
		if (node == null)
			return false;
		if (node instanceof ProjectNode) {
			CompositeNode cnode = (CompositeNode) node;
			for (int i = 0; i < cnode.size(); i++) {
				boolean tmp = normalize(cnode.getChildrenAt(i));
				if (tmp == false) {
					cnode.removeChild(i);
					i = -1;
				}
			}
			return true;
		}
		if (node instanceof TestCaseNode || node instanceof PackageNode
				|| node instanceof SourceFolderNode) {
			/*
			 * 隱藏沒有 TestMethodNode 的 TestCaseNode , TestPackageNode 及
			 * SourceFolderNode
			 */
			CompositeNode cnode = (CompositeNode) node;
			boolean result = false;
			for (int i = 0; i < cnode.size(); i++) {
				boolean tmp = normalize(cnode.getChildrenAt(i));
				if (tmp == true)
					result = true;
				else if (tmp == false) {
					cnode.removeChild(i);
					i = -1;
				}
			}
			cnode.setVisiable(result);
			return result;
		}
		if (node instanceof TestMethodNode) {
			TestMethodNode method = (TestMethodNode) node;
			if (method.size() != 0) {
				method.setVisiable(true);
				return true;
			} else {
				method.setVisiable(false);
				return false;
			}
		}
		return false;
	}

	public void createFilter(BaseNode node, boolean displayTestScript) {
		if (node == null)
			return;
		if (node instanceof TestMethodNode) {
			TestMethodNode method = (TestMethodNode) node;
			if (method.size() != 0) {
				method.setVisiable(displayTestScript);
			} else {
				method.setVisiable(!displayTestScript);
			}
		}
		if (node instanceof CompositeNode) {
			CompositeNode cnode = (CompositeNode) node;
			cnode.setVisiable(true);
			for (int i = 0; i < cnode.size(); i++) {
				createFilter(cnode.getChildrenAt(i), displayTestScript);
			}
		}
	}

	public void clearFilter(BaseNode node) {
		if (node == null)
			return;
		if (node.size() == 0 || node instanceof TestMethodNode) {
			node.setVisiable(true);
			return;
		}
		if (node instanceof CompositeNode) {
			CompositeNode cnode = (CompositeNode) node;
			cnode.setVisiable(true);
			for (int i = 0; i < cnode.size(); i++) {
				clearFilter(cnode.getChildrenAt(i));
			}
		}
	}
}
