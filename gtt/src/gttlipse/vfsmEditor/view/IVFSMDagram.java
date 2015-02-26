package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;

public interface IVFSMDagram {

	// for Declaration
	public String addDeclarationChild(State state);

	public String addDeclarationChild(String name);

	public void removeDeclarationChild(State node);

	public AbstractSuperState getFSMRoot();

	public Diagram getFSMMain();

	public AbstractSuperState getFSMDeclaration();

	public void save(String filename);

	public void read(String filename);

	public Diagram createDiagram();

	public Diagram createDiagram(String name);

}