package gttlipse.macro.report;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.NDefComponentNode;

public class CoverageCount {

	private double m_componentCoverage = 0;
	private double m_eventCoverage = 0;

	public CoverageCount() {

	}

	public double getComponentCoverage() {
		return m_componentCoverage;
	}

	public double getEventCoverage() {
		return m_eventCoverage;
	}

	public void doRefreshCoverage(AbstractMacroNode root) {
		int cnt = getComponentCnt(root);
		int coverCnt = getComponentCoverCnt(root);

		if (cnt != 0)
			m_componentCoverage = coverCnt * 100 / cnt;

		cnt = getEventCnt(root);
		coverCnt = getEventCoverCnt(root);

		if (cnt != 0)
			m_eventCoverage = coverCnt * 100 / cnt;

	}

	private int getComponentCnt(AbstractMacroNode parent) {
		if (parent instanceof MacroComponentNode) {
			int cnt = 1;
			for (int i = 0; i < parent.size(); i++) {
				cnt += getComponentCnt(parent.get(i));
			}

			return cnt;
		}

		if (parent instanceof ComponentNode) {
			return 1;
		}

		if (parent instanceof NDefComponentNode) {
			return 1;
		}

		return 0;
	}

	private int getComponentCoverCnt(AbstractMacroNode parent) {
		if (parent instanceof MacroComponentNode) {
			MacroComponentNode n = (MacroComponentNode) parent;
			int cnt;

			if (n.getEventCoverage().getCoverage() > 0)
				cnt = 1;
			else
				cnt = 0;

			for (int i = 0; i < parent.size(); i++) {
				cnt += getComponentCoverCnt(parent.get(i));
			}

			return cnt;
		}

		if (parent instanceof ComponentNode) {
			ComponentNode n = (ComponentNode) parent;
			if (n.getEventCoverage().getCoverage() > 0)
				return 1;
			else
				return 0;
		}

		return 0;
	}

	private int getEventCnt(AbstractMacroNode parent) {
		if (parent instanceof MacroComponentNode) {
			MacroComponentNode n = (MacroComponentNode) parent;
			int cnt;

			cnt = n.getEventCoverage().getNeedToCoverSize();

			for (int i = 0; i < parent.size(); i++) {
				cnt += getEventCnt(parent.get(i));
			}

			return cnt;
		}

		if (parent instanceof ComponentNode) {
			ComponentNode n = (ComponentNode) parent;
			return n.getEventCoverage().getNeedToCoverSize();
		}

		return 0;
	}

	private int getEventCoverCnt(AbstractMacroNode parent) {
		if (parent instanceof MacroComponentNode) {
			MacroComponentNode n = (MacroComponentNode) parent;
			int cnt;

			cnt = n.getEventCoverage().getCoverSize();

			for (int i = 0; i < parent.size(); i++) {
				cnt += getEventCoverCnt(parent.get(i));
			}

			return cnt;
		}

		if (parent instanceof ComponentNode) {
			ComponentNode n = (ComponentNode) parent;
			return n.getEventCoverage().getCoverSize();
		}

		return 0;
	}

}
