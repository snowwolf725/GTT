package gttlipse.macro.view;

import gttlipse.GTTlipse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


public class BadSmellListViewLabelProvider implements ITableLabelProvider {
	private ImageRegistry m_imageRegistry = null;
	
	BadSmellListViewLabelProvider() {
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

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex != 0)
			return null;

		if (element instanceof BadSmellItem)
			return m_imageRegistry.get("NDefNode");

		return m_imageRegistry.get("IDontKnow");
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element == null)
			return "";
		if (element instanceof BadSmellItem) {
			BadSmellItem item = (BadSmellItem)element;
			if(columnIndex == 0)
				return item.getImportance() + "";
			else if(columnIndex == 1)
				return item.getType();
			else if(columnIndex == 2)
				return item.getPath();
		}
		return "";
	}

}
