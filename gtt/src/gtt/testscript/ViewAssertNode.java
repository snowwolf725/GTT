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
package gtt.testscript;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.testscript.visitor.ITestScriptVisitor;

/**
 * 一個ViewAssertion node 由兩個部份組成：component 及 assertion
 * assertion 的行為類似Method，但是多了message 及 assert value (Test Oracle)。
 * @author zws
 *
 */
public class ViewAssertNode extends AbstractNode implements Cloneable{
	
	private IComponent m_Component = null;
	private Assertion m_Assertion = null;
	private String m_ActualValue = null;
	
	public void setActualValue(String v) {
		m_ActualValue = v;
	}
	public String getActualValue() {
		return m_ActualValue;
	}
	
	public void setComponent(IComponent c) {
		m_Component = c;
	}
	
	public IComponent getComponent() {
		return m_Component;
	}
	
	public void setAssertion(Assertion a) {
		m_Assertion = a;
	}
	
	public Assertion getAssertion() {
		return m_Assertion;
	}
	
	public ViewAssertNode(IComponent c, Assertion a) {
		m_Component = c.clone();
		m_Assertion = a.clone();
		m_Assertion.setMethod(a.getMethod());
	}
	
	public ViewAssertNode(IComponent c) {
		m_Component = c;
	}
	
	public ViewAssertNode() { }
	
	public String toString() {
		if(m_Assertion != null)
			return m_Component.getName() + "." + m_Assertion.toString();
		
		return m_Component.getName(); 
	}
	@Override
	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	@Override
	public String toDetailString() {
		if(m_Assertion != null)
			return m_Component.getName() + "." + m_Assertion.toString();
		
		return m_Component.getName(); 
	}

	@Override
	public String toSimpleString() {
		if(m_Assertion != null)
			return m_Component.getName() + "." + m_Assertion.toString();
		
		return m_Component.getName(); 
	}
	
	/**
	 * clone 給複製貼上使用
	 */
	public ViewAssertNode clone() {
		return  new ViewAssertNode(m_Component.clone(), m_Assertion.clone() );
	}
	
}
