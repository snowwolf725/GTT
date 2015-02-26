package gttlipse.macro.dialog;

//import java.util.Map;
//import java.util.TreeMap;

import gtt.macro.macroStructure.AbstractMacroNode;
//import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.macro.view.MacroViewPart;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Combo;


public class WebLaunchNodeDialog extends TitleAreaDialog implements SelectionListener{
	private LaunchNode m_node = null;
	
	private Text m_linkText;
	private Text m_broFocus;
	private Text m_waitTime;	
//	private Combo m_combo;
	private Label m_showLabel;	
	private Button m_button;
	private Shell m_shell;
	private AbstractMacroNode m_selectNode = null;
//	private Map<Integer, AbstractMacroNode> m_map = new TreeMap<Integer, AbstractMacroNode>();
	// private Text m_componentClassType;
	private Button[] functionOption;

	public WebLaunchNodeDialog(Shell parentShell, LaunchNode node) {
		super(parentShell);
		m_shell = parentShell;
		m_node = node;
		m_selectNode = getRootNode();		
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Launch Node");
		setMessage("Edit Launch Node");
		//set layout
		
		Group m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Launch Infomaction");
		
		GridLayout gridlayout = new GridLayout(2, false);
		m_group.setLayout(gridlayout);
		
		GridData textdata = new GridData();
		textdata.widthHint = 300;
		textdata.heightHint = 15;
		
		GridData radio = new GridData();
		radio.widthHint = 230;
		radio.heightHint = 15;
		radio.horizontalSpan = 2;
		
		GridData label = new GridData();
		label.widthHint = 180;
		
		//create widget
		functionOption = new Button[11];
		functionOption[0] = new Button(m_group, SWT.RADIO);
		functionOption[0].setText("URL Under Test :");
		
		m_linkText = new Text(m_group, SWT.NULL);
		m_linkText.setLayoutData(textdata);
		
		functionOption[1] = new Button(m_group, SWT.RADIO);
		functionOption[1].setText("Previous Page");
		functionOption[1].setLayoutData(radio);
		
		functionOption[2] = new Button(m_group, SWT.RADIO);
		functionOption[2].setText("Next Page");
		functionOption[2].setLayoutData(radio);
		
		functionOption[3] = new Button(m_group, SWT.RADIO);
		functionOption[3].setText("Change Browser Focus By Title:");
//		functionOption[3].setLayoutData(radio);
		m_broFocus = new Text(m_group, SWT.NULL);
		m_broFocus.setLayoutData(textdata);
		
		functionOption[4] = new Button(m_group, SWT.RADIO);
		functionOption[4].setText("Return to Default Browser");
		functionOption[4].setLayoutData(radio);
		
		functionOption[5] = new Button(m_group, SWT.RADIO);
		functionOption[5].setText("Close Browser");
		functionOption[5].setLayoutData(radio);
		
		//add solve javascript alert and confirm window
		//2010/10/12
		functionOption[6] = new Button(m_group, SWT.RADIO);
		functionOption[6].setText("Click OK On Next Alert");
		functionOption[6].setLayoutData(radio);
		
		functionOption[7] = new Button(m_group, SWT.RADIO);
		functionOption[7].setText("Choose OK On Next Confirmation");
		functionOption[7].setLayoutData(radio);
		
		functionOption[8] = new Button(m_group, SWT.RADIO);
		functionOption[8].setText("Choose Cancel On Next Confirmation");
		functionOption[8].setLayoutData(radio);
		
		//add for set AJAX waiting time
		//2010/11/27
		functionOption[9] = new Button(m_group, SWT.RADIO);
		functionOption[9].setText("Set AJAX waiting time:");
		m_waitTime = new Text(m_group, SWT.NULL);
		m_waitTime.setLayoutData(textdata);
		
		//add parse function
		//2011/02/16
		functionOption[10] = new Button(m_group, SWT.RADIO);
		functionOption[10].setText("Parse and store components of the current page to:");
		m_showLabel = new Label(m_group, SWT.NULL);
		m_showLabel.setText("");
		m_showLabel.setLayoutData(label);
		Label nullLabel = new Label(m_group, SWT.NULL);
		m_showLabel.setText("");		
		nullLabel.setLayoutData(label);
		m_button = new Button(m_group, SWT.PUSH);
		m_button.setText("select macro component");
		m_button.addSelectionListener(this);
//		m_combo = new Combo(m_group, SWT.DROP_DOWN | SWT.READ_ONLY);
//		m_combo.setVisibleItemCount(10);
//		AbstractMacroNode macroRoot = getRootNode();
//		addMComboData(m_combo, macroRoot);
//		m_combo.setLayoutData(textdata);
		
		//set default
		
		if(m_node.getArgument().equalsIgnoreCase("Loading URL")) {
			m_linkText.setText(m_node.getClassPath());
			functionOption[0].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Previous Page")){
			functionOption[1].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Next Page")){
			functionOption[2].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Change Browser")){
			m_broFocus.setText(m_node.getClassPath());
			functionOption[3].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Default Browser")){
			functionOption[4].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Close Browser")){
			functionOption[5].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Click OK On Next Alert")){
			functionOption[6].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Choose OK On Next Confirmation")){
			functionOption[7].setSelection(true);
		} 
		else if (m_node.getArgument().equalsIgnoreCase("Choose Cancel On Next Confirmation")){
			functionOption[8].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("AJAX waiting time")){
			m_waitTime.setText(m_node.getClassPath());
			functionOption[9].setSelection(true);
		}
		else if (m_node.getArgument().equalsIgnoreCase("Parse and store components of the current page")){
			if(MacroViewPart.getMacroPresenter().getDocument().findByPath(m_node.getClassPath())!=null){
//				m_combo.setText(MacroViewPart.getMacroPresenter().getDocument().findByPath(m_node.getClassPath()).getName());
				m_selectNode = MacroViewPart.getMacroPresenter().getDocument().findByPath(m_node.getClassPath());			
				m_showLabel.setText(m_selectNode.getName());				
			}
			functionOption[10].setSelection(true);
		}
		else {
			functionOption[0].setSelection(true);
		}
		
		return parent;
	}
	
	private AbstractMacroNode getRootNode() {
		return MacroViewPart.getMacroPresenter().getDocument().getMacroScript();
	}

	public void update() {
		System.out.println("Update...");
		//清空之前的記錄
		m_node.setClassPath("");
//		m_node.setClassName("");
//		m_node.setName("");
		if (functionOption[0].getSelection()) {
			m_node.setArgument("Loading URL");
			m_node.setClassPath(m_linkText.getText());
		}
		if (functionOption[1].getSelection()) {
			m_node.setArgument("Previous Page");
		}
		if (functionOption[2].getSelection()) {
			m_node.setArgument("Next Page");
		}
		if (functionOption[3].getSelection()) {
			m_node.setArgument("Change Browser");
			m_node.setClassPath(m_broFocus.getText());
		}
		if (functionOption[4].getSelection()) {
			m_node.setArgument("Default Browser");
		}
		if (functionOption[5].getSelection()) {
			m_node.setArgument("Close Browser");
		}
		if (functionOption[6].getSelection()) {
			m_node.setArgument("Click OK On Next Alert");			
		}
		if (functionOption[7].getSelection()) {
			m_node.setArgument("Choose OK On Next Confirmation");
		}
		if (functionOption[8].getSelection()) {
			m_node.setArgument("Choose Cancel On Next Confirmation");
		}		
		if (functionOption[9].getSelection()) {
			m_node.setArgument("AJAX waiting time");
			m_node.setClassPath(m_waitTime.getText());
		}
		if (functionOption[10].getSelection()) {
			m_node.setArgument("Parse and store components of the current page");		
			m_node.setClassPath(m_selectNode.getPath().toString());
//			m_node.setClassPath(m_map.get(m_combo.getSelectionIndex()).getPath().toString());
		}
		
	}

//	private void addMComboData(Combo m_combo, AbstractMacroNode macroNode){
//		m_combo.add(macroNode.getName());
//		m_map.put(m_map.size(), macroNode);	
//		for(int i = 0; i < macroNode.getChildren().length; i++) {
//			AbstractMacroNode child = macroNode.getChildren()[i];
//			if(child instanceof MacroComponentNode){
//				addMComboData(m_combo, child);
//			}
//		}
//	}
	
	protected void okPressed() {
		if(functionOption[10].getSelection() && m_showLabel.getText().isEmpty())
			return;
		update();
		super.okPressed();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		MacroComponentSelectionDialog dialog = new MacroComponentSelectionDialog(m_shell, m_node, (MacroComponentNode) m_selectNode);
		dialog.open();
		if(dialog.getReturnCode() == 0){
			m_selectNode = MacroViewPart.getMacroPresenter().getDocument().findByPath(m_node.getClassPath());
			System.out.println(m_selectNode.getName());
			m_showLabel.setText(m_selectNode.getName());			
		}
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		m_showLabel.setText("");
		
	}

}
