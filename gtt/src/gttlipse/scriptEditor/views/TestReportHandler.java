/**
 * 
 */
package gttlipse.scriptEditor.views;

import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.TestProject;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.TestScriptNodeFinder;
import gttlipse.web.loadtesting.view.LoadTestingResultItem;
import gttlipse.web.loadtesting.view.LoadTestingResultView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.views
 * 
 */
public class TestReportHandler implements IWindowListener {
	
	private TreeViewer viewer;
	
	private Set<Object> m_failnodes;
	
	TestReportHandler(TreeViewer _viewer,Set<Object> _failnodes){
		viewer = _viewer;
		m_failnodes = _failnodes;
	}
		
	public void windowActivated(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		if(viewer != null){
			ResourceFinder finder = new ResourceFinder();
			ProjectNode projectnode = TestProject.getProject();
			IProject project = finder.findIProject(projectnode);
			if(project == null)return;
			try {
				if(project.isSynchronized(IResource.DEPTH_ONE) == false) {
					project.refreshLocal(IResource.DEPTH_ONE, null);
				}
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			IFile LTfile = project.getFile("tempResult.txt");
			if(LTfile.exists()){
				/* 處理並顯示測試結果 */
				try {
					InputStream in = LTfile.getContents();
					BufferedReader input = new BufferedReader(new InputStreamReader(in));
					String str = "";
					LoadTestingResultView LTview = GTTlipse.getLoadTestingResultView();					
					while((str=input.readLine())!=null) {
						String[] temp = str.split(",");
						LoadTestingResultItem item = new LoadTestingResultItem(temp[0], temp[1], Long.parseLong(temp[2]));
						LTview.addResult(item);
						System.out.println(temp[0]);
						
					}
					
					//	 刪除檔案並更新 local file system
					input.close();
					LTfile.delete(false, null);
					project.refreshLocal(IResource.DEPTH_ONE, null);
					
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}				
			
			IFile file = project.getFile("GUITestResult.txt");
			if(file.exists()){
				m_failnodes.clear();// 清掉舊的記錄
				/* 處理並顯示測試結果 */
				try {
					InputStream in = file.getContents();
					BufferedReader input = new BufferedReader(new InputStreamReader(in));
					String str = "";
					TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
					while((str=input.readLine())!=null) {
						if(str.length()<=str.indexOf(".java")+5)continue;
						String classnodepath = str.substring(0, str.indexOf(".java")+5);
						String abstractnodepath = str.substring(str.indexOf(".java")+5,str.length());
						CompositeNode anode = (CompositeNode)nodefinder.findNode(new Path(classnodepath));
						if(anode != null && anode instanceof TestCaseNode){
							TestCaseNode classnode = (TestCaseNode)anode;
							AbstractNode failnode = nodefinder.findAbstractNode(classnode, new Path(abstractnodepath));
							if(failnode != null){
								m_failnodes.add(failnode);
								TestScriptDocument doc = findDoc(classnode,abstractnodepath);
								m_failnodes.add(doc);
							}
						}
					}
					
					//	 刪除檔案並更新 local file system
					input.close();
					file.delete(false, null);
					project.refreshLocal(IResource.DEPTH_ONE, null);
					
					/* 更新 Viewer */
					viewer.refresh();
					final GTTTestScriptView view = GTTlipse.findScriptView();
					view.getSite().getShell().redraw();
					if(view != null)
						view.setFocus();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
	}
	
	public TestScriptDocument findDoc(TestCaseNode node,String abstractnodepath){
		TestScriptDocument doc = null;
		String [] paths = abstractnodepath.split("/");
		if(paths.length <= 3)
			return doc;
		try{
			String methodname = paths[1];
			String str_docindex = paths[2];
			int docindex = Integer.parseInt(str_docindex);
			TestMethodNode method = (TestMethodNode)node.getChildrenByName(methodname);
			doc = method.getDocAt(docindex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public void windowClosed(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

	public void windowDeactivated(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

	public void windowOpened(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}
}
