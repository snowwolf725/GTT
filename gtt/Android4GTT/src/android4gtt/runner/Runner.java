package android4gtt.runner;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Instrumentation;

import dalvik.system.PathClassLoader;

public class Runner {
	private static Class<?> Solo;
	private static Class<?> Interpreter;
	private static Object solo = null;
	private static Object runner = null;
	
	public Runner(Object obj, InputStream desc_file) {
		File jarFile = new File("/data/app/android4gtt.setup-1.apk");   
		if ( jarFile.exists() ) {  
		PathClassLoader cl = new  PathClassLoader("/data/app/android4gtt.setup-1.apk", "/data/app/" , ClassLoader.getSystemClassLoader());    
			try {
				Solo = cl.loadClass("com.jayway.android.robotium.solo.Solo");
				Interpreter = cl.loadClass("android4gtt.scriptEditor.interpreter.Interpreter");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(Interpreter == null)
			System.out.println("Error");
		try {
			Constructor<?> con = Interpreter.getConstructor(Object.class, InputStream.class);
			runner = con.newInstance(obj, desc_file);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSolo(Instrumentation ins, Activity act) {
		try {
			Constructor<?> con = Solo.getConstructor(Instrumentation.class, Activity.class);
			solo = con.newInstance(ins, act);
			Method m = Interpreter.getMethod("setSolo", Solo);
			m.invoke(runner, solo);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
	public void setRes(Object res) {
		try {
			Method m = Interpreter.getMethod("setRes", Object.class);
			m.invoke(runner, res);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void GTTTestScript(String methodname, String scriptname) {
		try {
			Method m = Interpreter.getMethod("GTTTestScript", String.class, String.class);
			String msg = (String)m.invoke(runner, methodname, scriptname);
			if(msg != "")
				android.test.ActivityInstrumentationTestCase2.fail("[gttlipse] "+msg);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public void finalize() {
		try {
			Method m = Interpreter.getMethod("finalize");
			m.invoke(runner);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

