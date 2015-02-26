/**
 * 
 */
package gtt.oracle;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroNodeFactory;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JMenu;

/**
 * @author SnowWolf
 */

public class ComponentCollector {
	private List<IComponent> m_allComps = new Vector<IComponent>();

	private List<IComponent> m_usedComps = new Vector<IComponent>();
	
	private List<Component> m_allJavaComps = new Vector<Component>();

	private MacroComponentNode m_macro_window;

	private MacroComponentNode m_macro_parent;

	private boolean m_bCollect = false;

	private Window m_MainWindow = null;

	public ComponentCollector() {
	}

	public List<IComponent> getAllComps() {
		return m_allComps;
	}

	public List<IComponent> getUsedComps() {
		return m_usedComps;
	}
	
	public List<Component> getAllJavaComps() {
		return m_allJavaComps;
	}

	public void collect(List<IComponent> comps, Component comp) {
		if(comp==null)
			return; // 不需要 collect null 
		if (comp instanceof Container) // for awt.Container
			collectAWTContainerInfo(comps, null, (Container) comp, "", "");
		else
			collectOtherAWTContainerInfo(comps, comp, "", "");
	}

	public void collect(List<IComponent> comps, Component parentcomp,
			Component comp, String winTitle, String winType) {
		m_allJavaComps.add(comp);
		if (comp instanceof Container) // for awt.Container
			collectAWTContainerInfo(comps, parentcomp, (Container) comp,
					winTitle, winType);
		else
			collectOtherAWTContainerInfo(comps, comp, winTitle, winType);
	}

	private void collectOtherAWTContainerInfo(List<IComponent> list,
			Component comp, String winTitle, String winType) {
		IComponent c = SwingComponent.createDefault();
		c.setType(comp.getClass().getName());
		if(comp.getName() == null)
			c.setName("NULL");
		else
			c.setName(comp.getName());
		c.setTitle(winTitle);
		c.setWinType(winType);
		if (!list.contains(c))
			list.add(c);
	}

	private void collectAWTContainerInfo(List<IComponent> list,
			Component parent, Container container, String winTitle,
			String winType) {
		if (parent == null) {
			m_macro_window = new MacroNodeFactory()
					.createMacroComponentNode(container.getName());
			m_macro_parent = m_macro_window;
		}
		
		if (container instanceof Window) {
			winType = "java.awt.Window";
		}

		if (container instanceof Dialog) {
			winTitle = ((Dialog) container).getTitle();
			winType = "java.awt.Dialog";
		}

		if (container instanceof Frame) {
			winTitle = ((Frame) container).getTitle();
			winType = "java.awt.Frame";
		}

		storeIComponent(list, container, winTitle, winType);
		if(container.getClass().toString().startsWith("class javax.swing.JMenuBar"));

		/* visit child Component */
		visitChildrenComponents(list, container, winTitle, winType);
	}
	
	public MacroComponentNode getMacroComp() {
		return m_macro_window;
	}

	private void storeIComponent(List<IComponent> list, Component comp,
			String winTitle, String winType) {
		/* store Component Info */
		IComponent ic = SwingComponent.createDefault();

		String comptype = getSuperClass(comp.getClass());
		ic.setType(comptype);
		if(comp.getName() == null)
			ic.setName("NULL");
		else
			ic.setName(comp.getName());
		ic.setTitle(winTitle);
		ic.setWinType(winType);

		if (!list.contains(ic))
			list.add(ic);
		
		ComponentNode cnode = new MacroNodeFactory().createComponentNode(ic);
		m_macro_parent.add(cnode);
	}

	private void visitChildrenComponents(List<IComponent> list, Container cont,
			String winTitle, String winType) {
		MacroComponentNode parent = m_macro_parent;
		Container children = retriveContainerChildren(cont, parent);

		for (Component theChild : children.getComponents()) {
			collect(list, cont, theChild, winTitle, winType);
		}
		m_macro_parent = parent;
	}

	private Container retriveContainerChildren(Container container, MacroComponentNode parent) {
		MacroNodeFactory factory = new MacroNodeFactory();

		if (container.getClass() == JMenu.class) {
			// JCWang: JMenuItem 是存放在 JPopupMenu 中
			JMenu menu = (JMenu) container;
			String name = menu.getName();
			container = menu.getPopupMenu();
			MacroComponentNode node = factory
					.createMacroComponentNode(name);
			parent.add(node);
			m_macro_parent = node;
		} else if (container.getComponentCount() != 0) {
			if(container.getName() == null) {
				MacroComponentNode node = factory.createMacroComponentNode(container.getClass().toString());
				parent.add(node);
				m_macro_parent = node;
			} else {
				MacroComponentNode node = factory.createMacroComponentNode(container.getName());
				parent.add(node);
				m_macro_parent = node;
			}
		}

		return container;
	}

	private String getSuperClass(Class<?> cls) {
		if (cls == null)
			return "java.lang.Object";
		if (cls.getName().equals("java.lang.Object"))
			return "java.lang.Object";
		if (cls.getName().equals("java.awt.Component"))
			return cls.getName();
		if (cls.getName().equals("javax.swing.JComponent"))
			return cls.getName();

		List<IComponent> comlist = EventModelFactory.getDefault()
				.getComponents();
		Iterator<IComponent> ite = comlist.iterator();
		while (ite.hasNext()) {
			IComponent c = ite.next();
			// class name === component type
			if (cls.getName().equals(c.getType()))
				return cls.getName(); // ok! find it
		}

		return getSuperClass(cls.getSuperclass());
	}

	public void setCollect(boolean flag) {
		m_bCollect = flag;
	}

	public boolean isCollect() {
		return m_bCollect;
	}

	public void setMainWindow(Window win) {
		this.m_MainWindow = win;
	}

	public Window getMainWindow() {
		return m_MainWindow;
	}
}
