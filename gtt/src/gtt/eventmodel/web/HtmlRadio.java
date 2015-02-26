package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlRadio extends HtmlBaseElement {
	
	public HtmlRadio(WebElement elements) {
		super(elements);
	}
	
	public void click() {
		this.fundamentalElements.click();
	}
	
	public void setSelected() {
		this.fundamentalElements.setSelected();
	}
	
	public void clear() {
		this.fundamentalElements.clear();
	}
}
