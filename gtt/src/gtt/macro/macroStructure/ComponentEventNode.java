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
 * Created on 2005/4/11
 * 
 * @author 哲銘 這個class表達單一個元件的事件節點(event node)
 */
package gtt.macro.macroStructure;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ComponentReference;
import gtt.macro.macroStructure.ComponentReferenceImpl;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class ComponentEventNode extends AbstractMacroNode implements
		IHaveArgument {

	ComponentReference refComponent;

	private String eventType = ""; // 記載事件的類型(名稱)

	private int eventID = 0; // 記載事件類型(名稱)的編號
	
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
	

	private Arguments arguments = new Arguments();

	public ComponentEventNode() {
		this("");
	}

	public ComponentEventNode(String path) {
		refComponent = new ComponentReferenceImpl(path);
	}

	public ComponentEventNode(ComponentNode comp) {
		refComponent = new ComponentReferenceImpl(comp.getPath().toString());
	}

	public void setComponentPath(String path) {
		refComponent.setReferencepath(path);
	}

	public String getComponentPath() {
//		refComponent.lookupComponent(this);
		return refComponent.getReferencePath();
	}

	public ComponentNode getComponent() {
		refComponent.lookupComponent(this);
		return refComponent.getComponent();
	}

	private ComponentEventNode(ComponentEventNode node) {
		refComponent = new ComponentReferenceImpl(node.refComponent);
		dyType = node.getDyType();
		dyValue = node.getDyValue();
		dyIndex = node.getDyIndex();
		eventType = node.getEventType();
		eventID = node.getEventID();
		arguments = node.getArguments().clone();
	}

	public void setEvent(String etype, int eid) {
		eventType = etype;
		eventID = eid;
	}

	public String getEventType() {
		return eventType;
	}

	public int getEventID() {
		return eventID;
	}

	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	@Override
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	@Override
	public ComponentEventNode clone() {
		return new ComponentEventNode(this);
	}

	@Override
	public String toString() {
		return getName() + "." + eventType + arguments.toString();
	}

	@Override
	public String getName() {
		if (getComponent() == null) {
			return "[NULL REFERENCE]";
		}
		return getComponent().getName();
	}

	@Override
	public Arguments getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(Arguments list) {
		arguments = list;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_COMPONENT_EVENT_NODE;
	}
}
