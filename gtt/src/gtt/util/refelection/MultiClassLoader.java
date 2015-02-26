/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
/**
 * 
 * 
 */
package gtt.util.refelection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

/**
 * A simple test class loader capable of loading from multiple sources, such as
 * local files or a URL. Must be subclassed and the abstract method
 * loadClassBytes() implemented to provide the preferred source.
 * 
 * This class is derived from an article by Chuck McManis
 * http://www.javaworld.com/javaworld/jw-10-1996/indepth.src.html with large
 * modifications.
 * 
 * @author Jack Harich - 8/18/97
 */
public abstract class MultiClassLoader extends java.net.URLClassLoader {

	// ---------- Fields --------------------------------------
	private Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();

	private char classNameReplacementChar;

	protected boolean monitorOn = false;

	protected boolean sourceMonitorOn = false;

	// ---------- Initialization ------------------------------
	public MultiClassLoader() {
		super(new URL[]{});
	}

	// ---------- Superclass Overrides ------------------------
	/**
	 * This is a simple version for external clients since they will always want
	 * the class resolved before it is returned to them.
	 */
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return (loadClass(className, true));
	}

	// ---------- Abstract Implementation ---------------------
	public synchronized Class<?> loadClass(String className, boolean resolveIt)
			throws ClassNotFoundException {

		Class<?> result;
		byte[] classBytes;
		monitor(">> MultiClassLoader.loadClass(" + className + ", " + resolveIt
				+ ")");

		// ----- Check our local cache of classes
		result = (Class<?>) classes.get(className);
		if (result != null) {
			monitor(">> returning cached result.");
			return result;
		}

		// ----- Check with the primordial class loader
		// if (resolveIt) resolveClass(result);

		try {
			result = super.findSystemClass(className);
			monitor(">> returning system class (in CLASSPATH).");
			return result;
		} catch (ClassNotFoundException e) {
			monitor(">> Not a system class.");
		}
		
		// ----- Try to load it from class path
		// if (resolveIt) resolveClass(result);

		try {
			result = super.findClass(className);
			monitor(">> returning class (in CLASSPATH).");
			return result;
		} catch (ClassNotFoundException e) {
			monitor(">> Not a class in classpath.");
		} catch (Exception e){
			System.out.println(e.getClass().getName());
		}

		// ----- Try to load it from preferred source
		// Note loadClassBytes() is an abstract method
		classBytes = loadClassBytes(className);
		if (classBytes == null) {
			throw new ClassNotFoundException();
		}

		// ----- Define it (parse the class file)
		result = defineClass(null, classBytes, 0, classBytes.length);
		if (result == null) {
			throw new ClassFormatError();
		}

		// ----- Resolve if necessary
		if (resolveIt)
			resolveClass(result);

		// Done
		classes.put(className, result);
		monitor(">> Returning newly loaded class.");
		return result;
	}

	// ---------- Public Methods ------------------------------
	/**
	 * This optional call allows a class name such as "COM.test.Hello" to be
	 * changed to "COM_test_Hello", which is useful for storing classes from
	 * different packages in the same retrival directory. In the above example
	 * the char would be '_'.
	 */
	public void setClassNameReplacementChar(char replacement) {
		classNameReplacementChar = replacement;
	}

	// ---------- Protected Methods ---------------------------
	protected abstract byte[] loadClassBytes(String className);

	protected String formatClassName(String className) {
		if (classNameReplacementChar == '\u0000') {
			// '/' is used to map the package to the path
			return className.replace('.', '/') + ".class";
		} else {
			// Replace '.' with custom char, such as '_'
			return className.replace('.', classNameReplacementChar) + ".class";
		}
	}

	protected void monitor(String text) {
		if (monitorOn)
			print(text);
	}

	// --- Std
	protected static void print(String text) {
		System.out.println(text);
	}

	/* add for adding url dynamically */
	public void addURL(URL arg0){
		super.addURL(arg0);
	}
	
	public void addClassPath(String url){
		try {
			addURL(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addClassPaths(String [] paths){
		if(paths == null)return;
		for(String p:paths){
			/* skip empty string */
			if( p==null || p.equals(""))
				continue;
			
			addClassPath(p);
		}
	}
} // End class
