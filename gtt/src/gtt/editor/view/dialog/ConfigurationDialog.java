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
/*************************************************************************
 * Name   : TestConfigurationView
 * Date   : 2003/12/05
 * Authoe : 東宏.
 * Object : 設定 GTT 的 config之View , 可設定Global Sleep Time
 *           , Double Click Sensitivity 及 Capture Level 等參數.	
 *************************************************************************/
package gtt.editor.view.dialog;

import gtt.editor.configuration.IConfiguration;
import gtt.runner.RunnerUtil;
import gtt.util.refelection.ReflectionUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConfigurationDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String PATH = System.getProperty("user.dir")
			+ "/images/";

	IConfiguration m_Configuration;

	public ConfigurationDialog(IConfiguration data) {
		m_Configuration = data;
		this.setResizable(false);
		setTitle("Configuration");
		setName("ConfigurationDialog");
		initComponents();
		initLayout();
		initName();
		restoreConfigurationData();
		initMethodBox();

		setLocation(this.getX(), 0);
	}

	private void restoreConfigurationData() {
		_abstractionG.add(_highAbsLevel);
		_abstractionG.add(_lowAbsLevel);

		_windowTypeG.add(_frame);
		_windowTypeG.add(_dialog);

		m_ReplayModeG.add(m_Robot);
		m_ReplayModeG.add(m_ShortCut);

		_sensitivity.setMaximum(1000);
		_sensitivity.setMinimum(0);
		_sensitivity.setValue(m_Configuration.getSensitivityTime());
		_sensitivity.setPaintTicks(true);
		_sensitivity.setMajorTickSpacing(100);
		_sensitivity.setPaintLabels(true);

		_currentSensitivity.setText(String.valueOf(_sensitivity.getValue()));
		_sleepTime.setText("" + m_Configuration.getSleepTime());

		m_AUT_FilePath.setText(m_Configuration.getAUTFilePath());
		m_AUT_ClassPath.setText(m_Configuration.getClassPath());
		_windowTitle.setText(m_Configuration.getWindowTitle());
		_invokeArg.setText(m_Configuration.getAUTArgument());
		
		m_Oracle.setSelected(m_Configuration.getCollectOracle());
	}

	private void initName() {
		_cancel.setName("_cancel");
		_currentSensitivity.setName("_sensitivityL");
		_lowAbsLevel.setName("_low");
		_highAbsLevel.setName("_medium");
		_ok.setName("_ok");
		_sensitivity.setName("_clickSensitivity");
		_sleepTime.setName("_sleepTime");
		_frame.setName("_jframe");
		_dialog.setName("_jdialog");
		m_Robot.setName("_robot");
		m_ShortCut.setName("_shortCut");
		_windowTitle.setName("_windowTitle");
		_openAut.setName("_openApp");
		_invokeArg.setName("_invokeArg");
		_invokeMethod.setName("_invokeMethod");

		m_AUT_ClassPath.setName("m_AUT_ClassPath");
		m_Oracle.setName("cbCollectOracle");
	}

	private void initMethodBox() {
		boolean isSelected = false;

		if (m_AUT_FilePath.getText().equals(""))
			return;

		try {
			Class<?> classobj = RunnerUtil.loadClass(m_AUT_FilePath.getText(),
					m_AUT_ClassPath.getText());
			
			if(classobj==null) {
//				JOptionPane.showMessageDialog(this, "Load class fail.");
				return;
			}
			
			List<Method> ms = ReflectionUtil.getInvokedMethods(classobj);
			_invokeMethod.removeAllItems();
			for (int i = 0; i < ms.size(); i++) {
				String s = ms.get(i).toString();
				_invokeMethod.addItem(s);

				if (isSelected)
					continue;

				if (m_Configuration.getInvokeMethod() != null
						&& m_Configuration.getInvokeMethod().equals(s)) {
					_invokeMethod.setSelectedIndex(i);
					isSelected = true;
					continue;
				}

				if (s.indexOf("main") != -1) {
					_invokeMethod.setSelectedIndex(i);
				}
			}

		} catch (Exception ep) {
			JOptionPane.showMessageDialog(this, ep.toString());
			System.out.println(ep.toString());
		}
	}

	private void initComponents() {
		_abstractionG = new javax.swing.ButtonGroup();
		_windowTypeG = new javax.swing.ButtonGroup();
		m_ReplayModeG = new javax.swing.ButtonGroup();
		m_MainPanel = new javax.swing.JPanel();
		_sensitivity = new javax.swing.JSlider();
		_sleepTime = new JTextField();
		_sensitivityL = new javax.swing.JLabel();
		_sleepTimeL = new javax.swing.JLabel();
		_highAbsLevel = new javax.swing.JRadioButton();
		_lowAbsLevel = new javax.swing.JRadioButton();
		_frame = new javax.swing.JRadioButton();
		_dialog = new javax.swing.JRadioButton();
		m_Robot = new javax.swing.JRadioButton();
		m_ShortCut = new javax.swing.JRadioButton();
		_abstractionLevelLabel = new javax.swing.JLabel();
		_windowTypeL = new javax.swing.JLabel();
		m_ReplayModeL = new javax.swing.JLabel();
		_windowTitleL = new javax.swing.JLabel();
		_windowTitle = new javax.swing.JTextField();
		_openAppL = new javax.swing.JLabel();
		_openAut = (JButton) WidgetFactory.getButton(null,
				PATH + "openApp.jpg", PATH + "openAppUp.jpg", PATH
						+ "openAppDown.jpg", null, "Open Application", 25, 25,
				false);
		m_AUT_FilePath = new javax.swing.JTextField();
		_invokeArgL = new javax.swing.JLabel();
		_invokeArg = new javax.swing.JTextField();

		_AUTClassPathL = new javax.swing.JLabel();
		_appStartPointL = new javax.swing.JLabel();
		_ok = new javax.swing.JButton();
		_cancel = new javax.swing.JButton();
		_currentSensitivity = new javax.swing.JLabel();
		_invokeMethod = new javax.swing.JComboBox();
		m_AUT_ClassPath = new javax.swing.JTextField();
		
		m_Oracle = new javax.swing.JCheckBox("Collect Oracle");

	}

	private void initLayout() {
		getContentPane().setLayout(null);

		setTitle("Configuration");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		m_MainPanel.setLayout(null);

		_sensitivity.setMinorTickSpacing(100);
		_sensitivity.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				_sensitivityMouseClicked(evt);
			}

			public void mousePressed(java.awt.event.MouseEvent evt) {
				_sensitivityMousePressed(evt);
			}
		});
		_sensitivity
				.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(java.awt.event.MouseEvent evt) {
						_sensitivityMouseDragged(evt);
					}
				});

		m_MainPanel.add(_sensitivity);
		_sensitivity.setBounds(20, 50, 330, 50);

		_sensitivityL.setText("Default Doble Click Time (ms):");
		m_MainPanel.add(_sensitivityL);
		_sensitivityL.setBounds(20, 20, 185, 30);

		m_MainPanel.add(_currentSensitivity);
		_currentSensitivity.setBounds(210, 20, 80, 30);

		_sleepTimeL.setText("Default Sleep Time (ms):");
		m_MainPanel.add(_sleepTimeL);
		_sleepTimeL.setBounds(20, 120, 145, 30);

		m_MainPanel.add(_sleepTime);
		_sleepTime.setBounds(170, 125, 180, 25);

		_abstractionLevelLabel.setText("Event Capture Abstraction Level  :");
		m_MainPanel.add(_abstractionLevelLabel);
		_abstractionLevelLabel.setBounds(20, 165, 300, 30);

		_highAbsLevel.setText("High");
		m_MainPanel.add(_highAbsLevel);
		_highAbsLevel.setBounds(215, 165, 50, 26);

		_lowAbsLevel.setText("Low");
		m_MainPanel.add(_lowAbsLevel);
		_lowAbsLevel.setBounds(285, 165, 50, 26);

		if (m_Configuration.getAbstractionLevel() == IConfiguration.HIGH_LEVEL)
			_highAbsLevel.setSelected(true);
		else if (m_Configuration.getAbstractionLevel() == IConfiguration.LOW_LEVEL)
			_lowAbsLevel.setSelected(true);

		_windowTypeL.setText("Default Window ClassType :");
		m_MainPanel.add(_windowTypeL);
		_windowTypeL.setBounds(20, 190, 300, 25);

		_frame.setText("JFrame");
		m_MainPanel.add(_frame);
		_frame.setBounds(215, 190, 70, 26);

		_dialog.setText("JDialog");
		m_MainPanel.add(_dialog);
		_dialog.setBounds(285, 190, 70, 26);

		if (m_Configuration.getWindowClass().equals(JFrame.class.getName()))
			_frame.setSelected(true);
		else
			_dialog.setSelected(true);

		m_ReplayModeL.setText("Replay Mode :");
		m_MainPanel.add(m_ReplayModeL);
		m_ReplayModeL.setBounds(20, 215, 300, 25);

		m_Robot.setText("Robot");
		m_MainPanel.add(m_Robot);
		m_Robot.setBounds(215, 215, 70, 26);

		m_ShortCut.setText("Short Cut");
		m_MainPanel.add(m_ShortCut);
		m_ShortCut.setBounds(285, 215, 104, 26);

		m_Oracle.setSelected(m_Configuration.getCollectOracle());
		m_MainPanel.add(m_Oracle);
		m_Oracle.setBounds(20, 245, 300, 25);

		if (m_Configuration.getReplayMode() == IConfiguration.ROBOT_MODE)
			m_Robot.setSelected(true);
		else
			m_ShortCut.setSelected(true);

		_windowTitleL.setText("Window Title:");
		m_MainPanel.add(_windowTitleL);
		_windowTitleL.setBounds(20, 280, 330, 25);
		m_MainPanel.add(_windowTitle);
		_windowTitle.setBounds(20, 310, 330, 25);

		_openAppL.setText("Application file name:");
		m_MainPanel.add(_openAppL);
		_openAppL.setBounds(20, 350, 330, 25);
		m_MainPanel.add(m_AUT_FilePath);
		m_AUT_FilePath.setBounds(20, 380, 330, 25);

		_AUTClassPathL.setText("Classpath:");
		m_MainPanel.add(_AUTClassPathL);
		_AUTClassPathL.setBounds(20, 410, 180, 30);
		m_MainPanel.add(m_AUT_ClassPath);
		m_AUT_ClassPath.setBounds(20, 440, 330, 26);

		_appStartPointL.setText("Invoke method:");
		m_MainPanel.add(_appStartPointL);
		_appStartPointL.setBounds(20, 470, 180, 30);
		m_MainPanel.add(_invokeMethod);
		_invokeMethod.setBounds(20, 500, 330, 26);

		_invokeArgL.setText("Invoke arguments:");
		m_MainPanel.add(_invokeArgL);
		_invokeArgL.setBounds(20, 530, 330, 25);
		m_MainPanel.add(_invokeArg);
		_invokeArg.setBounds(20, 560, 330, 25);

		m_MainPanel.add(_openAut);
		_openAut.setBounds(20, 590, 25, 25);
		_openAut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_openAppActionPerformed(evt);
			}
		});

		_ok.setText("Ok");
		_ok.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_okActionPerformed(evt);
			}
		});
		m_MainPanel.add(_ok);
		_ok.setBounds(70, 620, 110, 28);

		_cancel.setText("Cancel");
		_cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_cancelActionPerformed(evt);
			}
		});

		m_MainPanel.add(_cancel);
		_cancel.setBounds(200, 620, 110, 30);

		getContentPane().add(m_MainPanel);
		m_MainPanel.setBounds(0, 0, 530, 690);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 378) / 2, (screenSize.height - 376) / 2,
				378, 690);
	}

	private void _cancelActionPerformed(java.awt.event.ActionEvent evt) {
		setVisible(false);
		dispose();
	}

	private void _sensitivityMousePressed(java.awt.event.MouseEvent evt) {
		_currentSensitivity.setText(String.valueOf(_sensitivity.getValue()));
	}

	private void _sensitivityMouseClicked(java.awt.event.MouseEvent evt) {
		_currentSensitivity.setText(String.valueOf(_sensitivity.getValue()));
	}

	private void _sensitivityMouseDragged(java.awt.event.MouseEvent evt) {
		_currentSensitivity.setText(String.valueOf(_sensitivity.getValue()));
	}

	private void _okActionPerformed(java.awt.event.ActionEvent evt) {
		storeConfigurationData();
		this.dispose();
	}

	private void _openAppActionPerformed(ActionEvent evt) {
		initMethodBox();
	}

	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		this.setVisible(false);
		this.dispose();
	}

	// 將 view 中的資料，全存回到 ConfigurationData 中
	private void storeConfigurationData() {
		m_Configuration.setSensitivityTime(_sensitivity.getValue());
		m_Configuration.setSleepTime(Integer.parseInt(_sleepTime.getText()));

		if (_highAbsLevel.isSelected())
			m_Configuration.setAbstractionLevel(IConfiguration.HIGH_LEVEL);
		else
			m_Configuration.setAbstractionLevel(IConfiguration.LOW_LEVEL);

		if (_frame.isSelected())
			m_Configuration.setWindowClass(JFrame.class.getName());
		else
			m_Configuration.setWindowClass(JDialog.class.getName());

		if (m_Robot.isSelected())
			m_Configuration.setReplayMode(IConfiguration.ROBOT_MODE);
		else if (m_ShortCut.isSelected())
			m_Configuration.setReplayMode(IConfiguration.SHORTCUT_MODE);

		m_Configuration.setWindowTitle(_windowTitle.getText());
		m_Configuration.setAUTArgument(_invokeArg.getText());
		m_Configuration.setAUTFilePath(m_AUT_FilePath.getText());
		m_Configuration.setClassPath(m_AUT_ClassPath.getText());
		m_Configuration.setCollectOracle(m_Oracle.isSelected());

		try {
			m_Configuration.setInvokeMethod(_invokeMethod.getSelectedItem()
					.toString());
		} catch (NullPointerException ep) {
		}

	}

	private javax.swing.ButtonGroup _abstractionG;

	private javax.swing.ButtonGroup _windowTypeG;

	private javax.swing.ButtonGroup m_ReplayModeG;

	private javax.swing.JButton _cancel;

	private javax.swing.JLabel _currentSensitivity;

	private javax.swing.JRadioButton _lowAbsLevel;

	private javax.swing.JRadioButton _highAbsLevel;

	private javax.swing.JButton _ok;

	private javax.swing.JPanel m_MainPanel;

	private javax.swing.JSlider _sensitivity;

	private javax.swing.JLabel _sensitivityL;

	private javax.swing.JTextField _sleepTime;

	private javax.swing.JLabel _sleepTimeL;

	private javax.swing.JLabel _abstractionLevelLabel;

	private javax.swing.JLabel _windowTypeL;

	private javax.swing.JRadioButton _frame;

	private javax.swing.JRadioButton _dialog;

	private javax.swing.JLabel m_ReplayModeL;

	private javax.swing.JRadioButton m_Robot;

	private javax.swing.JRadioButton m_ShortCut;

	private javax.swing.JLabel _windowTitleL;

	private javax.swing.JTextField _windowTitle;

	private javax.swing.JLabel _openAppL;

	private javax.swing.JTextField m_AUT_FilePath;

	private javax.swing.AbstractButton _openAut;

	private javax.swing.JLabel _invokeArgL;

	private javax.swing.JTextField _invokeArg;

	private javax.swing.JLabel _AUTClassPathL;

	private javax.swing.JComboBox _invokeMethod;

	private javax.swing.JLabel _appStartPointL;

	private javax.swing.JTextField m_AUT_ClassPath;
	private javax.swing.JCheckBox m_Oracle;
}
