package testing.web;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.eventmodel.web.HTMLComponent;
import gtt.runner.web.WebController;
import gtt.tester.web.WebTester;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jibble.simplewebserver.SimpleWebServer;
import org.openqa.selenium.By;

public class WebControllerTest extends TestCase {
	WebTester webTester;
	static SimpleWebServer server = null;
	int port = 7788;
	
	protected void setUp() throws Exception  {
		super.setUp();
		server = initialServer();
		webTester = new WebTester();
	}	
	
	protected void tearDown() throws Exception {
		super.tearDown();
		WebController.instance().closeAllBrowser();
	}	
	
	public void testFind() {
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setText("//button[1]");
		WebController.instance().startupWebPage("http://127.0.0.1:" + port + "/testPage1.html");
		assertEquals(WebController.instance().find(iconponment).getAttribute("type"), "button");
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setTitle("1");
		assertEquals(WebController.instance().find(iconponment).getAttribute("type"), "button");
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setName("testbutton");
		assertEquals(WebController.instance().find(iconponment).getAttribute("type"), "button");
		
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setName("test");
		iconponment.setIndex(1);
		assertEquals(WebController.instance().find(iconponment).getAttribute("value"), "1");
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setName("test");
		iconponment.setIndex(5);
		assertEquals(WebController.instance().find(iconponment).getAttribute("value"), "5");
		
		
	}
	
	public void testFindByLinkText() {
		
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlLink");
		iconponment.setWinType("this is a link");
		WebController.instance().startupWebPage("http://127.0.0.1:" + port + "/testPage1.html");
		assertEquals(WebController.instance().find(iconponment).getAttribute("href"), "testPage2.html");
		
	}
	
	public void testFindList() {
		WebController.instance().startupWebPage("http://127.0.0.1:" + port + "/testPage2.html");
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setText("//input");
		assertEquals(WebController.instance().findComponents(iconponment).size(), 6);
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setTitle("2");
		assertEquals(WebController.instance().findComponents(iconponment).size(), 3);
		
		iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setName("component1");
		assertEquals(WebController.instance().findComponents(iconponment).size(), 2);
		
	}
	
	public void testClickAlertOK() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setName("test_btn_1");
		iconponment.setText("//input[@id='test_btn_1']");
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testWebControllerPage.html");
		assertTrue(WebController.instance().getDriver().findElements(By.xpath(iconponment.getText())).size()>0);
		
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		WebController.instance().clickAlertOK();
		webTester.fire(eventnode);	
	}
	
	public void testClickConfirmOK() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setName("test_btn_2");
		iconponment.setText("//input[@id='test_btn_2']");
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testWebControllerPage.html");
		assertTrue(WebController.instance().getDriver().findElements(By.xpath(iconponment.getText())).size()>0);
		
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		//點擊  confirm 必須在發event動作之前		
		WebController.instance().clickConfirmOK();
		webTester.fire(eventnode);		
		assertEquals("Test Page 1", WebController.instance().getDriver()
				.getTitle());		
	}
	
	public void testClickConfirmCANCEL() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setName("test_btn_2");
		iconponment.setText("//input[@id='test_btn_2']");
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testWebControllerPage.html");
		assertTrue(WebController.instance().getDriver().findElements(By.xpath(iconponment.getText())).size()>0);
		
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		//點擊  confirm 必須在發event動作之前	
		WebController.instance().clickConfirmCANCEL();		
		webTester.fire(eventnode);			
		assertEquals("Test ALERT/CONFIRM Page", WebController.instance().getDriver()
				.getTitle());	
	}
	
	private SimpleWebServer initialServer() {
		if(server == null) {
			try {
				return new SimpleWebServer(new File("./testPage"), port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return server;
			}
		}
		return server;
	}
}
