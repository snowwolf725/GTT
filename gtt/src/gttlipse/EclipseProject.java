package gttlipse;

import gtt.macro.io.XmlMacroFitSaveVisitor;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.io.XmlTestScriptSaveVisitor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;


public class EclipseProject {
	
	public static String getProjectFile() {
		IProject project = getEclipseProject();
		if (project == null)
			return TestProject.PROJECT_FILENAME;
		return project.getLocation().toString() + "/" + TestProject.PROJECT_FILENAME;
	}
	
	public static String getProjectLocation() {
		IProject project = getEclipseProject();
		if (project == null)
			return "";
		return project.getLocation().toString();
	}
	
	public static IProject getEclipseProject() {
		ResourceFinder finder = new ResourceFinder();
		return finder.findIProject(TestProject.getProject());
	}
	
	public static void saveProject() {
		IProject project = getEclipseProject();
		if (project == null)
			return;
		GTTlipseConfig config = GTTlipseConfig.getInstance();
		Document xmlDocument = new org.apache.xerces.dom.DocumentImpl();
		XmlTestScriptSaveVisitor scriptv = null;
		scriptv = getTestScriptSaverFromPlugin(xmlDocument);
		if(scriptv == null)
			scriptv = new XmlTestScriptSaveVisitor( xmlDocument );
		XmlMacroFitSaveVisitor macrov = null;
		macrov = getMacroScriptSaverFromPlugin( xmlDocument );
		if(macrov == null)
			macrov = new XmlMacroFitSaveVisitor( xmlDocument );
		
		GTTFileSaver saver = new GTTFileSaver();
		saver.doSave(xmlDocument, scriptv, macrov, TestProject.getProject(), TestProject.getMacroDocument().getMacroScript(),
				getProjectFile(), config);

		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			root.refreshLocal(IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private static XmlTestScriptSaveVisitor getTestScriptSaverFromPlugin(final Document xmlDocument) {
		XmlTestScriptSaveVisitor saver = null;
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_TEST_SCRIPT_SAVER_ID);
		if(GTTlipse.getPlatformInfo() == null)
			return null;
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof XmlTestScriptSaveVisitor) {
					if(((XmlTestScriptSaveVisitor) o).specificTestPlatformID() == GTTlipse.getPlatformInfo().getTestPlatformID()) {
						saver = ((XmlTestScriptSaveVisitor) o);
						saver.setDocument(xmlDocument);
					}
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return saver;
	}
	
	private static XmlMacroFitSaveVisitor getMacroScriptSaverFromPlugin(final Document xmlDocument) {
		XmlMacroFitSaveVisitor saver = null;
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_MACRO_SCRIPT_SAVER_ID);
		if(GTTlipse.getPlatformInfo() == null)
			return null;
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof XmlMacroFitSaveVisitor) {
					if(((XmlMacroFitSaveVisitor) o).specificTestPlatformID() == GTTlipse.getPlatformInfo().getTestPlatformID()) {
						saver = ((XmlMacroFitSaveVisitor) o);
						saver.setDocument(xmlDocument);
					}
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return saver;
	}
}
