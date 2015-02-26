package gttlipse.macro.view;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.macro.action.MacroAction;
import gttlipse.macro.action.MacroActionManager;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.view.MacroTabularPresenter;
import gttlipse.view.FocusOnPoint;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;


public class MacroViewPart extends ViewPart {
	private TreeViewer m_treeviewer;
	private MacroPresenter m_presenter;
	private MacroActionManager m_actionMgr;
	private ViewContentProvider m_viewContent;
	private Set<Object> m_failnodes;
	
	// uhsing 2010/10/26
	private MacroTabularPresenter _presenter = null;

	public MacroViewPart() {
	}

	public TreeViewer getViewer() {
		return m_treeviewer;
	}

	public MacroPresenter getPresenter() {
		return m_presenter;
	}
	
	public MacroTabularPresenter getTabularPresenter() {
		return _presenter;
	}
	
	public Set<Object> getFailnodes() {
		return m_failnodes;
	}

	public void createPartControl(Composite content) {
		m_failnodes = new HashSet<Object>();

		// uhsing 2010/10/26
		_presenter = new MacroTabularPresenter();
		
		ToolTipHandler tooltip = new ToolTipHandler(content.getShell());
		// create tree viewer
		m_treeviewer = new TreeViewer(content, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		tooltip.activateHoverHelp(m_treeviewer.getTree());
		// create macro presenter
		m_presenter = new MacroPresenter(m_treeviewer);
		// create view content
		m_viewContent = new ViewContentProvider(getViewSite());

		// init dependencies among viewr, content, and presenter
		m_treeviewer.setContentProvider(m_viewContent);
		m_presenter.setViewContentProvider(m_viewContent);
		m_treeviewer.setLabelProvider(new ViewLabelProvider(m_failnodes));
		m_treeviewer.setInput(getViewSite());
		m_viewContent.initialize(m_presenter.getDocument().getMacroScript());
		m_treeviewer.refresh();

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		moveToPointTarget();
		
		// uhsing 2010/10/26
		initMouseListener();

		// 結束時將錯誤的節點圖像改變
		DebugPlugin.getDefault().addDebugEventListener(
				new IDebugEventSetListener() {
					@Override
					public void handleDebugEvents(final DebugEvent[] arg0) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								if (arg0[0].getKind() == DebugEvent.TERMINATE) {
									m_presenter.doAfterTerminate(m_failnodes, _presenter);
								}
							}
						});
					}
				});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(m_treeviewer.getControl());
		m_treeviewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, m_treeviewer);
	}

	private void moveToPointTarget() {
		m_treeviewer.getControl().addKeyListener(new KeyListener() {
			class selectChgListener implements ISelectionChangedListener {
				@Override
				public void selectionChanged(SelectionChangedEvent e) {
					IStructuredSelection selection = (IStructuredSelection) e
							.getSelection();
					Object selectedNode = selection.getFirstElement();
					if (selectedNode instanceof MacroEventCallerNode) {
						AbstractMacroNode refnode = ((MacroEventCallerNode) (selectedNode))
								.getReference();
						FocusOnPoint action = new FocusOnPoint();
						action.focusOnPoint(m_treeviewer, refnode);
						m_treeviewer.refresh();
					}
				}
			}

			private selectChgListener _listener = new selectChgListener();

			@Override
			public void keyReleased(KeyEvent event) {
				if (event.keyCode == SWT.ALT) {
					m_treeviewer.removeSelectionChangedListener(_listener);
				}
			}

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.ALT) {
					m_treeviewer.addSelectionChangedListener(_listener);
				}
			}
		});
	}

	// uhsing 2010/10/26
	private void initMouseListener() {
		m_treeviewer.getControl().addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {}

			@Override
			public void mouseDown(MouseEvent e) {
				// ALT + Left click (e.button == 1) && (e.stateMask == SWT.ALT)
				// Left Click
				if (e.button == 1) {
					IStructuredSelection selection = (IStructuredSelection)m_treeviewer.getSelection();
					Object selectedNode = selection.getFirstElement();
					
					if (selectedNode instanceof MacroComponentNode || selectedNode instanceof MacroEventNode) {
						_presenter.initialize(selectedNode, TableModel.NON_ERROR);
					}
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {}
		});
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void setupComponentNodePopMenu(IMenuManager manager) {
		MenuManager menuManager = (MenuManager) manager;
		menuManager.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME));
		refactorMenu.add(m_actionMgr
				.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT));
		refactorMenu.add(m_actionMgr
				.getAction(MacroAction.REFACTORING_MOVE_COMPONENT));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(refactorMenu);
		
		setMenuManager(menuManager);
	}

	private void setupMacroComponentNodePopMenu(IMenuManager manager) {
		MenuManager menuManager = (MenuManager) manager;

		MenuManager subMenu = new MenuManager("Insert Node　　");
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_COMPONENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_DYNAMIC_COMPONENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_MACRO_COMPONENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_MACRO_EVENT_NODE));
		
		// 加入fit node by pan
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_FIT_NODE));
		
		//加入include node @20110328 by loveshoo
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_INCLUDE_NODE));
		
		menuManager.add(subMenu);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menuManager.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME_WINDOW_TITLE));
		refactorMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_INLINE_MACRO_COMPONENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_MOVE_MACRO_COMPONENT));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(refactorMenu);


		// add detection submenu
		MenuManager detectionMenu = new MenuManager("Bad Smell Detection");
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_LONGMACROEVENT));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_LONGMACROCOMP));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_LONGARG));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_DUPLICATEEVENT));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_SHOTGUNSURGERYUSAGE));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_LACKENCAPSULATION));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_MIDDLEMAN));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_HIERARCHY));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_FEATUREENVY));
		detectionMenu.add(m_actionMgr.getAction(MacroAction.DETECT_ALLSMELL));
		menuManager.add(detectionMenu);
		
		// add statistic submenu
		MenuManager statisticMenu = new MenuManager("Statistic");
		statisticMenu.add(m_actionMgr.getAction(MacroAction.STAT_NODES));
		statisticMenu.add(m_actionMgr.getAction(MacroAction.STAT_SEARCH_COST));
		menuManager.add(statisticMenu);
		
		setMenuManager(menuManager);
	}

	private void setupMacroEventNodePopMenu(IMenuManager manager) {
		MenuManager menuManager = (MenuManager) manager;

		MenuManager subMenu = new MenuManager("Insert Node　　");
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_COMPONENT_EVENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_DYNAMIC_COMPONENT_EVENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_SINGLE_MACRO_EVENT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_VIEW_ASSERT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_EXISTENCE_ASSERT_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_LAUNCH_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_EVENT_TRIGGER_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_FIT_TABLE_ASSERTION_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_FIT_ASSERTION_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_SPLIT_DATA_NODE));

		menuManager.add(subMenu);

		menuManager.add(m_actionMgr.getAction(MacroAction.REPLAY));
		menuManager.add(m_actionMgr.getAction(MacroAction.MULTI_USER_REPLAY));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menuManager.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_MOVE_MACRO_EVENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_REMOVE_MIDDLE_MAN));
		// parameter submenu
		MenuManager parameterMenu = new MenuManager("Parameter　　");
		parameterMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_ADD_PARAMETER));
		parameterMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_REMOVE_PARAMETER));
		parameterMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME_PARAMETER));
		parameterMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_INLINE_PARAMETER));
		refactorMenu.add(parameterMenu);

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(refactorMenu);

		setMenuManager(menuManager);
	}

	private void setupComponentEventNodePopMenu(IMenuManager manager) {
		MenuManager menuManager = (MenuManager) manager;
		menuManager.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		menuManager.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_EVENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_PARAMETER));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(refactorMenu);

		setMenuManager(menuManager);
	}

	private void setupMacroEventCallPopMenu(IMenuManager manager) {
		MenuManager mgr = (MenuManager) manager;
		mgr.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));
		// move to point
		mgr.add(m_actionMgr.getAction(MacroAction.FOCUS_ON_POINT));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_EVENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_INLINE_MACRO_EVENT));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_HIDE_DELEGATE));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_PARAMETER));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(refactorMenu);

		setMenuManager(mgr);
	}

	private void setMenuManager(MenuManager mgr) {
		Tree tree = m_treeviewer.getTree();
		Menu menu = mgr.createContextMenu(tree);
		tree.setMenu(menu);
	}

	// 對node只有編輯、剪下、複制、貼上和刪除操作的跳出設定
	private void setupNormalType(IMenuManager manager) {
		MenuManager mgr = (MenuManager) manager;
		
		mgr.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		setMenuManager(mgr);
	}

	// 設定fit node之跳出選單
	private void setupFitNodePopMenu(IMenuManager manager) {
		MenuManager mgr = (MenuManager) manager;

		// 加入對應產生component name 的node
		MenuManager subMenu = new MenuManager("Insert Node　　");
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_SPLIT_DATA_AS_NAME_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_GENERATE_ORDER_NAME_NODE));
		subMenu.add(m_actionMgr.getAction(MacroAction.INSERT_FIX_NAME_NODE));

		mgr.add(subMenu);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.CUT_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.COPY_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.PASTE_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.DELETE_MACRO_NODE));

		// add refactoring submenu
		MenuManager refactorMenu = new MenuManager("Refactoring　　");
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_RENAME));
		refactorMenu.add(m_actionMgr.getAction(MacroAction.REFACTORING_EXTRACT_MACRO_COMPONENT));

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(refactorMenu);

		setMenuManager(mgr);
	}

	private void fillContextMenu(IMenuManager mgr) {
		AbstractMacroNode node = m_presenter.getSelectedNode();

		if (node instanceof ComponentNode) {
			setupComponentNodePopMenu(mgr);
			return;
		}

		if (node instanceof DynamicComponentNode) {
			setupNormalType(mgr);
			return;
		}
		
		if (node instanceof MacroComponentNode) {
			setupMacroComponentNodePopMenu(mgr);
			return;
		}

		if (node instanceof MacroEventNode) {
			setupMacroEventNodePopMenu(mgr);
			return;
		}

		if (node instanceof ComponentEventNode) {
			setupComponentEventNodePopMenu(mgr);
			return;
		}

		if (node instanceof MacroEventCallerNode) {
			setupMacroEventCallPopMenu(mgr);
			return;
		}

		if (node instanceof ViewAssertNode) {
			// view assert node need refactoring method
			setupComponentEventNodePopMenu(mgr);
			// setupNormalType(manager);
			return;
		}

		if (node instanceof ExistenceAssertNode) {
			// view assert node need refactoring method
			setupComponentEventNodePopMenu(mgr);
			// setupNormalType(manager);
			return;
		}

		if (node instanceof LaunchNode) {
			// view assert node need refactoring method
			setupComponentEventNodePopMenu(mgr);
			// setupNormalType(manager);
			return;
		}
		
		if (node instanceof IncludeNode) {
			setupNormalType(mgr);
		}

		if (node instanceof ModelAssertNode) {
			return;
		}

		if (node instanceof EventTriggerNode) {
			setupNormalType(mgr);
			return;
		}

		if (node instanceof FitStateAssertionNode) {
			setupNormalType(mgr);
			return;
		}

		if (node instanceof FitNode) {
			setupFitNodePopMenu(mgr);
			return;
		}

		if (node instanceof SplitDataAsNameNode) {
			setupNormalType(mgr);
			return;
		}

		if (node instanceof GenerateOrderNameNode) {
			setupNormalType(mgr);
			return;
		}

		if (node instanceof FixNameNode) {
			setupNormalType(mgr);
			return;
		}

		if (node instanceof FitAssertionNode) {
			setupNormalType(mgr);
			return;
		}
		
		if (node instanceof SplitDataNode) {
			setupNormalType(mgr);
			return;
		}
	}

	private void fillLocalToolBar(IToolBarManager mgr) {
		//remove this action @20110325 by loveshoo
//		mgr.add(m_actionMgr.getAction(MacroAction.GENERATE_TO_MACRO_COMPONENT));
		mgr.add(m_actionMgr.getAction(MacroAction.REFRESH));
		mgr.add(m_actionMgr.getAction(MacroAction.CREATE_METS));
		mgr.add(m_actionMgr.getAction(MacroAction.DETECT_OUTER_USAGE));
		mgr.add(new Separator());
		mgr.add(m_actionMgr.getAction(MacroAction.UP_MACRO_NODE));
		mgr.add(m_actionMgr.getAction(MacroAction.DOWN_MACRO_NODE));
		mgr.add(new Separator());
		mgr.add(m_actionMgr
				.getAction(MacroAction.INSERT_MACRO_EVENT_TO_TEST_SCRIPT));
		mgr.add(m_actionMgr.getAction(MacroAction.COPY_TEST_SCRIPT_TO_MACRO));
	}

	private void makeActions() {
		m_actionMgr = new MacroActionManager(this.getViewer(), m_presenter);
	}

	private void hookDoubleClickAction() {
		m_treeviewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				m_actionMgr.getAction(MacroAction.EDIT_MACRO_NODE).run();
			}
		});
	}

	public void setFocus() {
		m_treeviewer.getControl().setFocus();
	}

	static final String MACRO_VIEW_ID = "GTTlipse.views.macroScriptView";

	public static MacroViewPart getMacroViewPart() {
		IViewPart p = GTTlipse.findView(MACRO_VIEW_ID);
		if (p == null)
			p = GTTlipse.showView(MACRO_VIEW_ID);
		// 找到 MacroView
		return (MacroViewPart) p;
	}

	public static MacroPresenter getMacroPresenter() {
		return getMacroViewPart().getPresenter();
	}

	public static ViewLabelProvider getLabelProvider() {
		return (ViewLabelProvider) getMacroViewPart().getViewer()
				.getLabelProvider();
	}

}