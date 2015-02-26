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
package gtt.recorder.atje;

import java.awt.Component;
import java.awt.Container;

public class TemporaryComponentData {

	private Class<?> _windowClassType, _componentClassType;

	public void setWinType(Class<?> type) {
		_windowClassType = type;
	}

	private String _windowTitle = "";

	public void setTitle(String title) {
		_windowTitle = title;
	}

	private String _componentName = "";

	private String _text = "";

	public void setText(String text) {
		_text = text;
	}

	private int _indexInWindow = 0, _indexOfSameName = 0;

	public TemporaryComponentData(Component comp) {
		_componentClassType = comp.getClass();
		_componentName = comp.getName();

		// 暫時不考慮 Index 的問題
		_indexInWindow = 0;
		_indexOfSameName = 0;

		ComponentUtil.initWindowInformation(this, comp);
		ComponentUtil.initTextInformation(this, comp);
	}

	public Class<?> getWinType() {
		return _windowClassType;
	}

	public Class<?> getType() {
		return _componentClassType;
	}

	public String getTitle() {
		return _windowTitle;
	}

	public String getName() {
		return _componentName;
	}

	public int getIndex() {
		return _indexInWindow;
	}

	public int getIndexInwindow(Container cont, Class<?> classType,
			String componentName, int index) {
		if (cont == null)
			return _indexInWindow;

		_indexInWindow = index;
		Component children[] = cont.getComponents();

		for (int i = 0; i < children.length; i++) {
			if (classType.isInstance(children[i])) {
				if (componentName == null && children[i].getName() == null) {
					_indexInWindow += i;
					return _indexInWindow;
				} else if (componentName != null && children[i] != null) {
					if (componentName.matches(children[i].getName())) {
						_indexInWindow += i;
						return _indexInWindow;
					}
				}
			}
			if (children[i] instanceof Container) {
				int result;
				result = getIndexInwindow((Container) children[i], classType,
						componentName, index);
				if (result != -1) {
					_indexInWindow = result;
					return _indexInWindow;
				}
			}
		}
		return -1;
	}

	public int getIndexOfSameName() {
		return _indexOfSameName;
	}

	public String getText() {
		return _text;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof TemporaryComponentData))
			return false;

		TemporaryComponentData data = (TemporaryComponentData) obj;
		if (this.getClass() != data.getClass())
			return false;
		if (this.getType() != data.getType())
			return false;
		if (this.getName() != data.getName())
			return false;
		if (this.getIndex() != data.getIndex())
			return false;
		if (this.getWinType() != data.getWinType())
			return false;
		if (this.getTitle() != data.getTitle())
			return false;
		// finally, they are equal..
		return true;
	}

}
