package gttlipse.fit.dialog;

import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.fit.node.FitAssertionNode;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class AssertionPanel {
	private FitAssertionNode m_node = null;
	private ComponentNode m_select = null;

	private String m_method = null;
	private Button m_radio_getMethod = null;
	private Button m_radio_isMethod = null;
	private Button m_radio_otherMethod = null;

	private Combo m_combo_getMethod = null;
	private Combo m_combo_isMethod = null;
	private Combo m_combo_otherMethod = null;

	private Text m_message = null;

	private List<Method> m_VectorGetMethod = null;
	private List<Method> m_VectorIsMethod = null;
	private List<Method> m_VectorOtherMethod = null;

	private ArgumentPanel m_argument = null;

	private final String[] TypeItems = { "char", "double", "float", "int",
			"String", "boolean", "long", "short" };

	public AssertionPanel(Composite parent, FitAssertionNode node) {
		m_node = node;
		initPanel(parent);
	}

	private void updateGetMethod() {
		m_combo_getMethod.removeAll();
		for (int i = 0; i < m_VectorGetMethod.size(); i++) {
			String m = "get" + ReflectionUtil.getStringOfMethod((Method) m_VectorGetMethod.get(i));
			m_combo_getMethod.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if( mm != null && m_method != null )
				if( m_method.equals(mm) ) {
					m_radio_getMethod.setSelection(true);
					m_combo_getMethod.setText(m);
				}
		}
	}

	private void updateIsMethod() {
		m_combo_isMethod.removeAll();
		for (int i = 0; i < m_VectorIsMethod.size(); i++) {
			String m = "is" + ReflectionUtil.getStringOfMethod((Method) m_VectorIsMethod.get(i));
			m_combo_isMethod.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if( mm != null && m_method != null )
				if( m_method.equals(mm) ) {
					m_radio_isMethod.setSelection(true);
					m_combo_isMethod.setText(m);
				}
		}
	}

	private void updateOtherMethod() {
		m_combo_otherMethod.removeAll();
		for (int i = 0; i < m_VectorOtherMethod.size(); i++) {
			String m = ReflectionUtil.getStringOfMethod((Method) m_VectorOtherMethod.get(i));
			m_combo_otherMethod.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if( mm != null && m_method != null )
				if( m_method.equals(mm) ) {
					m_radio_otherMethod.setSelection(true);
					m_combo_otherMethod.setText(m);
				}
		}
	}

	private void updateMethods() {
		// 載入 get, is, other 相關 method
		Class<?> comClass = m_select.getComponentClass();
		m_VectorGetMethod =ReflectionUtil.getMethodsStartsWith(comClass, "get");
		m_VectorIsMethod = ReflectionUtil.getMethodsStartsWith(comClass, "is");
		m_VectorOtherMethod = ReflectionUtil.getMethodsStartsWith(comClass, "others");

		m_radio_getMethod.setSelection(false);
		m_radio_isMethod.setSelection(false);
		m_radio_otherMethod.setSelection(false);

		// 更新各個 Combo
		updateGetMethod();
		updateIsMethod();
		updateOtherMethod();
	}

	private void updateArgument() {
		String method = null;

		if( m_radio_getMethod.getSelection() )
			method = m_combo_getMethod.getText();

		if( m_radio_isMethod.getSelection() )
			method = m_combo_isMethod.getText();

		if( m_radio_otherMethod.getSelection() )
			method = m_combo_otherMethod.getText();

		String argString = method.substring(method.indexOf("("), method.indexOf(")"));

		List<String> argVector = genArguments(argString);

		m_argument.clearArgument();

		List<String> values = m_node.getArguments().values();
		for (int i = 0; i < argVector.size(); ++i) {
			String sarg = argVector.get(i).toString();
			String value = "";
			if( i < values.size() )
				value = (String)values.get(i);
			Argument arg = Argument.create(sarg, "", value);
			m_argument.addArgument(arg);
		}
	}

	private Vector<String> genArguments(String argString) {
		Vector<String> argVector = new Vector<String>();
		int typeIndex = 0;
		while (typeIndex != -1) {
			typeIndex = -1;
			int minIndex = argString.length();
			int matchLocation = 0;
			for (int i = 0; i < TypeItems.length; ++i) {
				matchLocation = argString.indexOf(TypeItems[i]);
				if (matchLocation >= 0 && matchLocation < minIndex) {
					minIndex = matchLocation;
					typeIndex = i;
				}
			}
			if (typeIndex > -1) {
				argVector.add(TypeItems[typeIndex]);
				matchLocation = argString.indexOf(TypeItems[typeIndex]);
				argString = argString.substring(matchLocation
						+ TypeItems[typeIndex].length(), argString.length());
			}
		}
		return argVector;
	}

	public void setSelectNode(AbstractMacroNode selectNode) {
		if( selectNode == null ) return;
		if( !(selectNode instanceof ComponentNode) ) return;
		m_select = (ComponentNode)selectNode;
		updateMethods();
		updateArgument();
	}

	class ModifyListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e){
			updateArgument();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			updateArgument();
		}
	}

	private void initMethodGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN );
		lg.setText( "Method" );

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		lg.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.widthHint = 450;
		lg.setLayoutData(gd);

		ModifyListener listener = new ModifyListener();

		GridData gd2 = new GridData();
		gd2.widthHint = 300;

		m_radio_getMethod = new Button(lg, SWT.RADIO);
		m_radio_getMethod.setText("Get:");
		m_radio_getMethod.addSelectionListener(listener);
		m_combo_getMethod = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_combo_getMethod.setVisibleItemCount(10);
		m_combo_getMethod.addSelectionListener(listener);
		m_combo_getMethod.setLayoutData(gd2);

		m_radio_isMethod = new Button(lg, SWT.RADIO);
		m_radio_isMethod.setText("Is:");
		m_radio_isMethod.addSelectionListener(listener);
		m_combo_isMethod = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_combo_isMethod.setVisibleItemCount(10);
		m_combo_isMethod.addSelectionListener(listener);
		m_combo_isMethod.setLayoutData(gd2);

		m_radio_otherMethod = new Button(lg, SWT.RADIO);
		m_radio_otherMethod.setText("Other:");
		m_radio_otherMethod.addSelectionListener(listener);
		m_combo_otherMethod = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_combo_otherMethod.setVisibleItemCount(10);
		m_combo_otherMethod.addSelectionListener(listener);
		m_combo_otherMethod.setLayoutData(gd2);
	}

	private void initInfoGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN );
		lg.setText( "Informaction" );

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		lg.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.widthHint = 450;
		lg.setLayoutData(gd);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;

		Label _message = new Label(lg, SWT.NULL);
		_message.setText("Message:");
		m_message = new Text(lg, SWT.NULL);
		m_message.setLayoutData(textFildData);

		setMessage(m_node.getAssertion().getMessage());
	}


	private void initArgument(Composite parent) {
		m_argument = new ArgumentPanel(parent, false, ArgumentPanel.ALL_BUTTON);
		List<String> type = m_node.getArguments().types();
		List<String> value = m_node.getArguments().values();
		for(int i=0;i<type.size();i++) {
			Argument arg = Argument.create((String)type.get(i), "", (String)value.get(i));
			m_argument.addArgument(arg);
		}
	}

	private void initPanel(Composite parent) {
		m_method = m_node.getAssertion().getMethodName();
		initMethodGroup(parent);
		initArgument(parent);
		initInfoGroup(parent);
	}

	public void setMethodName(String m) {
		m_method = m;
	}

	public String getMethodName() {
		String method = null;

		if( m_radio_getMethod.getSelection() )
			method = m_combo_getMethod.getText();

		if( m_radio_isMethod.getSelection() )
			method = m_combo_isMethod.getText();

		if( m_radio_otherMethod.getSelection() )
			method = m_combo_otherMethod.getText();

		String argString = method.substring(0, method.indexOf("("));

		return argString;
	}
	
	public Method getMethod() {
		Method method = null;

		if( m_radio_getMethod.getSelection() )
			method = m_VectorGetMethod.get(m_combo_getMethod.indexOf(m_combo_getMethod.getText()));

		if( m_radio_isMethod.getSelection() )
			method = m_VectorIsMethod.get(m_combo_isMethod.indexOf(m_combo_isMethod.getText()));

		if( m_radio_otherMethod.getSelection() )
			method = m_VectorOtherMethod.get(m_combo_otherMethod.indexOf(m_combo_otherMethod.getText()));
		
		return method;
	}

	public int getArgCount() {
		return m_argument.getItemCount();
	}

	public String getArgType(int row) {
		return m_argument.getType(row);
	}

	public String getArgName(int row) {
		return m_argument.getName(row);
	}

	public String getArgValue(int row) {
		return m_argument.getValue(row);
	}

	public String getMessage() {
		return m_message.getText();
	}

	public void clearArgument() {
		m_argument.clearArgument();
	}

	public void addArgument(Argument arg) {
		m_argument.addArgument(arg);
	}

	public void setMessage(String msg) {
		m_message.setText(msg);
	}
}
