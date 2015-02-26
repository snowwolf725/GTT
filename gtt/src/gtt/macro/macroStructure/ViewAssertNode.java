/*
. * Copyright (C) 2006-2009
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
/*
 * Created on 2005/4/11
 */
package gtt.macro.macroStructure;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ComponentReference;
import gtt.macro.macroStructure.ComponentReferenceImpl;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.macro.dialog.EditDialogFactory;

import java.lang.reflect.Method;

public class ViewAssertNode extends AbstractMacroNode implements IHaveArgument {

	private ComponentReference m_reftoComponent;

	private Assertion assertion = new Assertion();

	//dynamic node use 
	private String dyType = "";//紀錄要以何種屬性來判斷元件
	
	private String dyValue = "";//紀錄屬性的數值
	
	private int dyIndex = 1;//紀錄元件的index

	public String getDyType() {
		return dyType;
	}

	public void setDyType(String dyType) {
		this.dyType = dyType;
	}

	public String getDyValue() {
		return dyValue;
	}

	public void setDyValue(String dyValue) {
		this.dyValue = dyValue;
	}

	public int getDyIndex() {
		return dyIndex;
	}

	public void setDyIndex(int dyIndex) {
		this.dyIndex = dyIndex;
	}
	
	
	public ViewAssertNode() {
		m_reftoComponent = new ComponentReferenceImpl();
	}

	public ViewAssertNode(String path) {
		m_reftoComponent = new ComponentReferenceImpl(path);
	}

	public ViewAssertNode(ComponentNode comp) {
		 m_reftoComponent = new ComponentReferenceImpl(comp.getPath().toString());
	}

	private	ViewAssertNode(ViewAssertNode node) {
		m_reftoComponent = new ComponentReferenceImpl(node.getComponentPath());
		dyType = node.getDyType();
		dyValue = node.getDyValue();
		dyIndex = node.getDyIndex();
		assertion = node.getAssertion().clone();
	}

	public void setComponentPath(String path) {
		m_reftoComponent.setReferencepath(path);
	}

	public String getComponentPath() {
		return m_reftoComponent.getReferencePath();
	}

	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	@Override
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public ComponentNode getComponent() {
		m_reftoComponent.lookupComponent(this);
		return m_reftoComponent.getComponent();
	}

	public Assertion getAssertion() {
		// uhsing 2011/01/25
		if (assertion.getMethod() == null) {
			initAssertionMethod();
		}
		return assertion;
	}

	// uhsing 2011/01/25
	private void initAssertionMethod() {
		if (getComponent() != null) {
			Method method = null;
			String type = getComponent().getType();
			String methodName = assertion.getFullMethodName();
			
			if (!methodName.equals("")) {
				method = ReflectionUtil.getMethodFromFullString(type, methodName);
			}
			else {
				methodName = assertion.getMethodName();
				
				// Use class type and method name to get specific Method
				try {
					method = ReflectionUtil.getMethod(Class.forName(type), methodName);
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			// Set the Method
			assertion.setMethod(method);
		}
	}
	
	@Override
	public ViewAssertNode clone() {
		return new ViewAssertNode(this);
	}

	@Override
	public String toString() {
		if (assertion == null)
			return getComponent().getName();
		return getComponent().getName() + "." + assertion.toString();
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void setName(String name) {
		// nothing to do
	}

	@Override
	public Arguments getArguments() {
		return assertion.getArguments();
	}

	@Override
	public void setArguments(Arguments list) {
		assertion.setArguments(list);
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_VIEW_ASSERT_NODE;
	}
}
