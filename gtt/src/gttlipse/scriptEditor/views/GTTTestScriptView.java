package gttlipse.scriptEditor.views;

import gtt.eventmodel.IComponent;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.TestProject;
import gttlipse.fit.Action.AddReferenceFitNodeAction;
import gttlipse.scriptEditor.actions.ActionFactory;
import gttlipse.scriptEditor.actions.ActionManager;
import gttlipse.scriptEditor.actions.IActionFactory;
import gttlipse.scriptEditor.actions.node.AddReferenceMacroEventNode;
import gttlipse.scriptEditor.def.ActionType;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.InvisibleRootNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This view is GTTTestScriptView.
 */
public class GTTTestScriptView extends ViewPart implements ISaveablePart2{
	private ActionManager actionmanager = null;

	private static ImageRegistry image_registry;

	private TreeViewer viewer;

	private URL baseurl;

	private SelectionChangHandler m_selectionHandler;
	
	private TestReportHandler m_testreporthandler;
	
	private TestFileChangeListener m_filechangelistener;
	
	private Set<Object> m_failnodes;
	
	private Queue<IComponent> m_comps;
	
	private boolean isDirty = false;
	
	// uhsing 2010/11/10
//	private ScriptTabularPresenter _presenter = null;
	
	// uhsing 2010/11/10
	private TestCaseNode _testCaseNode = null;
	
	/**
	 * The constructor.
	 */
	public GTTTestScriptView() {}
	
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        init(site);
    }
    
    public void dispose() {
    	GTTlipse.getDefault().getWorkbench().removeWindowListener(m_testreporthandler);
    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
    	workspace.removeResourceChangeListener(m_filechangelistener);
    	super.dispose();
    }
    
    public TestReportHandler getReportHandler() {
    	return m_testreporthandler;
    }
    
    public boolean getMacroScriptEntryPoint(){
    	SourceFolderNode src = (SourceFolderNode) TestProject.getProject().getChildrenByName("gtt");
    	if(src == null)	return false;
    	PackageNode pkg = (PackageNode)src.getChildrenByName("gttMacroScript");
    	if(pkg == null) return false;
    	TestCaseNode classnode = (TestCaseNode)pkg.getChildrenByName("MacroEntryPoint");
    	if(classnode == null) return false;
    	TestMethodNode method = (TestMethodNode)classnode.getChildrenByName("testMacro");
    	if(method == null) return false;
    	TestScriptDocument doc = method.getDocByName("Macro Script", 1);
    	if(doc == null)	return false;
    	AbstractNode root = doc.getScript();
    	while(root.size()!=0)
    		root.remove(0);
    	// using specific compare strategy
    	getTreeViewer().setComparer(new IElementComparer(){

			@Override
			public boolean equals(Object a, Object b) {
				if(a instanceof BaseNode && b instanceof BaseNode){
					return ((BaseNode) a).getName().equals(((BaseNode) b).getName());
				}else if(a instanceof TestScriptDocument && b instanceof TestScriptDocument){
					return ((TestScriptDocument) a).getName().equals(((TestScriptDocument) b).getName());
				}else{
					return a.equals(b);
				}
			}

			@Override
			public int hashCode(Object element) {
				return 0;
			}
    		
    	});
    	getTreeViewer().setSelection(new StructuredSelection(doc), true);
    	getTreeViewer().setComparer(null);
    	return true;
    }

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		m_failnodes = new HashSet<Object>();
		m_comps = new ConcurrentLinkedQueue<IComponent>();
		baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		initImageRegistry();
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ScriptViewContentProvider(getViewSite()));
		viewer.setLabelProvider(new ScriptViewLabelProvider(image_registry,m_failnodes));
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		// uhsing 2010/11/10
//		_presenter = new ScriptTabularPresenter();

		// uhsing 2010/11/10
		initMouseListener();
		
		/* TestReportHandler */
		m_testreporthandler = new TestReportHandler(viewer,m_failnodes);
//        GTTlipse.getDefault().getWorkbench().addWindowListener(m_testreporthandler);
        /* Setup Resource Listener */
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		m_filechangelistener = new TestFileChangeListener(viewer);
		m_filechangelistener.setDisplay(this.getSite().getShell().getDisplay());
		workspace.addResourceChangeListener(m_filechangelistener);
		/* Setup hotkey */
		viewer.getControl().addKeyListener(new HotKeyListener(actionmanager));
		// Setup GTTTestScriptView as the default view of gtt file
		if( GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage() != null) {
			IPackagesViewPart vt = (IPackagesViewPart) GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.eclipse.jdt.ui.PackageExplorer");
			if(vt != null)
				vt.getTreeViewer().addDoubleClickListener(new GTTFileHandler());
		}
		// Setup Selection Change Handler
		m_selectionHandler = new SelectionChangHandler();
		m_selectionHandler.setViewer(viewer);
		// Setup Preference Page
		GTTlipse.getDefault().getPreferenceStore().setValue("LoadScript", "Manual");
		GTTlipse.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener()
		{
		   public void propertyChange(PropertyChangeEvent e)
		   {
			   if(e.getProperty().equals("LoadScript") && 
				  !e.getOldValue().equals(e.getNewValue())) {
				  IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				  page.removeSelectionListener(m_selectionHandler);
				  if(e.getNewValue().equals("Auto")) {
					  page.addSelectionListener(m_selectionHandler);
				  } else if(e.getNewValue().equals("Manual")) {
					  page.removeSelectionListener(m_selectionHandler);
				  }
			   }
		   }
		});
		
		// add listener to execution command
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new ILaunchesListener(){

			@Override
			public void launchesAdded(ILaunch[] launches) {
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	doSave(null);
				    }
				});
			}

			@Override
			public void launchesChanged(ILaunch[] launches) {
				// TODO Auto-generated method stub
			}

			@Override
			public void launchesRemoved(ILaunch[] launches) {
				// TODO Auto-generated method stub
			}
			
		});
		//當執行結束後更新畫面
		DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener(){

			@Override
			public void handleDebugEvents(final DebugEvent[] arg0) {				
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	if(arg0[0].getKind() == DebugEvent.TERMINATE){
							m_testreporthandler = new TestReportHandler(viewer,m_failnodes);
							m_testreporthandler.windowActivated(null);
						}
				    }
				});
				
					
			}
			
		});
	}
	
	private void initImageRegistry() {
//		 init GTTlipse picture
		URL imgurl;
		image_registry = GTTlipse.getDefault().getImageRegistry();
		String  picfile[][] = {
				{TestScriptTage.PROJECT_NODE,"javaProject.png"},{TestScriptTage.SOURCE_FOLDER_NODE,"SourceFolder.png"},
				{TestScriptTage.PACKAGE_NODE,"Package.png"},
				{TestScriptTage.CLASS_NODE,"TestCase.png"},{TestScriptTage.METHOD_NODE,"Method.png"},
				{TestScriptTage.TEST_SCRIPT_DOCUMENT,"TestScriptDocument.png"},
				{TestScriptTage.TEST_SCRIPT_DOCUMENT_FAIL,"TestScriptDocument_Fail.png"},{TestScriptTage.FOLDER_NODE,"FolderNode.png"},
				{TestScriptTage.EVENT_NODE,"EventNode.png"},{TestScriptTage.EVENT_NODE_FAIL,"EventNode_Fail.png"},
				{TestScriptTage.ASSERT_NODE_FAIL,"AssertNode_Fail.png"},{TestScriptTage.ASSERT_NODE,"AssertNode.png"},
				{TestScriptTage.AUT_INFO_NODE,"AUTInfoNode.png"},
				{TestScriptTage.ABOUT,"about.gif"},{TestScriptTage.DEL,"delete.gif"},
				{TestScriptTage.EDIT,"edit.jpg"},{TestScriptTage.DEL,"delete.gif"},
				{TestScriptTage.COPY,"copy.jpg"},{TestScriptTage.CUT,"cut.gif"},
				{TestScriptTage.PASTE,"paste.gif"},{TestScriptTage.UP,"up.gif"},
				{TestScriptTage.DOWN,"down.gif"},
				{TestScriptTage.REFERENCE_MACRO_EVENT_NODE,"node2_eventList.gif"},
				{TestScriptTage.REFERENCE_MACRO_EVENT_NODE_FAIL,"node2_eventList_Fail.gif"},
				{TestScriptTage.REFERENCE_FIT_NODE, "fitnode.gif"},
				{TestScriptTage.ORACLE_NODE,"TestOracle.png"},{TestScriptTage.ORACLE_NODE_FAIL,"TestOracle_Fail.png"},
				{TestScriptTage.OK,"folderOk.gif"},{TestScriptTage.ERROR,"folderErr.gif"},
				{TestScriptTage.REPLAY,"replay2.gif"}
				};
		try {
			for(int i=0;i<picfile.length;i++) {
				if(image_registry.get(picfile[i][0]) == null){
					// Register image files
					imgurl = new URL(baseurl, picfile[i][1]);
					image_registry.put(picfile[i][0], ImageDescriptor.createFromURL(imgurl));
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// uhsing 2010/11/10
	private void initMouseListener() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// Find and store the test case node
				_testCaseNode = findTestCaseNode();
			}
		});
	}
	
	// uhsing 2010/11/10
	private TestCaseNode findTestCaseNode() {
		IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		Object node = selection.getFirstElement();
		
		// Identify the parent of the selected node is TestCaseNode or not
		while(!(node instanceof InvisibleRootNode) && (node != null)) {
			if (node instanceof BaseNode) {
				if (node instanceof TestCaseNode) {
					return (TestCaseNode)node;
				}
				System.out.println("BaseNode: " + ((BaseNode)node).getName());
				node = ((BaseNode)node).getParent();
			}
			else if (node instanceof AbstractNode) {
				System.out.println("AbstractNode: " + ((AbstractNode)node).toString());
				if(((AbstractNode)node).getDocument() != null) {
					node = ((AbstractNode)node).getDocument();
				}
				else {
					node = ((AbstractNode)node).getParent();
				}
			}
			else if (node instanceof TestScriptDocument) {
				System.out.println("TestScriptDocument: " + ((TestScriptDocument)node).toString());
				node = ((TestScriptDocument)node).getParent();
			}
			else {
				System.out.println("Unknown: " + node.toString());
			}
		}
		return null;
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				GTTTestScriptView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
//		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillContextMenu(IMenuManager manager) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		MenuManager menuManager = (MenuManager) manager;
		if (selection.getFirstElement() instanceof ProjectNode) {
			menuManager.add(actionmanager.getAction(ActionType.ADD_SOURCE_FOLDER_NODE));
			// refactoring menu
			MenuManager refactorMenu = new MenuManager("Refactor");
			menuManager.add(refactorMenu);
			int addlist[] = {ActionType.REFEACTORING_RENAME_WINDOW_TITLE};
			actionmanager.addToContributionManager(refactorMenu, addlist);
			menuManager.add(refactorMenu);
		}else if (selection.getFirstElement() instanceof SourceFolderNode ||
			selection.getFirstElement() instanceof PackageNode) {
			MenuManager subMenu = new MenuManager("Add Node");
			menuManager.add(subMenu);
			int addlist[] = {ActionType.ADD_PACKAGE_NODE,	ActionType.ADD_CASE_NODE};
			actionmanager.addToContributionManager(subMenu, addlist);
		} else if (selection.getFirstElement() instanceof TestCaseNode) {
			menuManager.add(actionmanager.getAction(ActionType.ADD_METHOD_NODE));
			menuManager.add(actionmanager.getAction(ActionType.REPLAY));
			// refactoring menu
			MenuManager refactorMenu = new MenuManager("Refactor");
			menuManager.add(refactorMenu);
			int addlist[] = {ActionType.REFEACTORING_RENAME_WINDOW_TITLE};
			actionmanager.addToContributionManager(refactorMenu, addlist);
			menuManager.add(refactorMenu);
		} else if (selection.getFirstElement() instanceof TestMethodNode) {
			int addlist[] = {ActionType.ADD_TEST_SCRIPT_DOCUMENT,	ActionType.PASTE_NODE,
							 ActionType.REPLAY};
			actionmanager.addToContributionManager(menuManager, addlist);
		} else if (selection.getFirstElement() instanceof TestScriptDocument ||
				   selection.getFirstElement() instanceof FolderNode) {
			MenuManager subMenu = new MenuManager("Add Node");
			menuManager.add(subMenu);
			int addlist[] = {ActionType.ADD_FOLDER_NODE,	ActionType.ADD_EVENT_NODE,
							 ActionType.ADD_ASSERT_NODE,	ActionType.ADD_AUTINFO_NODE,
							 ActionType.ADD_ORACLE_NODE};
			actionmanager.addToContributionManager(subMenu, addlist);
		} else if (selection.getFirstElement() instanceof EventNode ||
			selection.getFirstElement() instanceof ViewAssertNode) {
			MenuManager refactorMenu = new MenuManager("Refactor");
			menuManager.add(refactorMenu);
			int addlist[] = {ActionType.REFEACTORING_RENAME};
			actionmanager.addToContributionManager(refactorMenu, addlist);
		}		
		if (!(selection.getFirstElement() instanceof PackageNode) &&
			!(selection.getFirstElement() instanceof TestCaseNode) &&
			!(selection.getFirstElement() instanceof TestMethodNode)) {
			int addlist[] = {ActionType.EDIT_NODE,	ActionType.COPY_NODE,
							 ActionType.CUT_NODE,	ActionType.PASTE_NODE};
			actionmanager.addToContributionManager(menuManager, addlist);
		}
		int addlist[] = {
				ActionType.DEL_NODE,		ActionType.SEPARATOR,	ActionType.UP_MOVE_NODE,
				ActionType.DOWN_MOVE_NODE,	ActionType.GOTO_CODE,	ActionType.SEPARATOR,
				ActionType.OPEN_NEW_FILE};
		actionmanager.addToContributionManager(menuManager, addlist);
		// drillDownAdapter.addNavigationActions(menuManager);
		// Other plug-ins can contribute there actions here
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		Tree tree = viewer.getTree();
		Menu menu = menuManager.createContextMenu(tree);
		tree.setMenu(menu);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		int addlist[] = {
				ActionType.OPEN_NEW_FILE,	ActionType.SEPARATOR,		ActionType.STOP_RECORD,
				ActionType.RECORD,			ActionType.SYNC,			ActionType.SEPARATOR,
				ActionType.DEL_NODE,		ActionType.UP_MOVE_NODE,	ActionType.DOWN_MOVE_NODE,
				ActionType.FILTER,			ActionType.ADD_ORACLE,		ActionType.COLLECT_COMP_INFO
				};
		actionmanager.addToContributionManager(manager, addlist);
		// drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		int[] actionlist ={
				ActionType.OPEN_NEW_FILE,	ActionType.ADD_ORACLE,			ActionType.STOP_RECORD,
				ActionType.RECORD,			ActionType.GOTO_CODE,			ActionType.SYNC,
				ActionType.ADD_PACKAGE_NODE,ActionType.ADD_CASE_NODE,		ActionType.ADD_METHOD_NODE,
				ActionType.ADD_TEST_SCRIPT_DOCUMENT,						ActionType.COLLECT_COMP_INFO,
				ActionType.ADD_FOLDER_NODE,	ActionType.ADD_ASSERT_NODE,		ActionType.ADD_EVENT_NODE,
				ActionType.ADD_AUTINFO_NODE,ActionType.DEL_NODE,			ActionType.EDIT_NODE,			
				ActionType.COPY_NODE,		ActionType.CUT_NODE,			ActionType.PASTE_NODE,			
				ActionType.UP_MOVE_NODE,	ActionType.DOWN_MOVE_NODE,		ActionType.FILTER,
				ActionType.ADD_SOURCE_FOLDER_NODE,							ActionType.ADD_ORACLE_NODE,
				ActionType.REFEACTORING_RENAME, 							ActionType.REFEACTORING_RENAME_WINDOW_TITLE,
				ActionType.REPLAY
				};
		actionmanager = new ActionManager(viewer);
		actionmanager.initActions(actionlist);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				if (selection.getFirstElement() instanceof ProjectNode)
					actionmanager.getAction(ActionType.OPEN_NEW_FILE).run();
				else actionmanager.getAction(ActionType.EDIT_NODE).run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public TreeViewer getTreeViewer(){
		return viewer;
	}

	public void addReferenceMacroEventNode(String path){
		AddReferenceMacroEventNode action = new AddReferenceMacroEventNode();
		action.setViewer(viewer);
		action.addNode(path);
	}
	
	public void addReferenceFitNode(String path){
		AddReferenceFitNodeAction action = new AddReferenceFitNodeAction();
		action.setViewer(viewer);
		action.addNode(path);
	}
	
	public void addComponent(IComponent comp){
		if(m_comps.size() >= 20)
			m_comps.remove();
		m_comps.add(comp);
	}
	
	public Queue<IComponent> getHistoryComponents(){
		return m_comps;
	}

	@Override
	public int promptToSaveOnClose() {
		return 0;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IActionFactory factory = new ActionFactory();
		IAction saveAction = factory.getAction(viewer, ActionType.SAVE_FILE);
		saveAction.run();
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}
	
	public void setDirty(boolean _isDirty) {
		isDirty = _isDirty;
		firePropertyChange(PROP_DIRTY);
		
		// uhsing 2010/11/10
		if (isDirty) {
			if (_testCaseNode != null) {
				//_presenter.initialize(_testCaseNode);
			}
		}
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		// TODO Auto-generated method stub
		return true;
	}
}
