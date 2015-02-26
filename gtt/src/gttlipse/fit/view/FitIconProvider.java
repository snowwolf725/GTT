package gttlipse.fit.view;

import gttlipse.GTTlipse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


public class FitIconProvider implements ITableLabelProvider{
	ImageRegistry m_imageRegistry = null;
	URL baseurl;

	public FitIconProvider() {
		super();
		initImageRegistry();
	}

	private void initImageRegistry() {
		// init GTTlipse picture
		baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		URL imgurl;
		m_imageRegistry = GTTlipse.getDefault().getImageRegistry();
		String picfile[][] = {
				{ "ComponentNode", "node2_component.gif" },
				{ "ComponentEventNode", "node2_componentEvent.gif" },
				{ "SingleMacroEventNode", "node2_macroEvent.gif" },
				{ "MacroComponentNode", "node2_macro.gif" },
				{ "MacroEventNode", "node2_eventList.gif" },
				{ "ModelAssertNode", "node2_JUnitAssert.gif" },
				{ "ViewAssertNode", "node2_componentAssert.gif" },
				{ "NDefNode", "NDefComponent.png" } ,
				{ "IDontKnow", "IDontKnow.gif" } };
		try {
			 if( m_imageRegistry.get("ComponentNode") != null ) return;
			 for (int i = 0; i < picfile.length; i++) {
				imgurl = new URL(baseurl, picfile[i][1]);
				m_imageRegistry.put(picfile[i][0], ImageDescriptor.createFromURL(imgurl));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

}
