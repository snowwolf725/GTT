package gttlipse.editor.ui;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.views.GTTTestScriptView;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class WebComponentInfoPanel implements IComponentInfoPanel {
	private IComponent m_com = null;
	private Group m_group = null;

	private Text m_linkText;
	private Text m_windowTitle;
	private Combo m_componentClassType = null;
	// private Text m_componentClassType;
	private Text m_nodeName;
	private Text m_text;
	private Text m_indexInPage;
	private Button[] radios;

	public WebComponentInfoPanel(Composite parent, IComponent com) {
		m_com = com;
		initPanel(parent);
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

	private void initPanel(Composite parent) {
		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Component Infomaction");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		m_group.setLayout(gridlayout);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;
		textFildData.heightHint = 12;

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
		Label _indexInPage = new Label(m_group, SWT.NULL);
		_indexInPage.setText("Index In Page: ");

		GridData radio1data = new GridData();
		radio1data.horizontalSpan = 2;

		radios = new Button[3];
		radios[0] = new Button(m_group, SWT.RADIO);
		radios[0].setText("All Components");
		radios[0].setLayoutData(radio1data);

		radios[1] = new Button(m_group, SWT.RADIO);
		radios[1].setText("Last Component");
		radios[1].setLayoutData(radio1data);

		radios[2] = new Button(m_group, SWT.RADIO);
		radios[2].setText("Index in Page");

		m_indexInPage = new Text(m_group, SWT.BORDER);
		m_indexInPage.setLayoutData(textFildData);
//		m_indexInPage.setText(String.valueOf(m_com.getIndex()));
		m_indexInPage.setText(String.valueOf(1));
		// m_indexInPage.setEditable(false);

		if (m_com.getIndex() == 0)
			radios[1].setSelection(true);
		else if (m_com.getIndex() == -1)
			radios[0].setSelection(true);
		else {
			radios[2].setSelection(true);
			m_indexInPage.setText(String.valueOf(m_com.getIndex()));
		}

	}

	public Group getGroup() {
		return m_group;
	}

	public void update() {
		System.out.println("Update...");
		m_com.setTitle(m_windowTitle.getText());
		m_com.setType(m_componentClassType.getText());
		m_com.setName(m_nodeName.getText());
		m_com.setText(m_text.getText());
		if (radios[0].getSelection()) {
			m_com.setIndex(-1);
		}
		if (radios[1].getSelection()) {
			m_com.setIndex(0);
		}
		if (radios[2].getSelection()) {
			m_com.setIndex(Integer.parseInt(m_indexInPage.getText()));
		}
		m_com.setWinType(m_linkText.getText());
		GTTTestScriptView view = GTTlipse.showScriptView();
		view.addComponent(m_com);
	}

	public IComponent getComponent() {
		update();
		return m_com;
	}

	public void setListener(ModifyListener listener) {
		m_componentClassType.addModifyListener(listener);
	}

	public void setComponent(IComponent comp) {
		if (comp.getName() != null)
			m_nodeName.setText(comp.getName());
		if (comp.getWinType() != null)
			m_linkText.setText(comp.getWinType());
		if (comp.getTitle() != null)
			m_windowTitle.setText(comp.getTitle());
		if (comp.getType() != null)
			m_componentClassType.setText(comp.getType());
		if (comp.getText() != null)
			m_text.setText(comp.getText());
		m_indexInPage.setText("0");
		if (comp.getIndex() == 0)
			radios[1].setSelection(true);
		else if (comp.getIndex() == -1)
			radios[0].setSelection(true);
		else {
			radios[2].setSelection(true);
			m_indexInPage.setText(comp.getIndex() + "");
		}

	}

	public Combo getComponentClassType() {
		return m_componentClassType;
	}
}
