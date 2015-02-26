package gttlipse.fit.node;

import gtt.testscript.AbstractNode;
import gtt.testscript.visitor.ITestScriptVisitor;

public class ReferenceFitNode extends AbstractNode {
	String m_path;

	public ReferenceFitNode(String path) {
		m_path = path;
	}

	public void setRefPath(String path) {
		m_path = path;
	}
	
	public String getReferencePath() {
		return m_path;
	}
	
	@Override
	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	@Override
	public ReferenceFitNode clone() {
		return new ReferenceFitNode(m_path);
	}

	@Override
	public String toDetailString() {
		return toString();
	}

	@Override
	public String toSimpleString() {
		return toString();
	}
	
	public String toString() {
		return m_path;// .replaceAll(".*::", "");
	}

}
