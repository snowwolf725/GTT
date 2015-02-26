/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

import java.util.ArrayList;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.TestScript
 * 
 */
public class CompositeNode extends BaseNode {
	protected ArrayList<BaseNode> _children;

	public CompositeNode() {
		_children = new ArrayList<BaseNode>();
		m_parent = null;
		m_nodeName = "Node";
	}

	public CompositeNode(String name) {
		_children = new ArrayList<BaseNode>();
		m_parent = null;
		m_nodeName = name;
	}

	final public boolean checkName(String name) {
		BaseNode[] children = getChildren();
		for (int i = 0; i < children.length; i++)
			if (children[i].getName().matches(name))
				return false;

		return true;
	}

	final public void removeChild(BaseNode child) {
		_children.remove(child);
		child.setParent(null);
	}

	// add by David Wu 2007/02/01
	final public void removeChild(int index) {
		BaseNode child = (BaseNode) _children.get(index);
		_children.remove(index);
		child.setParent(null);
	}

	public int size() {
		return _children.size();
	}

	final public BaseNode[] getChildren() {
		return (BaseNode[]) _children.toArray(new BaseNode[_children.size()]);
	}

	final public BaseNode getChildrenAt(int i) {
		if (!hasChildren())
			return null;

		return getChildren()[i];
	}

	// add by David Wu 2007/02/01
	final public int indexOfChild(BaseNode node) {
		return _children.indexOf(node);
	}

	// change String.match() to String.equals by David Wu 2007/02/10
	final public BaseNode getChildrenByName(String name) {
		if (!hasChildren())
			return null;

		BaseNode[] nodes = getChildren();
		for (int i = 0; i < nodes.length; i++)
			if (nodes[i].getName().equals(name))
				return nodes[i];

		return null;
	}

	public void addChild(BaseNode child) {
		_children.add(child);
		child.setParent(this);
	}

	// add by David Wu 2007/02/01
	final public void setChild(int index, BaseNode child) {
		if (index < size()) {
			_children.set(index, child);
			child.setParent(this);
		}
	}

	final public boolean hasChildren() {
		return size() > 0;
	}

	final public boolean moveChildFront(BaseNode child) {
		BaseNode[] children = getChildren();

		if (children[0].equals(child))
			return false;

		for (int i = 1; i < children.length; i++) {
			if (children[i].equals(child)) {
				BaseNode tmp = children[i - 1];
				children[i - 1] = children[i];
				children[i] = tmp;
				break;
			}
		}

		setChildren(children);
		return true;
	}

	final public boolean moveChildRear(BaseNode child) {
		BaseNode[] children = getChildren();

		if (children[children.length - 1].equals(child))
			return false;

		for (int i = children.length - 1; i >= 0; i--) {
			if (children[i].equals(child)) {
				BaseNode tmp = children[i + 1];
				children[i + 1] = children[i];
				children[i] = tmp;
				break;
			}
		}

		setChildren(children);
		return true;
	}

	final public void setChildren(BaseNode[] children) {
		clearChildren();
		for (int i = 0; i < children.length; i++)
			_children.add(children[i]);
	}

	final public void clearChildren() {
		if (hasChildren())
			_children.clear();
	}

	final protected String genSerialName(String prefix) {
		String newname = "";
		BaseNode[] children = getChildren();
		boolean legal = false;
		int count = 1;
		while (legal == false) {
			legal = true;
			newname = prefix + count;
			for (int i = 0; i < children.length; i++)
				if (children[i].getName().matches(newname))
					legal = false;
			count++;
		}
		return newname;
	}

	@Override
	public void accept(IGTTlipseScriptVisitor v) {
	}

	@Override
	public BaseNode clone() {
		return null;
	}

}
