package gtt.macro.macroStructure;

import gtt.macro.DefaultMacroFinder;
import gtt.macro.MacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ComponentReference;
import gtt.macro.macroStructure.MacroPath;

public class ComponentReferenceImpl implements ComponentReference {
	private String path = null;
	private ComponentNode component = null;

	public ComponentReferenceImpl(String path) {
		this.path = path;
		// null, because it should be looked again
		component = null;
	}

//	public ComponentReferenceImpl(ComponentNode c) {
//		path = c.getPath().toString();
//		component = c;
//	}

	public ComponentReferenceImpl(ComponentReference r) {
		path = r.getReferencePath();
		component = r.getComponent();
	}

	public ComponentReferenceImpl() {
		path = null;
		component = null;
	}

	@Override
	public void setReferencepath(String path) {
		this.path = path;
		// null, because it should be looked again
		component = null;
	}

	@Override
	public ComponentNode getComponent() {
		return component;
	}

	@Override
	public String getReferencePath() {
		return path;
	}

	@Override
	public boolean lookupComponent(AbstractMacroNode node) {
		if (hasObtainedComponent())
			return true;
		if (isLegalToLookup(node) == false)
			return false;

		component = lookup(node);
		return component != null;
	}

	private boolean hasObtainedComponent() {
		return component != null;
	}

	private ComponentNode lookup(AbstractMacroNode node) {
		ComponentNode c = lookupInLocal(node);
		if (c != null) {
			changeToLocalComponentFullPath(c);
			return c;
		}

		return lookupInGlobal(node);
	}

	private void changeToLocalComponentFullPath(ComponentNode c) {
		path = c.getPath().toString();
	}

	private ComponentNode lookupInLocal(AbstractMacroNode node) {
		AbstractMacroNode local = DefaultMacroFinder.findLocalRoot(node);

		MacroFinder finder = new DefaultMacroFinder(local);
		return finder.findComponentNodeByName(getNameOfComponent());
	}

	private String getNameOfComponent() {
		String[] paths = path.split(MacroPath.PATH_SEPARATOR);
		return paths[paths.length - 1];
	}

	private ComponentNode lookupInGlobal(AbstractMacroNode node) {
		AbstractMacroNode root = DefaultMacroFinder.findRoot(node);

		MacroFinder finder = new DefaultMacroFinder(root);
		return finder.findComponentNodeByPath(path);
	}

	private boolean isLegalToLookup(AbstractMacroNode node) {
		if (node == null)
			return false;
		if (path == null)
			return false;

		if (DefaultMacroFinder.findLocalRoot(node) == null)
			return false;

		return true;
	}

	@Override
	public DynamicComponentNode getDynamicComponent() {
		return null;
	}
}
