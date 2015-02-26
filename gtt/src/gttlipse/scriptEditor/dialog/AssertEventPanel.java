/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gtt.oracle.IOracleHandler;
import gtt.testscript.OracleNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.editor.ui.ArgumentPanel;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class AssertEventPanel {

	private OracleNode m_node;

	private Combo m_com_comps;

	private Combo m_com_methods = null;

	private ArgumentPanel m_argsPanel = null;

	public AssertEventPanel(Composite parent, OracleNode _oracle) {
		m_node = _oracle;
		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 200;

		/* Components */
		final Label m_lbl_comps = new Label(area, SWT.NULL);
		m_lbl_comps.setText("Components:");
		m_com_comps = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_comps.setLayoutData(data);
		m_com_comps.setVisibleItemCount(10);

		final IEventModel model = EventModelFactory.getDefault();
		List<IComponent> coms = model.getComponents();
		TreeSet<String> sset_coms = new TreeSet<String>();
		Iterator<IComponent> ite = coms.iterator();
		while (ite.hasNext()) {
			IComponent c = (IComponent) ite.next();
			c.setName("name");
			sset_coms.add(c.getType());
		}

		// 修正沒有 JComponent 和 Component 的問題
		sset_coms.add("javax.swing.JComponent");
		sset_coms.add("java.awt.Component");

		Iterator<String> ite_sset = sset_coms.iterator();
		boolean firstmethod = true;
		while (ite_sset.hasNext()) {
			String com = (String) ite_sset.next();
			m_com_comps.add(com);
			if (firstmethod) {
				m_com_comps.setText(com);
				firstmethod = false;
			}
		}

		/* Event Type */
		final Label m_lbl_events = new Label(area, SWT.NULL);
		m_lbl_events.setText("Event Type:");
		createAssertMethodCombo(area);

		/* Assert Args */
		m_argsPanel = new ArgumentPanel(area, false, ArgumentPanel.ALL_BUTTON);
		// Iterator ite = assertion.getArguIterator();
		// while(ite.hasNext()) {
		// Argument a = (Argument)ite.next();
		// m_argsPanel.addArgument(a.getType(),a.getName(),a.getValue());
		// }
	}

	private void createAssertMethodCombo(final Composite area) {
		// create GUI layout
		m_com_methods = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_methods.setVisibleItemCount(10);
		m_com_methods.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				/* 設定參數面板 */
				Method m = ReflectionUtil.getMethodFromFullString(m_com_comps
						.getText(), m_com_methods.getText());
				Class<?>[] args = m.getParameterTypes();
				m_argsPanel.clearArgument();
				for (Class<?> arg : args) {
					Argument a = Argument.create("", arg.getName(), "");
					m_argsPanel.addArgument(a);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		AddAssertMethodToCombo(m_com_comps.getText());
	}

	private void AddAssertMethodToCombo(String classname) {
		// add assert method to GUI
		try {
			if (!classname.equals("")) {
				Class<?> _class = Class.forName(classname);
				List<Method> methodOthers = ReflectionUtil.getMethodsStartsWith(_class, "");
				boolean firstmethod = true;
				for (int i = 0; i < methodOthers.size(); i++) {
					Method m = (Method) methodOthers.get(i);
					String prefix = "";
					if (m.getName().startsWith("get"))
						prefix = "get";
					else if (m.getName().startsWith("is"))
						prefix = "is";
					m_com_methods.add(prefix
							+ ReflectionUtil.getMethodString(m));
					if (firstmethod == true) {
						m_com_methods.setText(ReflectionUtil
								.getStringOfMethod(m));
						firstmethod = false;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update() {
		m_node.getOracleData().setLevel(IOracleHandler.Level.APPLICATION_LEVEL);
	}
}
