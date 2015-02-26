package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlBaseElement {
	
	protected WebElement fundamentalElements = null;
	
	public HtmlBaseElement(WebElement element) {
		this.fundamentalElements = element;
	}
	
//	public void setFundamentalElements(WebElement fundamentalElements) {
//		this.fundamentalElements = fundamentalElements;
//	}

	public String getAttribute(String attribute) {
		return fundamentalElements.getAttribute(attribute);
	}
	
	public String getTagName() {
		return fundamentalElements.getTagName();
	}
	
	public String getText() {
		return fundamentalElements.getText();
	}
	
	public String getValue() {
		return fundamentalElements.getValue();
	}
	
	public boolean isEnabled() {
		return fundamentalElements.isEnabled();
	}
	
	public boolean isSelected() {
		return fundamentalElements.isSelected();
	}
	
	public boolean toggle() {
		return fundamentalElements.toggle();
	}
		
}
