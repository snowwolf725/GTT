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
 * Interface of Component (Swing, SWT, etc,...)
 * 
 * 2006/08/04
 * @author zwshen
 *
 */
public interface IComponent extends Cloneable {

	public IComponent clone();
	
	/**
	 * equals only for match name
	 */
	public boolean equals(Object arg0);
	
	/**
	 * for match some infomation
	 */
	public boolean match(Object obj);
	
	/**
	 * 1. Component type
	 */
	public String getType();
	
	public void setType(String type);
	/**
	 * 2. Window Type
	 */
	public String getWinType();
	
	public void setWinType(String type);
	/**
	 * 3. Window Title
	 */
	public String getTitle() ;
	public void setTitle(String title);
	
	/**
	 * 4. Component Name 
	 */
	public String getName();
	
	public void setName(String name);
	
	/**
	 * 5. Text(Label, Caption)
	 */
	
	public String getText();
	
	public void setText(String text);
	
	/**
	 * 6. the index of a component in a window
	 */
	
	public int getIndex();
	
	public void setIndex(int idx);
	
	/**
	 * 7. the index of the same name component in window
	 */
	public int getIndexOfSameName();
	
	public void setIndexOfSameName(int idx);
	
}
