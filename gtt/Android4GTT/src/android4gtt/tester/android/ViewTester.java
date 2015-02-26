package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

public class ViewTester implements IComponentTester {

	private Solo m_solo = null;
	
	public ViewTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof View))
			return false;
		
		int eid = info.getEventId();
		if (eid == AndroidTesterTag.CLICK) {
			m_solo.clickOnView((View)comp);
			return true;
		}
		
		if (eid == AndroidTesterTag.CLICK_LONG) {
			m_solo.clickLongOnView((View)comp);
			return true;
		}
		
		return false;
	}
}
