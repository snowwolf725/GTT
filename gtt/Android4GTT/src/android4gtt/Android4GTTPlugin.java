package android4gtt;

import gttlipse.EclipseProject;
import gttlipse.GTTlipse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Android4GTTPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "Android4GTT";

	// The shared instance
	private static Android4GTTPlugin plugin;
	
	/**
	 * The constructor
	 */
	public Android4GTTPlugin() {
		JUnitCore.addTestRunListener(new TestRunListener() {
			public void testCaseFinished(ITestCaseElement e) {
				GTTlipse.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						try {
							Runtime rt = Runtime.getRuntime();
							Process proc = rt.exec("adb pull /mnt/sdcard/Android4GTT/MacroGUITestResult.txt " + EclipseProject.getProjectLocation() + "/MacroGUITestResult.txt");				            
							if(waitFor(proc))
								proc = rt.exec("adb pull /mnt/sdcard/Android4GTT/Test_Result.txt " + EclipseProject.getProjectLocation() + "/GUITestResult.txt");
							if(waitFor(proc))
								GTTlipse.findMacroView().getPresenter().doAfterTerminate(GTTlipse.findMacroView().getFailnodes(), GTTlipse.findMacroView().getTabularPresenter());
						        GTTlipse.findScriptView().getReportHandler().windowActivated(null);
						        System.out.println("Show Result");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				});
			}
		});
	}
	
	public static boolean waitFor(Process proc) {
		String line = null;
		InputStream stderr = proc.getErrorStream ();
        InputStreamReader esr = new InputStreamReader (stderr);
        BufferedReader ebr = new BufferedReader (esr);
        InputStream stdout = proc.getInputStream ();
        InputStreamReader osr = new InputStreamReader (stdout);
        BufferedReader obr = new BufferedReader (osr);
        
        try {
        	System.out.println ("<error>");
        	while ( (line = ebr.readLine ()) != null)
			    System.out.println(line);
            System.out.println ("</error>");
            
            System.out.println ("<output>");
            while ( (line = obr.readLine ()) != null)
                System.out.println(line);
            System.out.println ("</output>");
            int exitVal = proc.waitFor();
            if(exitVal == 0)
    			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		return false;
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Android4GTTPlugin getDefault() {
		return plugin;
	}

}
