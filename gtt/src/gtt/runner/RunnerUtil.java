package gtt.runner;

import java.awt.Frame;
import java.awt.Window;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.TestOut;

import abbot.util.PathClassLoader;

public class RunnerUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Window pickMainWindow(String prefer_classtype) {
		// JWindow, JDialog, JApplet, etc...,
		Window[] win = Frame.getFrames();

		Vector wins = new Vector();
		Vector win_classnames = new Vector();
		for (int i = 0; i < win.length; i++) {
			if (win[i].getClass().getPackage() != null
					&& (win[i].getClass().getPackage().toString().indexOf(
							"editor.view") != -1 || win[i].getClass()
							.getPackage().toString().indexOf("gtt.mutant") != -1))
				continue;

			if (!win[i].isVisible())
				continue;

			wins.add(win[i]);
			win_classnames.add(win[i].getClass().toString());
		}

		if (wins.size() == 1) {
			return (Window) wins.elementAt(0);
		}

		if (prefer_classtype != null
				&& win_classnames.indexOf(prefer_classtype) != -1) {
			return (Window) wins.elementAt(win_classnames
					.indexOf(prefer_classtype));
		}

		if (wins.size() > 1) {
			Object name = JOptionPane
					.showInputDialog(
							null,
							"Please select the static member function to get application",
							"Error", JOptionPane.YES_OPTION, null,
							win_classnames.toArray(), win_classnames
									.elementAt(0));
			if (name == null)
				return null;

			return (Window) wins.elementAt(win_classnames.indexOf(name));
		}

		return null;
	}

	public static synchronized Class<?> loadClass(String name,
			String[] classpaths) {
		String classpath = null;
		try {
			classpath = classpaths.toString();
		} catch (NullPointerException npe) {
			classpath = System.getProperty("java.class.path");
		}

		return loadClass(name, classpath);
	}

	public static String getSystemClassPath() {
		return System.getProperty("java.class.path");
	}

	public static synchronized Class<?> loadClass(String cls, String classpath) {
		try {
			PathClassLoader loader = new PathClassLoader(classpath);
			return loader.loadClass(cls);
		} catch (ClassNotFoundException ce) {
			System.out.println(ce.toString() + ", fails when loading " + cls);
		}
		return null;
	}

	// @deprecated
	// private static synchronized Class<?> loadClass_2(String url,
	// String[] classpaths) {
	// try {
	// // �ϥ� Class Loader ��J App
	// // String[] classpaths = getClassPath();
	// FileClassLoader classloader = new FileClassLoader();
	// classloader.addClassPaths(classpaths);
	// Class<?> myclass = classloader.loadFile(url);
	// if (myclass == null || myclass.getName() == null
	// || myclass.getName().indexOf(".") == -1)
	// return myclass;
	// String classname = myclass.getName();
	// String classpath = classname.replaceAll("\\.", "\\\\");
	// String filepath = url.substring(0, url.lastIndexOf(classpath));
	// String newpath = filepath + classname + ".class";
	// classloader = new FileClassLoader();
	// classloader.addClassPaths(classpaths);
	// return classloader.loadFile(newpath);
	// } catch (ClassNotFoundException ce) {
	// System.err.print("Exception: " + ce.toString());
	// }
	// return null;
	// }

	public static final int JEMMY_TIME_OUT = 100;

	public static void initJemmyModule() {
		// JemmyProperties
		// .setCurrentDispatchingModel(JemmyProperties.ROBOT_MODEL_MASK);

		System.out.println("Jemmy v" + JemmyProperties.getMajorVersion() + "."
				+ JemmyProperties.getMinorVersion());

		JemmyProperties
				.setCurrentDispatchingModel(JemmyProperties.SHORTCUT_MODEL_MASK);
		JemmyProperties.getCurrentTimeouts().setTimeout(
				"Te-Timeout.st.WholeTestTimeout", JEMMY_TIME_OUT);
		// quiet mode
		JemmyProperties.setCurrentOutput(TestOut.getNullOutput());
	}

}
