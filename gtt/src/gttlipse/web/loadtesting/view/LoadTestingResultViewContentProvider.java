package gttlipse.web.loadtesting.view;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

public class LoadTestingResultViewContentProvider implements
		IStructuredContentProvider, ITreeContentProvider {
	private IViewSite m_viewSite = null;
	private LoadTestingResultItem invisibleRoot = new LoadTestingResultItem();
	
	public LoadTestingResultItem getRoot() {
		return invisibleRoot;
	}
	
	public LoadTestingResultViewContentProvider(IViewSite viewsite) {
		m_viewSite = viewsite;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof LoadTestingResultItem)
			return ((LoadTestingResultItem)parentElement).getChildren();
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element.toString().equals("InvisibleRoot"))
			return null;
		else return invisibleRoot;
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((LoadTestingResultItem)element).hasChildren();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement.equals(m_viewSite))
			return getChildren(invisibleRoot);
		return getChildren(inputElement);
	}

}

