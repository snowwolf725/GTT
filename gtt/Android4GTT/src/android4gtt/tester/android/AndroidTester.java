package android4gtt.tester.android;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.logger.Logger;
import gtt.tester.swing.IComponentTester;
import gtt.tester.swing.ITester;
import gtt.testscript.EventNode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.R;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android4gtt.component.ControlComponent;
import android4gtt.component.ScreenComponent;

import com.jayway.android.robotium.solo.Solo;

public class AndroidTester implements ITester {
	
	private ViewTester defaultTester = null;
	private Solo m_solo = null;
	private Object m_res = null;
	
	public AndroidTester(Solo solo, Object res) {
		m_solo = solo;
		m_res = res;
		defaultTester = new ViewTester(solo);
		registrTester(solo);
	}

	private HashMap<Class<?>, IComponentTester> m_Tester = new HashMap<Class<?>, IComponentTester>();

	public void addTester(Class<?> cls, IComponentTester tester) {
		m_Tester.put(cls, tester);
	}

	public IComponentTester mappedTester(Object obj) {
		// 先找出Class可以直接對應的Tester
		IComponentTester tester = m_Tester.get(obj.getClass());
		if (tester != null) {
			return tester;
		}

		Set<Class<?>> keys = m_Tester.keySet();
		Iterator<Class<?>> ite = keys.iterator();
		while (ite.hasNext()) {
			// 否則，則找出sub-class可對應的Tester
			Class<?> cls = ite.next();
			if (cls.isAssignableFrom(obj.getClass())) {
				return m_Tester.get(cls);
			}
		}
		// 預設也會有一個 tester
		return defaultTester;
	}

	private void registrTester(Solo solo) {
		// 註冊各個class 的tester
		//addTester(View.class, new ViewTester(solo));
		addTester(Button.class, new ViewTester(solo));
		addTester(ToggleButton.class, new ViewTester(solo));
		addTester(RadioButton.class, new ViewTester(solo));
		addTester(CheckBox.class, new ViewTester(solo));
		addTester(TextView.class, new ViewTester(solo));
		addTester(CheckedTextView.class, new ViewTester(solo));
		addTester(ImageView.class, new ViewTester(solo));
		addTester(ImageButton.class, new ViewTester(solo));
		addTester(CheckBox.class, new ViewTester(solo));
		addTester(EditText.class, new EditTextTester(solo));
		addTester(MultiAutoCompleteTextView.class, new EditTextTester(solo));
		addTester(ListView.class, new ListViewTester(solo));
		addTester(Spinner.class, new SpinnerTester(solo));
		addTester(DatePicker.class, new DatePickerTester(solo));
		addTester(TimePicker.class, new TimePickerTester(solo));
		addTester(ProgressBar.class, new ProgressBarTester(solo));
		addTester(RatingBar.class, new ProgressBarTester(solo));
		addTester(SeekBar.class, new ProgressBarTester(solo));
		addTester(SlidingDrawer.class, new SlidingDrawerTester(solo));
		addTester(ControlComponent.class, new ControlTester(solo));
		addTester(ScreenComponent.class, new ScreenTester(solo));
	}

	private void logging(String msg) {
		// 應該使用 logging 機制，會比較乾淨
		// zws 2007/01/04
		String prefix = "AndroidTester";
		Logger.getSimpleLogger().log(prefix + "-" + msg);
	}

	private Object findComponent(IComponent info) {
		int id = 0;
		View view = null;
		
		if (info.getType().equals("android4gtt.component.ControlComponent"))
			return new ControlComponent(info, m_solo);
		
		if (info.getType().equals("android4gtt.component.ScreenComponent"))
			return new ScreenComponent(info, m_solo);
		
		try{
			@SuppressWarnings("unchecked")
			Field field = ((Class<R.id>) m_res).getField(info.getName());
			id = field.getInt(new R.id());
		} catch (Exception e) {
			logging("Component name isn't integer");
		}
		
		Activity act = m_solo.getCurrentActivity();
		view = act.findViewById(id);
		
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + 20000;
		while (System.currentTimeMillis() <= endTime && view == null) {
			try {
				Thread.sleep(m_GlobalSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (info.getType().equals("android.widget.ListView")) {
				view = m_solo.getCurrentListViews().get(0);
			}
			else {
				act = m_solo.getCurrentActivity();
				view = act.findViewById(id);
			}
		}
		
		if (view == null) {
			logging("Could not find \"" + info + "\".");
			return null;
		}
		if (!(view instanceof View)) { 
			logging("\"" + info + "\" isn't an Android View");
			return null;
		}
		return view; 
	}

	public synchronized boolean fire(EventNode node) {
		Object component = findComponent(node.getComponent());
		if (component == null) 
			return false;

		try {
			dispatchFireEvent(node.getEvent(), component);
			if (m_GlobalSleepTime > 0)
				Thread.sleep(m_GlobalSleepTime);
		} catch (Exception exp) {
			logging("[error] " + exp.toString());
			return false; // 這個事件無法成功發送
		}
		// 至此一切正常
		return true;
	}

	protected void dispatchFireEvent(IEvent event, Object comp) {
		// 找出 Component對應的 Tester
		IComponentTester tester = mappedTester(comp);

		try {
			boolean r = tester.fireEvent(event, comp);

			// 發成功的事件，要考慮sleep time
			if (r == true) {
				//sleep(event);
			}
		} catch (NullPointerException nep) {
			nep.printStackTrace();
		}
	}

	private void sleep(IEvent info) {
//		if (info.getArguments().getValue("SleepTime") == null)
//			return;
//		String sleepTime = info.getArguments().getValue("SleepTime");
		try {
			//Integer.parseInt(sleepTime);
			Thread.sleep(m_GlobalSleepTime);
		} catch (Exception e) {
			// nothing to do
		}
	}

	private long m_GlobalSleepTime = DEFAULT_SLEEP_TIME;

	public void setSleepTime(long st) {
		if (st < 0)
			st = 0;
		m_GlobalSleepTime = st;
	}
}

