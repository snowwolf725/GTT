package gttlipse.fit.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

public class FitTableViewEditor extends EditorPart {

	public static final String ID = "GTTlipse.fit.table.editor.FitTableViewEditor"; //$NON-NLS-1$

	/**
	 * Create contents of the editor part
	 * 
	 * @param parent
	 */
	public FitTableViewEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		System.currentTimeMillis();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());

		KTable m_table = new KTable(container, SWT.FULL_SELECTION | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL | SWTX.FILL_WITH_LASTCOL
				| SWTX.EDIT_ON_KEY);
		m_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
//		File file = (File) input;
		//System.out.println(file.getName());
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
