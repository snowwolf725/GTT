/**
 * 
 */
package gttlipse.scriptEditor.views;

import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.io.XmlTestScriptSaveVisitor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.views
 * 
 */
public class SelectionChangHandler implements ISelectionListener {
	TreeViewer m_viewer = null;

	public void setViewer(TreeViewer viewer) {
		m_viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		IProject currentproject = null;
		ISelection selections = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getSelection();
		if (selections instanceof IStructuredSelection) {
			Object[] selectedObjects = ((IStructuredSelection) selections)
					.toArray();
			for (int i = 0; i < selectedObjects.length; i++) {
				if (selectedObjects[i] instanceof IResource) {
					IResource resource = (IResource) selectedObjects[i];
					currentproject = resource.getProject();
				} else if (selectedObjects[i] instanceof IJavaElement) {
					IJavaElement resource = (IJavaElement) selectedObjects[i];
					currentproject = resource.getJavaProject().getProject();
				}
			}
		}
		if (currentproject != null && currentproject.isOpen()) {
			// save old script
			IProject project = null;
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject projects[] = root.getProjects();
			String projectname = TestProject.getProject().getName();
			for (int i = 0; i < projects.length; i++) {
				if (projects[i].getName().matches(projectname)
						&& projects[i].isOpen())
					project = projects[i];
			}
			if (project != null) {
				String gttfile = project.getLocation().toString()
						+ "/GTTlipse.gtt";
				XmlTestScriptSaveVisitor v = new XmlTestScriptSaveVisitor();
				TestProject.getProject().accept(v);
				v.saveFile(gttfile);
			}
			// load new Script
			if (loadProjectScript(currentproject)) {
				TestProject.updateScriptSync();
			} else {
				TestProject.initProject();
			}
		}
		if (m_viewer != null)
			m_viewer.refresh();
	}

	public boolean loadProjectScript(IProject project) {
		IFile file = project.getFile(new Path("GTTlipse.gtt"));
		System.out.println("Script:" + file.getLocation());
		if (file == null || !file.exists()) {
			System.out.println("Test Script not found.");
			return false;
		}
		TestProject.loadTestScript();
		return true;
	}

}
