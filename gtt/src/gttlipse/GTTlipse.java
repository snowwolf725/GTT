package gttlipse;

import gtt.eventmodel.EventModelFactory;
import gttlipse.fit.view.GTTFitView;
import gttlipse.fit.view.GTTFitViewDefinition;
import gttlipse.macro.view.BadSmellListView;
import gttlipse.macro.view.MacroViewPart;
import gttlipse.scriptEditor.interpreter.TestResultListener;
import gttlipse.scriptEditor.views.GTTTestScriptView;
import gttlipse.vfsmCoverageAnalyser.view.CoverageView;
import gttlipse.web.loadtesting.view.LoadTestingResultView;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class GTTlipse extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "GTTlipse";
	
	// The extension point ID
	public static final String EP_TESTPLATFORMINFO_ID = "GTTlipse.TestPlatformInfo";
	
	public static final String EP_EVENTMODEL_ID = "GTTlipse.EventModel";
	
	public static final String EP_MACRO_NODE_EDITDIALOG_ID = "GTTlipse.Macro.NodeEditingDialog";
	
	public static final String EP_CONFIG_LOADER_ID = "GTTlipse.IO.ConfigFileLoader";
	
	public static final String EP_CONFIG_SAVER_ID = "GTTlipse.IO.ConfigFileSaver";
	
	public static final String EP_TEST_SCRIPT_SAVER_ID = "GTTlipse.IO.TestScriptFileSaver";
	
	public static final String EP_MACRO_SCRIPT_SAVER_ID = "GTTlipse.IO.MacroScriptFileSaver";
	
	public static final String EP_TEST_SCRIPT_LOADER_ID = "GTTlipse.IO.TestScriptFileLoader";
	
	public static final String EP_MACRO_SCRIPT_LOADER_ID = "GTTlipse.IO.MacroScriptFileLoader";
	
	public static final String EP_SCRIPT_ACTION_LAUNCHER_ID = "GTTlipse.ScriptEditor.Action.Launcher";
	
	public static final String EP_SCRIPT_ACTION_DEFAULT_EVENTNODE_ID = "GTTlipse.ScriptEditor.Action.DefaultEventNode";
	
	public static final String EP_SCRIPT_ACTION_DEFAULT_ASSERTNODE_ID = "GTTlipse.ScriptEditor.Action.DefaultAssertNode";
	
	// The shared instance.
	private static GTTlipse plugin;
	
	// The platform info
	private static ITestPlatformInfo m_info;

	// Test Script Define
	public static final int FolderNode = 11;
	public static final int EventNode = 12;
	public static final int AssertNode = 13;
	public static final int MacroNode = 14;

	private static TestResultListener listener;

	/**
	 * The constructor.
	 */
	public GTTlipse() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		listener = new TestResultListener();
		JUnitCore.addTestRunListener(listener);

		String path = JavaCore.getClasspathVariable("ECLIPSE_HOME")
				+ "/plugins/GTTlipse_1.0.0/";

		EventModelFactory.setPluginPath(path);
		JavaCore.setClasspathVariable("GTT_HOME", new Path(path), null);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		// SaveGTTFile runner = new SaveGTTFile();
		// runner.run(null);
		JUnitCore.removeTestRunListener(listener);
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static GTTlipse getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static GTTTestScriptView showScriptView() {
		return (GTTTestScriptView) showView("GTTlipse.views.GTTTestScriptView");
		// return findScriptView();
	}

	public static GTTTestScriptView findScriptView() {
		return (GTTTestScriptView) findView("GTTlipse.views.GTTTestScriptView");
	}

	public static MacroViewPart findMacroView() {
		return (MacroViewPart) findView("GTTlipse.views.macroScriptView");
	}
	
	public static CoverageView findGTTCoverageView() {
		return (CoverageView) showView("GTTlipse.views.GTTCoverageView");
	}
	
	public static BadSmellListView findBadSmellListView() {
		return (BadSmellListView) findView("GTTlipse.views.badSmellListView");
	}
	
	public static BadSmellListView showBadSmellListView() {
		return (BadSmellListView) showView("GTTlipse.views.badSmellListView");
	}
	
	// add Load Testing result view by loveshoo
	// @2011-08-14 
	public static LoadTestingResultView findLoadTestingResultView() {
		return (LoadTestingResultView) findView("GTTlipse.views.loadTestingResultView");
	}
	
	public static LoadTestingResultView showLoadTestingResultView() {
		return (LoadTestingResultView) showView("GTTlipse.views.loadTestingResultView");
	}
	
	public static LoadTestingResultView getLoadTestingResultView() {
		if (findView("GTTlipse.views.loadTestingResultView") == null) {
			showView("GTTlipse.views.loadTestingResultView");
		}

		return (LoadTestingResultView) findView("GTTlipse.views.loadTestingResultView");
	}

	public static IViewPart findView(String viewid) {
		try {
			return plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IViewPart showView(String viewid) {
		try {
			return plugin.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().showView(viewid);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GTTFitView getFitView() {
		if (findView(GTTFitViewDefinition.FitViewId) == null) {
			showView(GTTFitViewDefinition.FitViewId);
		}

		return (GTTFitView) findView(GTTFitViewDefinition.FitViewId);
	}
	
	public static void makeScriptViewDirty() {
		showScriptView().setDirty(true);
	}

	public static ITestPlatformInfo getPlatformInfo() {
		return m_info;
	}

	public static void setPlatformInfo(ITestPlatformInfo info) {
		m_info = info;
	}
	
	public static void loadPlatformInfo() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
		.getConfigurationElementsFor(gttlipse.GTTlipse.EP_TESTPLATFORMINFO_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof ITestPlatformInfo) {
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							// Handle exception in client
						}
		
						@Override
						public void run() throws Exception {
							int platformID = ((ITestPlatformInfo) o).getTestPlatformID();
							if(platformID == GTTlipseConfig.getInstance().getPlatformOfTesting())
								setPlatformInfo((ITestPlatformInfo) o);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
