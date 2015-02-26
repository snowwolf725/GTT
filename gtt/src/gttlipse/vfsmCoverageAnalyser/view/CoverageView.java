package gttlipse.vfsmCoverageAnalyser.view;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.ComponentEventNode;
import gttlipse.GTTlipse;
import gttlipse.macro.view.MacroViewPart;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.views.GTTTestScriptView;
import gttlipse.scriptEditor.views.ScriptViewLabelProvider;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.analysis.CoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.EdgePairCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.analysis.EventCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.analysis.StateCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.model.CoverageErrorPairItem;
import gttlipse.vfsmCoverageAnalyser.model.CoverageErrorReport;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmCoverageAnalyser.preprocess.CoveragePreprocessor;
import gttlipse.vfsmCoverageAnalyser.suggest.EdgePairCoverageSuggestionProvider;
import gttlipse.vfsmCoverageAnalyser.suggest.EventCoverageSuggestionProvider;
import gttlipse.vfsmCoverageAnalyser.suggest.StateCoverageSuggestionProvider;
import gttlipse.vfsmCoverageAnalyser.view.dialog.AddClassNodeDialog;
import gttlipse.vfsmCoverageAnalyser.view.dialog.AddMethodNodeDialog;
import gttlipse.vfsmCoverageAnalyser.view.dialog.AddSourceFolderNodeDialog;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMPresenter;
import gttlipse.vfsmEditor.view.VFSMEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;


public class CoverageView extends ViewPart {
	private IProject _project = null;

	private Button _preprocessButton = null;

	private Table _processedTable = null;

	private Combo _selectVfsmCombo = null;

	private Button _analyzeButton;

	private Button _suggestionButton;

	private Button _stateSuggestionCheckButton;

	private Button _transitionSuggestionCheckButton;

	private Button _edgePairSuggestionCheckButton;

	private Button _stateButton;

	private Button _transitionButton;

	private Button _transitionPairButton;

	private FSMInformation _fsmInfo = null;

	private TestCaseInformation _testCaseInfo = null;

	private CoveragePreprocessor _preprocessor = null;

	private StateCoverageAnalyst _stateAnalyst;

	private EventCoverageAnalyst _eventAnalyst;

	private EdgePairCoverageAnalyst _edgePairAnalyst;

	private boolean _stateAnalyzeFlag, _transitionAnalyzeFlag,
			_transitionPairAnalyzeFlag;

	private boolean _stateSuggestionFlag, _eventSuggestionFlag,
			_edgePairSuggestionFlag;

	private TreeViewer _suggestionTreeViewer = null;

	private Tree _coverageTree = null;

	private SuggestionImplementer _suggestionImplementer = null;

	private URL baseurl;

	private Table _resultTable;

	private static ImageRegistry image_registry;

	private IFile file = null;

	final private CompositeNode _invisibleNode = new CompositeNode();;

	final private SourceFolderNode _suggestionFolderNode = new SourceFolderNode();

	ICAPreprocessedTab _icaPreprocessTab;

	public Tree getCoverageTree() {
		return _coverageTree;
	}

	public void setProject(IProject project) {
		_project = project;

		if (_icaPreprocessTab != null)
			_icaPreprocessTab.setProject(_project);
		if (_preprocessButton != null)
			_preprocessButton.setEnabled(true);

		VFSMFileFinder vfsmFinder = new VFSMFileFinder(_project);
		LinkedList<IFile> vfsmFiles = vfsmFinder.getVFSMFiles();

		if (_selectVfsmCombo != null) {
			_selectVfsmCombo.removeAll();
			for (IFile file : vfsmFiles) {
				_selectVfsmCombo.add(file.getName());
			}
		}
	}

	final public BaseNode getBaseNodes() {
		TreeViewer viewer = getTreeViewer();
		TreeItem items[] = viewer.getTree().getItems();
		if (items[0] != null)
			return (ProjectNode) items[0].getData();

		return null;
	}

	final public Tree getTree() {
		GTTTestScriptView scriptView = GTTlipse.showScriptView();
		TreeViewer viewer = scriptView.getTreeViewer();
		return viewer.getTree();
	}

	final public TreeViewer getTreeViewer() {
		GTTTestScriptView scriptView = GTTlipse.showScriptView();
		return scriptView.getTreeViewer();
	}

	final public ScriptViewLabelProvider getScriptViewLabelProvider() {
		TreeViewer viewer = getTreeViewer();

		return (ScriptViewLabelProvider) viewer.getLabelProvider();
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

	private void markErrorItemScriptPart(CoverageErrorPairItem errorItem) {
		getTreeViewer().expandAll();

		Tree tree = getTree();

		TreeItem[] treeItems = tree.getItems();

		LinkedList<TreeItem> queue = new LinkedList<TreeItem>();

		System.out.println("ErrorITemIndex" + errorItem.getErrorIndex());
		queue.add(treeItems[0]);

		String obj = errorItem.getMethodNode().getName();

		while (!queue.isEmpty()) {
			TreeItem treeItem = queue.removeFirst();

			treeItems = treeItem.getItems();

			for (int index = 0; index < treeItems.length; index++)
				queue.addLast(treeItems[index]);

			if (treeItem.getText().equals(obj)) {
				LinkedList<TreeItem> queueForFound = new LinkedList<TreeItem>();

				queueForFound.add(treeItem);

				boolean gotError = false;
				int counter = 0;
				while (!queueForFound.isEmpty()) {

					treeItem = queueForFound.removeFirst();
					treeItems = treeItem.getItems();

					for (int index = 0; index < treeItems.length; index++)
						queueForFound.addLast(treeItems[index]);
					if (gotError == false) {
						if (treeItem.getText().equals(
								errorItem.getMethodNode().getName())) {
							treeItem.setForeground(new Color(null, 254, 0, 0));
						} else if (treeItem.getText().equals(
								errorItem.getEndEventNode().toSimpleString())) {
							System.out.println("Counter " + counter);
							if (errorItem.getErrorIndex() <= counter) {
								treeItem.setForeground(new Color(null, 254, 0,
										0));
								gotError = true;
							} else {
								counter++;
								treeItem.setForeground(new Color(null, 0, 254,
										0));
							}

						} else {
							counter++;
							treeItem.setForeground(new Color(null, 0, 254, 0));
						}
					} else
						treeItem.setForeground(new Color(null, 0, 0, 0));

				}

				getTreeViewer().refresh();
				return;
			}
		}
	}

	private MacroDocument getMacroDoc() {
		MacroDocument macroDoc = null;
		macroDoc = MacroViewPart.getMacroPresenter().getDocument();

		return macroDoc;
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

	private void markErrorItemFSMPart(CoverageErrorPairItem errorItem) {
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

	private void showPathFSMPart(LinkedList<Vertex> vertices) {
		for (int index = 0; index < vertices.size(); index++) {
			List<Edge> edges = vertices.get(index).getOutEdgeList();
			ConnectionBase conn = null;

			if (index != vertices.size() - 1) {
				for (int j = 0; j < edges.size(); j++)
					if (edges.get(j).destination().equals(
							vertices.get(index + 1)))
						conn = edges.get(j).getParentConnection();
			}

			((State) vertices.get(index).getUserObject())
					.setBackgroundColor(new Color(null, 0, 254, 0));
			if (conn != null) {
				((Connection) conn).setIsMarkedErrorPart(true);
			}
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

	private void resetEverything() {
		restoreAllStatesColor();
		restoreAllEdgesColor();
		getVFSMPresenter(file).diplayMainDiagram();

		_processedTable.removeAll();
		_resultTable.removeAll();

		// _selectVfsmCombo.removeAll();
		_selectVfsmCombo.setEnabled(true);

		_preprocessButton.setEnabled(true);

		_analyzeButton.setEnabled(false);

		_suggestionButton.setEnabled(false);

		_stateButton.setEnabled(false);
		_stateButton.setSelection(true);
		_stateAnalyzeFlag = true;

		_transitionButton.setEnabled(false);
		_transitionButton.setSelection(true);
		_transitionAnalyzeFlag = true;

		_transitionPairButton.setEnabled(false);
		_transitionPairButton.setSelection(true);
		_transitionPairAnalyzeFlag = true;

		_stateSuggestionCheckButton.setSelection(false);
		_stateSuggestionCheckButton.setEnabled(false);

		_transitionSuggestionCheckButton.setSelection(false);
		_transitionSuggestionCheckButton.setEnabled(false);

		_edgePairSuggestionCheckButton.setSelection(false);
		_edgePairSuggestionCheckButton.setEnabled(false);

		_stateAnalyzeFlag = _transitionAnalyzeFlag = _transitionPairAnalyzeFlag = true;

		_stateSuggestionFlag = _eventSuggestionFlag = _edgePairSuggestionFlag = false;

		_suggestionFolderNode.clearChildren();
		_suggestionTreeViewer.refresh();

		_coverageTree.removeAll();
	}

	private void initImageRegistry() {
		// init GTTlipse picture
		URL imgurl;
		image_registry = GTTlipse.getDefault().getImageRegistry();
		String picfile[][] = {
				{ TestScriptTage.PROJECT_NODE, "javaProject.png" },
				{ TestScriptTage.SOURCE_FOLDER_NODE, "SourceFolder.png" },
				{ TestScriptTage.PACKAGE_NODE, "Package.png" },
				{ TestScriptTage.CLASS_NODE, "TestCase.png" },
				{ TestScriptTage.METHOD_NODE, "Method.png" },
				{ TestScriptTage.TEST_SCRIPT_DOCUMENT, "TestScriptDocument.png" },
				{ TestScriptTage.TEST_SCRIPT_DOCUMENT_FAIL,
						"TestScriptDocument_Fail.png" },
				{ TestScriptTage.FOLDER_NODE, "FolderNode.png" },
				{ TestScriptTage.EVENT_NODE, "EventNode.png" },
				{ TestScriptTage.EVENT_NODE_FAIL, "EventNode_Fail.png" },
				{ TestScriptTage.ASSERT_NODE_FAIL, "AssertNode_Fail.png" },
				{ TestScriptTage.ASSERT_NODE, "AssertNode.png" },
				{ TestScriptTage.AUT_INFO_NODE, "AUTInfoNode.png" },
				{ TestScriptTage.ABOUT, "about.gif" },
				{ TestScriptTage.DEL, "delete.gif" },
				{ TestScriptTage.EDIT, "edit.jpg" },
				{ TestScriptTage.DEL, "delete.gif" },
				{ TestScriptTage.COPY, "copy.jpg" },
				{ TestScriptTage.CUT, "cut.gif" },
				{ TestScriptTage.PASTE, "paste.gif" },
				{ TestScriptTage.UP, "up.gif" },
				{ TestScriptTage.DOWN, "down.gif" },
				{ TestScriptTage.REFERENCE_MACRO_EVENT_NODE,
						"node2_eventList.gif" },
				{ TestScriptTage.REFERENCE_FIT_NODE, "fitnode.gif" },
				{ TestScriptTage.ORACLE_NODE, "TestOracle.png" },
				{ TestScriptTage.ORACLE_NODE_FAIL, "TestOracle_Fail.png" },
				{ TestScriptTage.OK, "folderOk.gif" },
				{ TestScriptTage.ERROR, "folderErr.gif" } };
		try {
			for (int i = 0; i < picfile.length; i++) {
				if (image_registry.get(picfile[i][0]) == null) {
					// Register image files
					imgurl = new URL(baseurl, picfile[i][1]);
					image_registry.put(picfile[i][0], ImageDescriptor
							.createFromURL(imgurl));
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createPartControl(Composite parent) {
		baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		initImageRegistry();

		parent.setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));

		TabFolder tf = new TabFolder(parent, SWT.BOTTOM);

		TabItem preprocessTab = new TabItem(tf, SWT.BORDER);
		preprocessTab.setText("Preprocess");
		TabItem analysisTab = new TabItem(tf, SWT.BORDER);
		analysisTab.setText("Analysis");
		TabItem suggestionTab = new TabItem(tf, SWT.BORDER);
		suggestionTab.setText("Suggestion");

		Composite preprocessPart = new Composite(tf, SWT.NONE);
		Composite analyzeComposite = new Composite(tf, SWT.NONE);
		Composite suggestionComposite = new Composite(tf, SWT.NONE);

		setUpPreprocessTabView(preprocessPart);
		preprocessTab.setControl(preprocessPart);

		setUpAnalysisTabView(analyzeComposite);
		analysisTab.setControl(analyzeComposite);

		setUpSuggestionTabView(suggestionComposite);
		suggestionTab.setControl(suggestionComposite);
		initializeToolBar();

	}

	private void setUpPreprocessTabView(Composite processTabComposite) {
		Composite selectVfsmComposite = new Composite(processTabComposite,
				SWT.NONE);
		final GridData gd_selectVfsmComposite = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		selectVfsmComposite.setLayoutData(gd_selectVfsmComposite);

		final Group preprocessTableGroup = new Group(processTabComposite,
				SWT.NONE);
		preprocessTableGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		preprocessTableGroup.setLayout(new GridLayout());
		preprocessTableGroup.setText("Preprocessed Result");

		_processedTable = new Table(preprocessTableGroup, SWT.FULL_SELECTION
				| SWT.BORDER);
		_processedTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		TableColumn tc1 = new TableColumn(_processedTable, SWT.CENTER);
		TableColumn tc2 = new TableColumn(_processedTable, SWT.CENTER);

		_processedTable.setHeaderVisible(true);
		tc1.setText("Test Case");
		tc2.setText("FSM");
		tc1.setWidth(300);
		tc2.setWidth(300);

		_processedTable.addSelectionListener(new SelectionListener() {
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
				currentItemIndex = _processedTable.getSelectionIndex();
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

			// private void dispose() {
			//				
			// }

		});

		// selectVfsmGroup.setSize(300, 20);
		// selectVfsmGroup.setLayout(new FillLayout(SWT.HORIZONTAL |
		// SWT.VERTICAL));

		Group selectVfsmGroup = new Group(selectVfsmComposite, SWT.NONE);
		Group resetGroup = new Group(selectVfsmComposite, SWT.NONE);

		resetGroup.setText("Reset Analyser");

		final Label selectLabel = new Label(selectVfsmGroup, SWT.NONE);
		final GridData gd_selectLabel = new GridData();
		selectLabel.setLayoutData(gd_selectLabel);
		selectLabel.setText("Select the VFSM file:");

		_selectVfsmCombo = new Combo(selectVfsmGroup, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		_selectVfsmCombo.setSize(300, 20);
		_selectVfsmCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		_preprocessButton = new Button(selectVfsmGroup, SWT.PUSH);
		_preprocessButton.setLayoutData(new GridData());

		Button resetButton = new Button(resetGroup, SWT.PUSH);

		resetButton.setText("Reset");
		resetButton.setLayoutData(new GridData());
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				resetEverything();
			}

		});

		selectVfsmGroup
				.setText("Choose the file you want to identify consisitency");
		_selectVfsmCombo.setSize(200, 10);

		GridData selectVfsmGroupGridData = new GridData(SWT.FILL, SWT.FILL,
				true, true);
		selectVfsmGroup.setLayout(new GridLayout(3, false));
		selectVfsmGroup.setLayoutData(selectVfsmGroupGridData);

		GridData resetGroupGridData = new GridData(SWT.LEFT, SWT.LEFT, true,
				true);
		resetGroup.setLayout(new GridLayout(1, false));
		resetGroup.setLayoutData(resetGroupGridData);

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
						_fsmInfo = new FSMInformation(superState, getMacroDoc());
						_fsmInfo.printData();
					}
					BaseNode baseNode = getBaseNodes();
					Vector<BaseNode> sourceNodes = new Vector<BaseNode>();
					sourceNodes.add(baseNode);
					_testCaseInfo = new TestCaseInformation(sourceNodes,
							getMacroDoc());
					_testCaseInfo.printData();

					_preprocessor = new CoveragePreprocessor(_testCaseInfo,
							_fsmInfo);
					_preprocessor.preprocess();

					CoverageErrorReport errorReport = _preprocessor
							.getErrorReport();

					for (int index = 0; index < errorReport.getErrorItemSize(); index++) {
						CoverageErrorPairItem errorItem = errorReport
								.getErrorItem(index);
						TableItem ti = new TableItem(_processedTable, SWT.NONE);
						ti.setText(new String[] {
								errorItem.getMethodNode().getName(),
								((State) errorItem.getEndVertex()
										.getUserObject()).getName() });
					}

					_processedTable.update();

					_suggestionImplementer = new SuggestionImplementer(
							getMacroDoc());

					_preprocessButton.setEnabled(false);
					if (_analyzeButton != null)
						_analyzeButton.setEnabled(true);

					_selectVfsmCombo.setEnabled(false);
					_stateButton.setEnabled(true);
					_transitionButton.setEnabled(true);
					_transitionPairButton.setEnabled(true);

				}
			}

		});

		GridLayout pcg = new GridLayout(1, false);
		pcg.makeColumnsEqualWidth = false;
		processTabComposite.setLayout(pcg);
		processTabComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));

		selectVfsmComposite.setLayout(new GridLayout(2, false));

	}

	private void setUpAnalysisTabView(Composite analysisTabComposite) {

		Group analyzeSetUpGroup = new Group(analysisTabComposite, SWT.NONE);
		Group tableGroup = new Group(analysisTabComposite, SWT.NONE);
		Group treeGroup = new Group(analysisTabComposite, SWT.NONE);

		analyzeSetUpGroup.setText("Set Up Coverage Type");
		tableGroup.setText("Coverage Result");
		treeGroup.setText("Information");

		_coverageTree = new Tree(treeGroup, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		_coverageTree
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite textComposite = new Composite(analyzeSetUpGroup, SWT.NONE);
		Composite radioButtonComposite = new Composite(analyzeSetUpGroup,
				SWT.NONE);

		textComposite.setLayout(new GridLayout(1, false));
		radioButtonComposite.setLayout(new GridLayout(1, false));

		Composite checkStatePart = new Composite(radioButtonComposite, SWT.NONE);
		_stateButton = new Button(checkStatePart, SWT.CHECK);
		_stateButton.setText("State Coverage");
		_stateButton.setEnabled(false);
		_stateButton.setSelection(true);
		_stateAnalyzeFlag = _stateButton.getSelection();
		_stateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				_stateAnalyzeFlag = _stateButton.getSelection();

			}

		});

		Composite checkEventPart = new Composite(radioButtonComposite, SWT.NONE);
		_transitionButton = new Button(checkEventPart, SWT.CHECK);
		_transitionButton.setText("Transition Coverage");
		_transitionButton.setEnabled(false);
		_transitionButton.setSelection(true);
		_transitionAnalyzeFlag = _transitionButton.getSelection();
		_transitionButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				_transitionAnalyzeFlag = _transitionButton.getSelection();

			}

		});

		Composite checkEdgePairPart = new Composite(radioButtonComposite,
				SWT.NONE);
		_transitionPairButton = new Button(checkEdgePairPart, SWT.CHECK);
		_transitionPairButton.setText("TransitionPair Coverage");
		_transitionPairButton.setEnabled(false);
		_transitionPairButton.setSelection(true);
		_transitionPairAnalyzeFlag = _transitionPairButton.getSelection();
		_transitionPairButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				_transitionPairAnalyzeFlag = _transitionPairButton
						.getSelection();

			}

		});

		Composite buttonAnalyzeComposite = new Composite(analyzeSetUpGroup,
				SWT.NONE);
		buttonAnalyzeComposite.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));

		_resultTable = new Table(tableGroup, SWT.NONE);
		_resultTable
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumn tc1 = new TableColumn(_resultTable, SWT.CENTER);
		TableColumn tc2 = new TableColumn(_resultTable, SWT.CENTER);
		TableColumn tc3 = new TableColumn(_resultTable, SWT.CENTER);
		TableColumn tc4 = new TableColumn(_resultTable, SWT.CENTER);

		tc1.setText("Coverage Type");
		tc1.setWidth(150);
		tc2.setText("All Count");
		tc2.setWidth(100);
		tc3.setText("Covered Count");
		tc3.setWidth(100);
		tc4.setText("Covereage");
		tc4.setWidth(100);

		_resultTable.setHeaderVisible(true);
		_resultTable.setLinesVisible(true);

		tableGroup.setLayout(new GridLayout(1, false));
		tableGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeGroup.setLayout(new GridLayout(1, false));
		treeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		treeGroup.pack();

		_analyzeButton = new Button(buttonAnalyzeComposite, SWT.PUSH);
		_analyzeButton.setText("Analyze");
		_analyzeButton.setEnabled(false);

		_analyzeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (_stateAnalyzeFlag == true) {
					_stateAnalyst = new StateCoverageAnalyst(_testCaseInfo,
							_fsmInfo);
					System.out.println("Start State Analyze");
					_stateAnalyst.analyze();
					System.out.println("End State Analyze");
					CoverageAnalyzedResult report = _stateAnalyst.getResult();
					TableItem ti = new TableItem(_resultTable, SWT.NONE);
					Integer allCount = report.getAllComponentCount();
					Integer coveredCount = report.getCoveredComponentCount();
					Float coverage = report.getCoverage();
					DecimalFormat myformat = new java.text.DecimalFormat(
							"#0.00");
					String coverageStr = ""
							+ myformat.format(coverage.doubleValue());
					ti.setText(new String[] { "State Coverage",
							allCount.toString(), coveredCount.toString(),
							coverageStr + "%" });
				}
				if (_transitionAnalyzeFlag == true) {
					_eventAnalyst = new EventCoverageAnalyst(_testCaseInfo,
							_fsmInfo);
					_eventAnalyst.analyze();
					CoverageAnalyzedResult report = _eventAnalyst.getResult();
					TableItem ti = new TableItem(_resultTable, SWT.NONE);
					Integer allCount = report.getAllComponentCount();
					Integer coveredCount = report.getCoveredComponentCount();
					Float coverage = report.getCoverage();
					DecimalFormat myformat = new java.text.DecimalFormat(
							"#0.00");
					String coverageStr = ""
							+ myformat.format(coverage.doubleValue());
					ti.setText(new String[] { "Transition Coverage",
							allCount.toString(), coveredCount.toString(),
							coverageStr + "%" });
				}
				if (_transitionPairAnalyzeFlag == true) {
					_edgePairAnalyst = new EdgePairCoverageAnalyst(
							_testCaseInfo, _fsmInfo);
					_edgePairAnalyst.analyze();
					CoverageAnalyzedResult report = _edgePairAnalyst
							.getResult();
					TableItem ti = new TableItem(_resultTable, SWT.NONE);
					Integer allCount = report.getAllComponentCount();
					Integer coveredCount = report.getCoveredComponentCount();
					Float coverage = report.getCoverage();
					DecimalFormat myformat = new java.text.DecimalFormat(
							"#0.00");
					String coverageStr = ""
							+ myformat.format(coverage.doubleValue());
					ti.setText(new String[] { "TransitionPair Coverage",
							allCount.toString(), coveredCount.toString(),
							coverageStr + "%" });
				}
				_stateButton.setEnabled(false);
				_transitionButton.setEnabled(false);
				_transitionPairButton.setEnabled(false);

				_stateSuggestionFlag = false;
				_eventSuggestionFlag = false;
				_edgePairSuggestionFlag = false;

				_stateSuggestionCheckButton.setEnabled(_stateAnalyzeFlag);
				_transitionSuggestionCheckButton
						.setEnabled(_transitionAnalyzeFlag);
				_edgePairSuggestionCheckButton
						.setEnabled(_transitionPairAnalyzeFlag);
				// resultTable.pack();
				_analyzeButton.setEnabled(false);
				_suggestionButton.setEnabled(true);
			}

		});

		_resultTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				TableItem[] tableItems = _resultTable.getSelection();
				if (tableItems[0].getText().equals("State Coverage")) {
					LinkedList<Object> coveredList = _stateAnalyst.getResult()
							.getCoveredPart();
					LinkedList<Object> uncoveredList = _stateAnalyst
							.getResult().getUncoveredPart();

					restoreAllStatesColor();
					restoreAllEdgesColor();

					for (int index = 0; index < coveredList.size(); index++)
						((State) coveredList.get(index))
								.setBackgroundCoveredColor();

					getVFSMPresenter(file).diplayMainDiagram();

					_coverageTree.removeAll();

					for (int index = 0; index < coveredList.size(); index++) {
						TreeItem item = new TreeItem(_coverageTree, SWT.None);
						item
								.setText(((State) coveredList.get(index))
										.getName());
						item.setForeground(new Color(null, 0, 255, 0));
					}

					for (int index = 0; index < uncoveredList.size(); index++) {
						TreeItem item = new TreeItem(_coverageTree, SWT.None);
						item.setText(((State) uncoveredList.get(index))
								.getName());
						item.setForeground(new Color(null, 255, 0, 0));
					}

					_coverageTree.redraw();

				} else if (tableItems[0].getText()
						.equals("Transition Coverage")) {

					LinkedList<Object> coveredList = _eventAnalyst.getResult()
							.getCoveredPart();
					LinkedList<Object> uncoveredList = _eventAnalyst
							.getResult().getUncoveredPart();

					restoreAllStatesColor();
					restoreAllEdgesColor();

					for (int index = 0; index < coveredList.size(); index++) {

						System.out.println((((Edge) coveredList.get(index))
								.getUserObject()).getClass());
						Connection conn = (Connection) ((Edge) coveredList
								.get(index)).getParentConnection();
						conn.setEventCovered(((Edge) coveredList.get(index))
								.getUserObject());
					}

					List<Edge> edges = _fsmInfo.getEdgesList();

					for (int index = 0; index < edges.size(); index++) {
						Connection conn = (Connection) (edges.get(index))
								.getParentConnection();
						conn.setIsSelectedCovered(true);
					}

					getVFSMPresenter(file).diplayMainDiagram();

					_coverageTree.removeAll();

					for (int index = 0; index < coveredList.size(); index++) {
						TreeItem item = new TreeItem(_coverageTree, SWT.None);
						String text = new String();
						text = "("
								+ ((State) ((Edge) coveredList.get(index))
										.origin().getUserObject()).getName()
								+ ")";
						text += ((ComponentEventNode) ((Edge) coveredList
								.get(index)).getUserObject()).getName();
						text += "("
								+ ((State) ((Edge) coveredList.get(index))
										.destination().getUserObject())
										.getName() + ")";
						item.setText(text);
						item.setForeground(new Color(null, 0, 255, 0));
					}

					for (int index = 0; index < uncoveredList.size(); index++) {
						TreeItem item = new TreeItem(_coverageTree, SWT.None);
						String text = new String();
						text = "("
								+ ((State) ((Edge) uncoveredList.get(index))
										.origin().getUserObject()).getName()
								+ ")";
						text += ((ComponentEventNode) ((Edge) coveredList
								.get(index)).getUserObject()).getName();
						text += "("
								+ ((State) ((Edge) uncoveredList.get(index))
										.destination().getUserObject())
										.getName() + ")";
						item.setText(text);
						item.setForeground(new Color(null, 255, 0, 0));
					}

					_coverageTree.redraw();
				}
			}

		});

		buttonAnalyzeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));

		checkStatePart.setLayout(new GridLayout(1, false));
		checkEventPart.setLayout(new GridLayout(1, false));
		checkEdgePairPart.setLayout(new GridLayout(1, false));
		analyzeSetUpGroup.setLayout(new GridLayout(1, true));
		analyzeSetUpGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));

		GridLayout gd = new GridLayout(3, true);
		gd.makeColumnsEqualWidth = false;
		analysisTabComposite.setLayout(gd);
	}

	private void setUpSuggestionTabView(Composite suggestionTabComposite) {

		Group suggestionSetUpGroup = new Group(suggestionTabComposite, SWT.NONE);
		Group treeViewerGroup = new Group(suggestionTabComposite, SWT.NONE);

		final HashMap<String, LinkedList<Object>> suggestionMap = new HashMap<String, LinkedList<Object>>();

		suggestionSetUpGroup.setText("Choose Coverage Type for Suggestion");
		treeViewerGroup.setText("Provided Suggestion");

		Composite textComposite = new Composite(suggestionSetUpGroup, SWT.NONE);
		Composite radioButtonComposite = new Composite(suggestionSetUpGroup,
				SWT.NONE);

		treeViewerGroup.setLayout(new GridLayout(1, false));
		treeViewerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		treeViewerGroup.pack();

		textComposite.setLayout(new GridLayout(1, false));
		radioButtonComposite.setLayout(new GridLayout(1, false));

		Composite checkStatePart = new Composite(radioButtonComposite, SWT.NONE);
		_stateSuggestionCheckButton = new Button(checkStatePart, SWT.CHECK);
		_stateSuggestionCheckButton.setText("Suggestion for State Coverage");
		_stateSuggestionCheckButton.setEnabled(false);
		_stateSuggestionCheckButton
				.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						_stateSuggestionFlag = _stateSuggestionCheckButton
								.getSelection();
					}

				});

		Composite checkEventPart = new Composite(radioButtonComposite, SWT.NONE);
		_transitionSuggestionCheckButton = new Button(checkEventPart, SWT.CHECK);
		_transitionSuggestionCheckButton
				.setText("Suggestion for Transition Coverage");
		_transitionSuggestionCheckButton.setEnabled(false);
		_transitionSuggestionCheckButton
				.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						_eventSuggestionFlag = _transitionSuggestionCheckButton
								.getSelection();
					}

				});

		Composite checkEdgePairPart = new Composite(radioButtonComposite,
				SWT.NONE);
		_edgePairSuggestionCheckButton = new Button(checkEdgePairPart,
				SWT.CHECK);
		_edgePairSuggestionCheckButton
				.setText("Suggestion for Transition-Pair Coverage");
		_edgePairSuggestionCheckButton.setEnabled(false);
		_edgePairSuggestionCheckButton
				.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						_edgePairSuggestionFlag = _edgePairSuggestionCheckButton
								.getSelection();
					}
				});

		_suggestionTreeViewer = new TreeViewer(treeViewerGroup, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		_suggestionTreeViewer.getTree().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		_invisibleNode.addChild(_suggestionFolderNode);
		_suggestionFolderNode.setName("Suggestion");
		_suggestionTreeViewer
				.setContentProvider(new SuggestionTreeViewContentProvider(
						getViewSite(), _invisibleNode));
		_suggestionTreeViewer
				.setLabelProvider(new SuggestionTreeViewLabelProvider(
						image_registry));
		_suggestionTreeViewer.setInput(getViewSite());

		_suggestionTreeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent arg0) {
						IStructuredSelection selection = (IStructuredSelection) _suggestionTreeViewer
								.getSelection();

						if (selection.getFirstElement() instanceof TestMethodNode) {
							LinkedList<Vertex> paths = new LinkedList<Vertex>();
							LinkedList<Object> eventList = suggestionMap
									.get(((TestMethodNode) selection
											.getFirstElement()).getName());

							restoreAllStatesColor();
							restoreAllEdgesColor();

							_preprocessor.trace(eventList, paths);
							showPathFSMPart(paths);
							getVFSMPresenter(file).diplayMainDiagram();

						}

					}

				});

		_suggestionTreeViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					@Override
					public void doubleClick(DoubleClickEvent arg0) {
						IStructuredSelection selection = (IStructuredSelection) _suggestionTreeViewer
								.getSelection();

						if (selection.getFirstElement() instanceof SourceFolderNode) {
							Dialog dialog = new AddSourceFolderNodeDialog(null);
							((AddSourceFolderNodeDialog) dialog)
									.setDefaultText(((SourceFolderNode) selection
											.getFirstElement()).getName());
							((AddSourceFolderNodeDialog) dialog)
									.setSourceFolderNode((SourceFolderNode) selection
											.getFirstElement());
							((AddSourceFolderNodeDialog) dialog)
									.setProjectNode((ProjectNode) getBaseNodes());
							((AddSourceFolderNodeDialog) dialog)
									.setTreeViewer(getTreeViewer());
							dialog.open();

						} else if (selection.getFirstElement() instanceof TestCaseNode) {
							Dialog dialog = new AddClassNodeDialog(null);
							((AddClassNodeDialog) dialog)
									.setDefaultText(((TestCaseNode) selection
											.getFirstElement()).getName());
							((AddClassNodeDialog) dialog)
									.setClassNode((TestCaseNode) selection
											.getFirstElement());
							((AddClassNodeDialog) dialog)
									.setProjectNode((ProjectNode) getBaseNodes());
							((AddClassNodeDialog) dialog)
									.setTreeViewer(getTreeViewer());
							dialog.open();
						} else if (selection.getFirstElement() instanceof TestMethodNode) {
							Dialog dialog = new AddMethodNodeDialog(null);
							((AddMethodNodeDialog) dialog)
									.setDefaultText(((TestMethodNode) selection
											.getFirstElement()).getName());
							((AddMethodNodeDialog) dialog)
									.setMethodNode((TestMethodNode) selection
											.getFirstElement());
							((AddMethodNodeDialog) dialog)
									.setProjectNode((ProjectNode) getBaseNodes());
							((AddMethodNodeDialog) dialog)
									.setTreeViewer(getTreeViewer());
							dialog.open();
						}

					}

				});

		Composite buttonSuggestionComposite = new Composite(
				suggestionSetUpGroup, SWT.NONE);
		buttonSuggestionComposite.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));
		buttonSuggestionComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, false, false));

		_suggestionButton = new Button(buttonSuggestionComposite, SWT.PUSH);
		_suggestionButton.setText("Suggestion");
		_suggestionButton.setEnabled(false);

		_suggestionButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (_stateSuggestionFlag == true) {
					int suggestionCount = 1;
					StateCoverageSuggestionProvider stateSuggestionProvider = new StateCoverageSuggestionProvider(
							_testCaseInfo, _fsmInfo);
					LinkedList<LinkedList<Object>> suggestion = stateSuggestionProvider
							.provideSuggestion(_stateAnalyst.getResult());

					TestCaseNode stateClassNode = new TestCaseNode();
					_suggestionFolderNode.addChild(stateClassNode);
					stateClassNode.setName("StateSuggestions");

					System.out.println("StateCovergeSuggestion"
							+ suggestion.size());
					for (int i = 0; i < suggestion.size(); i++) {
						TestMethodNode methodNode = _suggestionImplementer
								.transformEventList(suggestion.get(i));
						methodNode.setName("testStateSuggestion"
								+ suggestionCount);
						stateClassNode.addChild(methodNode);
						suggestionMap.put(methodNode.getName(), suggestion
								.get(i));
						suggestionCount++;

						// for(int j = 0; j < suggestion.get(i).size(); j++) {
						// System.out.print(((ComponentEventNode)suggestion.get(i).get(j)).getName());
						// System.out.print(">");
						// }
						// System.out.println();
					}
					_suggestionTreeViewer.refresh();

				}
				if (_eventSuggestionFlag == true) {
					int suggestionCount = 1;
					EventCoverageSuggestionProvider eventSuggestionProvider = new EventCoverageSuggestionProvider(
							_testCaseInfo, _fsmInfo);
					LinkedList<LinkedList<Object>> suggestion = eventSuggestionProvider
							.provideSuggestion(_eventAnalyst.getResult());

					TestCaseNode eventClassNode = new TestCaseNode();
					_suggestionFolderNode.addChild(eventClassNode);
					eventClassNode.setName("TranstionSuggestions");

					System.out.println("TransitionCovergeSuggestion");
					for (int i = 0; i < suggestion.size(); i++) {
						TestMethodNode methodNode = _suggestionImplementer
								.transformEventList(suggestion.get(i));
						methodNode.setName("testTransitionSuggestion"
								+ suggestionCount);
						suggestionMap.put(methodNode.getName(), suggestion
								.get(i));
						eventClassNode.addChild(methodNode);
						suggestionCount++;

					}
					_suggestionTreeViewer.refresh();

				}
				if (_edgePairSuggestionFlag == true) {
					int suggestionCount = 1;
					EdgePairCoverageSuggestionProvider edgePairSuggestionProvider = new EdgePairCoverageSuggestionProvider(
							_testCaseInfo, _fsmInfo);
					LinkedList<LinkedList<Object>> suggestion = edgePairSuggestionProvider
							.provideSuggestion(_edgePairAnalyst.getResult());

					TestCaseNode transitionPairClassNode = new TestCaseNode();
					_suggestionFolderNode.addChild(transitionPairClassNode);
					transitionPairClassNode.setName("TranstionPairSuggestions");

					System.out.println("TransitionPairCovergeSuggestion");
					for (int i = 0; i < suggestion.size(); i++) {
						TestMethodNode methodNode = _suggestionImplementer
								.transformEventList(suggestion.get(i));
						methodNode.setName("testTransitionPairSuggestion"
								+ suggestionCount);
						suggestionMap.put(methodNode.getName(), suggestion
								.get(i));
						transitionPairClassNode.addChild(methodNode);
						suggestionCount++;

					}
					_suggestionTreeViewer.refresh();
				}
				_stateSuggestionCheckButton.setEnabled(false);
				_transitionSuggestionCheckButton.setEnabled(false);
				_edgePairSuggestionCheckButton.setEnabled(false);
				_suggestionTreeViewer.expandToLevel(2);
				_suggestionButton.setEnabled(false);
			}

		});

		checkStatePart.setLayout(new GridLayout(1, false));
		checkEventPart.setLayout(new GridLayout(1, false));
		checkEdgePairPart.setLayout(new GridLayout(1, false));
		suggestionSetUpGroup.setLayout(new GridLayout(1, true));
		suggestionSetUpGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));

		GridLayout gd = new GridLayout(2, true);
		gd.makeColumnsEqualWidth = false;
		suggestionTabComposite.setLayout(gd);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void initializeToolBar() {
		getViewSite().getActionBars().getToolBarManager();
	}
}
