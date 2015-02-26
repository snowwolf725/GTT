package gttlipse.scriptEditor.dialog;

import gttlipse.EclipseProject;
import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;
import gttlipse.IConfigLoader;
import gttlipse.TestProject;
import gttlipse.fit.view.GTTFitView;
import gttlipse.macro.view.MacroViewPart;
import gttlipse.resource.ResourceFinder;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.def.ProjectPropertyKey;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.io.LoadScript;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SelectProjectDialogModel {
	private Text txtProjectName = null;
	private Shell shell;

	public SelectProjectDialogModel(Shell parentShell, Text proejctName) {
		shell = parentShell;
		txtProjectName = proejctName;
	}

	public void doModifyProject() throws CoreException {
		String[] selectedProjectPath = getSelectedProjectFullPath();
		if (selectedProjectPath.length < 2)
			return;

		IProject project = getSelectedProject(selectedProjectPath);
		if (project == null)
			return;

		updateSourceFolderOfProject(project);
		updateProjectContent(project);
		updateImportLibrary(project);

		project.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private void updateProjectContent(IProject project) {
		String gttfile = project.getLocation().toString() + "/"
				+ TestProject.PROJECT_FILENAME;

		File scriptfile = new File(gttfile);
		if (scriptfile.exists()) {
			// 開啟舊專案
			updateExistsProjectContent(project, gttfile);
		} else {
			// 開啟新專案
			updateNewProjectContent(project);
		}
	}

	private void updateExistsProjectContent(IProject project, String gttfile) {
		TestProject.getProject().setName(project.getName());

		TestProject.loadConfig(gttfile);
		GTTlipse.loadPlatformInfo();
		LoadScript loader = getTestScriptLoaderFromPlugin();
		if(loader != null)
			TestProject.loadTestScript(loader, gttfile);
		else
			TestProject.loadTestScript(gttfile);
		TestProject.updateScriptSync();
		
		loadPluginConfig(gttfile);

		MacroViewPart.getMacroPresenter().openFile(gttfile);

		updateFitProject(project);
	}
	
	private void loadPluginConfig(final String gttfile) {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_CONFIG_LOADER_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IConfigLoader) {
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							// Handle exception in client
						}

						@Override
						public void run() throws Exception {
							((IConfigLoader) o).loadConfig(gttfile);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private static LoadScript getTestScriptLoaderFromPlugin() {
		LoadScript loader = null;
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_TEST_SCRIPT_LOADER_ID);
		if(GTTlipse.getPlatformInfo() == null)
			return null;
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof LoadScript) {
					if(((LoadScript) o).specificTestPlatformID() == GTTlipse.getPlatformInfo().getTestPlatformID()) {
						loader = ((LoadScript) o);
					}
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return loader;
	}

	private void updateNewProjectContent(IProject project) {
		ProjectNode prjnode = TestProject.getProject();

		prjnode.clearChildren();
		prjnode.setName(project.getName());

		TestProject.updateScriptSync();

		MacroViewPart.getMacroPresenter().initMacroDocument();

		chooseTestingPlatform();
		
		// 存檔，如不預先存檔則無存檔關閉後會自動跳至swing模式，未必正確。
		EclipseProject.saveProject();
	}

	private void updateSourceFolderOfProject(IProject project)
			throws JavaModelException {
		// Author : Charles Ku
		// Used to Coverage Analyst
		GTTlipse.findGTTCoverageView().setProject(project);

		String[] projectPath = getSelectedProjectFullPath();
		// Check Source Folder
		if (projectPath.length > 2) {
			// Setup Source Folder to Project Property
			setSourceFolder(project, txtProjectName.getText());
		} else if (projectPath.length == 2) {
			IJavaProject javaproject = JavaCore.create(project);
			IPackageFragmentRoot[] packageroot = javaproject
					.getPackageFragmentRoots();
			for (int i = 0; i < packageroot.length; i++) {
				if (packageroot[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
					String folder = packageroot[i].getPath().toOSString();
					setSourceFolder(project, folder);
					break;
				}
			}
		}
	}

	private String[] getSelectedProjectFullPath() {
		String result = txtProjectName.getText().replaceAll("\\\\", "\\/");
		return result.split("\\/");
	}

	private IProject getSelectedProject(String[] projectPath) {
		String projectName = projectPath[1];

		ResourceFinder finder = new ResourceFinder();
		return finder.findIProject(projectName);
	}

	private void setSourceFolder(IProject project, String folder) {
		QualifiedName key = new QualifiedName("",
				ProjectPropertyKey.SOURCE_FOLDER);
		try {
			project.setPersistentProperty(key, folder);
		} catch (CoreException exp) {
			exp.printStackTrace();
		}
	}

	private void chooseTestingPlatform() {
		// 出現對話框提示選擇java或web測試
		SelectProjectTypeDialog dlg = new SelectProjectTypeDialog(shell);
		dlg.open();
	}
	
	private void updateImportLibrary(IProject project)
			throws JavaModelException {
		TestFileManager manager = new TestFileManager();
		if(GTTlipseConfig.testingOnSwingPlatform() || GTTlipseConfig.testingOnWebPlatform()) {
			manager.addJarFile(project, "abbot.jar");
			manager.addJarFile(project, "bsh.jar");
			manager.addJarFile(project, "runner.jar");
			manager.addJarFile(project, "jakarta-regexp-1.3.jar");
			manager.addJarFile(project, "myjemmy.jar");
			manager.addJarFile(project, "junit.jar");
			manager.addJarFile(project, "jaxp-ri-1.4.2.jar");
		}
		// 判斷是java或web測試匯入不同的jar檔案
		if (GTTlipseConfig.testingOnWebPlatform()) {
			manager.addJarFile(project, "selenium-server-standalone-2.0b3.jar");
		}
		if(GTTlipse.getPlatformInfo()!= null && GTTlipse.getPlatformInfo().getJarFiles() != null) {
			for(String file:GTTlipse.getPlatformInfo().getJarFiles())
				manager.addJarFile(project, file);
		}

		manager.addDescriptorFile(project);
		manager.addMacroEntryPoint(project);
	}

	private void updateFitProject(IProject project) {
		GTTFitView fitView = GTTlipse.getFitView();

		if (fitView != null) {
			fitView.getFitPresenter().Open(
					project.getLocation().toString() + "/");
		}
	}

}
