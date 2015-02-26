package gttlipse.macro.view;

import gtt.macro.macroStructure.AbstractMacroNode;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

public class BadSmellItem {
	private String m_type;

	private String m_path;
	
	private int m_importance;
	
	private Vector<BadSmellItem> m_items = new Vector<BadSmellItem>();
	private Vector<SimpleEntry<AbstractMacroNode, String>> m_smellNodes = new Vector<SimpleEntry<AbstractMacroNode, String>>();
	
	BadSmellItem() {
		setType("InvisibleRoot");
	}
	
	public BadSmellItem(String type, String path, int importance) {
		m_type = type;
		m_path = path;
		m_importance = importance;
	}
	
	public void setImportance(int importance) {
		m_importance = importance;
	}
	
	public int getImportance() {
		return m_importance;
	}
	
	public String getType() {
		return m_type;
	}
	
	public String getPath() {
		return m_path;
	}
	
	public void setPath(String m_path) {
		this.m_path = m_path;
	}
	
	public void setType(String m_type) {
		this.m_type = m_type;
	}
	
	public void add(BadSmellItem item){
		m_items.add(item);
	}
	
	public void clear(){
		m_items.clear();
	}
	
	public void addNode(AbstractMacroNode node){
		AbstractMacroNode duplicateNode = node.clone();
		duplicateNode.setBadSmellData(node.getBadSmellData().clone());
		m_smellNodes.add(new SimpleEntry<AbstractMacroNode, String>(duplicateNode,node.getPath().toString()));
	}
	
	public Vector<SimpleEntry<AbstractMacroNode, String>> getSmells() {
		return m_smellNodes;
	}
	
	public String toString(){
		return getType();
	}
	
	public Object[] getChildren() {
		return m_items.toArray();
	}
	
	public boolean hasChildren() {
		return m_items.isEmpty()?false:true;
	}
	
}
