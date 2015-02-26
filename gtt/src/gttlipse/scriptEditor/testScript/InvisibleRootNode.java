/**
 * 
 */
package gttlipse.scriptEditor.testScript;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.TestScript
 * 
 */
public class InvisibleRootNode extends CompositeNode {

	public InvisibleRootNode() {
		super();
	}

	public void addChild(ProjectNode child) {
		if (_children.size() > 0)
			return; // ���঳��ӥH�W��project child

		_children.add(child);
		child.setParent(this);
	}
}
