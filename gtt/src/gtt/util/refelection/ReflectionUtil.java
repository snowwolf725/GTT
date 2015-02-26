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
package gtt.util.refelection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ReflectionUtil 提供與 Reflection 的services 由 Tools.java 移值過來
 * 
 * @date 20051123
 * @author zwshen
 * 
 */
public class ReflectionUtil {
	// 2004 art
	public static List<Method> getInstanceMethod(Class<?> c) {
		List<Method> results = getMethodsStartsWith(c, "");
		for (int i = 0; i < results.size(); i++) {
			String n = results.get(i).toString();
			if (n.indexOf("static") != -1 && n.indexOf("public") != -1)
				continue; // 略過 public
			results.remove(i);
			i--;
		}

		// 最後，只留下 static 且回傳值不是void 的
		// static T instance()
		return results;
	}

	// 2004 art
	public static List<Method> getInvokedMethods(Class<?> c) {
		List<Method> methods = getMethodsStartsWith(c, "");
		for (int i = methods.size() - 1; i >= 0; i--) {
			if (methods.get(i).toString().indexOf("static") == -1
					|| methods.get(i).toString().indexOf("get") != -1)
				methods.remove(i); // non-static 或 get 開頭
		}

		return methods;
	}

	@SuppressWarnings({ "rawtypes" })
	public static List<Method> getMethodsStartsWith(Class c, String startsWith) {

		List<Method> result = new LinkedList<Method>();
		if (c == null)
			return result;

		Method[] methods = c.getMethods();

		if (startsWith == "others") {
			for (int i = 0; i < methods.length; i++) {
				if (!(methods[i].getName().startsWith("get") || methods[i]
						.getName().startsWith("is"))) {
					if (isSimpleTypes(methods[i].getParameterTypes()))
						result.add(methods[i]);
				}
			}
		} else if (startsWith == "get" || startsWith == "is") {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith(startsWith)) {
					if (isSimpleTypes(methods[i].getParameterTypes()))
						result.add(methods[i]);
				}
			}
		} else {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith(startsWith)) {
					result.add(methods[i]);
				}
			}
		}

		// sorting
		Collections.sort(result, new MethodComparator());
		return result;
	}

	public static Method getMainMethod(Class<?> c) {
		List<Method> list = getMethodsStartsWith(c, "main");
		if (list.size() < 1)
			return null;
		return list.get(0);
	}

	public static Method getMethod(Class<?> c, String method) {
		Method[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getName().equals(method)
					|| methods[i].toString().equals(method))
				return methods[i];

		return null; // not-found
	}

	@SuppressWarnings("rawtypes")
	public static List<Method> getMethodsStartsWithAndNoParam(Class c,
			String prefix) {
		if (c == null)
			return null;

		Method[] methods = c.getMethods();
		List<Method> result = new LinkedList<Method>();

		for (int i = 0; i < methods.length; i++) {
			// 函式開頭不符合 prefix
			if (!methods[i].getName().startsWith(prefix))
				continue;

			// 有參數的，也不符合
			if (methods[i].getParameterTypes().length != 0)
				continue;

			result.add(methods[i]);
		}

		return result;
	}

	public static List<Method> getMethodsReturnWith(Class<?> c,
			Class<?>[] returnType) {
		if (c == null)
			return null;

		Method[] methods = c.getMethods();
		List<Method> result = new LinkedList<Method>();

		for (int j = 0; j < returnType.length; j++) {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getReturnType().equals(returnType[j]))
					result.add(methods[i]);
			}
		}

		// sorting
		Collections.sort(result, new MethodComparator());
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<Method> getMethodsBooleanReturnValue(Class c) {
		if (c == null)
			return null;

		Method[] methods = c.getMethods();
		List<Method> result = new LinkedList<Method>();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getReturnType().equals(boolean.class))
				result.add(methods[i]);
		}

		// sorting
		Collections.sort(result, new MethodComparator());
		return result;
	}

	public static String getStringOfMethod(Method method) {
		StringBuffer sb = new StringBuffer();
		sb.append(getMethodString(method));
		sb.append(" : ");
		sb.append(getReturnType(method));
		return sb.toString();
	}

	// uhsing 2011/01/25
	// The String is something like getText( ) : String
	public static String getFullStringOfMethod(Method method) {
		StringBuffer sb = new StringBuffer();
		sb.append(getFullMethodString(method));
		sb.append(" : ");
		sb.append(getReturnType(method));
		return sb.toString();
	}
	
	// uhsing 2011/02/15
	public static Object[] getArgumentTypes(String classType, String methodName) {
		Method method = getMethodFromFullString(classType, methodName);
		return getArgumentTypes(method);
	}
	
	// uhsing 2011/02/15
	public static Object[] getArgumentTypes(Method method) {
		Class<?>[] types = method.getParameterTypes();
		Object[] result = new Object[types.length];
		
		// Copy the simple name of class type
		for(int i = 0; i < types.length; i++) {
			result[i] = types[i].getSimpleName();
		}
		return result;
	}
	
	/**
	 * Get the method description as string
	 * 
	 * @param method
	 *            The method from a class
	 * @return The String of this method eg: getXXXValue(int,int[])
	 */
	public static String getMethodString(Method method) {
		StringBuffer sb = new StringBuffer();

		if (method.getName().startsWith("get"))
			sb.append(method.getName().substring(3));
		else if (method.getName().startsWith("is"))
			sb.append(method.getName().substring(2));
		else
			sb.append(method.getName());
		sb.append("( ");

		Class<?>[] cc = method.getParameterTypes();
		for (int i = 0; i < cc.length; i++) {
			String s = null;
			String clzname = cc[i].getName();
			if (clzname.startsWith("["))
				s = getArrayTypeClassToString(clzname);
			else
				s = getLastStringOfClassType(clzname);

			if (s != null)
				sb.append(s);
			if (i < cc.length - 1)
				sb.append(" , ");
		}
		sb.append(" )");

		return sb.toString();
	}

	public static String getFullMethodString(Method method) {
		StringBuffer sb = new StringBuffer();
		sb.append(method.getName());
		sb.append("( ");

		Class<?>[] cc = method.getParameterTypes();
		for (int i = 0; i < cc.length; i++) {
			String s = null;
			String string = cc[i].getName();
			if (string.startsWith("["))
				s = getArrayTypeClassToString(string);
			else
				s = getLastStringOfClassType(string);

			if (s != null)
				sb.append(s);
			if (i < cc.length - 1)
				sb.append(" , ");
		}
		sb.append(" )");

		return sb.toString();
	}

	/**
	 * Get the return type of a passed method convert to String
	 * 
	 * @param method
	 *            The method which we want to get return type
	 * @return String The name of class eg: Vector
	 */
	public static String getReturnType(Method method) {
		String rtnType = method.getReturnType().getName();
		if (rtnType.startsWith("[")) // Array type
			return getArrayTypeClassToString(rtnType);

		return getLastStringOfClassType(rtnType);
	}

	/**
	 * Get the name of a class
	 * 
	 * @param clztype
	 *            The class name with package eg: java.util.Vector
	 * @return String The name of class eg: Vector
	 */
	public static String getLastStringOfClassType(String clztype) {
		String result = clztype;

		int index = clztype.lastIndexOf(".");
		if (index != -1)
			result = result.substring(index + 1);
		if (result != null)
			result = result.trim();

		return result;
	}

	/**
	 * To convert [[[B to byte[][][] ; [[Ljava.lang.String; to String[][]
	 * 
	 * @param target
	 *            The string of [B which wants to convert to byte[] B byte C
	 *            char D double F float I int J long Lclassname; class or
	 *            interface S short Z boolean
	 */
	public static String getArrayTypeClassToString(String target) {
		if (!target.startsWith("["))
			return target;

		StringBuffer sb = new StringBuffer();
		if (target.endsWith("B")) {
			sb.append("byte");
		} else if (target.endsWith("C")) {
			sb.append("char");
		} else if (target.endsWith("D")) {
			sb.append("double");
		} else if (target.endsWith("F")) {
			sb.append("float");
		} else if (target.endsWith("I")) {
			sb.append("int");
		} else if (target.endsWith("L")) {
			sb.append("long");
		} else if (target.endsWith("S")) {
			sb.append("short");
		} else if (target.endsWith("Z")) {
			sb.append("boolean");
		} else if (target.endsWith(";")) {
			String temp = getLastStringOfClassType(target);
			temp = temp.substring(0, temp.length() - 1);
			sb.append(temp);
		}

		int depth = target.lastIndexOf("[") + 1;
		for (int i = 0; i < depth; i++)
			sb.append("[]");

		return sb.toString();
	}

	public static String getClassName(String filePath) {
		if (filePath == null)
			return null;

		String fileName = filePath.substring(getLastSlashIdx(filePath) + 1);
		int indexDot = fileName.lastIndexOf(".");
		if (indexDot < 0)
			return null;
		return fileName.substring(0, indexDot);
	}

	public static String getClassPath(String filePath) {
		if (filePath == null)
			return null;

		return filePath.substring(0, getLastSlashIdx(filePath) + 1);
	}

	public static Object getValue(Object obj, Method method, Object[] args) {
		try {
			return obj.getClass().getMethod(method.getName(),
					method.getParameterTypes()).invoke(obj, args);
		} catch (NoSuchMethodException nsme) {
			System.err.println("CheckMethod.equalsTo(): " + nsme.toString());
		} catch (IllegalAccessException iae) {
			System.err.println("CheckMethod.equalsTo(): " + iae.toString());
		} catch (InvocationTargetException ite) {
			System.err.println("CheckMethod.equalsTo(): " + ite.toString());
		}

		return null;
	}

	/**
	 * 要兼顧 // 及 \ 的處理，免得在 linux 上無法載入AUT 取得string中，左右斜線(slash)最後出現的位置
	 * 
	 * @author zwshen
	 * @date 20051115
	 * @param filePath
	 * @return
	 */
	private static int getLastSlashIdx(String str) {
		int result = str.lastIndexOf("\\");
		if (result >= 0)
			return result;
		return str.lastIndexOf("/");
	}

	/**
	 * 判斷傳進來的classes 是否每一個都是 Simple Type.
	 * 
	 * @author zwshen
	 * @date 20051124
	 */
	private static boolean isSimpleTypes(Class<?>[] classes) {

		for (int i = 0; i < classes.length; i++) {
			if (!isSimpleType(classes[i]))
				return false; // 這一個不為 Simple type..
		}
		// all is simple type
		return true;
	}

	/**
	 * 判斷傳進來的class 是否為 Simple Type
	 * 
	 * @author zwshen
	 * @date 20051123
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean isSimpleType(Class target) {
		if (target.isPrimitive())
			return true;
		if (target.isAssignableFrom(Boolean.class))
			return true;
		if (target.isAssignableFrom(Integer.class))
			return true;
		if (target.isAssignableFrom(String.class))
			return true;
		if (target.isAssignableFrom(Double.class))
			return true;
		if (target.isAssignableFrom(Long.class))
			return true;
		if (target.isAssignableFrom(Character.class))
			return true;

		return false;
	}

	/* 由 Method String(包含傳回值) 取得對應的 Method */
	public static Method getMethodFromFullString(String classname,
			String methodname) {
		if (classname.equals(""))
			return null;
		String prefix = "";
		Class<?> _class = null;
		try {
			_class = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		List<Method> methods = null;
		if (methodname.startsWith("get")) {
			methods = getMethodsStartsWith(_class, "get");
			prefix = "get";
		} else if (methodname.startsWith("is")) {
			methods = getMethodsStartsWith(_class, "is");
			prefix = "is";
		} else {
			methods = getMethodsStartsWith(_class, "others");
		}

		if (methods == null || methodname == null)
			return null;

		Iterator<Method> ite = methods.iterator();
		while (ite.hasNext()) {
			Method m = (Method) ite.next();
			if (methodname.equals(prefix + getStringOfMethod(m)))
				return m;
		}

		return null;
	}

	// 呼叫static main 函式
	public static void invokeMain(Method main, String[] args) {
		try {
			if (main.getParameterTypes().length != 0) {
				main.invoke(null, new Object[] { args });
			} else {
				main.invoke(null, new Object[] {});
			}
		} catch (IllegalArgumentException e) {
			System.err.println("instanceAUTbyMain err:" + e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("instanceAUTbyMain err:" + e.getMessage());
		} catch (InvocationTargetException e) {
			System.err.println("instanceAUTbyMain err:" + e.getMessage());
		}
	}

	public static Class<?> loadClass(String url) {
		try {
			return new FileClassLoader().loadFile(url + ".class");
		} catch (ClassNotFoundException ce) {
		}

		return null;
	}
}
