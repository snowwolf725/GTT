package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class VFSMActionFactory {
	public VFSMActionFactory() {

	}

	public Action getAction(IVFSMPresenter presenter, int type) {
		if (type == ActionType.DOUBLECLICK_ACTION)
			return new DoubleClickHandle(presenter);
		if (type == ActionType.NEWCONTEXT_ACTION)
			return new NewContext(presenter);
		if (type == ActionType.COPYNODE_ACTION)
			return new CopyNode(presenter);
		if (type == ActionType.PASTENODE_ACTION)
			return new PasteNode(presenter);
		if (type == ActionType.REMOVENODE_ACTION)
			return new RemoveNode(presenter);
		if (type == ActionType.MOVEUP_ACTION)
			return new MoveUpNode(presenter);
		if (type == ActionType.MOVEDOWN_ACTION)
			return new MoveDownNode(presenter);
		if (type == ActionType.DECLARATION_ACTION)
			return new Declaration(presenter);
		if (type == ActionType.INHERITANCE_ACTION)
			return new Inheritance(presenter);
		if (type == ActionType.TCGENERATION_ACTION)
			return new TestCaseGeneration(presenter);
//		if (type == ActionType.OPENFILE_ACTION)
//			return new OpenFile(presenter);

		return null;
	}

}
