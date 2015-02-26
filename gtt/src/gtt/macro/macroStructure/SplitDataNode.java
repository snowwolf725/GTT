package gtt.macro.macroStructure;

import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class SplitDataNode extends AbstractMacroNode {

	private String _name = "";
	private String _target = "";
	private String _separator = "";
	private String _data = "";
	
	public static String DEFAULT_SEPARATOR = ",";
	public static String DEFAULT_TARGET = "SplitData Target";
	
	private SplitDataNode(String name, String target) {
		_name = name;
		_target = target;
		_separator = DEFAULT_SEPARATOR;
	}
	
	private SplitDataNode(SplitDataNode node) {
		_name = node.getName();
		_target = node.getTarget();
		_separator = node.getSeparator();
		_data = node.getData();
	}
	
	public static SplitDataNode create(String name, String target) {
		return new SplitDataNode(name, target);
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setTarget(String target) {
		_target = target;
	}
	
	public void setSeparator(String separator) {
		_separator = separator;
	}
	
	public void setData(String data) {
		_data = data;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getTarget() {
		return _target;
	}
	
	public String getSeparator() {
		return _separator;
	}
	
	public String getData() {
		return _data;
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}
	
	@Override
	public SplitDataNode clone() {
		return new SplitDataNode(this);
	}
	
	@Override
	public String toString() {
		return getName() + "(" + _data + ")" + ":" + _target;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_SPLITEDATA_NODE;
	}
}
