package gttlipse.vfsmCoverageAnalyser.view.dialog;

import gtt.testscript.TestScriptDocument;
import gttlipse.resource.ResourceFinder;
import gttlipse.resource.ResourceManager;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.TestScriptNodeFinder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class AddMethodNodeDialog extends TitleAreaDialog{
	private String _nodeName;
	private Text _text;
	private TestMethodNode _methodNode;
	private ProjectNode _projectNode;
	private TreeViewer _viewer;
	
	
	public AddMethodNodeDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText( "Type in the name of Method" );
		this.setTitle("Insert Suggestion");
		this.setMessage("Insert suggestions to Test Case Project");
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		final Label label = new Label(area, SWT.NULL);
		label.setText("Method Name:");
		_text = new Text(area, SWT.NULL);
		_text.setText(_nodeName);
		_text.setLayoutData(data);
		
		return area;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		
		Button btn_ok = createButton(parent, SWT.OK, "OK", true);
		
		btn_ok.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				

				ResourceFinder finder = new ResourceFinder();
				IProject prj = finder.findIProject(_projectNode);
				ResourceManager manager = new ResourceManager();
				
				
				TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
				manager.addJavaSourceFolder(prj, "Suggestion");
				SourceFolderNode srcfolder = (SourceFolderNode)nodefinder.findNode(new Path("/"+_projectNode.getName()+"/Suggestion"));

				
				String filename = ((TestCaseNode)_methodNode.getParent()).getName();
				manager.AddJavaClass(srcfolder, filename);
				TestCaseNode classnode = (TestCaseNode)srcfolder.getChildrenByName(filename);
				TestFileManager filemanager = new TestFileManager();
				
				filemanager.addTestMethod(classnode,_text.getText());
				TestMethodNode methodNode = (TestMethodNode)classnode.getChildrenByName(_text.getText());
				TestScriptDocument doc = _methodNode.getDocAt(0).clone();
				methodNode.addDocument(doc);
				filemanager.addScriptDocument(doc, true);
				
				
				_viewer.refresh();
				
				setReturnCode(SWT.OK);
				close();
			}
		});
		
		Button btn_cancel = createButton(parent, SWT.CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
			
		});
		
	}
	
	public void setDefaultText(String name) {
		_nodeName = name;
	}
	
	public void setProjectNode(ProjectNode projectNode) {
		_projectNode = projectNode;
	}
	
	public void setTreeViewer(TreeViewer viewer) {
		_viewer = viewer;
	}
	
	public void setMethodNode(TestMethodNode methodNode) {
		_methodNode = methodNode;
	}
	
	
}
