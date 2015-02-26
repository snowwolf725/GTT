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
 * Created on 2005/5/19
 */
package gtt.macro.view.dialog;

import gtt.editor.configuration.IConfiguration;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.view.IMacroTree;
import gtt.util.refelection.FileClassLoader;
import gtt.util.refelection.ReflectionUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author 哲銘
 * 
 */
class ModelAssertPanel extends JPanel implements IDataNodePanel {
	/**
	 * default serialVerisonUID
	 */
	private static final long serialVersionUID = 1L;

	private final String LABEL_NAME = "Name:";

	private final String LABEL_DESCRIPT = "Assertion Node of Class File for test";

	private final String LABEL_DESCRIPT2 = "That is use method for starts with \"test\" at test class.";

	private final String LABEL_CLASSURL = "Class URL:";

	private final String BUTTON_BROWSER = "Browse..";

	private final String LABEL_METHOD = "Method:";

	private final String BUTTON_REFRESH = "Refresh list";

	private final String BUTTON_APPLY = "Apply";

	private final String BUTTON_RESET = "Reset";

	private JTree m_EventListTreeView = null;

	// private ModelAssertNode m_DataNode = null;
	// refactoring by S.K.Wen
	private ModelAssertNode m_DataNode = null;

	// refactoring by S.K.Wen
	public boolean isAccept(AbstractMacroNode node, JTree eventListTreeView,
			IMacroTree macroTree, DefaultMutableTreeNode parentMacroNode) {
		if (node == null)
			return false;
		if (!(node instanceof ModelAssertNode))
			return false;

		m_EventListTreeView = eventListTreeView;
		m_DataNode = (ModelAssertNode) node;
		resetUI();
		return true;
	}

	private void resetUI() {
		if (m_DataNode == null)
			return;

		// updateNameUIForNode();
		// loadMethodForComponent(m_DataNode);
		// clear
		String url = m_DataNode.getClassUrl();
		// refactoring by S.K.Wen
		// MacroModelAssertNode nodeObj =
		// (MacroModelAssertNode)m_DataNode.getUserObject();
		// String url = nodeObj.getClassUrl();
		if (url != null)// || url.compareTo("")==0)
			updateDataUIForNode();
		updateUI();
	}

	// private JLabel m_LabelNameContext=new JLabel();
	private JTextField m_TextFieldClassUrl = new JTextField();

	private JButton m_ButtonClassUrlBrowser = new JButton();

	private JComboBox m_ComboBoxMethod = new JComboBox();

	private JButton m_ButtonRefresh = new JButton();

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	public ModelAssertPanel() {
		setLayout(new BorderLayout());
		initChooser();
		// ===initial part===
		Box detailBox = Box.createVerticalBox();
		initNamePart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initClassUrlPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initMethodPart(detailBox);
		detailBox.add(Box.createVerticalStrut(5));
		initButtonPart(detailBox);
		detailBox.add(Box.createVerticalStrut(200));
		// ==================
		add(BorderLayout.CENTER, detailBox);

		initAction();
		initName();
	}

	private void initName() {
		m_TextFieldClassUrl.setName("CFAEditor_UrlText");
		m_ButtonClassUrlBrowser.setName("CFAEditor_Browser");
		m_ComboBoxMethod.setName("CFAEditor_Method");
		m_ButtonRefresh.setName("CFAEditor_Refresh");
		m_ButtonApply.setName("CFAEditor_Apply");
		m_ButtonReset.setName("CFAEditor_Reset");
	}

	protected void initChooser() {
		JFileChooser _chooser = new JFileChooser(
				IConfiguration.DEFAULT_DIRECTORY);
		_chooser.setName("File chooser");
		// _chooser.addChoosableFileFilter(new TestFileFilter("class",
		// "Class Files"));
		// TestFileView fileView = new TestFileView();
		// fileView.putIcon("class", new ImageIcon("images/javaUp.jpg"));
		// _chooser.setCurrentDirectory(new File("C:/"));
		// _chooser.setFileView(fileView);
		_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_chooser.setMultiSelectionEnabled(false);
		_chooser.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * @param detailBox
	 */
	private void initNamePart(Box detailBox) {
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory
				.createJLabel(LABEL_NAME, 80, 20, false);
		nameBox.add(labelName);
		nameBox.add(Box.createHorizontalStrut(5));
		Box contextBox = Box.createVerticalBox();
		JLabel labelNameDescript = WidgetFactory.createJLabel(LABEL_DESCRIPT,
				330, 20, false);
		contextBox.add(labelNameDescript);
		JLabel labelNameDescript2 = WidgetFactory.createJLabel(LABEL_DESCRIPT2,
				330, 20, false);
		contextBox.add(labelNameDescript2);
		nameBox.add(contextBox);
		nameBox.add(Box.createHorizontalGlue());
		detailBox.add(nameBox);
	}

	/**
	 * @param detailBox
	 */
	private void initClassUrlPart(Box detailBox) {
		Box classUrlBox = Box.createHorizontalBox();
		classUrlBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel(LABEL_CLASSURL, 80, 20,
				false);
		classUrlBox.add(labelName);
		classUrlBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJTextComponent(m_TextFieldClassUrl, "", 330, 20);
		classUrlBox.add(m_TextFieldClassUrl);
		classUrlBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonClassUrlBrowser, BUTTON_BROWSER,
				100, 20);
		classUrlBox.add(m_ButtonClassUrlBrowser);
		classUrlBox.add(Box.createHorizontalGlue());
		detailBox.add(classUrlBox);
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
		WidgetFactory.setupSize(m_ComboBoxMethod, 330, 20);
		methodBox.add(m_ComboBoxMethod);
		methodBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonRefresh, BUTTON_REFRESH, 100, 20);
		methodBox.add(m_ButtonRefresh);
		methodBox.add(Box.createHorizontalGlue());
		detailBox.add(methodBox);
	}

	/**
	 * @param detailBox
	 */
	private void initButtonPart(Box detailBox) {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(90));
		WidgetFactory.setupJButton(m_ButtonApply, BUTTON_APPLY, 80, 20);
		buttonBox.add(m_ButtonApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, BUTTON_RESET, 80, 20);
		buttonBox.add(m_ButtonReset);
		buttonBox.add(Box.createHorizontalGlue());
		detailBox.add(buttonBox);
	}

	public void initAction() {
		m_ButtonClassUrlBrowser.addActionListener(new AbstractAction(
				"SelectGetMethodRButton") {
			private static final long serialVersionUID = -8983559515098651353L;

			public void actionPerformed(ActionEvent e) {
				doBrowser();
			}
		});
		m_ButtonRefresh.addActionListener(new AbstractAction(
				"SelectIsMethodRButton") {
			private static final long serialVersionUID = 2496509979262874122L;

			public void actionPerformed(ActionEvent e) {
				doRefresh();
			}
		});
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataToNode();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDataUIForNode();
			}
		});
	}

	/** 由檔案總管取得外部測試檔案 */
	protected void doBrowser() {
		JFileChooser _chooser = new JFileChooser(
				IConfiguration.DEFAULT_DIRECTORY);

		_chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int retval = _chooser.showDialog(null, null);

		if (retval != JFileChooser.APPROVE_OPTION)
			return;

		String url = _chooser.getSelectedFile().getPath();

		String classPath = url.substring(0, url.lastIndexOf(".class")); // 去掉.
		// class副檔名

		Class<?> c = loadClass(classPath);
		if (c == null)
			return;

		m_TextFieldClassUrl.setText(classPath);
		loadMethodInComboBox(c);
	}

	/** 由Class檔案，更新m_ComboBoxMethod中的函式列表 */
	protected boolean doRefresh() {
		m_ComboBoxMethod.removeAllItems();
		if (m_TextFieldClassUrl.getText().equals(""))
			return false;
		Class<?> c = loadClass(m_TextFieldClassUrl.getText());
		if (c == null)
			return false;
		loadMethodInComboBox(c);
		return true;
	}

	protected void loadMethodInComboBox(Class<?> c) {
		m_ComboBoxMethod.removeAllItems();
		List<Method> methodTest = ReflectionUtil
				.getMethodsStartsWith(c, "test");

		Class<?>[] returnType = { Test.class, TestSuite.class };

		// 將 test suite 中的所有test全拿出來，跟 methodTest 放在一起
		List<Method> methodTestSuite = ReflectionUtil.getMethodsReturnWith(c,
				returnType);
		for (int i = 0; i < methodTestSuite.size(); i++)
			methodTest.add(methodTestSuite.get(i));

		if (methodTest.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"There is no any test methods (start with \"test\") in the "
							+ m_TextFieldClassUrl.getText(),
					"Refresh test class file fail", JOptionPane.YES_OPTION);
			return;
		}

		for (int i = 0; i < methodTest.size(); i++) {
			m_ComboBoxMethod.addItem(methodTest.get(i));
		}
	}

	protected Class<?> loadClass(String url) {
		Class<?> c = null;
		try {
			System.out.println(url);
			c = new FileClassLoader().loadFile(url + ".class");
		} catch (ClassNotFoundException ce) {
			JOptionPane.showMessageDialog(null, "Url of file can't be found!\n"
					+ m_TextFieldClassUrl.getText(), "Load test class error",
					JOptionPane.YES_OPTION);
		}
		if (c == null) {
			JOptionPane.showMessageDialog(null, "Url of file can't be found!\n"
					+ m_TextFieldClassUrl.getText(), "Load test class error",
					JOptionPane.YES_OPTION);
			return null;
		}
		return c;
	}

	/**
	 *
	 */
	protected void updateDataToNode() {
		String classUrl = m_TextFieldClassUrl.getText();

		Class<?> c = loadClass(classUrl);

		if (c == null) {
			JOptionPane.showMessageDialog(null,
					"Url of file can't be found!\nURL:"
							+ m_TextFieldClassUrl.getText()
							+ "\nApply to node failed!",
					"Load test class error", JOptionPane.YES_OPTION);
			return;
		}

		if (m_ComboBoxMethod.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(null,
					"Method is null\nApply to node failed!", "Error",
					JOptionPane.YES_OPTION);
			return;
		}

		Method method = (Method) m_ComboBoxMethod.getSelectedItem();

		m_DataNode.setInfo(classUrl, method.getName());
		// refactoring by S.K.Wen
		// MacroModelAssertNode nodeObj =
		// (MacroModelAssertNode)m_DataNode.getUserObject();
		// nodeObj.setClassUrlAndMethod(classUrl, method.getName());
		m_EventListTreeView.updateUI();
	}

	/**
	 *
	 */
	protected void updateDataUIForNode() {
		if (m_DataNode == null)
			return;

		if (m_DataNode.getClassUrl() == null)
			return;
		// refactoring by S.K.Wen
		// MacroModelAssertNode nodeObj =
		// (MacroModelAssertNode)m_DataNode.getUserObject();
		//
		// if(nodeObj.getClassUrl() == null)
		// return;

		m_TextFieldClassUrl.setText(m_DataNode.getClassUrl());
		// refactoring by S.K.Wen
		// m_TextFieldClassUrl.setText(nodeObj.getClassUrl());
		if (doRefresh() == false)
			return;
		String methodName = m_DataNode.getMethod();
		// refactoring by S.K.Wen
		// String methodName = nodeObj.getMethod();
		for (int i = 0; i < m_ComboBoxMethod.getItemCount(); ++i) {
			String itemName = m_ComboBoxMethod.getItemAt(i).toString();
			if (itemName.equals(methodName)) {
				m_ComboBoxMethod.setSelectedIndex(i);
				// i=m_ComboBoxMethod.getItemCount();
				break; // exit for
			}
		}
		updateUI();
	}
}
