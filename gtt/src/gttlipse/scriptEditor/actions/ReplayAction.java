package gttlipse.scriptEditor.actions;

import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;


public class ReplayAction extends EnhancedAction {
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		JUnitLaunchShortcut sk = getLauncher();
		ResourceFinder finder = new ResourceFinder();
		if(selection.getFirstElement() instanceof TestCaseNode) {
			IFile file = finder.findIFile((TestCaseNode)selection.getFirstElement());
		    sk.launch(new StructuredSelection(file), "run");
		}
		else if(selection.getFirstElement() instanceof TestMethodNode) {
			TestMethodNode methodnode = (TestMethodNode) selection.getFirstElement();
			IFile file = finder.findIFile((TestCaseNode)methodnode.getParent());
			ICompilationUnit  classfile = (ICompilationUnit)JavaCore.create(file);
			try {
				for(IType type:classfile.getTypes()){
					IMethod method = type.getMethod(methodnode.getName(), new String[]{});
					if(method != null)
						sk.launch(new StructuredSelection(method), "run");
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private JUnitLaunchShortcut getLauncher() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_SCRIPT_ACTION_LAUNCHER_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				ILauncher launcher = (ILauncher)o;
				if (launcher instanceof ILauncher &&
					GTTlipse.getPlatformInfo().getTestPlatformID() == launcher.getPlatformID()) {
					return launcher.getLauncher();
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return new JUnitLaunchShortcut();
	}
}
