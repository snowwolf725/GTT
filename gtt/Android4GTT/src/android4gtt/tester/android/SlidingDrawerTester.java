package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.widget.SlidingDrawer;
import com.jayway.android.robotium.solo.Solo;

public class SlidingDrawerTester implements IComponentTester {

	private Solo m_solo = null;
	
	public SlidingDrawerTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof SlidingDrawer))
			return false;
		
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.OPEN) {
			m_solo.setSlidingDrawer(((SlidingDrawer)comp), Solo.OPENED);
			return true;
		}
		
		if (eid == AndroidTesterTag.CLOSE) {
			m_solo.setSlidingDrawer(((SlidingDrawer)comp), Solo.CLOSED);
			return true;
		}
		
		return false;
	}
}
