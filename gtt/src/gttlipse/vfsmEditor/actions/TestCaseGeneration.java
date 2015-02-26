package gttlipse.vfsmEditor.actions;

import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.EclipseProject;
import gttlipse.TestProject;
import gttlipse.macro.dialog.TestGenerationDialog;
import gttlipse.resource.ResourceManager;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.testgen.algorithm.TestCaseGenerator;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.builder.GraphBuilder;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;


public class TestCaseGeneration extends Action {

	private IVFSMPresenter m_Presenter = null;
	private LaunchNode m_autInfoNode = null;
	private ProgressDialog m_progressDlg = null;
	private int m_caseSelection = -1;
	private int m_step = -1;

	public TestCaseGeneration(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {

		GraphBuilder gbuilder = new GraphBuilder();
		// ±N Main diagram Âà¦¨ IGraph
		IGraph graph = gbuilder.build(m_Presenter.getDiagram().getFSMMain()
				.getMainState());

		printFSMResult(graph);
		TestCaseGenerator generator = new TestCaseGenerator(graph);

		TestGenerationDialog dialog = new TestGenerationDialog(m_Presenter
				.getShell());
		dialog.open();

		if (dialog.getReturnCode() == SWT.OK) {
			try {
				gttlipse.GTTlipse.getDefault().getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().showView(
								"GTTlipse.views.GTTTestScriptView");
			} catch (PartInitException e) {
				e.printStackTrace();
			}

			m_autInfoNode = dialog.getLaunchNode();
			m_caseSelection = dialog.getSelectionNum();
			m_step = dialog.getStepNum();
			m_progressDlg = new ProgressDialog(m_Presenter.getShell());
			m_progressDlg.open();
			TestSuite caseContainer = algorithmSwitch(m_caseSelection,
					generator);
			buildScriptByTestCase(caseContainer);
			m_progressDlg.close();

		}
	}

	private TestSuite algorithmSwitch(int selection, TestCaseGenerator generator) {
		TestSuite caseContainer = null;

		switch (selection) {
		case 0:
			// caseContainer = generator.DFSForCompleteStateCoverage();
			caseContainer = generator.forStateCoverage();
			break;
		case 1:
			caseContainer = generator.forTransitionCoverage();
			break;
		case 2:
			caseContainer = generator.forConcesutiveCoverage();
			break;
		case 3:
			caseContainer = randomAlgorithmn(m_step, generator);
		}
		return caseContainer;
	}

	private TestSuite randomAlgorithmn(int step, TestCaseGenerator generator) {
		if (m_step != -1)
			return generator.monkeyTest(step);
		else
			return null;
	}

	private void buildScriptByTestCase(TestSuite caseContainer) {
		TestFileManager fileManager = new TestFileManager();
		TestMethodNode methodNode = initScriptBase(fileManager);
		TestScriptDocument scriptDoc = null;

		for (int i = 0; i < caseContainer.size(); i++) {
			int index = i + 1;
			scriptDoc = methodNode.addInteractionSequence();
			scriptDoc.setName("Interaction Sequence "
					+ String.format("%03d", index));
			buildNewTestSriptDoc(scriptDoc, caseContainer.getTestCase(i));
			if (i != caseContainer.size() - 1)
				fileManager.addScriptDocument(scriptDoc, false);
			else
				fileManager.addScriptDocument(scriptDoc, true);
			m_progressDlg.setProgressTick((i + 1) * 100 / caseContainer.size());
		}
	}

	private TestMethodNode initScriptBase(TestFileManager fileManager) {
		SourceFolderNode sourceNode = null;
		TestMethodNode methodNode = null;

		ResourceManager manager = new ResourceManager();

		String folderName = "TestCaseFolder";
		ProjectNode projectNode = TestProject.getProject();
		sourceNode = (SourceFolderNode) projectNode
				.getChildrenByName(folderName);

		if (sourceNode == null) {
			sourceNode = new SourceFolderNode();
			sourceNode.setName(folderName);
			projectNode.addChild(sourceNode);

			manager.addJavaSourceFolder(EclipseProject.getEclipseProject(),
					folderName);
		}

		String className = sourceNode.nextNewTestCaseName();
		manager.AddJavaClass(sourceNode, className);

		TestCaseNode classNode = (TestCaseNode) sourceNode
				.getChildrenByName(className);

		methodNode = new TestMethodNode();
		String methodName = classNode.nextNewTestMethodName();
		methodNode.setName(methodName);
		classNode.addChild(methodNode);
		fileManager.addTestMethod(classNode, methodName);

		projectNode = TestProject.getProject();
		sourceNode = (SourceFolderNode) projectNode
				.getChildrenByName("TestCaseFolder");
		classNode = (TestCaseNode) sourceNode.getChildrenByName(className);
		methodNode = (TestMethodNode) classNode.getChildrenByName(methodName);
		return methodNode;
	}

	private void buildNewTestSriptDoc(TestScriptDocument doc,
			Object[] eventNodeList) {
		LaunchNode autNode = (LaunchNode) m_autInfoNode.clone();
		doc.getScript().add(autNode);

		for (int i = 0; i < eventNodeList.length; i++) {
			if (eventNodeList[i] instanceof EventNode) {
				EventNode event = (EventNode) eventNodeList[i];
				doc.getScript().add(event, doc.getScript().size());
				continue;
			}

			if (eventNodeList[i] instanceof MacroEventNode) {
				MacroEventNode me = (MacroEventNode) eventNodeList[i];
				NodeFactory factory = new NodeFactory();

				doc.getScript().add(
						factory.createReferenceMacroEventNode(me.getPath()
								.toString()), doc.getScript().size());
				continue;
			}

			if (eventNodeList[i] instanceof AbstractNode) {
				AbstractNode me = (AbstractNode) eventNodeList[i];
				doc.getScript().add(me.clone());
			}
		}
	}

	// debug function
	private void printFSMResult(IGraph FSM) {
		List<Vertex> vertexList = FSM.vertices();

		for (int i = 0; i < vertexList.size(); i++) {
			Vertex v = vertexList.get(i);
			State graph = (State) v.getUserObject();
			String stateName = "";
			String input = "<INPUT>\n";
			String output = "<OUTPUT>\n";

			List<Edge> inEdgeList = v.getInEdgeList();
			List<Edge> outEdgeList = v.getOutEdgeList();
			stateName = getGraphName(graph);
			/* input connection */
			for (int j = 0; j < inEdgeList.size(); j++) {
				String inputStr = "";
				String componentName = "";
				String eventName = "";

				Edge edge = inEdgeList.get(j);
				if (edge.getUserObject() instanceof Connection) {
					Connection conn = (Connection) edge.getUserObject();
					if (conn != null) {
						componentName = "idontknow";
						eventName = conn.getEvent();
					}
				}
				Vertex orig = edge.origin();
				State origGraph = (State) orig.getUserObject();
				String origName = getGraphName(origGraph);

				Vertex dest = edge.destination();
				State destGraph = (State) dest.getUserObject();
				String destName = getGraphName(destGraph);

				inputStr = "    [" + j + "]" + componentName + "." + eventName
						+ "( " + origName + " -> " + destName + " )\n";

				input += inputStr;
			}
			/* output connection */
			for (int j = 0; j < outEdgeList.size(); j++) {
				String outputStr = "";
				String componentName = "";
				String eventName = "";

				Edge edge = outEdgeList.get(j);
				if (edge.getUserObject() instanceof Connection) {
					Connection conn = (Connection) edge.getUserObject();
					if (conn != null) {
						componentName = "idontknow";
						eventName = conn.getEvent();
					}
				}
				Vertex orig = edge.origin();
				State origGraph = (State) orig.getUserObject();
				String origName = getGraphName(origGraph);

				Vertex dest = edge.destination();
				State destGraph = (State) dest.getUserObject();
				String destName = getGraphName(destGraph);

				outputStr = "    [" + j + "]" + componentName + "." + eventName
						+ "( " + origName + " -> " + destName + " )\n";

				output += outputStr;
			}

			System.out.println("[" + stateName + "]");
			System.out.println("  " + input);
			System.out.println("  " + output);
		}
		System.out.println("--End--");
	}

	/* recursive parse node name, top down */
	private String getGraphName(State graph) {
		State tempGraph = graph;
		String name = "";

		while (tempGraph.getName().equals(VFSMDef.FSM_MAIN) == false) {
			name = tempGraph.getName() + " : " + name;
			tempGraph = tempGraph.getParent();
		}

		name = VFSMDef.FSM_MAIN + " : " + name;
		return name;
	}
}

class ProgressDialog extends TrayDialog {
	private ProgressBar m_progressBar = null;
	private Label m_label = null;

	// constructor
	public ProgressDialog(Shell parentShell) {
		super(parentShell);
		this.create();
		this.getShell().setText("Analyzing");
		this.setBlockOnOpen(false);
	}

	public void setProgressTick(int tick) {
		m_progressBar.setSelection(tick);
		m_progressBar.redraw();
		m_label.setText(tick + "% complete...");
		m_label.redraw();
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);

		ProgressBar progressBar = new ProgressBar(area, SWT.SMOOTH | SWT.FILL);
		progressBar.setSize(100, 20);

		Label textLabel = new Label(area, SWT.NULL);
		textLabel.setText("0% complete...");

		progressBar.setBounds(15, 20, 300, 20);
		textLabel.setBounds(15, 50, 315, 20);

		m_progressBar = progressBar;
		m_label = textLabel;
		return area;
	}

	// handle event of the button on the buttonbar
	protected void createButtonsForButtonBar(Composite parent) {
	}
}
