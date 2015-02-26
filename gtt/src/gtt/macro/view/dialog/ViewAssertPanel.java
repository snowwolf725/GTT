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
/*
 * Created on 2005/4/13
 */
package gtt.macro.view.dialog;

import gtt.editor.view.BoxTableView;
import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.view.IMacroTree;
import gtt.oracle.AssertionChecker;
import gtt.util.refelection.ReflectionUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author 哲銘 這個class是節點編輯介面的class,用來編輯一個元件驗證節點
 */
class ViewAssertPanel extends JPanel implements IDataNodePanel {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private final String LABEL_DESCRIPT = "Assertion Node of Basic Component";

	private final String LABEL_METHOD = "Method:";

	private final String LABEL_AUGMENT = "Argment:";

	private final String LABEL_ASSERT = "Assert Value:";

	private final String VALUE_BOOLEAN = "booleanValue";

	private final String LABEL_MESSAGE = "Message:";

	private JTree m_curMacroEventTree = null;

	private ViewAssertNode m_vaNode = null;

	private List<Method> m_getMethods = null;
	private List<Method> m_isMethods = null;
	private List<Method> m_otherMethods = null;

	public boolean isAccept(AbstractMacroNode data, JTree cur_tree,
			IMacroTree outerMacroJTree,
			DefaultMutableTreeNode cur_MacroEventRoot) {

		if (data == null)
			return false;
		if (!(data instanceof ViewAssertNode))
			return false;

		m_vaNode = (ViewAssertNode) data;
		m_curMacroEventTree = cur_tree;
		initUI();
		return true;
	}

	private void initUI() {
		if (m_vaNode == null)
			return;

		updateNameUIForNode();
		loadMethodForComponent();

		// clear
		if (m_vaNode.getAssertion().getMethodName().compareTo("") == 0) {
			// resetAll();
			enableMethodComboBox();
		} else
			updateDataUIForNode();

		updateUI();
	}

	private JLabel m_LabelNameContext = new JLabel();

	private JRadioButton m_RadioBtnGet = new JRadioButton();

	private JRadioButton m_RadioBtnIs = new JRadioButton();

	private JRadioButton m_RadioBtnOther = new JRadioButton();

	private JComboBox m_cbGetMethod = new JComboBox();

	private JComboBox m_cbIsMethod = new JComboBox();

	private JComboBox m_cbOtherMethod = new JComboBox();

	private BoxTableView m_BoxTable = null;

	private JTextField m_TextFieldAssertValue = new JTextField();

	private JComboBox m_ComboBoxAssertValueBoolean = null;

	private CardLayout m_cardManagerValue = new CardLayout();

	private JPanel m_cardPanelValue = new JPanel();

	private JTextField m_TextFieldMessage = new JTextField();

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	public ViewAssertPanel() {
		setLayout(new BorderLayout());

		// ===initial part===
		Box detailBox = Box.createVerticalBox();
		initNamePart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initMethodPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initArgumentPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initAssertPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initMessagePart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initButtonPart(detailBox);
		detailBox.add(Box.createVerticalStrut(200));
		// ==================
		add(BorderLayout.CENTER, detailBox);
		// m_ComboBoxAssertMethodGet.setSelectedIndex(0);

		initAction();
		initName();
	}

	private void initName() {
		m_LabelNameContext.setName("CAEditor_LabelName");
		m_RadioBtnGet.setName("CAEditor_BtnGet");
		m_RadioBtnIs.setName("CAEditor_BtnIs");
		m_RadioBtnOther.setName("CAEditor_BtnOther");
		m_cbGetMethod.setName("CAEditor_MethodGet");
		m_cbIsMethod.setName("CAEditor_MethodIs");
		m_cbOtherMethod.setName("CAEditor_MethodOther");
		m_BoxTable.setName("CAEditor_Argument");
		m_TextFieldAssertValue.setName("CAEditor_AssertValueText");
		m_ComboBoxAssertValueBoolean.setName("CAEditor_AssertValueBoolean");
		m_TextFieldMessage.setName("CAEditor_MessageText");
		m_ButtonApply.setName("CAEditor_Apply");
		m_ButtonReset.setName("CAEditor_Reset");
	}

	/**
	 * @param detailBox
	 */
	private void initNamePart(Box detailBox) {
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel("Assert", 80, 20, false);
		nameBox.add(labelName);
		nameBox.add(Box.createHorizontalStrut(5));
		Box contextBox = Box.createVerticalBox();
		JLabel labelNameDescript = WidgetFactory.createJLabel(LABEL_DESCRIPT,
				330, 20, false);
		contextBox.add(labelNameDescript);
		m_LabelNameContext = WidgetFactory.createJLabel("", 330, 20, false);
		contextBox.add(m_LabelNameContext);
		nameBox.add(contextBox);
		nameBox.add(Box.createHorizontalGlue());
		detailBox.add(nameBox);
	}

	/**
	 * @param detailBox
	 */
	private void initMethodPart(Box detailBox) {
		Box methodBox = Box.createHorizontalBox();
		methodBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel(LABEL_METHOD, 80, 20,
				false);
		methodBox.add(labelName);
		methodBox.add(Box.createHorizontalStrut(5));
		Box contextBox = Box.createVerticalBox();
		Box getBox = Box.createHorizontalBox();
		Box isBox = Box.createHorizontalBox();
		Box otherBox = Box.createHorizontalBox();
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

		getBox.add(m_RadioBtnGet);
		getBox.add(m_cbGetMethod);
		isBox.add(m_RadioBtnIs);
		isBox.add(m_cbIsMethod);
		otherBox.add(m_RadioBtnOther);
		otherBox.add(m_cbOtherMethod);
		contextBox.add(getBox);
		contextBox.add(isBox);
		contextBox.add(otherBox);
		methodBox.add(contextBox);
		methodBox.add(Box.createHorizontalGlue());
		detailBox.add(methodBox);
	}

	/**
	 * @param detailBox
	 */
	private void initArgumentPart(Box detailBox) {

		Box argBox = Box.createHorizontalBox();
		argBox.add(Box.createHorizontalStrut(5));
		JLabel labelArg = WidgetFactory.createJLabel(LABEL_AUGMENT, 80, 20,
				false);
		argBox.add(labelArg);
		argBox.add(Box.createHorizontalStrut(5));

		m_BoxTable = BoxTableView.createArgumentBoxView();
		m_BoxTable.setMaxHeight(50);
		argBox.add(m_BoxTable.getView());
		argBox.add(Box.createHorizontalGlue());
		detailBox.add(argBox);
	}

	/**
	 * @param detailBox
	 */
	private void initAssertPart(Box detailBox) {
		Box valueBox = Box.createHorizontalBox();
		valueBox.add(Box.createHorizontalStrut(5));
		JLabel labelAssert = WidgetFactory.createJLabel(LABEL_ASSERT, 80, 20,
				false);
		valueBox.add(labelAssert);
		valueBox.add(Box.createHorizontalStrut(5));
		valueBox.add(m_TextFieldAssertValue);
		valueBox.add(Box.createHorizontalGlue());

		Box booleanBox = Box.createHorizontalBox();
		booleanBox.add(Box.createHorizontalStrut(5));
		JLabel labelAssertBoolean = WidgetFactory.createJLabel(LABEL_ASSERT,
				80, 20, false);
		booleanBox.add(labelAssertBoolean);
		booleanBox.add(Box.createHorizontalStrut(5));
		m_ComboBoxAssertValueBoolean = new JComboBox(AssertionChecker.BooleanItem);
		booleanBox.add(m_ComboBoxAssertValueBoolean);
		booleanBox.add(Box.createHorizontalGlue());

		m_cardPanelValue.setLayout(m_cardManagerValue);
		m_cardPanelValue.add(valueBox, "Value");
		m_cardPanelValue.add(booleanBox, VALUE_BOOLEAN);
		detailBox.add(m_cardPanelValue);
	}

	/**
	 * @param detailBox
	 */
	private void initMessagePart(Box detailBox) {
		Box messageBox = Box.createHorizontalBox();
		messageBox.add(Box.createHorizontalStrut(5));
		JLabel labelMessage = WidgetFactory.createJLabel(LABEL_MESSAGE, 80, 20,
				false);
		messageBox.add(labelMessage);
		messageBox.add(Box.createHorizontalStrut(5));
		messageBox.add(m_TextFieldMessage);
		messageBox.add(Box.createHorizontalGlue());
		detailBox.add(messageBox);
	}

	/**
	 * @param detailBox
	 */
	private void initButtonPart(Box detailBox) {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(90));
		WidgetFactory.setupJButton(m_ButtonApply, "Apply", 80, 20);
		buttonBox.add(m_ButtonApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, "Reset", 80, 20);
		buttonBox.add(m_ButtonReset);
		buttonBox.add(Box.createHorizontalGlue());
		detailBox.add(buttonBox);
	}

	// private void loadMethodForComponent(ViewAssertNode dataNode) {
	private void loadMethodForComponent() {
		// 從class 檔讀取不同的methods
		try {
			loadingMethods(Class.forName(m_vaNode.getComponent().getType()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 移掉所有ComboBox的資料，要重新加入
		removeAllComboBox();

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
	private void removeAllComboBox() {
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
				updateArgumentUI();
			}
		});
		m_RadioBtnIs.addActionListener(new AbstractAction(
				"SelectIsMethodRButton") {
			public void actionPerformed(ActionEvent e) {
				enableMethodComboBox();
				updateArgumentUI();
			}
		});
		m_RadioBtnOther.addActionListener(new AbstractAction(
				"SelectOtherMethodRButton") {
			public void actionPerformed(ActionEvent e) {
				enableMethodComboBox();
				updateArgumentUI();
			}
		});
		m_cbGetMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentUI();
			}
		});
		m_cbIsMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentUI();
			}
		});
		m_cbOtherMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateArgumentUI();
			}
		});
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doApply();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataUIForNode();
			}
		});
	}

	protected void enableMethodComboBox() {
		boolean bGet = false;
		boolean bIs = false;
		boolean bOther = false;
		if (m_RadioBtnGet.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, "Value");
			bGet = true;
		}

		if (m_RadioBtnIs.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, VALUE_BOOLEAN);
			bIs = true;
		}

		if (m_RadioBtnOther.isSelected()) {
			m_cardManagerValue.show(m_cardPanelValue, "Value");
			bOther = true;
		}
		m_cbGetMethod.setEnabled(bGet);
		m_cbIsMethod.setEnabled(bIs);
		m_cbOtherMethod.setEnabled(bOther);
	}

	private void updateArgumentUI() {
		String selectedMethod = getSelectedMethod();
		if (selectedMethod == null)
			return;

		String argString = selectedMethod.substring(
				selectedMethod.indexOf("("), selectedMethod.indexOf(")"));

		Vector<String> argVector = genArguments(argString);

		m_BoxTable.removeAll();
		for (int i = 0; i < argVector.size(); ++i) {
			String sarg = argVector.get(i).toString();
			m_BoxTable.addRow(getArg(sarg));
		}

		updateUI();
	}

	private Vector<JComponent> getArg(String s) {
		JLabel argType = new JLabel(s);
		JComponent argValue;
		if (s.compareTo("boolean") == 0)
			argValue = new JComboBox(AssertionChecker.BooleanItem);
		else
			argValue = new JTextField();
		Vector<JComponent> aArg = new Vector<JComponent>();
		aArg.add(argType);
		aArg.add(argValue);
		aArg.add(argType);	//type==name
		return aArg;
	}

	// move method from updateArgumentUI
	// zws 2006/10/19
	private Vector<String> genArguments(String argString) {
		Vector<String> argVector = new Vector<String>();
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
				argVector.add(AssertionChecker.TypeItems[typeIndex]);
				matchLocation = argString.indexOf(AssertionChecker.TypeItems[typeIndex]);
				argString = argString.substring(matchLocation
						+ AssertionChecker.TypeItems[typeIndex].length(), argString
						.length());
			}
		}
		return argVector;
	}

	// move method from updateArgumentUI
	// zws 2006/10/19
	private String getSelectedMethod() {
		if (m_RadioBtnGet.isSelected()) {
			if (m_cbGetMethod.getSelectedItem() != null)
				return m_cbGetMethod.getSelectedItem().toString();
		} else if (m_RadioBtnIs.isSelected()) {
			if (m_cbIsMethod.getSelectedItem() != null)
				return m_cbIsMethod.getSelectedItem().toString();
		} else if (m_RadioBtnOther.isSelected()) {
			if (m_cbOtherMethod.getSelectedItem() != null)
				return m_cbOtherMethod.getSelectedItem().toString();
		}

		return null;
	}

	private void doApply() {
		m_vaNode.getAssertion().setMethodName(getMethodName());
		m_vaNode.getAssertion().setValue(getAssertionValue());
		m_vaNode.getAssertion().setMessage(m_TextFieldMessage.getText());

		applyArguments();

		m_curMacroEventTree.updateUI();
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private String getAssertionValue() {
		if (m_RadioBtnIs.isSelected()) // true/false
			return m_ComboBoxAssertValueBoolean.getSelectedItem().toString();

		return m_TextFieldAssertValue.getText();
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private void applyArguments() {
		m_vaNode.getArguments().clear();
		for (int i = 0; i < m_BoxTable.getRowCount(); ++i) {
			String type = m_BoxTable.getValue(i, 0);
			String value = m_BoxTable.getValue(i, 1);
			String name = m_BoxTable.getValue(i, 2);
			Argument arg = Argument.create(type, name, value);
			m_vaNode.getArguments().add(arg);
		}
	}

	// move method from updateDataToNode
	// zws 2006/10/19
	private String getMethodName() {
		if (m_RadioBtnGet.isSelected()) {
			int i = m_cbGetMethod.getSelectedIndex();
			return (((Method) m_getMethods.get(i)).getName());
		} else if (m_RadioBtnIs.isSelected()) {
			int i = m_cbIsMethod.getSelectedIndex();
			return (((Method) m_isMethods.get(i)).getName());
		} else if (m_RadioBtnOther.isSelected()) {
			int i = m_cbOtherMethod.getSelectedIndex();
			return ((Method) m_otherMethods.get(i)).getName();
		}

		return ""; // empty string
	}

	private void updateDataUIForNode() {
		String method = m_vaNode.getAssertion().getMethodName();
		List<String> argVector = m_vaNode.getArguments().types();

		JComboBox comboBox = null;
		if (method.indexOf("get") >= 0) {
			comboBox = m_cbGetMethod;
			m_RadioBtnGet.setSelected(true);
		} else if (method.indexOf("is") >= 0) {
			comboBox = m_cbIsMethod;
			m_RadioBtnIs.setSelected(true);
		} else {
			comboBox = m_cbOtherMethod;
			m_RadioBtnOther.setSelected(true);
		}
		int i = 0, indexOfMethod = -1;
		while (i < comboBox.getItemCount()) {
			String methodAtBox = comboBox.getItemAt(i).toString();
			if (methodAtBox.indexOf(method) >= 0) {
				int newNameStart = methodAtBox.indexOf(method)
						+ method.length();
				int newNameEnd = methodAtBox.indexOf(":"); // 去掉return type
				String arg_type_string = new String(methodAtBox.substring(
						newNameStart, newNameEnd));
				System.out.println("Method Argument:" + arg_type_string);
				boolean isWrong = false;
				int j = 0;
				for (j = 0; isWrong == false && j < argVector.size(); ++j) {
					int s = arg_type_string
							.indexOf(argVector.get(j).toString());
					if (s >= 0)
						arg_type_string = arg_type_string.substring(s
								+ argVector.get(j).toString().length(),
								arg_type_string.length());
					else
						isWrong = true;
				}
				if (isWrong == false && j == argVector.size()) {
					for (int k = 0; k < AssertionChecker.TypeItems.length; ++k) {
						if (arg_type_string.indexOf(AssertionChecker.TypeItems[k]) >= 0)
							isWrong = true;
					}
					if (isWrong == false) {
						indexOfMethod = i;
						i = comboBox.getItemCount();
					}
				}
			}
			i++;
		}
		if (indexOfMethod == -1) {
			System.out
					.println("Error ! at Class Panel_DetailEditor_ComponentAssert:");
			System.out.println("Can't found Method:<" + method + "> for "
					+ m_vaNode.getComponent().getType());
			indexOfMethod = 0;
		}
		comboBox.setSelectedIndex(indexOfMethod);
		enableMethodComboBox();
		updateArgumentUI();
		List<String> valueVector = m_vaNode.getArguments().values();
		for (i = 0; i < valueVector.size(); ++i) {
			m_BoxTable.setValue(valueVector.get(i).toString(), i, 1);
		}
		/*
		 * for(i=0;i<valueVector.size();++i){
		 * m_ArgTableModel.setValueAt(valueVector.get(i),i,1); }
		 */
		if (m_RadioBtnIs.isSelected()) {
			if (m_vaNode.getAssertion().getValue().compareTo("true") == 0)
				m_ComboBoxAssertValueBoolean.setSelectedIndex(0);
			else
				m_ComboBoxAssertValueBoolean.setSelectedIndex(1);
		} else {
			m_TextFieldAssertValue.setText(m_vaNode.getAssertion().getValue());
		}

		m_TextFieldMessage.setText(m_vaNode.getAssertion().getMessage());
		updateUI();
	}

	private void updateNameUIForNode() {
		if (m_vaNode == null)
			return;
		String context = new String();
		List<String> comPath = m_vaNode.getPath().list();
		for (int i = 0; i < comPath.size(); ++i) {
			String name = comPath.get(i).toString();
			int s = name.indexOf("::");
			while (s > 0) {
				name = name.substring(s + 2, name.length());
				s = name.indexOf("::");
			}
			if (i > 0)
				context += ".";
			context += name;
		}
		m_LabelNameContext.setText(context);
		m_LabelNameContext.updateUI();
	}
}
