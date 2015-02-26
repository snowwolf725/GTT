package android4gtt.tester.android;

import java.util.ArrayList;
import java.util.Iterator;

import gtt.eventmodel.IEvent;
import gtt.tester.swing.IComponentTester;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

public class ListViewTester implements IComponentTester {

	private Solo m_solo = null;
	
	public ListViewTester(Solo solo) {
		m_solo = solo;
	}
	
	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof ListView))
			return false;
		
		ArrayList<ListView> listViews = m_solo.getCurrentListViews();
		Iterator<ListView> ite = listViews.iterator();
		int index = -1;
		while (ite.hasNext()) {
			index++;
			ListView listView = ite.next();
			if(listView.equals((ListView)comp))
				break;
		}
		
		if(index < 0)
			return false;
		
		int eid = info.getEventId();
		if (eid == AndroidTesterTag.CLICK_IN_LIST) {
			int line = Integer.parseInt(info.getArguments().getValue("Line"));
			m_solo.clickInList(line, index);
			return true;
		}
		if (eid == AndroidTesterTag.CLICK_LONG_IN_LIST) {
			int line = Integer.parseInt(info.getArguments().getValue("Line"));
			m_solo.clickLongInList(line, index);
			return true;
		}
		if (eid == AndroidTesterTag.SCROLL_DOWN_LIST) {
			m_solo.scrollDownList(index);
			return true;
		}
		if (eid == AndroidTesterTag.SCROLL_UP_LIST) {
			m_solo.scrollUpList(index);
			return true;
		}
		return false;
	}
}
