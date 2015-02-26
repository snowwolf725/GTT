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
			return; // 不能有兩個以上的project child

		_children.add(child);
		child.setParent(this);
	}
}
