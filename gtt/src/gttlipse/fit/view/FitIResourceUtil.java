package gttlipse.fit.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public class FitIResourceUtil {
	static public IFolder getTableFolder(IJavaProject project) {
		try {
			Object[] element = project.getNonJavaResources();
			for (int i = 0; i < element.length; i++) {
				IResource file = (IResource) element[i];
				if (file.getType() == IResource.FOLDER
						&& file.getName().compareTo(
								GTTFitViewDefinition.TableFolderName) == 0) {
					IFolder folder = (IFolder) element[i];
					return folder;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public IResource findNonJavaResourceOfProject(IJavaProject project,
			String name) {
		try {
			IResource[] elements = getTableFolder(project).members();
			if (elements.length <= 0)
				return null;
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof IFile
						&& elements[i].toString().contains(name))
					return elements[i];
				if (elements[i] instanceof IFolder) {
					IResource target = null;
					target = findNonJavaResourceOfFolder((IFolder) elements[i],
							name);
					if (target != null)
						return target;
				}

			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public IResource findNonJavaResourceOfFolder(IFolder folder,
			String name) {
		try {
			if (folder.members().length <= 0)
				return null;
			IResource[] elements = folder.members();
			for (int i = 0; i < folder.members().length; i++) {
				if (elements[i] instanceof IFile
						&& elements[i].toString().contains(name))
					return elements[i];
				if (elements[i] instanceof IFolder)
					return findNonJavaResourceOfFolder((IFolder) elements[i],
							name);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
