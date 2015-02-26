package android4gtt;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gttlipse.EclipseProject;
import gttlipse.ITestPlatformInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jdt.core.JavaCore;

import android4gtt.eventmodel.AndroidComponent;
import android4gtt.eventmodel.AndroidModel;

public class AndroidPlatformInfo implements ITestPlatformInfo {
	private static IEventModel theModel = null;
	private static String path = "";
	public AndroidPlatformInfo() {
		
	}

	@Override
	public String getTestPlatformName() {
		return "Android";
	}

	@Override
	public int getTestPlatformID() {
		return 3;
	}

	@Override
	public String getDescriptionFilePath() {
		return "descriptor/android.xml";
	}

	@Override
	public String[] getJarFiles() {
		return new String[]{"Android4GTT.jar"};
	}

	@Override
	public IEventModel createEventModel() {
		path = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins/" + getPluginFolder() + "/" + getDescriptionFilePath();
		if (theModel == null) {
			theModel = new AndroidModel();
			theModel.initialize(path);
		}
		return theModel;
	}
	
	public static IEventModel getEventModel() {
		return theModel;
	}
	
	public static void setEventModel(IEventModel model) {
		theModel = model;
	}

	@Override
	public String getPluginFolder() {
		return "Android4GTT";
	}

	@Override
	public IComponent createDefaultComponent() {
		return AndroidComponent.createDefault();
	}

	@Override
	public void copyDescription() {
		String path = EclipseProject.getProjectLocation() + "/res/raw";
		new File(path).mkdirs();
		
		try {
			FileInputStream in = null;			
			in = new FileInputStream(EclipseProject.getProjectLocation() + "/" + getDescriptionFilePath());
			FileOutputStream out = null;			
			out = new FileOutputStream(path + "/android.xml");
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveFile() {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("adb remount");
			Process proc= rt.exec("adb push " + EclipseProject.getProjectFile() + " /sdcard/Android4GTT/GTTlipse.gtt");
			if(Android4GTTPlugin.waitFor(proc))
				System.out.println("Push GTTlipse.gtt to device.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
