/**
 * 
 */
package gttlipse.scriptEditor.util;

import gtt.eventmodel.IComponent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.util
 * 
 */
public class ComponentStorage {
	private Map<String,IComponent> m_nameToComp = new HashMap<String,IComponent>();
	private Map<String,String> m_CompnameToRealname = new HashMap<String,String>();
	private Map<String,String> m_CompToCompparent = new HashMap<String,String>();
	private Map<String,String> m_CompToComptext = new HashMap<String,String>();
	private Map<String,String> m_importTypeInfo = new HashMap<String,String>();
	private boolean iswindowclass = false;	// 該 Class 是繼承 JFram 或 JDialog
	private String classname = "";
	
	public ComponentStorage() {
		
	}
	
	public void addComponent(String compname,IComponent comp) {
		if(comp.getType().startsWith("javax.swing")){
			IComponent newcomp=m_nameToComp.get(compname);
			if(newcomp == null) {
				m_nameToComp.put(compname, comp);
			}
		}else if(m_importTypeInfo.containsKey(comp.getType())){
			comp.setType(m_importTypeInfo.get(comp.getType()));
			IComponent newcomp=m_nameToComp.get(compname);
			if(newcomp == null) {
				m_nameToComp.put(compname, comp);
			}
		}
	}
	
	public void addImportInfo(String type){
		if(type.startsWith("javax.swing.")){
			String simplename = type.replace("javax.swing.", "");
			m_importTypeInfo.put(simplename, type);
		}else if(type.startsWith("java.awt.")){
			String simplename = type.replace("java.awt.", "");
			m_importTypeInfo.put(simplename, type);
		}
	}
	
	public void addCompNameInfo(String compname,String realname){
		m_CompnameToRealname.put(compname, realname);
	}
	
	public void addCompParentInfo(String compname){
		addCompParentInfo(getClassname(),compname);
	}
	
	public void addCompParentInfo(String parentcompname,String compname){
		m_CompToCompparent.put(compname, parentcompname);
	}
	
	public void addCompTextInfo(String comp,String comptext){
		m_CompToComptext.put(comp, comptext);
	}
	
	public void setParentComponent(String parentcompname,String compname){
		IComponent parentcomp=m_nameToComp.get(parentcompname);
		IComponent comp=m_nameToComp.get(compname);
		if(parentcomp == null||comp == null)return;
		if(!parentcomp.getType().equals("javax.swing.JFrame")&&
		   !parentcomp.getType().equals("javax.swing.JDialog"))return;
		comp.setWinType(parentcomp.getType());
		comp.setTitle(parentcomp.getTitle());
	}
	
	public void setRealName(String compname,String realname){
		IComponent comp=m_nameToComp.get(compname);
		if(comp != null){
			comp.setName(realname);
			IComponent test = m_nameToComp.get(compname);
			test.getIndex();
		}
	}
	
	public void setText(String compname,String text){
		IComponent comp=m_nameToComp.get(compname);
		if(comp != null)
			comp.setText(text);
	}
	
	public void setWinTitle(String compname,String title){
		IComponent comp=m_nameToComp.get(compname);
		if(comp != null)
			comp.setTitle(title);
	}
	
	public void setWinTitle(String title){
		IComponent comp = m_nameToComp.get(getClassname());
		if(comp != null)
			comp.setTitle(title);
	}
	
	public void setWinType(String compname,String type){
		IComponent comp=m_nameToComp.get(compname);
		if(comp != null)
			comp.setWinType(type);
	}
	
	public void clearComponent() {
		m_nameToComp.clear();
	}
	
	public Vector<IComponent> getComponet(){
		Set<String> keys = m_nameToComp.keySet();
		Vector<IComponent> result=new Vector<IComponent>();
		Iterator<String> ite = keys.iterator();
		while(ite.hasNext()){
			String compname = (String)ite.next();
			IComponent comp=m_nameToComp.get(compname);
			if(comp != null)
				result.add(comp);
		}
		return result;
	}
	
	public void dump(){
		Vector<IComponent> comps = getComponet();
		Iterator<IComponent> ite = comps.iterator();
		while(ite.hasNext()){
			IComponent comp= ((IComponent)ite.next());
			if(comp != null)
				System.out.println("name:"+comp.getName()+"\t,type:"+comp.getType()+"\t,text:"+comp.getText()+
						"\t,title"+comp.getTitle()+"\t,wintype:"+comp.getWinType());
		}
	}
	
	public void analysis(){
		Set<String> compToRealnameKeys = m_CompnameToRealname.keySet();
		Set<String> compToCompparentKeys = m_CompToCompparent.keySet();
		Set<String> compToCompTextKeys = m_CompToComptext.keySet();
		// Setup Component name
		Iterator<String> ite = compToRealnameKeys.iterator();
		while(ite.hasNext()){
			String compname = (String)ite.next();
			String realname = m_CompnameToRealname.get(compname);
			if(realname != null)
				setRealName(compname,realname);
		}
		// 推論出每個 Component 是包含在哪個 Window/Dialog 內
		boolean isFin;
		do{
			isFin=true;
			ite = compToCompparentKeys.iterator();
			while(ite.hasNext()){
				String comp = (String)ite.next();
				String parentcomp = m_CompToCompparent.get(comp);
				if(parentcomp != null){
					Iterator<String> ite2 = compToCompparentKeys.iterator();
					while(ite2.hasNext()){
						String comp2 = (String)ite2.next();
						String parentcomp2 = m_CompToCompparent.get(comp2);
						if(parentcomp2 != null&&parentcomp2.equals(comp)){
							// a 在  b 內,b 在 c 內,則 a 在 c 中
							m_CompToCompparent.put(comp2, parentcomp);
							isFin = false;
						}
					}
				}
			}
		}while(isFin==false);
		// setup component parent
		ite = compToCompparentKeys.iterator();
		while(ite.hasNext()){
			String comp = (String)ite.next();
			String parentcomp = m_CompToCompparent.get(comp);
			if(parentcomp != null)
				setParentComponent(parentcomp,comp);
		}
		// setup component text
		ite = compToCompTextKeys.iterator();
		while(ite.hasNext()){
			String comp = (String)ite.next();
			String text = m_CompToComptext.get(comp);
			if(text != null)
				setText(comp,text);
		}
	}

	/**
	 * @param iswindowclass the iswindowclass to set
	 */
	public void setIswindowclass(boolean iswindowclass) {
		this.iswindowclass = iswindowclass;
	}

	/**
	 * @return the iswindowclass
	 */
	public boolean getIswindowclass() {
		return iswindowclass;
	}

	/**
	 * @param classname the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}
}
