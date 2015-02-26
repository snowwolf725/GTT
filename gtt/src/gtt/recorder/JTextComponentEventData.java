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
 * Created on 2004/2/19
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gtt.recorder;

import gtt.eventmodel.IComponent;

/************************************************************************************
 * 名稱 : JTextComponentEventData.java 作者 : 東宏 日期 :2004/2/19 , 下午 07:23:12 備註 :
 ************************************************************************************/
public class JTextComponentEventData extends JComponentEventData {

	String _text;
	int _pos = 0;
	int _posEnd = 0;

	public JTextComponentEventData(IComponent data, int eid) {
		super(data, eid);
	}

	public JTextComponentEventData(IComponent source,
			int eventType, int pos, String text) {
		super(source, eventType);
		_pos = pos;
		_text = text;
	}

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public int getPosition() {
		return _pos;
	}

	public void setPosition(int pos) {
		_pos = pos;
	}

	public int get_posEnd() {
		return _posEnd;
	}

	public void set_posEnd(int i) {
		_posEnd = i;
	}
}
