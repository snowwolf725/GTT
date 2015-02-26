package gttlipse.fit.dialog;

import gttlipse.fit.table.FitTable;
import gttlipse.fit.view.GTTFitViewDefinition;
import gttlipse.fit.view.SWTResourceManager;
import gttlipse.fit.view.ViewInfo;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.kupzog.ktable.KTable;

public class AddNewTableDialog extends TitleAreaDialog implements
		SelectionListener {
	Text m_tableNameText = null;
	Combo m_fixtureTypeCombo = null;
	KTable m_table = null;
	ViewInfo m_viewInfo = null;
	FitTable m_fitTable = null;
	String m_classPath = null;
	String m_fixtureType;
	String m_tableName;

	public AddNewTableDialog(Shell parentShell, String path) {
		super(parentShell);
		m_fitTable = new FitTable();
		m_classPath = path;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Add Table");
		setTitle("Add Table Dialog");
		setMessage("You sholud set table name and fixture type for new table here!");

		final Composite area = new Composite(parent, SWT.NULL);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		area.setLayout(new GridLayout());

		final Composite newTableInfoComposite = new Composite(area, SWT.BORDER
				| SWT.EMBEDDED);
		final GridLayout newTableInfoGridLayout = new GridLayout();
		newTableInfoGridLayout.makeColumnsEqualWidth = true;
		newTableInfoGridLayout.numColumns = 2;
		newTableInfoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 2, 1));
		newTableInfoComposite.setLayout(newTableInfoGridLayout);
		newTableInfoCompositeLayout(newTableInfoComposite);

		return parent;
	}

	public void newTableInfoCompositeLayout(Composite parent) {
		final Label tableNameLabel = new Label(parent, SWT.CENTER);
		tableNameLabel.setFont(SWTResourceManager.getFont("Times New Roman",
				10, SWT.BOLD | SWT.ITALIC));
		tableNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		tableNameLabel.setText("Table Name:");

		m_tableNameText = new Text(parent, SWT.BORDER);
		m_tableNameText.setText("TableName");
		m_tableNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));

		final Label fixtureTypeLabel = new Label(parent, SWT.CENTER);
		fixtureTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman",
				10, SWT.BOLD | SWT.ITALIC));
		fixtureTypeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		fixtureTypeLabel.setText("Fixture Type:");

		m_fixtureTypeCombo = new Combo(parent, SWT.READ_ONLY);
		m_fixtureTypeCombo.setItems(new String[] {
				GTTFitViewDefinition.FixtureTypeDefaultSelection,
				GTTFitViewDefinition.ColumnFixtureSelection,
				GTTFitViewDefinition.RowFixtureSelection,
				GTTFitViewDefinition.ActionFixtureSelection });
		m_fixtureTypeCombo.setFont(SWTResourceManager.getFont(
				"Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		m_fixtureTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));
		m_fixtureTypeCombo
				.select(GTTFitViewDefinition.FixtureTypeDefaultSelectionValue);
		m_fixtureTypeCombo.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				if (m_fixtureTypeCombo.getText().compareTo(
						GTTFitViewDefinition.FixtureTypeDefaultSelection) == 0)
					m_fixtureTypeCombo
							.remove(GTTFitViewDefinition.FixtureTypeDefaultSelectionValue);
			}

			public void focusLost(final FocusEvent e) {
				if (m_fixtureTypeCombo.getText().compareTo("") == 0)
					m_fixtureTypeCombo
							.setText(GTTFitViewDefinition.ColumnFixtureSelection);
			}
		});
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_modify = createButton(parent, SWT.Modify, "Create", true);
		btn_modify.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				// String savePath = "";
				// TableSaver tableSaver = new TableSaver();
				//
				// FitTableCreator.create(m_fitTable, m_viewInfo);
				// savePath = m_classPath +
				// GTTFitViewDefinition.SelectFolder.get(m_fitTable.getFixtureType())
				// + GTTFitViewDefinition.FolderSimbol +
				// m_fitTable.getTableName() +
				// GTTFitViewDefinition.SelectSubtitle.get(m_fitTable.getFixtureType());
				// tableSaver.visit(m_fitTable);
				// tableSaver.saveFile(savePath);
				m_fixtureType = m_fixtureTypeCombo.getText();
				m_tableName = m_tableNameText.getText();
				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	public String getFixtureType() {
		return m_fixtureType;
	}

	public String getTableName() {
		return m_tableName;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
	}
}
