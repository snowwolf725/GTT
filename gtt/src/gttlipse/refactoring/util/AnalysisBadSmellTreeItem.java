package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.widgets.TreeItem;

public class AnalysisBadSmellTreeItem {
	private boolean _isAnalysis = true;
	private List<TreeItem> _items = null;

	public AnalysisBadSmellTreeItem() {
		_items = new Vector<TreeItem>();
	}

	public void setAnalysis(boolean analysis) {
		_isAnalysis = analysis;
	}

	public List<TreeItem> analysis(TreeItem root) {
		handleMacroComponent(root);
		return _items;
	}

	private void handleMacroComponent(TreeItem item) {
		analysisChangebackground(item);

		// close expand when rate = 0
		int rate = ((AbstractMacroNode) item.getData()).getBadSmellData().getBadSmellScore();
		if (_isAnalysis == true && rate == 0) {
			item.setExpanded(false);
		} else if(_isAnalysis == false){
			((AbstractMacroNode) item.getData()).getBadSmellData().setBadSmellScore(0);
			((AbstractMacroNode) item.getData()).getBadSmellData().setTotalBadSmellScore(0);
		}

		// dispatch node
		for (int i = 0; i < item.getItemCount(); i++) {
			if (item.getItem(i).getData() instanceof MacroComponentNode) {
				handleMacroComponent(item.getItem(i));
			}
			if (item.getItem(i).getData() instanceof MacroEventNode) {
				handleMacroEvent(item.getItem(i));
			}
		}
	}

	private void handleMacroEvent(TreeItem item) {
		analysisChangebackground(item);

		// close expand when rate = 0
		int rate = ((AbstractMacroNode) item.getData()).getBadSmellData().getBadSmellScore();
		if (_isAnalysis == true && rate == 0) {
			item.setExpanded(false);
		} else if(_isAnalysis == false){
			((AbstractMacroNode) item.getData()).getBadSmellData().setBadSmellScore(0);
			((AbstractMacroNode) item.getData()).getBadSmellData().setTotalBadSmellScore(0);
		}

		// dispatch node
		for (int i = 0; i < item.getItemCount(); i++) {
			if (item.getItem(i).getData() instanceof ComponentEventNode) {
				handleComponentEvent(item.getItem(i));
			}
			if (item.getItem(i).getData() instanceof ViewAssertNode) {
				handleViewAssert(item.getItem(i));
			}
			if (item.getItem(i).getData() instanceof MacroEventCallerNode) {
				handleEventCaller(item.getItem(i));
			}
		}
	}

	private void handleComponentEvent(TreeItem item) {
		analysisChangebackground(item);
	}

	private void handleViewAssert(TreeItem item) {
		analysisChangebackground(item);
	}

	private void handleEventCaller(TreeItem item) {
		analysisChangebackground(item);
	}

	private void analysisChangebackground(TreeItem item) {
		if (_isAnalysis == true) {
			int rate = ((AbstractMacroNode) item.getData()).getBadSmellData().getBadSmellScore();
			// if use outer component
			if (rate > 0) {
				// only use outer component need change to red
				_items.add(item);
			}
		} else {
			// all item change to white
			_items.add(item);
		}
	}
}
