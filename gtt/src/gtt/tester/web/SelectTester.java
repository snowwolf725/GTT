package gtt.tester.web;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.HtmlBaseElement;
import gtt.eventmodel.web.HtmlSelect;

public class SelectTester extends HtmlComponentTester {
	@Override
	public boolean fireEvent(IEvent info, HtmlBaseElement com) {
		
		if (!(com instanceof HtmlSelect))
			return false;
		
		HtmlSelect select = (HtmlSelect)com;
		
		int eid = info.getEventId();
		
		if (eid == WebTesterTag.DESELECT_All) {
			select.deselectAll();
			return true;
		}
		
		if (eid == WebTesterTag.DESELECT_BY_INDEX) {
			int x = Integer.parseInt(info.getArguments().getValue("Index"));
			try {
			select.deselectByIndex(x);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		if (eid == WebTesterTag.DESELECT_BY_VALUE) {
			String text = info.getArguments().getValue("Value");
			try {
			select.deselectByValue(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		if (eid == WebTesterTag.DESELECT_BY_VISIBLE_TEXT) {
			String text = info.getArguments().getValue("VisibleValue");
			try {
			select.deselectByVisibleText(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		if (eid == WebTesterTag.SELECT_BY_INDEX) {
			int x = Integer.parseInt(info.getArguments().getValue("Index"));
			try {
			select.selectByIndex(x);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		if (eid == WebTesterTag.SELECT_BY_VALUE) {
			String text = info.getArguments().getValue("Value");
			try {
			select.selectByValue(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		if (eid == WebTesterTag.SELECT_BY_VISIBLE_TEXT) {
			String text = info.getArguments().getValue("VisibleValue");
			try {
			select.selectByVisibleText(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	} 
}
