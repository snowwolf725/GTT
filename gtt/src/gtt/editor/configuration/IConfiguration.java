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

import org.netbeans.jemmy.JemmyProperties;

/*
 * zws 2006/12/14
 */
public interface IConfiguration {

	public static final String USER_DIRECTORY = System.getProperty("user.dir");
	public static final String IMAGE_DIRECTORY = USER_DIRECTORY + "/images/";
	public static String DEFAULT_DIRECTORY = USER_DIRECTORY + "/";

	public static final int DEFAULT_SLEEP_TIME = 10;

	public static final int DEFAULT_SENSITIVITY_TIME = 500;

	public static final int HIGH_LEVEL = 1;

	public static final int MEDIUM_LEVEL = 2;

	public static final int LOW_LEVEL = 3;

	public static final int JDIALOG = 10;

	public static final int JFRAME = 11;

	public static final int ROBOT_MODE = JemmyProperties.ROBOT_MODEL_MASK;

	public static final int SHORTCUT_MODE = JemmyProperties.SHORTCUT_MODEL_MASK;

	public int getSensitivityTime();

	public void setSensitivityTime(int time);

	public int getSleepTime();

	public void setSleepTime(int time);

	public int getAbstractionLevel();

	public void setAbstractionLevel(int level);

	public String getWindowClass();

	public void setWindowClass(String classType);

	public int getReplayMode();

	public void setReplayMode(int mode);

	public String getAUTFilePath();

	public void setAUTFilePath(String path);

	public String getAUTArgument();

	public void setAUTArgument(String arg);

	public String getWindowTitle();

	public void setWindowTitle(String title);

	// class path

	public String getClassPath();

	public void setClassPath(String path);

	// invoke method

	public String getInvokeMethod();

	public void setInvokeMethod(String method);

	// XML IO
	public boolean openFile(String file);

	public boolean saveFile(String file);
	
	// for Oracle Config
	public boolean getCollectOracle();
	public void setCollectOracle(boolean f);

}