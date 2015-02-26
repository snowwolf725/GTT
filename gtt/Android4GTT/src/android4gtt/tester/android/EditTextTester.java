package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

public class EditTextTester extends ViewTester {

	private Solo m_solo = null;
	
	public EditTextTester(Solo solo) {
		super(solo);
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if(super.fireEvent(info, comp) == true)
			return true;

		if (!(comp instanceof EditText))
			return false;
		
		int eid = info.getEventId();
		
		if (eid == AndroidTesterTag.CLEAR_TEXT) {
			m_solo.clearEditText((EditText)comp);
			return true;
		}
		
		if (eid == AndroidTesterTag.ENTER_TEXT) {
			String text = info.getArguments().getValue("Text");
			m_solo.enterText((EditText)comp, text);
			return true;
		}
		
		return false;
	}

}
