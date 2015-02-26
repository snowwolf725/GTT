/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.eventmodel.swing;
import gtt.eventmodel.Argument;
import gtt.eventmodel.IDescriptorReader;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwingDescriptorReader implements IDescriptorReader {
	SwingModel theModel = null;
	String cur_Scope = "";
	String m_File;
	
	public SwingDescriptorReader(String desc_file) {
		m_File = desc_file;
	}
	
	public void setModel(IEventModel model) {
		theModel = (SwingModel)model;
	}
	
	public boolean read() {
		if(theModel == null) 
			return false;
		
		try {
			org.apache.xerces.parsers.DOMParser parser = new org.apache.xerces.parsers.DOMParser();
			parser.parse(m_File);
			Document doc = parser.getDocument();
			
			if (doc.getChildNodes().item(0).getNodeName().compareTo(
					SwingTag.ROOT_NAME) != 0) {
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
		if (name.compareTo(SwingTag.SCOPE) == 0) {
			branchNode(ele);// for INHERITANCE relationship
		}
		
		/*
		 * <SwingComponent> </SwingComponent>
		 */
		if (name.compareTo(SwingTag.COMPONENT_SET) == 0) {
			componentSetNode(ele);	// for Component
		}
		
		/*
		 * <Events> </Events>
		 */
		if (name.compareTo(SwingTag.EVENT_SET) == 0) {
			eventSetNode(ele);	// for EventData
		}
		
		/*
		 * <CustomizeType> </CustomizeType>
		 */
		if (name.compareTo("CustomizeType") == 0) {
			customizeTypeNode(ele);	// for EventData
		}
		
		
		// others, ignore it
	}

	private void branchNode(Element ele) {
		String old_scope = cur_Scope;
		
		
		
		// 切至新一層的scope
		if( cur_Scope.length()==0 )
			cur_Scope = ele.getAttribute(SwingTag.NAME);
		else {
			cur_Scope += "." + ele.getAttribute(SwingTag.NAME);
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
		String winType = ele.getAttribute(SwingTag.WINDOW_CLASS_TYPE);
//		String title = ele.getAttribute(SwingTag.WINDOW_TITLE);
		String type = ele.getAttribute(SwingTag.COMPONENT_CLASS_TYPE);
//		String name = ele.getAttribute(SwingTag.COMPONENT_NAME);
//		String text = ele.getAttribute(SwingTag.TEXT);
//		String index = ele.getAttribute(SwingTag.INDEX_IN_WINDOW);
//		String indexOfName = ele.getAttribute(SwingTag.INDEX_OF_SAME_NAME);
		
		/* 增加一個新的 SwingComponent */
		SwingComponent c = SwingComponent.createComponent(type, winType, "", "", "", 0, 0);
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
		String name = ele.getAttribute(SwingTag.NAME);	
		String data = ele.getAttribute(SwingTag.DATA);
		
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
	
	private void customizeTypeNode(Element ele) {
		for (int idx = 0; idx < ele.getChildNodes().getLength(); idx++) {
			if (isElementNode(ele.getChildNodes().item(idx))) {
				singleCustomizeType( (Element)ele.getChildNodes().item(idx));
			}
		}
	}
	
	private void singleCustomizeType(Element ele) {
//		System.out.println(ele.getAttribute("name") + " "+ ele.getAttribute("dataType"));
	}

	public static boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	@Override
	public boolean read(InputStream in) {
		// TODO Auto-generated method stub
		return false;
	}

}