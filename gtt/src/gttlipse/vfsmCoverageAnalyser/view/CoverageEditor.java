package gttlipse.vfsmCoverageAnalyser.view;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingModel;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.analysis.CoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.EdgePairCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.analysis.EventCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.analysis.StateCoverageAnalyst;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmCoverageAnalyser.suggest.EdgePairCoverageSuggestionProvider;
import gttlipse.vfsmCoverageAnalyser.suggest.EventCoverageSuggestionProvider;
import gttlipse.vfsmCoverageAnalyser.suggest.StateCoverageSuggestionProvider;
import gttlipse.vfsmEditor.model.State;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;


public class CoverageEditor extends GraphicalEditorWithPalette {
	private static ImageRegistry image_registry;

	private URL baseurl;

	private TreeViewer _treeView;

	// private VFSMDiagram _vfsmDiagram;

	private TestCaseInformation _testCaseInfo;

	private ProjectNode _projectNode;

	private FSMInformation _fsmInfo;

	private IGraph _graph;

	private Set<Object> m_failnodes;

	private StateCoverageAnalyst _stateCoverageAnalyst;

	private EventCoverageAnalyst _eventCoverageAnalyst;

	private EdgePairCoverageAnalyst _edgePairCoverageAnalyst;

	public CoverageEditor() {
		_projectNode = new ProjectNode("Demo Project");
		prepareScriptDemoData(_projectNode);

		Vector<BaseNode> sourceNodes = new Vector<BaseNode>();
		sourceNodes.add(_projectNode);

		_testCaseInfo = new TestCaseInformation(sourceNodes, null);
		// _testCaseInfo.printData();

		_graph = new AdjacencyListGraph();
		prepareFSMDemoData(_graph);
		_fsmInfo = new FSMInformation(_graph);
		// _fsmInfo.printData();

		_stateCoverageAnalyst = new StateCoverageAnalyst(_testCaseInfo,
				_fsmInfo);
		_eventCoverageAnalyst = new EventCoverageAnalyst(_testCaseInfo,
				_fsmInfo);
		_edgePairCoverageAnalyst = new EdgePairCoverageAnalyst(_testCaseInfo,
				_fsmInfo);

		_stateCoverageAnalyst.analyze();

		CoverageAnalyzedResult stateReport = _stateCoverageAnalyst.getResult();

		System.out.println(stateReport.getCoverage() * 100 + "%");
		System.out.println(stateReport.getAllComponentCount());

		StateCoverageSuggestionProvider stateSuggestionProvider = new StateCoverageSuggestionProvider(
				_testCaseInfo, _fsmInfo);
		LinkedList<LinkedList<Object>> suggestion = stateSuggestionProvider
				.provideSuggestion(stateReport);

		System.out.println("StateCovergeSuggestion");
		for (int i = 0; i < suggestion.size(); i++) {
			for (int j = 0; j < suggestion.get(i).size(); j++) {
				System.out.print(((EventNode) suggestion.get(i).get(j))
						.toDetailString());
				System.out.print(">");
			}
			System.out.println();
		}

		_eventCoverageAnalyst.analyze();

		CoverageAnalyzedResult stateReport2 = _eventCoverageAnalyst.getResult();

		System.out.println(stateReport2.getCoverage() * 100 + "%");
		System.out.println(stateReport2.getAllComponentCount());

		EventCoverageSuggestionProvider eventSuggestionProvider = new EventCoverageSuggestionProvider(
				_testCaseInfo, _fsmInfo);
		LinkedList<LinkedList<Object>> suggestion2 = eventSuggestionProvider
				.provideSuggestion(stateReport2);

		System.out.println("EventCovergeSuggestion");
		for (int i = 0; i < suggestion2.size(); i++) {
			for (int j = 0; j < suggestion2.get(i).size(); j++) {
				System.out.print(((EventNode) suggestion2.get(i).get(j))
						.toDetailString());
				System.out.print(">");
			}
			System.out.println();
		}

		_edgePairCoverageAnalyst.analyze();

		CoverageAnalyzedResult stateReport3 = _edgePairCoverageAnalyst
				.getResult();

		System.out.println(stateReport3.getCoverage() * 100 + "%");
		System.out.println(stateReport3.getAllComponentCount());

		EdgePairCoverageSuggestionProvider edgePairSuggestionProvider = new EdgePairCoverageSuggestionProvider(
				_testCaseInfo, _fsmInfo);
		LinkedList<LinkedList<Object>> suggestion3 = edgePairSuggestionProvider
				.provideSuggestion(stateReport3);

		System.out.println("EdgePairCovergeSuggestion");
		for (int i = 0; i < suggestion3.size(); i++) {
			for (int j = 0; j < suggestion3.get(i).size(); j++) {
				System.out.print(((EventNode) suggestion3.get(i).get(j))
						.toDetailString());
				System.out.print(">");
			}
			System.out.println();
		}

	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Composite treePart = new Composite(parent, SWT.NONE);
		Composite vfsmPart = new Composite(parent, SWT.NONE);

		initTreeViewPart(treePart);
		initVFSMPart(vfsmPart);

		baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		initImageRegistry();

		_treeView.setContentProvider(new CoverageViewContentProvider(
				getEditorSite(), _projectNode));
		_treeView.setLabelProvider(new CoverageViewLabelProvider(
				image_registry, m_failnodes));
		_treeView.setInput(getEditorSite());

		Tree tree = _treeView.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private void initTreeViewPart(Composite treePart) {
		treePart.setLayout(new GridLayout(1, true));
		treePart.setLayoutData(new GridData(600, SWT.FILL, false, true));

		// Composite treePartReadPart = new Composite(treePart, SWT.NONE);
		_treeView = new TreeViewer(treePart, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		Composite treePartCoveragePart = new Composite(treePart, SWT.NONE);

		// treePartReadPart.setLayout(new GridLayout(3, false));

		// Text urlText = new Text(treePartReadPart, SWT.SINGLE);
		// Button buttonPath = new Button(treePartReadPart, SWT.PUSH);
		// Button buttonPush = new Button(treePartReadPart, SWT.PUSH);

		Composite checkStatePart = new Composite(treePartCoveragePart, SWT.NONE);
		Composite checkEventPart = new Composite(treePartCoveragePart, SWT.NONE);
		Composite analyzePart = new Composite(treePartCoveragePart, SWT.NONE);

		checkStatePart.setLayout(new GridLayout(3, false));
		checkEventPart.setLayout(new GridLayout(3, false));
		analyzePart.setLayout(new GridLayout(1, false));

		Label stateText = new Label(checkStatePart, SWT.SINGLE);
		stateText.setText("State Coverage");
		Button stateYesButton = new Button(checkStatePart, SWT.RADIO);
		stateYesButton.setText("Yes");
		Button stateNoButton = new Button(checkStatePart, SWT.RADIO);
		stateNoButton.setText("No");

		Label eventText = new Label(checkEventPart, SWT.SINGLE);
		eventText.setText("Event Coverage");
		Button eventYesButton = new Button(checkEventPart, SWT.RADIO);
		eventYesButton.setText("Yes");
		Button eventNoButton = new Button(checkEventPart, SWT.RADIO);
		eventNoButton.setText("No");

		treePartCoveragePart.setLayout(new GridLayout(1, false));
		Button buttonAnalyze = new Button(analyzePart, SWT.PUSH);
		buttonAnalyze.setText("Analyze");

		// urlText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// buttonPush.setText("Load");
		// buttonPush.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true));
		// buttonPath.setText("Path");
		// buttonPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true));
	}

	private void initVFSMPart(Composite vfsmPart) {
		vfsmPart.setLayout(new GridLayout(1, true));
		vfsmPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite diagramPart = new Composite(vfsmPart, SWT.H_SCROLL
				| SWT.V_SCROLL);
		diagramPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite infoPart = new Composite(vfsmPart, SWT.NONE);
		infoPart.setLayout(new GridLayout());
		infoPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Text info = new Text(infoPart, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		info.setEditable(false);
		info.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		// TODO Auto-generated method stub
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		this.setSite(site);
		this.setInput(editorInput);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initializeGraphicalViewer() {
		// TODO Auto-generated method stub
		// getGraphicalViewer().addDropTargetListener(
		// new DiagramTemplateTransferDropTargetListener(
		// getGraphicalViewer()));
		//		
		// _sashForm = new SashForm(getGraphicalViewer().getControl()
		// .getParent().getParent(), SWT.HORIZONTAL);

	}

	private void prepareScriptDemoData(ProjectNode root) {
		NodeFactory nodeFactory = new NodeFactory();

		SourceFolderNode sourceFolderNode = new SourceFolderNode("src");
		root.addChild(sourceFolderNode);

		TestCaseNode class1 = new TestCaseNode("Class1");

		sourceFolderNode.addChild(class1);

		TestMethodNode method1 = new TestMethodNode("Method1");
		TestMethodNode method2 = new TestMethodNode("Method2");
		TestMethodNode method3 = new TestMethodNode("Method3");

		class1.addChild(method1);
		class1.addChild(method2);
		class1.addChild(method3);

		FolderNode folderNode;

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test1_1 = new TestScriptDocument(folderNode);
		method1.addDocument(test1_1);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test1_2 = new TestScriptDocument(folderNode);
		method1.addDocument(test1_2);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test1_3 = new TestScriptDocument(folderNode);
		method1.addDocument(test1_3);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test2_1 = new TestScriptDocument(folderNode);
		method2.addDocument(test2_1);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test2_2 = new TestScriptDocument(folderNode);
		method2.addDocument(test2_2);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test3_1 = new TestScriptDocument(folderNode);
		method3.addDocument(test3_1);

		folderNode = nodeFactory.createFolderNode("root");
		TestScriptDocument test3_2 = new TestScriptDocument(folderNode);
		method3.addDocument(test3_2);

		EventNode eventNode;

		eventNode = createEventNode("b1", "javax.swing.JButton", "PUSH");
		test1_1.insertNode(test1_1.getScript(), eventNode);

		eventNode = createEventNode("b5", "javax.swing.JButton", "PUSH");
		test1_2.insertNode(test1_2.getScript(), eventNode);

		eventNode = createEventNode("b9", "javax.swing.JButton", "PUSH");
		test1_2.insertNode(test1_2.getScript(), eventNode);

		eventNode = createEventNode("b12", "javax.swing.JButton", "PUSH");
		test1_3.insertNode(test1_3.getScript(), eventNode);

		eventNode = createEventNode("b2", "javax.swing.JButton", "PUSH");
		test2_1.insertNode(test2_1.getScript(), eventNode);

		eventNode = createEventNode("b6", "javax.swing.JButton", "PUSH");
		test2_1.insertNode(test2_1.getScript(), eventNode);

		eventNode = createEventNode("b13", "javax.swing.JButton", "PUSH");
		test2_1.insertNode(test2_1.getScript(), eventNode);

		eventNode = createEventNode("b11", "javax.swing.JButton", "PUSH");
		test2_2.insertNode(test2_2.getScript(), eventNode);

		eventNode = createEventNode("b1", "javax.swing.JButton", "PUSH");
		test3_1.insertNode(test3_1.getScript(), eventNode);

		eventNode = createEventNode("b4", "javax.swing.JButton", "PUSH");
		test3_1.insertNode(test3_1.getScript(), eventNode);

		eventNode = createEventNode("b13", "javax.swing.JButton", "PUSH");
		test3_2.insertNode(test3_2.getScript(), eventNode);

		eventNode = createEventNode("b11", "javax.swing.JButton", "PUSH");
		test3_2.insertNode(test3_2.getScript(), eventNode);

	}

	private void prepareFSMDemoData(IGraph graph) {
		Vertex v0, v1, v2, v3, v4, v5, v6, v7, v8;
		State s0, s1, s2, s3, s4, s5, s6, s7, s8;

		s0 = new State("Start");
		v0 = _graph.addVertex(s0);

		s1 = new State("State1");
		v1 = _graph.addVertex(s1);

		s2 = new State("State2");
		v2 = _graph.addVertex(s2);

		s3 = new State("State3");
		v3 = _graph.addVertex(s3);

		s4 = new State("State4");
		v4 = _graph.addVertex(s4);

		s5 = new State("State5");
		v5 = _graph.addVertex(s5);

		s6 = new State("State6");
		v6 = _graph.addVertex(s6);

		s7 = new State("State 7");
		v7 = _graph.addVertex(s7);

		s8 = new State("End");
		v8 = _graph.addVertex(s8);

		_graph.setStart(v0);

		_graph.addEdge(v0, v1, createEventNode("b1", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v0, v2, createEventNode("b2", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v1, v6, createEventNode("b3", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v1, v3, createEventNode("b4", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v1, v4, createEventNode("b5", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v2, v3, createEventNode("b6", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v2, v5, createEventNode("b7", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v6, v5, createEventNode("b8", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v4, v7, createEventNode("b9", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v5, v7, createEventNode("b10", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v6, v8, createEventNode("b11", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v7, v8, createEventNode("b12", "javax.swing.JButton",
				"PUSH"));
		_graph.addEdge(v3, v6, createEventNode("b13", "javax.swing.JButton",
				"PUSH"));

	}

	private EventNode createEventNode(String componentName,
			String componentType, String eventName) {
		EventNode eventNode;
		IComponent ic;
		IEvent ie;
		NodeFactory nodeFactory = new NodeFactory();
		ic = new SwingModel().createComponent(componentType);
		ic.setName(componentName);
		ic.setTitle("TEST");
		ie = EventModelFactory.getDefault().getEvent(ic, eventName);
		eventNode = nodeFactory.createEventNode(ic, ie);

		return eventNode;
	}

}
