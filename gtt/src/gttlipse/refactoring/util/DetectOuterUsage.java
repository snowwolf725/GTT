package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

import java.util.Iterator;


public class DetectOuterUsage extends MacroRefactorVisitor {
	private AbstractMacroNode _root = null;
	
//	public DetectOuterUsage() {}
	
	public DetectOuterUsage(AbstractMacroNode root) {
		_root = root;
	}
	
	public void detect() {
		_root.accept(this);
	}
	
	private boolean analysisInSameScope(String path, String scopeRootPath) {
//		/**
//		 * Can use component and Event in this layer and 1 layer under this layer
//		 * */
//		String paths[] = path.split(MacroPath.PATH_SEPARATOR);
//		String scopeRootPaths[] = scopeRootPath.split(MacroPath.PATH_SEPARATOR);
//		
//		// scope is this layer and 1 layer under this layer
//		if(paths.length > scopeRootPaths.length) {
//			if((paths.length - scopeRootPaths.length) <= 2) {
//				for(int i = 0; i < scopeRootPaths.length; i++) {
//					if(paths[i].equals(scopeRootPaths[i]) == false) {
//						return false;
//					}
//				}
//				return true;
//			}
//		}
		
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
		}
	}
}
