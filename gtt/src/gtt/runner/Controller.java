package gtt.runner;

import gtt.editor.configuration.IConfiguration;
import gtt.recorder.CaptureData;
import gtt.recorder.EventAbstracter;
import gtt.recorder.Recorder;
import gtt.recorder.SwingRecorder;
import gtt.testscript.AbstractNode;
import gtt.util.refelection.ReflectionUtil;

import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

public class Controller {
	private Object m_theAUTApp = null;

	private Window m_theAUTWindow = null; // 通常是 theAUTWindow

	private String m_AUT_ClassType = null;

	private Recorder _recorder = null;

	private Method _invokeMethod = null;

	public Controller() {
	}

	public void startRecord() {
		if (m_theAUTWindow == null)
			return;

		_recorder = new SwingRecorder();
		// 開始錄影
		_recorder.active();
	}

	public List<AbstractNode> stopRecordAndGetResult(int level) {
		if (m_theAUTWindow == null || _recorder == null)
			return null;

		_recorder.inactive();

		List<CaptureData> captured_data = _recorder.getCaptureData();
		cleanRecorder();
		if (captured_data.size() > 0) {
			// 將AWTEvent抽像化成EventNode
			return new EventAbstracter().abstraction(captured_data);
		}
		// empty result
		return new LinkedList<AbstractNode>();
	}

	/*
	 * stop the interception action reload system eventqueue.
	 */
	private void cleanRecorder() {
		if (m_theAUTWindow == null)
			return;
		if (_recorder == null)
			return;
		_recorder.inactive();
		_recorder = null;
	}

	private String[] formArgument(String arg) {
		String[] emptyArgu = {};
		if (arg == null || arg.length() == 0)
			return emptyArgu;

		return new String[] { arg };
	}

	public Window getAppMainWindow() {
		return m_theAUTWindow;
	}

	private void appearAUTWindow() {
		if (m_theAUTApp == null)
			return;
		if (!(m_theAUTApp instanceof Window))
			return;

		m_theAUTWindow = (Window) m_theAUTApp;
		m_theAUTWindow.setLocation(0, 0);
		m_theAUTWindow.setVisible(true);
		m_theAUTWindow.toFront();
	}

	/**
	 * invokde the AUT, which loaded by ClassLoader
	 */
	private synchronized void invokeAUT(Class<?> theAUTCls, String invokeArgs) {
		if (_invokeMethod == null)
			return;

		ReflectionUtil.invokeMain(_invokeMethod, formArgument(invokeArgs));
	}

	private Method getMainMethod(Class<?> theAUTCls) {
		if (_invokeMethod != null
				&& _invokeMethod.getName().indexOf("main") != -1)
			return _invokeMethod; // 已經有invoke main

		// 從class身上挑 main method 來啟動
		return ReflectionUtil.getMainMethod(theAUTCls);
	}

	final static String DEFAULT_CLASS_PATHS = ".";

	public void loadAUT(IConfiguration config) {
		loadAUTbyMain(config.getAUTFilePath(), config.getAUTArgument(), config
				.getClassPath());
	}

	public void loadAUTbyMain(LaunchData launchData) {
		loadAUTbyMain(launchData.getClassName(), launchData.getArgument(),
				launchData.getClasspath());
	}

	public void loadAUTbyMain(String clsName, String invokeArgument,
			String classpaths) {
		if (clsName == null)
			return;

		reset();

		Class<?> theAutClass = RunnerUtil.loadClass(clsName, classpaths);
		if (theAutClass == null)
			return; // failed to load aut class
		// 設定invoke main method
		_invokeMethod = getMainMethod(theAutClass);
		/**
		 *沒有main method 可以啟動AUT，就不再做後續處理； 直接顯示錯誤訊息 - zwshen 2010/01/13
		 */
		if (_invokeMethod == null)
			return;
		System.out.println("invoke AUT");
		invokeAUT(theAutClass, invokeArgument);
		setAppMainWindow(RunnerUtil.pickMainWindow(m_AUT_ClassType));
		appearAUTWindow();

		/* handing for multi-thread application and heavy application */
		if (m_theAUTApp == null)
			waitWindow();

		if (m_theAUTApp == null) {
			System.out.println("failed to loading " + theAutClass.toString());
			return;
		}

		if (Window.class.isAssignableFrom(theAutClass)) {
			setAUTWindow(theAutClass);
			appearAUTWindow();
		}

		// if (m_theAUTWindow == null) {
		// List<Method> invoked_methods = ReflectionUtil
		// .getInvokedMethods(theAutClass);
		// if (invoked_methods.size() == 0) {
		// JOptionPane.showMessageDialog(null,
		// "Please provide static methods to invoke AUT.\n",
		// "GTT", JOptionPane.YES_OPTION);
		// return;
		// }
		//
		// _invokeMethod = (Method) JOptionPane.showInputDialog(null,
		// "Select one to invoke AUT", "GTT",
		// JOptionPane.YES_OPTION, null,
		// invoked_methods.toArray(), invoked_methods.get(0));
		//
		// if (_invokeMethod != null) {
		// invokeAUT(theAutClass, invokeArgument);
		// }
		// }

		if (m_theAUTWindow != null)
			m_AUT_ClassType = m_theAUTWindow.getClass().toString();
	}

	public void setAUT(Window win) {
		reset();

		setAppMainWindow(win);
		appearAUTWindow();

		/* handing for multi-thread application and heavy application */
		if (m_theAUTApp == null)
			waitWindow();

		if (m_theAUTApp == null) {
			System.out.println("failed to loading " + win.toString());
			return;
		}

		if (Window.class.isAssignableFrom(win.getClass())) {
			setAUTWindow(win.getClass());
			appearAUTWindow();
		}

		if (m_theAUTWindow != null)
			m_AUT_ClassType = m_theAUTWindow.getClass().toString();
	}

	private void waitWindow() {
		try {
			// sleep 一段時間，再來重新找aut
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println(e.toString());
		}

		for (Frame frame : JFrame.getFrames()) {
			if (frame.isVisible() == true) {
				setAUTWindow(frame);
				appearAUTWindow();
				break;
			}
		}
	}

	public void reset() {
		terminate();
		m_theAUTApp = null;
		m_theAUTWindow = null;
		m_AUT_ClassType = null;
		_invokeMethod = null;
	}

	// private void selectAUTApp(Class<?> theAutClass)
	// throws IllegalArgumentException, IllegalAccessException,
	// InvocationTargetException {
	//
	// List<Method> instance_methods = ReflectionUtil
	// .getInstanceMethod(theAutClass);
	//
	// if (instance_methods.size() == 0) {
	// JOptionPane
	// .showMessageDialog(
	// null,
	// "Please provide at least one Static Method to create AUT instance.\n",
	// "Error", JOptionPane.OK_OPTION);
	// return;
	// }
	//
	// Method m = (Method) JOptionPane.showInputDialog(null,
	// "Select one to create AUT", "Error", JOptionPane.YES_OPTION,
	// null, instance_methods.toArray(), instance_methods.get(0));
	// if (m != null) {
	// m_theAUTApp = m.invoke(null, new Object[] {});
	// }
	//
	// }

	private void setAUTWindow(Object aut) {
		m_theAUTApp = aut;
	}

	private void setAppMainWindow(Window win) {
		m_theAUTApp = win;
		m_theAUTWindow = win;
	}

	public void showAppWindow() {
		if (m_theAUTWindow == null)
			return;

		m_theAUTWindow.setVisible(true);
	}

	public void terminate() {
		if (m_theAUTWindow == null)
			return;

		m_theAUTWindow.dispose();
		m_theAUTWindow = null;
	}

}
