package testing.web;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.web.htmlPaser.ConvertPageToComponent;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jibble.simplewebserver.SimpleWebServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class ConvertPageToComponentTest extends TestCase{
	
	ConvertPageToComponent cptc = null;
	WebDriver wd = null;
	static SimpleWebServer server = null;
	AbstractMacroNode testNode = null;
	int port = 7788;
	
	protected void setUp() throws Exception  {
		super.setUp();
		server = initialServer();
		wd = new HtmlUnitDriver(); 
		testNode = new MacroComponentNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testConvertTitle() {
		wd.get("http://127.0.0.1:7788/testCmsIndex.html");
		cptc = new ConvertPageToComponent(wd.getPageSource());
		cptc.Convert(testNode);
		assertEquals(testNode.getChildren()[0].getName(), "Easy Conference Management System");
		assertTrue(testNode.getChildren()[0] instanceof ComponentNode);
		assertEquals(testNode.getChildren()[2].getName(), "form group");
		assertTrue(testNode.getChildren()[2] instanceof MacroComponentNode);
	}
	
	private SimpleWebServer initialServer() {
		if(server == null) {
			try {
				return new SimpleWebServer(new File("./testPage"), port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				return server;
			}
		}
		return server;
	}
}
