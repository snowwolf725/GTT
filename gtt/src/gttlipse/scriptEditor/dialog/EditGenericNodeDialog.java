/**
 * 
 */
package gttlipse.scriptEditor.dialog;



import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.dialog
 * 
 */
public class EditGenericNodeDialog extends TitleAreaDialog {
	private Text _txt_nodename;

	private Object node;
	
	Shell _shell;

	/**
	 * 
	 */
	public EditGenericNodeDialog(Shell shell, Object obj) {
		super(shell);
		// TODO Auto-generated constructor stub
		this._shell=shell;
		this.node = obj;
		this._txt_nodename = null;
		this.create();
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		final Label lbl_nodename = new Label(area, SWT.NULL);
		lbl_nodename.setText("Node name:");
		_txt_nodename = new Text(area, SWT.SINGLE);
		if (node.toString() == null)
			_txt_nodename.setText("");
		else if (node instanceof CompositeNode)
			_txt_nodename.setText(node.toString());
		else if (node instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument)node;
			_txt_nodename.setText(doc.getName());
		} else if (node instanceof FolderNode)
			_txt_nodename.setText(((FolderNode)node).getName());
		_txt_nodename.setLayoutData(data);

		if (node.toString() != null) {
			this.setTitle("Edit " + node.getClass().getSimpleName());
			this.setMessage("Modify the property of " + node.getClass().getSimpleName() + ".");
		}
		
		return parent;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (node instanceof CompositeNode)
					SetupNodeInfo();
				else if(node instanceof TestScriptDocument) {
					((TestScriptDocument)node).setName(_txt_nodename.getText());
				} else if(node instanceof FolderNode) {
					((FolderNode)node).setName(_txt_nodename.getText());
				}
				setReturnCode(SWT.Modify);
				close();
			}

			/**
			 * 
			 */
			private void SetupNodeInfo() {
				 if (node instanceof TestMethodNode&&
						 _txt_nodename.getText().matches("setUp"))
				 {
					 MessageBox box=new MessageBox(_shell,SWT.OK);
					 box.setText("Warring");
					 box.setMessage("The node name is illegal.");
					 box.open();
					 return;
				 }
				 if(!((BaseNode)node).getParent().checkName(_txt_nodename.getText()))
				 {
					 MessageBox box=new MessageBox(_shell,SWT.OK);
					 box.setText("Warring");
					 box.setMessage("The node name is illegal.");
					 box.open();
					 return;
				 }
				((BaseNode)node).setName(_txt_nodename.getText());
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	public Object getValue() {
		return node;
	}
}
