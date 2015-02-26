package gtt.tester.web;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.HtmlBaseElement;

public interface WebComponentTester {
	public boolean fireEvent(IEvent info, HtmlBaseElement comp);

}


