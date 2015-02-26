package gttlipse.macro.action;

import gttlipse.GTTlipse;
import gttlipse.fit.Action.InsertFitAssertionNodeAction;
import gttlipse.fit.Action.InsertFitNodeAction;
import gttlipse.fit.Action.InsertFitStateAssertionNodeAction;
import gttlipse.fit.Action.InsertFixNameNodeAction;
import gttlipse.fit.Action.InsertGenerateOrderNameNodeAction;
import gttlipse.fit.Action.InsertNoEventTriggerdeAction;
import gttlipse.fit.Action.InsertSplitDataAsNameNodeAction;
import gttlipse.macro.action.badsmell.ClearBadSmellAction;
import gttlipse.macro.action.badsmell.DetectAllSmell;
import gttlipse.macro.action.badsmell.DetectDuplicateMacroEventAction;
import gttlipse.macro.action.badsmell.DetectFeatureEnvyAction;
import gttlipse.macro.action.badsmell.DetectHierarchyAction;
import gttlipse.macro.action.badsmell.DetectLackEncapsulationAction;
import gttlipse.macro.action.badsmell.DetectLongArgListAction;
import gttlipse.macro.action.badsmell.DetectLongMacroCompAction;
import gttlipse.macro.action.badsmell.DetectLongMacroEventAction;
import gttlipse.macro.action.badsmell.DetectMiddleManAction;
import gttlipse.macro.action.badsmell.DetectOuterUsageAction;
import gttlipse.macro.action.badsmell.DetectShotgunSurgeryUsageAction;
import gttlipse.macro.action.badsmell.LocateBadSmellAction;
import gttlipse.macro.action.node.CopyNodeAction;
import gttlipse.macro.action.node.CutNodeAction;
import gttlipse.macro.action.node.DeleteNodeAction;
import gttlipse.macro.action.node.DownMoveAction;
import gttlipse.macro.action.node.EditNodeAction;
import gttlipse.macro.action.node.InsertComponentEventNodeAction;
import gttlipse.macro.action.node.InsertComponentNodeAction;
import gttlipse.macro.action.node.InsertDynamicComponentEventNodeAction;
import gttlipse.macro.action.node.InsertDynamicComponentNodeAction;
import gttlipse.macro.action.node.InsertExistenceAssertNodeAction;
import gttlipse.macro.action.node.InsertIncludeNodeAction;
import gttlipse.macro.action.node.InsertLaunchNodeAction;
import gttlipse.macro.action.node.InsertMacroComponentNodeAction;
import gttlipse.macro.action.node.InsertMacroEventCallAction;
import gttlipse.macro.action.node.InsertMacroEventNodeAction;
import gttlipse.macro.action.node.InsertMacroEventToTestScript;
import gttlipse.macro.action.node.InsertSplitDataNodeAction;
import gttlipse.macro.action.node.InsertViewAssertNodeAction;
import gttlipse.macro.action.node.PasteMacroNodeAction;
import gttlipse.macro.action.node.UpMoveAction;
import gttlipse.macro.action.refactoring.RefactoringAddParameterAction;
import gttlipse.macro.action.refactoring.RefactoringExtractMacroComponentAction;
import gttlipse.macro.action.refactoring.RefactoringExtractMacroEventAction;
import gttlipse.macro.action.refactoring.RefactoringExtractParameterAction;
import gttlipse.macro.action.refactoring.RefactoringHideDelegateAction;
import gttlipse.macro.action.refactoring.RefactoringInlineMacroComponentAction;
import gttlipse.macro.action.refactoring.RefactoringInlineMacroEventAction;
import gttlipse.macro.action.refactoring.RefactoringInlineParameterAction;
import gttlipse.macro.action.refactoring.RefactoringMoveComponentAction;
import gttlipse.macro.action.refactoring.RefactoringMoveMacroComponentAction;
import gttlipse.macro.action.refactoring.RefactoringMoveMacroEventAction;
import gttlipse.macro.action.refactoring.RefactoringRemoveMiddleManAction;
import gttlipse.macro.action.refactoring.RefactoringRemoveParameterAction;
import gttlipse.macro.action.refactoring.RefactoringRenameAction;
import gttlipse.macro.action.refactoring.RefactoringRenameParameterAction;
import gttlipse.macro.action.refactoring.RefactoringRenameWindowTitleAction;
import gttlipse.scriptEditor.actions.IActionFactory;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;


public class MacroViewActionFactory implements IActionFactory {
	
	public MacroViewActionFactory() {}

	public Action getAction(TreeViewer viewer, int type) {
		if (type == MacroAction.NEW_FILE) {
			return createAction(new NewFileAction(), "New File", "New File",
					"newFile.jpg");
		}

		if (type == MacroAction.OPEN_FILE) {
			return createAction(new OpenFileAction(), "Open File", "Open File",
					"openFile.jpg");
		}

		if (type == MacroAction.SAVE_FILE) {
			return createAction(new SaveFileAction(), "Save File", "Save File",
					"saveFile.jpg");
		}
		
		if (type == MacroAction.SAVE_FILE_AS) {
			return createAction(new SaveFileAsAction(), "Save File As",
					"Save File As ...", "saveAsFile.jpg");
		}

		if (type == MacroAction.CUT_MACRO_NODE) {
			return createAction(new CutNodeAction(), "Cut", "Cut", "cut.gif");
		}

		if (type == MacroAction.COPY_MACRO_NODE) {
			return createAction(new CopyNodeAction(), "Copy", "Copy", "copy.gif");
		}

		if (type == MacroAction.PASTE_MACRO_NODE) {
			return createAction(new PasteMacroNodeAction(), "Paste", "Paste",
					"paste.gif");
		}

		if (type == MacroAction.DELETE_MACRO_NODE) {
			return createAction(new DeleteNodeAction(), "Delete", "Delete",
					"delete.gif");
		}

		if (type == MacroAction.UP_MACRO_NODE) {
			return createAction(new UpMoveAction(), "Up", "Up Move", "up.gif");
		}

		if (type == MacroAction.DOWN_MACRO_NODE) {
			return createAction(new DownMoveAction(), "Down", "Down Move",
					"down.gif");
		}

		if (type == MacroAction.EDIT_MACRO_NODE) {
			return createAction(new EditNodeAction(), "Edit", "Edit", "edit.jpg");
		}

		if (type == MacroAction.INSERT_COMPONENT_NODE) {
			return createAction(new InsertComponentNodeAction(),
					"Component Node", "Insert Component", "node2_component.gif");
		}
		
		if (type == MacroAction.INSERT_COMPONENT_EVENT_NODE) {
			return createAction(new InsertComponentEventNodeAction(),
					"Component Event Node", "Insert Component Event",
					"node2_componentEvent.gif");
		}
		
		if (type == MacroAction.INSERT_SINGLE_MACRO_EVENT_NODE) {
			return createAction(new InsertMacroEventCallAction(),
					"Single Macro Event Node", "Insert MacroEventCaller",
					"node2_macroEvent.gif");
		}
		
		if (type == MacroAction.INSERT_MACRO_COMPONENT_NODE) {
			return createAction(new InsertMacroComponentNodeAction(),
					"Macro Component Node", "Insert Macro Component",
					"node2_macro.gif");
		}

		if (type == MacroAction.INSERT_MACRO_EVENT_NODE) {
			return createAction(new InsertMacroEventNodeAction(),
					"Macro Event Node", "Insert Macro Event",
					"node2_eventList.gif");
		}

		if (type == MacroAction.INSERT_VIEW_ASSERT_NODE) {
			return createAction(new InsertViewAssertNodeAction(),
					"View Assert Node", "Insert Assert",
					"node2_componentAssert.gif");
		}

		if (type == MacroAction.INSERT_EXISTENCE_ASSERT_NODE) {
			return createAction(new InsertExistenceAssertNodeAction(),
					"Existence Assert Node", "Insert Assert",
					"node2_componentExistAssert.gif");
		}

		if (type == MacroAction.INSERT_LAUNCH_NODE) {
			return createAction(new InsertLaunchNodeAction(), "Launch Node",
					"Insert Launch Node", "node2_Launch.gif");
		}
		
		
		if(type == MacroAction.INSERT_INCLUDE_NODE) {
			return createAction(new InsertIncludeNodeAction(),
					"Include Node", "Include Node", "IncludeNode.jpg");
		}		

		if (type == MacroAction.INSERT_MACRO_EVENT_TO_TEST_SCRIPT) {
			return createAction(new InsertMacroEventToTestScript(),
					"Insert Macro Event to Test Script",
					"Insert Macro Event to Test Script", "insertToScript.gif");
		}

		if (type == MacroAction.COPY_TEST_SCRIPT_TO_MACRO) {
			return createAction(new CopyTestScriptToMacro(),
					"Copy Test Script to Macro", "Copy Test Script to Macro",
					"cloneFromScript.gif");
		}
		
		if (type == MacroAction.CREATE_METS) {
			return createAction(new CreateMETS(), "Create ETS", "Create ETS",
					"t_analyze.ico");
		}

		if (type == MacroAction.REFRESH) {
			return createAction(new RefreshAction(), "Refresh", "Refresh",
					"refresh.gif");
		}

		if (type == MacroAction.SAVE_MFSM) {
			return createAction(new SaveMFSMAction(), "Save MFSM", "Save MFSM",
					"saveFile.jpg");
		}
		
		if (type == MacroAction.INSERT_EVENT_TRIGGER_NODE) {
			return createAction(new InsertNoEventTriggerdeAction(),
					"EventTrigger Node", "Insert EventTrigger Node",
					"eventTriggerNode.ico");
		}

		if (type == MacroAction.INSERT_FIT_TABLE_ASSERTION_NODE) {
			return createAction(new InsertFitStateAssertionNodeAction(),
					"Fit State Assertion Node", "Fit State Assertion Node",
					"stateassert.ico");
		}

		if (type == MacroAction.INSERT_FIT_NODE) {
			return createAction(new InsertFitNodeAction(), "Fit Node",
					"Fit Node", "fitnode.gif");
		}

		if (type == MacroAction.INSERT_SPLIT_DATA_AS_NAME_NODE) {
			return createAction(new InsertSplitDataAsNameNodeAction(),
					"Split Data As Name", "Split Data As Name",
					"generationaltypei.ico");
		}

		if (type == MacroAction.INSERT_GENERATE_ORDER_NAME_NODE) {
			return createAction(new InsertGenerateOrderNameNodeAction(),
					"Generate Order Name", "Generate Order Name",
					"generationaltypeii.ico");
		}

		if (type == MacroAction.INSERT_FIX_NAME_NODE) {
			return createAction(new InsertFixNameNodeAction(), "Fix Name",
					"Fix Name", "generationaltypeiii.ico");
		}
		
		if (type == MacroAction.INSERT_FIT_ASSERTION_NODE) {
			return createAction(new InsertFitAssertionNodeAction(),
					"Fit Assertion", "Fit Assertion", "fitassertionnode.GIF");
		}
		
		if (type == MacroAction.REFACTORING_RENAME) {
			return createAction(new RefactoringRenameAction(), "Rename",
					"Rename", "transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_ADD_PARAMETER) {
			return createAction(new RefactoringAddParameterAction(),
					"Add Parameter", "Add Parameter", "transparent.png");
		}

		if (type == MacroAction.REFACTORING_REMOVE_PARAMETER) {
			return createAction(new RefactoringRemoveParameterAction(),
					"Remove Parameter", "Remove Parameter", "transparent.png");
		}

		if (type == MacroAction.REFACTORING_RENAME_PARAMETER) {
			return createAction(new RefactoringRenameParameterAction(),
					"Rename Parameter", "Rename Parameter", "transparent.png");
		}

		if (type == MacroAction.REFACTORING_EXTRACT_MACRO_EVENT) {
			return createAction(new RefactoringExtractMacroEventAction(),
					"Extract Macro Event", "Extract Macro Event",
					"transparent.png");
		}

		if (type == MacroAction.REFACTORING_INLINE_MACRO_EVENT) {
			return createAction(new RefactoringInlineMacroEventAction(),
					"Inline Macro Event", "Inline Macro Event",
					"transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_MOVE_MACRO_EVENT) {
			return createAction(new RefactoringMoveMacroEventAction(),
					"Move Macro Event", "Move Macro Event", "transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT) {
			return createAction(new RefactoringExtractMacroComponentAction(),
					"Extract Macro Component", "Extract Macro Component",
					"transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_RENAME_WINDOW_TITLE) {
			return createAction(new RefactoringRenameWindowTitleAction(),
					"Rename WindowTitle", "Rename WindowTitle",
					"transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_MOVE_COMPONENT) {
			return createAction(new RefactoringMoveComponentAction(),
					"Move Component", "Move Component", "transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_MOVE_MACRO_COMPONENT) {
			return createAction(new RefactoringMoveMacroComponentAction(),
					"Move Macro Component", "Move Macro Component",
					"transparent.png");
		}

		if (type == MacroAction.REFACTORING_REMOVE_MIDDLE_MAN) {
			return createAction(new RefactoringRemoveMiddleManAction(),
					"Remove Middle Man", "Remove Middle Man", "transparent.png");
		}
		
		if (type == MacroAction.REPLAY) {
			return createAction(new ReplayAction(), "Replay", "Replay",
					"replay2.gif");
		}
		
		if (type == MacroAction.MULTI_USER_REPLAY) {
			return createAction(new MultiUserReplayAction(), "Multi-user Replay", "Multi-user Replay",
					"replay2.gif");
		}		
		
		if (type == MacroAction.DETECT_OUTER_USAGE) {
			return createAction(new DetectOuterUsageAction(),
					"Detect Outer Utility Rate", "Detect Outer Utility Rate",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_LONGMACROEVENT) {
			return createAction(new DetectLongMacroEventAction(),
					"Detect Long Macro Event", "Detect Long Macro Event",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_LONGMACROCOMP) {
			return createAction(new DetectLongMacroCompAction(),
					"Detect Long Macro Component", "Detect Long Macro Component",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_LONGARG) {
			return createAction(new DetectLongArgListAction(),
					"Detect Long Argument", "Detect Long Parameter List",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_DUPLICATEEVENT) {
			return createAction(new DetectDuplicateMacroEventAction(),
					"Detect Duplicate Event", "Detect Duplicate Event",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_SHOTGUNSURGERYUSAGE) {
			return createAction(new DetectShotgunSurgeryUsageAction(),
					"Detect Shotgun Surgery Usage", "Detect Shotgun Surgery Usage",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_LACKENCAPSULATION) {
			return createAction(new DetectLackEncapsulationAction(),
					"Detect Lack Encapsulation", "Detect Lack of Encapsulation",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_MIDDLEMAN) {
			return createAction(new DetectMiddleManAction(),
					"Detect Middle ManAction", "Detect Middle ManAction",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_FEATUREENVY) {
			return createAction(new DetectFeatureEnvyAction(),
					"Detect Feature Envy", "Detect Feature Envy",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_HIERARCHY) {
			return createAction(new DetectHierarchyAction(),
					"Detect Inconsistent Hierarchy", "Detect Inconsistent Hierarchy",
					"replay2.gif");
		}
		
		if (type == MacroAction.DETECT_ALLSMELL) {
			return createAction(new DetectAllSmell(),
					"Detect All Bad Smell", "Detect All Bad Smell",
					"replay2.gif");
		}
		
		if (type == MacroAction.LOCATE_BADSMELL) {
			return createAction(new LocateBadSmellAction(),
					"Locate Bad Smell", "Locate Bad Smell",
					"replay2.gif");
		}
		
		if (type == MacroAction.CLEAR_BADSMELL) {
			return createAction(new ClearBadSmellAction(),
					"Clear Bad Smell", "Clear Bad Smell",
					"delDown.jpg");
		}

		if (type == MacroAction.CLEAR_LOADTESTINGRESULT) {
			return createAction(new ClearLoadTestingResult(),
					"Clear Results", "Clear Results",
					"delDown.jpg");
		}
		
		//remove this action @20110325 by loveshoo
//		if (type == MacroAction.GENERATE_TO_MACRO_COMPONENT) {
//			return createAction(new GenerateMacroComponentAction(),
//					"Generate Macro Component From SUT", "Generate Macro Component From SUT",
//					"stateErr.gif");
//		}
 
		if (type == MacroAction.REFACTORING_INLINE_MACRO_COMPONENT) {
			return createAction(new RefactoringInlineMacroComponentAction(),
					"Inline Macro Component", "Inline Macro Component",
					"transparent.png");
		}

		if (type == MacroAction.REFACTORING_HIDE_DELEGATE) {
			return createAction(new RefactoringHideDelegateAction(),
					"Hide Delegate", "Hide Delegate", "transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_EXTRACT_PARAMETER) {
			return createAction(new RefactoringExtractParameterAction(),
					"Extract Parameter", "Extract Parameter", "transparent.png");
		}
		
		if (type == MacroAction.REFACTORING_INLINE_PARAMETER) {
			return createAction(new RefactoringInlineParameterAction(),
					"Inline Parameter", "Inline Parameter", "transparent.png");
		}

		if (type == MacroAction.FOCUS_ON_POINT) {
			return createAction(new FocusOnPointAction(),
					"Focus on (Alt+Left Click)", "Focus on (Alt+Left Click)",
					"paste.gif");
		}

		if (type == MacroAction.INSERT_SPLIT_DATA_NODE) {
			return createAction(new InsertSplitDataNodeAction(),
					"Split Data Node", "Split Data Node", "generationaltypei.ico");
		}
		
		if (type == MacroAction.STAT_NODES) {
			return createAction(new StatisticNodesAction(),
					"Statistic Nodes", "Statistic Nodes", "hand.jpg");
		}
		
		if (type == MacroAction.STAT_SEARCH_COST) {
			return createAction(new StatisticSearchingCostAction(),
					"Statistic searching cost", "Statistic searching cost", "hand.jpg");
		}
		
		if (type == MacroAction.INSERT_DYNAMIC_COMPONENT_NODE) {
			return createAction(new InsertDynamicComponentNodeAction(),
					"Dynamic Component Node", "Dynamic Component Node", "DynamicComponent.ico");
		}
		
		if (type == MacroAction.INSERT_DYNAMIC_COMPONENT_EVENT_NODE) {
			return createAction(new InsertDynamicComponentEventNodeAction(),
					"Dynamic Component Event Node", "Dynamic Component Event Node", "DynamicEvent.ico");
		}
		
		return null;
	}

	private Action createAction(Action action, String text, String tip,
			String imgname) {
		action.setText(text);
		action.setToolTipText(tip);
		URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		try {
			URL imgurl = new URL(baseurl, imgname);
			action.setImageDescriptor(ImageDescriptor.createFromURL(imgurl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return action;
	}
}
