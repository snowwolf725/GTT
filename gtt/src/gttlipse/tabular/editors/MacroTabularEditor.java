package gttlipse.tabular.editors;

import gttlipse.fit.view.SWTResourceManager;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.HeaderAnalyzer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

public class MacroTabularEditor extends TabularEditor {

	private KTable _table = null;
	private TableModel _model = null;

	public MacroTabularEditor() {
		super();
	}
	
	public void dispose() {
		super.dispose();
	}
	
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
	public void createPartControl(Composite parent) {
		_table = new KTable(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWTX.EDIT_ON_KEY);
		_table.setBackground(SWTResourceManager.getColor(255, 255, 255));
		_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		_model = new TableModel(_table);
		_model.addRowItem(TableConstants.FIRST_HEADER_ROW, createHeader());
		_model.checkTableType();
		_model.setTableListener(_table);
		_table.setModel(_model);
	}
	
	@Override
	public void setFocus() {}
	
	@Override
	public TableModel getTableModel() {
		return _model;
	}

	@Override
	public void refresh() {
		_table.redraw();
	}

	@Override
	protected Object[] createHeader() {
		return HeaderAnalyzer.createDefaultHeader(TableConstants.DEFAULT_MACRO_HEADER, _model.getColumnCount());
	}
}
