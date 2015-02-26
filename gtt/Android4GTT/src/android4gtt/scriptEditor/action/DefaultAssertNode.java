package android4gtt.scriptEditor.action;

import gtt.eventmodel.Assertion;
import gtt.testscript.NodeFactory;
import gtt.testscript.ViewAssertNode;
import gttlipse.scriptEditor.actions.node.IDefaultAssertNode;
import android4gtt.eventmodel.AndroidComponent;

public class DefaultAssertNode implements IDefaultAssertNode {

	@Override
	public ViewAssertNode getDefaultAssertNode() {
		NodeFactory factory = new NodeFactory();
		ViewAssertNode assertnode;
		AndroidComponent com = AndroidComponent.createDefault();
		com.setName("Assert Object");
		Assertion a = new Assertion();
		assertnode = factory.createViewAssertNode(com, a);
		return assertnode;
	}

	@Override
	public int getPlatformID() {
		return 3;
	}

}
