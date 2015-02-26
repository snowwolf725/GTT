package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.widget.DatePicker;
import com.jayway.android.robotium.solo.Solo;

public class DatePickerTester implements IComponentTester {

	private Solo m_solo = null;
	
	public DatePickerTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof DatePicker))
			return false;
		
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.SET_DATEPICKER) {
			int year = Integer.parseInt(info.getArguments().getValue("Year"));
			int month = Integer.parseInt(info.getArguments().getValue("Month"));
			int day = Integer.parseInt(info.getArguments().getValue("Day"));
			m_solo.setDatePicker(((DatePicker)comp), year, month-1, day);
			return true;
		}
		
		return false;
	}
}
