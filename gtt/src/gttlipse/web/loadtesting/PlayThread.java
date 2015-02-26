package gttlipse.web.loadtesting;


import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

public class PlayThread extends Thread {
	private ICompilationUnit m_classFile = null;
	private TestMethodNode m_methodNode = null;
	private JUnitLaunchShortcut m_sk = null;

	public PlayThread(TestMethodNode methodNode, int threadNum) {
		m_methodNode = methodNode;
		m_sk = new JUnitLaunchShortcut();
		ResourceFinder finder = new ResourceFinder();

		IFile file = finder.findIFile((TestCaseNode) m_methodNode.getParent());
		m_classFile = (ICompilationUnit) JavaCore.create(file);

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		try {
			for (IType type : m_classFile.getTypes()) {
				final IMethod method = type.getMethod(m_methodNode.getName(), new String[] {});

				if (method != null) {
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							m_sk.launch(new StructuredSelection(method), "run");
						}
					});

				}
			}
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}		
	}

}
