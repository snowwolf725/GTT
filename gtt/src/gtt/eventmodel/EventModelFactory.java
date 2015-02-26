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
package gtt.eventmodel;

import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingModel;
import gtt.eventmodel.web.HTMLComponent;
import gtt.eventmodel.web.WebModel;
import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;

public class EventModelFactory {
	// //////////////////////////////////////////////////////////////////
	// singleton 2007/10/29 -
	// 避免要重複載入　 swing.xml　的工作，提昇效率
	private static IEventModel theModel = null;
	private static String path = "";

	// Compatible for Eclipse version
	public static void setPluginPath(String _path) {
		path = _path;
	}

	public static String getPluginPath() {
		return path;
	}

	public static IComponent createComponent() {
		if (GTTlipseConfig.testingOnWebPlatform())
			return HTMLComponent.createDefault(); // web version
		// other - Swing version
		else if(GTTlipseConfig.testingOnSwingPlatform())
			return SwingComponent.createDefault();
		else
			return GTTlipse.getPlatformInfo().createDefaultComponent();
	}

	public static IEventModel getDefault() {
		if (GTTlipseConfig.testingOnWebPlatform())
			return createWebModel(); // web version
		else if(GTTlipseConfig.testingOnSwingPlatform())
			return createSwingModel();
		else {
			return GTTlipse.getPlatformInfo().createEventModel();
		}
	}

	private static IEventModel createWebModel() {
		if (theModel == null) {
			theModel = new WebModel();
			theModel.initialize(path + "descriptor/web.xml");
		}
		return theModel;
	}

	// for unit test
	public static IEventModel createSwingModel() {
		if (theModel == null) {
			theModel = new SwingModel();
			theModel.initialize(path + "descriptor/swing.xml");
		}
		return theModel;
	}

	// 加了static變成single pattern，為了單元測試增加一個destroy method
	public static void destroy() {
		theModel = null;
	}

}
