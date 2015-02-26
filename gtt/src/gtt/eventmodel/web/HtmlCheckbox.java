package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlCheckbox extends HtmlBaseElement {
	
	public HtmlCheckbox(WebElement elements) {
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
