package gtt.macro.macroStructure;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class DynamicComponentEventNode extends AbstractMacroNode implements IHaveArgument {

	private ComponentReference _refComponent = null; 
	
	private Arguments _arguments = new Arguments();
	
	private String _eventType = "";
	
	private int _eventID = 0;
	
	public DynamicComponentEventNode() {
		this("");
	}
	
	public DynamicComponentEventNode(String path) {
		_refComponent = new DynamicComponentReferenceImpl(path);
	}
	
	public DynamicComponentEventNode(DynamicComponentNode node) {
		_refComponent = new DynamicComponentReferenceImpl(node.getPath().toString());
	}
	
	public DynamicComponentEventNode(DynamicComponentEventNode target) {
		_refComponent = new DynamicComponentReferenceImpl(target._refComponent);
		_arguments = target.getArguments().clone();
		_eventType = target.getEventType();
		_eventID = target.getEventID();
	}
	
	public void setComponentPath(String path) {
		_refComponent.setReferencepath(path);
	}
	
	public void setEvent(String etype, int eid) {
		_eventType = etype;
		_eventID = eid;
	}
	
	public String getComponentPath() {
		return _refComponent.getReferencePath();
	}

	public DynamicComponentNode getComponent() {
		_refComponent.lookupComponent(this);
		return _refComponent.getDynamicComponent();
	}
	
	@Override
	public String getName() {
		if (getComponent() == null) {
			return "[NULL REFERENCE]";
		}
		return getComponent().getName();
	}
	
	public String getEventType() {
		return _eventType;
	}

	public int getEventID() {
		return _eventID;
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}
	
	@Override
	public AbstractMacroNode clone() {
		return new DynamicComponentEventNode(this);
	}
	
	@Override
	public String toString() {
		return getName() + "." + _eventType + _arguments.toString();
	}

	@Override
	public Arguments getArguments() {
		return _arguments;
	}

	@Override
	public void setArguments(Arguments list) {
		_arguments = list;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_DYNAMIC_COMPONENT_EVENT_NODE;
	}
}
