package testing.editor;

import gtt.testscript.AbstractNode;
import gtt.testscript.visitor.ITestScriptVisitor;


class MockNode extends AbstractNode {

	@Override
	public void accept(ITestScriptVisitor v) {
		// TODO Auto-generated method stub
	}

	@Override
	public String toDetailString() {
		return "toDetailString";
	}

	@Override
	public String toSimpleString() {
		return "toSimpleString";
	}

	public String toString() {
		return "toString";
	}

	@Override
	public AbstractNode clone() {
		return this;
	}

}