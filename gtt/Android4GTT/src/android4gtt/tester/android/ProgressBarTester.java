package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.widget.ProgressBar;
import com.jayway.android.robotium.solo.Solo;

public class ProgressBarTester implements IComponentTester {

	private Solo m_solo = null;
	
	public ProgressBarTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof ProgressBar))
			return false;
		
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.SET_PROGRESS) {
			int progress = Integer.parseInt(info.getArguments().getValue("Progress"));
			m_solo.setProgressBar(((ProgressBar)comp), progress);
			return true;
		}
		
		return false;
	}
}
