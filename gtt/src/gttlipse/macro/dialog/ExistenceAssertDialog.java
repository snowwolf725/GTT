package gttlipse.macro.dialog;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gtt.macro.macroStructure.ExistenceAssertNode;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExistenceAssertDialog extends TitleAreaDialog {
	private ExistenceAssertNode m_node = null;
	
	private IComponent m_com = null;
	private Group m_group = null;
	private Group m_group2 = null;
	private Text m_linkText;
	private Text m_windowTitle;
	private Combo m_componentClassType = null;
	// private Text m_componentClassType;
	private Text m_nodeName;
	private Text m_text;
	private Text m_indexInPage;
	private Text m_count;
	private Button[] assertOption;
	private Button[] assertValue;

	public ExistenceAssertDialog(Shell parentShell, ExistenceAssertNode node) {
		super(parentShell);
		m_node = node;
		m_com = node.getComponent();
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Existence Assert");
		setMessage("Edit Existence Assert");

		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Component Infomaction");
		
		m_group2 = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group2.setText("Except Result");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		m_group.setLayout(gridlayout);
		m_group2.setLayout(gridlayout);
		
		GridData textFildData = new GridData();
		textFildData.widthHint = 300;
		textFildData.heightHint = 12;
		
		GridData radio2data = new GridData();
		radio2data.widthHint = 200;
		radio2data.horizontalSpan = 2;

		// Element Node Name
		Label _nodeName = new Label(m_group, SWT.NULL);
		_nodeName.setText("Name: ");
		m_nodeName = new Text(m_group, SWT.BORDER);
		m_nodeName.setLayoutData(textFildData);
		if (m_com.getName() != null)
			m_nodeName.setText(m_com.getName());

		// Component Class Type
		createComponentTypeCombo(m_group, textFildData);

		// Element xpath
		Label _text = new Label(m_group, SWT.NULL);
		_text.setText("Element Xpath: ");
		m_text = new Text(m_group, SWT.BORDER);
		m_text.setLayoutData(textFildData);
		if (m_com.getText() != null)
			m_text.setText(m_com.getText());

		// Element id
		Label _windowTitle = new Label(m_group, SWT.NULL);
		_windowTitle.setText("Element Id: ");
		m_windowTitle = new Text(m_group, SWT.BORDER);
		m_windowTitle.setLayoutData(textFildData);
		if (m_com.getTitle() != null)
			m_windowTitle.setText(m_com.getTitle());

		// Link Text
		Label _linkText = new Label(m_group, SWT.NULL);
		_linkText.setText("Link Text: ");
		m_linkText = new Text(m_group, SWT.BORDER);
		m_linkText.setLayoutData(textFildData);
		if (m_com.getWinType() != null)
			m_linkText.setText(m_com.getWinType());

		// Index in Page
		assertOption = new Button[2];
		assertOption[0] = new Button(m_group, SWT.RADIO);
		assertOption[0].setText("All Components");
		assertOption[0].setLayoutData(radio2data);
		
		assertOption[1] = new Button(m_group, SWT.RADIO);
		assertOption[1].setText("Index In Page:");
		
//		Label _indexInPage = new Label(m_group, SWT.NULL);
//		_indexInPage.setText("Index In Page: ");

		m_indexInPage = new Text(m_group, SWT.BORDER);
		m_indexInPage.setLayoutData(textFildData);
		
		//assert existence
		final Label _assertValue = new Label(m_group2, SWT.NULL);
		_assertValue.setText("Assert:");
		_assertValue.setLayoutData(radio2data);
		
		
		assertValue = new Button[2];
		assertValue[0] = new Button(m_group2, SWT.RADIO);
		assertValue[0].setText("Existence");
		
		assertValue[1] = new Button(m_group2, SWT.RADIO);
		assertValue[1].setText("Non Existence");
		
		final Label _assertCount = new Label(m_group2, SWT.NULL);
		_assertCount.setText("Assert Count:");
		_assertCount.setLayoutData(radio2data);
		
		m_count = new Text(m_group2, SWT.BORDER);
		m_count.setLayoutData(textFildData);
		
		
		if (m_node.getExpectResult() == true)
			assertValue[0].setSelection(true);
		else if (m_node.getExpectResult() == false)
			assertValue[1].setSelection(true);
		
		if (m_com.getIndex() == 0) {
			assertOption[0].setSelection(true);
			m_count.setVisible(true);
        	_assertCount.setVisible(true);
        	assertValue[0].setVisible(false);
        	assertValue[1].setVisible(false);
        	_assertValue.setVisible(false);
		}
		else {
			assertOption[1].setSelection(true);
			m_indexInPage.setText(String.valueOf(m_com.getIndex()));
			m_count.setVisible(false);
        	_assertCount.setVisible(false);
        	assertValue[0].setVisible(true);
        	assertValue[1].setVisible(true);
        	_assertValue.setVisible(true);
		}
			
		m_count.setText(String.valueOf(m_node.getExpectResultCount()));
		
		
		Listener listener0 = new Listener() {
            public void handleEvent (Event e) {
            	m_count.setVisible(true);
            	_assertCount.setVisible(true);
            	assertValue[0].setVisible(false);
            	assertValue[1].setVisible(false);
            	_assertValue.setVisible(false);
            }
		};

		Listener listener1 = new Listener() {
            public void handleEvent (Event e) {
            	m_count.setVisible(false);
            	_assertCount.setVisible(false);
            	assertValue[0].setVisible(true);
            	assertValue[1].setVisible(true);
            	_assertValue.setVisible(true);
            }
		};
		
		assertOption[0].addListener(SWT.Selection, listener0);
		assertOption[1].addListener(SWT.Selection, listener1);
		
		
		
		
		return parent;
	}

	private void createComponentTypeCombo(final Composite area, GridData data) {
		final Label lbl_comptype = new Label(area, SWT.NULL);
		lbl_comptype.setText("Component type:");
		m_componentClassType = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_componentClassType.setVisibleItemCount(10);

		final IEventModel model = EventModelFactory.getDefault();
		List<IComponent> coms = model.getComponents();
		TreeSet<String> sset_coms = new TreeSet<String>();
		Iterator<?> ite = coms.iterator();
		while (ite.hasNext()) {
			IComponent c = (IComponent) ite.next();
			// System.out.println("Component" + c.g());
			c.setName("name");
			sset_coms.add(c.getType());

		}

		ite = sset_coms.iterator();
		boolean firstmethod = true;
		while (ite.hasNext()) {
			String com = (String) ite.next();
			m_componentClassType.add(com);
			if (firstmethod) {
				m_componentClassType.setText(com);
				firstmethod = false;
			}
		}

		m_componentClassType.setLayoutData(data);
		if (m_com != null) {
			m_componentClassType.setText(m_com.getType());
		}
	}

	public void update() {
		m_com.setTitle(m_windowTitle.getText());
		m_com.setType(m_componentClassType.getText());
		m_com.setName(m_nodeName.getText());
		m_com.setText(m_text.getText());
		m_com.setWinType(m_linkText.getText());
		
		if (assertValue[0].getSelection()) {
			m_node.setExpectResult(true);
		}
		if (assertValue[1].getSelection()) {
			m_node.setExpectResult(false);
		}
		
		if (assertOption[0].getSelection()) {
			m_node.setIndex(0);//0代表全選
		}
		if (assertOption[1].getSelection()) {
			m_node.setIndex(Integer.valueOf(m_indexInPage.getText()));
		}
		
		m_node.setExpectResultCount(Integer.valueOf(m_count.getText()));
	}
	
	protected void okPressed() {
		update();
		
		super.okPressed();
	}

	

}
