package gtt.macro.macroStructure;

import gtt.macro.DefaultMacroFinder;
import gtt.macro.MacroFinder;

public class DynamicComponentReferenceImpl implements ComponentReference {

	private String _path = "";
	private DynamicComponentNode _component = null;
	
	public DynamicComponentReferenceImpl(String path) {
		_path = path;
		_component = null;
	}
	
	public DynamicComponentReferenceImpl(ComponentReference r) {
		_path = r.getReferencePath();
		_component = r.getDynamicComponent();
	}

	public DynamicComponentReferenceImpl() {
		_path = null;
		_component = null;
	}
	
	@Override
	public void setReferencepath(String path) {
		_path = path;
		_component = null;
	}
	
	@Override
	public String getReferencePath() {
		return _path;
	}

	@Override
	public DynamicComponentNode getDynamicComponent() {
		return _component;
	}
	
	@Override
	public ComponentNode getComponent() {
		return null;
	}
	
	@Override
	public boolean lookupComponent(AbstractMacroNode node) {
		if (hasObtainedComponent()) {
			return true;
		}
		
		if (isLegalToLookup(node) == false) {
			return false;
		}

		_component = lookup(node);
		return _component != null;
	}
	
	private boolean hasObtainedComponent() {
		return _component != null;
	}
	
	private DynamicComponentNode lookup(AbstractMacroNode node) {
		DynamicComponentNode c = lookupInLocal(node);
		
		if (c != null) {
			changeToLocalComponentFullPath(c);
			return c;
		}

		return lookupInGlobal(node);
	}
	
	private void changeToLocalComponentFullPath(DynamicComponentNode c) {
		_path = c.getPath().toString();
	}

	private DynamicComponentNode lookupInLocal(AbstractMacroNode node) {
		AbstractMacroNode local = DefaultMacroFinder.findLocalRoot(node);

		MacroFinder finder = new DefaultMacroFinder(local);
		return finder.findDynamicComponentNodeByName(getNameOfComponent());
	}
	
	private String getNameOfComponent() {
		String[] paths = _path.split(MacroPath.PATH_SEPARATOR);
		return paths[paths.length - 1];
	}

	private DynamicComponentNode lookupInGlobal(AbstractMacroNode node) {
		AbstractMacroNode root = DefaultMacroFinder.findRoot(node);

		MacroFinder finder = new DefaultMacroFinder(root);
		return finder.findDynamicComponentNodeByPath(_path);
	}
	
	private boolean isLegalToLookup(AbstractMacroNode node) {
		if (node == null) {
			return false;
		}
		
		if (_path == null) {
			return false;
		}

		if (DefaultMacroFinder.findLocalRoot(node) == null) {
			return false;
		}

		return true;
	}
}
