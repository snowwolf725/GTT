package gttlipse.tabular.editors;

import gttlipse.tabular.table.TableModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;


public abstract class TabularEditor extends EditorPart {

	@Override
	public void doSave(IProgressMonitor monitor) {}

	@Override
	public void doSaveAs() {}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
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

	@Override
	public abstract void createPartControl(Composite parent);

	@Override
	public abstract void setFocus();
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}
	
	public abstract TableModel getTableModel();
	
	public void setEditorTitle(String title) {
		setPartName(title);
	}
	
	public abstract void refresh();
	
	protected abstract Object[] createHeader();
}
