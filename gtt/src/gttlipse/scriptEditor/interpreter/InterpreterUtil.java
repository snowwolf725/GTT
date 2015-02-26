package gttlipse.scriptEditor.interpreter;

import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;

public class InterpreterUtil {

	public static TestCaseNode findClassNode(Object obj) {
		return findClassNode(packagePath(obj), className(obj));
	}

	private static TestCaseNode findClassNode(String packagepath, String classname) {
		BaseNode[] folders = getScriptFolders();
		String[] paths = packagepath.split("\\."); 
		for (int i = 0; i < folders.length; i++) {
			SourceFolderNode folder = (SourceFolderNode) folders[i];
			if (packagepath.equals("") || packagepath == null) {
				// class in the default package
				if (folder.getChildrenByName(classname) != null) {
					return (TestCaseNode) folder.getChildrenByName(classname);
				}
			} else if (paths.length == 0) {
				// package.classname
				if (folder.getChildrenByName(packagepath) != null) {
					PackageNode packagenode = (PackageNode) folder
							.getChildrenByName(packagepath);
					return (TestCaseNode) packagenode.getChildrenByName(classname);
				}
			} else {
				// package1.package.classname
				if (folder.getChildrenByName(paths[0]) != null) {
					PackageNode packagenode = (PackageNode) folder
							.getChildrenByName(paths[0]);
					for (int j = 1; j < paths.length; j++) {
						packagenode = (PackageNode) packagenode
								.getChildrenByName(paths[j]);
					}
					return (TestCaseNode) packagenode.getChildrenByName(classname);
				}
			}
		}
		return null;
	}

	private static BaseNode[] getScriptFolders() {
		return TestProject.getProject().getChildren();
	}

	private static String packagePath(Object obj) {
		try {
			if (obj.getClass().getPackage() != null)
				return obj.getClass().getPackage().getName();

			String packagename = obj.getClass().getCanonicalName();
			int index = obj.getClass().getCanonicalName().indexOf(".");
			if (index == -1)
				return "";

			return packagename.substring(0, index);
		} catch (NullPointerException exp) {
			return "";
		}
	}

	private static String className(Object obj) {
		return obj.getClass().getSimpleName();
	}

}
