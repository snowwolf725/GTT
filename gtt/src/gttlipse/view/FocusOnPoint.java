package gttlipse.view;

import gtt.macro.macroStructure.AbstractMacroNode;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

public class FocusOnPoint {
	public FocusOnPoint() {}
	
	public void focusOnPoint(TreeViewer viewer, AbstractMacroNode node) {
    	viewer.expandToLevel(node, 1);
    	TreeItem item = viewer.getTree().getItem(0);
    	item = searchTreeNodeByData(item, node);
    	if(item != null) {
    		viewer.getTree().deselectAll();
    		viewer.getTree().select(item);
    		viewer.getTree().setTopItem(item);
    	}
	}
	
	private TreeItem searchTreeNodeByData(TreeItem item, Object obj) {
		TreeItem result = null;
		
		if(obj.equals(item.getData()))
			return item;
		
		int size = item.getItems().length;
		
		for(int i =0; i < size; i++) {
			result = searchTreeNodeByData(item.getItem(i), obj);
			if(result != null) {
				return result;
			}
				
		}
		return result;
	}
}
