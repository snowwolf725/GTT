package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Element;

import org.eclipse.swt.widgets.Shell;


public interface IVFSMPresenter {
	
//	public void automaticLayout();
	
	public Shell getShell();

	public Element getSelectionElement();

	public void refreshTreeView();

	public IVFSMDagram getDiagram();
	
	public void addDeclarationChild(String name);
	
	public void inheritanceFrom(AbstractSuperState ss);
	
	public void removeDeclarationChild(AbstractSuperState ss) ;
	
	public void diplayDiagram(String name);
	
	public void diplayMainDiagram();
}
