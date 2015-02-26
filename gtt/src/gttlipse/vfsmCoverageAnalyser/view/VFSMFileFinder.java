package gttlipse.vfsmCoverageAnalyser.view;

import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class VFSMFileFinder {
	private IProject _project;
	
	public VFSMFileFinder(IProject project) {
		_project = project;
	}
	
	public LinkedList<IFile> getVFSMFiles() {
		final LinkedList<IFile> vfsmFiles = new LinkedList<IFile>();
		
		try {
			_project.accept(new IResourceVisitor(){

				@Override
				public boolean visit(IResource arg0) throws CoreException {
					// TODO Auto-generated method stub
					if( arg0.getType() == IResource.FILE &&
						arg0.getName().endsWith("vfsm")){
						IFile file = (IFile)arg0;
						vfsmFiles.add(file);
					}
					return true;
				}
				
			}, IResource.DEPTH_ONE,  IResource.NONE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vfsmFiles;
	}
}
