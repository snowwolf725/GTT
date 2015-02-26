package gttlipse.fit.view;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;


public class TreeViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	IViewSite m_viewSite;
	IJavaProject m_project;

    public TreeViewContentProvider(IViewSite viewsite) {
    	m_viewSite = viewsite;
    	m_project = null;
    	
    }

    public void initContentProvider(String projectName) {
    	m_project = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProject(projectName);
    }
    
    private void refreshProject() {
    	try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE,null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public IResource[] getElements(Object parent) {
		refreshProject();
		if (parent.equals(m_viewSite)) {
			try {
				if(m_project != null && FitIResourceUtil.getTableFolder(m_project) != null)
					return FitIResourceUtil.getTableFolder(m_project).members();
				return ResourcesPlugin.getWorkspace().getRoot().members();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return getChildren(parent);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public IResource[] getChildren(Object parentElement) {
		if(parentElement.equals(m_viewSite))
			return null;
		IFolder folder = (IFolder)parentElement;
		try {
			return folder.members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof IFolder) {
			IFolder folder = (IFolder)element;
			return folder.getParent();
		} else return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element.equals(m_viewSite))
			return false;
		if(element instanceof IFolder)
			return true;
		return false;
	}
}
