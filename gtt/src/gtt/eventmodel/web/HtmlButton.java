package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlButton extends HtmlBaseElement {
	
	public HtmlButton(WebElement elements) {
		super(elements);
	}

	public void click() {
		this.fundamentalElements.click();
	}
}
