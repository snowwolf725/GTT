package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

public interface INodeEditingDialogBuilder {
	public abstract int specificTestPlatformID();
	public abstract int specificNodeID();
	public abstract TitleAreaDialog crateDialog(Shell parentShell, AbstractMacroNode node, List<String> generationList);
}
