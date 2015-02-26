package gttlipse.tabular.util;

import gttlipse.tabular.table.TableModel;

public class TabularUtil {

	public static void fillStringArray(String[] target, int start) {
		for(int i = start; i < target.length; i++) {
			target[i] = "";
		}
	}
	
	public static String[] createStringArray(int size) {
		String[] array = null;
		
		if (size >= 0) {
			array = new String[size];
			fillStringArray(array, 0);
		}
		return array;
	}
	
	public static String getNameFromTitle(String title) {
		int index = title.indexOf("(");
		
		if (index != -1) {
			return title.substring(0, index);
		}
		return title;
	}
	
	public static int findHeaderIndex(TableModel model, int selectedRow) {
		int index = 0;
		
		// Find header by decreasing the index
		for(int i = selectedRow; i > 0; i--) {
			if (model.isHeaderRow(i)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
}
