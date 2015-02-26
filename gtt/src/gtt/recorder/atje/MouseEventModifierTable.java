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

import java.awt.event.InputEvent;

/*
 * Extract class from JComponentEventData
 * zws 2006/04/02
 */
public class MouseEventModifierTable {
    private static final String[] MBID={"16","8","4"};
    private static final String[] MBNAME={"Left","Middle","Right"};
    
    public static String modify(int mouse) {
    	String match = Integer.toString(mouse);
        for(int idx=0; idx < MBID.length; ++idx) {
            if(MBID[idx].equals( match )) {
                return MBNAME[idx] + ":" + MBID[idx];
            }
        } // end for
        // finally, can't match the mouse value
        return EventModifierTable.getModifier(0);
    }
    
    private static final String UNKNOWN_TYPE = "Unknown Type";
    
	public static  String getModifierString(int eid) {
		switch (eid) {
		case InputEvent.BUTTON1_MASK:
			return "Left";
		case InputEvent.BUTTON2_MASK:
			return "Middle";
		case InputEvent.BUTTON3_MASK:
			return "Right";
		}
		return UNKNOWN_TYPE;
	}
	
	public static  String getShortModifierString(int eid) {
		String s = getModifierString(eid);
		return s.substring(0, 1);
	}

    
}