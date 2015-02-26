/**
 * 
 */
package gttlipse.scriptEditor.util;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class ScrapBook {

	private static Object _data[];

	public static void copy(Object obj[]) {
		_data = obj;
	}
	
	public static Object[] getData() {
		return _data;
	}
}
