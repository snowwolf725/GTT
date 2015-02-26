package testing.web;

import gtt.eventmodel.web.HtmlTable;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jibble.simplewebserver.SimpleWebServer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlTableTest extends TestCase {
	static SimpleWebServer server = null;
	int port = 7788;
	WebDriver webdriver = null;
	HtmlTable htmltable = null;
	
	protected void setUp() throws Exception  {
		super.setUp();
		server = initialServer();
		
		webdriver = new HtmlUnitDriver();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		webdriver.close();
	}

	public void testTableColSize() {
		webdriver.get("http://127.0.0.1:" + port + "/testPage1.html");
		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[1]")));
		assertEquals(htmltable.getTableColSize(), 2);

		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[2]")));
		assertEquals(htmltable.getTableColSize(), 2);
	}
	
	public void testTableRowSize() {
		webdriver.get("http://127.0.0.1:" + port + "/testPage1.html");
		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[1]")));
		assertEquals(htmltable.getTableRowSize(), 3);

		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[2]")));
		assertEquals(htmltable.getTableRowSize(), 2);
	}
	
	public void testTableGetValue() {
		webdriver.get("http://127.0.0.1:" + port + "/testPage1.html");
		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[1]")));
		assertEquals(htmltable.getTableValue(1, 1), "11");
		assertEquals(htmltable.getTableValue(1, 2), "12");
		assertEquals(htmltable.getTableValue(2, 1), "21");
		assertEquals(htmltable.getTableValue(2, 2), "22");
		
		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[1]//table")));
		assertEquals(htmltable.getTableValue(1, 1), "t1");
		assertEquals(htmltable.getTableValue(1, 2), "t2");
		assertEquals(htmltable.getTableValue(2, 1), "t3");
		assertEquals(htmltable.getTableValue(2, 2), "t4");
		
		htmltable = new HtmlTable(webdriver.findElement(By.xpath("//table[2]")));
		assertEquals(htmltable.getTableValue(1, 1), "21");
		assertEquals(htmltable.getTableValue(1, 2), "22");
		assertEquals(htmltable.getTableValue(2, 2), "32");
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
