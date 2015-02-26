/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.editor.ui.ArgumentPanel;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class AssertPanel implements ModifyListener {
	private Text m_txt_assertValue = null;

	private Text m_txt_assertMsg = null;

	private ArgumentPanel m_argsPanel = null;

	private Combo m_com_getMethodBox = null;

	private Combo m_com_isMethodBox = null;

	private Combo m_com_othersMethodBox = null;

	private Button m_radio_getMethodBox;

	private Button m_radio_isMethodBox;

	private Button m_radio_othersMethodBox;

	private Combo m_com_comptype;

	private Assertion m_assertion;

	public AssertPanel(Composite assertInfoComp, Assertion assertion,
			Combo com_comptype) {
		/* Setup Layout */
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 1;
		GridData data = new GridData();
		data.widthHint = 300;
		data.heightHint = 20;
		final Composite area = new Composite(assertInfoComp, SWT.NULL);
		area.setLayout(gridlayout);
		final Composite area2 = new Composite(assertInfoComp, SWT.NULL);
		area2.setLayout(gridlayout2);
		/* store reference */
		m_assertion = assertion;
		m_com_comptype = com_comptype;
		/* Assert Methods */
		createAssertMethodCombo(area);
		Method m = assertion.getMethod();
		if (m != null) {
			if (m.getName().startsWith("get")) {
				m_radio_getMethodBox.setSelection(true);
				m_com_getMethodBox.setText(ReflectionUtil.getStringOfMethod(m));
			} else if (m.getName().startsWith("is")) {
				m_radio_isMethodBox.setSelection(true);
				m_com_isMethodBox.setText(ReflectionUtil.getStringOfMethod(m));
			} else {
				m_radio_othersMethodBox.setSelection(true);
				m_com_othersMethodBox.setText(ReflectionUtil
						.getStringOfMethod(m));
			}
		}

		/* Assert value */
		final Label lbl_assertvalue = new Label(area, SWT.NULL);
		lbl_assertvalue.setText("Assert Value:");
		m_txt_assertValue = new Text(area, SWT.BORDER);
		m_txt_assertValue.setLayoutData(data);
		if (assertion != null) {
			m_txt_assertValue.setText(assertion.getValue());
		}

		/* Assert Message */
		final Label lbl_assertmsg = new Label(area, SWT.NULL);
		lbl_assertmsg.setText("Assert Message:");
		m_txt_assertMsg = new Text(area, SWT.BORDER);
		m_txt_assertMsg.setLayoutData(data);
		if (assertion != null) {
			m_txt_assertMsg.setText(assertion.getMessage());
		}

		/* Assert Args */
		m_argsPanel = new ArgumentPanel(area2, false, ArgumentPanel.ALL_BUTTON);
		Iterator<Argument> ite = assertion.getArguments().iterator();
		while (ite.hasNext()) {
			m_argsPanel.addArgument(ite.next());
		}
	}

	// refactoy by zwshen: extract class
	class MethodBoxSelectionListener implements SelectionListener {

		public MethodBoxSelectionListener(boolean bGet, boolean bIs,
				boolean bOther) {
			bGetMethod = bGet;
			bIsMethod = bIs;
			bOtherMethod = bOther;
		}

		private boolean bGetMethod = true;
		private boolean bIsMethod = true;
		private boolean bOtherMethod = true;

		public void widgetSelected(SelectionEvent e) {
			/* �]�w radio Button */
			m_radio_getMethodBox.setSelection(bGetMethod);
			m_radio_isMethodBox.setSelection(bIsMethod);
			m_radio_othersMethodBox.setSelection(bOtherMethod);
			/* �]�w�Ѽƭ��O */
			Method m = getAssertMethodFromString(m_com_comptype.getText());
			Class<?>[] args = m.getParameterTypes();
			m_argsPanel.clearArgument();
			for (Class<?> arg : args) {
				Argument a = Argument.create("", arg.getName(), "");
				m_argsPanel.addArgument(a);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}

	}

	private void createAssertMethodCombo(final Composite area) {
		// create GUI layout
		m_radio_getMethodBox = new Button(area, SWT.RADIO);
		m_radio_getMethodBox.setText("Method Start With get:");
		m_com_getMethodBox = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_getMethodBox.setVisibleItemCount(10);
		m_com_getMethodBox.addSelectionListener(new MethodBoxSelectionListener(
				true, false, false));
		m_radio_isMethodBox = new Button(area, SWT.RADIO);
		m_radio_isMethodBox.setText("Method Start With is:");
		m_com_isMethodBox = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_isMethodBox.setVisibleItemCount(10);
		m_com_isMethodBox.addSelectionListener(new MethodBoxSelectionListener(
				false, true, false));

		m_radio_othersMethodBox = new Button(area, SWT.RADIO);
		m_radio_othersMethodBox.setText("Other Method:");
		m_com_othersMethodBox = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_othersMethodBox.setVisibleItemCount(10);
		m_com_othersMethodBox
				.addSelectionListener(new MethodBoxSelectionListener(false,
						false, true));

		AddAssertMethodToCombo(m_com_comptype.getText());
	}

	/**
	 * 
	 */
	private void AddAssertMethodToCombo(String classname) {
		// add assert method to GUI
		try {
			if (!classname.equals("")) {
				Class<?> _class = Class.forName(classname);
				List<Method> methodGet = ReflectionUtil.getMethodsStartsWith(
						_class, "get");
				List<Method> methodIs = ReflectionUtil.getMethodsStartsWith(
						_class, "is");
				List<Method> methodOthers = ReflectionUtil
						.getMethodsStartsWith(_class, "others");
				boolean firstmethod = true;
				for (int i = 0; i < methodGet.size(); i++) {
					Method m = (Method) methodGet.get(i);
					m_com_getMethodBox.add(ReflectionUtil.getStringOfMethod(m));
					if (firstmethod == true) {
						m_com_getMethodBox.setText(ReflectionUtil
								.getStringOfMethod(m));
						firstmethod = false;
					}
				}
				firstmethod = true;
				for (int i = 0; i < methodIs.size(); i++) {
					Method m = (Method) methodIs.get(i);
					m_com_isMethodBox.add(ReflectionUtil.getStringOfMethod(m));
					if (firstmethod == true) {
						m_com_isMethodBox.setText(ReflectionUtil
								.getStringOfMethod(m));
						firstmethod = false;
					}
				}
				firstmethod = true;
				for (int i = 0; i < methodOthers.size(); i++) {
					Method m = (Method) methodOthers.get(i);
					m_com_othersMethodBox.add(ReflectionUtil
							.getStringOfMethod(m));
					if (firstmethod == true) {
						m_com_othersMethodBox.setText(ReflectionUtil
								.getStringOfMethod(m));
						firstmethod = false;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Method getAssertMethodFromString(String classname) {
		try {
			if (classname.equals(""))
				return null;
			Class<?> _class = Class.forName(classname);
			List<Method> methods = null;
			String methodname = null;
			if (m_radio_getMethodBox.getSelection()) {
				methods = ReflectionUtil.getMethodsStartsWith(_class, "get");
				methodname = m_com_getMethodBox.getText();
			} else if (m_radio_isMethodBox.getSelection()) {
				methods = ReflectionUtil.getMethodsStartsWith(_class, "is");
				methodname = m_com_isMethodBox.getText();
			} else {
				methods = ReflectionUtil.getMethodsStartsWith(_class, "others");
				methodname = m_com_othersMethodBox.getText();
			}

			if (methods == null || methodname == null)
				return null;

			Iterator<Method> ite = methods.iterator();
			while (ite.hasNext()) {
				Method m = (Method) ite.next();
				if (ReflectionUtil.getStringOfMethod(m).equals(methodname))
					return m;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
	 * .ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		// TODO Auto-generated method stub
		m_com_getMethodBox.removeAll();
		m_com_isMethodBox.removeAll();
		m_com_othersMethodBox.removeAll();
		Object obj = e.getSource();
		Combo com_comptype = null;
		if (obj instanceof Combo) {
			com_comptype = (Combo) obj;
		}
		String classname = com_comptype.getText();
		AddAssertMethodToCombo(classname);
	}

	public Assertion update() {
		m_assertion.setMessage(m_txt_assertMsg.getText());
		m_assertion.setValue(m_txt_assertValue.getText());
		Method m = getAssertMethodFromString(m_com_comptype.getText());
		m_assertion.setMethod(m);
		// clean args and add new args
		m_assertion.getArguments().clear();

		for (int i = 0; i < m_argsPanel.getItemCount(); i++) {
			Argument a = Argument.create(m_argsPanel.getType(i), m_argsPanel
					.getName(i), m_argsPanel.getValue(i));
			m_assertion.getArguments().add(a);
		}
		return m_assertion;
	}
}
