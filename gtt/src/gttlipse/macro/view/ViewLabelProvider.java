package gttlipse.macro.view;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class ViewLabelProvider extends LabelProvider {
	private boolean m_viewUsage = false;

	private Set<Object> m_failnodes;

	private ImageRegistry m_imageRegistry = null;

	public ViewLabelProvider(Set<Object> _failnodes) {
		super();
		initImageRegistry();
		m_failnodes = _failnodes;
	}

	public String getText(Object obj) {
		if (obj == null)
			return "";
		// for outer usage
		if (m_viewUsage == true) {
			AbstractMacroNode node = (AbstractMacroNode) obj;
			return node.toString() + node.getBadSmellData().getBadSmellInfo();
		}
		return obj.toString();
	}

	public void setStatisticView(boolean isUsage) {
		m_viewUsage = isUsage;
	}

	public boolean isStatisticView() {
		return m_viewUsage;
	}

	public Image getImage(Object obj) {
		if (obj instanceof ComponentNode)
			return m_imageRegistry.get("ComponentNode");
		if (obj instanceof DynamicComponentNode)
			return m_imageRegistry.get("DynamicComponentNode");
		if (obj instanceof ComponentEventNode)
			return m_imageRegistry.get("ComponentEventNode");
		if (obj instanceof DynamicComponentEventNode)
			return m_imageRegistry.get("DynamicComponentEventNode");
		if (obj instanceof MacroEventCallerNode)
			return m_imageRegistry.get("MacroEventCallerNode");
		if (obj instanceof MacroComponentNode)
			return m_imageRegistry.get("MacroComponentNode");

		if (obj instanceof MacroEventNode) {
			if (m_failnodes.contains(obj) == true)
				return m_imageRegistry.get("MacroEventNode_Fail");
			return m_imageRegistry.get("MacroEventNode");
		}
		if (obj instanceof ModelAssertNode)
			return m_imageRegistry.get("ModelAssertNode");
		if (obj instanceof ViewAssertNode) {
			if (m_failnodes.contains(obj) == true)
				return m_imageRegistry.get("ViewAssertNode_Fail");

			return m_imageRegistry.get("ViewAssertNode");
		}
		if (obj instanceof ExistenceAssertNode) {
			if (m_failnodes.contains(obj) == true)
				return m_imageRegistry.get("ViewAssertNode_Fail");

			return m_imageRegistry.get("ExistenceAssertNode");
		}
		if (obj instanceof EventTriggerNode)
			return m_imageRegistry.get("EventTriggerNode");
		if (obj instanceof LaunchNode)
			return m_imageRegistry.get("LaunchNode");
		if (obj instanceof IncludeNode)
			return m_imageRegistry.get("IncludeNode");
		if (obj instanceof FitStateAssertionNode)
			return m_imageRegistry.get("FitStateAssertionNode");
		if (obj instanceof FitNode)
			return m_imageRegistry.get("FitNode");
		if (obj instanceof SplitDataAsNameNode)
			return m_imageRegistry.get("GenerationalTypeINode");
		if (obj instanceof GenerateOrderNameNode)
			return m_imageRegistry.get("GenerationalTypeIINode");
		if (obj instanceof FixNameNode)
			return m_imageRegistry.get("GenerationalTypeIIINode");
		if (obj instanceof FitAssertionNode)
			return m_imageRegistry.get("FitAssertionNode");
		if (obj instanceof SplitDataNode)
			return m_imageRegistry.get("SplitDataNode");

		// default
		return m_imageRegistry.get("IDontKnow");
	}

	private void initImageRegistry() {
		// init GTTlipse picture
		m_imageRegistry = GTTlipse.getDefault().getImageRegistry();
		String pictures[][] = { { "ComponentNode", "node2_component.gif" },
				{ "DynamicComponentNode", "DynamicComponent.ico" },
				{ "ComponentEventNode", "node2_componentEvent.gif" },
				{ "DynamicComponentEventNode", "DynamicEvent.ico" },
				{ "MacroEventCallerNode", "node2_macroEvent.gif" },
				{ "MacroComponentNode", "node2_macro.gif" },
				{ "MacroEventNode", "node2_eventList.gif" },
				{ "ModelAssertNode", "node2_JUnitAssert.gif" },
				{ "ViewAssertNode", "node2_componentAssert.gif" },
				{ "ExistenceAssertNode", "node2_componentExistAssert.gif" },
				{ "LaunchNode", "node2_Launch.gif" },
				{ "IncludeNode", "IncludeNode.jpg" },				
				{ "NDefNode", "NDefComponent.png" },
				{ "IDontKnow", "IDontKnow.gif" },
				{ "EventTriggerNode", "eventTriggerNode.ico" },
				{ "FitStateAssertionNode", "stateassert.ico" },
				{ "FitNode", "fitnode.gif" },
				{ "GenerationalTypeINode", "generationaltypei.ico" },
				{ "GenerationalTypeIINode", "generationaltypeii.ico" },
				{ "GenerationalTypeIIINode", "generationaltypeiii.ico" },
				{ "FitAssertionNode", "fitassertionnode.GIF" },
				{ "ViewAssertNode_Fail", "ViewAssertNode_Fail.gif" },
				{ "MacroEventNode_Fail", "node2_eventList_Fail.gif" },
				{ "SplitDataNode", "generationaltypei.ico"} };
		try {
			if (m_imageRegistry.get("ComponentNode") != null)
				return;

			URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
			for (int i = 0; i < pictures.length; i++) {
				URL imgurl = new URL(baseurl, pictures[i][1]);
				m_imageRegistry.put(pictures[i][0], ImageDescriptor
						.createFromURL(imgurl));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}