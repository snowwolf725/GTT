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
package gtt.util.refelection;

import java.io.FileInputStream;

/**
 * Loads class bytes from a file.
 * 
 * @author Jack Harich - 8/30/97
 */
public class FileClassLoader extends MultiClassLoader {

	public FileClassLoader() {
	}

	// ---------- Private Fields ------------------------------
	private String filePrefix;

	// ---------- Initialization ------------------------------
	/**
	 * Attempts to load from a local file using the relative "filePrefix", ie
	 * starting at the current directory. For example
	 * 
	 * @param filePrefix
	 *            could be "webSiteClasses\\site1\\".
	 */
	public FileClassLoader(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	// ---------- Abstract Implementation ---------------------
	protected byte[] loadClassBytes(String className) {

		className = formatClassName(className);
		if (sourceMonitorOn) {
			print(">> from file: " + className);
		}
		byte result[];
		String fileName = filePrefix + className;
		try {
			FileInputStream inStream = new FileInputStream(fileName);
			// *** Is available() reliable for large files?
			result = new byte[inStream.available()];
			inStream.read(result);
			inStream.close();
			return result;

		} catch (Exception e) {
			// If we caught an exception, either the class
			// wasn't found or it was unreadable by our process.
			print("### File '" + fileName + "' not found.");
			return null;
		}
	}

	public Class<?> loadFile(String url) throws ClassNotFoundException {
		String className = ReflectionUtil.getClassName(url);
		if (className == null)
			return null;
		// 指定 filePrefix (就是指檔案的路徑)
		this.filePrefix = ReflectionUtil.getClassPath(url);
		// 就能load class 了
		return loadClass(className);
	}

	public void setClassPath(String[] paths) {
		if (paths == null)
			return;
		for (String path : paths) {
			addClassPath(path);
		}
	}

} // End class
