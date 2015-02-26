package gtt.eventmodel.web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HtmlSelect extends HtmlBaseElement {

	Select selectElement = null;
	
	public HtmlSelect(WebElement elements) {
		super(elements);
		
		try {
			selectElement = new Select(fundamentalElements);
		} catch (Exception e) {
			System.out.println("Component type is not select!");
			e.printStackTrace();
		}
	}


	public void deselectAll() {
		selectElement.deselectAll();
	}

	public void deselectByIndex(int index) {
		selectElement.deselectByIndex(index);
	}

	public void deselectByValue(String value) {
		selectElement.deselectByValue(value);
	}

	public void deselectByVisibleText(String text) {
		selectElement.deselectByVisibleText(text);
	}

	public void selectByIndex(int index) {
		selectElement.selectByIndex(index);
	}

	public void selectByValue(String value) {
		selectElement.selectByValue(value);
	}

	public void selectByVisibleText(String text) {
		selectElement.selectByVisibleText(text);
	}
}
