package gtt.macro.visitor;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.macro.view.BadSmellItem;
import gttlipse.macro.view.BadSmellListView;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

import java.util.Iterator;

public class FeatureEnvySmellVisitor extends MacroRefactorVisitor {
	private BadSmellListView m_view = GTTlipse.findBadSmellListView();
	private BadSmellItem m_item;
	private final int IMPORTANCE_RED = 3;
	private final int IMPORTANCE_YELLOW = 2;
	private final int IMPORTANCE_GREEN = 1;
	private final String BS_FEATURE_ENVY = "Feature Envy";
	
	public FeatureEnvySmellVisitor() {
		
	}
	
	private boolean analysisInSameScope(String path, String scopeRootPath) {
		
		/**
		 * Can use all component and Event under the root layer
		 * */
		if(path.length() < scopeRootPath.length()) {
			return false;
		}
		
		if(path.startsWith(scopeRootPath)) {
			return true;
		}
		
		return false;
	}

	@Override
	public void visit(ComponentEventNode node) {
		String path = node.getComponentPath();
		String scopeRootPath = node.getParent().getParent().getPath().toString();
		// if use outer component then set usage = 1
		if(analysisInSameScope(path, scopeRootPath) == true) {
			node.getBadSmellData().setBadSmellScore(0);
		}
		else {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
			m_item.addNode(node);
		}
	}

	@Override
	public void visit(MacroComponentNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		int usage = 0;
		int totalUsage = 0;
		
		// get child outer usage and total usage
		while (ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			child.accept(this);
			usage += child.getBadSmellData().getBadSmellScore();
			totalUsage += child.getBadSmellData().getTotalBadSmellScore();
		}
		
		node.getBadSmellData().setBadSmellScore(usage);
		node.getBadSmellData().setTotalBadSmellScore(totalUsage);
	}

	@Override
	public void visit(MacroEventNode node) {		
		Iterator<AbstractMacroNode> ite = node.iterator();
		int usage = 0;
		
		// get child outer usage
		while (ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			child.accept(this);
			usage += child.getBadSmellData().getBadSmellScore();
		}
		
		node.getBadSmellData().setBadSmellScore(usage);
		node.getBadSmellData().setTotalBadSmellScore(node.size());
		if(usage < node.size()/2) {
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_GREEN);
			m_item = new BadSmellItem(BS_FEATURE_ENVY, node.getPath().toString(), IMPORTANCE_GREEN);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		} else if(usage < node.size()) {
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
			m_item = new BadSmellItem(BS_FEATURE_ENVY, node.getPath().toString(), IMPORTANCE_YELLOW);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		} else if(usage == node.size()) {
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_RED);
			m_item = new BadSmellItem(BS_FEATURE_ENVY, node.getPath().toString(), IMPORTANCE_RED);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		}
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		String path = node.getReferencePath();
		String scopeRootPath = node.getParent().getParent().getPath().toString();
		// if use outer component then set usage = 1
		if(analysisInSameScope(path, scopeRootPath) == true) {
			node.getBadSmellData().setBadSmellScore(0);
		}
		else {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_GREEN);
			m_item.addNode(node);
		}
	}

	@Override
	public void visit(ViewAssertNode node) {
		String path = node.getComponentPath();
		String scopeRootPath = node.getParent().getParent().getPath().toString();
		// if use outer component then set usage = 1
		if(analysisInSameScope(path, scopeRootPath) == true) {
			node.getBadSmellData().setBadSmellScore(0);
		}
		else {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_GREEN);
			m_item.addNode(node);
		}
	}
}
