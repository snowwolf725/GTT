package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlElement extends HtmlBaseElement {
	
	public HtmlElement(WebElement elements) {
		super(elements);
	}
	
	public void click() {
		this.fundamentalElements.click();
	}
	
	public void setSelected() {
		this.fundamentalElements.setSelected();
	}
}