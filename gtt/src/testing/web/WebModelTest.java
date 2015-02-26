package testing.web;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.WebModel;

import java.util.LinkedList;

import junit.framework.TestCase;

public class WebModelTest extends TestCase {	
	private WebModel webModel;
	
	protected void setUp() throws Exception  {
		super.setUp();
		webModel = new WebModel();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInitialize(){
		String _path = "./descriptor/web.xml";		
		webModel.initialize(_path);
		assertTrue(webModel.getComponentSize()==11);
		for(int i = 0; i < webModel.getComponentSize(); i++){
			if(webModel.getComponents().get(i).getType().equals("gtt.eventmodel.web.HtmlElement")){
				LinkedList<IEvent> event = (LinkedList<IEvent>) webModel.getEvents(webModel.getComponents().get(i));
				assertTrue(event.size()==2);
				assertTrue(event.get(0).getName().equals("CLICK"));
				assertTrue(event.get(1).getName().equals("SET_SELECTED"));
			}
		}
	}
	
}
