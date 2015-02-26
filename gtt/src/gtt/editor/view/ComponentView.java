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
 * Created on 2005/3/15
 */
package gtt.editor.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ComponentView implements IView {

	private static final long serialVersionUID = 1L;
	
	private JPanel m_MainPanel = new JPanel();

	private JTextField TF_WindowClassType = new JTextField();

	private JTextField TF_WindowTitle = new JTextField();

	private JTextField TF_ComponentClassType = new JTextField();

	private JTextField TF_ComponentName = new JTextField();

	private JTextField TF_Text = new JTextField();

	private JTextField TF_IndexInWindow = new JTextField();

	private JTextField TF_IndexOfSameName = new JTextField();

	public ComponentView() {
		init();
		initLayout();
	}

	private void initLayout() {
		m_MainPanel.setLayout(new BoxLayout(m_MainPanel, BoxLayout.Y_AXIS));
		m_MainPanel.add(TF_WindowClassType);
		m_MainPanel.add(TF_WindowTitle);
		m_MainPanel.add(TF_ComponentClassType);
		m_MainPanel.add(TF_ComponentName);
		m_MainPanel.add(TF_Text);
		m_MainPanel.add(TF_IndexInWindow);
		m_MainPanel.add(TF_IndexOfSameName);
	}

	private void init() {
		TF_WindowClassType.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Window Class Type"));
		TF_WindowTitle.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Window Title"));
		TF_ComponentClassType.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Component Class Type"));
		TF_ComponentName.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Component Name"));
		TF_Text.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.RAISED), "Text"));
		TF_IndexInWindow.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Index In Window"));
		TF_IndexOfSameName.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED), "Index Of Same Name"));
	}

	public void clearContext() {
		TF_WindowClassType.setText("");
		TF_WindowTitle.setText("");
		TF_ComponentClassType.setText("");
		TF_ComponentName.setText("");
		TF_Text.setText("");
		TF_IndexInWindow.setText("");
		TF_IndexOfSameName.setText("");
	}

	public void setEanbled(boolean enable) {
		m_MainPanel.setEnabled(enable);
		TF_WindowClassType.setEnabled(enable);
		TF_WindowTitle.setEnabled(enable);
		TF_ComponentClassType.setEnabled(enable);
		TF_ComponentName.setEnabled(enable);
		TF_Text.setEnabled(enable);
		TF_IndexInWindow.setEnabled(enable);
		TF_IndexOfSameName.setEnabled(enable);
	}

	/**
	 * 增加getter/setter，去掉與ComponentData的相依性
	 * zws 2006/10/28
	 */
	public String geComponentClasstype() {
		return TF_ComponentClassType.getText();
	}

	public String getWinClasstype() {
		return TF_WindowClassType.getText();
	}

	public String getWinTitle() {
		return TF_WindowTitle.getText();
	}

	public String getComponentText() {
		return TF_Text.getText();
	}

	public String getIndexOfSameName() {
		if (TF_IndexOfSameName.getText().equals(""))
			return "0";

		return TF_IndexOfSameName.getText();
	}

	public String getIndexInWindow() {
		if (TF_IndexInWindow.getText().equals(""))
			return "0";

		return TF_IndexInWindow.getText();
	}

	public String getComponentName() {
		return TF_ComponentName.getText();
	}

	public void setName(String name) {
		m_MainPanel.setName(name);
		TF_WindowClassType.setName(name + "_WindowClassType");
		TF_WindowTitle.setName(name + "_WindowTitle");
		TF_ComponentClassType.setName(name + "_ComponentClassType");
		TF_ComponentName.setName(name + "_ComponentName");
		TF_Text.setName(name + "_Text");
		TF_IndexInWindow.setName(name + "_IndexInWindow");
		TF_IndexOfSameName.setName(name + "_IndexOfSameName");
	}

	public void setComponentClassType(String type) {
		TF_ComponentClassType.setText(type);
	}

	public void setComponentName(String name) {
		TF_ComponentName.setText(name);
	}

	public void setIndexInWindow(String idx) {
		TF_IndexInWindow.setText(idx);
	}

	public void setIndexOfSameName(String idx) {
		TF_IndexOfSameName.setText(idx);
	}

	public void setComponentText(String text) {
		TF_Text.setText(text);
	}

	public void setWinClassType(String type) {
		TF_WindowClassType.setText(type);
	}

	public void setWinTtile(String title) {
		TF_WindowTitle.setText(title);
	}

	@Override
	public JPanel getView() {
		return m_MainPanel;
	}

	@Override
	public String getViewName() {
		return "ComponentView";
	}
}
