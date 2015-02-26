package gtt.oracle;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.tester.finder.DefaultComponentFinder;
import gtt.testscript.NodeFactory;
import gtt.testscript.ViewAssertNode;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class OracleUtil {

	private static AssertableMethodLoader loader = new AssertableMethodLoader();

	private static NodeFactory factory = new NodeFactory();

	public static List<ViewAssertNode> makeAssertList(List<IComponent> list,
			OracleData oracle) {

		List<ViewAssertNode> result_list = new Vector<ViewAssertNode>();
		Iterator<IComponent> ite = list.iterator();
		while (ite.hasNext()) {
			IComponent ic = (IComponent) ite.next();

			if (ic == null || ic.getName() == null)
				continue;

			if (ic.getType().equals("javax.swing.JComponent")
					|| ic.getType().equals("java.awt.Component"))
				continue;

			if (DefaultComponentFinder.instance().find(ic) == null)
				continue;

			if (oracle.getType() == IOracleHandler.EventType.DEFAULT
					|| oracle.getType() == IOracleHandler.EventType.ALL) {
				handleDefaultEvent(result_list, ic);
			} else if (oracle.getType() == IOracleHandler.EventType.USER_SELECTED) {
				// nothing to do- zwshen 2009/12/21
				// for user selected event
				// handleUserSelecedtEvent(oracle, result_list, ic);
			}
		}

		return result_list;
	}

	// handle default event
	static void handleDefaultEvent(List<ViewAssertNode> result_list,
			IComponent comp) {
		List<Method> methods = loader.getMethods(comp.getType());
		if (methods == null)
			return;
		Iterator<Method> ite = methods.iterator();
		while (ite.hasNext()) {
			Method m = (Method) ite.next();
			Assertion a = new Assertion();
			a.setMethod(m);
			ViewAssertNode n = factory.createViewAssertNode(comp, a);
			boolean result = setupExpectedValue(n);
			if (result == true)
				result_list.add(n);
		}
	}

	public static String getActualValue(Object comp, Assertion as) {
		try {
			Method invokeMethod = comp.getClass().getMethod(as.getMethodName(),
					as.typeClasses());

			Object value = null;
			Object[] argValues = as.getArguments().values().toArray();
			if (argValues.length == 0)
				value = invokeMethod.invoke(comp);
			if (argValues.length == 1)
				value = invokeMethod.invoke(comp, argValues[0]);
			if (argValues.length == 2) {
				int v1 = Integer.valueOf((String) argValues[0]);
				int v2 = Integer.valueOf((String) argValues[1]);
				value = invokeMethod.invoke(comp, v1, v2);
			}
			value = parseObjectValue(value);

			// fix for special characters
			String result = value.toString();
			result = result.replaceAll("\\n", "\\\\n");
			result = result.replaceAll("\\t", "\\\\t");
			return result;
		} catch (Exception e) {
			// no vaule
			return "";
		}
	}

	private static Object parseObjectValue(Object value)
			throws BadLocationException {
		if (value instanceof Document) {
			Document doc = (Document) value;
			return doc.getText(0, doc.getLength());
		}

		return value;
	}

	public static boolean setupExpectedValue(ViewAssertNode node) {
		Component comp = DefaultComponentFinder.instance().find(
				node.getComponent());
		if (comp == null)
			return false;
		return collectActucalValue(comp, node.getAssertion());
	}

	private static boolean collectActucalValue(Component c, Assertion info) {
		if (info == null)
			return false;

		info.setValue(OracleUtil.getActualValue(c, info));
		return true;
	}
}
