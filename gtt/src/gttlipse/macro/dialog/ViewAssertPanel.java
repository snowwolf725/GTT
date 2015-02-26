package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.editor.ui.DynamicComponentPanel;

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


public class ViewAssertPanel {
	private Button m_radio_getMethod = null;
	private Button m_radio_isMethod = null;
	private Button m_radio_otherMethod = null;

	private Combo m_getMethodCombo = null;
	private Combo m_isMethodCombo = null;
	private Combo m_otherMethodCombo = null;

	private Text m_assertValue = null;
	private Text m_message = null;
	private Text m_correctAmount = null;

	private ViewAssertNode m_node = null;
	private ComponentNode m_select = null;
	private String m_method = null;

	private List<Method> m_getMethods = null;
	private List<Method> m_isMethods = null;
	private List<Method> m_otherMethods = null;

	private ArgumentPanel m_argument = null;

	private DynamicComponentPanel m_dynPanel = null;

	private final String[] TypeItems = { "char", "double", "float", "int",
			"String", "boolean", "long", "short" };

	public ViewAssertPanel(Composite parent, ViewAssertNode node) {
		m_node = node;
		initPanel(parent);
	}

	public void setSelectNode(AbstractMacroNode node) {
		if (node == null)
			return;
		if (!(node instanceof ComponentNode))
			return;
		m_select = (ComponentNode) node;
		m_dynPanel.dynamicVisible(m_select);

		updateMethods();
		updateArgument();
	}

	class ModifyListener implements SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			updateArgument();
			if (m_select != null)
				m_dynPanel.dynamicVisible(m_select);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			updateArgument();
			if (m_select != null)
				m_dynPanel.dynamicVisible(m_select);
		}
	}

	private void updateMethods() {
		m_radio_getMethod.setSelection(false);
		m_radio_isMethod.setSelection(false);
		m_radio_otherMethod.setSelection(false);

		// 更新各個 Combo
		updateGetMethod();
		updateIsMethod();
		updateOtherMethod();
	}

	private void updateGetMethod() {
		m_getMethodCombo.removeAll();

		m_getMethods = ReflectionUtil.getMethodsStartsWith(m_select.getComponentClass(), "get");

		for (int i = 0; i < m_getMethods.size(); i++) {
			String m = "get"
					+ ReflectionUtil.getStringOfMethod((Method) m_getMethods
							.get(i));
			m_getMethodCombo.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if (mm != null && m_method != null)
				if (m_method.equals(mm)) {
					m_radio_getMethod.setSelection(true);
					m_getMethodCombo.setText(m);
				}
		}
	}

	private void updateIsMethod() {
		m_isMethodCombo.removeAll();

		m_isMethods = ReflectionUtil.getMethodsStartsWith(m_select.getComponentClass(), "is");

		for (int i = 0; i < m_isMethods.size(); i++) {
			String m = "is"
					+ ReflectionUtil.getStringOfMethod((Method) m_isMethods
							.get(i));
			m_isMethodCombo.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if (mm != null && m_method != null)
				if (m_method.equals(mm)) {
					m_radio_isMethod.setSelection(true);
					m_isMethodCombo.setText(m);
				}
		}
	}

	private void updateOtherMethod() {
		m_otherMethodCombo.removeAll();

		m_otherMethods = ReflectionUtil.getMethodsStartsWith(m_select.getComponentClass(), "others");

		for (int i = 0; i < m_otherMethods.size(); i++) {
			String m = ReflectionUtil.getStringOfMethod((Method) m_otherMethods
					.get(i));
			m_otherMethodCombo.add(m);

			String mm = m.substring(0, m.indexOf("("));
			if (mm == null)
				continue;
			if (m_method.equals(mm)) {
				m_radio_otherMethod.setSelection(true);
				m_otherMethodCombo.setText(m);
			}
		}
	}

	private void updateArgument() {
		List<String> arguments = parseSelectedMethodArgument();
		if (arguments == null)
			return;

		m_argument.clearArgument();
		List<String> values = m_node.getArguments().values();

		for (int i = 0; i < arguments.size(); ++i) {
			String sarg = arguments.get(i);
			if (i < values.size())
				m_argument
						.addArgument(Argument.create(sarg, "", values.get(i)));
			else
				m_argument.addArgument(Argument.create(sarg, "", ""));
		}
	}

	private List<String> parseSelectedMethodArgument() {
		String method = getSelectedMethod();
		if (method == null)
			return null;

		String argString = method.substring(method.indexOf("("), method
				.indexOf(")"));

		List<String> arguments = new Vector<String>();
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
				arguments.add(TypeItems[typeIndex]);
				matchLocation = argString.indexOf(TypeItems[typeIndex]);
				argString = argString.substring(matchLocation
						+ TypeItems[typeIndex].length(), argString.length());
			}
		}
		return arguments;
	}

	private String getSelectedMethod() {
		if (m_radio_getMethod.getSelection())
			return m_getMethodCombo.getText();
		if (m_radio_isMethod.getSelection())
			return m_isMethodCombo.getText();
		if (m_radio_otherMethod.getSelection())
			return m_otherMethodCombo.getText();
		return null;
	}

	private void initMethodGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN);
		lg.setText("Method");

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
		m_getMethodCombo = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_getMethodCombo.setVisibleItemCount(10);
		m_getMethodCombo.addSelectionListener(listener);
		m_getMethodCombo.setLayoutData(gd2);

		m_radio_isMethod = new Button(lg, SWT.RADIO);
		m_radio_isMethod.setText("Is:");
		m_radio_isMethod.addSelectionListener(listener);
		m_isMethodCombo = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_isMethodCombo.setVisibleItemCount(10);
		m_isMethodCombo.addSelectionListener(listener);
		m_isMethodCombo.setLayoutData(gd2);

		m_radio_otherMethod = new Button(lg, SWT.RADIO);
		m_radio_otherMethod.setText("Other:");
		m_radio_otherMethod.addSelectionListener(listener);
		m_otherMethodCombo = new Combo(lg, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_otherMethodCombo.setVisibleItemCount(10);
		m_otherMethodCombo.addSelectionListener(listener);
		m_otherMethodCombo.setLayoutData(gd2);
	}

	private void initInfoGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN);
		lg.setText("Informaction");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		lg.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.widthHint = 450;
		lg.setLayoutData(gd);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;

		Label _assertValue = new Label(lg, SWT.NULL);
		_assertValue.setText("Assert Value:");
		m_assertValue = new Text(lg, SWT.NULL);
		m_assertValue.setLayoutData(textFildData);

		Label _message = new Label(lg, SWT.NULL);
		_message.setText("Message:");
		m_message = new Text(lg, SWT.NULL);
		m_message.setLayoutData(textFildData);

		Label _correctAmount = new Label(lg, SWT.NULL);
		_correctAmount.setText("Correct Amount:");
		m_correctAmount = new Text(lg, SWT.NULL);
		m_correctAmount.setLayoutData(textFildData);

		setAssertValue(m_node.getAssertion().getValue());
		setMessage(m_node.getAssertion().getMessage());
		setCorrectAmount(m_node.getAssertion().getExpectedSizeOfCheck());
	}

	private void initArgument(Composite parent) {
		m_argument = new ArgumentPanel(parent, false, ArgumentPanel.ALL_BUTTON);
		List<String> type = m_node.getArguments().types();
		List<String> value = m_node.getArguments().values();
		for (int i = 0; i < type.size(); i++) {
			Argument arg = Argument.create((String) type.get(i), "",
					(String) value.get(i));
			m_argument.addArgument(arg);
		}
	}

	private void initDynamicComponentPanel(Composite parent) {
		m_dynPanel = new DynamicComponentPanel(parent, m_node);
	}

	private void initPanel(Composite parent) {
		m_method = m_node.getAssertion().getMethodName();
		initMethodGroup(parent);
		initArgument(parent);
		initInfoGroup(parent);
		initDynamicComponentPanel(parent);
	}

	public void setMethodName(String m) {
		m_method = m;
	}

	public Method getMethod() {
		if (m_radio_getMethod.getSelection())
			return m_getMethods.get(m_getMethodCombo.indexOf(m_getMethodCombo
					.getText()));
		if (m_radio_isMethod.getSelection())
			return m_isMethods.get(m_isMethodCombo.indexOf(m_isMethodCombo
					.getText()));
		if (m_radio_otherMethod.getSelection())
			return m_otherMethods.get(m_otherMethodCombo
					.indexOf(m_otherMethodCombo.getText()));
		return null;
	}

	public int getArgCount() {
		return m_argument.getItemCount();
	}

	public String getArgType(int idx) {
		return m_argument.getType(idx);
	}

	public String getArgName(int idx) {
		return m_argument.getName(idx);
	}

	public String getArgValue(int idx) {
		return m_argument.getValue(idx);
	}

	public String getAssertValue() {
		return m_assertValue.getText();
	}

	public String getMessage() {
		return m_message.getText();
	}

	public String getCorrectAmount() {
		return m_correctAmount.getText();
	}

	public void clearArgument() {
		m_argument.clearArgument();
	}

	private void setAssertValue(String value) {
		m_assertValue.setText(value);
	}

	private void setMessage(String msg) {
		m_message.setText(msg);
	}

	private void setCorrectAmount(int num) {
		m_correctAmount.setText(String.valueOf(num));
	}

	public String getDynamicComponentType() {
		return m_dynPanel.getDynamicComponentType();
	}

	public String getDynamicComponentValue() {
		return m_dynPanel.getDynamicComponentValue();
	}

	public int getDynamicComponentIndex() {
		return m_dynPanel.getDynamicComponentIndex();
	}
}
