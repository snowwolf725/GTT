package gttlipse.macro.view;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.macro.MacroDocument;
import gtt.macro.MacroUtil;
import gtt.macro.io.MacroXmlReader;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ETSBuilder;
import gtt.macro.visitor.BadSmellStatisticsVisitor;
import gtt.macro.visitor.DuplicateScriptSmellVisitor;
import gtt.macro.visitor.FeatureEnvySmellVisitor;
import gtt.macro.visitor.HierarchySmellVisitor;
import gtt.macro.visitor.LackEncapsulationSmellVisitor;
import gtt.macro.visitor.LongScriptSmellVisitor;
import gtt.macro.visitor.MiddleManSmellVisitor;
import gtt.macro.visitor.ShotgunSurgeryUsageSmellVisitor;
import gtt.macro.visitor.StatisticNodesVistior;
import gtt.macro.visitor.StatisticSearchingCost;
import gtt.testscript.TestScriptDocument;
import gtt.util.DataPool;
import gttlipse.EclipseProject;
import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;
import gttlipse.TestProject;
import gttlipse.fit.node.FitNode;
import gttlipse.macro.dialog.SetSimulationNumberDialog;
import gttlipse.macro.dialog.WebPageToComponentDialog;
import gttlipse.preferences.PreferenceConstants;
import gttlipse.refactoring.util.AnalysisBadSmellTreeItem;
import gttlipse.refactoring.util.DetectOuterUsage;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.actions.ILauncher;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.dialog.GetAUTHierarchyDialog;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.testScript.io.LoadScript;
import gttlipse.scriptEditor.views.GTTTestScriptView;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.view.MacroTabularPresenter;
import gttlipse.view.ChangeTreeItemBackground;
import gttlipse.view.FocusOnPoint;
import gttlipse.web.htmlPaser.ConvertPageToComponent;
import gttlipse.web.loadtesting.PlayThread;
import gttlipse.web.loadtesting.view.LoadTestingResultView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TreeItem;

public class MacroPresenter {
	public static String ERROR_MACRO_EVENT = "Error Macro Event Node";
	public static String ERROR_EVENT_INDEX = "Error Event Index";
	
	private NodeManager m_nodemanager;
	private RefactoringHandler m_rhandler;
	private TreeViewer m_treeViewer;
	private MacroDocument m_Document;
	private ViewContentProvider m_viewContent;
	private AbstractMacroNode m_copy = null;
	private int m_simNumber = 1;
	private DataPool _pool = null;
	private LoadTestingResultView m_resultView = null;

	public MacroPresenter(TreeViewer viewer) {
		m_nodemanager = new NodeManager(viewer);
		m_rhandler = new RefactoringHandler(viewer);
		m_treeViewer = viewer;
		// singleton (或許需要移掉 -zwshen 2009/01/14)
		m_Document = TestProject.getMacroDocument();
		
		// Initialize a reference and some data
		_pool = DataPool.getDataPool();
		m_resultView = new LoadTestingResultView();
		resetData();
	}

	public void setViewContentProvider(ViewContentProvider content) {
		m_viewContent = content;
	}
	
	public void setSimNumber(int number) {
		m_simNumber = number;
	}	

	public TreeViewer getTreeViewer() {
		return m_treeViewer;
	}

	public MacroDocument getDocument() {
		return m_Document;
	}

	public void initMacroDocument() {
		m_Document.getMacroScript().removeAll();
		m_Document.getMacroScript().setName("AUT");
	}

	public AbstractMacroNode getSelectedNode() {
		IStructuredSelection selection = (IStructuredSelection) m_treeViewer
				.getSelection();
		if (selection.size() == 0)
			return null;
		return (AbstractMacroNode) selection.getFirstElement();
	}

	public List<AbstractMacroNode> getSelectedNodes() {
		IStructuredSelection selection = (IStructuredSelection) m_treeViewer
				.getSelection();

		List<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			AbstractMacroNode node = (AbstractMacroNode) iter.next();
			nodes.add(node);
		}

		return nodes;
	}

	public void deleteMacroNode() {
		m_nodemanager.deleteMacroNode();
	}

	public void editSelectedNode() {
		m_nodemanager.editSelectedNode();
	}

	public void insertComponentNode(String type) {
		m_nodemanager.insertComponentNode(type);
	}

	public void insertComponentEventNode() {
		m_nodemanager.insertComponentEventNode();
	}

	private void refreshandExpendTreeViewer(AbstractMacroNode node) {
		m_treeViewer.refresh();
		m_treeViewer.setExpandedState(node, true);
	}

	public void insertMacroEventCallNode() {
		m_nodemanager.insertMacroEventCallNode();
	}

	public void insertMacroComponentNode() {
		m_nodemanager.insertMacroComponentNode();
	}

	public void insertMacroEventNode() {
		m_nodemanager.insertMacroEventNode();
	}

	public void insertModelAssertNode() {
		m_nodemanager.insertModelAssertNode();
	}

	public void insertViewAssertNode() {
		m_nodemanager.insertViewAssertNode();
	}

	public void insertExistenceAssertNode() {
		m_nodemanager.insertExistenceAssertNode();
	}

	public void insertLaunchNode() {
		m_nodemanager.insertLaunchNode();
	}
	
	public void insertIncludeNode() {
		m_nodemanager.insertIncludeNode();
	}

	// 新增加入Event Trigger node相關動作 by Pan
	public void insertEventTriggerNode() {
		m_nodemanager.insertEventTriggerNode();
	}

	// 新增加入FitAssertion node相關動作 by Pan
	public void insertFitAssertionNode() {
		m_nodemanager.insertFitAssertionNode();
	}

	// 新增加入FitTableAssertion node相關動作 by Pan
	public void insertFitStateAssertionNode() {
		m_nodemanager.insertFitStateAssertionNode();
	}

	// 新增加入Fit node 相關動作 by Pan
	public void insertFitNode() {
		m_nodemanager.insertFitNode();
	}

	// 新增加入 Split Data As Name node 相關動作 by Pan
	public void insertSplitDataAsNameNode() {
		m_nodemanager.insertSplitDataAsNameNode();
	}

	// 新增加入 Generate Order Name node 相關動作 by Pan
	public void insertGenerateOrderNameNode() {
		m_nodemanager.insertGenerateOrderNameNode();
	}

	// 新增加入 Fix Name node 相關動作 by Pan
	public void insertFixNameNode() {
		m_nodemanager.insertFixNameNode();
	}

	// uhsing 2011.03.16
	public void insertSplitDataNode() {
		m_nodemanager.insertSplitDataNode();
	}
	
	// uhsing 2011.03.22
	public void insertDynamicComponentNode() {
		m_nodemanager.insertDynamicComponentNode();
	}
	
	// uhsing 2011.03.28
	public void insertDynamicComponentEventNode() {
		m_nodemanager.insertDynamicComponentEventNode();
	}
	
	public void insertMacroEventToTestScript() {
		List<AbstractMacroNode> nodes = getSelectedNodes();
		
		for (int i = 0; i < nodes.size(); i++) {
			AbstractMacroNode select = (AbstractMacroNode) nodes.get(i);

			if (!(select instanceof MacroEventNode)
					&& !(select instanceof FitNode))
				continue;

			insertScript(select);
		}
	}

	// uhsing 2011.03.25 - Reduce the coupling with UI
	public static void insertScript(AbstractMacroNode select) {
		String path = select.getPath().toString();

		if (select instanceof MacroEventNode) {
			GTTlipse.showScriptView().addReferenceMacroEventNode(path);
		}
		else if (select instanceof FitNode) {
			GTTlipse.showScriptView().addReferenceFitNode(path);
		}
		else {
			// Unknown type, ignore it
			return;
		}
	}
	
	public static String getNodePath(AbstractMacroNode node) {
		// 因為GTT在Eclipse下會多一個invisible node，因此需要做path上的調整，也就是把起頭跟最後的"::"拿掉
		String path = node.getPath().toString();
		if (path.startsWith("::"))
			path = path.substring(2);
		if (path.endsWith("::"))
			path = path.substring(0, path.length() - 3);
		
		return path;
	}

	public void doCopy() {
		AbstractMacroNode select = getSelectedNode();
		if (select == null)
			return;
		m_copy = select.clone();
	}

	public void doCut() {
		AbstractMacroNode select = getSelectedNode();
		if (select == null)
			return;
		m_copy = select.clone();

		AbstractMacroNode parent = select.getParent();
		MacroUtil.removeNode(parent, select);

		refreshandExpendTreeViewer(parent);
	}

	public void doPaste() {
		if (m_copy == null)
			return;

		AbstractMacroNode select = getSelectedNode();
		if (select == null)
			return;

		if (select.add(m_copy) == true)
			refreshandExpendTreeViewer(select);
	}

	public void copyTestScriptToMacro() {
		AbstractMacroNode select = getSelectedNode();

		if (!(select instanceof MacroComponentNode))
			return;

		GTTTestScriptView view = GTTlipse.showScriptView();
		IStructuredSelection selection = (IStructuredSelection) view
				.getTreeViewer().getSelection();
		Iterator<?> iter = selection.iterator();

		MacroEventNode me = MacroEventNode.create("NewMacroEvent");
		select.add(me);

		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof gtt.testscript.EventNode) {
				gtt.testscript.EventNode node = (gtt.testscript.EventNode) obj;
				IComponent component = node.getComponent();
				IEvent event = node.getEvent();

				ComponentNode com = new ComponentNode(component);
				com.setName(component.getName());

				ComponentEventNode cen = new ComponentEventNode(com);

				cen.setEvent(event.getName(), event.getEventId());
				cen.setArguments(event.getArguments().clone());

				MacroUtil.insertNode(select, com);
				MacroUtil.insertNode(me, cen);
			}
		}

		m_treeViewer.refresh();
	}

	public void upMove() {
		MacroUtil.upNode(getSelectedNode());
		m_treeViewer.refresh();
	}

	public void downMove() {
		MacroUtil.downNode(getSelectedNode());
		m_treeViewer.refresh();
	}

	public void createMETS() {
		ETSBuilder builder = new ETSBuilder(m_Document.getMacroScript());
		builder.build();
	}

	private String fileexten[] = { "*.gtt", "*.xml" };

	public void openFile() {
		FileDialog fdialog = new FileDialog(m_treeViewer.getControl()
				.getShell(), SWT.OPEN);
		fdialog.setFilterExtensions(fileexten);
		fdialog.open();

		if (fdialog.getFileName() == "")
			return;

		String filename = fdialog.getFilterPath();
		filename += "\\" + fdialog.getFileName();

		m_Document.openFile(filename);

		refreshContentProvider();
	}

	public void openFile(String filename) {
		MacroXmlReader loader = getMacroScriptLoaderFromPlugin();
		if(loader != null)
			m_Document.openFile(loader, filename, null);
		else
			m_Document.openFile(filename);
		refreshContentProvider();
	}
	
	private static MacroXmlReader getMacroScriptLoaderFromPlugin() {
		MacroXmlReader loader = null;
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_MACRO_SCRIPT_LOADER_ID);
		if(GTTlipse.getPlatformInfo() == null)
			return null;
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof MacroXmlReader) {
					if(((LoadScript) o).specificTestPlatformID() == GTTlipse.getPlatformInfo().getTestPlatformID()) {
						loader = ((MacroXmlReader) o);
					}
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return loader;
	}

	public void saveFile() {
		FileDialog fdialog = new FileDialog(m_treeViewer.getControl()
				.getShell(), SWT.SAVE);
		fdialog.setFilterExtensions(fileexten);
		fdialog.open();

		String filepath = fdialog.getFilterPath();
		filepath += "\\" + fdialog.getFileName();

		m_Document.saveFile(filepath);
	}

	public void newFile() {
		m_Document = MacroDocument.create();
		refreshContentProvider();
	}

	public void refresh() {
		m_Document.openFile(EclipseProject.getProjectFile());
		refreshContentProvider();
	}

	private void refreshContentProvider() {
		m_viewContent.initialize(m_Document.getMacroScript());
		m_treeViewer.refresh();
	}

	public void saveMFSM() {
		IProject project = EclipseProject.getEclipseProject();
		if (project == null)
			return;

		String MFSMfile = project.getLocation().toString() + "/"
				+ TestScriptTage.MFSMFILE;
		// String MFSMGraphFile = project.getLocation().toString() + "/"
		// + TestScriptTage.MFSM_GRAPH_FILE;

		ETSBuilder build = new ETSBuilder(m_Document.getMacroScript());
		build.build();
		build.saveGraph(MFSMfile);

		// VisualFSMGeneration visual = new VisualFSMGeneration();
		// visual.visualizeMFSM(MFSMfile, MFSMGraphFile);
	}

	public void rename() {
		m_nodemanager.rename();
	}

	public void addMacroEventParameter() {
		m_rhandler.addMacroEventParameter();
	}

	public void removeMacroEventParameter() {
		m_rhandler.removeMacroEventParameter();
	}

	public void renameMacroEventParameter() {
		m_rhandler.renameMacroEventParameter();
	}

	public void extractMacroEvent() {
		m_rhandler.extractMacroEvent();
	}

	public void inlineMacroEvent() {
		m_rhandler.inlineMacroEvent();
	}

	public void moveMacroEvent() {
		m_rhandler.moveMacroEvent();
	}

	public void extractMacroComponent() {
		m_rhandler.extractMacroComponent();
	}

	public void renameWindowTitle() {
		m_rhandler.renameWindowTitle();
	}

	public void moveMacroComponent() {
		m_rhandler.moveMacroComponent();
	}
	
	public void locateBadSmell() {
		TreeItem item = GTTlipse.findMacroView().getViewer().getTree().getItem(0);
		AnalysisBadSmellTreeItem analysiser = new AnalysisBadSmellTreeItem();
		List<TreeItem> items = null;
		ChangeTreeItemBackground.clearBackground(item);
		
		IStructuredSelection selection = (IStructuredSelection) GTTlipse.findBadSmellListView().getTreeViewer().getSelection();
		if (selection.size() == 0)
			return;
		
		if(selection.getFirstElement() instanceof BadSmellItem) {
			BadSmellItem sitem = (BadSmellItem) selection.getFirstElement();
			for(SimpleEntry<AbstractMacroNode, String> smell:sitem.getSmells()){
				AbstractMacroNode node = smell.getKey();
				AbstractMacroNode realNode = getDocument().findByPath(smell.getValue());
				if(realNode != null) {
					realNode.getBadSmellData().setBadSmellScore(node.getBadSmellData().getBadSmellScore());
					realNode.getBadSmellData().setTotalBadSmellScore(node.getBadSmellData().getTotalBadSmellScore());
					realNode.getBadSmellData().setRGB(node.getBadSmellData().getColor());
				}
			}
		}
		
		BadSmellStatisticsVisitor visitor = new BadSmellStatisticsVisitor();
		GTTlipse.findMacroView().getPresenter().getDocument().getMacroScript().accept(visitor);
		GTTlipse.findMacroView().getViewer().expandAll();
		analysiser.setAnalysis(true);
		GTTlipse.findMacroView().getViewer().refresh();
		item = GTTlipse.findMacroView().getViewer().getTree().getItem(0);
		items = analysiser.analysis(item);
		if (items.size() > 0) {
			ChangeTreeItemBackground.changeBackground(items);
		}
		
		GTTlipse.findMacroView().getViewer().refresh();
	}
	
	public void clearBadSmell() {
		GTTlipse.findBadSmellListView().clearBadSmell();
	}
	
	public void clearLoadTestingResult() {
		GTTlipse.findLoadTestingResultView().clearResults();
	}

	public void moveComponent() {
		m_rhandler.moveComponent();
	}

	public void removeMiddleMan() {
		m_rhandler.removeMiddleMan();
	}

	public void replayMacroScript() {
		if (GTTlipse.showScriptView().getMacroScriptEntryPoint() == false) {
			return;
		}
		
		insertMacroEventToTestScript();			
		replay();		
	}
	
	//2011-04-19 add for web loading test by loveshoo
	public void loadingTest() {
		if (GTTlipse.showScriptView().getMacroScriptEntryPoint() == false) {
			return;
		}
		insertMacroEventToTestScript();				
		
		IStructuredSelection selection = (IStructuredSelection)GTTlipse.showScriptView().getTreeViewer().getSelection();
		if (selection == null) {
			return;
		}
		
		TestScriptDocument doc = (TestScriptDocument)selection.getFirstElement();
		if (doc == null) {
			return;
		}
		
		TestMethodNode methodNode = (TestMethodNode)doc.getParent();
		if (methodNode == null) {
			return;
		}
		
		SetSimulationNumberDialog dialog = new SetSimulationNumberDialog(m_treeViewer.getControl().getShell(), this);
		dialog.open();		
		
		if (dialog.getReturnCode() == 1) {
			return;
		}
		else{	
			m_resultView = GTTlipse.findLoadTestingResultView();
			m_resultView.clearResults();
			for(int i = 0; i < m_simNumber; i++) {
				PlayThread pThread = new PlayThread(methodNode, m_simNumber);
				pThread.start();
			}
		}		
	}	

	// uhsing 2011.03.24 - Reduce the coupling with UI 
	public static void replay() {
		IStructuredSelection selection = (IStructuredSelection)GTTlipse.showScriptView().getTreeViewer().getSelection();
		if (selection == null) {
			return;
		}
		
		TestScriptDocument doc = (TestScriptDocument)selection.getFirstElement();
		if (doc == null) {
			return;
		}
		
		TestMethodNode methodNode = (TestMethodNode)doc.getParent();
		if (methodNode == null) {
			return;
		}
		
		JUnitLaunchShortcut sk = getLauncher();
		ResourceFinder finder = new ResourceFinder();

		IFile file = finder.findIFile((TestCaseNode)methodNode.getParent());
		ICompilationUnit classFile = (ICompilationUnit)JavaCore.create(file);
		
		try {
			for (IType type : classFile.getTypes()) {
				IMethod method = type.getMethod(methodNode.getName(), new String[] {});
				
				if (method != null) {
					sk.launch(new StructuredSelection(method), "run");
				}
			}
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
	
	private static JUnitLaunchShortcut getLauncher() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_SCRIPT_ACTION_LAUNCHER_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				ILauncher launcher = (ILauncher)o;
				if (launcher instanceof ILauncher &&
					GTTlipse.getPlatformInfo().getTestPlatformID() == launcher.getPlatformID()) {
					return launcher.getLauncher();
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return new JUnitLaunchShortcut();
	}
	
	public void detectOuterUsage() {
		// Get provider and set view outer usage
		ViewLabelProvider provider = (ViewLabelProvider) m_treeViewer
				.getLabelProvider();
		TreeItem item = m_treeViewer.getTree().getItem(0);
		AnalysisBadSmellTreeItem analysiser = new AnalysisBadSmellTreeItem();
		// when view isn't Statistic view, then set to become Statistic view
		// view
		if (provider.isStatisticView() == false) {
			// detect outer usage
			DetectOuterUsage detecter = new DetectOuterUsage(m_Document
					.getMacroScript());
			detecter.detect();

			// change background
			m_treeViewer.expandAll();
			analysiser.setAnalysis(true);
			List<TreeItem> items = analysiser.analysis(item);
			if (items.size() > 0) {
				// change to red
				Device device = items.get(0).getBackground().getDevice();
				ChangeTreeItemBackground.changeBackground(items, new Color(
						device, 236, 66, 108));
			}
			provider.setStatisticView(true);
		} else {
			// change background
			analysiser.setAnalysis(false);
			List<TreeItem> items = analysiser.analysis(item);
			if (items.size() > 0) {
				// change to white
				Device device = items.get(0).getBackground().getDevice();
				ChangeTreeItemBackground.changeBackground(items, new Color(
						device, 255, 255, 255));
			}
			provider.setStatisticView(false);
		}

		m_treeViewer.refresh();
	}
	
	public void detectLongMacroEvent() {
		LongScriptSmellVisitor visitor = new LongScriptSmellVisitor();
		visitor.setType(LongScriptSmellVisitor.TYPE_MACROEVENT);
		visitor.setSizeLimit(GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_LONGMACROEVENT));
		getSelectedNode().accept(visitor);
	}
	
	public void detectLongMacroComp() {
		LongScriptSmellVisitor visitor = new LongScriptSmellVisitor();
		visitor.setType(LongScriptSmellVisitor.TYPE_MACROCOMP);
		visitor.setSizeLimit(GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_LONGMACROCOM));
		getSelectedNode().accept(visitor);
	}
	
	public void detectLongArg() {
		LongScriptSmellVisitor visitor = new LongScriptSmellVisitor();
		visitor.setType(LongScriptSmellVisitor.TYPE_ARG);
		visitor.setSizeLimit(GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_LONGPARLIST));
		getSelectedNode().accept(visitor);
	}
	
	public void detectDuplicateEvent() {
		GTTlipse.showBadSmellListView();
		DuplicateScriptSmellVisitor visitor = new DuplicateScriptSmellVisitor();
		getSelectedNode().accept(visitor);
	}
	
	public void detectShotgunSurgeryUsage() {
		ShotgunSurgeryUsageSmellVisitor visitor = new ShotgunSurgeryUsageSmellVisitor();
		visitor.setSizeLimit(GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_SHOTGUNSURGERY));
		getSelectedNode().accept(visitor);
		visitor.analysis();
	}
	
	public void detectLackEncapsulation() {
		LackEncapsulationSmellVisitor visitor = new LackEncapsulationSmellVisitor();
		getSelectedNode().accept(visitor);
	}
	
	public void detectMiddleMan() {
		MiddleManSmellVisitor visitor = new MiddleManSmellVisitor();
		getSelectedNode().accept(visitor);
	}
	
	public void detectHierarchy() {
		GTTlipse.showBadSmellListView();
		// Setup AUT
		GetAUTHierarchyDialog dialog = new GetAUTHierarchyDialog(m_treeViewer.getControl().getShell());
		dialog.open();
		
		if(dialog.getReturnCode() == SWT.CANCEL) {
			return;
		} else if (dialog.getReturnCode() == SWT.OK && dialog.getMacroWindow() == null) {
			MessageBox box = new MessageBox(m_treeViewer.getControl().getShell(),	SWT.OK);
			box.setText("Warring");
			box.setMessage("AUT not found");
			box.open();
			return;
		}
		MacroComponentNode macroWin = dialog.getMacroWindow();
		
		// Detect
		HierarchySmellVisitor visitor = new HierarchySmellVisitor();
		visitor.setActualScript(macroWin);
		visitor.setAllJavaComp(dialog.getAllJavaComp());
		getSelectedNode().accept(visitor);
	}
	
	public void detectFeatureEnvy(){
		FeatureEnvySmellVisitor visitor = new FeatureEnvySmellVisitor();
		getSelectedNode().accept(visitor);
	}
	
	public void detectAllBadsmell(){
		detectLongMacroEvent();
		detectLongArg();
		detectLongMacroComp();
		detectDuplicateEvent();
		detectShotgunSurgeryUsage();
		detectLackEncapsulation();
		detectMiddleMan();
		detectHierarchy();
		detectFeatureEnvy();
	}
	
	public void generateMacroComponent() {
		List<AbstractMacroNode> selects = getSelectedNodes();
		if (selects.size() == 0)
			return;

		if (selects.size() != 1) {
			System.out.println("Select more then 1 node!!");
		} else if (selects.get(0) instanceof MacroComponentNode) {
			if(GTTlipseConfig.testingOnWebPlatform()) {
				WebPageToComponentDialog dialog = new WebPageToComponentDialog(
						m_treeViewer.getControl().getShell());
				int result = dialog.open();
				// 將得到的原始碼轉成DOM
				if (result == 0) {
					ConvertPageToComponent ct = new ConvertPageToComponent(dialog
							.getSourcePage());
					ct.Convert(selects.get(0));
				}
			} else {
				// Setup AUT
				ViewLabelProvider provider = (ViewLabelProvider) m_treeViewer
				.getLabelProvider();
				GetAUTHierarchyDialog dialog = new GetAUTHierarchyDialog(m_treeViewer.getControl().getShell());
				if (provider.isStatisticView() == false) {
					dialog.open();
					
					if(dialog.getReturnCode() == SWT.CANCEL) {
						return;
					} else if (dialog.getReturnCode() == SWT.OK && dialog.getMacroWindow() == null) {
						MessageBox box = new MessageBox(m_treeViewer.getControl().getShell(),	SWT.OK);
						box.setText("Warring");
						box.setMessage("AUT not found");
						box.open();
						return;
					}
				}
				selects.get(0).add(dialog.getMacroWindow());
			}
		}
		refreshContentProvider();
		// }
	}

	public void inlineMacroComponent() {
		m_rhandler.inlineMacroComponent();
	}

	// uhsing 2011/08/09 - Reset some data in the data pool
	private void resetData() {
		_pool.setData(ERROR_MACRO_EVENT, null);
		_pool.setData(ERROR_EVENT_INDEX, TableModel.NON_ERROR);
	}
	
	// uhsing 2011/08/09 - When the test script has been replayed,
	// update the tabular editor
	private void updateTabular(MacroTabularPresenter presenter, boolean isReset) {
		Object node = _pool.getData(ERROR_MACRO_EVENT);
		int index = Integer.parseInt(_pool.getData(ERROR_EVENT_INDEX).toString());
		
		if (node != null) {
			// Reset the data
			if (isReset) {
				index = TableModel.NON_ERROR;
				resetData();
			}
			
			presenter.initialize(node, index);
		}
	}
	
	public void doAfterTerminate(Set<Object> m_failnodes, MacroTabularPresenter presenter) {
		IProject project = EclipseProject.getEclipseProject();
		if (project == null)
			return;	

		m_failnodes.clear();// 清掉舊的記錄
		try {
			if (project.isSynchronized(IResource.DEPTH_ONE) == false) {
				project.refreshLocal(IResource.DEPTH_ONE, null);
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}		
		
		IFile file = project.getFile("MacroGUITestResult.txt");

		if (file.exists() == false) {
			/* 更新 Viewer */
			m_treeViewer.refresh();
			updateTabular(presenter, true);
			return;
		}

		/* 處理並顯示測試結果 */
		try {
			InputStream in = file.getContents();
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			String str = "";
			int i = 0;
			AbstractMacroNode node = null;
			while ((str = input.readLine()) != null) {
				i++;
				if (i % 2 != 0) {// 第一行存放父節點路徑
					node = m_Document.findByPath(str);
					if (node != null) {
						m_failnodes.add(node);
						_pool.setData(ERROR_MACRO_EVENT, node);
					}
				} else {// 第二行存放驗證結果錯誤節點的index
					int index = Integer.valueOf(str);
					node = node.get(index);
					if (node != null) {
						m_failnodes.add(node);
						_pool.setData(ERROR_EVENT_INDEX, index);
					}
				}
			}
			// 刪除檔案並更新 local file system
			input.close();
			file.delete(false, null);
			project.refreshLocal(IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* 更新 Viewer */
		m_treeViewer.refresh();
		updateTabular(presenter, false);		
	}

	public void hideDelegate() {
		m_rhandler.hideDelegate();
	}

	public void extractMacroEventParameter() {
		m_rhandler.extractMacroEventParameter();
	}

	public void inlineMacroEventParameter() {
		m_rhandler.inlineMacroEventParameter();
	}

	public void focusOnPoint() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroEventCallerNode))
			return;

		AbstractMacroNode caller = ((MacroEventCallerNode) (selectNode))
				.getReference();
		FocusOnPoint action = new FocusOnPoint();
		action.focusOnPoint(m_treeViewer, caller);

		/* 更新 Viewer */
		m_treeViewer.refresh();
	}
	
	public void statisticNodes() {
		StatisticNodesVistior visitor = new StatisticNodesVistior();
		AbstractMacroNode selectNode = getSelectedNode();
		selectNode.accept(visitor);
		MessageDialog.openInformation(getTreeViewer().getTree().getShell(),
				"Node Statistic Result", visitor.getResult() );
	}
	
	public void statisticSearchingCost() {
		StatisticSearchingCost visitor = new StatisticSearchingCost();
		AbstractMacroNode selectNode = getSelectedNode();
		visitor.setRoot(selectNode);
		selectNode.accept(visitor);
		MessageDialog.openInformation(getTreeViewer().getTree().getShell(),
				"Average Searching Cost", visitor.getResult() );
	}
}
