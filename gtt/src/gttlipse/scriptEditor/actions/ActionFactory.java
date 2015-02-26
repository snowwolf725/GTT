/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.runner.Controller;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.refactoring.script.RenameAction;
import gttlipse.refactoring.script.RenameWindowTitleAction;
import gttlipse.scriptEditor.actions.node.AddAUTInfoNodeAction;
import gttlipse.scriptEditor.actions.node.AddAssertNodeAction;
import gttlipse.scriptEditor.actions.node.AddClassNodeAction;
import gttlipse.scriptEditor.actions.node.AddEventNodeAction;
import gttlipse.scriptEditor.actions.node.AddFolderNodeAction;
import gttlipse.scriptEditor.actions.node.AddMethodNodeAction;
import gttlipse.scriptEditor.actions.node.AddOracleNodeAction;
import gttlipse.scriptEditor.actions.node.AddPackageNodeAction;
import gttlipse.scriptEditor.actions.node.AddSourceFolderNodeAction;
import gttlipse.scriptEditor.actions.node.AddTestScriptDocumentAction;
import gttlipse.scriptEditor.actions.node.CopyNodeAction;
import gttlipse.scriptEditor.actions.node.CutNodeAction;
import gttlipse.scriptEditor.actions.node.DelNodeAction;
import gttlipse.scriptEditor.actions.node.DownMoveNodeAction;
import gttlipse.scriptEditor.actions.node.EditNodeAction;
import gttlipse.scriptEditor.actions.node.PasteNodeAction;
import gttlipse.scriptEditor.actions.node.UpMoveNodeAction;
import gttlipse.scriptEditor.def.ActionType;
import gttlipse.scriptEditor.def.TestScriptTage;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;


/**
 * @author SnowWolf725
 * 
 * created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class ActionFactory implements IActionFactory{
	private Controller _controller = new Controller(); // 2003art.
	protected ImageDescriptor m_imageDesc;
	private URL baseurl = GTTlipse.getDefault().getBundle().getEntry(TestScriptTage.GTTLIPSEIMGFOLDER);;

	public ActionFactory() {
	}

	/* (non-Javadoc)
	 * @see GTTlipse.actions.IActionFactory#getAction(org.eclipse.jface.viewers.TreeViewer, int)
	 */
	public Action getAction(TreeViewer viewer, int type) {
		EnhancedAction action = null;
		switch(type) {
		case ActionType.OPEN_NEW_FILE:
			action = new SelectProjectAction();
			setupAction(action,"Create association", "Create association between GTTlipse and Java Project","newFile.jpg");
			break;
		case ActionType.OPEN_FILE:
			action = new OpenFileAction();
			setupAction(action,"Open File", "Open GTT Script File","openFile.jpg");
			break;
		case ActionType.SAVE_FILE :
			action = new SaveFileAction();
			setupAction(action,"Save File", "Save GTT Script File","saveFile.jpg");
			break;
		case ActionType.SAVE_AS_FILE :
			action = new SaveAsFileAction();
			setupAction(action,"Save As File", "Save As GTT Script File","saveAsFile.jpg");
			break;
		case ActionType.STOP_RECORD :
			action = new StopAction();
			StopAction _stop = (StopAction)action;
			_stop.setController(_controller);
			setupAction(action,"Stop", "Stop Record", "recordStop.jpg");
			break;
		case ActionType.RECORD :
			action = new RecordAction();
			RecordAction _record = (RecordAction)action;
			_record.setController(_controller);
			setupAction(action,"Record", "Record action", "record.jpg");
			break;
		case ActionType.REPLAY :
			action = new ReplayAction();
			setupAction(action,"Replay", "Replay action", "replay2.gif");
			break;
		case ActionType.GOTO_CODE :
			action = new GotoCodeAction();
			setupAction(action,"Goto Code", "Goto Code","defToMacro.gif");
			break;
		case ActionType.SYNC :
			action = new SyncScriptAndCodeAction();
			setupAction(action,"Syn Script and Code", "Syn Script and Code","Sync.png");
			break;
		case ActionType.ADD_SOURCE_FOLDER_NODE :
			action = new AddSourceFolderNodeAction();
			setupAction(action,"Add SourceFolderNode", "AddSourceFolderNode action", "SourceFolder.png");
			break;
		case ActionType.ADD_PACKAGE_NODE :
			action = new AddPackageNodeAction();
			setupAction(action,"Add PackageNode", "AddPackageNode action", "Package.png");
			break;
		case ActionType.ADD_CASE_NODE :
			action = new AddClassNodeAction();
			setupAction(action,"Add ClassNode", "AddClassNode action", "TestCase.png");
			break;
		case ActionType.ADD_METHOD_NODE :
			action = new AddMethodNodeAction();
			setupAction(action,"Add MethodNode", "AddMethodNode action", "Method.png");
			break;
		case ActionType.ADD_TEST_SCRIPT_DOCUMENT :
			action = new AddTestScriptDocumentAction();
			setupAction(action,"Add TestScriptDocument", "AddTestScriptDocument action", "TestScriptDocument.png");
			break;
		case ActionType.ADD_FOLDER_NODE :
			action = new AddFolderNodeAction();
			setupAction(action,"Add FolderNode", "AddFolderNode action", "FolderNode.png");
			break;
		case ActionType.ADD_ASSERT_NODE :
			action = new AddAssertNodeAction();
			setupAction(action,"Add AssertNode", "AddAssertNode action", "AssertNode.png");
			break;
		case ActionType.ADD_EVENT_NODE :
			action = new AddEventNodeAction();
			setupAction(action,"Add EventNode", "AddEventNode action", "EventNode.png");
			break;
		case ActionType.ADD_AUTINFO_NODE :
			action = new AddAUTInfoNodeAction();
			setupAction(action,"Add AUTInfoNode", "Add AUTInfoNode action", "AUTInfoNode.png");
			break;
		case ActionType.ADD_ORACLE_NODE :
			action = new AddOracleNodeAction();
			setupAction(action,"Add OracleNode", "Add OracleNode action", "TestOracle.png");
			break;
		case ActionType.DEL_NODE :
			action = new DelNodeAction();
			setupAction(action,"Del Node", "DelNode action", "delete.gif");
			break;
		case ActionType.EDIT_NODE :
			action = new EditNodeAction();
			setupAction(action,"Edit Node", "EditNode action", "edit.jpg");
			break;
		case ActionType.COPY_NODE :
			action = new CopyNodeAction();
			setupAction(action,"Copy Node", "CopyNode action", "copy.gif");
			break;
		case ActionType.CUT_NODE :
			action = new CutNodeAction();
			setupAction(action,"Cut Node", "CutNode action", "cut.gif");
			break;
		case ActionType.PASTE_NODE :
			action = new PasteNodeAction();
			setupAction(action,"Paste Node", "PasteNode action", "paste.gif");
			break;
		case ActionType.UP_MOVE_NODE :
			action = new UpMoveNodeAction();
			setupAction(action,"Up Move Node", "MoveNode action", "up.gif");
			break;
		case ActionType.DOWN_MOVE_NODE :
			action = new DownMoveNodeAction();
			setupAction(action,"Down Move Node", "MoveNode action","down.gif");
			break;
		case ActionType.FILTER :
			action = new FilterAction();
			setupAction(action,"Display Test Script", "Display Test Script","copy.gif");
			break;
		case ActionType.ADD_ORACLE :
			action = new AddTestOracleAction();
			setupAction(action,"Add Oracle Node To Script", "Add Oracle Node To Script","manualInsert.gif");
			break;
		case ActionType.COLLECT_COMP_INFO :
			action = new CollectCompInfoAction();
			setupAction(action,"Collect Componenet Info", "Collect Componenet Info","configuration.gif");
			break;
		case ActionType.REFEACTORING_RENAME:
			action = new RenameAction();
			setupAction(action,"Rename", "Rename","transparent.png");
			break;
		case ActionType.REFEACTORING_RENAME_WINDOW_TITLE:
			action = new RenameWindowTitleAction();
			setupAction(action,"Rename Window Title", "Rename Window Title","transparent.png");
			break;
		}
		
		if(action != null)
			action.setViewer(viewer);
		else System.out.println(type);
		return action;
	}
	
	private void setupAction(EnhancedAction item, String text, String tiptext,
			String imgname) {
		item.setText(text);
		item.setToolTipText(tiptext);
		try {
			URL imgurl = new URL(baseurl, imgname);
			item.setImageDescriptor(ImageDescriptor.createFromURL(imgurl));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
