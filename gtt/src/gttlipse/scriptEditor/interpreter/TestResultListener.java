package gttlipse.scriptEditor.interpreter;

import gttlipse.EclipseProject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;


public class TestResultListener extends TestRunListener {

	static final String REPORT_FILENAME = "GTTlipse.rep";

	public void sessionStarted(ITestRunSession session) {
		IProject project = EclipseProject.getEclipseProject();

		if (project == null)
			return;

		try {
			if (!project.isSynchronized(IResource.DEPTH_ONE))
				refreshLocalProject(project);

			IFile report_file = project.getFile(REPORT_FILENAME);

			if (!report_file.exists())
				return; // can't find GTTLipse report file

			report_file.delete(true, null);

			initCoverageReport(report_file.getLocation().toString());

			// refresh project
			refreshLocalProject(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void refreshLocalProject(IProject project) throws CoreException {
		project.refreshLocal(IResource.DEPTH_ONE, null);
	}

	private void initCoverageReport(String file) {
		try {
			FileOutputStream out = new FileOutputStream(file, true);
			writeReportStructure(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeReportStructure(FileOutputStream out) throws IOException {
		out.write("<?xml version='1.0' encoding='BIG5'?>".getBytes());
		out.write("<GTT>".getBytes());
		out.write("<MacroStructure>".getBytes());
		out.write("</MacroStructure>".getBytes());
		out.write("</GTT>".getBytes());
	}

}
