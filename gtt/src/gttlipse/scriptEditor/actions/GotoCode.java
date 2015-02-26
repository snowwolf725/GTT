/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.testscript.TestScriptDocument;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.MarkerManger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class GotoCode {
	private IFile file = null;

	public GotoCode() {
	}

	public void GotoTestFile(TestCaseNode classnode) {
		ResourceFinder finder = new ResourceFinder();
		file = finder.findIFile(classnode);
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void GotoTestMethod(TestMethodNode methodnode) {
		TestCaseNode classnode = (TestCaseNode) methodnode.getParent();
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		GotoTestFile(classnode);

		MarkerManger factory = new MarkerManger(file);
		factory.createMarker();
		IMarker markers[];
		try {
			markers = file.findMarkers(IMarker.BOOKMARK, true,
					IResource.DEPTH_INFINITE);

			for (int i = 0; i < markers.length; i++) {
				String markerName = markers[i].getAttribute(IMarker.TEXT)
						.toString();
				if (methodnode.getName().matches(markerName)) {
					IDE.openEditor(page, markers[i], true);
					break;
				}
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void GotoTestScriptDoc(TestScriptDocument doc) {
		TestCaseNode classnode;
		TestMethodNode methodnode = null;
		methodnode = (TestMethodNode) doc.getParent();
		classnode = (TestCaseNode) methodnode.getParent();
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		GotoTestFile(classnode);
		MarkerManger factory = new MarkerManger(file);
		factory.createMarker();
		IMarker markers[];
		try {
			markers = file.findMarkers(IMarker.BOOKMARK, true,
					IResource.DEPTH_INFINITE);

			// 得知使用者所選的 Node 所對應的 Marker 為何
			int index = methodnode.indexOf(doc);
			String docmarkername = "D" + index + "_" + doc.getParent().getName()
					+ "_" + doc.getName();
			// 跳到對應的位置
			for (int i = 0; i < markers.length; i++) {
				String markerName = markers[i].getAttribute(IMarker.MESSAGE)
						.toString();
				if (docmarkername.matches(markerName)) {
					IDE.openEditor(page, markers[i], true);
					break;
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
