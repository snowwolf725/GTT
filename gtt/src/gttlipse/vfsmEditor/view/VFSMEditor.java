/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.view;

import gttlipse.GTTlipse;
import gttlipse.vfsmEditor.actions.ActionType;
import gttlipse.vfsmEditor.actions.ToggleAutomaticLayoutAction;
import gttlipse.vfsmEditor.actions.VFSMActionFactory;
import gttlipse.vfsmEditor.dnd.DiagramTemplateTransferDropTargetListener;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.parts.VFSMPartFactory;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;


/**
 * @author zhanghao modify by Jason Wang To change the template for this
 *         generated type comment go to Window - Preferences - Java - Code Style
 *         - Code Templates
 */
public class VFSMEditor extends GraphicalEditorWithPalette implements
		IVFSMEditor {

	private IVFSMPresenter m_Presenter;

	private DragEnableTreeViewer m_treeViewer;
	private PaletteRoot m_paletteRoot;
//	private Action openOutlineAction;
	private Action m_doubleClickAction;
	private Action m_TCGenerationAction;
	private Action m_declarationAction;
//	private Action m_inheritAction;
	private Action m_newContextAction;
	private Action m_copyAction;
	private Action m_pasteAction;
	private Action m_deleteAction;
	private Action m_moveUpAction;
	private Action m_moveDownAction;
	
	public IVFSMPresenter getPresenter(){
		return m_Presenter;
	}

	public VFSMEditor() {
		setupImageRegistry();
		setEditDomain(new DefaultEditDomain(this));
	}

	IVFSMDagram m_FSMDiagram = new VFSMDiagram();

	/**
	 * inital a VFSM diagram -
	 */
	private void initVFSMDiagram() {
		m_FSMDiagram = new VFSMDiagram();
	}

	public class DragEnableTreeViewer extends TreeViewer implements
			TransferDragSourceListener {

		public DragEnableTreeViewer(Composite parent, int style) {
			super(parent, style);
		}

		public Transfer getTransfer() {
			return TemplateTransfer.getInstance();
		}

		protected void hookControl(Control control) {
			super.hookControl(control);
			int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
			DragSource drag = new DragSource(control, operations);
			drag.addDragListener(this);
			drag.setTransfer(new Transfer[] { this.getTransfer() });
		}

		public void dragFinished(DragSourceEvent event) {
			TemplateTransfer.getInstance().setTemplate(null);
		}

		public void dragSetData(DragSourceEvent event) {
			event.data = getTemplate();
		}

		public void dragStart(DragSourceEvent event) {
			Object template = getTemplate();
			if (template == null)
				event.doit = false;
			TemplateTransfer.getInstance().setTemplate(template);
		}

		protected Object getTemplate() {
			Object selectionObject = ((IStructuredSelection) this
					.getSelection()).getFirstElement();
			if (selectionObject == null)
				return null;
			if (selectionObject instanceof Node) {
				return selectionObject;
			}
			return null;
		}
	}

	public void createPartControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		super.createPartControl(sashForm);
		sashForm.setWeights(new int[] { 100, 20 });
		doOpenFile();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
		getGraphicalViewer().setEditPartFactory(new VFSMPartFactory());
		// create a ContextMenuProvider to do pop up menu.
		ContextMenuProvider contextMenu = new VFSMEditorContextMenuProvider(
				getGraphicalViewer(), getActionRegistry());
		getGraphicalViewer().setContextMenu(contextMenu);
		// create a Graphical Viewer to outline
//		openOutlineAction = new OpenOutlineViewAction();
	}

	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().addDropTargetListener(
				new DiagramTemplateTransferDropTargetListener(
						getGraphicalViewer()));

		initVFSMDiagram();

		Composite sashForm = new SashForm(getGraphicalViewer().getControl()
				.getParent().getParent(), SWT.HORIZONTAL);
		initTreeViewForm(sashForm);
		initActions();
		hookContextMenu();
		hookDoubleClickAction();
	}

	private void initTreeViewForm(Composite sashForm) {
		SashForm form = new SashForm(sashForm, SWT.VERTICAL);
		// 存檔按鈕
		createBtnSave(form);
		// 讀檔按鈕
		createBtnOpen(form);
		// Tree View
		initTreeView(form);
		// 指定以上三個widgets 的寬度
		form.setWeights(new int[] { 1, 1, 20 });
	}

	private void initTreeView(SashForm sashForm2) {
		m_treeViewer = new DragEnableTreeViewer(sashForm2, DND.DROP_MOVE);
		m_treeViewer.setContentProvider(new ViewContentProvider(m_FSMDiagram));
		m_treeViewer.setLabelProvider(new ViewLabelProvider(IMAGE_REGISTRY));

		m_treeViewer.getControl().setBounds(0, 20, 120, 250);
		m_treeViewer.setInput(m_FSMDiagram.getFSMRoot());
		m_treeViewer.expandToLevel(6);

		m_Presenter = new VFSMPresenter(this, m_treeViewer, getGraphicalViewer());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see GTTlipse.VFSMEditor.ui.IVFSMEditor#setPartName(java.lang.String)
	 */
	public void setPartName(String name) {
		super.setPartName(name);
	}

	private void createBtnOpen(Composite parent) {
		Button btnOpen = new Button(parent, SWT.PUSH);
		btnOpen.setText("Open");
		btnOpen.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doOpenFile();
			}
		});
	}

	private void createBtnSave(Composite parent) {
		Button btnSave = new Button(parent, SWT.PUSH);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSaveFile();
			}
		});
	}

	private void doOpenFile() {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		String filename = file.getLocation().toOSString();
		m_FSMDiagram.read(filename);

		// 讀完檔之後 ，要更新 view/model 的關係
		m_treeViewer.setInput(m_FSMDiagram.getFSMRoot());
		m_treeViewer.refresh();
		// 顯示 main diagram
		m_Presenter.diplayMainDiagram();
	}

	private void doSaveFile() {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		String filename = file.getLocation().toOSString();
		m_FSMDiagram.save(filename);
	}

	protected void createActions() {
		super.createActions();
	}

	public void doSave(IProgressMonitor monitor) {
		doSaveFile();
	}

	public void doSaveAs() {
	}

	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}

	private void initActions() {
		VFSMActionFactory factory = new VFSMActionFactory();

		m_doubleClickAction = factory.getAction(m_Presenter,
				ActionType.DOUBLECLICK_ACTION);

		m_TCGenerationAction = factory.getAction(m_Presenter,
				ActionType.TCGENERATION_ACTION);
		m_TCGenerationAction.setImageDescriptor(IMAGE_REGISTRY
				.getDescriptor("analyze"));

		m_declarationAction = factory.getAction(m_Presenter,
				ActionType.DECLARATION_ACTION);
		m_declarationAction.setImageDescriptor(IMAGE_REGISTRY
				.getDescriptor("composite"));

		m_newContextAction = factory.getAction(m_Presenter,
				ActionType.NEWCONTEXT_ACTION);
		m_newContextAction.setImageDescriptor(IMAGE_REGISTRY
				.getDescriptor("newFile"));

		m_copyAction = factory
				.getAction(m_Presenter, ActionType.COPYNODE_ACTION);
		m_copyAction.setImageDescriptor(IMAGE_REGISTRY.getDescriptor("copy"));

		m_pasteAction = factory.getAction(m_Presenter,
				ActionType.PASTENODE_ACTION);
		m_pasteAction.setImageDescriptor(IMAGE_REGISTRY.getDescriptor("paste"));

		m_deleteAction = factory.getAction(m_Presenter,
				ActionType.REMOVENODE_ACTION);
		m_deleteAction.setImageDescriptor(IMAGE_REGISTRY
				.getDescriptor("delete"));

		m_moveUpAction = factory
				.getAction(m_Presenter, ActionType.MOVEUP_ACTION);
		m_moveUpAction.setImageDescriptor(IMAGE_REGISTRY.getDescriptor("up"));

		m_moveDownAction = factory.getAction(m_Presenter,
				ActionType.MOVEDOWN_ACTION);
		m_moveDownAction.setImageDescriptor(IMAGE_REGISTRY
				.getDescriptor("down"));

		IAction action = new ToggleAutomaticLayoutAction(m_Presenter);
		getActionRegistry().registerAction(action);

		setActionText();
	}

	private void setActionText() {
		m_TCGenerationAction.setText("TestCase Generation");
		m_declarationAction.setText("Declaration");
		// m_inheritAction.setText("Inheritance");
		m_newContextAction.setText("New File");
		m_copyAction.setText("Copy Graph");
		m_pasteAction.setText("Paste Graph");
		m_deleteAction.setText("Delete Graph");
		m_moveUpAction.setText("Move up Graph");
		m_moveDownAction.setText("Move down Graph");
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(m_newContextAction);
		manager.add(m_copyAction);
		manager.add(m_pasteAction);
		manager.add(m_deleteAction);
		manager.add(new Separator());
		manager.add(m_moveUpAction);
		manager.add(m_moveDownAction);
		manager.add(new Separator());
		manager.add(m_declarationAction);
		// manager.add(m_inheritAction);
		manager.add(m_TCGenerationAction);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				VFSMEditor.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(m_treeViewer.getControl());
		m_treeViewer.getControl().setMenu(menu);
	}

	private void hookDoubleClickAction() {
		m_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				m_doubleClickAction.run();
			}
		});
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);
		// if (selection instanceof IStructuredSelection) {
		// //下面呼叫的是一個空函式？？ -zws 20080717
		// openOutlineAction.update((IStructuredSelection) selection);
		// }
		/* if selection changed, refresh the viewer */
		m_treeViewer.refresh();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if (adapter == IContentOutlinePage.class)
			return new VFSMContentOutlinePage();
		if (adapter == ZoomManager.class)
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());
		return super.getAdapter(adapter);
	}

	protected FlyoutPreferences getPalettePreferences() {
		return PaletteFactory.createPalettePreferences();
	}

	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer
						.addDragSourceListener(new TemplateTransferDragSourceListener(
								viewer));
			}
		};
	}

	protected PaletteRoot getPaletteRoot() {
		if (m_paletteRoot == null) {
			m_paletteRoot = PaletteFactory.createPalette();
			PaletteFactory.createPalettePreferences();
		}
		return m_paletteRoot;
	}

	protected void initializePaletteViewer() {
		super.initializePaletteViewer();
		getPaletteViewer().addDragSourceListener(
				new TemplateTransferDragSourceListener(getPaletteViewer()));
	}

	// private TransferDropTargetListener createTransferDropTargetListener() {
	// return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
	// protected CreationFactory getFactory(Object template) {
	// return new SimpleFactory((Class<?>) template);
	// }
	// };
	// }

	/*
	 * This class is a Editor part ContextMenuProvider that create a Popup menu
	 * for canvas. Popup menu can pop up UNDO, REDO and DELETE actions.
	 */
	private class VFSMEditorContextMenuProvider extends ContextMenuProvider {
//		private ActionRegistry actionRegistry;

		public VFSMEditorContextMenuProvider(EditPartViewer viewer,
				ActionRegistry registry) {
			super(viewer);
//			actionRegistry = registry;
		}

		public void buildContextMenu(IMenuManager menu) {
			// Add standard action groups to the menu
			GEFActionConstants.addStandardActionGroups(menu);
			// Add actions to the menu
			menu.add(new Separator("edit"));
			menu.add(getActionRegistry()
					.getAction(ActionFactory.DELETE.getId()));
			menu.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
			menu.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
			// menu.add(new Separator("zoom"));
			//menu.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN)
			// );
			//menu.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT
			// ));
		}

//		private IAction getAction(String actionId) {
//			return actionRegistry.getAction(actionId);
//		}
	}

	/*
	 * This class is a IContentOutlinePage that implement a microcosm to show
	 * the Diagram.
	 */
	private class VFSMContentOutlinePage implements IContentOutlinePage {
		private Canvas canvas;
		private ScrollableThumbnail thumbnail;
		private DisposeListener disposeListener;

		public void createControl(Composite parent) {
			this.canvas = new Canvas(parent, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);
			ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart();
			this.thumbnail = new ScrollableThumbnail((Viewport) rootEditPart
					.getFigure());
			this.thumbnail.setSource(rootEditPart
					.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);

			disposeListener = new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(
					disposeListener);
		}

		public Control getControl() {
			return this.canvas;
		}

		public void dispose() {
			if (getGraphicalViewer().getControl() != null
					&& !getGraphicalViewer().getControl().isDisposed()) {
				getGraphicalViewer().getControl().removeDisposeListener(
						disposeListener);
			}
		}

		public void setActionBars(IActionBars actionBars) {
			// nothing to do
		}

		public void setFocus() {
			// nothing to do
		}

		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			// nothing to do
		}

		public ISelection getSelection() {
			// nothing to do
			return null;
		}

		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			// nothing to do
		}

		public void setSelection(ISelection selection) {
			// nothing to do
		}
	}

	private static void setupImageRegistry() {
		URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");
		try {
			imgRegistry(baseurl, "transition", "connectTag.ico");
			imgRegistry(baseurl, "input", "inputTag.ico");
			imgRegistry(baseurl, "output", "outputTag.ico");
			imgRegistry(baseurl, "state", "t_state.ico");
			imgRegistry(baseurl, "superstate", "t_superstate.ico");
			imgRegistry(baseurl, "initial", "t_initial.ico");
			imgRegistry(baseurl, "final", "t_final.ico");
			imgRegistry(baseurl, "andsuperstate", "andState24.ico");
			imgRegistry(baseurl, "folder", "ssfolder.gif");
			imgRegistry(baseurl, "file", "file.gif");
			imgRegistry(baseurl, "inherit", "inheriatnceAct.ico");
			imgRegistry(baseurl, "composite", "t_compositionAct.ico");
			imgRegistry(baseurl, "analyze", "t_Analyze.ico");
			imgRegistry(baseurl, "declaration", "t_declaration.ico");
			imgRegistry(baseurl, "instance", "t_instance.ico");
			imgRegistry(baseurl, "main", "t_main.ico");
			imgRegistry(baseurl, "newFile", "newFile.gif");
			imgRegistry(baseurl, "openFile", "openFile.gif");
			imgRegistry(baseurl, "delete", "delete.gif");
			imgRegistry(baseurl, "copy", "copy.gif");
			imgRegistry(baseurl, "paste", "paste.gif");
			imgRegistry(baseurl, "copy", "copy.gif");
			imgRegistry(baseurl, "cut", "cut.gif");
			imgRegistry(baseurl, "up", "up.gif");
			imgRegistry(baseurl, "down", "down.gif");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private static void imgRegistry(URL baseurl, String tag, String file) throws MalformedURLException {
		URL imgurl;
		if(IMAGE_REGISTRY.get(tag) == null) {
			imgurl = new URL(baseurl, file);			
			IMAGE_REGISTRY.put(tag, ImageDescriptor.createFromURL(imgurl));
		}
	}

}