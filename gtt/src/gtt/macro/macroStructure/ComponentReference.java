package gtt.macro.macroStructure;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;


public interface ComponentReference {

	public ComponentNode getComponent();

	public DynamicComponentNode getDynamicComponent();
	
	public String getReferencePath();

	public void setReferencepath(String path);

	public boolean lookupComponent(AbstractMacroNode node);

}