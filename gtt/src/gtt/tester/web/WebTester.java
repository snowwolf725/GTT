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
package gtt.tester.web;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.HtmlBaseElement;
import gtt.eventmodel.web.HtmlButton;
import gtt.eventmodel.web.HtmlElement;
import gtt.eventmodel.web.HtmlForm;
import gtt.eventmodel.web.HtmlInputText;
import gtt.eventmodel.web.HtmlLink;
import gtt.eventmodel.web.HtmlSelect;
import gtt.eventmodel.web.HtmlTable;
import gtt.eventmodel.web.HtmlTextArea;
import gtt.eventmodel.web.HtmlImg;
import gtt.eventmodel.web.HtmlRadio;
import gtt.eventmodel.web.HtmlCheckbox;
import gtt.logger.Logger;
import gtt.runner.web.WebController;
import gtt.tester.swing.ITester;
import gtt.testscript.EventNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * 使用 HtmlUnit 模組來發事件
 * 使用 Event ID 來判斷要發送的事件類型
 * gsx 2009/11/30
 */

/// Note 070522 : 每個event都含有sleep time 的參數，但是還沒有被放進播放中
public class WebTester implements ITester {
	
	public WebTester() {
		registrTester();
	}

	private HashMap<Class<?>, WebComponentTester> m_Tester = new HashMap<Class<?>, WebComponentTester>();

	public void addTester(Class<?> cls, WebComponentTester tester) {
		m_Tester.put(cls, tester);
	}

	public WebComponentTester getTester(Object obj) {

		WebComponentTester tester =  m_Tester.get(obj.getClass());
		if(tester!=null) {
//			System.out.println("using " + tester.getClass().toString());
			return tester;
		}
		Set<Class<?>> keys = m_Tester.keySet();
		Iterator<Class<?>> ite = keys.iterator();
		while (ite.hasNext()) {
			Class<?> cls = ite.next();
			if (cls.isAssignableFrom(obj.getClass())) {
				return m_Tester.get(cls);
			}
		}
		return new HtmlComponentTester();
	}

	private void registrTester() {
		// 註冊各個class 的tester
//		addTester(HtmlComponent.class, new HtmlComponentTester());
		addTester(HtmlElement.class, new WebElementTester());
		addTester(HtmlForm.class, new WebElementTester());
		addTester(HtmlButton.class, new WebElementTester());
		addTester(HtmlInputText.class, new WebElementTester());
		addTester(HtmlLink.class, new WebElementTester());
		addTester(HtmlTable.class, new WebElementTester());
		addTester(HtmlTextArea.class, new WebElementTester());
		addTester(HtmlSelect.class, new SelectTester());
		addTester(HtmlImg.class, new WebElementTester());
		addTester(HtmlRadio.class, new WebElementTester());
		addTester(HtmlCheckbox.class, new WebElementTester());
	}

	private void logging(String msg) {
		// 應該使用 logging 機制，會比較乾淨
		// zws 2007/01/04
		String prefix = "WebTester";
		Logger.getSimpleLogger().log(prefix + "-" + msg);
	}

	private HtmlBaseElement findComponent(IComponent com_info) {
		HtmlBaseElement targetComponent = null;
		targetComponent = WebController.instance().find(com_info);
		if (targetComponent == null) {
			logging("Couldn't find \"" + com_info + "\".");
			return null;
		}
		if (!(targetComponent instanceof HtmlBaseElement)) { 
			logging("\"" + com_info + "\" isn't a DomNode");
			return null;
		}
		
		return targetComponent;
	}

	public boolean fire(EventNode node) {
		HtmlBaseElement com = findComponent(node.getComponent());
		
		if(node.getComponent().getIndex() == -1) {
			System.out.println("Event can't send to all Compoents");
			return false;
		}
		
		// add for Load Testing result show error node
		// @2011-08-15
		if (com == null) {
			return false;
		}
		try {
			dispatchFireEvent(node.getEvent(), (HtmlBaseElement) com);
			if (m_GlobalSleepTime > 0)
				Thread.sleep(m_GlobalSleepTime);
			
//			System.out.println(WebController.instance().getPage().getXmlEncoding());
			
		} catch (Exception exp) {
			logging("[error]-->" + exp.toString());
			return false; // 這個事件無法成功發送
		}
		// 至此一切正常
		return true;
	}

	private final static HtmlComponentTester DEFAULT_TESTER = new HtmlComponentTester();

	protected void dispatchFireEvent(IEvent info, HtmlBaseElement targetComponent) {
		WebComponentTester tester = getTester(targetComponent);
		if (tester == null)
			tester = DEFAULT_TESTER;
		try {
			if (!tester.fireEvent(info, targetComponent)) {
//				System.out.println("dispatchFireEvent error: " + info);
				return;
			}
		} catch (NullPointerException nep) {
			// nothing for null pointer
//			System.out.println("dispatchFireEvent ");
			nep.printStackTrace();
		}		
		sleepWithEvent(info);
	}

	private void sleepWithEvent(IEvent info) {
		if (info.getArguments().getValue("SleepTime") == null)
			return;
		String sleepTime = info.getArguments().getValue("SleepTime");
		try {
			if (!sleepTime.equals(IEvent.NULL_VALUE) && sleepTime.length() > 0)
				Thread.sleep(Integer.parseInt(sleepTime));
		} catch (Exception e) {
			// nothing for sleep time
		}
	}

	private long m_GlobalSleepTime = DEFAULT_SLEEP_TIME;
	
	public void setSleepTime(long st) {
		if (st < 0)
			st = 0;
		m_GlobalSleepTime = st;
	}
}
