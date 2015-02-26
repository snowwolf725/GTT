package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlImg extends HtmlBaseElement {
	
	public HtmlImg(WebElement elements) {
		super(elements);
	}

	public void click() {
		this.fundamentalElements.click();
	}
}
