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
/**
 *  2005/11/08  -Refactoring
 *  zws 
 * 
 */
package gtt.util.refelection;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Loads class bytes from a URL, such as "http://www.mindspring.com/~happyjac/".
 * 
 * @author Jack Harich - 8/30/97
 */
public class URLClassLoader extends MultiClassLoader {

	// ---------- Private Fields ------------------------------
	private String urlString;

	// ---------- Initialization ------------------------------
	public URLClassLoader(String urlString) {
		this.urlString = urlString;
	}

	// ---------- Abstract Implementation ---------------------
	protected byte[] loadClassBytes(String className) {

		className = formatClassName(className);
		try {
			URL url = new URL(urlString + className);
			URLConnection connection = url.openConnection();
			if (sourceMonitorOn) {
				print("Loading from URL: " + connection.getURL());
			}
			monitor("Content type is: " + connection.getContentType());

			InputStream inputStream = connection.getInputStream();
			int length = connection.getContentLength();
			monitor("InputStream length = " + length); // Failure if -1

			byte[] data = new byte[length];
			inputStream.read(data); // Actual byte transfer
			inputStream.close();
			return data;

		} catch (Exception ex) {
			print("### URLClassLoader.loadClassBytes() - Exception:");
			ex.printStackTrace();
			return null;
		}
	}

} // End class
