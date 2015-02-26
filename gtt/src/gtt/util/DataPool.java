package gtt.util;

import java.util.HashMap;

public class DataPool {

	private static DataPool _pool = null;
	
	private HashMap<String, Object> _map = null;
	
	private DataPool() {
		_map = new HashMap<String, Object>();
	}
	
	public static DataPool getDataPool() {
		// Singleton pattern
		if (_pool == null) {
			_pool = new DataPool();
		}
		return _pool;
	}
	
	public void setData(String key, Object data) {
		_map.put(key, data);
	}
	
	public Object getData(String key) {
		return _map.get(key);
	}
}
