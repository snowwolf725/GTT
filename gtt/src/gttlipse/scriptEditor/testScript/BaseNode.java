/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

import java.util.ArrayList;
import java.util.List;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript
 * 
 */
public abstract class BaseNode implements Cloneable {

	protected String m_nodeName;

	protected CompositeNode m_parent;

	protected boolean m_isVisiable;

	public BaseNode() {
		
	}

	public BaseNode(String name) {
		m_nodeName = name;
	}

	final public void setName(String newName) {
		m_nodeName = newName;
	}

	final public String getName() {
		return m_nodeName;
	}

	final public String toString() {
		return getName();
	}

	final public void setParent(CompositeNode parent) {
		m_parent = parent;
	}

	final public CompositeNode getParent() {
		return m_parent;
	}

	final public List<String> getFullPath() {
		BaseNode anode = this;
		List<String> fullpath = new ArrayList<String>();
		if (anode instanceof ProjectNode) {
			fullpath.add(anode.toString());
			return fullpath;
		}

		do {
			fullpath.add(anode.toString());
			anode = anode.getParent();
		} while (anode != null && !(anode instanceof ProjectNode));

		return fullpath;
	}

	final public String getPath() {
		BaseNode anode = this;
		String fullpath = "";
		do {
			if (anode instanceof ProjectNode) {
				fullpath = "/" + anode.getName() + fullpath;
			} else if (anode instanceof TestCaseNode) {
				fullpath = "/" + anode.getName() + ".java" + fullpath;
			} else if (anode instanceof BaseNode) {
				fullpath = "/" + anode.getName() + fullpath;
			}
			anode = anode.getParent();
		} while (anode != null && !(anode instanceof InvisibleRootNode));

		return fullpath;
	}

	public int size() {
		return 0;
	}

	final public void setVisiable(boolean isvisiable) {
		this.m_isVisiable = isvisiable;
	}

	final public boolean getVisiable() {
		return m_isVisiable;
	}

	public abstract BaseNode clone();

	public abstract void accept(IGTTlipseScriptVisitor v);
}