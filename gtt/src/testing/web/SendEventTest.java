package testing.web;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.eventmodel.web.HTMLComponent;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.runner.web.WebController;
import gtt.tester.macro.MacroTester;
import gtt.tester.web.WebTester;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gttlipse.GTTlipseConfig;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jibble.simplewebserver.SimpleWebServer;
import org.openqa.selenium.By;


public class SendEventTest extends TestCase {

	WebTester webTester;
	static SimpleWebServer server = null;
	int port = 7788;

	protected void setUp() throws Exception {
		super.setUp();
		server = initialServer();
		webTester = new WebTester();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		WebController.instance().closeAllBrowser();
	}

	public void testSendButtonEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlButton");
		iconponment.setName("testbutton");
		iconponment.setText("//button[@name='testbutton']");
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");
		assertTrue(WebController.instance().getDriver().findElements(By.xpath(iconponment.getText())).size()>0);
		
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);		
		webTester.fire(eventnode);
		assertEquals("Test Page 2", WebController.instance().getDriver()
				.getTitle());
	}

	public void testSendLinkEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlLink");
		iconponment.setName("testlink");
		iconponment.setText("//a[@href='testPage2.html']");
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");
		webTester.fire(eventnode);
		assertEquals("Test Page 2", WebController.instance().getDriver()
				.getTitle());
	}

	public void testInputTextEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlInputText");
		iconponment.setName("testInputText");
		iconponment.setText("//input[@name='component1']");
		iconponment.setIndex(1);
		IEvent ievent = SwingEvent.create(10003, "INPUT");
		Argument arg = Argument
				.create("java.lang.String", "InputValue", "test");
		Arguments argList = new Arguments();
		argList.add(arg);
		ievent.setArguments(argList);
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage2.html");
		webTester.fire(eventnode);
		assertEquals("1test", WebController.instance().getDriver().findElement(
				By.xpath("//input[@name='component1']")).getValue());
	}
	
	public void testRadioEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlRadio");
		iconponment.setName("radio1");
		iconponment.setText("//input[@type='radio']");
		iconponment.setIndex(1);
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);		
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");		
		webTester.fire(eventnode);
	}
	
	public void testCheckboxEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlCheckbox");
		iconponment.setName("checkbox1");
		iconponment.setText("//input[@type='checkbox']");
		iconponment.setIndex(1);
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);		
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");
		webTester.fire(eventnode);
	}	
	
	public void testImgClickEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlImg");
		iconponment.setName("testImg");
		iconponment.setText("//img[@src='logo00.jpg']");
		iconponment.setIndex(1);
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		Argument arg = Argument
				.create("java.lang.String", "InputValue", "test");
		Arguments argList = new Arguments();
		argList.add(arg);
		ievent.setArguments(argList);
		EventNode eventnode = m_NodeFactory
				.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");
		webTester.fire(eventnode);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("Test Page 2", WebController.instance().getDriver()
				.getTitle());
	}	
	
	public void testElementClickEvent() {
		NodeFactory m_NodeFactory = new NodeFactory();
		IComponent iconponment = HTMLComponent.createDefault();
		iconponment.setType("gtt.eventmodel.web.HtmlElement");
		iconponment.setName("testElement");
		iconponment.setText("//div[@id='element1']");
		iconponment.setIndex(1);
		IEvent ievent = SwingEvent.create(10002, "CLICK");
		EventNode eventnode = m_NodeFactory.createEventNode(iconponment, ievent);
		eventnode.setEvent(ievent);
		eventnode.setComponent(iconponment);		
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage1.html");		
		webTester.fire(eventnode);
	}	
	
	public void testAssertList() throws Exception {
		GTTlipseConfig.setToTestingOnWebPlatform();
		MacroComponentNode root = new MacroComponentNode();
		ComponentNode conponment = ComponentNode.create();

		ViewAssertNode van = new ViewAssertNode();
		MacroEventNode men = new MacroEventNode();
		root.setName("root");

		root.add(conponment);
		root.add(men);
		conponment.setType("gtt.eventmodel.web.HtmlInputText");		
		conponment.setName("testInputText");
		conponment.setText("//input[@id='2']");
		van.setComponentPath("root::testInputText");
		van.setName("assertValue1Nums");		
		van.getAssertion().setExpectedSizeOfCheck(3);
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage2.html");
		MacroTester macroTester = new MacroTester(new MacroDocument(root));
		men.add(van);
		assertEquals(true, macroTester.fire(men));
	}

	public void testAssertList2() throws Exception {
		GTTlipseConfig.setToTestingOnWebPlatform();
		MacroComponentNode root = new MacroComponentNode();
		ComponentNode conponment = ComponentNode.create();
		
		ViewAssertNode van = new ViewAssertNode();
		MacroEventNode men = new MacroEventNode();
		root.setName("root");
		
		root.add(conponment);
		root.add(men);
		conponment.setType("gtt.eventmodel.web.HtmlInputText");			
		conponment.setName("testInputText");
		conponment.setText("//input[@id='3']");
		van.setComponentPath("root::testInputText");
		van.setName("assertValue1Nums");
		van.getAssertion().setMethodName("getValue");
		van.getAssertion().setValue("4");
		WebController.instance().startupWebPage(
				"http://127.0.0.1:" + port + "/testPage2.html");
		MacroTester macroTester = new MacroTester(new MacroDocument(root));
		men.add(van);
		assertEquals(true, macroTester.fire(men));
	}

	private SimpleWebServer initialServer() {
		if (server == null) {
			try {
				return new SimpleWebServer(new File("./testPage"), port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				return server;
			}
		}
		return server;
	}
}
