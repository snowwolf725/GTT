package gttlipse.macro.report;

import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.util.Pair;
import gttlipse.GTTlipse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


public class CoverageReportLabelProvider implements ITableLabelProvider {
	private ImageRegistry m_imageRegistry = null;

	public CoverageReportLabelProvider() {
		super();
		initImageRegistry();
	}

	private void initImageRegistry() {
		String pictures[][] = { { "ComponentNode", "node2_component.gif" },
				{ "ComponentEventNode", "node2_componentEvent.gif" },
				{ "MacroComponentNode", "node2_macro.gif" },
				{ "MacroEventNode", "node2_eventList.gif" },
				{ "MacroEventCallerNode", "node2_macroEvent.gif" },
				{ "ModelAssertNode", "node2_JUnitAssert.gif" },
				{ "ViewAssertNode", "node2_componentAssert.gif" },
				{ "ExistenceAssertNode", "node2_componentExistAssert.gif" },
				{ "NDefNode", "NDefComponent.png" },
				{ "IDontKnow", "IDontKnow.gif" } };
		m_imageRegistry = GTTlipse.getDefault().getImageRegistry();
		if (m_imageRegistry.get("ComponentNode") != null)
			return;

		try {
			URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");

			for (int idxOfPicture = 0; idxOfPicture < pictures.length; idxOfPicture++) {
				URL imgurl = new URL(baseurl, pictures[idxOfPicture][1]);
				m_imageRegistry.put(pictures[idxOfPicture][0], ImageDescriptor
						.createFromURL(imgurl));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex != 0)
			return null;

		if (element instanceof NDefComponentNode)
			return m_imageRegistry.get("NDefNode");
		if (element instanceof ComponentNode)
			return m_imageRegistry.get("ComponentNode");
		if (element instanceof MacroEventCallerNode)
			return m_imageRegistry.get("MacroEventCallerNode");
		if (element instanceof MacroComponentNode)
			return m_imageRegistry.get("MacroComponentNode");
		if (element instanceof MacroEventNode)
			return m_imageRegistry.get("MacroEventNode");
		if (element instanceof ModelAssertNode)
			return m_imageRegistry.get("ModelAssertNode");
		if (element instanceof ViewAssertNode)
			return m_imageRegistry.get("ViewAssertNode");
		if (element instanceof ExistenceAssertNode)
			return m_imageRegistry.get("ExistenceAssertNode");

		return m_imageRegistry.get("IDontKnow");
	}

	private String doFormat(int covered, int total) {
		double coverage = 0;
		if (total == 0) {
			if (covered == 0)
				coverage = -1;
			else
				coverage = covered * 100;
		} else
			coverage = covered * 100 / total;

		String format = "";
		if (coverage < 0)
			format += "       N/A, ";
		else {
			if (coverage < 10)
				format += "    ";
			else if (coverage < 100)
				format += "  ";
			format += coverage + "% , ";
		}

		if (covered < 10)
			format += "    ";
		else if (covered < 100)
			format += "  ";

		format += covered + "/";
		format += total;

		return format;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element == null)
			return "";

		int PE_covered = 0;
		int PE_total = 0;

		int PC_covered = 0;
		int PC_total = 0;

		int ME_covered = 0;
		int ME_total = 0;

		int MC_covered = 0;
		int MC_total = 0;

		if (element instanceof MacroComponentNode) {
			MacroComponentNode node = (MacroComponentNode) element;

			Pair totalEventCoverage = node.getTotalEventCoverage();

			PE_covered = Integer.parseInt(totalEventCoverage.first.toString());
			PE_total = Integer.parseInt(totalEventCoverage.second.toString());

			Pair totalComponentCoverage = node.getTotalComponentCoverage();
			PC_covered = Integer.parseInt(totalComponentCoverage.first
					.toString());
			PC_total = Integer.parseInt(totalComponentCoverage.second
					.toString());

			Pair totalMacroEventCoverage = node.getTotalMacroEventCoverage();
			ME_covered = Integer.parseInt(totalMacroEventCoverage.first
					.toString());
			ME_total = Integer.parseInt(totalMacroEventCoverage.second
					.toString());

			Pair totalMacroComponentCoverage = node
					.getTotalMacroComponentCoverage();
			MC_covered = Integer.parseInt(totalMacroComponentCoverage.first
					.toString());
			MC_total = Integer.parseInt(totalMacroComponentCoverage.second
					.toString());
		}

		if (element instanceof ComponentNode) {
			ComponentNode node = (ComponentNode) element;
			EventCoverage cov = node.getEventCoverage();

			PE_covered = cov.getCoverSize();
			PE_total = cov.getNeedToCoverSize();
		}

		if (element instanceof NDefComponentNode) {
			NDefComponentNode node = (NDefComponentNode) element;
			EventCoverage cov = node.getEventCoverage();

			PE_covered = cov.getCoverSize();
			PE_total = cov.getNeedToCoverSize();
		}

		switch (columnIndex) {
		case 0:
			return element.toString();
		case 1:
			return doFormat(PE_covered, PE_total);
		case 2:
			if (!(element instanceof MacroComponentNode))
				return "";
			return doFormat(PC_covered, PC_total);
		case 3:
			if (!(element instanceof MacroComponentNode))
				return "";
			return doFormat(ME_covered, ME_total);
		case 4:
			if (!(element instanceof MacroComponentNode))
				return "";
			return doFormat(MC_covered, MC_total);
		default:
			return "";
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
