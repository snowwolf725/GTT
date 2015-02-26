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

import gtt.editor.configuration.IConfiguration;
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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author zws
 *
 *         create at 2006/12/08
 *
 */
class ModelAssertPanel extends JPanel {
	/**
	 * default serialVerisonUID
	 */
	private static final long serialVersionUID = 1L;

	private final String LABEL_CLASSURL = "Class Path";

	private final String BUTTON_BROWSER = "Browse...";

	private final String LABEL_METHOD = "Test Method";

	private final String BUTTON_REFRESH = "Refresh list";

	private final String BUTTON_APPLY = "Apply";

	private final String BUTTON_RESET = "Reset";

	private JTextField m_TextFieldClassUrl = new JTextField();

	private JButton m_ButtonClassUrlBrowser = new JButton();

	private JComboBox m_ComboBoxMethod = new JComboBox();

	private JButton m_ButtonRefresh = new JButton();

	private JButton m_ButtonApply = new JButton();

	private JButton m_ButtonReset = new JButton();

	gtt.testscript.ModelAssertNode m_Node;

	public gtt.testscript.ModelAssertNode getModelAssertNode() {
		return m_Node;
	}

	public ModelAssertPanel(gtt.testscript.ModelAssertNode node) {
		m_Node = node;

		initLayout();
		initAction();
		initName();

		doReset();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		initChooser();
		// ===initial part===
		Box detailBox = Box.createVerticalBox();
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createClassPathBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createMethodBox());
		detailBox.add(Box.createVerticalStrut(5));
		detailBox.add(createButtonBox());
		detailBox.add(Box.createVerticalStrut(20));
		add(BorderLayout.CENTER, detailBox);
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

	private Box createClassPathBox() {
		Box classpathBox = Box.createHorizontalBox();
		classpathBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel(LABEL_CLASSURL, 80, 20,
				false);
		classpathBox.add(labelName);
		classpathBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJTextComponent(m_TextFieldClassUrl, "", 330, 20);
		classpathBox.add(m_TextFieldClassUrl);
		classpathBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonClassUrlBrowser, BUTTON_BROWSER,
				100, 20);
		classpathBox.add(m_ButtonClassUrlBrowser);
		classpathBox.add(Box.createHorizontalGlue());
		return classpathBox;
	}

	private Box createMethodBox() {
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
		// detailBox.add(methodBox);
		return methodBox;
	}

	private Box createButtonBox() {
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalStrut(90));
		WidgetFactory.setupJButton(m_ButtonApply, BUTTON_APPLY, 80, 20);
		buttonBox.add(m_ButtonApply);
		buttonBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJButton(m_ButtonReset, BUTTON_RESET, 80, 20);
		buttonBox.add(m_ButtonReset);
		buttonBox.add(Box.createHorizontalGlue());
		return buttonBox;
	}

	public void initAction() {
		m_ButtonClassUrlBrowser.addActionListener(new AbstractAction(
				"SelectGetMethodRButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				doBrowser();
			}
		});
		m_ButtonRefresh.addActionListener(new AbstractAction(
				"SelectIsMethodRButton") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				doRefresh();
			}
		});
		m_ButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doApply();
			}
		});
		m_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doReset();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadMethodInComboBox(Class c) {
		m_ComboBoxMethod.removeAllItems();
		List methodTest = ReflectionUtil.getMethodsStartsWith(c, "test");
		Class[] returnType = { Test.class, TestSuite.class };

		// 將 test suite 中的所有test全拿出來，跟 methodTest 放在一起
		List<Method> methodTestSuite = ReflectionUtil.getMethodsReturnWith(c,
				returnType);
		for (int i = 0; i < methodTestSuite.size(); i++)
			methodTest.add(methodTestSuite.get(i));

		if (methodTest.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"There is no any test method (start with \"test\") in the "
							+ m_TextFieldClassUrl.getText(), "Refresh fail",
					JOptionPane.YES_OPTION);
			return;
		}

		for (int i = 0; i < methodTest.size(); i++) {
			m_ComboBoxMethod.addItem(methodTest.get(i));
		}
	}

	protected Class<?> loadClass(String url) {
		Class<?> c = null;
		try {
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
	 * do apply when pressing apply button
	 */
	protected void doApply() {
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

		Method method = ((Method) m_ComboBoxMethod.getSelectedItem());

		// m_DataNode.setClassUrlAndMethod(classUrl, method.getName());
		m_Node.setClassPath(classUrl);
		m_Node.setAssertMethod(method);
		// m_EventListTreeView.updateUI();
	}

	/**
	 * do reset when pressing reset button
	 */
	protected void doReset() {
		if (m_Node == null)
			return;
		if (m_Node.getClassPath() == null)
			return;

		m_TextFieldClassUrl.setText(m_Node.getClassPath());

		if (!doRefresh())
			return;

		// String method_name =
		// ReflectionUtil.getStringOfMethod(m_Node.getAssertMethod());
		if (m_Node.getAssertMethod() == null) {
			m_ComboBoxMethod.setSelectedIndex(0);
		} else {
			String method_name = m_Node.getAssertMethod().toString();
			for (int i = 0; i < m_ComboBoxMethod.getItemCount(); ++i) {
				String itemName = m_ComboBoxMethod.getItemAt(i).toString();
				if (itemName.equals(method_name)) {
					m_ComboBoxMethod.setSelectedIndex(i);
					break; // exit for
				}
			}
		}
		updateUI();
	}
}
