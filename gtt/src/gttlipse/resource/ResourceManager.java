/**
 * 
 */
package gttlipse.resource;

import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * @author 王榮麒
 * 
 *         created first in project GTTlipse.util.IResource
 * 
 */
public class ResourceManager {
	ResourceFinder finder = null;

	public ResourceManager() {
		finder = new ResourceFinder();
	}

	public boolean AddJavaPackage(PackageNode javapackage) {
		if (javapackage == null)
			return false;
		// 上一層是 Package 的目錄
		IFolder folder = (IFolder) finder
				.findJavaPackage((PackageNode) javapackage.getParent());
		if (folder == null)
			return false;

		// IProject project = finder.findIProject(javapackage);
		// project.getFolder()
		// folder.create(force, local, monitor);
		return true;
	}

	public boolean AddJavaPackageUnderSourceFolder(String srcfolder,
			String packagename) {
		IProject project = finder.findIProject(srcfolder);
		String foldername = srcfolder + "/" + packagename;
		return tryingToCreateFolder(project.getFolder(foldername));
	}

	public boolean AddJavaPackageUnderPackage(PackageNode pkg, String pkgname) {
		List<String> list = pkg.getFullPath();
		for (int i = 0; i < list.size(); i++)
			pkgname = list.get(i) + "/" + pkgname;

		IProject project = finder.findIProject(pkg);
		return tryingToCreateFolder(project.getFolder(pkgname));
	}

	private boolean tryingToCreateFolder(IFolder folder) {
		// IFolder folder = project.getFolder(foldername);

		try {
			if (folder.exists())
				return false;

			folder.create(false, true, null);
			folder.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean DelJavaPackage(PackageNode javapackage) {
		IProject project = finder.findIProject(javapackage);
		List<String> list = javapackage.getFullPath();
		String packagename = "";

		for (int i = 0; i < list.size(); i++)
			packagename = list.get(i) + "/" + packagename;
		IFolder folder = project.getFolder(packagename);
		try {
			if (!folder.exists())
				return false;
			folder.delete(true, null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean DelJavaClass(TestCaseNode classnode) {
		IProject project = finder.findIProject(classnode);
		IFile file = finder.findIFile(project, classnode.getName() + ".java",
				classnode);

		try {
			if (file == null)
				return false;
			if (!file.exists())
				return false;

			file.delete(true, null);
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean AddJavaClass(BaseNode anode, String classname) {
		IProject project = finder.findIProject(anode);

		if (project == null)
			return false;

		String packagepath = getPackagePath(anode);

		try {
			IFile file = null;
			if (packagepath.equals("/" + project.getName() + "/"))
				file = project.getFile(new Path(classname + ".java"));
			else
				file = project.getFile(new Path(packagepath + classname
						+ ".java"));

			if (file.exists())
				return false;

			String classpackage = "";
			while (anode instanceof PackageNode) {
				if (classpackage.equals(""))
					classpackage = anode.getName();
				else
					classpackage = anode.getName() + "." + classpackage;
				anode = anode.getParent();
			}

			TestFileManager manager = new TestFileManager();
			InputStream in = new ByteArrayInputStream(manager.getNewTestFile(
					classpackage, classname).getBytes());
			file.create(in, true, null);
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getPackagePath(BaseNode anode) {
		List<String> list = anode.getFullPath();
		String packagepath = "";

		for (int i = 0; i < list.size(); i++)
			packagepath = list.get(i) + "/" + packagepath;
		return packagepath;
	}

	public void addJavaSourceFolder(IProject project, String foldername) {

		IJavaProject javaproject = JavaCore.create(project);
		IClasspathEntry srcEntry = JavaCore.newSourceEntry(new Path("/"
				+ project.getName() + "/" + foldername));

		try {
			IClasspathEntry rawClsPath[] = javaproject.getRawClasspath(); // 原本的
			IClasspathEntry tmpclasspath[] = new IClasspathEntry[rawClsPath.length + 1]; // 暫存的

			tryingToCreateFolder(project.getFolder(foldername));

			// 設定第一個 class path 為新加入的 srcfolder
			tmpclasspath[0] = srcEntry;

			// 整理 classpath 並略過 null 的 classpath , 及統計共有多少個 classpath
			boolean isexist = false;
			String projName = "\\" + project.getName();
			int index = 1;
			for (int i = 0; i < rawClsPath.length; i++) {
				if (rawClsPath[i] == null)
					continue;
				if (rawClsPath[i].getPath().toOSString().equals(
						projName + "\\" + foldername))
					isexist = true;
				if (!rawClsPath[i].getPath().toOSString().equals(projName)) {
					// 去掉專案的根目錄,遇到專案的根目錄則不加
					tmpclasspath[index] = rawClsPath[i];
					index++;
				}
			}
			// 複製給新的 classpath
			IClasspathEntry newclasspath[] = new IClasspathEntry[index];
			for (int i = 0, j = 0; i < tmpclasspath.length; i++) {
				if (tmpclasspath[i] != null) {
					newclasspath[j] = tmpclasspath[i];
					j++;
				}
			}
			if (isexist == false) {
				// set output folder
				IPath outfolder = javaproject.getOutputLocation();
				String oldoutputfolder = outfolder.toOSString().replaceAll(
						"\\\\", "");
				oldoutputfolder = oldoutputfolder.replaceAll("\\/", "");
				if (oldoutputfolder.equals(projName))
					javaproject.setOutputLocation(new Path("/" + projName
							+ "/bin"), null);
				// set new class path
				javaproject.setRawClasspath(newclasspath, null);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	// private void createNonExistedFolder(IProject project, String foldername)
	// throws CoreException {
	// IFolder folder = project.getFolder(foldername);
	// if (folder.exists())
	// return;
	//		
	// /* 若 SourceFolder 不存在則先建立 */
	// folder.create(false, true, null);
	// project.refreshLocal(IResource.DEPTH_INFINITE, null);
	// }

	public void DelJavaSourceFolder(SourceFolderNode folder) {
		ResourceFinder finder = new ResourceFinder();
		IProject project = finder.findIProject(folder);

		DelJavaSourceFolder(project, folder.getName());
	}

	public void DelJavaSourceFolder(IProject project, String foldername) {
		try {
			String projectname = project.getName();
			IJavaProject javaproject = JavaCore.create(project);
			IClasspathEntry classpath[] = javaproject.getRawClasspath();
			IClasspathEntry newclasspath[] = new IClasspathEntry[classpath.length - 1];

			IFolder folder = project.getFolder(foldername);
			if (!folder.exists())
				return;

			folder.delete(true, null);

			boolean isexist = false;
			for (int i = 0, index = 0; i < classpath.length; i++) {
				foldername = foldername.replaceAll("\\/", "\\\\");
				if (classpath[i].getPath().toOSString().equals(
						"\\" + projectname + "\\" + foldername)) {
					isexist = true;
				} else {
					newclasspath[index] = classpath[i];
					index++;
				}
			}
			if (isexist == true) {
				// set new class path
				javaproject.setRawClasspath(newclasspath, null);
			}
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
