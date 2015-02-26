/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.editor.view.dialog;

import gtt.editor.view.BoxTableView;
import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.oracle.AssertionChecker;
import gtt.testscript.ViewAssertNode;
import gtt.util.refelection.ReflectionUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author zwshen
 */
class ViewAssertPanel extends JPanel {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private final String LABEL_METHOD = "Method:";

	private final String LABEL_AUGMENT = "Argument:";

	private final String LABEL_AUGTYPE = "Type";

	private final String LABEL_AUGVALUE = "Value";

	private final String LABEL_ASSERT = "Assert Value:";

	private final String VALUE_BOOLEAN = "booleanValue";

	private final String LABEL_MESSAGE = "Message:";

	private final int DATAUI_WIDTH_TYPE = 100;

	private final int DATAUI_WIDTH_VALUE = 100;

	private final int DATAUI_HEIGHT = 17;

	private List<Method> m_getMethods = null;

	private List<Method> m_isMethods = null;

	private List<Method> m_otherMethods = null;

	private JLabel m_LabelNameContext = new JLabel();

	private JRadioButton m_RadioBtnGet = new JRadioButton();

	private JRadioButton m_RadioBtnIs = new JRadioButton();

	private JRadioButton m_RadioBtnOther = new JRadioButton();

	private JComboBox m_cbGetMethod = new JComboBox();

	private JComboBox m_cbIsMethod = new JComboBox();

	private JComboBox m_cbOtherMethod = new JComboBox();

	private BoxTableView m_ArgumentTable = null;

	private JTextField m_TextFieldAssertValue = new JTextField();

	private JComboBox m_ComboBoxAssertValueBoolean = null;

	private CardLayout m_cardManagerValue = new CardLayout();

	private JPanel m_cardPanelValue = new JPanel();

	private JTextField m_TextFieldMessage = new JTextField();

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	private JComboBox m_cbCompareOperator = null;

	private gtt.testscript.ViewAssertNode m_ViewAssertNode;

	private void initUI() {
		if (m_ViewAssertNode == null)
			return;
		updateNameLabel();
		initMethodDropDownLists();
		if (m_ViewAssertNode.getAssertion().getMethod() == null) {
			// resetAll();
			enableMethodComboBox();
		} else
			doReset();

		updateUI();
	}

	public ViewAssertPanel(gtt.testscript.ViewAssertNode node) {
		m_ViewAssertNode = node;

		initMainLayout();
		initAction();
		initName();
		initUI();
	}

	private void initMainLayout() {
		setLayout(new BorderLayout());
		// ===initial part===
		Box detailBox = Box.createVerticalBox();
		detailBox.add(createNameBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createMethodPart());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createArgumentPart());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createAssertPart());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createMessagePart());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createButtonPart());
		detailBox.add(Box.createVerticalStrut(100));
		// ==================
		add(BorderLayout.CENTER, detailBox);
	}

	public ViewAssertNode getViewAssertNode() {
		return m_ViewAssertNode;
	}

	private void initName() {
		m_LabelNameContext.setName("ViewAssertPanel_LabelName");
		m_RadioBtnGet.setName("ViewAssertPanel_BtnGet");
		m_RadioBtnIs.setName("ViewAssertPanel_BtnIs");
		m_RadioBtnOther.setName("ViewAssertPanel_BtnOther");
		m_cbGetMethod.setName("ViewAssertPanel_MethodGet");
		m_cbIsMethod.setName("ViewAssertPanel_MethodIs");
		m_cbOtherMethod.setName("ViewAssertPanel_MethodOther");
		m_ArgumentTable.setName("ViewAssertPanel_Argument");
		m_TextFieldAssertValue.setName("ViewAssertPanel_AssertValueText");
		m_ComboBoxAssertValueBoolean
				.setName("ViewAssertPanel_AssertValueBoolean");
		m_TextFieldMessage.setName("ViewAssertPanel_MessageText");
		m_ButtonApply.setName("ViewAssertPanel_Apply");
		m_ButtonReset.setName("ViewAssertPanel_Reset");
	}

	private Box createNameBox() {
		Box nameBox = Box.createHorizontalBox();
		// nameBox.add(Box.createHorizontalStrut(5));
		nameBox.add(WidgetFactory.createJLabel("Assert", 80, 20, false));
		nameBox.add(Box.createHorizontalStrut(5));
		m_LabelNameContext = WidgetFactory.createJLabel("", 330, 20, false);
		Box contextBox = Box.createVerticalBox();
		contextBox.add(m_LabelNameContext);
		nameBox.add(contextBox);
		nameBox.add(Box.createHorizontalGlue());
		return nameBox;
	}

	private Box createMethodPart() {
		Box methodBox = Box.createHorizontalBox();
		methodBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel(LABEL_METHOD, 80, 20,
				false);
		methodBox.add(labelName);
		methodBox.add(Box.createHorizontalStrut(5));

		m_RadioBtnGet.setText("get:");
		m_RadioBtnIs.setText("is:");
		m_RadioBtnOther.setText("other:");
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(m_RadioBtnGet);
		btnGroup.add(m_RadioBtnIs);
		btnGroup.add(m_RadioBtnOther);
		m_RadioBtnGet.setSelected(true);
		m_cbIsMethod.setEnabled(false);
		m_cbOtherMethod.setEnabled(false);

		Box getBox = Box.createHorizontalBox();
		getBox.add(m_RadioBtnGet);
		getBox.add(m_cbGetMethod);

		Box isBox = Box.createHorizontalBox();
		isBox.add(m_RadioBtnIs);
		isBox.add(m_cbIsMethod);

		Box otherBox = Box.createHorizontalBox();
		otherBox.add(m_RadioBtnOther);
		otherBox.add(m_cbOtherMethod);

		Box contextBox = Box.createVerticalBox();
		contextBox.add(getBox);
		contextBox.add(isBox);
		contextBox.add(otherBox);
		methodBox.add(contextBox);
		methodBox.add(Box.createHorizontalGlue());
		return methodBox;
	}

	@SuppressWarnings("unchecked")
	private Box createArgumentPart() {
		Box argBox = Box.createHorizontalBox();
		argBox.add(Box.createHorizontalStrut(5));
		JLabel labelArg = WidgetFactory.createJLabel(LABEL_AUGMENT, 80, 20,
				false);
		argBox.add(labelArg);
		argBox.add(Box.createHorizontalStrut(5));

		m_ArgumentTable = new BoxTableView(getTitleNames(), getWidths(),
				DATAUI_HEIGHT);
		m_ArgumentTable.setMaxHeight(50);
		argBox.add(m_ArgumentTable.getView());
		argBox.add(Box.createHorizontalGlue());
		return argBox;
	}

	// move method from initArgumentPart()
	// zws 2006/10/19
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Vector getTitleNames() {
		Vector titleNameVector = new Vector();
		titleNameVector.add(LABEL_AUGTYPE);
		titleNameVector.add(LABEL_AUGVALUE);
		return titleNameVector;
	}

	// move method from initArgumentPart()
	// zws 2006/10/19
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Vector getWidths() {
		Vector widthVector = new Vector();
		widthVector.add(Integer.toString(DATAUI_WIDTH_TYPE));
		widthVector.add(Integer.toString(DATAUI_WIDTH_VALUE));
		return widthVector;
	}

	private JPanel createAssertPart() {
		Box valueBox = Box.createHorizontalBox();
		valueBox.add(Box.createHorizontalStrut(5));
		valueBox.add(WidgetFactory.createJLabel(LABEL_ASSERT, 80, 20, false));
		valueBox.add(Box.createHorizontalStrut(5));
		initCompareOperator();

		valueBox.add(m_cbCompareOperator);
		valueBox.add(Box.createHorizontalStrut(5));
		valueBox.add(m_TextFieldAssertValue);
		valueBox.add(Box.createHorizontalGlue());

		Box booleanBox = Box.createHorizontalBox();
		booleanBox.add(Box.createHorizontalStrut(5));
		booleanBox.add(WidgetFactory.createJLabel(LABEL_ASSERT, 80, 20, false));
		booleanBox.add(Box.createHorizontalStrut(5));
		m_ComboBoxAssertValueBoolean = new JComboBox(AssertionChecker.BooleanItem);
		booleanBox.add(m_ComboBoxAssertValueBoolean);
		booleanBox.add(Box.createHorizontalGlue());

		m_cardPanelValue.setLayout(m_cardManagerValue);
		m_cardPanelValue.add(valueBox, "value");
		m_cardPanelValue.add(booleanBox, VALUE_BOOLEAN);
		return m_cardPanelValue;
	}

	private void initCompareOperator() {
		m_cbCompareOperator = new JComboBox();
		m_cbCompareOperator.addItem("==");
		m_cbCompareOperator.addItem("!=");
		m_cbCompareOperator.addItem("<=");
		m_cbCompareOperator.addItem(">=");
		m_cbCompareOperator.addItem("<");
		m_cbCompareOperator.addItem(">");
		m_cbCompareOperator.addItem("isNull");
		m_cbCompareOperator.addItem("isNotNUll");
		m_cbCompareOperator.setSelectedItem(Assertion
				.getOperatorString(m_ViewAssertNode.getAssertion()
						.getCompareOperator()));
	}

	private Box createMessagePart() {
		Box messageBox = Box.createHorizontalBox();
		messageBox.add(Box.createHorizontalStrut(5));
		JLabel labelMessage = WidgetFactory.createJLabel(LABEL_MESSAGE, 80, 20,
				false);
		messageBox.add(labelMessage);
		messageBox.add(Box.createHorizontalStrut(5));
		messageBox.add(m_TextFieldMessage);
		messageBox.add(Box.createHorizontalGlue());
		return messageBox;
	}

	private Box createButtonPart() {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(90));
		WidgetFactory.setupJButton(m_ButtonApply, "Apply", 80, 20);
		buttonBox.add(m_ButtonApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, "Reset", 80, 20);
		buttonBox.add(m_ButtonReset);
		buttonBox.add(Box.createHorizontalGlue());
		return buttonBox;
	}

	private void initMethodDropDownLists() {
		IComponent com = m_ViewAssertNode.getComponent();

		if (com == null) {
			System.out.println("Cann't find the Component ["
					+ m_LabelNameContext.getText() + "].");
			return;
		}

		// 從class 檔讀取不同的methods
		try {
			loadingMethods(Class.forName(com.getType()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// 移掉所有ComboBox的資料，要重新加入
		removeAllBoxs();
		puttingMethods(m_cbGetMethod, m_getMethods.iterator());
		puttingMethods(m_cbIsMethod, m_isMethods.iterator());
		puttingMethods(m_cbOtherMethod, m_otherMethods.iterator());
	}

	private void puttingMethods(JComboBox box, Iterator<Method> ite) {
		while (ite.hasNext()) {
			box.addItem(ReflectionUtil.getStringOfMethod((Method) ite.next()));
		}
	}

	// move method from loadMethodForCompoennt
	// zws 2006/10/19
	private void removeAllBoxs() {
		m_cbGetMethod.removeAllItems();
		m_cbIsMethod.removeAllItems();
		m_cbOtherMethod.removeAllItems();

		m_cbGetMethod.setAutoscrolls(true);
		m_cbIsMethod.setAutoscrolls(true);
		m_cbOtherMethod.setAutoscrolls(true);
	}

	// move method from loadMethodForCompoennt
	// zws 2006/10/19
	private void loadingMethods(Class<?> comClass) {
		m_getMethods = ReflectionUtil.getMethodsStartsWith(comClass, "get");
		m_isMethods = ReflectionUtil.getMethodsStartsWith(comClass, "is");
		m_otherMethods = ReflectionUtil.getMethodsStartsWith(comClass, "others");
	}

	@SuppressWarnings( { "serial" })
	private void initAction() {
		m_RadioBtnGet.addActionListener(new AbstractAction(
				"SelectGetMethodRButton") {
			public void actionPerformed(ActionEvent e) {
				enableMethodComboBox();
				updateArgumentBoxTypes();
			}
		});
		m_RadioBtnIs.addActionListener(new AbstractAction(
				"SelectIsMethodRButton") {
			public void actionPerformed(ActionEvent e) {
				enableMethodComboBox();
				updateArgumentBoxTypes();
			}
		});
		m_RadioBtnOther.addActionListener(new AbstractAction(
				"SelectOtherMethodRButton") {
			public void actionPerformed(ActionEvent e) {
				enableMethodComboBox();
				updateArgumentBoxTypes();
			}
		});
		m_cbGetMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentBoxTypes();
			}
		});
		m_cbIsMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentBoxTypes();
			}
		});
		m_cbOtherMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentBoxTypes();
			}
		});
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAccept();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doReset();
			}
		});
	}

	protected void enableMethodComboBox() {
		boolean get = false;
		boolean is = false;
		boolean other = false;
		if (m_RadioBtnGet.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, "value");
			get = true;
		}

		if (m_RadioBtnIs.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, VALUE_BOOLEAN);
			is = true;
		}

		if (m_RadioBtnOther.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, "value");
			other = true;
		}
		m_cbGetMethod.setEnabled(get);
		m_cbIsMethod.setEnabled(is);
		m_cbOtherMethod.setEnabled(other);

	}

	private void clearAssertionUI() {
		m_TextFieldMessage.setText("");
		m_TextFieldAssertValue.setText("");
		if (m_RadioBtnIs.isSelected())
			m_ComboBoxAssertValueBoolean.setSelectedIndex(1);
	}

	@SuppressWarnings("unchecked")
	private void updateArgumentBoxTypes() {
		String selectedMethod = getSelectedMethod();
		if (selectedMethod == null)
			return;

		String argString = selectedMethod.substring(
				selectedMethod.indexOf("("), selectedMethod.indexOf(")"));

		Vector<String> argVector = genArguments(argString);

		m_ArgumentTable.removeAll();
		for (int i = 0; i < argVector.size(); ++i) {
			String sarg = argVector.get(i).toString();
			m_ArgumentTable.addRow(createArgumentBoxRow(sarg));
		}

		clearAssertionUI(); // clear message and value ui
		updateUI();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Vector createArgumentBoxRow(String s) {
		JLabel argType = new JLabel(s);
		JComponent argValue;
		if (s.compareTo("boolean") == 0)
			argValue = new JComboBox(AssertionChecker.BooleanItem);
		else
			argValue = new JTextField();
		Vector aArg = new Vector();

		aArg.add(argType);
		aArg.add(argValue);
		return aArg;
	}

	// move method from updateArgumentUI
	// zws 2006/10/19
	private Vector<String> genArguments(String argString) {
		Vector<String> vector = new Vector<String>();
		int typeIndex = 0;
		while (typeIndex != -1) {
			typeIndex = -1;
			int minIndex = argString.length();
			int matchLocation = 0;
			for (int i = 0; i < AssertionChecker.TypeItems.length; ++i) {
				matchLocation = argString.indexOf(AssertionChecker.TypeItems[i]);
				if (matchLocation >= 0 && matchLocation < minIndex) {
					minIndex = matchLocation;
					typeIndex = i;
				}
			}
			if (typeIndex > -1) {
				vector.add(AssertionChecker.TypeItems[typeIndex]);
				matchLocation = argString.indexOf(AssertionChecker.TypeItems[typeIndex]);
				argString = argString.substring(matchLocation
						+ AssertionChecker.TypeItems[typeIndex].length(), argString
						.length());
			}
		}
		return vector;
	}

	// move method from updateArgumentUI
	// zws 2006/10/19
	private String getSelectedMethod() {
		if (m_RadioBtnGet.isSelected()) {
			if (m_cbGetMethod.getSelectedItem() != null)
				return m_cbGetMethod.getSelectedItem().toString();
		}
		if (m_RadioBtnIs.isSelected()) {
			if (m_cbIsMethod.getSelectedItem() != null)
				return m_cbIsMethod.getSelectedItem().toString();
		}
		if (m_RadioBtnOther.isSelected()) {
			if (m_cbOtherMethod.getSelectedItem() != null)
				return m_cbOtherMethod.getSelectedItem().toString();
		}

		return null;
	}

	/**
	 * update data on view into model
	 */
	private void doAccept() {
		m_ViewAssertNode.getAssertion().setMethod(selectedMethod());
		m_ViewAssertNode.getAssertion()
				.setMessage(m_TextFieldMessage.getText());
		m_ViewAssertNode.getAssertion().setValue(getAsserValue());
		m_ViewAssertNode.getAssertion().setCompareOperator(
				getCompareOperatorValue());
		acceptArguments();
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private String getAsserValue() {
		if (m_RadioBtnIs.isSelected())
			return m_ComboBoxAssertValueBoolean.getSelectedItem().toString();
		return m_TextFieldAssertValue.getText();
	}

	private String getCompareOperatorValue() {
		return m_cbCompareOperator.getSelectedItem().toString();
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private void acceptArguments() {
		m_ViewAssertNode.getAssertion().getArguments().clear();
		// 更新argument 值
		for (int i = 0; i < m_ArgumentTable.getRowCount(); ++i) {
			String type = m_ArgumentTable.getValue(i, 0);
			String value = m_ArgumentTable.getValue(i, 1);
			m_ViewAssertNode.getAssertion().getArguments().add(
					Argument.create(type, type, value));
		}
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private Method selectedMethod() {
		if (m_RadioBtnGet.isSelected()) {
			int i = m_cbGetMethod.getSelectedIndex();
			return ((Method) m_getMethods.get(i));
		}

		if (m_RadioBtnIs.isSelected()) {
			int i = m_cbIsMethod.getSelectedIndex();
			return ((Method) m_isMethods.get(i));
		}

		if (m_RadioBtnOther.isSelected()) {
			int i = m_cbOtherMethod.getSelectedIndex();
			return (Method) m_otherMethods.get(i);
		}

		return null; // empty string
	}

	private void doReset() {
		Method m = m_ViewAssertNode.getAssertion().getMethod();
		if (m == null)
			return;
		String name = m.getName();
		JComboBox comboBox = getComboBoxByNmae(name);

		int indexOfMethod = -1;
		for (int idx = 0; idx < comboBox.getItemCount(); idx++) {
			String methodAtBox = comboBox.getItemAt(idx).toString();
			if (methodAtBox.indexOf(name) >= 0) {
				if (isArgumentTypeMatch(name, methodAtBox)) {
					indexOfMethod = idx;
					break;
				}
			}
			idx++;
		}

		if (indexOfMethod == -1) {
			System.out.println("Can't found Method:<" + name + "> within "
					+ m_ViewAssertNode.getComponent());
			indexOfMethod = 0;
		}

		comboBox.setSelectedIndex(indexOfMethod);
		enableMethodComboBox();
		updateArgumentBoxTypes();
		updateArgumentBoxValues();

		updateUI();
	}

	private boolean isArgumentTypeMatch(String method_name,
			String methodNameAtBox) {

		int argStartIdx = methodNameAtBox.indexOf(method_name)
				+ method_name.length();
		int argEndIdx = methodNameAtBox.indexOf(":"); // 去掉return type

		StringTokenizer st = new StringTokenizer(methodNameAtBox.substring(
				argStartIdx, argEndIdx), ",( )");
		Iterator<Argument> ite = m_ViewAssertNode.getAssertion()
				.getArguments().iterator();
		while (st.hasMoreTokens() && ite.hasNext()) {
			if (!st.nextToken().equals(((Argument) ite.next()).getType()))
				return false;
		}
		// System.out.println("isArgumentTypeMatch [" + method_name +"]-[" +
		// methodNameAtBox + "]");

		return true;
	}

	private void updateArgumentBoxValues() {
		Iterator<Argument> ite = m_ViewAssertNode.getAssertion()
				.getArguments().iterator();
		int rowIndex = 0;
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			m_ArgumentTable.setValue(a.getValue(), rowIndex++, 1);
		}

		if (m_RadioBtnIs.isSelected()) {
			if (m_ViewAssertNode.getAssertion().getValue().compareTo("true") == 0)
				m_ComboBoxAssertValueBoolean.setSelectedIndex(0);
			else
				m_ComboBoxAssertValueBoolean.setSelectedIndex(1);
		} else
			m_TextFieldAssertValue.setText(m_ViewAssertNode.getAssertion()
					.getValue());
		m_TextFieldMessage
				.setText(m_ViewAssertNode.getAssertion().getMessage());

	}

	private JComboBox getComboBoxByNmae(String name) {
		// get methods
		if (name.indexOf("get") >= 0) {
			m_RadioBtnGet.setSelected(true);
			return m_cbGetMethod;
		}
		// is methods
		if (name.indexOf("is") >= 0) {
			m_RadioBtnIs.setSelected(true);
			return m_cbIsMethod;
		}

		// other methods
		m_RadioBtnOther.setSelected(true);
		return m_cbOtherMethod;
	}

	private void updateNameLabel() {
		if (m_ViewAssertNode == null)
			return;
		m_LabelNameContext.setText(m_ViewAssertNode.toString());
	}
}
