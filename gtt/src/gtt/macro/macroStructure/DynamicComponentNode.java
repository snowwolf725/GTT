package gtt.macro.macroStructure;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;
import gttlipse.tabular.util.TreeStructureUtil;

import java.util.ArrayList;

public class DynamicComponentNode extends AbstractMacroNode {
	
	public static String DEFAULT_NAME = "DynamicComponent";
	public static String DEFAULT_SOURCE = "DynamicComponent Source";
	public static String DEFAULT_TARGET = "DynamicComponent Target";
	
	// Composite Pattern
	private ComponentNode _compNode = null;
	
	private ArrayList<ComponentNode> _nodes = new ArrayList<ComponentNode>();
	
	private String _name = "";
	private String _source = "";
	
	private DynamicComponentNode(IComponent c) {
		_compNode = ComponentNode.create(c);
	}
	
	private DynamicComponentNode(DynamicComponentNode node) {
		_source = node.getSource();
		_compNode = node.getComponentNode().clone();
	}
	
	public static DynamicComponentNode create() {
		return new DynamicComponentNode(EventModelFactory.createComponent());
	}
	
	public static DynamicComponentNode create(IComponent c) {
		return new DynamicComponentNode(c);
	}
	
	public static DynamicComponentNode create(String type, String name) {
		return new DynamicComponentNode(SwingComponent.create(type, name));
	}
	
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}
	
	@Override
	public DynamicComponentNode clone() {
		return new DynamicComponentNode(this);
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setSource(String source) {
		_source = source;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getSource() {
		return _source;
	}
	
	public ComponentNode getComponentNode() {
		return _compNode;
	}
	
	public String toString() {
		String type = getType();
		
		// Get the component type something like that "JButton"
		int index = type.lastIndexOf(".");
		type = type.substring(index);
		
		return _name + ":" + type;
	}
	
	public void dynamicFinding(String altSrc) {
		// Reset the list
		_nodes.clear();
		
		if (altSrc.equals(_source)) {
			// When the alternative source is just the key of DataPool
			multipleFinding();
		}
		else {
			// When the alternative source is a real name of the component
			singleFinding(altSrc);
		}
	}
	
	private void singleFinding(String altSrc) {
		if (altSrc.equals("")) {
			// Set a null object into DataPool
			setVariable(DEFAULT_TARGET, null);
			return;
		}
		
		// Find a component node by the alternative source
		AbstractMacroNode root = DefaultMacroFinder.findRoot(this);
		find(root, altSrc);
		
		// Set a list of component nodes into DataPool
		setVariable(DEFAULT_TARGET, _nodes);
	}
	
	@SuppressWarnings("unchecked")
	private void multipleFinding() {
		// Get a list of names from the DataPool
		ArrayList<String> names = (ArrayList<String>)getVariable(_source);
		if (names == null) {
			// Set a null object into DataPool
			setVariable(DEFAULT_TARGET, null);
			return;
		}
		
		// Find component nodes by its names
		AbstractMacroNode root = DefaultMacroFinder.findRoot(this);
		for(String name : names) {
			find(root, name);
		}
		
		// Set a list of component nodes into DataPool
		setVariable(DEFAULT_TARGET, _nodes);
	}
	
	private void find(AbstractMacroNode root, String name) {
		AbstractMacroNode node = TreeStructureUtil.findNode(root, name);
		
		if (node instanceof ComponentNode) {
			_nodes.add((ComponentNode)node);
		}
	}
	
	// The following part belongs to the ComponentNode by Composition
	public IComponent getComponent() {
		return _compNode.getComponent();
	}
	
	public String getWinType() {
		return _compNode.getWinType();
	}

	public String getTitle() {
		return _compNode.getTitle();
	}

	public String getType() {
		return _compNode.getType();
	}
	
	public String getComponentName() {
		return _compNode.getName();
	}

	public String getText() {
		return _compNode.getText();
	}

	public int getIndex() {
		return _compNode.getIndex();
	}

	public int getIndexOfSameName() {
		return _compNode.getIndexOfSameName();
	}
	
	public void setWinType(String type) {
		_compNode.setWinType(type);
	}

	public void setTitle(String title) {
		_compNode.setTitle(title);
	}

	public void setType(String type) {
		_compNode.setType(type);
	}

	public void setComponentName(String name) {
		_compNode.setName(name);
	}

	public void setText(String text) {
		_compNode.setText(text);
	}

	public void setIndex(int index) {
		_compNode.setIndex(index);
	}

	public void setIndexOfSameName(int index) {
		_compNode.setIndexOfSameName(index);
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_DYNAMIC_COMPONENT_NODE;
	}
}
