package testing.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

/**
 * Mock Object for AbstractMacroNode
 * 
 * @author zwshen 2007/07/09
 */
public class MockAbstractMacroNode extends AbstractMacroNode {

	@Override
	public void accept(IMacroStructureVisitor v) {
		// nothing to do
	}

	@Override
	public AbstractMacroNode clone() {
		return new MockAbstractMacroNode();
	}

	@Override
	public String getName() {
		return m_name;
	}

	String m_name = "MockAbstractMacroNode";

	@Override
	public void setName(String name) {
		m_name = name;

	}
	
	@Override
	public boolean add(AbstractMacroNode node) {
		if(node==null) return false;
		
		node.setParent(this);
		return true;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_MOCK_ABSTRACT_MACRO_NODE;
	}

}
