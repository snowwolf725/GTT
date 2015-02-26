package gttlipse.fit.view;

import gttlipse.fit.dialog.AddNewTableDialog;
import gttlipse.fit.table.FitTable;
import gttlipse.fit.table.TableItem;
import gttlipse.fit.table.TableRow;
import gttlipse.fit.table.TableSaver;

import java.io.File;

import org.eclipse.jface.viewers.TreeViewer;

import de.kupzog.ktable.KTable;

public class FitViewPresenter {
	TreeViewer m_treeViewer;
	String m_tableFolderPath;
	TreeViewContentProvider m_treeViewContentProvider;
	ViewInfo m_viewInfo;
	KTable m_kTable;
	FitTable m_fitTable;
	String m_projectPath;

	public FitViewPresenter(TreeViewer viewer, KTable table,
			TreeViewContentProvider provider) {
		m_kTable = table;
		m_treeViewer = viewer;
		m_treeViewContentProvider = provider;
	}

//	public FitViewPresenter(KTable table) {
//		m_kTable = table;
//	}

	public void setFitTable(FitTable table) {
		m_fitTable = table;
	}

	public FitTable getFitTable() {
		return m_fitTable;
	}

	public void setViewInfo(ViewInfo viewInfo) {
		m_viewInfo = viewInfo;
	}

	public ViewInfo getViewInfo() {
		return m_viewInfo;
	}

	public void saveFile() {
		TableSaver saver = new TableSaver();
		String p = m_tableFolderPath
				+ GTTFitViewDefinition.SelectFolder.get(m_fitTable
						.getFixtureType())
				+ GTTFitViewDefinition.FolderSimbol
				+ m_fitTable.getTableName()
				+ GTTFitViewDefinition.SelectSubtitle.get(m_fitTable
						.getFixtureType());
		saver.save(m_fitTable, p);
		m_treeViewer.refresh();
	}

	public void newFile() {
		AddNewTableDialog dialog = new AddNewTableDialog(m_treeViewer
				.getControl().getShell(), getTableFolderPath());
		dialog.open();

		if (dialog.getReturnCode() == 1)
			return;

		m_viewInfo = new ViewInfo();
		m_viewInfo.setKTable(m_kTable);
		m_viewInfo.setTableName(dialog.getTableName());
		m_viewInfo.setFixtureType(dialog.getFixtureType());
		m_viewInfo.setTableSize(GTTFitViewDefinition.InitTableColumnSize,
				GTTFitViewDefinition.InitTableRowSize);

		TableViewCompositeCreator.createTable(m_viewInfo);
		updateModel();
		m_treeViewer.refresh();
	}

	public void refreshFile() {
		// System.out.println(m_treeViewer.getSelection().getClass());
		// if(m_treeViewer.getSelection() instanceof IResource) {
		// IResource file = (IResource)m_treeViewer.getSelection();
		// try {
		// if(file != null && file.exists())
		// file.delete(true, null);
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
		// }
		m_treeViewer.refresh();
	}

	// 從viewInfo建立一個對應的fit table的形式，之後應分類成不同fixture，產生不同形式
	static private FitTable createFitTable(ViewInfo viewInfo) {
		FitTable table = new FitTable();
		if (viewInfo == null)
			return table;

		table.clearTable();

		System.out.println("FitTableCreator:"
				+ ((TableItem) viewInfo.createTableModel().getItemObject(
						GTTFitViewDefinition.TableNameTextColumn,
						GTTFitViewDefinition.TableNameTextRow)).getText());
		table.setTableName(((TableItem) viewInfo.createTableModel().getItemObject(
				GTTFitViewDefinition.TableNameTextColumn,
				GTTFitViewDefinition.TableNameTextRow)).getText());
		table.setFixtureType(((TableItem) viewInfo.createTableModel().getItemObject(
				GTTFitViewDefinition.FixtureTypeTextColumn,
				GTTFitViewDefinition.FixtureTypeTextRow)).getText());

		// 儲存fit table中，如果是column table和 row table則是存變數的row，若是action
		// table則是存呼叫的macro
		for (int i = 0; i < viewInfo.getColumnCount(); i++)
			table.addColumnElement(new TableItem(((TableItem) viewInfo.createTableModel()
					.getItemObject(i,
							GTTFitViewDefinition.VariableRowOfColumnFixture))
					.getText(), ((TableItem) viewInfo.createTableModel()
					.getItemObject(i,
							GTTFitViewDefinition.VariableRowOfColumnFixture))
					.getColor()));

		// 儲存fit table中，資料的row，減一是因為扣除變數的row
		for (int i = GTTFitViewDefinition.DataRowOfColumnFixture; i < viewInfo
				.getRowCount()
				+ GTTFitViewDefinition.DataRowOfColumnFixture; i++) {
			TableRow row = new TableRow();
			for (int j = 0; j < viewInfo.getColumnCount(); j++)
				row.add(new TableItem(((TableItem) viewInfo.createTableModel()
						.getItemObject(j, i)).getText(), ((TableItem) viewInfo
						.createTableModel().getItemObject(j, i)).getColor()));
			table.addRowElement(row);
		}

		return table;
	}

	public void updateModel() {
		m_fitTable = createFitTable(m_viewInfo);
	}

	public void Open(String projectPath) {
		m_projectPath = projectPath;
		m_tableFolderPath = m_projectPath
				+ GTTFitViewDefinition.TableFolderName
				+ GTTFitViewDefinition.FolderSimbol;
		m_treeViewContentProvider.initContentProvider(getProjectName());
		initFitProject(projectPath);
		m_treeViewer.refresh();
	}

	public void addColumn() {
		if (m_fitTable.addColumnSize())
			m_viewInfo.createTableModel().addColumnSize();
		updateView();
	}

	public void subColumn() {
		if (m_fitTable.subColumnSize())
			m_viewInfo.createTableModel().subColumnSize();
		updateView();
	}

	public void addRow() {
		if (m_fitTable.addRowSize())
			m_viewInfo.createTableModel().addRowSize();
		updateView();
	}

	public void subRow() {
		if (m_fitTable.subRowSize())
			m_viewInfo.createTableModel().subRowSize();
		updateView();
	}

	public String getProjectPath() {
		return m_projectPath;
	}

	public String getTableFolderPath() {
		return m_tableFolderPath;
	}

	public String getProjectName() {
		String split[] = m_projectPath.split("/");
		return split[split.length - 1];
	}

	public String getProjectRoot() {
		String path = m_projectPath;
		path = path.substring(0, path.lastIndexOf("/") - 1);
		path = path.substring(0, path.lastIndexOf("/"));
		return path;
	}

	public void updateView() {
		try {
			m_viewInfo = new ViewInfo();
			m_viewInfo.setKTable(m_kTable);
			m_viewInfo.setTableName(m_fitTable.getTableName());
			m_viewInfo.setFixtureType(m_fitTable.getFixtureType());
			m_viewInfo.setTableSize(m_fitTable.getColumnCount(), m_fitTable
					.getRowCount());
			TableViewCompositeCreator.refreshTable(m_fitTable, m_viewInfo);
		} catch (NullPointerException e) {
			System.err.println("Fit table in fitPresenter is null");
		}
	}

	// initial project belong fit and get project path by
	// SelectJavaProjectDialog in GTTScriptEditor
	public void initFitProject(String projectPath) {
		File tableFolder = new File(projectPath
				+ GTTFitViewDefinition.TableFolderName);

		if (!tableFolder.exists()) {
			new File(projectPath + GTTFitViewDefinition.TableFolderName)
					.mkdir();
			createSubfolder(projectPath);
		} else
			createSubfolder(projectPath);
	}

	private void createSubfolder(String path) {
		File columnTableFolder = new File(m_tableFolderPath
				+ GTTFitViewDefinition.ColumnTableFolderName);
		File rowTableFolder = new File(m_tableFolderPath
				+ GTTFitViewDefinition.RowTableFolderName);
		File ActionTableFolder = new File(m_tableFolderPath
				+ GTTFitViewDefinition.ActionTableFolderName);
		if (!columnTableFolder.exists())
			columnTableFolder.mkdir();
		if (!rowTableFolder.exists())
			rowTableFolder.mkdir();
		if (!ActionTableFolder.exists())
			ActionTableFolder.mkdir();
	}
}
