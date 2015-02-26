package gttlipse.macro.action;

import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ETSBuilder;
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
import gttlipse.testgen.testsuite.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;


public class CreateMETS extends MacroViewAction {

	private LaunchNode m_autInfoNode = null;
	private ETSBuilder m_build = null;

	public CreateMETS() {
		super();
	}

	public void run() {
		m_build = new ETSBuilder(getPresenter().getDocument().getMacroScript());
		m_build.build();
		_start();
	}

	private void _start() {
		TestGenerationDialog dialog = new TestGenerationDialog(null);
		dialog.open();
		if (dialog.getReturnCode() != SWT.OK)
			return;
		m_autInfoNode = dialog.getLaunchNode();
		TestSuite suite = getTestSuite(dialog.getSelectionNum(), dialog
				.getStepNum());
		buildScriptByTestCase(suite);
	}

	private TestSuite getTestSuite(int selection, int steps) {
		TestCaseGenerator generator = new TestCaseGenerator(m_build.getGraph());
		switch (selection) {
		case 0:
			return generator.forStateCoverage();
		case 1:
			return generator.forTransitionCoverage();
		case 2:
			return generator.forConcesutiveCoverage();
		case 3:
			return generator.monkeyTest(steps);
		}
		return null;
	}

	private TestMethodNode initScriptBase(TestFileManager fileManager) {

		IProject prj = EclipseProject.getEclipseProject();
		ResourceManager manager = new ResourceManager();

		// String folderName = projectNode.getNewSourceFolderName();
		String folderName = "TestCaseFolder";
		ProjectNode projectNode = TestProject.getProject();
		SourceFolderNode sourceNode = null;
		sourceNode = (SourceFolderNode) projectNode
				.getChildrenByName(folderName);
		if (sourceNode == null) {
			sourceNode = new SourceFolderNode();
			sourceNode.setName(folderName);
			projectNode.addChild(sourceNode);
			manager.addJavaSourceFolder(prj, folderName);
		}

		String className = sourceNode.nextNewTestCaseName();
		manager.AddJavaClass(sourceNode, className);
		TestCaseNode classNode = (TestCaseNode) sourceNode
				.getChildrenByName(className);

		TestMethodNode methodNode = new TestMethodNode();
		String methodName = classNode.nextNewTestMethodName();
		methodNode.setName(methodName);
		classNode.addChild(methodNode);
		fileManager.addTestMethod(classNode, methodName);
		return methodNode;
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
			// try {
			// if(i%10 == 0)
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// m_progressDlg.setProgressTick( ( i + 1 ) * 100 /
			// caseContainer.castListSize() );
		}
	}

	private void buildNewTestSriptDoc(TestScriptDocument doc,
			Object[] eventNodeList) {
		NodeFactory factory = new NodeFactory();
		// if(m_caseSelection!=3)
		// {
		LaunchNode launch = new NodeFactory().createLaunchNode("Launch", ".");
		launch.setClassName(m_autInfoNode.getClassName());
		doc.getScript().add(launch);
		// }
		for (int i = 0; i < eventNodeList.length; i++) {
			if (eventNodeList[i] instanceof EventNode) {
				// insert event
				EventNode event = (EventNode) eventNodeList[i];
				doc.getScript().add(event, doc.getScript().size());
				continue;
			}

			if (eventNodeList[i] instanceof MacroEventNode) {
				// insert macro event
				MacroEventNode me = (MacroEventNode) eventNodeList[i];
				doc.getScript().add(
						factory.createReferenceMacroEventNode(me.getPath()
								.toString()), doc.getScript().size());
				continue;
			}
		}
	}

	/*
	 * private void buildScriptByTestCase( TestCaseContainer caseContainer ) {
	 * ProjectNode projectNode = InvisibleRootNode.getTestProject();
	 * SourceFolderNode sourceNode = null; MethodNode methodNode = new
	 * MethodNode(); TestScriptDocument scriptDoc = null;
	 * 
	 * IResourceFinder finder = new IResourceFinder(); IProject prj =
	 * finder.findIProject( projectNode ); IResourceManager manager = new
	 * IResourceManager();
	 * 
	 * //String folderName = projectNode.getNewSourceFolderName(); String
	 * folderName = "TestCaseFolder"; sourceNode =
	 * (SourceFolderNode)projectNode.getChildrenByName( folderName ); if(
	 * sourceNode == null ) { sourceNode = new SourceFolderNode();
	 * sourceNode.setName( folderName ); projectNode.addChild( sourceNode );
	 * manager.addJavaSourceFolder( prj, folderName ); }
	 * 
	 * String className = sourceNode.getNewClassNodeName();
	 * manager.AddJavaClass( sourceNode, className ); ClassNode classNode =
	 * (ClassNode)sourceNode.getChildrenByName( className );
	 * 
	 * TestFileManager fileManager = new TestFileManager(); String methodName =
	 * classNode.getNewMethodNodeName(); methodNode.setName( methodName );
	 * classNode.addChild( methodNode ); fileManager.addTestMethod( classNode,
	 * methodName );
	 * 
	 * for( int i = 0; i < caseContainer.castListSize(); i++ ) { int index = i +
	 * 1; scriptDoc = methodNode.addNewNode(); scriptDoc.setName(
	 * "Interaction Sequence" + index ); buildNewTestSriptDoc( scriptDoc,
	 * caseContainer.getTestCase( i ) ); methodNode.addDoc( scriptDoc );
	 * fileManager.addTestScriptDocument( scriptDoc ); try { Thread.sleep(200);
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * private void buildNewTestSriptDoc( TestScriptDocument doc, Object[]
	 * eventNodeList ) { NodeFactory factory = new NodeFactory(); AUTInfoNode
	 * autNode = (AUTInfoNode)factory.createAUTInfoNode( "AUTInfo", "" );
	 * autNode.setAUTFilePath( m_autInfoNode.getAUTFilePath() );
	 * doc.getScript().add( autNode );
	 * 
	 * for( int i = 0; i < eventNodeList.length; i++ ) { if( eventNodeList[i]
	 * instanceof EventNode ) { EventNode event = (EventNode)eventNodeList[i];
	 * doc.getScript().add( event ); continue; }
	 * 
	 * if( eventNodeList[i] instanceof MacroEventConditionNode ) {
	 * MacroEventConditionNode me = (MacroEventConditionNode)eventNodeList[i];
	 * doc.getScript().add( factory.createReferenceMacroEventNode(me.getPath())
	 * ); continue; } } }
	 */
}
