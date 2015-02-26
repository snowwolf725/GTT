package gttlipse.view;

import gtt.macro.macroStructure.AbstractMacroNode;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.TreeItem;

public class ChangeTreeItemBackground {
	public ChangeTreeItemBackground() {}
	
	public static void changeBackground(TreeItem item, Color color) {
		item.setBackground(color);
	}
	
	public static void clearBackground(TreeItem item) {
		Device device = item.getBackground().getDevice();
		AbstractMacroNode node = (AbstractMacroNode)item.getData();
		if(node != null && node.getBadSmellData() != null) {
			node.getBadSmellData().setBadSmellScore(0);
			node.getBadSmellData().setTotalBadSmellScore(0);
			node.getBadSmellData().setRGB(255, 255, 255);
		}
		for(TreeItem child:item.getItems()) {
			Color color = new Color(device, 255, 255, 255);
			child.setBackground(color);
			clearBackground(child);
		}
	}
	
	public static void changeBackground(List<TreeItem> items, Color color) {
		for(int i = 0; i < items.size(); i++) {
			items.get(i).setBackground(color);
		}
	}
	
	public static void changeBackground(List<TreeItem> items) {
		Device device = items.get(0).getBackground().getDevice();
		for(int i = 0; i < items.size(); i++) {
			AbstractMacroNode node = (AbstractMacroNode)items.get(i).getData();
			Color color = new Color(device, 
					node.getBadSmellData().getColorR(), 
					node.getBadSmellData().getColorG(), 
					node.getBadSmellData().getColorB());
			items.get(i).setExpanded(true);
			items.get(i).setBackground(color);
		}
	}
}
