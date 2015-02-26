package android4gtt.scriptEditor.action;

import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gttlipse.scriptEditor.actions.node.IDefaultEventNode;
import android4gtt.eventmodel.AndroidComponent;
import android4gtt.eventmodel.AndroidEvent;

public class DefaultEventNode implements IDefaultEventNode {

	@Override
	public EventNode getDefaultEventNode() {
		NodeFactory factory = new NodeFactory();
		EventNode eventnode;
		AndroidComponent com = AndroidComponent.createDefault();
		com.setType("android.widget.Button");
		AndroidEvent event = AndroidEvent.create(501, "CLICK");
		eventnode = factory.createEventNode(com, event);
		return eventnode;
	}

	@Override
	public int getPlatformID() {
		return 3;
	}

}
