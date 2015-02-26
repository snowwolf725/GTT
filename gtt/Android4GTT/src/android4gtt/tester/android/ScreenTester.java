package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;

import com.jayway.android.robotium.solo.Solo;

public class ScreenTester implements IComponentTester {

private Solo m_solo = null;
	
	public ScreenTester(Solo solo) {
		m_solo = solo;
	}
	
	public boolean fireEvent(IEvent info, Object comp) {
		int eid = info.getEventId();
		if (eid == AndroidTesterTag.CLICK_ON_SCREEN) {
			float x = Float.parseFloat(info.getArguments().getValue("X"));
			float y = Float.parseFloat(info.getArguments().getValue("Y"));
			m_solo.clickOnScreen(x, y);
			return true;
		}
		if (eid == AndroidTesterTag.CLICK_LONG_ON_SCREEN) {
			float x = Float.parseFloat(info.getArguments().getValue("X"));
			float y = Float.parseFloat(info.getArguments().getValue("Y"));
			m_solo.clickLongOnScreen(x, y);
			return true;
		}
		if (eid == AndroidTesterTag.CLICK_ON) {
			String text = info.getArguments().getValue("Text");
			if(m_solo.searchText(text)) {
				m_solo.clickOnText(text);
				return true;
			}
			return false;
		}
		if (eid == AndroidTesterTag.CLICK_LONG_ON) {
			String text = info.getArguments().getValue("Text");
			if(m_solo.searchText(text)) {
				m_solo.clickLongOnText(text);
				return true;
			}
			return false;
		}
		if (eid == AndroidTesterTag.CLICK_LONG_ON_AND_PRESS) {
			String text = info.getArguments().getValue("Text");
			int index = Integer.parseInt(info.getArguments().getValue("Index"));
			if(m_solo.searchText(text)) {
				m_solo.clickLongOnTextAndPress(text, index);
				return true;
			}
			return false;
		}
		if (eid == AndroidTesterTag.SCROLL_DOWN) {
			m_solo.scrollDown();
			return true;
		}
		if (eid == AndroidTesterTag.SCROLL_UP) {
			m_solo.scrollUp();
			return true;
		}
		if (eid == AndroidTesterTag.SCROLL_TO_SIDE) {
			int side = Integer.parseInt(info.getArguments().getValue("Side"));
			m_solo.scrollToSide(side);
			return true;
		}
		if (eid == AndroidTesterTag.DRAG) {
			float iniX = Float.parseFloat(info.getArguments().getValue("FromX"));
			float iniY = Float.parseFloat(info.getArguments().getValue("ToX"));
			float endX = Float.parseFloat(info.getArguments().getValue("FromY"));
			float endY = Float.parseFloat(info.getArguments().getValue("ToY"));
			int steps = Integer.parseInt(info.getArguments().getValue("StepCount"));
			m_solo.drag(iniX, endX, iniY, endY, steps);
			return true;
		}
		
		return false;
	}
}
