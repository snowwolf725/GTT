package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.widget.TimePicker;

import com.jayway.android.robotium.solo.Solo;

public class TimePickerTester implements IComponentTester {

	private Solo m_solo = null;
	
	public TimePickerTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof TimePicker))
			return false;
		
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.SET_TIMEPICKER) {
			int hour = Integer.parseInt(info.getArguments().getValue("Hour"));
			int minute = Integer.parseInt(info.getArguments().getValue("Minute"));
			m_solo.setTimePicker(((TimePicker)comp), hour, minute);
			return true;
		}
		
		return false;
	}
}