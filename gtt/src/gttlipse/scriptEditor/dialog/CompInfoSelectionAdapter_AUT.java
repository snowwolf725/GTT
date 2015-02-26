/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.editor.configuration.IConfiguration;
import gtt.eventmodel.IComponent;
import gtt.runner.Controller;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gttlipse.editor.ui.IComponentInfoPanel;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class CompInfoSelectionAdapter_AUT extends SelectionAdapter {
	private Controller m_controller = new Controller(); // 2003art.

	private boolean m_isRecording = false;

	private IComponentInfoPanel m_componentInfoPanel;

	private AbstractNode m_node;

	private Button m_selecomp;

	public CompInfoSelectionAdapter_AUT(
			IComponentInfoPanel _componentInfoPanel, AbstractNode _node,
			Button _selecomp) {
		m_componentInfoPanel = _componentInfoPanel;
		m_node = _node;
		m_selecomp = _selecomp;
	}

	public void widgetSelected(SelectionEvent e) {
		if (m_isRecording == false) {
			LaunchNode launchNode = getAUTInfoNode(m_node);
			if (launchNode == null || launchNode.getClassName().equals(""))
				return;
			m_controller.reset();
			m_controller.loadAUTbyMain(launchNode.getLaunchData());
			m_controller.startRecord();
			m_controller.showAppWindow();
			m_isRecording = true;
			m_selecomp.setText("Stop capturing");
		} else if (m_isRecording == true) {
			m_isRecording = false;
			m_selecomp.setText("Select Component by capturing");
			List<IComponent> coms = new Vector<IComponent>();

			// 錄影完成後會得到 EventsNode List
			List<AbstractNode> result = m_controller
					.stopRecordAndGetResult(IConfiguration.MEDIUM_LEVEL);
			m_controller.reset();

			Iterator<AbstractNode> ite = result.iterator();
			while (ite.hasNext()) {

				EventNode en = (EventNode) ite.next();
				// ComponentEventData eventdata = (ComponentEventData)
				// ite.next();
				if (en == null)
					continue;
				if (en.getComponent() == null)
					continue;
				//				
				coms.add(en.getComponent());
			}
			// mark by zwshen 2007/11/20
			// if (eventdata.getComponentData() != null) {
			// // resovle origen component type
			// String comptype = getSuperClass(eventdata
			// .getComponentData().getType(), comlist);
			// // set component info
			// IComponent com = SwingComponent.createComponent();
			// com.setType(comptype);
			// if (eventdata.getComponentData().getName() != null)
			// com.setName(eventdata.getComponentData().getName());
			// if (eventdata.getComponentData().getWinType() != null)
			// com.setWinType(eventdata.getComponentData()
			// .getWinType().getName());
			// if (eventdata.getComponentData().getText() != null)
			// com.setText(eventdata.getComponentData().getText());
			// if (eventdata.getComponentData().getTitle() != null)
			// com.setTitle(eventdata.getComponentData().getTitle());
			// com.setIndex(eventdata.getComponentData().getIndex());
			// com.setIndexOfSameName(eventdata.getComponentData()
			// .getIndexOfSameName());
			// coms.add(com);
			// }
			// }
			SelectComponentDialog dialog = new SelectComponentDialog(null, coms);
			dialog.open();
			IComponent comp = dialog.getComponent();
			if (comp != null) {
				m_componentInfoPanel.setComponent(comp);
			}
		}
	}

	// private String getSuperClass(Class childClass, List<IComponent> coms) {
	// if (childClass == null
	// || childClass.getName().equals("java.lang.Object"))
	// return "java.lang.Object";
	//
	// if (childClass.getName().equals("java.awt.Component")
	// || childClass.getName().equals("javax.swing.JComponent"))
	// return childClass.getName();
	//
	// Iterator<IComponent> ite = coms.iterator();
	// while (ite.hasNext()) {
	// IComponent c = (IComponent) ite.next();
	// c.setName("name");
	// if (childClass.getName().equals(c.getType()))
	// return c.getType();
	// }
	// return getSuperClass(childClass.getSuperclass(), coms);
	// }

	private LaunchNode getAUTInfoNode(AbstractNode anode) {
		if (anode instanceof LaunchNode) {
			return (LaunchNode) anode;
		} else if (anode instanceof FolderNode) {
			for (int i = anode.size() - 1; i >= 0; i--) {
				AbstractNode tmp = getAUTInfoNode(anode.get(i));
				if (tmp != null && tmp instanceof LaunchNode) {
					return (LaunchNode) tmp;
				}
			}
		} else if (anode instanceof AbstractNode) {
			int index = anode.getParent().indexOf(anode);
			if (index <= 0) {
				if (anode.getParent().toSimpleString().equals("Root")) {
					return null;
				} else {
					AbstractNode folder = anode.getParent();
					while (folder.getParent().indexOf(folder) == 0) {
						if (folder.getParent().toSimpleString().equals("Root"))
							return null;
						else
							folder = folder.getParent();
					}
					int folderindex = folder.getParent().indexOf(folder);
					LaunchNode tmp = getAUTInfoNode(folder.getParent().get(
							folderindex - 1));
					return tmp;
				}
			}
			do {
				index--;
				LaunchNode tmp = getAUTInfoNode(anode.getParent().get(index));
				if (tmp != null && tmp instanceof LaunchNode)
					return tmp;
			} while (index > 0);
		}
		return null;
	}
}
