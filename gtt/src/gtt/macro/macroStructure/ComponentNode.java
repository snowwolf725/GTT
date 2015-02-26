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
/*
 * Created on 2005/3/12 by 哲銘
 */
package gtt.macro.macroStructure;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

/**
 * @author 哲銘 這個Class表達單一個Swing元件(預設的Swing元件) 如： JButtom, JMenu, ...
 */
public class ComponentNode extends AbstractMacroNode {

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	private IComponent m_componentData = EventModelFactory.createComponent();

	private EventCoverage m_coverage;

	private void initCoverage() {
		m_coverage = new EventCoverage(m_componentData);
	}

	public ComponentNode(IComponent data) {
		m_componentData = data.clone();		
		m_Name = getName();
		initCoverage();
	}
	
	// copy construct
	public ComponentNode(ComponentNode data) {
		m_componentData = data.getComponent().clone();
		m_Name = data.getName();
		initCoverage();
	}

	public EventCoverage getEventCoverage() {
		return m_coverage;
	}

	public static ComponentNode create(IComponent d) {
		return new ComponentNode(d);
	}

	public static ComponentNode create() {
		return create(EventModelFactory.createComponent());
	}

	public static ComponentNode create(String type, String name) {
		return create(SwingComponent.create(type, name));
	}

	public IComponent getComponent() {
		return m_componentData;
	}

	// 將相關操作交給IComponent做
	public String getWinType() {
		return m_componentData.getWinType();
	}

	public String getTitle() {
		return m_componentData.getTitle();
	}

	public String getType() {
		return m_componentData.getType();
	}
	//node name
	public String getName() {
		if(m_Name.equals("")) {
			return getComponentName();
		}
		return m_Name;
	}
	
	/**
	 *  Add Component name getter by soriel 100408
	 */
	public String getComponentName() {
		return m_componentData.getName();
	}

	public String getText() {
		return m_componentData.getText();
	}

	public int getIndex() {
		return m_componentData.getIndex();
	}

	public int getIndexOfSameName() {
		return m_componentData.getIndexOfSameName();
	}

	public void setWinType(String type) {
		m_componentData.setWinType(type);
	}

	public void setTitle(String title) {
		m_componentData.setTitle(title);
	}

	public void setType(String type) {
		m_componentData.setType(type);
	}

	public void setName(String name) {
		m_Name = name;
	}
	
	/**
	 * Add Component name setter by soriel 100408
	 */
	public void setComponentName(String name) {
		m_componentData.setName(name);
	}

	public void setText(String text) {
		m_componentData.setText(text);
	}

	public void setIndex(int idx) {
		m_componentData.setIndex(idx);
	}

	public void setIndexOfSameName(int idx) {
		m_componentData.setIndexOfSameName(idx);
	}

	public String toString() {
		String classType = m_componentData.getType();
		while (classType.indexOf(".") > 0) {
			classType = classType.substring(classType.indexOf(".") + 1,
					classType.length());
		}
		if (getName() == null || getName().equals(""))
			return classType;

		return getName() + ":" + classType;
	}

	public ComponentNode clone() {
		return new ComponentNode(this);
	}

	public Class<?> getComponentClass() {
		try {
			return Class.forName(getType());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_COMPONENT_NODE;
	}

}
