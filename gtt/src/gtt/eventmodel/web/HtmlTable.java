package gtt.eventmodel.web;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HtmlTable extends HtmlBaseElement{
	
	public HtmlTable(WebElement elements) {
		super(elements);
	}
	
	List<WebElement> webelemenes = null;
	
	public int getTableColSize() {
		//先找到所有tr
		webelemenes = fundamentalElements.findElements(By.xpath("./thead/tr"));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tbody/tr")));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tfoot/tr")));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tr")));
		//對每個tr去找裡面的td
		int count = 0;
		int temp = 0;
		Iterator<WebElement> iter = webelemenes.iterator();
		
		while(iter.hasNext()) {
			temp = iter.next().findElements(By.xpath("./td")).size();
			//找出最多td的tr並把td數目記在count中
			if(temp > count)
				count = temp;
		}
		
		
		return count;
	}
	
	public int getTableRowSize() {
		webelemenes = fundamentalElements.findElements(By.xpath("./thead/tr"));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tbody/tr")));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tfoot/tr")));
		webelemenes.addAll(fundamentalElements.findElements(By.xpath("./tr")));
		return webelemenes.size();
	}
	
	public String getTableValue(int x, int y) {
		WebElement webElemene = null;
		try {
			webElemene = fundamentalElements.findElement(By.xpath("./tr[" + String.valueOf(x) + "]/td[" + String.valueOf(y) + "]"));
			return webElemene.getText();
		} catch (Exception e) {
			try {
				webElemene = fundamentalElements.findElement(By.xpath("./thead/tr[" + String.valueOf(x) + "]/td[" + String.valueOf(y) + "]"));
				return webElemene.getText();
			} catch (Exception e2) {
				try {
					webElemene = fundamentalElements.findElement(By.xpath("./tbody/tr[" + String.valueOf(x) + "]/td[" + String.valueOf(y) + "]"));
						return webElemene.getText();
				} catch (Exception e3) {
					return "";
				}
			}
		}
	}
	
}
