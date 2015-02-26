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
package gtt.macro.io;

import gtt.eventmodel.IEvent;
import gtt.macro.visitor.IMacroFitVisitor;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.util.Iterator;


public class XmlMacroFitSaveVisitor extends XmlMacroSaveVisitor implements
		IMacroFitVisitor {

	// Constructor
	public XmlMacroFitSaveVisitor() {
		super();
	}

	public XmlMacroFitSaveVisitor(org.w3c.dom.Document doc) {
		super(doc);
	}
	
	// for GTT plug-in
	public void setDocument(org.w3c.dom.Document doc) {
		m_XmlDoc = doc;
		m_XmlParent = m_XmlDoc.createElement("MacroStructure");
	}
	
	// for GTT plug-in
	public int specificTestPlatformID(){ 
		return 1;
	}

	public void visit(EventTriggerNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("ConversionNode");
		x.setAttribute("data", node.getData());
		x.setAttribute("GenerationKey", node.getGenerationKey());
		x.setAttribute("ComponentType", node.getComponentType());
		x.setAttribute("WindowType", node.getWindowType());
		x.setAttribute("WindowTitle", node.getWindowTitle());

		org.w3c.dom.Element eventList = m_XmlDoc.createElement("EventList");
		Iterator<?> ite = node.getEventList().iterator();
		while (ite.hasNext()) {
			IEvent ie = (IEvent) ite.next();
			eventList.appendChild(saveEventList(ie));
		}
		x.appendChild(eventList);
		m_XmlParent.appendChild(x);
	}

	private org.w3c.dom.Element saveEventList(IEvent ie) {

		org.w3c.dom.Element event = m_XmlDoc.createElement("Event");
		event.setAttribute("name", ie.getName());
		event.setAttribute("ID", Integer.toString(ie.getEventId()));
		// save ArgumentList
		event.appendChild(createArgumentListXmlFromList(ie.getArguments()));

		return event;
	}

	public void visit(FitStateAssertionNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("FitTableAssertionNode");
		x.setAttribute("project", node.getProjectPath());
		x.setAttribute("name", node.getName());
		x.setAttribute("windowTitle", node.getWindowTitle());
		x.setAttribute("windowType", node.getWindowType());
		x.setAttribute("tablePath", node.getFitTableSource().toString());
		m_XmlParent.appendChild(x);
	}

	public void visit(FitNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("FitNode");

		x.setAttribute("project", node.getProjectPath());
		x.setAttribute("name", node.getName());
		x.setAttribute("macro", node.getMacroEventCallerNode()
				.getReferencePath());
		x.setAttribute("fixtureType", node.getFixtureType());
		x.setAttribute("tablePath", node.getFitTableSource().toString());

		m_XmlParent.appendChild(x);

		saveChildren(node, x);
	}

	public void visit(SplitDataAsNameNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("GenerationalTypeINode");

		x.setAttribute("variableNameOfData", node.getVariable());
		x.setAttribute("name", node.getName());

		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(GenerateOrderNameNode node) {
		org.w3c.dom.Element x = m_XmlDoc
				.createElement("GenerationalTypeIINode");

		x.setAttribute("variableNameOfStart", node.getVariableNameOfStart());
		x.setAttribute("variableNameOfEnd", node.getVariableNameOfEnd());
		x.setAttribute("prefix", node.getPrefix());
		x.setAttribute("suffix", node.getSuffix());
		x.setAttribute("name", node.getName());

		// save ArgumentList
		x.appendChild(createArgumentListXml(node));

		m_XmlParent.appendChild(x);

	}

	@Override
	public void visit(FixNameNode node) {
		org.w3c.dom.Element x = m_XmlDoc
				.createElement("GenerationalTypeIIINode");

		x.setAttribute("componentName", node.getComponentName());
		x.setAttribute("name", node.getName());

		m_XmlParent.appendChild(x);
	}

	@Override
	public void visit(FitAssertionNode node) {
		org.w3c.dom.Element x = m_XmlDoc.createElement("FitAssertionNode");
		x.setAttribute("componentType", node.getComponentType());
		x.setAttribute("wiindowType", node.getWindowType());
		x.setAttribute("wiindowTitle", node.getWindowTitle());
		x
				.setAttribute("assertionDataVariable", node
						.getAssertionDataVariable());
		x.setAttribute("generationKey", node.getGenerationKey());

		org.w3c.dom.Element method = m_XmlDoc.createElement("AssertMethod");
		method.setAttribute("name", node.getAssertion().getMethodName());
		method.setAttribute("message", node.getAssertion().getMessage());
		x.appendChild(method);

		m_XmlParent.appendChild(x);
	}
}
