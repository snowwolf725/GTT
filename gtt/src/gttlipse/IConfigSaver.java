package gttlipse;

public interface IConfigSaver {
	public abstract void setDocument(org.w3c.dom.Document doc);
	
	public abstract void doSave();
	
	public abstract org.w3c.dom.Node getRoot();
}
