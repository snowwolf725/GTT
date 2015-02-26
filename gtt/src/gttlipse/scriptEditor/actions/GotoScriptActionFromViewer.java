/**
 * 
 */
package gttlipse.scriptEditor.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class GotoScriptActionFromViewer implements IEditorActionDelegate {
	private static final String EXT_PROPERTIES = "java";
	private IEditorPart m_targetEditor;


	private boolean onJavaFile;

	/**
	 * 
	 */
	public GotoScriptActionFromViewer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		this.m_targetEditor = targetEditor;
		onJavaFile = false;
		IFile file = getFile();
		if (file != null && file.getFileExtension() != null && file.getFileExtension().equals(EXT_PROPERTIES)) {
			onJavaFile = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// TODO Auto-generated method stub
		if (!onJavaFile) {
			refuse();
		} else {
			ITextEditor myeditor = (ITextEditor)m_targetEditor;
			IDocument doc = myeditor.getDocumentProvider().getDocument(m_targetEditor.getEditorInput());
			TextSelection selection = (TextSelection)myeditor.getSelectionProvider().getSelection();
			GoToScript gotoscript = new GoToScript();
			gotoscript.gotoScriptFromMethodAndDoc(doc,selection.getStartLine(),getFile());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	private final IFile getFile() {
		IFile result = null;
		if (m_targetEditor instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) m_targetEditor;
			IEditorInput input = editor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				result = ((IFileEditorInput) input).getFile();
			}
		}
		return result;
	}

	private Shell getShell() {
		Shell result = null;
		if (m_targetEditor != null) {
			result = m_targetEditor.getSite().getShell();
		} else {
			result = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
		}
		return result;
	}

	private void refuse() {
		String title = "Error";
		String message = "This acion only can used in Java File.";
		MessageDialog.openInformation(getShell(), title, message);
	}
}
