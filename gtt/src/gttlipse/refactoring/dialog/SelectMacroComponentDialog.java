package gttlipse.refactoring.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.macro.dialog.ComponentPanel;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * @author Soriel
 *
 */
public class SelectMacroComponentDialog extends TitleAreaDialog implements SelectionListener {	
	private Text m_txt_name;
	private String _newName;
	private String _oldName;
	private ComponentPanel _componentPanel = null;
	private AbstractMacroNode _root = null;
	private AbstractMacroNode _event = null;
	private AbstractMacroNode _select = null;
	private boolean _nameFieldVisible = false;

	public SelectMacroComponentDialog(Shell parentShell, AbstractMacroNode event) {
		super(parentShell);
		_oldName = event.getName();
		_event = event;
		_nameFieldVisible = false;
	}
	
	public SelectMacroComponentDialog(Shell parentShell, AbstractMacroNode event, boolean visible) {
		super(parentShell);
		_oldName = event.getName();
		_event = event;
		_nameFieldVisible = visible;
	}
	
	public SelectMacroComponentDialog(Shell parentShell) {
		super(parentShell);
		_oldName = "";
		_event = null;
	}
	
	public SelectMacroComponentDialog(Shell parentShell, boolean visible) {
		super(parentShell);
		_oldName = "";
		_event = null;
		_nameFieldVisible = visible;
	}
	
	public String getNewName() {
		return _newName;
	}	
	
	public AbstractMacroNode getSelectNode() {
		return _select;
	}
	
	public void setRoot(AbstractMacroNode root) {
		_root = root;
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		this.getShell().setText("Select Macro Component");
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		if(_nameFieldVisible == true) {
			/* name field */
			Label lbl_newName = new Label(area, SWT.NULL);
			lbl_newName.setText("name: ");
			m_txt_name = new Text(area, SWT.BORDER);
			m_txt_name.setLayoutData(data);
			m_txt_name.setText(_oldName);
			m_txt_name.selectAll();
		}
		
		initialComponentPanel(area);
		
		return parent;
	}
	
	private void initialComponentPanel(Composite parent) {
		_componentPanel = new ComponentPanel(parent, _root, _event, ComponentPanel.PARSE_MACRO_COMPONENT);
		_componentPanel.addSelectionListener(this);
		if(_event != null) {
			_componentPanel.setSelectNode(_event.getParent());
		}
		else {
			_componentPanel.setSelectNode(_root);
		}
		_componentPanel.setupTree();
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_ok = createButton(parent, SWT.OK, "OK", true);
		btn_ok.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				if(_nameFieldVisible) {
					_newName = m_txt_name.getText();
					if(_newName.equals("")) {
						return;
					}
				}
				_select = _componentPanel.getSelectNode();
				setReturnCode(SWT.OK);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", false);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}
	
	private void doSelect() {
		Tree t = _componentPanel.getTree();
		
		if( t.getSelectionCount() == 0 ) return;
		TreeItem _selectTreeItem = t.getSelection()[0];
		AbstractMacroNode _select = (AbstractMacroNode)_selectTreeItem.getData();
		
		if( _select == null ) return;
		if( !(_select instanceof MacroComponentNode) ) return;
		
		_componentPanel.setSelectNode(_select);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		doSelect();
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		doSelect();
	}
}
