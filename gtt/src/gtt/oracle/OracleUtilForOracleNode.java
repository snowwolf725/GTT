package gtt.oracle;

import gtt.eventmodel.IComponent;
import gtt.tester.finder.DefaultComponentFinder;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ViewAssertNode;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class OracleUtilForOracleNode {

	public static List<ViewAssertNode> makeAssertList(List<IComponent> list,
			OracleNode node) {

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

			/* 只關注要收集的 Componet */
			OracleData oracle = node.getOracleData();
			if (oracle.getLevel() == IOracleHandler.Level.COMPONENT_LEVEL) {
				int index = node.getParent().indexOf(node);

				if (index - 1 >= 0) {
					// 前一個 component
					AbstractNode pre = node.getParent().get(index - 1);
					if (compareComponent(ic, pre) == false)
						continue;
				}

				if (index + 1 <= node.getParent().getChildren().length) {
					// 後一個 component
					AbstractNode next = node.getParent().get(index + 1);
					if (compareComponent(ic, next) == false)
						continue;
				}
			}

			if (oracle.getType() == IOracleHandler.EventType.DEFAULT
					|| oracle.getType() == IOracleHandler.EventType.ALL) {
				// for default event
				OracleUtil.handleDefaultEvent(result_list, ic);
			} else if (oracle.getType() == IOracleHandler.EventType.USER_SELECTED) {
				// for user selected event
				handleUserSelecedtEvent(node, result_list, ic);
			}
		}

		return result_list;
	}

	// handle user selected events
	private static void handleUserSelecedtEvent(OracleNode node,
			List<ViewAssertNode> result_list, IComponent comp) {
		/* user select event */
		Iterator<AbstractNode> ite = node.iterator();
		while (ite.hasNext()) {
			// OracleNode下儲存的，一定是 ViewAssertNode
			ViewAssertNode va = (ViewAssertNode) ite.next();
			if (!(va.getComponent().getType().equals(comp.getType())))
				continue;

			va.setComponent(comp);
			if (OracleUtil.setupExpectedValue(va))
				result_list.add(va);
		}
	}

	private static boolean compareComponent(IComponent ic, AbstractNode n) {
		if (n == null)
			return false;
		if (!(n instanceof EventNode))
			return false;

		Component compa = DefaultComponentFinder.instance().find(ic);
		if (compa == null)
			return false;

		IComponent c = ((EventNode) n).getComponent();
		Component compb = DefaultComponentFinder.instance().find(c);

		return compa.equals(compb);
	}
}
