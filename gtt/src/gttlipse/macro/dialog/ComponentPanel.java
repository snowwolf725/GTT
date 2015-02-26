package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.GTTlipse;
import gttlipse.macro.view.MacroViewPart;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class ComponentPanel {
	private Tree m_tree = null;
	private AbstractMacroNode m_rootNode = null;
	private AbstractMacroNode m_sharedComponentNode = null;
	private AbstractMacroNode m_parentNode = null;
	private Vector<AbstractMacroNode> m_includeNode = null;	
	private AbstractMacroNode m_selectNode = null;
	private AbstractMacroNode m_systemNode = null;	

	private static final Image m_macroComponentImage = GTTlipse.getDefault()
			.getImageRegistry().get("MacroComponentNode");
	private static final Image m_componentImage = GTTlipse.getDefault()
			.getImageRegistry().get("ComponentNode");
	private static final Image m_dynamicComponentImage = GTTlipse.getDefault()
			.getImageRegistry().get("DynamicComponentNode");
	
	private Label m_selectNodeName = null;
	private Label m_message = null;
	
	public static final int PARSE_SINGLE_LAYER = 0;
	public static final int PARSE_ALL_COMPONENT = 1;
	public static final int PARSE_MACRO_COMPONENT = 2;
	public static final int PARSE_MACRO_SHAREDCOMPONENT = 3;
	public static final int PARSE_DYNAMIC_COMPONENT = 4;
	
	private int _parseType = 0;

	public ComponentPanel(Composite parent, AbstractMacroNode root, AbstractMacroNode checkNode) {
		this(parent, root, checkNode, PARSE_ALL_COMPONENT);
	}

	public ComponentPanel(Composite parent, AbstractMacroNode root, int parseType) {
		// find global root
		while(!(root.getParent() instanceof InvisibleRootNode) && root.getParent() != null) {
			root = root.getParent();
		}
		m_rootNode = root;
		_parseType = parseType;
		setSharedComponentNode();
		initPanel(parent);
	}
	
	public ComponentPanel(Composite parent, AbstractMacroNode root, AbstractMacroNode checkNode, int parseType) {
		// find global root
		while(!(root.getParent() instanceof InvisibleRootNode) && root.getParent() != null) {
			root = root.getParent();
		}
		m_rootNode = root;
		m_parentNode = checkNode.getParent().getParent();
		_parseType = parseType;
		setIncludeNode();
		setSystemNode();
		initPanel(parent);
	}	

	// 建立一個 Tree node
	private TreeItem createTreeItem(TreeItem tParent, AbstractMacroNode node) {

		if (!(node instanceof ComponentNode)
				&& !(node instanceof MacroComponentNode)
				&& !(node instanceof DynamicComponentNode))
			return null;

		TreeItem item;

		if (tParent == null)
			item = new TreeItem(m_tree, SWT.NULL);
		else
			item = new TreeItem(tParent, SWT.NULL);

		item.setImage(getImage(node));

		item.setText(node.toString());
		item.setData(node);

		if (m_selectNode != null) {
			if (m_selectNode.getPath().toString().equals(
					node.getPath().toString()))
				item.setBackground(UsedColors.GREEN_COLOR);
		}

		return item;
	}

	private Image getImage(AbstractMacroNode node) {
		if (node instanceof MacroComponentNode)
			return m_macroComponentImage;
		if (node instanceof ComponentNode)
			return m_componentImage;
		if (node instanceof DynamicComponentNode)
			return m_dynamicComponentImage;
		return null;
	}

	public void setupTree() {
		if (m_rootNode == null)
			return;

		m_tree.removeAll();

		TreeItem item = createTreeItem(null, m_rootNode);
		// change by soriel 100421
		// support three type parse tree
		switch(_parseType) {
		case PARSE_SINGLE_LAYER:
			for (int i = 0; i < m_rootNode.size(); i++) {
				createTreeItem(item, (AbstractMacroNode) m_rootNode.get(i));
			}
			break;
		case PARSE_ALL_COMPONENT:
			parseAllComponent(item, m_rootNode);
			//set IncludeNode
			if(m_includeNode.size() > 0) {
				for(int i = 0; i < m_includeNode.size(); i++) {
					item = createTreeItem(null, m_includeNode.get(i));
					parseAllComponent(item, m_includeNode.get(i));
				}
			}
			break;
		case PARSE_MACRO_COMPONENT:
			parseMacroComponent(item, m_rootNode);
			//set IncludeNode
			if(m_includeNode.size() > 0) {
				for(int i = 0; i < m_includeNode.size(); i++) {
					item = createTreeItem(null, m_includeNode.get(i));
					parseMacroComponent(item, m_includeNode.get(i));
				}
			}
			if(m_systemNode != null) {
				item = createTreeItem(null, m_systemNode);
			}
			break;
		case PARSE_MACRO_SHAREDCOMPONENT:
			parseMacroComponent(item, m_rootNode);
			if(m_sharedComponentNode != null) {
				item = createTreeItem(null, m_sharedComponentNode);
				parseMacroComponent(item, m_sharedComponentNode);
			}
			break;
		case PARSE_DYNAMIC_COMPONENT:
			parseDynamicComponent(item, m_rootNode);
			break;
		}

		// 展開第一層
		m_tree.getItem(0).setExpanded(true);
	}
	
	private void parseMacroComponent(TreeItem item, AbstractMacroNode node) {
		for(int i = 0; i < node.size(); i++) {
			if (node.get(i) instanceof MacroComponentNode) {
				TreeItem newItem = createTreeItem(item, node.get(i));
				parseMacroComponent(newItem, node.get(i));				
			}
		}
	}
	
	private void parseAllComponent(TreeItem item, AbstractMacroNode node) {
		for(int i = 0; i < node.size(); i++) {
			if (node.get(i) instanceof MacroComponentNode) {
				TreeItem newItem = createTreeItem(item, node.get(i));
				parseAllComponent(newItem, node.get(i));				
			}
			
			if (node.get(i) instanceof ComponentNode) {
				createTreeItem(item, node.get(i));
			}
		}
	}

	private void parseDynamicComponent(TreeItem item, AbstractMacroNode node) {
		for(int i = 0; i < node.size(); i++) {
			if (node.get(i) instanceof MacroComponentNode) {
				TreeItem newItem = createTreeItem(item, node.get(i));
				parseDynamicComponent(newItem, node.get(i));				
			}
			
			if (node.get(i) instanceof DynamicComponentNode) {
				createTreeItem(item, node.get(i));
			}
		}
	}
	
	public void setExpanded(AbstractMacroNode node) {
		List<String> v = node.getPath().list();
		TreeItem ti = m_tree.getItem(0);
		ti.setExpanded(true);
		for (int i = 1; i < v.size(); i++) {
			String path = (String) v.get(i);
			for (int j = 0; j < ti.getItemCount(); j++) {
				if (path.equals(((AbstractMacroNode) ti.getItem(j).getData())
						.getName())) {
					ti = ti.getItem(j);
					ti.setExpanded(true);
				}
			}
		}
	}

	private void initInfoGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN);
		lg.setText("Informaction");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		lg.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.widthHint = 450;
		lg.setLayoutData(gd);

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 2;

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;

		m_message = new Label(lg, SWT.NULL);
		m_message.setLayoutData(textFildData);

		Composite selectNodeName = new Composite(lg, SWT.NONE);
		selectNodeName.setLayout(tabLayout);

		Label _name = new Label(selectNodeName, SWT.NULL);
		_name.setText("Select Component Name: ");

		m_selectNodeName = new Label(selectNodeName, SWT.NULL);
		m_selectNodeName.setLayoutData(textFildData);
	}

	private void initTreeGroup(Composite parent) {
		// init group
		Group lg = new Group(parent, SWT.SHADOW_ETCHED_IN);
		lg.setText("Select Component");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		lg.setLayout(gridlayout);

		// init tree
		m_tree = new Tree(lg, SWT.SINGLE);
		GridData gd = new GridData();
		gd.heightHint = 300;
		gd.widthHint = 450;
		m_tree.setLayoutData(gd);

		// setupTree();
	}

	public void setSelectNodeName(String name) {
		m_selectNodeName.setText(name);
	}

	public void setMesssage(String msg) {
		m_message.setText(msg);
	}

	private void initPanel(Composite parent) {
		initInfoGroup(parent);
		initTreeGroup(parent);
	}

	public void setSelectNode(AbstractMacroNode node) {
		m_selectNode = node;
	}

	public Tree getTree() {
		return m_tree;
	}

	public AbstractMacroNode getSelectNode() {
		return m_selectNode;
	}

	public void addSelectionListener(SelectionListener listener) {
		m_tree.addSelectionListener(listener);
	}
	
	private  AbstractMacroNode setSharedComponentNode() {
		//find aut node
		AbstractMacroNode AUTNode = m_rootNode;
		while(AUTNode.getParent() != null && !(AUTNode.getParent() instanceof InvisibleRootNode)) {
			AUTNode = AUTNode.getParent();
		}
		if(!(AUTNode.getParent() instanceof InvisibleRootNode))
			return null;
		//find share component node
		for(int i = 0; i< AUTNode.getChildren().length; i++) {
			if(AUTNode.get(i).getName().equalsIgnoreCase("SharedComponent")) {
				m_sharedComponentNode = AUTNode.get(i);
				break;
			}
		} 
		
		return m_sharedComponentNode;
	}
	
	private void setIncludeNode() {
		//find Include node
		m_includeNode = new Vector<AbstractMacroNode>();
		for(int i = 0; i < m_parentNode.getChildren().length; i++) {
			if(m_parentNode.getChildren()[i] instanceof IncludeNode) {
				m_includeNode.add(MacroViewPart.getMacroPresenter().getDocument().findByPath(((IncludeNode) m_parentNode.getChildren()[i]).getIncludeMacroComPath()));
			}
		}
	}
	
	private AbstractMacroNode setSystemNode() {
		//find root node
		while(m_rootNode.getParent() != null && !(m_rootNode.getParent() instanceof InvisibleRootNode)) {
			m_rootNode = m_rootNode.getParent();
		}
		if(!(m_rootNode.getParent() instanceof InvisibleRootNode))
			return null;
		//find System node
		AbstractMacroNode invisibleRootNode = m_rootNode.getParent();
		for(int i = 0; i< invisibleRootNode.getChildren().length; i++) {
			if(invisibleRootNode.get(i).getName().equalsIgnoreCase("SYSTEM")) {
				m_systemNode = invisibleRootNode.get(i);
				break;
			}
		} 
		
		return m_systemNode;
	}
}
