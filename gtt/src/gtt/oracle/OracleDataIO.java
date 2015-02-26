package gtt.oracle;

import gtt.testscript.ViewAssertNode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class OracleDataIO {
	
	// 記錄目前當下運作的mutant name
	public static String mutant_name = "";

	private static final String ORACLE_DIRECTION = "oracle/";

	public static void writeOracle(List<ViewAssertNode> list, OracleData d) {
		// save it out
		OracleInfoWriter saver = new OracleInfoWriter(list);
		saver.saveFile(ORACLE_DIRECTION + d.filename());
	}

	public static List<ViewAssertNode> readOracle(OracleData d) {
		OracleInfoReader reader = new OracleInfoReader();
		return reader.readOracle(ORACLE_DIRECTION + d.filename());
	}

	public static void writeAsHtml(OracleData d, List<ViewAssertNode> nodes) {
		Iterator<ViewAssertNode> ite = nodes.iterator();
		StringBuilder info = new StringBuilder("<table border=1>");
		info.append("<tr>");
		info.append("<th>#</th>");
		info.append("<th>ComponentType</th>");
		info.append("<th>ComponentName</th>");
		info.append("<th>Assertion</th>");
		info.append("<th>ActualVale</th>");
		info.append("</tr>");
		int ct = 1;
		while (ite.hasNext()) {
			ViewAssertNode va = ite.next();
			info.append("<tr>");
			info.append("<td>" + (ct++) + "</td>");
			info.append("<td>" + va.getComponent().getType() + "</td>");
			info.append("<td>" + va.getComponent().getName() + "</td>");
			info.append("<td>" + va.getAssertion().toString() + "</td>");
			if (va.getActualValue() == null) {
				// green
				info.append("<td><font color=GREE>Ok.</font></td>");
			} else {
				// red
				info.append("<td><b><font color=RED>");
				info.append(va.getActualValue());
				info.append("</font></b></td>");
			}
			info.append("</tr>");
		}
		info.append("</table>");

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(ORACLE_DIRECTION + d.filename() + "_"
					+ mutant_name + "_result.html");
			out.write(info.toString().getBytes());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
