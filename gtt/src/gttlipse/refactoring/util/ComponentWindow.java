package gttlipse.refactoring.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class ComponentWindow {
	Map<String, Vector<String>> _map = null;
	
	public ComponentWindow() {
		_map = new HashMap<String, Vector<String>>();
	}
	
	public void addWindowData(String title, String type) {
		Vector<String> temp;
		if(_map.containsKey(title)) {
			temp = _map.get(title);
			for(int i = 0; i < temp.size(); i++) {
				if(temp.get(i).equals(type))
					return;	// exist same type
			}
			temp.add(type);
		}
		else {
			temp = new Vector<String>();
			temp.add(type);
		}
		_map.put(title, temp);
	}
	
	public Vector<String> getType(String title) {
		return _map.get(title);
	}
	
	public Iterator<String> iterator() {
		return _map.keySet().iterator();
	}
}
