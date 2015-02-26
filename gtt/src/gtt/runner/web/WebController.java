package gtt.runner.web;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.web.HtmlButton;
import gtt.eventmodel.web.HtmlElement;
import gtt.eventmodel.web.HtmlForm;
import gtt.eventmodel.web.HtmlInputText;
import gtt.eventmodel.web.HtmlLink;
import gtt.eventmodel.web.HtmlSelect;
import gtt.eventmodel.web.HtmlTable;
import gtt.eventmodel.web.HtmlTextArea;
import gtt.eventmodel.web.HtmlRadio;
import gtt.eventmodel.web.HtmlCheckbox;
import gtt.eventmodel.web.HtmlImg;
import gtt.eventmodel.web.HtmlBaseElement;
import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.GTTlipseConfig;
import gttlipse.web.WebTestingConfig;
import gttlipse.web.htmlPaser.ConvertPageToComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class WebController {

	private WebDriver webDriver = null;

	private String underTestURL = null;
	
	private String windowHandle = null;
	
	private int m_AJAXwaittime = 0;

	public String getWindowHandle() {
		return windowHandle;
	}
	private final static WebController m_Instance = new WebController();

	public WebController() {
	}

	private void initDriver() {
		if (GTTlipseConfig.getInstance().getWebDriverType() == WebTestingConfig.WEB_DRIVER_HTMLUNIT) {
			webDriver = new HtmlUnitDriver();
		} else if (GTTlipseConfig.getInstance().getWebDriverType() == WebTestingConfig.WEB_DRIVER_InternetExplorer) {
			webDriver = new InternetExplorerDriver();
		} else if (GTTlipseConfig.getInstance().getWebDriverType() == WebTestingConfig.WEB_DRIVER_CHROME) {
			webDriver = new ChromeDriver();
			
		} else {
			webDriver = new FirefoxDriver();
		}
		windowHandle = webDriver.getWindowHandle();
	}

	public WebDriver getDriver() {
		return webDriver;
	}

	public static WebController instance() {
		return m_Instance;
	}

	public void startupWebPage(String url) {
		if(webDriver == null)
			initDriver();		
		underTestURL = url;
		webDriver.get(underTestURL);
	}

	public List<HtmlBaseElement> findComponents(IComponent com) {

		List<HtmlBaseElement> foundHtmlElements = new ArrayList<HtmlBaseElement>();

		List<WebElement> allWebElements = obtainAllWebElements(com);
		
		if(allWebElements.size()==0 && m_AJAXwaittime > 0){
			int iCheckTimes = 0;
			do {
				try {
					Thread.sleep(1000);
					allWebElements = obtainAllWebElements(com);
				}
				catch(Exception e) {
					
				}
				iCheckTimes++;
			}
			while(iCheckTimes < m_AJAXwaittime && allWebElements.size()==0);
			m_AJAXwaittime = 0;			
		}
				
		Iterator<WebElement> iter = allWebElements.iterator();
		while (iter.hasNext()) {
			foundHtmlElements.add(WebElementToHtmlElement(iter.next()));
		}

		return foundHtmlElements;
	}

	private List<WebElement> obtainAllWebElements(IComponent com) {
		List<WebElement> temp = null;

		if(!com.getText().isEmpty()) {// 找xpath
			temp = webDriver.findElements(By.xpath(com.getText()));
		}
		else if(!com.getTitle().isEmpty()) {// 找id
			temp = webDriver.findElements(By.id(com.getTitle()));
		}
		else if(!com.getWinType().isEmpty()) {// 找link text
			temp = webDriver.findElements(By.linkText(com.getWinType()));	
		}
		else if(!com.getName().isEmpty()) {// by name
			temp = webDriver.findElements(By.name(com.getName()));
		}
		else {
			temp = new ArrayList<WebElement>();
		}
		typeFilter(temp, com.getType());
		return temp;
	}

	public HtmlBaseElement find(IComponent com) {
		List<HtmlBaseElement> components = null;
		try {
			components = findComponents(com);
			if(components.size() == 0)
				return null;
			if(com.getIndex() == 0) {
				// 如果index為0自動抓取最後一個
				return components.get(components.size() - 1);
			} else {
				return components.get(com.getIndex() - 1);
			}
		} catch (Exception e) {
			System.out.println(e);
//			System.out.println("Components size = " + components.size());
//			System.out.println("Index Value = " + com.getIndex() + ", Component NO Found!");
		}

		return null;
	}
	
	public void closeAllBrowser() {
		if(webDriver != null) {
			Iterator<String> iter = webDriver.getWindowHandles().iterator();
			while(iter.hasNext()) {
				webDriver.switchTo().window(iter.next());
				webDriver.close();
			}
		}
		webDriver = null;
	}
	
	public void clickAlertOK() {
		if(webDriver != null){
			((JavascriptExecutor)webDriver).executeScript("window.alert = function(msg){};");			
//    		new Thread(new Runnable() {
//				@Override
//				public void run() {
//					((JavascriptExecutor)webDriver).executeScript("window.alert = function(msg){};");		
//				}
//    		}).start();			
		}
	}

	public void clickConfirmOK() {
		if(webDriver != null) {		
			((JavascriptExecutor)webDriver).executeScript("window.confirm = function(msg){return true;};");
		}
	}
	
	public void clickConfirmCANCEL() {
		if(webDriver != null) {
			((JavascriptExecutor)webDriver).executeScript("window.confirm = function(msg){return false;};");
		}
	}
	
	public void waitingTime(int iWaitTime) {
		if(webDriver != null && iWaitTime > 0) {
			m_AJAXwaittime = iWaitTime;
		}
	}
	
	public void webTransForm(AbstractMacroNode macroNode) {
		if(webDriver != null && macroNode != null) {
			ConvertPageToComponent ct = new ConvertPageToComponent(webDriver.getPageSource());
			ct.Convert(macroNode);
		}
	}

	private HtmlBaseElement WebElementToHtmlElement(WebElement element) {

		if (element.getTagName().equalsIgnoreCase("input")) {
			if(element.getAttribute("type").equalsIgnoreCase("radio")){
				return new HtmlRadio(element);
			}
			else if(element.getAttribute("type").equalsIgnoreCase("checkbox")){
				return new HtmlCheckbox(element);
			}
			else if(element.getAttribute("type").equalsIgnoreCase("button") || element.getAttribute("type").equalsIgnoreCase("submit") || element.getAttribute("type").equalsIgnoreCase("reset")){
				return new HtmlButton(element);
			}
			else if(element.getAttribute("type").equalsIgnoreCase("text") || element.getAttribute("type").equalsIgnoreCase("password")){
				return new HtmlInputText(element);
			}
			else if(element.getAttribute("type").equalsIgnoreCase("img")){
				return new HtmlImg(element);
			}
			else {
				return new HtmlElement(element); 
			}
		}

		if (element.getTagName().equalsIgnoreCase("textarea")) {
			return new HtmlTextArea(element);
		}

		if (element.getTagName().equalsIgnoreCase("form")) {
			return new HtmlForm(element);
		}

		if (element.getTagName().equalsIgnoreCase("a")) {
			return new HtmlLink(element);
		}

		if (element.getTagName().equalsIgnoreCase("button")) {
			return new HtmlButton(element);
		}
		
		if (element.getTagName().equalsIgnoreCase("select")) {
			return new HtmlSelect(element);
		}
		
		if (element.getTagName().equalsIgnoreCase("table")) {
			return new HtmlTable(element);
		}
		
		if (element.getTagName().equalsIgnoreCase("img")) {
			return new HtmlImg(element);
		}

		return new HtmlElement(element);
	}
	private void typeFilter(List<WebElement> temp, String type) {
		Iterator<WebElement> iter = temp.iterator();
		while(iter.hasNext()) {
			WebElement element = iter.next();
			if(element.getTagName().equalsIgnoreCase("input")) {
				if(!type.equals("gtt.eventmodel.web.HtmlInputText")	
						&& !type.equals("gtt.eventmodel.web.HtmlRadio")
						&& !type.equals("gtt.eventmodel.web.HtmlCheckbox")
						&& !type.equals("gtt.eventmodel.web.HtmlButton")) {
					iter.remove();
				}
			}

			else if(element.getTagName().equalsIgnoreCase("textarea")) {
				if(!type.equals("gtt.eventmodel.web.HtmlTextArea")) {
					iter.remove();
				}
			}

			else if(element.getTagName().equalsIgnoreCase("form")) {
				if(!type.equals("gtt.eventmodel.web.HtmlForm")) {
					iter.remove();
				}
			}

			else if(element.getTagName().equalsIgnoreCase("a")) {
				if(!type.equals("gtt.eventmodel.web.HtmlLink")) {
					iter.remove();
				}
			}

			else if(element.getTagName().equalsIgnoreCase("button")) {
				if(!type.equals("gtt.eventmodel.web.HtmlButton")) {
					iter.remove();
				}
			}
			
			else if(element.getTagName().equalsIgnoreCase("select")) {
				if(!type.equals("gtt.eventmodel.web.HtmlSelect")) {
					iter.remove();
				}
			}
			
			else if(element.getTagName().equalsIgnoreCase("table")) {
				if(!type.equals("gtt.eventmodel.web.HtmlTable")) {
					iter.remove();
				}
			}
			
			else if(element.getTagName().equalsIgnoreCase("img")) {
				if(!type.equals("gtt.eventmodel.web.HtmlImg")) {
					iter.remove();
				}
			}
		}
	}
}
