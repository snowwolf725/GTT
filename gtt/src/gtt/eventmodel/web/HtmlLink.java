package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlLink extends HtmlBaseElement {
	
	public HtmlLink(WebElement elements) {
		super(elements);
	}

	public void click() {
		this.fundamentalElements.click();
	}
}
