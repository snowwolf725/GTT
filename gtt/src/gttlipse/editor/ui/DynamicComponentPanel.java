package gttlipse.editor.ui;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ViewAssertNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DynamicComponentPanel {

	private AbstractMacroNode m_node = null;
	private Group m_dynGroup = null;
	private Combo m_attribute = null;
	private Label lbl_attrType = null;
	private Label _indexInPage = null;
	private	Button[] radios = null;
	private Text m_indexInPage = null;
	private Text m_typeValue = null;
	private Composite m_parent = null;
	
	public DynamicComponentPanel(Composite parent, AbstractMacroNode node) {
		if(node instanceof ComponentEventNode || node instanceof ViewAssertNode) {
			m_node = node;
		}
		m_parent = parent;
		initDynamicComponentPanel();
	}
	
	private void initDynamicComponentPanel() {
		m_dynGroup = new Group(m_parent, SWT.SHADOW_ETCHED_IN);
		m_dynGroup.setText("Dynamic Component Setting");
		
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 2;
		m_dynGroup.setLayout(gridlayout2);
		
		GridData gd2 = new GridData();
		gd2.widthHint = 78;
		
		GridData textFildData = new GridData();
		textFildData.widthHint = 300;
		textFildData.heightHint = 12;
		
		GridData label = new GridData();
		label.widthHint = 300;
		label.horizontalSpan = 2;
		
		GridData radio1data = new GridData();
		radio1data.horizontalSpan = 2;
		
		//type combo
		lbl_attrType = new Label(m_dynGroup, SWT.NULL);
		lbl_attrType.setText("Decide Attribute:");
		lbl_attrType.setLayoutData(label);
		
		m_attribute = new Combo(m_dynGroup, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_attribute.setVisibleItemCount(5);
		m_attribute.setLayoutData(gd2);
		m_attribute.add("Name:");
		m_attribute.add("Element Xpath:");
		m_attribute.add("Element Id:");
		m_attribute.add("Link Text:");
		
		//type value text 
		m_typeValue = new Text(m_dynGroup, SWT.BORDER);
		m_typeValue.setLayoutData(textFildData);
		
		// Index in Page
		_indexInPage = new Label(m_dynGroup, SWT.NULL);
		_indexInPage.setText("Index In Page: ");
		_indexInPage.setLayoutData(label);
		
		//radio
		radios = new Button[3];
		radios[0] = new Button(m_dynGroup, SWT.RADIO);
		radios[0].setText("All Components");
		radios[0].setLayoutData(radio1data);
		
		radios[1] = new Button(m_dynGroup, SWT.RADIO);
		radios[1].setText("Last Component");
		radios[1].setLayoutData(radio1data);
		
		radios[2] = new Button(m_dynGroup, SWT.RADIO);
		radios[2].setText("Index in Page");
		
		m_indexInPage = new Text(m_dynGroup, SWT.BORDER);
		m_indexInPage.setLayoutData(textFildData);
		
		//initial
		m_dynGroup.setVisible(false);
		lbl_attrType.setVisible(false);
		m_attribute.setVisible(false);
		radios[0].setVisible(false);
		radios[1].setVisible(false);
		radios[2].setVisible(false);
		m_indexInPage.setVisible(false);
		_indexInPage.setVisible(false);
		
		m_attribute.setText("Name:");
		m_typeValue.setText("");
		radios[1].setSelection(true);
		m_indexInPage.setText("1");
	}
	
	//判斷是否要顯示動態決定元件選項
	public void dynamicVisible(ComponentNode selectnode) {
		if(m_node != null && selectnode instanceof ComponentNode) {
			if(selectnode.getComponentName().isEmpty() &&  selectnode.getText().isEmpty() && selectnode.getWinType().isEmpty() && selectnode.getTitle().isEmpty()) {
				m_dynGroup.setVisible(true);
				lbl_attrType.setVisible(true);
				m_attribute.setVisible(true);
				radios[0].setVisible(true);
				radios[1].setVisible(true);
				radios[2].setVisible(true);
				m_indexInPage.setVisible(true);
				_indexInPage.setVisible(true);
				m_typeValue.setVisible(true);
				//event node setting
				if(m_node instanceof ComponentEventNode) {
					if(!((ComponentEventNode)m_node).getDyType().isEmpty()) {
						m_attribute.setText(((ComponentEventNode)m_node).getDyType());
					}
					if(!((ComponentEventNode)m_node).getDyValue().isEmpty()) {
						m_typeValue.setText(((ComponentEventNode)m_node).getDyValue());
					}
					if(((ComponentEventNode)m_node).getDyIndex() == 0) {
						radios[1].setSelection(true);
						radios[0].setSelection(false);
						radios[2].setSelection(false);
					}
					else if(((ComponentEventNode)m_node).getDyIndex() == -1) {
						radios[0].setSelection(true);
						radios[1].setSelection(false);
						radios[2].setSelection(false);
					}
					else {
						radios[2].setSelection(true);
						radios[0].setSelection(false);
						radios[1].setSelection(false);
						m_indexInPage.setText(String.valueOf(((ComponentEventNode)m_node).getDyIndex()));
					}
				}
				//assert node setting
				if(m_node instanceof ViewAssertNode) {
					if(!((ViewAssertNode)m_node).getDyType().isEmpty()) {
						m_attribute.setText(((ViewAssertNode)m_node).getDyType());
					}
					if(!((ViewAssertNode)m_node).getDyValue().isEmpty()) {
						m_typeValue.setText(((ViewAssertNode)m_node).getDyValue());
					}
					if(((ViewAssertNode)m_node).getDyIndex() == 0) {
						radios[1].setSelection(true);
						radios[0].setSelection(false);
						radios[2].setSelection(false);
					}
					else if(((ViewAssertNode)m_node).getDyIndex() == -1) {
						radios[0].setSelection(true);
						radios[1].setSelection(false);
						radios[2].setSelection(false);
					}
					else {
						radios[2].setSelection(true);
						radios[0].setSelection(false);
						radios[1].setSelection(false);
						m_indexInPage.setText(String.valueOf(((ViewAssertNode)m_node).getDyIndex()));
					}
				}
			}
			else {
				m_dynGroup.setVisible(false);
				lbl_attrType.setVisible(false);
				m_attribute.setVisible(false);
				radios[0].setVisible(false);
				radios[1].setVisible(false);
				radios[2].setVisible(false);
				m_indexInPage.setVisible(false);
				_indexInPage.setVisible(false);
				m_typeValue.setVisible(false);
				
				m_attribute.setText("Name:");
				m_typeValue.setText("");
				radios[1].setSelection(true);
				radios[0].setSelection(false);
				radios[2].setSelection(false);
				m_indexInPage.setText("1");
			}
		}
	}
	
	public int getDynamicComponentIndex() {
		if(radios[0].getSelection()) {
			return -1;//代表全部都選
		}else if (radios[1].getSelection()) {
			return 0;//代表最後一個
		}
		else 
			return Integer.valueOf(m_indexInPage.getText());
	}
	
	public String getDynamicComponentType() {
		return m_attribute.getText();
	}
	
	public String getDynamicComponentValue() {
		return m_typeValue.getText();
	}
}
