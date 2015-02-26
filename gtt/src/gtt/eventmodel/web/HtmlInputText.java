package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlInputText extends HtmlBaseElement {
	
	public HtmlInputText(WebElement elements) {
		super(elements);
	}

	public void sendKeys(String text) {
		this.fundamentalElements.sendKeys(text);
	}
	
	public void clear() {
		this.fundamentalElements.clear();
	}
}
