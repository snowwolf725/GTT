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
package gtt.editor.configuration;

import javax.swing.JFrame;

public class ConfigurationData implements IConfiguration {

	String m_WindowClass = JFrame.class.getName();

	int m_SensitivityTime = DEFAULT_SENSITIVITY_TIME;

	int m_SleepTime = DEFAULT_SLEEP_TIME;

	int m_ReplayMode = ROBOT_MODE;

	int m_AbstractionLevel = HIGH_LEVEL;

	String m_WindowTitle;

	String m_AUTFilePath;

	String m_ClassPath = ".";

	String m_AUTArgument = "";

	String m_InvokeMethod = "";

	boolean bCollectOracle = false;

	public String getAUTArgument() {
		return m_AUTArgument;
	}

	public String getAUTFilePath() {
		return m_AUTFilePath;
	}

	public int getAbstractionLevel() {
		return m_AbstractionLevel;
	}

	public int getSensitivityTime() {
		return m_SensitivityTime;
	}

	public int getReplayMode() {
		return m_ReplayMode;
	}

	public int getSleepTime() {
		return m_SleepTime;
	}

	public String getWindowClass() {
		return m_WindowClass;
	}

	public String getWindowTitle() {
		return m_WindowTitle;
	}

	public void setAUTArgument(String arg) {
		m_AUTArgument = arg;

	}

	public void setAUTFilePath(String path) {
		m_AUTFilePath = path;

	}

	public void setAbstractionLevel(int level) {
		if (level != LOW_LEVEL && level != MEDIUM_LEVEL && level != HIGH_LEVEL)
			return;

		m_AbstractionLevel = level;
	}

	public void setSensitivityTime(int time) {
		m_SensitivityTime = time;
	}

	public void setReplayMode(int mode) {
		if (mode != ROBOT_MODE && mode != SHORTCUT_MODE)
			return;
		m_ReplayMode = mode;
	}

	public void setSleepTime(int time) {
		if (time < 0)
			m_SleepTime = 0; // 最少是0
		else
			m_SleepTime = time;
	}

	public void setWindowClass(String classType) {
		// 只接受 JDialog/JFrame
		// if (classType != JDialog.class && classType != JFrame.class)
		// return;

		m_WindowClass = classType;

	}

	public void setWindowTitle(String title) {
		m_WindowTitle = title;
	}

	public String getClassPath() {
		return m_ClassPath;
	}

	public void setClassPath(String path) {
		m_ClassPath = path;
	}

	public String getInvokeMethod() {
		return m_InvokeMethod;
	}

	public void setInvokeMethod(String m) {
		m_InvokeMethod = m;
	}

	/**
	 * read content from XML file
	 */
	public boolean openFile(String file) {
		return new ConfigLoad(this).load(file);
	}

	/**
	 * save content into XML file
	 */
	public boolean saveFile(String file) {
		return new ConfigSave(this).save(file);
	}

	@Override
	public boolean getCollectOracle() {
		return bCollectOracle;
	}

	@Override
	public void setCollectOracle(boolean f) {
		bCollectOracle = f;
	}

}
