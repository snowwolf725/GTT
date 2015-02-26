package gtt.tester.macro;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.EventCoverage;
import gtt.macro.MacroCoverageAnalysis;
import gtt.macro.MacroDocument;
import gtt.macro.MacroUtil;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.testscript.EventNode;
import gttlipse.scriptEditor.def.TestScriptTage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class Reporter {
	private MacroDocument m_macroDoc = null;
	private Map<IComponent, Vector<IEvent>> m_ComEventCoverage = null;
	private Map<String, Vector<String>> m_MacroComEventCoverage = null;
	private List<IComponent> m_allComps = null;
	private List<IComponent> m_usedComps = null;
	private List<IComponent> m_nonUsedComps = null;
	private IEventModel m_model = EventModelFactory.getDefault();

	public Reporter(MacroDocument macroDoc) {
		m_macroDoc = macroDoc;
		init();
	}

	private void init() {
		m_ComEventCoverage = new HashMap<IComponent, Vector<IEvent>>();
		m_MacroComEventCoverage = new HashMap<String, Vector<String>>();
	}

	public void initCoverage(AbstractMacroNode p) {
		if (p instanceof ComponentNode) {
			ComponentNode n = (ComponentNode) p;
			EventCoverage cov = n.getEventCoverage();
			Set<String> events = cov.getEvents();

			Iterator<String> ite = events.iterator();
			while (ite.hasNext()) {
				String event = (String) ite.next();
				if (cov.isCover(event)) {
					IEvent e = m_model.getEvent(n.getComponent(), event);
					setCoverage(n.getComponent(), e);
				}
			}
			return;
		}

		if (p instanceof MacroComponentNode) {
			for (int i = 0; i < p.size(); i++) {
				initCoverage(p.get(i));
			}
			return;
		}

		if (p instanceof MacroEventNode) {
			setCoverage((MacroEventNode) p);
			return;
		}

		if (p instanceof ComponentEventNode) {
			return;
		}

		if (p instanceof MacroEventCallerNode) {
			return;
		}
	}

//	public Map<IComponent, Vector<IEvent>> getComponentEventCoverage() {
//		return m_ComEventCoverage;
//	}
//
//	public Map<String, Vector<String>> getMacroComponentEventCoverage() {
//		return m_MacroComEventCoverage;
//	}

	MacroCoverageAnalysis m_MacroCoverAnalysis = new MacroCoverageAnalysis();

	public void setCoverageInfo(List<IComponent> allComps,
			List<IComponent> usedComps) {
		setAllComponents(allComps);
		setUsedComponents(usedComps);
		m_MacroCoverAnalysis.setCoverage(m_ComEventCoverage);
		m_MacroCoverAnalysis.setMacroCoverage(m_MacroComEventCoverage);
	}

	private void setupCoverage(AbstractMacroNode p) {
		if (p instanceof ComponentNode) {
			return;
		}

		if (p instanceof MacroComponentNode) {
			MacroComponentNode par = (MacroComponentNode) p;
			EventCoverage cov = par.getEventCoverage();

			Set<String> events = cov.getNeedToCoverSet();
			Iterator<String> ite = events.iterator();

			while (ite.hasNext()) {
				String event = (String) ite.next();

				if (cov.isCover(event)) {
					AbstractMacroNode child = new DefaultMacroFinder(par)
							.findByName(event);
					setupCoverage(child);
				}
			}

			for (int i = 0; i < par.size(); i++) {
				AbstractMacroNode child = par.get(i);
				if (child instanceof MacroComponentNode)
					setupCoverage(par.get(i));
			}

			return;
		}

		if (p instanceof MacroEventNode) {
			for (int i = 0; i < p.size(); i++)
				setupCoverage(p.get(i));
		}

		if (p instanceof ComponentEventNode) {
			ComponentEventNode n = (ComponentEventNode) p;
			ComponentNode ref = (ComponentNode) n.getComponent();
			if (ref == null)
				return;
			EventCoverage cov = ref.getEventCoverage();
			cov.setCover(n.getEventType(), true);
		}

		if (p instanceof MacroEventCallerNode) {
			MacroEventCallerNode n = (MacroEventCallerNode) p;
			AbstractMacroNode ref = n.getReference();
			if (ref != null)
				setupCoverage(ref);
		}
	}

	public void computeCoverage() {
		this.m_MacroCoverAnalysis.computeCoverage(m_macroDoc);

		// 有時 ComponentNode 的name 一樣時，會變成在第一個ComponentNode才有coverage資訊
		// 現在另外寫一個函式來處理這件事
		setupCoverage(m_macroDoc.getMacroScript());
	}

	private void setAllComponents(List<IComponent> allComps) {
		m_allComps = allComps;
	}

	private void setUsedComponents(List<IComponent> usedComps) {
		m_usedComps = usedComps;
	}

	public void setCoverage(IComponent component, IEvent event) {
		IComponent componentCover = null;

		// 檢查這個component是否已經cover
		Set<IComponent> keys = m_ComEventCoverage.keySet();
		Iterator<IComponent> ite = keys.iterator();
		while (ite.hasNext()) {
			IComponent com = (IComponent) ite.next();
			if (com.equals(component))
				componentCover = com;
		}

		// 這個Component已經在Coverge中
		if (componentCover != null) {
			boolean exist = false;

			Vector<IEvent> events = m_ComEventCoverage.get(componentCover);

			for (int i = 0; i < events.size(); i++) {
				if (events.get(i).equals(event))
					exist = true;
			}

			if (!exist) {
				events.add(event);
			}
		} else {
			Vector<IEvent> events = new Vector<IEvent>();
			events.add(event);
			m_ComEventCoverage.put(component, events);
		}
	}

	public void setCoverage(EventNode node) {
		IComponent componentCover = null;

		// 檢查這個component是否已經cover
		Set<IComponent> keys = m_ComEventCoverage.keySet();
		Iterator<IComponent> ite = keys.iterator();
		while (ite.hasNext()) {
			IComponent com = (IComponent) ite.next();
			if (com.equals(node.getComponent()))
				componentCover = com;
		}

		// 這個Component已經在Coverge中
		if (componentCover != null) {
			boolean exist = false;

			IEvent event = node.getEvent();
			Vector<IEvent> events = m_ComEventCoverage.get(componentCover);

			for (int i = 0; i < events.size(); i++) {
				if (events.get(i).equals(event))
					exist = true;
			}

			if (!exist) {
				events.add(event);
			}
		} else {
			Vector<IEvent> events = new Vector<IEvent>();
			events.add(node.getEvent());
			m_ComEventCoverage.put(node.getComponent(), events);
		}
	}

	public void setCoverage(MacroEventNode node) {
		// 檢查這個component是否已經cover
		String componentCover = null;
		Set<String> keys = m_MacroComEventCoverage.keySet();
		Iterator<String> ite = keys.iterator();

		MacroComponentNode p = (MacroComponentNode) node.getParent();
		if (p == null)
			return;

		while (ite.hasNext()) {
			String com = (String) ite.next();
			if (com.equals(p.getName())) {
				componentCover = com;
			}
		}

		// 這個 component已經cover
		if (componentCover != null) {
			boolean exist = false;

			String event = node.getName();
			Vector<String> events = m_MacroComEventCoverage.get(componentCover);

			for (int i = 0; i < events.size(); i++) {
				if (events.get(i).equals(event))
					exist = true;
			}

			if (!exist) {
				events.add(event);
			}

		} else {
			Vector<String> events = new Vector<String>();
			events.add(node.getName());
			m_MacroComEventCoverage.put(p.getName(), events);
		}
	}

	public void doRemoveNDefNode() {
		MacroComponentNode p = (MacroComponentNode) m_macroDoc.getMacroScript();
		Iterator<AbstractMacroNode> ite = p.iterator();

		Vector<AbstractMacroNode> remove = new Vector<AbstractMacroNode>();
		while (ite.hasNext()) {
			AbstractMacroNode child = (AbstractMacroNode) ite.next();
			if (child instanceof NDefComponentNode) {
				remove.add(child);
			}
		}

		for (int i = 0; i < remove.size(); i++) {
			MacroUtil.removeNode(p, (AbstractMacroNode) remove.get(i));
		}
	}

	public void doSaveReport() {
		List<IComponent> v = getNonUsedComps();

		for (int i = 0; i < v.size(); i++) {
			NDefComponentNode n = new NDefComponentNode(v.get(i));
			MacroUtil.insertNode(m_macroDoc.getMacroScript(), n);
		}

		m_macroDoc.saveFileWithoutEvent(TestScriptTage.GTTLIPSEREPORTFILE);
	}

	private boolean isComExistInDoc(AbstractMacroNode parent, IComponent com) {
		if (parent instanceof MacroComponentNode) {
			MacroComponentNode node = (MacroComponentNode) parent;
			for (int i = 0; i < node.size(); i++) {
				if (isComExistInDoc(node.get(i), com))
					return true;
			}
		}

		if (parent instanceof ComponentNode) {
			ComponentNode node = (ComponentNode) parent;
			if (node.getComponent().equals(com))
				return true;
		}

		if (parent instanceof NDefComponentNode) {
			NDefComponentNode node = (NDefComponentNode) parent;
			if (node.getComponent().equals(com))
				return true;
		}

		return false;
	}

	private List<IComponent> getNonUsedComps() {
		if (m_allComps == null || m_usedComps == null)
			return null;

		m_nonUsedComps = new Vector<IComponent>();
		for (int i = 0; i < m_allComps.size(); i++) {
			if (!isComExistInDoc(m_macroDoc.getMacroScript(), m_allComps.get(i)))
				m_nonUsedComps.add(m_allComps.get(i));
		}
		// System.out.println("Non-Used Components: " + m_nonUsedComps.size());
		return m_nonUsedComps;
	}

}
