package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlTextArea extends HtmlBaseElement{

	public HtmlTextArea(WebElement elements) {
		super(elements);
	}

	public void sendKeys(String text) {
		this.fundamentalElements.sendKeys(text);
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
