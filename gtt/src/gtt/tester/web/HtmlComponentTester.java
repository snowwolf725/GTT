package gtt.tester.web;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.HtmlBaseElement;

public class HtmlComponentTester implements WebComponentTester{
	@Override
	public boolean fireEvent(IEvent info, HtmlBaseElement comp) {
		
		if (!(comp instanceof HtmlBaseElement))
			return false;
		
	return true;
	} 
}
