package gttlipse.vfsmEditor.model;

public interface IVFSMVisitor {
	public void visit(Initial node);

	public void visit(Final node);

	public void visit(State node);

	public void visit(SuperState node);

	public void visit(AndSuperState node);

	public void visit(ProxySuperState node);
}
