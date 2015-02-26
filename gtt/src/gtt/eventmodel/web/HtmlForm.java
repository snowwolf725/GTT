package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;

public class HtmlForm extends HtmlBaseElement {
	
	public HtmlForm(WebElement element) {
		super(element);
	}

	public void submit() {
		this.fundamentalElements.submit();
	}
}
