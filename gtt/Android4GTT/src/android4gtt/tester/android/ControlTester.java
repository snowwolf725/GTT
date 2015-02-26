package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;

import com.jayway.android.robotium.solo.Solo;

public class ControlTester implements IComponentTester {

private Solo m_solo = null;
	
	public ControlTester(Solo solo) {
		m_solo = solo;
	}
	
	public boolean fireEvent(IEvent info, Object comp) {
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.GO_BACK) {
			m_solo.goBack();
			return true;
		}
		
		if (eid == AndroidTesterTag.GO_BACK_TO) {
			String name = info.getArguments().getValue("ActivityName");
			m_solo.goBackToActivity(name);
			return true;
		}
		
		if (eid == AndroidTesterTag.SEND_KEY) {
			String key = info.getArguments().getValue("Key");
			int keyCode = KeyConverter.StringToKeyCode(key);
			m_solo.sendKey(keyCode);
			return true;
		}
		
		if (eid == AndroidTesterTag.CLICK_ON_MENUITEM) {
			String text = info.getArguments().getValue("Text");
			m_solo.clickOnMenuItem(text);
			return true;
		}
		
		if (eid == AndroidTesterTag.CLICK_ON_SUBMENUITEM) {
			String text = info.getArguments().getValue("Text");
			m_solo.clickOnMenuItem(text, true);
			return true;
		}
		
		if (eid == AndroidTesterTag.PRESS_MENUITEM) {
			int index = Integer.parseInt(info.getArguments().getValue("Index"));
			m_solo.pressMenuItem(index);
			return true;
		}
		
		if (eid == AndroidTesterTag.WAIT_ACTIVITY) {
			String name = info.getArguments().getValue("ActivityName");
			return m_solo.waitForActivity(name, 20000);
		}
		
		return false;
	}
}
