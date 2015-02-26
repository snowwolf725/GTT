package android4gtt.eventmodel;

import gtt.eventmodel.Argument;
import gtt.eventmodel.IDescriptorReader;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class AndroidDescriptorReader implements IDescriptorReader {
	AndroidModel theModel = null;
	String cur_Scope = "";
	String m_File;
	
	public AndroidDescriptorReader(String desc_file) {
		m_File = desc_file;
	}
	
	public AndroidDescriptorReader() {
	
	}
	
	public void setModel(IEventModel model) {
		theModel = (AndroidModel)model;
	}
	
	public boolean read() {
		if(theModel == null) 
			return false;
		
		try {
			DOMParser parser = new DOMParser();
			parser.parse(m_File);
			Document doc = parser.getDocument();
			
			if (doc.getChildNodes().item(0).getNodeName().compareTo(
					AndroidTag.ROOT_NAME) != 0) {
				System.out.println("Component descriptor format is not correct.");
				return false;
			}
			
			doInterpret(doc);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("DOMParser do parse ERROR for file \"" + m_File
				+ "\".");
		return false;
	}
	
	public boolean read(InputStream desc_file) {
		if(theModel == null) 
			return false;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(desc_file);
			
			if (doc.getChildNodes().item(0).getNodeName().compareTo(
					AndroidTag.ROOT_NAME) != 0) {
				System.out.println("Component descriptor format is not correct.");
				return false;
			}
			
			doInterpret(doc);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("DOMParser do parse ERROR for file \"" + m_File
				+ "\".");
		return false;
	}

	private void doInterpret(Document doc) {
		NodeList childs = doc.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			if (isElementNode(childs.item(i))) {
				dispatch((Element) childs.item(i));
			}
		}
	}
	
	protected void dispatch(Node node) {
		if( !isElementNode(node) ) return;
		
		Element ele = (org.w3c.dom.Element) node;
		String name = ele.getNodeName();
		
		/*
		 * <Inheritnace> </Inheritance>
		 */
		if (name.compareTo(AndroidTag.SCOPE) == 0) {
			branchNode(ele);// for INHERITANCE relationship
		}
		
		/*
		 * <SwingComponent> </SwingComponent>
		 */
		if (name.compareTo(AndroidTag.COMPONENT_SET) == 0) {
			componentSetNode(ele);	// for Component
		}
		
		/*
		 * <Events> </Events>
		 */
		if (name.compareTo(AndroidTag.EVENT_SET) == 0) {
			eventSetNode(ele);	// for EventData
		}		
		
		// others, ignore it
	}

	private void branchNode(Element ele) {
		String old_scope = cur_Scope;
		
		
		
		// 切至新一層的scope
		if( cur_Scope.length()==0 )
			cur_Scope = ele.getAttribute(AndroidTag.NAME);
		else {
			cur_Scope += "." + ele.getAttribute(AndroidTag.NAME);
			// 將之前scope所屬的事件，都一併加至cur_Scope中
			addEventIntoModel(old_scope);
		}
		
		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++)
			dispatch(ele.getChildNodes().item(idx));
		
		// 換回這一層的scope
		cur_Scope = old_scope;
	}
	
	private void addEventIntoModel(String scope) {
		String sub_scope = scope;
		while( sub_scope != "" ) {
			addEventList(sub_scope);
			int idx = sub_scope.lastIndexOf(".");
			if(idx == -1) break; 	// 不需要再切了 
			sub_scope = sub_scope.substring(0, idx );
		}
	}

	private void addEventList(String sub_scope) {
		List<IEvent> events = theModel.getEvents(sub_scope);
		Iterator<IEvent> ite = events.iterator();
		while(ite.hasNext()) {
			theModel.addEvent( (IEvent)ite.next(), cur_Scope);
		}
	}	

	/**
	 * ele 為一個 <SwingComponent>...</SwingComponent>的子樹
	 * 裡面定義了數個component資訊
	 * @param ele
	 */
	private void componentSetNode(Element ele) {
		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++) {
			if( isElementNode(ele.getChildNodes().item(idx)))
				singleComponentNode( (Element) ele.getChildNodes().item(idx));
		}
	}
	
	/**
	 * 從 <Component>...</Component>取得一個Component資訊，並加到model中
	 * 元件資訊只保留Component Type及 Window Type (如JFrame、JDialog等)
	 * @param ele
	 */
	private void singleComponentNode(Element ele) {
		// find a swing component information
		String type = ele.getAttribute(AndroidTag.COMPONENT_CLASS_TYPE);
		
		/* 增加一個新的 SwingComponent */
		AndroidComponent c = AndroidComponent.createComponent(type, "", "", "", "", 0, 0);
		theModel.addComponent( c );
		/* 記錄 SwingComponent　的package資訊 */
		theModel.addScope(c, cur_Scope);
	}

	
	/**
	 * ele 為一個 <Events> </Events>的子樹
	 * 裡面定義了數個事件資訊
	 * @param ele
	 */
	private void eventSetNode(Element ele) {
		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++) {
			if( isElementNode(ele.getChildNodes().item(idx)))
				singleEventNode( (Element) ele.getChildNodes().item(idx));
		}
	}
	
	/**
	 * 從 <Event>...</Event>取得一個Event資訊
	 * @param ele
	 */
	private void singleEventNode(Element ele) {
		// find a swing component information
		String name = ele.getAttribute(AndroidTag.NAME);	
		String data = ele.getAttribute(AndroidTag.DATA);
		
		IEvent event = theModel.createEvent( Integer.parseInt(data), name);

		// 取得event所需要的參數s
		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++) {
			if( isElementNode(ele.getChildNodes().item(idx))) {
				arguments( event, (Element) ele.getChildNodes().item(idx));
				break; // 一個event只需要一組 arguments
			}
		}

		theModel.addEvent( event, cur_Scope );
	}

	private void arguments(IEvent theEvent, Element argus) {
		for (int idx = 0; idx < argus.getChildNodes().getLength(); idx++) {
			if( isElementNode(argus.getChildNodes().item(idx))) {
				// get a argument
				Element e = (Element) argus.getChildNodes().item(idx);
				Argument a = Argument.create( e.getAttribute("type"),  e.getAttribute("name"), "" );
				theEvent.getArguments().add(a);
			}
		}
	}
	
//	private void customizeTypeNode(Element ele) {
//		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++) {
//			if (isElementNode(ele.getChildNodes().item(idx))) {
//				singleCustomizeType( (Element)ele.getChildNodes().item(idx));
//			}
//		}
//	}
	
//	private void singleCustomizeType(Element ele) {
//		System.out.println(ele.getAttribute("name") + " "+ ele.getAttribute("dataType"));
//	}

	public static boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

}