package gttlipse.macro.dialog;

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
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipseConfig;
import gttlipse.fit.dialog.EventTriggerDialog;
import gttlipse.fit.dialog.FitAssertionDialog;
import gttlipse.fit.dialog.FitNodeDialog;
import gttlipse.fit.dialog.FitStateAssertionDialog;
import gttlipse.fit.dialog.FixNameDialog;
import gttlipse.fit.dialog.GenerateOrderNameDialog;
import gttlipse.fit.dialog.SplitDataAsNameDialog;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

public class EditDialogFactory {
	public static final int NID_BREAKER_NODE 					= 1;
	public static final int NID_COMMENT_NODE 					= 2;
	public static final int NID_COMPONENT_EVENT_NODE 			= 3;
	public static final int NID_COMPONENT_NODE 					= 4;
	public static final int NID_NDEFCOMPONENT_NODE 				= 5;
	public static final int NID_COMPOSITE_MACRO_NODE 			= 6;
	public static final int NID_FIT_NODE 						= 7;
	public static final int NID_INVISIBLEROOT_NODE 				= 8;
	public static final int NID_MACRO_COMPONENT_NODE		 	= 9;
	public static final int NID_SYSTEM_NODE 					= 10;
	public static final int NID_MACRO_EVENT_NODE 				= 11;
	public static final int NID_DYNAMIC_COMPONENT_EVENT_NODE 	= 12;
	public static final int NID_DYNAMIC_COMPONENT_NODE 			= 13;
	public static final int NID_EVENT_TRIGGER_NODE 				= 14;
	public static final int NID_EXISTENCE_ASSERT_NODE 			= 15;
	public static final int NID_FIT_ASSERTION_NODE 				= 16;
	public static final int NID_FIT_STATE_ASSERTION_NODE 		= 17;
	public static final int NID_FIXNAME_NODE 					= 18;
	public static final int NID_GENERATE_ORDERNAME_NODE 		= 19;
	public static final int NID_INCLUDE_NODE 					= 20;
	public static final int NID_LAUNCH_NODE 					= 21;
	public static final int NID_MACRO_EVENT_CALLER_NODE 		= 22;
	public static final int NID_MOCK_ABSTRACT_MACRO_NODE 		= 23;
	public static final int NID_MODE_ASSERT_NODE 				= 24;
	public static final int NID_ORACLE_NODE 					= 25;
	public static final int NID_SLEEP_NODE 						= 26;
	public static final int NID_SPLITEDATA_ASNAME_NODE 			= 27;
	public static final int NID_SPLITEDATA_NODE 				= 28;
	public static final int NID_VIEW_ASSERT_NODE 				= 29;
	
	public static TitleAreaDialog getEditingDialog(Shell parentShell, AbstractMacroNode node, List<String> generationList) {
		TitleAreaDialog dialog = null;
		if (node == null)
			return null;
		
		dialog = createDialogfromPlugin(parentShell, node, generationList); 
		if(dialog != null)
			return dialog;
		
		switch(node.getNodeID()) {
		case NID_COMPONENT_NODE:
			if (GTTlipseConfig.testingOnWebPlatform()) {
				dialog = new WebComponentDialog(parentShell, (ComponentNode) node);
			}
			else
				dialog = new ComponentDialog(parentShell, (ComponentNode) node);
			break;
		case NID_DYNAMIC_COMPONENT_NODE:
			dialog = new DynamicComponentDialog(parentShell, (DynamicComponentNode)node);
			break;
		case NID_LAUNCH_NODE:
			dialog = new WebLaunchNodeDialog(parentShell, (LaunchNode) node);
			break;
		case NID_INCLUDE_NODE:
			dialog = new IncludeNodeDialog(parentShell, (IncludeNode)node);
			break;
		case NID_MACRO_COMPONENT_NODE:
			dialog = new MacroComponentDialog(parentShell, (MacroComponentNode)node);
			break;
		case NID_MACRO_EVENT_NODE:
			dialog = new MacroEventDialog(parentShell, (MacroEventNode)node);
			break;
		case NID_COMPONENT_EVENT_NODE:
			dialog = new ComponentEventDialog(parentShell, (ComponentEventNode)node);
			break;
		case NID_DYNAMIC_COMPONENT_EVENT_NODE:
			dialog = new DynamicComponentEventDialog(parentShell, (DynamicComponentEventNode)node);
			break;
		case NID_MACRO_EVENT_CALLER_NODE:
			dialog = new MacroEventCallerDialog(parentShell, (MacroEventCallerNode)node);
			break;
		case NID_VIEW_ASSERT_NODE:
			dialog = new ViewAssertDialog(parentShell, (ViewAssertNode)node);
			break;
		case NID_EXISTENCE_ASSERT_NODE:
			dialog = new ExistenceAssertDialog(parentShell, (ExistenceAssertNode)node);
			break;
		case NID_EVENT_TRIGGER_NODE:
			dialog = new EventTriggerDialog(parentShell, (EventTriggerNode)node, generationList);
			break;
		case NID_FIT_STATE_ASSERTION_NODE:
			dialog = new FitStateAssertionDialog(parentShell, (FitStateAssertionNode)node);
			break;
		case NID_FIT_NODE:
			dialog = new FitNodeDialog(parentShell, (FitNode)node);
			break;
		case NID_SPLITEDATA_ASNAME_NODE:
			dialog = new SplitDataAsNameDialog(parentShell, (SplitDataAsNameNode)node);
			break;
		case NID_GENERATE_ORDERNAME_NODE:
			dialog = new GenerateOrderNameDialog(parentShell, (GenerateOrderNameNode)node);
			break;
		case NID_FIXNAME_NODE:
			dialog = new FixNameDialog(parentShell, (FixNameNode)node);
			break;
		case NID_FIT_ASSERTION_NODE:
			dialog = new FitAssertionDialog(parentShell, (FitAssertionNode)node, generationList);
			break;
		case NID_SPLITEDATA_NODE:
			dialog = new SplitDataDialog(parentShell, (SplitDataNode)node);
			break;
		}
		
		return dialog;
	}
	
	private static TitleAreaDialog createDialogfromPlugin(Shell parentShell, AbstractMacroNode node, List<String> generationList) {
		TitleAreaDialog dialog = null;
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_MACRO_NODE_EDITDIALOG_ID);
		if(gttlipse.GTTlipse.getPlatformInfo() == null)
			return null;
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof INodeEditingDialogBuilder) {
					int testPlatformID = ((INodeEditingDialogBuilder) o).specificTestPlatformID();
					if(testPlatformID != gttlipse.GTTlipse.getPlatformInfo().getTestPlatformID())
						return null;
					int nodeID = ((INodeEditingDialogBuilder) o).specificNodeID();
					if(nodeID == node.getNodeID())
						dialog = ((INodeEditingDialogBuilder) o).crateDialog(parentShell, node, generationList);
					if(dialog != null)
						return dialog;
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
}
