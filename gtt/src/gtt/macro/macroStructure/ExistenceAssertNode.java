package gtt.macro.macroStructure;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class ExistenceAssertNode extends AbstractMacroNode{
	
	IComponent componentInfo = EventModelFactory.createComponent();
	boolean expectResult = false;
	int expectResultCount = 1;
	
	
	public int getExpectResultCount() {
		return expectResultCount;
	}

	public void setExpectResultCount(int expectResultCount) {
		this.expectResultCount = expectResultCount;
	}

	public boolean getExpectResult() {
		return expectResult;
	}

	public void setExpectResult(boolean expectResult) {
		this.expectResult = expectResult;
	}

	public ExistenceAssertNode() {
	}

	private	ExistenceAssertNode(ExistenceAssertNode node) {
		componentInfo = node.getComponent().clone();
		expectResult = node.getExpectResult();
	}
	
	
	
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public IComponent getComponent() {
		return componentInfo;
	}

	// 將相關操作交給IComponent做
	public String getWinType() {
		return componentInfo.getWinType();
	}

	public String getTitle() {
		return componentInfo.getTitle();
	}

	public String getType() {
		return componentInfo.getType();
	}
	
	public String getComponentName() {
		return componentInfo.getName();
	}

	public String getText() {
		return componentInfo.getText();
	}

	public int getIndex() {
		return componentInfo.getIndex();
	}

	public int getIndexOfSameName() {
		return componentInfo.getIndexOfSameName();
	}

	public void setWinType(String type) {
		componentInfo.setWinType(type);
	}

	public void setTitle(String title) {
		componentInfo.setTitle(title);
	}

	public void setType(String type) {
		componentInfo.setType(type);
	}
	
	public void setComponentName(String name) {
		componentInfo.setName(name);
	}

	public void setText(String text) {
		componentInfo.setText(text);
	}

	public void setIndex(int idx) {
		componentInfo.setIndex(idx);
	}

	public void setIndexOfSameName(int idx) {
		componentInfo.setIndexOfSameName(idx);
	}

	@Override
	public String toString() {
			return getComponent().getName() + ".Existence?";
	}

	public ExistenceAssertNode clone() {
		return new ExistenceAssertNode(this);
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_EXISTENCE_ASSERT_NODE;
	}

}
