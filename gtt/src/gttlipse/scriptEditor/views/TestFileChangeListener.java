/**
 * 
 */
package gttlipse.scriptEditor.views;

import gtt.testscript.TestScriptDocument;
import gttlipse.TestProject;
import gttlipse.resource.ResourceFinder;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.TestScriptNodeFinder;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.util.IResource
 * 
 */
public class TestFileChangeListener implements IResourceChangeListener {
	
	private TreeViewer _viewer;
	
	private Display _display;
	
	private Vector<TestMethodNode> addmethods = new Vector<TestMethodNode>();
	
	/**
	 * 
	 */
	public TestFileChangeListener() {
		// TODO Auto-generated constructor stub
	}

	public TestFileChangeListener(TreeViewer viewer) {
		// TODO Auto-generated constructor stub
		this._viewer = viewer;
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		final ProjectNode proj = TestProject.getProject();
		
		if (event.getType() != IResourceChangeEvent.POST_CHANGE)
			return;

		// get the delta
		IResourceDelta rootDelta = event.getDelta();
		if (rootDelta == null)
			return;
		
		IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
			TestCaseNode classnode = null;

			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource resource = delta.getResource();
//				 only interested in added resources
				if (delta.getKind() == IResourceDelta.ADDED && delta.getMovedFromPath() == null)
					return addResource(resource, proj);
//				 only interested in removed resources
				if (delta.getKind() == IResourceDelta.REMOVED) {
					return delResource(delta,resource, proj);
				}
				// only interested in changed resources
				if (delta.getKind() == IResourceDelta.CHANGED)
					modifyFile(delta, resource);

				return true;
			}

			/**
			 * @param delta
			 * @param resource
			 */
			private boolean modifyFile(IResourceDelta delta, IResource resource) {
				// only interested in content changes
				if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
					return true;
				
				// only interested in files with the "java" extension
				if (resource.getType() == IResource.FILE
						&& "java".equalsIgnoreCase(resource.getFileExtension())) {
					System.out.println("Chang:" + resource.getFullPath());
					
					TestScriptNodeFinder finder = new TestScriptNodeFinder();
					BaseNode node = finder.findNode(resource.getFullPath());
					if( node instanceof TestCaseNode) {
						classnode = (TestCaseNode)node;
						if(classnode == null) return true;
					} else return true;
					// rebuild all method and TestScriptDocument
					final TestCaseNode oldclassnode = classnode.clone();
					classnode.clearChildren();
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					ICompilationUnit unit = JavaCore.createCompilationUnitFrom((IFile)resource);
					parser.setSource(unit);
					ASTNode astroot = parser.createAST(null);
					astroot.accept(new ASTVisitor() {
						private TestMethodNode _methodnode=null;
						private TestMethodNode _oldmethodnode=null;
						
						public boolean visit(MethodDeclaration node) {
							String methodname = node.getName().getFullyQualifiedName();
							_oldmethodnode = (TestMethodNode) oldclassnode.getChildrenByName(methodname);
//							oldclassnode.removeChild(_methodnode);
							if(_oldmethodnode == null) {
								_oldmethodnode = new TestMethodNode(methodname);
								addmethods.add(_oldmethodnode);
							}
							_methodnode = _oldmethodnode.clone();
							_methodnode.clearDoc();
							classnode.addChild(_methodnode);
							return true;
						}
						
						public boolean visit(ExpressionStatement node) {
							String line = node.toString();
							Pattern pattern = Pattern.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
							Matcher m = pattern.matcher(line);
							if (m.find()) {
								// 分析出需要的 TestDocument 資訊
								String str = m.group();
								str=str.replaceAll(TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN, "");
								str=str.replaceAll("\"\\);", "");
								TestScriptDocument doc = _oldmethodnode.getDocByName(str,1);
								if(doc != null) {
									_methodnode.addDocument(doc.clone());
									//_oldmethodnode.removeDoc(doc);
								} else {
									for(int i=0;i<oldclassnode.size();i++){
										TestMethodNode method = (TestMethodNode)oldclassnode.getChildrenAt(i);
										doc = method.getDocByName(str, 1);
										if(doc != null){
											_methodnode.addDocument(doc.clone());
											break;
										}
									}
									if(doc == null)
										_methodnode.addNewDoc(str);
								}
//								_methodnode.addNewNode(str);
							}
							return true;
						}
					});
					fixMethodReName(oldclassnode);
					addmethods.clear();
				}
				updateTreeViewer();
				return true;
			}
			
			private void fixMethodReName(TestCaseNode oldclassnode){
				for(int i=0;i<oldclassnode.getChildren().length;i++){
					boolean match = false;
					TestMethodNode method = (TestMethodNode)oldclassnode.getChildrenAt(i);
					if( method.getDocuments() == null )
						continue;
					Iterator<TestMethodNode> ite = addmethods.iterator();
					while( ite.hasNext() && (match == false) ){
						boolean isequal = true;
						TestMethodNode m = (TestMethodNode)ite.next();
						if( m.getDocuments() == null )
							continue;
						if( m.getDocuments().length != method.getDocuments().length )
							continue;
						for( int k = 0; k< m.getDocuments().length && isequal ; k++) {
							String methodname = method.getDocAt(k).getName();
							if(m.getDocAt(k).getName().equals(methodname) == false ) {
								isequal = false;
							}
						}
						if(isequal){
							m.clearDoc();
							for( int k = 0; k< method.getDocuments().length ; k++) {
								m.addDocument( method.getDocAt(k).clone() );
							}
						}
					}
				}
			}
			
			private void BuildMethodAndDoc(final TestCaseNode classnode) {
//				 rebuild all method and TestScriptDocument
				ResourceFinder finder = new ResourceFinder(); 
				IFile file = finder.findIFile(classnode);
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
				parser.setSource(unit);
				ASTNode astroot = parser.createAST(null);
				astroot.accept(new ASTVisitor() {
					private TestMethodNode _methodnode=null;
					
					public boolean visit(MethodDeclaration node) {
						String methodname = node.getName().getFullyQualifiedName();
						_methodnode = (TestMethodNode) classnode.getChildrenByName(methodname);
						if(_methodnode == null) {
							_methodnode = new TestMethodNode(methodname);
							classnode.addChild(_methodnode);
						}
						_methodnode.clearDoc();
						return true;
					}
					
					public boolean visit(ExpressionStatement node) {
						String line = node.toString();
						Pattern pattern = Pattern.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
						Matcher m = pattern.matcher(line);
						if (m.find()) {
							// 分析出需要的 TestDocument 資訊
							String str = m.group();
							str=str.replaceAll(TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN, "");
							str=str.replaceAll("\"\\);", "");
							_methodnode.addNewDoc(str);
						}
						return true;
					}
				});
			}

			/**
			 * @param resource
			 * @param projectnode
			 */
			private boolean addResource(IResource resource, ProjectNode projectnode) {
				System.out.println("ADD:"+resource.getFullPath().toOSString());
				String paths[] = resource.getFullPath().segments();
				BaseNode srcfolder = null;
				//not match project name
				if(!paths[0].matches(projectnode.getName()))
					return true;
				
				ResourceFinder finder = new ResourceFinder();
				IProject prj = finder.findIProject(projectnode.getName());
				Vector<String> srcfolders = finder.getSourceFolderPaths( prj );
				Iterator<String> ite = srcfolders.iterator();
				boolean insrcfolder = false;
				int srcoffset = 1;
				String resourcepath = resource.getFullPath().toOSString();
				while(ite.hasNext()) {
					String src=(String)ite.next();
					if(resourcepath.equals("/" + projectnode.getName()) ||
					   resourcepath.equals("\\" + projectnode.getName()) ){
						/* JCWang:暫時迴避掉新專案會出現與專案同名的 SourceFolder 
						 * 之後要在改...
						 * */
					}else if(resourcepath.equals(src)) {
						// add source folder
						resourcepath = resourcepath.replaceAll("\\\\","/");
						String srcfoldername = resourcepath.replaceAll("/" + projectnode.getName() + "/", "");
						SourceFolderNode folder = new SourceFolderNode(srcfoldername);
						projectnode.addChild(folder);
						updateTreeViewer();
						return true;
					} else if(resourcepath.startsWith(src)) {
						TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
						srcfolder = nodefinder.findNode(new Path(src));
						insrcfolder = true;
						srcoffset = src.split("\\\\").length;
						if( srcfolder == null ) return true;
					}
				}
				
				if(insrcfolder == false)
					return true;
				
				CompositeNode parent = (CompositeNode)srcfolder;
				BaseNode child = null;
				for(int i=srcoffset-1;i<paths.length-1;i++) {
					child = parent.getChildrenByName(paths[i]);
					if(child == null) {
						child = new PackageNode(paths[i]);
						parent.addChild(child);
					}
					if(child instanceof CompositeNode)
						parent = (CompositeNode) child;
					child = null;
				}
				 
				if(resource.getType() == IResource.FILE) {
					/* 在 SourceFolder 內的不一定是 java 檔案 */
					if(paths[paths.length-1].indexOf(".java") == -1)
						return true;
					String filename = paths[paths.length-1].replaceAll(".java", "");
					child = parent.getChildrenByName(filename);
					if(child == null) {
						child = new TestCaseNode(filename);
						parent.addChild(child);
					}
					BuildMethodAndDoc((TestCaseNode)child);
				} else if(resource.getType() == IResource.FOLDER) {
					child = parent.getChildrenByName(paths[paths.length-1]);
					if(child == null) {
						child = new PackageNode(paths[paths.length-1]);
						parent.addChild(child);
					}
				}
//				System.out.println("ADD:"+resource.getFullPath());
				updateTreeViewer();
				return true;
			}

			/**
			 * 
			 * 方法 updateTreeViewer
			 * 不能直接呼叫 _viewer.refresh 會造成 Tread 存取衝突
			 */
			private void updateTreeViewer() {
				_display.syncExec(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						_viewer.refresh();
					}
				});
			}

			/**
			 * @param resource
			 * @param projectnode
			 */
			private boolean delResource(IResourceDelta delta, IResource resource, ProjectNode projectnode) {
				System.out.println("DEL:"+resource.getFullPath());
				String paths[] = resource.getFullPath().segments();
				BaseNode srcfolder = null;
				//not match project name
				if(!paths[0].matches(proj.getName()))
					return true;
				
				// for source folder
				ResourceFinder finder = new ResourceFinder();
				IProject prj = finder.findIProject(projectnode.getName());
				Vector<String> srcfolders = finder.getSourceFolderPaths( prj );
				Iterator<String> ite = srcfolders.iterator();
				boolean insrcfolder = false;
				int srcoffset = 1;
				String resourcepath = resource.getFullPath().toOSString().replaceAll("\\\\", "\\/");
				while(ite.hasNext()) {
					String src=(String)ite.next();
					src = src.replaceAll("\\\\", "\\/");
					if(resourcepath.equals(src)) {
						// del source folder
						TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
						BaseNode node = nodefinder.findNode(resource.getFullPath());
						if(node == null) return true;
						else {
							CompositeNode parent = node.getParent();
							parent.removeChild(node);
							updateTreeViewer();
						}
						return true;
					} else if(resourcepath.startsWith(src+"/")) {
						insrcfolder = true;
						srcoffset = src.split("\\/").length;
						ResourceFinder resfinder = new ResourceFinder();
						IResource res = resfinder.findJavaSourceFolder(new Path(src));
						if(res == null)
							return true;
						TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
						srcfolder = nodefinder.findNode(new Path(src));
						if( srcfolder == null ) return true;
						break;
					}
				}
				
				if(insrcfolder == false) {
					// 刪除時  Eclipse 有時會先刪除 ClassPath 中的 Source Folder, 所以會找不到
					TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
					BaseNode node = nodefinder.findNode(resource.getFullPath());
					if(node == null) return true;
					else {
						CompositeNode parent = node.getParent();
						parent.removeChild(node);
						updateTreeViewer();
					}
					return true;
				}
				
				CompositeNode parent = (CompositeNode)srcfolder;
				BaseNode child = null;
				for(int i=srcoffset-1;i<paths.length-1;i++) {
					child = parent.getChildrenByName(paths[i]);
					if(child == null)
						return true;
					if(child instanceof CompositeNode)
						parent = (CompositeNode)child;
					child = null;
				}
				
				/* 刪除檔案或目錄 */
				if(parent == null)return true;
				else{
					String childname = paths[paths.length-1];
					if(childname.endsWith(".java")){
						// Class Node
						childname = childname.replaceAll(".java", "");
						child = parent.getChildrenByName(childname);
						if(child != null && delta.getMovedToPath() != null)
							moveResource(resource.getFullPath(),delta.getMovedToPath(),projectnode,(CompositeNode)child);
					}
					else {
						// Package Node
						child = parent.getChildrenByName(childname);
						if(child != null && delta.getMovedToPath() != null)
							moveResource(resource.getFullPath(),delta.getMovedToPath(),projectnode,(CompositeNode)child);
					}
				}
				if(child == null)return true;
				else
					parent.removeChild(child);

				updateTreeViewer();
				return true;
			}
		};

		try {
			rootDelta.accept(visitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void moveResource(IPath moveFromPath, IPath moveToPath, ProjectNode projectnode, CompositeNode node){
		TestScriptNodeFinder nodefinder = new TestScriptNodeFinder();
		BaseNode targetnode = nodefinder.findNode(moveToPath);
		CompositeNode parent = null;
		if(targetnode == null){
			IPath parentpath = moveToPath.removeLastSegments(1);
			parent = (CompositeNode)nodefinder.findNode(parentpath);
		} else {
			parent = targetnode.getParent();
			parent.removeChild(targetnode);
		}
		if(parent == null )
			return;
		
		String childname = moveToPath.segment(moveToPath.segmentCount()-1);
		if(node instanceof TestCaseNode){
			childname = childname.replaceAll(".java", "");
			TestCaseNode classnode = (TestCaseNode)node.clone();
			classnode.setName(childname);
			if(parent instanceof SourceFolderNode){
				SourceFolderNode src = (SourceFolderNode)parent;
				src.addChild(classnode);
			} else if(parent instanceof PackageNode){
				PackageNode packagenode = (PackageNode)parent;
				packagenode.addChild(classnode);
			}
		}else if(node instanceof PackageNode){
			PackageNode packagenode = (PackageNode)node.clone();
			packagenode.setName(childname);
			if(parent instanceof SourceFolderNode){
				SourceFolderNode src = (SourceFolderNode)parent;
				src.addChild(packagenode);
			} else if(parent instanceof PackageNode){
				PackageNode parentpackagenode = (PackageNode)parent;
				parentpackagenode.addChild(packagenode);
			}
		}
	}
	
	public void setDisplay(Display display) {
		_display = display;
	}
}
