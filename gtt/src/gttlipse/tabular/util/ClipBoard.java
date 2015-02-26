package gttlipse.tabular.util;

public class ClipBoard {

	private static Object _macroItem = null;
	private static Object _scriptItem = null;
	
	public static void setMacroItem(Object item) {
		_macroItem = item;
	}
	
	public static void setScriptItem(Object item) {
		_scriptItem = item;
	}
	
	public static Object getMacroItem() {
		return _macroItem;
	}
	
	public static Object getScriptItem() {
		return _scriptItem;
	}
}
