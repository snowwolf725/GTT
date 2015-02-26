/**
 * 
 */
package gttlipse.scriptEditor.views;

import gttlipse.GTTlipse;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.views
 * 
 */
public class GTTFileHandler implements IDoubleClickListener {
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (!(selection.getFirstElement() instanceof IFile))
			return;

		IFile file = (IFile) selection.getFirstElement();

		if (!file.getName().endsWith(".gtt"))
			return;

		event.getViewer().setSelection(null);
		GTTTestScriptView view = GTTlipse.showScriptView();
		view.setFocus();
	}
}
