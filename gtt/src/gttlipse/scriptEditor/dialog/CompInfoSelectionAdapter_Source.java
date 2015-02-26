/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.IComponent;
import gttlipse.EclipseProject;
import gttlipse.editor.ui.IComponentInfoPanel;
import gttlipse.scriptEditor.util.CompInfoCollector;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class CompInfoSelectionAdapter_Source extends SelectionAdapter {

	private IComponentInfoPanel m_componentInfoPanel;

	private Shell m_shell;

	public CompInfoSelectionAdapter_Source(
			IComponentInfoPanel _componentInfoPanel, Shell _shell) {
		m_componentInfoPanel = _componentInfoPanel;
		m_shell = _shell;
	}

	public void widgetSelected(SelectionEvent e) {
		// 取得使用者所選的檔案並進行元件分析
		IProject project = EclipseProject.getEclipseProject();
		if (project == null)
			return;
		ElementTreeSelectionDialog typeseledialog = new ElementTreeSelectionDialog(
				m_shell, new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		typeseledialog.setInput(project);
		typeseledialog.open();
		Object[] objs = typeseledialog.getResult();
		if (objs == null)
			return;
		final CompInfoCollector collector = new CompInfoCollector();
		for (Object obj : objs) {
			if (!(obj instanceof IResource))
				continue;
			try {
				IResource resource = (IResource) obj;
				resource.accept(new IResourceVisitor() {

					public boolean visit(IResource resource)
							throws CoreException {
						if (resource.getType() == IResource.FILE
								&& resource.getFileExtension().equals("java")) {
							IFile file = (IFile) resource;
							if (!file.getFileExtension().equals("java"))
								return false;
							collector.addFile(file);
							return false;
						} else
							return true;
					}

				}, IResource.DEPTH_INFINITE, IResource.FILE);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// 列出分析結果
		Vector<IComponent> comps = collector.getResult();
		SelectComponentDialog dialog = new SelectComponentDialog(null, comps);
		dialog.open();
		IComponent comp = dialog.getComponent();
		if (comp != null) {
			m_componentInfoPanel.setComponent(comp);
		}
	}
}
