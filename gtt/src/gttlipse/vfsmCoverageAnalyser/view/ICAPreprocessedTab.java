package gttlipse.vfsmCoverageAnalyser.view;

import gttlipse.GTTlipse;
import gttlipse.macro.view.MacroViewPart;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.views.GTTTestScriptView;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.model.CoverageErrorPairItem;
import gttlipse.vfsmCoverageAnalyser.model.CoverageErrorReport;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmCoverageAnalyser.preprocess.CoveragePreprocessor;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMPresenter;
import gttlipse.vfsmEditor.view.VFSMEditor;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;


public class ICAPreprocessedTab {
	private CoveragePreprocessor _preprocessor;

	// private SuggestionImplementer _suggestionImplementer;

	private FSMInformation _fsmInfo;

	private TestCaseInformation _testCaseInfo;

	private Composite _parent;

	private Combo _selectVfsmCombo;

	private Button _preprocessButton;

	private Button _analyzeButton;

	private IProject _project = null;

	private IFile file = null;

	public ICAPreprocessedTab(Composite parent) {
		_parent = parent;
	}

	public void setFSMInformation(FSMInformation fsmInfo) {
		_fsmInfo = fsmInfo;
	}

	public void setTestCaseInformation(TestCaseInformation testCaseInfo) {
		_testCaseInfo = testCaseInfo;
	}

	public void setSuggestionImplementer(
			SuggestionImplementer suggestionImplementer) {
		// _suggestionImplementer = suggestionImplementer;
	}

	public void setPreprocesseButton(Button preprocessButton) {
		_preprocessButton = preprocessButton;
	}

	public void setSelectVfsmCombo(Combo selectVfsmCombo) {
		_selectVfsmCombo = selectVfsmCombo;
	}

	public void setAnalyzeButton(Button analyzeButton) {
		_analyzeButton = analyzeButton;
	}

	public void setProject(IProject project) {
		_project = project;
	}

	private void openVFSMFile(IFile file) {
		FileEditorInput fileinput = new FileEditorInput(file);
		try {
			GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().openEditor(fileinput,
							"GTTlipse.VFSMEditor.ui.VFSMEditor");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AbstractSuperState getSuperState(IFile file) {
		FileEditorInput fileinput = new FileEditorInput(file);
		IEditorReference[] editors = GTTlipse.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findEditors(
						fileinput, "GTTlipse.VFSMEditor.ui.VFSMEditor",
						IWorkbenchPage.MATCH_INPUT);
		VFSMEditor editor = (VFSMEditor) editors[0].getEditor(true);
		return editor.getPresenter().getDiagram().getFSMMain().getMainState();
	}

	final public BaseNode getBaseNodes() {
		TreeViewer viewer = getTreeViewer();

		TreeItem items[] = viewer.getTree().getItems();

		if (items[0] != null)
			return (ProjectNode) items[0].getData();

		return null;
	}

	final public Tree getTree() {
		return getTreeViewer().getTree();
	}

	final public TreeViewer getTreeViewer() {
		GTTTestScriptView scriptView = GTTlipse.showScriptView();
		
		return scriptView.getTreeViewer();
	}

	private IVFSMPresenter getVFSMPresenter(IFile file) {
		FileEditorInput fileinput = new FileEditorInput(file);
		IEditorReference[] editors = GTTlipse.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findEditors(
						fileinput, "GTTlipse.VFSMEditor.ui.VFSMEditor",
						IWorkbenchPage.MATCH_INPUT);
		VFSMEditor editor = (VFSMEditor) editors[0].getEditor(true);
		return editor.getPresenter();
	}

	private void restoreAllStatesColor() {
		List<Vertex> vertices = _fsmInfo.getVerticeList();

		for (int index = 0; index < vertices.size(); index++) {
			((State) vertices.get(index).getUserObject())
					.setBackgroundOriginalColor();
		}
	}

	private void restoreAllEdgesColor() {
		List<Edge> edges = _fsmInfo.getEdgesList();

		for (int index = 0; index < edges.size(); index++) {
			((Connection) edges.get(index).getParentConnection())
					.setIsSelectedCovered(false);
			((Connection) edges.get(index).getParentConnection())
					.setIsMarkedErrorPart(false);
		}
	}

	public void markErrorItemFSMPart(CoverageErrorPairItem errorItem) {
		LinkedList<Vertex> vertices = errorItem.getPath();

		for (int index = 0; index < vertices.size(); index++) {
			if (index == vertices.size() - 1)
				((State) vertices.get(index).getUserObject())
						.setBackgroundColor(new Color(null, 254, 0, 0));
			else {
				List<Edge> edges = vertices.get(index).getOutEdgeList();
				ConnectionBase conn = null;

				for (int j = 0; j < edges.size(); j++)
					if (edges.get(j).destination().equals(
							vertices.get(index + 1)))
						conn = edges.get(j).getParentConnection();

				((State) vertices.get(index).getUserObject())
						.setBackgroundColor(new Color(null, 0, 254, 0));
				if (conn != null) {
					((Connection) conn).setIsMarkedErrorPart(true);
				}
			}
		}

	}

	public void markErrorItemScriptPart(CoverageErrorPairItem errorItem) {
		getTreeViewer().expandAll();

		Tree tree = getTree();

		TreeItem[] treeItems = tree.getItems();

		LinkedList<TreeItem> queue = new LinkedList<TreeItem>();

		queue.add(treeItems[0]);

		String obj = errorItem.getMethodNode().getName();

		while (!queue.isEmpty()) {
			TreeItem treeItem = queue.removeFirst();

			treeItems = treeItem.getItems();
			System.out.println(treeItem.getText() + " has " + treeItems.length
					+ " items.");
			for (int index = 0; index < treeItems.length; index++)
				queue.addLast(treeItems[index]);

			if (treeItem.getText().equals(obj)) {
				LinkedList<TreeItem> queueForFound = new LinkedList<TreeItem>();

				queueForFound.add(treeItem);

				boolean gotError = false;
				while (!queueForFound.isEmpty()) {

					treeItem = queueForFound.removeFirst();
					treeItems = treeItem.getItems();
					System.out.println(treeItems.length);
					for (int index = 0; index < treeItems.length; index++)
						queueForFound.addLast(treeItems[index]);
					if (gotError == false) {
						if (treeItem.getText().equals(
								errorItem.getMethodNode().getName())) {
							treeItem.setForeground(new Color(null, 254, 0, 0));
						} else if (treeItem.getText().equals(
								errorItem.getEndEventNode().toSimpleString())) {
							treeItem.setForeground(new Color(null, 254, 0, 0));
							gotError = true;
						} else
							treeItem.setForeground(new Color(null, 0, 254, 0));
					} else
						treeItem.setForeground(new Color(null, 0, 0, 0));

				}

				getTreeViewer().refresh();
				return;
			}
		}
	}

	private void restoreErrorItemFSMPart(CoverageErrorPairItem errorItem) {
		LinkedList<Vertex> vertices = errorItem.getPath();

		for (int index = 0; index < vertices.size(); index++) {
			if (index == vertices.size() - 1)
				((State) vertices.get(index).getUserObject())
						.setBackgroundOriginalColor();
			else {
				List<Edge> edges = vertices.get(index).getOutEdgeList();
				ConnectionBase conn = null;

				for (int j = 0; j < edges.size(); j++)
					if (edges.get(j).destination().equals(
							vertices.get(index + 1)))
						conn = edges.get(j).getParentConnection();

				((State) vertices.get(index).getUserObject())
						.setBackgroundOriginalColor();
				if (conn != null) {
					((Connection) conn).setIsMarkedErrorPart(false);
				}
			}
		}

	}

	private void markNodeSpecial(String obj, Color color) {
		getTreeViewer().expandAll();

		Tree tree = getTree();

		TreeItem[] treeItems = tree.getItems();

		LinkedList<TreeItem> queue = new LinkedList<TreeItem>();

		queue.add(treeItems[0]);

		// System.out.println("Mark Item:" + obj.toString());
		// System.out.println("Object Type:" + obj.getClass());

		while (!queue.isEmpty()) {
			TreeItem treeItem = queue.removeFirst();

			treeItems = treeItem.getItems();
			// System.out.println(treeItem.getText()+ " has " + treeItems.length
			// + " items.");
			for (int index = 0; index < treeItems.length; index++)
				queue.addLast(treeItems[index]);

			if (treeItem.getText().equals(obj)) {
				LinkedList<TreeItem> queueForFound = new LinkedList<TreeItem>();

				queueForFound.add(treeItem);
				while (!queueForFound.isEmpty()) {
					treeItem = queueForFound.removeFirst();
					treeItems = treeItem.getItems();
					for (int index = 0; index < treeItems.length; index++)
						queueForFound.addLast(treeItems[index]);
					treeItem.setForeground(color);
				}

				getTreeViewer().refresh();
				return;
			}
		}
	}

	public void create() {
		Composite selectVfsmComposite = new Composite(_parent, SWT.NONE);
		final Composite processedTablePart = new Composite(_parent, SWT.NONE);

		Group selectVfsmGroup = new Group(selectVfsmComposite, SWT.NONE);

		_selectVfsmCombo = new Combo(selectVfsmGroup, SWT.DROP_DOWN
				| SWT.READ_ONLY);

		_preprocessButton = new Button(selectVfsmGroup, SWT.PUSH);

		Group processedTableGroup = new Group(processedTablePart, SWT.NONE);
		processedTableGroup.setText("Not match items list");

		final Table processedTable = new Table(processedTableGroup, SWT.BORDER
				| SWT.FULL_SELECTION);
		processedTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		TableColumn tc1 = new TableColumn(processedTable, SWT.CENTER);
		TableColumn tc2 = new TableColumn(processedTable, SWT.CENTER);
		TableColumn tc3 = new TableColumn(processedTable, SWT.CENTER);

		processedTable.setHeaderVisible(true);
		tc1.setText("Test Case");
		tc2.setText("FSM");
		tc3.setText("Description");
		tc1.setWidth(200);
		tc2.setWidth(200);
		tc3.setWidth(500);

		selectVfsmGroup
				.setText("Choose the file you want to identify consisitency");
		_selectVfsmCombo.setSize(200, 10);

		GridData selectVfsmGroupGridData = new GridData(SWT.FILL, SWT.FILL,
				true, true);
		selectVfsmGroupGridData.horizontalSpan = 3;
		selectVfsmGroup.setLayout(new GridLayout(2, false));

		selectVfsmGroup.setLayoutData(selectVfsmGroupGridData);

		_preprocessButton.setText("Preprocess");
		_preprocessButton.setEnabled(false);

		_preprocessButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (_selectVfsmCombo != null) {
					int selectIndex = _selectVfsmCombo.getSelectionIndex();
					String selectItem = _selectVfsmCombo.getItem(selectIndex);

					file = _project.getFile(selectItem);

					openVFSMFile(file);
					AbstractSuperState superState = getSuperState(file);
					if (superState != null) {
						_fsmInfo = new FSMInformation(superState, MacroViewPart
								.getMacroPresenter().getDocument());
						_fsmInfo.printData();

						// ProjectNode projectNode = new
						// ProjectNode("Demo Project");
						// prepareScriptDemoData(projectNode);
						// Vector<BaseNode> sourceNodes = new
						// Vector<BaseNode>();
						// sourceNodes.add(projectNode);
						// _testCaseInfo = new TestCaseInformation(sourceNodes,
						// getMacroDoc());
						//						
						// _testCaseInfo.printData();
						//						
						// IGraph graph = new AdjacencyListGraph();
						// prepareFSMDemoData(graph);
						// _fsmInfo = new FSMInformation(graph);
						// _fsmInfo.printData();
					}
					BaseNode baseNode = getBaseNodes();
					Vector<BaseNode> sourceNodes = new Vector<BaseNode>();
					sourceNodes.add(baseNode);
					_testCaseInfo = new TestCaseInformation(sourceNodes,
							MacroViewPart.getMacroPresenter().getDocument());
					_testCaseInfo.printData();

					_preprocessor = new CoveragePreprocessor(_testCaseInfo,
							_fsmInfo);
					_preprocessor.preprocess();

					CoverageErrorReport errorReport = _preprocessor
							.getErrorReport();

					for (int index = 0; index < errorReport.getErrorItemSize(); index++) {
						CoverageErrorPairItem errorItem = errorReport
								.getErrorItem(index);
						TableItem ti = new TableItem(processedTable, SWT.NONE);
						ti.setText(new String[] {
								errorItem.getMethodNode().getName(),
								((State) errorItem.getEndVertex()
										.getUserObject()).getName(), "No" });
					}

					processedTable.update();

					// _suggestionImplementer = new
					// SuggestionImplementer(getMacroDoc());

					_preprocessButton.setEnabled(false);
					if (_analyzeButton != null)
						_analyzeButton.setEnabled(true);

					_selectVfsmCombo.setEnabled(false);
				}
			}

		});

		processedTable.addSelectionListener(new SelectionListener() {
			Color originalColor = new Color(null, 0, 0, 0);

			int selectedItemIndex = -1;

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int currentItemIndex;
				currentItemIndex = processedTable.getSelectionIndex();
				CoverageErrorReport errorReport = _preprocessor
						.getErrorReport();
				restoreAllStatesColor();
				restoreAllEdgesColor();

				System.out.println("Index:" + currentItemIndex);

				if (currentItemIndex >= 0) {
					if (selectedItemIndex >= 0) {
						markNodeSpecial(errorReport.getErrorItem(
								selectedItemIndex).getMethodNode().getName(),
								originalColor);
						restoreErrorItemFSMPart(errorReport
								.getErrorItem(selectedItemIndex));
					}
					selectedItemIndex = currentItemIndex;
					markErrorItemScriptPart(errorReport
							.getErrorItem(selectedItemIndex));
					markErrorItemFSMPart(errorReport
							.getErrorItem(selectedItemIndex));
					getVFSMPresenter(file).diplayMainDiagram();

				} else
					selectedItemIndex = -1;
			}

		});

		GridLayout pcg = new GridLayout(1, false);
		pcg.makeColumnsEqualWidth = false;
		_parent.setLayout(pcg);
		_parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		selectVfsmComposite.setLayout(new GridLayout(2, false));

		// selectVfsmGroup.setSize(300, 20);
		// selectVfsmGroup.setLayout(new FillLayout(SWT.HORIZONTAL |
		// SWT.VERTICAL));

		GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableGridData.horizontalSpan = 2;
		processedTablePart.setLayout(new GridLayout(1, false));
		processedTablePart.pack();
		processedTablePart.setLayoutData(tableGridData);

	}

}
