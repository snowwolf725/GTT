package gttlipse.web.htmlPaser;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConvertPageToComponent {
	DOMParser parser = null;
	private boolean isAlreadyExist = false;
	private boolean compareMode = false;
	private boolean isGroup = false;
	private Vector<ComponentNode> vExistNodes = new Vector<ComponentNode>();
	private Vector<AbstractMacroNode> vNewNodes = new Vector<AbstractMacroNode>();	
	private AbstractMacroNode findNode = null;
	private AbstractMacroNode startMacroNode = null;
	private AbstractMacroNode checkNode = null;//暫存比對紀錄節點
	private String checkParentNodePath = "";//暫存比對紀錄節點xpath
	private String checkNodeName = "";//暫存比對紀錄節點的node name
	private boolean checkExist = false;//暫存比對紀錄節點的存在狀態
	private AbstractMacroNode newMacroNode = null;
	private AbstractMacroNode removedMacroNode = null; 
	private String newNodePreName = "NEW_COM: ";
	
	public ConvertPageToComponent(String SourcePage) {
		 InputSource s = new InputSource(new StringReader(SourcePage)); 
		 parser = new DOMParser();
		 try {
			parser.parse(s);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Convert(AbstractMacroNode startNode) {
		try{	
			startMacroNode = startNode;
			if(startNode.getChildren().length > 0){
				vExistNodes.clear();
				compareMode = true;
			}
			
			if(compareMode) {
				initCompareSetting();
			}
			
			parse(parser.getDocument().getFirstChild(), startNode);
			if(compareMode) {			
				moveAndClearComponents(startNode);
				updateNewCompoenents();				
				startMacroNode.add(removedMacroNode);
				startMacroNode.add(newMacroNode);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initCompareSetting() {
		String removedMacroCompName = "REMOVED Compoenents";
		String newMacroCompName = "NEW Compoenents";
		for(int i = 0; i < startMacroNode.getChildren().length; i++) {
			String tempName = startMacroNode.getChildren()[i].getName();
			if(tempName.contains(removedMacroCompName) || tempName.contains(newMacroCompName)) {
				startMacroNode.remove(startMacroNode.getChildren()[i]);
				i--;
			}
		}
		removedMacroNode = MacroComponentNode.create(removedMacroCompName);
		newMacroNode = MacroComponentNode.create(newMacroCompName);

	}
	
	private void parse(Node node, AbstractMacroNode parentNode) {
		if(isElementNode(node)) {
			AbstractMacroNode tempNode = dispatch((org.w3c.dom.Element) node, parentNode);
			for(int i = 0; i < node.getChildNodes().getLength(); i++) {
				parse(node.getChildNodes().item(i), tempNode);
			}
		}
	}
	
	
	private AbstractMacroNode dispatch(Element e, AbstractMacroNode parentNode) {
		AbstractMacroNode tempNode = parentNode;
		if(isComponent(e.getNodeName())) {
			tempNode = processComponent(e, parentNode);
//			if(parentNode.getName().equalsIgnoreCase("group") && validNotSolveNodeTag(tempNode)){
//				parentNode.setName(tempNode.getName() + "..." + tranferTagName(e.getTagName()) + " group");
//			}
							
			//除了form和table以外的group判斷
			//在此加入parentNode path判斷，達成group需求
			boolean isFilterIndex = true; 
			if(e.getNodeName().equalsIgnoreCase(checkNodeName) && findXpath((org.w3c.dom.Element)e.getParentNode(), isFilterIndex).equals(checkParentNodePath)){
				if(isGroup){
					if(!isAlreadyExist) {
						checkNode.getParent().add(tempNode);
					}
				}
				else{
					if(!isAlreadyExist) {
						if(!checkExist){
							parentNode.remove(tempNode);
							AbstractMacroNode groupNode = processMacroComponent(checkNodeName.toLowerCase()+" group", parentNode);
							groupNode.add(checkNode);
							groupNode.add(tempNode);	
							groupNode.setName(checkNode.getName() + "..." + tranferTagName(checkNodeName) +" group");							
						}
						else{
							checkNode.getParent().add(tempNode);
							checkNode.getParent().setName(checkNode.getParent().getName().replaceAll(newNodePreName, ""));
						}
					}
					isGroup = true;
				}
			}
			else{
				setCheckNodeData(e, tempNode);
			}
		}
		
		//處理<form>作group
		if(e.getNodeName().equalsIgnoreCase("form") && !isAlreadyExist) {
			parentNode.remove(tempNode);			
			AbstractMacroNode newGroupNode = processMacroComponent(tempNode.getName() + " group", parentNode);
			if(compareMode) {
				newGroupNode.setName(newNodePreName + newGroupNode.getName());
			}		
			newGroupNode.add(tempNode);
			tempNode = newGroupNode;			
		}
		
//		//處理<ul>、<ol>作group
//		if (e.getNodeName().equalsIgnoreCase("ul") || e.getNodeName().equalsIgnoreCase("ol")){
//			tempNode = processMacroComponent("group", parentNode);
//			setCheckNodeData(e, tempNode);
//		}					
		return tempNode;
	}
	
	private void setCheckNodeData(Element e, AbstractMacroNode tempNode){
		checkNodeName = e.getNodeName().toLowerCase();
		checkNode = tempNode;
		boolean isFilterIndex = true;
		checkParentNodePath = findXpath((org.w3c.dom.Element)e.getParentNode(), isFilterIndex);
		checkExist = isAlreadyExist;
		isGroup = false;			
	}

	private static boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}
	
	private AbstractMacroNode processComponent(Element e, AbstractMacroNode parentNode) {
		ComponentNode tempNode = ComponentNode.create();
		//針對非linkText做處理
		if(e.hasChildNodes() && e.getFirstChild().getNodeName().equalsIgnoreCase("img")) {
			tempNode.setName(getComponentName((org.w3c.dom.Element)e.getChildNodes().item(0)));
			//做優化縮減xpath的路徑動作
			tempNode.setText(refineXpath(e));			
			//直接找xpath, 不做優化
//			tempNode.setText(findXpath((org.w3c.dom.Element)e.getChildNodes().item(0)));
			tempNode.setTitle(findId((org.w3c.dom.Element)e.getChildNodes().item(0), tempNode.getText()));
			tempNode.setComponentName(findName((org.w3c.dom.Element)e.getChildNodes().item(0), tempNode.getText()));			
			tempNode.setType(getComponentType((org.w3c.dom.Element)e.getChildNodes().item(0)));
		}
		else {
			tempNode.setName(getComponentName(e));		
			//做優化縮減xpath的路徑動作
			tempNode.setText(refineXpath(e));
			//直接找xpath, 不做優化
//			tempNode.setText(findXpath(e));
			tempNode.setTitle(findId(e, tempNode.getText()));
			tempNode.setComponentName(findName(e, tempNode.getText()));
			tempNode.setType(getComponentType(e));
		}
		
		compareComponentNodes(tempNode, startMacroNode);		
		if(findNode == null) {
			isAlreadyExist = false;
			parentNode.add(tempNode);			
			if(compareMode) {
				tempNode.setName(newNodePreName + tempNode.getName());
			}			
		}
		else{
			isAlreadyExist = true;
			tempNode = (ComponentNode) findNode;
			findNode = null;
		}
		vExistNodes.add(tempNode);

		return tempNode;		
	}
	
	private AbstractMacroNode processMacroComponent(String nodeName, AbstractMacroNode parentNode) {
		MacroComponentNode tempNode = MacroComponentNode.create(nodeName);		
		parentNode.add(tempNode);		
		return tempNode;					
	}
	
//	private void startCompareNodes(AbstractMacroNode tempNode){
//		for(int i = 0; i < tempNode.getChildren().length; i++){
//			if(tempNode.getChildren()[i] instanceof ComponentNode){
//				compareComponentNodes((ComponentNode) tempNode.getChildren()[i], startMacroNode);
//				if(findNode!=null){
//					findNode.setName(((ComponentNode) tempNode.getChildren()[i]).getName());
//					findNode=null;
//				}
//				else{								
//					addNewNode(tempNode.getChildren()[i]);
//				}
//			}
//			else{
//				startCompareNodes(tempNode.getChildren()[i]);
//			}
//		}	
//	}
	
	private void compareComponentNodes(ComponentNode tempNode, AbstractMacroNode originalNode){		
		for(int i = 0; i < originalNode.getChildren().length; i++){
			if(originalNode.getChildren()[i] instanceof ComponentNode){
				if(((ComponentNode) originalNode.getChildren()[i]).getType().equalsIgnoreCase(tempNode.getType())){
					if(!tempNode.getTitle().isEmpty() && ((ComponentNode) originalNode.getChildren()[i]).getTitle().equalsIgnoreCase(tempNode.getTitle())) {
						originalNode.getChildren()[i].setName(tempNode.getName().replaceAll(newNodePreName, ""));						
						findNode = originalNode.getChildren()[i];
						break;
					}
					else if(!tempNode.getText().isEmpty() && ((ComponentNode) originalNode.getChildren()[i]).getText().equalsIgnoreCase(tempNode.getText())) {
						originalNode.getChildren()[i].setName(tempNode.getName().replaceAll(newNodePreName, ""));
						findNode = originalNode.getChildren()[i];
						break;
					}
				}
			}
			else{
				compareComponentNodes(tempNode, originalNode.getChildren()[i]);
			}
		}		
	}
	
	private void moveAndClearComponents(AbstractMacroNode macroComponent) {
		Vector<AbstractMacroNode> vRemovedNodes = new Vector<AbstractMacroNode>();
		if(macroComponent.getName().contains(newNodePreName)) {
			vNewNodes.add(macroComponent);
		}
		else{
			for(int i = 0; i < macroComponent.getChildren().length; i++) {
				if(macroComponent.getChildren()[i] instanceof ComponentNode){
					boolean isExist = false;
					if(vExistNodes.contains(macroComponent.getChildren()[i])) {
						isExist = true;
					}
					if(!isExist){
						vRemovedNodes.add(macroComponent.getChildren()[i]);
					}
					if(macroComponent.getChildren()[i].getName().contains(newNodePreName)) {
						vNewNodes.add(macroComponent.getChildren()[i]);
					}
				}
				else{
					moveAndClearComponents(macroComponent.getChildren()[i]);
				}
			}
		}
		
		if(vRemovedNodes.size() > 0) {
			if(vRemovedNodes.size() == macroComponent.getChildren().length) {
				updateOldComponents(macroComponent);			
			}
			else{
				for(int i = 0; i < vRemovedNodes.size(); i++){
					updateOldComponents(vRemovedNodes.get(i));
		//			macroComponent.remove(vClearNodes.get(i));
				}
			}
			vRemovedNodes.clear();
		}	
	}
	
	private void updateOldComponents(AbstractMacroNode node) {
		removedMacroNode.add(node.clone());
	}
	
	private void updateNewCompoenents() {
		if(vNewNodes.size() > 0) {
			for(int i = 0; i < vNewNodes.size(); i++){
				vNewNodes.get(i).getParent().remove(vNewNodes.get(i));
				newMacroNode.add(vNewNodes.get(i));
				vNewNodes.get(i).setName(vNewNodes.get(i).getName().replaceAll(newNodePreName, ""));
			}
			vNewNodes.clear();
		}				
	}
	
	private String tranferTagName(String tagName){
		if(tagName.equalsIgnoreCase("a")) {
			return " link"; 
		}
		return tagName.toLowerCase();
	}
	
	private String findXpathAttribute(Element e, boolean isFilterIndex) {
		String xpath = "/" + e.getNodeName().toLowerCase();
		//如果有id或name就記錄id或name
		if(e.hasAttribute("id")) {
			xpath = xpath + "[";
			xpath = xpath + "@id='" + e.getAttribute("id") + "'";
			xpath = xpath + "]";
		}
		
		if(e.hasAttribute("href")) {
			xpath = xpath + "[";
			xpath = xpath + "@href='" + e.getAttribute("href") + "'";
			xpath = xpath + "]";
		}
		
		if(e.hasAttribute("name")) {
			xpath = xpath + "[";
			xpath = xpath + "@name='" + e.getAttribute("name") + "'";
			xpath = xpath + "]";
		}		
		
		if(!isFilterIndex){
			if(e.hasAttribute("class")) {
				xpath = xpath + "[";
				xpath = xpath + "@class='" + e.getAttribute("class") + "'";
				xpath = xpath + "]";
			}
		}
		
		//沒有id或name就用順序判斷
		if(!e.hasAttribute("name") && !e.hasAttribute("id") && !e.hasAttribute("href") && !e.hasAttribute("class")) {
			Node parentNode = e.getParentNode();
			//取得tag的順序，比較group時須移除
			if(!isFilterIndex){
				int count = 0, position = 0;
				
				for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
					if(parentNode.getChildNodes().item(i).getNodeName().equalsIgnoreCase(e.getNodeName()))
						count++;				
					if(parentNode.getChildNodes().item(i).equals(e))
						position = count;
				}
				if(count > 1){
					xpath = xpath + "[";				
					xpath = xpath + String.valueOf(position);
					xpath = xpath + "]";				
				}
			}
		}
	
		return xpath;
	  } 

	private String findXpath(Element e, boolean isFilterIndex) {
		String xpath = findXpathAttribute(e, isFilterIndex);
	   	Node tempNode = e.getParentNode();
		//找出父節點的路徑
		while(true) {
			try {
				if(tempNode.getNodeName().equalsIgnoreCase("HTML")) {
					xpath = findXpathAttribute((org.w3c.dom.Element)tempNode, isFilterIndex) + xpath;
					break;
				}
				xpath = findXpathAttribute((org.w3c.dom.Element)tempNode, isFilterIndex) + xpath;
				tempNode = tempNode.getParentNode();
			} catch (Exception x) {
				// TODO Auto-generated catch block
				break;
			}
		}
		return xpath;
	}
	
	private String refineXpath(Element e) {
		boolean isFilterIndex = false;
		String xpath = "//" + e.getNodeName().toLowerCase();
		if(e.hasAttribute("id")) {
			xpath = xpath + "[";
			xpath = xpath + "@id='" + e.getAttribute("id") + "'";
			xpath = xpath + "]";
			return xpath;
		}
		
		if(e.hasAttribute("href") && !e.getAttribute("href").equals("#")) {
			xpath = xpath + "[";
			xpath = xpath + "@href='" + e.getAttribute("href") + "'";
			xpath = xpath + "]";
			return xpath;			
		}
		
		if(e.hasAttribute("src")) {
			xpath = xpath + "[";
			xpath = xpath + "@src='" + e.getAttribute("src") + "'";
			xpath = xpath + "]";
			return xpath;			
		}
		
		if(e.hasAttribute("name")) {
			xpath = xpath + "[";
			xpath = xpath + "@name='" + e.getAttribute("name") + "'";
			xpath = xpath + "]";
			return xpath;			
		}		
		
		return findXpath(e, isFilterIndex);
		
	}
	
	private String findId(Element e, String xpath) {
		String id = "";
		if(e.hasAttribute("id")) {
			id = e.getAttribute("id");
		}
		return id;
	}
	
	private String findName(Element e, String xpath) {
		String name = "";
		if(e.hasAttribute("name")){
			name = e.getAttribute("name");
		}		
		return name;
	}
	
	private boolean isComponent(String type) {
		if(type.equalsIgnoreCase("title")) {
			return true;
		} 
		if(type.equalsIgnoreCase("input")) {
			return true;
		} 
		if(type.equalsIgnoreCase("a")) {
			return true;
		} 
		
		if(type.equalsIgnoreCase("textarea")) {
			return true;
		} 
		
		if(type.equalsIgnoreCase("form")) {
			return true;
		} 
		
		if(type.equalsIgnoreCase("button")) {
			return true;
		} 
		
		if(type.equalsIgnoreCase("select")) {
			return true;
		} 
		
		if(type.equalsIgnoreCase("img")) {
			return true;
		}
		
		return false;
	}
	
	private String getComponentType(Element e) {
		if(e.getNodeName().equalsIgnoreCase("form")) {
			return "gtt.eventmodel.web.HtmlForm";
		} 
		if(e.getNodeName().equalsIgnoreCase("input")) {
			if(e.getAttribute("type").equalsIgnoreCase("button") || e.getAttribute("type").equalsIgnoreCase("submit") || e.getAttribute("type").equalsIgnoreCase("reset")){
				return "gtt.eventmodel.web.HtmlButton";
			}
			else if(e.getAttribute("type").equalsIgnoreCase("radio")){
				return "gtt.eventmodel.web.HtmlRadio";
			}
			else if(e.getAttribute("type").equalsIgnoreCase("checkbox")){
				return "gtt.eventmodel.web.HtmlCheckbox";
			}
			else if(e.getAttribute("type").equalsIgnoreCase("text") || e.getAttribute("type").equalsIgnoreCase("hidden") || e.getAttribute("type").equalsIgnoreCase("password")){
				return "gtt.eventmodel.web.HtmlInputText";								
			}
			else if(e.getAttribute("type").equalsIgnoreCase("image")){
				return "gtt.eventmode.web.HtmlImg";
			}
			else{
				return "gtt.eventmodel.web.HtmlElement";
			}
		} 
		if(e.getNodeName().equalsIgnoreCase("a")) {
			if(e.hasChildNodes() && e.getFirstChild().getNodeName().equalsIgnoreCase("img")){
				return "gtt.eventmodel.web.HtmlImg";
			}
			else{
				return "gtt.eventmodel.web.HtmlLink";
			}
		} 
		
		if(e.getNodeName().equalsIgnoreCase("button")) {
			return "gtt.eventmodel.web.HtmlButton";
		} 
		
		if(e.getNodeName().equalsIgnoreCase("textarea")) {
			return "gtt.eventmodel.web.HtmlTextArea";
		} 
		
		if(e.getNodeName().equalsIgnoreCase("table")) {
			return "gtt.eventmodel.web.HtmlTable";
		} 
		
		if(e.getNodeName().equalsIgnoreCase("select")) {
			return "gtt.eventmodel.web.HtmlSelect";
		}
		
		if(e.getNodeName().equalsIgnoreCase("img")) {
			return "gtt.eventmodel.web.HtmlImg";
		} 
		
		if(e.getNodeName().equalsIgnoreCase("form")) {
			return "gtt.eventmode.web.HtmlFrom";
		}
		
		return "gtt.eventmodel.web.HtmlElement";
	}
	
	private String getComponentName(Element e) {

		int count = 1;
		String tempComponentName = "";		
		Node parentNode = e.getParentNode();
		//title
		if(e.getNodeName().equalsIgnoreCase("title")) {
			if(e.getTextContent().trim().length() > 0){
				return e.getTextContent().trim();
			}
			return e.getNodeName().toLowerCase();
		}
		
		//a 
		if(e.getNodeName().equalsIgnoreCase("a")) {
			for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
				if(parentNode.getChildNodes().item(i).equals(e))
					break;
				if(parentNode.getChildNodes().item(i).getTextContent().equalsIgnoreCase(e.getTextContent()))
					count++;
			}
			
			if(e.getTextContent().trim().length() == 0){
				return "link" + count; 
			}			
			if(count > 1){
				return e.getTextContent().trim() + count;
			}
			return e.getTextContent().trim();
		}
		
		//img
		if(e.getNodeName().equalsIgnoreCase("img")) {
			for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
				if(parentNode.getChildNodes().item(i).equals(e))
					break;
					try{
						if(parentNode.getChildNodes().item(i).getAttributes().getNamedItem("alt").getNodeValue().equalsIgnoreCase(e.getAttribute("alt")))
							count++;
					} catch (Exception ex) {
						// TODO: handle exception
						//如果父節點的子節點中有相同的img就比對alt屬性，但未必全部都有alt屬性，如果沒有就發生例外，不列入計數，程式繼續執行						
					}
			}
					
			if(e.getAttribute("alt").trim().length() == 0){
				return "img" + count; 
			}				
			if(count > 1){
				return e.getAttribute("alt").trim() + count;
			}
			return e.getAttribute("alt").trim();
		}
		
		//input 
		if(e.getNodeName().equalsIgnoreCase("input") && e.hasAttribute("name")) {
			for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
				if(parentNode.getChildNodes().item(i).equals(e))
						break;
				if(parentNode.getChildNodes().item(i).getNodeName().equalsIgnoreCase("input")) {
					try {
						if(parentNode.getChildNodes().item(i).getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase(e.getAttribute("name")))
							count++;
					} catch (Exception e2) {
					// TODO: handle exception
					//如果父節點的子節點中有相同的input就比對名稱，但未必全部都有name屬性，如果沒有就發生例外，不列入計數，程式繼續執行
					}
				}
				
			}
			
			if(e.getAttribute("name").trim().length() == 0){
				return "input" + count;
			}
			if(e.getAttribute("name").equalsIgnoreCase("button") || e.getAttribute("name").equalsIgnoreCase("submit") || e.getAttribute("name").equalsIgnoreCase("cancel")){
				tempComponentName = e.getAttribute("id").trim() + " button";				
			}
			else if(e.getAttribute("name").equalsIgnoreCase("radio") || e.getAttribute("name").equalsIgnoreCase("checkbox")){
				tempComponentName = e.getAttribute("id").trim() + " " + e.getAttribute("name");
			}
			else{
				tempComponentName = e.getAttribute("name");				
			}
			if(count > 1){
				return tempComponentName.trim() + count;
			}
			return tempComponentName.trim();
		}
		
		//other
		for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
			if(parentNode.getChildNodes().item(i).equals(e))
					break;
			if(parentNode.getChildNodes().item(i).getNodeName().equalsIgnoreCase(e.getNodeName()))
					count++;
		}
		
		tempComponentName = e.getAttribute("id").trim() + " " + e.getNodeName().toLowerCase();		
		if(count > 1){
			return tempComponentName.trim() + count;
		}
		return tempComponentName.trim();
	}
}
