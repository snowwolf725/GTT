package gttlipse.componentEditor;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;



public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	private ComponentTreeNode invisibleRoot = null;
	private IViewSite m_viewSite = null;

    ViewContentProvider(IViewSite viewsite) {
    	m_viewSite = viewsite;
    }
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (parent.equals( m_viewSite )) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		return ((ComponentTreeNode)child).getParent();
	}

	public Object[] getChildren(Object parent) {
		return invisibleRoot.getChildren();
	}

	public boolean hasChildren(Object parent) {
		return ((ComponentTreeNode)parent).size() > 0 ? true : false;
	}

	private void initialize() {
		invisibleRoot = new ComponentTreeNode("");

		final IEventModel model = EventModelFactory.getDefault();
		List<IComponent> coms = model.getComponents();
		TreeSet<String> sset_coms = new TreeSet<String>();
		Iterator<IComponent> ite = coms.iterator();
		while (ite.hasNext()) {
			IComponent c = ite.next();
			c.setName("name");
			sset_coms.add(c.getType());
		}
		
		Iterator<String> sset_ite = sset_coms.iterator();
		while (ite.hasNext()) {
			invisibleRoot.add( new ComponentTreeNode(sset_ite.next()) );
		}
	}
}
