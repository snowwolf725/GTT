package android4gtt.macro.node.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.macro.dialog.EditDialogFactory;
import gttlipse.macro.dialog.INodeEditingDialogBuilder;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

public class AndroidComponetNodeDialogBuilder implements
		INodeEditingDialogBuilder {

	@Override
	public int specificNodeID() {
		return EditDialogFactory.NID_COMPONENT_NODE;
	}

	@Override
	public TitleAreaDialog crateDialog(Shell parentShell,
			AbstractMacroNode node, List<String> generationList) {
		AndroidComponentEditDialog dialog = new AndroidComponentEditDialog(parentShell, (ComponentNode)node);
		return dialog;
	}

	@Override
	public int specificTestPlatformID() {
		return gttlipse.GTTlipse.getPlatformInfo().getTestPlatformID();
	}

}
