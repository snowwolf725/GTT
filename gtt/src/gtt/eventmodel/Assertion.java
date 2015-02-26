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
package gtt.eventmodel;

import gtt.util.refelection.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Iterator;

public class Assertion implements Cloneable, IHaveArgument {

	public enum CompareOperator {
		LessThan, GreaterThan, EqualTo, NotEqual, LessThanOrEqual, GreaterThanOrEqual, isNull, isNotNull
	}

	public static String getOperatorString(CompareOperator op) {
		if (op == CompareOperator.EqualTo)
			return "==";
		if (op == CompareOperator.NotEqual)
			return "!=";
		if (op == CompareOperator.LessThanOrEqual)
			return "<=";
		if (op == CompareOperator.GreaterThanOrEqual)
			return ">=";
		if (op == CompareOperator.LessThan)
			return "<";
		if (op == CompareOperator.GreaterThan)
			return ">";
		if (op == CompareOperator.isNull)
			return "isNull";
		if (op == CompareOperator.isNotNull)
			return "isNotNull";
		return "???";
	}

	private Method m_method;
	private String m_methodName;
	private String m_fullMethodName = "";

	private String m_message;

	private String expectedValue;

	private CompareOperator m_Operator = CompareOperator.EqualTo;

	private Arguments arguments = new Arguments();

	private int expectedSizeOfCheck = 1; // 預設只有一個是true

	public int getExpectedSizeOfCheck() {
		return expectedSizeOfCheck;
	}

	public void setExpectedSizeOfCheck(int size) {
		this.expectedSizeOfCheck = size;
	}

	public Assertion() {
		m_message = "";
		expectedValue = "";
		m_methodName = "";
	}

	public CompareOperator getCompareOperator() {
		return m_Operator;
	}

	public void setCompareOperator(CompareOperator op) {
		m_Operator = op;
	}

	public void setCompareOperator(String op) {
		if (op.equals("=="))
			m_Operator = CompareOperator.EqualTo;
		if (op.equals("!="))
			m_Operator = CompareOperator.NotEqual;
		if (op.equals("<="))
			m_Operator = CompareOperator.LessThanOrEqual;
		if (op.equals("&lt;="))
			m_Operator = CompareOperator.LessThanOrEqual;
		if (op.equals(">="))
			m_Operator = CompareOperator.GreaterThanOrEqual;
		if (op.equals("&gt;="))
			m_Operator = CompareOperator.GreaterThanOrEqual;
		if (op.equals("<"))
			m_Operator = CompareOperator.LessThan;
		if (op.equals("&lt;"))
			m_Operator = CompareOperator.LessThan;
		if (op.equals(">"))
			m_Operator = CompareOperator.GreaterThan;
		if (op.equals("&gt;"))
			m_Operator = CompareOperator.GreaterThan;
		if (op.equals("isNull"))
			m_Operator = CompareOperator.isNull;
		if (op.equals("isNotNull"))
			m_Operator = CompareOperator.isNotNull;
	}

	public String getMessage() {
		return m_message;
	}

	public void setMessage(String m) {
		this.m_message = m;
	}

	public Method getMethod() {
		return m_method;
	}

	public String getMethodName() {
		return m_methodName;
	}

	// uhsing 2011/01/25
	public String getFullMethodName() {
		return m_fullMethodName;
	}
	
	public void setMethod(Method m) {
		m_method = m;
		if (m != null) {
			m_methodName = m.getName();
			m_fullMethodName = ReflectionUtil.getFullStringOfMethod(m);
		}
		else {
			m_methodName = null;
			m_fullMethodName = "";
		}
	}

	// uhsing 2011/01/25
	public void setFullMethodName(String fullName) {
		m_fullMethodName = fullName;
	}
	
	public void setMethodName(String name) {
		m_methodName = name;
	}

	public String getValue() {
		return expectedValue;
	}

	public void setValue(String v) {
		this.expectedValue = v;
	}

	public static final String NULL_METHOD = "[NULL METHOD]";

	public String toString() {
		if (getMethodName() == null || getMethodName().equals(""))
			return NULL_METHOD;

		StringBuilder result = new StringBuilder(m_methodName);
		result.append(arguments.toString());
		if (expectedValue != null && expectedValue != "") {
			result.append(getOperatorString(m_Operator));
			result.append(expectedValue);
		}
		return result.toString();
	}

	public Assertion clone() {
		Assertion a = new Assertion();
		a.setMethod(m_method);
		a.setMethodName(m_methodName);
		a.setMessage(m_message);
		a.setValue(expectedValue);
		a.setCompareOperator(m_Operator);
		a.setArguments(arguments.clone());
		return a;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Assertion))
			return false;

		Assertion as = (Assertion) o;

		return as.getMethod() == m_method && as.getMessage().equals(m_message)
				&& as.getCompareOperator() == m_Operator
				&& as.getValue().equals(expectedValue);
	}

	public Class<?>[] typeClasses() {
		if (arguments.size() == 0)
			return new Class[] {};

		Class<?>[] result = new Class[arguments.size()];
		Iterator<Argument> ite = arguments.iterator();
		int i = 0;
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			// 基本型態因為不是class所以不能用class.forname要做例外處理
			if (a.getType().equals("int")) {
				result[i] = int.class;
			} else if (a.getType().equals("boolean")) {
				result[i] = boolean.class;
			} else if (a.getType().equals("long")) {
				result[i] = long.class;
			} else if (a.getType().equals("double")) {
				result[i] = double.class;
			} else if (a.getType().equals("char")) {
				result[i] = char.class;
			} else if (a.getType().equals("byte")) {
				result[i] = byte.class;
			} else if (a.getType().equals("short")) {
				result[i] = short.class;
			} else if (a.getType().equals("float")) {
				result[i] = float.class;
			} else if (a.getType().equals("String")) {
				result[i] = String.class;
			} else {
				try {
					result[i] = Class.forName(a.getType());
					System.out.println(Thread.currentThread()
							.getContextClassLoader());
				} catch (ClassNotFoundException e) {
					// can't load java native class type (Ex. String, int, etc)
					System.out.println("Argument Type ClassNotFound");
					result[i] = null;
				}
			}
			i++;

		}
		return result;
	}

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments args) {
		arguments = args;
	}

}
