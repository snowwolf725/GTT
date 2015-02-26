package android4gtt.tester.android;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;

import java.util.ArrayList;
import java.util.Iterator;

import android.widget.Spinner;

import com.jayway.android.robotium.solo.Solo;

public class SpinnerTester implements IComponentTester {

	private Solo m_solo = null;
	
	public SpinnerTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof Spinner))
			return false;
		
		ArrayList<Spinner> spinners = m_solo.getCurrentSpinners();
		Iterator<Spinner> ite = spinners.iterator();
		int index = -1;
		while (ite.hasNext()) {
			index++;
			Spinner spinner = ite.next();
			if(spinner.equals((Spinner)comp))
				break;
		}
		
		if(index < 0)
			return false;
		
		int eid = info.getEventId();
		if (eid == AndroidTesterTag.CLICK_IN_SPINNER) {
			int item = Integer.parseInt(info.getArguments().getValue("ItemID"));
			m_solo.pressSpinnerItem(index, item);
			return true;
		}
		return false;
	}
}
