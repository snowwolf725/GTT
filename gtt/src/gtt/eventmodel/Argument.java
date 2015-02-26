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

/**
 *  event 把计篈パtype name value ┮舱Θ
 * 
 * @author zwshen
 * 
 */
public class Argument implements Cloneable {

	private String m_Type;

	private String m_Name;

	private String m_Value;

	public Argument clone() {
		return new Argument(m_Type, m_Name, m_Value);
	}

	private Argument(String type, String name, String value) {
		m_Type = type;
		m_Name = name;
		m_Value = value;
	}

	public static Argument create(String type, String name, String value) {
		return new Argument(type, name, value);
	}

	public static Argument create(String type, String name) {
		return new Argument(type, name, null);
	}

	public Argument(String type, String name) {
		this(type, name, null);
	}

	/*
	 * 把计""盽琌穦跑  type の name 碞ぃ惠璶setter
	 */
	public String getType() {
		return m_Type;
	}

	public String getName() {
		return m_Name;
	}

	public String getValue() {
		return m_Value;
	}

	public void setType(String type) {
		m_Type = type;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public void setValue(String value) {
		m_Value = value;
	}
}
