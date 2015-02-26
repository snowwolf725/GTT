/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.testscript.LaunchNode;
import gtt.util.refelection.URLClassLoader;
import gttlipse.EclipseProject;
import gttlipse.resource.ResourceFinder;
import gttlipse.widget.table.TableModel;

import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class EditAUTInfoNodeDialog extends TitleAreaDialog {
	private Text m_text_autname;
	private Text m_text_autarg;
	private LaunchNode m_loadautnode;
	private Group m_group = null;
	private KTable m_table = null;
	private TableModel m_tableModel = null;

	/**
	 * @param parentShell
	 */
	public EditAUTInfoNodeDialog(Shell parentShell, LaunchNode node) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_text_autname = null;
		m_loadautnode = node;
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;

		final Composite area2 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 3;
		area2.setLayout(gridlayout2);

		final Composite area3 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout3 = new GridLayout();
		gridlayout3.numColumns = 1;
		area3.setLayout(gridlayout3);

		/* aut location */
		final Label m_lbl_autname = new Label(area2, SWT.NULL);
		m_lbl_autname.setText("AUT file:");
		m_text_autname = new Text(area2, SWT.NULL);
		m_text_autname.setLayoutData(data);
		m_text_autname.setText(m_loadautnode.getLaunchData().getClassName());
		Button button = new Button(area2, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String fullypath = getMainTypeClassFile();
				fullypath = fullypath.replaceAll("\\\\", "\\/");
				String classCanonicalName = resolveClassCanonicalName(fullypath);
				String classPath = resolveClassPathFromFullyPath(fullypath,
						classCanonicalName);
				String classPaths[] = { classPath };
				m_tableModel.clear();
				m_tableModel.addItemText(classPaths);
				m_text_autname.setText(classCanonicalName);
				m_table.redraw();
			}

			private String resolveClassCanonicalName(String fullypath) {
				String classpath = fullypath;
				int lastch = classpath.lastIndexOf('/');
				String classname = classpath.substring(lastch + 1, classpath
						.length() - 6);
				String path = classpath.substring(0, lastch);
				URLClassLoader loader = new URLClassLoader("file:/" + path
						+ "/");
				Class<?> theAutClass = null;
				try {
					theAutClass = loader.loadClass(classname);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				return theAutClass.getCanonicalName();
			}

			private String resolveClassPathFromFullyPath(String fullypath,
					String classCanonicalName) {
				String classpath = fullypath;
				int packagelength = classCanonicalName.split("\\.").length - 1;
				for (; packagelength >= 0; packagelength--) {
					int index = classpath.lastIndexOf('/');
					if (index != -1)
						classpath = classpath.substring(0, index);
				}
				return classpath;
			}
		});

		/* aut arg */
		final Label m_lbl_autarg = new Label(area2, SWT.NULL);
		m_lbl_autarg.setText("AUT Arg:");
		m_text_autarg = new Text(area2, SWT.NULL);
		m_text_autarg.setLayoutData(data);
		m_text_autarg.setText(m_loadautnode.getLaunchData().getArgument());

		/* AUT classpath */
		initClassPathPanel(area3);
		if (m_loadautnode.getLaunchData().getClasspath() != null) {
			String[] classpaths = m_loadautnode.getLaunchData().getClasspath()
					.split(";");
			for (String path : classpaths)
				m_tableModel.addItemText(new String[] { path });
			m_table.redraw();
		}

		return parent;
	}

	private void initClassPathPanel(Composite parent) {
		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("ClassPath List");

		// init table
		m_table = new KTable(m_group, SWT.FULL_SELECTION | SWT.MULTI
				| SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		m_tableModel = new TableModel(m_table, 1);
		m_table.setModel(m_tableModel);
		m_tableModel.setColumnHeaderText(new String[] { "ClassPath" });
		m_tableModel.setAllColumnWidth(new int[] { 300 });
		m_tableModel.setRowHeight(0, 25);
		m_tableModel.setColumnEditable(0, TableModel.EDITMODE_EDIT);
		m_tableModel.setTableLinsterer(m_table);
		m_tableModel.initialize();

		// craete add button for adding item
		final Button addJarBtn = new Button(m_group, SWT.PUSH);
		addJarBtn.setText("Add Jar File");

		addJarBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IProject project = EclipseProject.getEclipseProject();
				if (project == null)
					return;
				ElementTreeSelectionDialog typeseledialog = new ElementTreeSelectionDialog(
						null, new WorkbenchLabelProvider(),
						new WorkbenchContentProvider());
				typeseledialog.setInput(project);
				typeseledialog.open();
				Object[] objs = typeseledialog.getResult();
				if (objs == null)
					return;
				for (Object obj : objs) {
					if (obj instanceof IResource) {
						try {
							IResource resource = (IResource) obj;
							resource.accept(new IResourceVisitor() {

								public boolean visit(IResource resource)
										throws CoreException {
									// TODO Auto-generated method stub
									if (resource.getType() == IResource.FILE
											&& resource.getFileExtension()
													.equals("jar")) {
										IFile file = (IFile) resource;
										String classpath = "";
										try {
											classpath = "file:///"
													+ file.getLocationURI()
															.toURL().toString()
															.replaceFirst(
																	"file:/",
																	"");
										} catch (MalformedURLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										m_tableModel
												.addItemText(new String[] { classpath });
										m_table.redraw();
										return false;
									} else
										return true;
								}

							}, IResource.DEPTH_INFINITE, IResource.FILE);
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				m_table.redraw();
			}
		});

		// craete add button for adding item
		final Button addFolderBtn = new Button(m_group, SWT.PUSH);
		addFolderBtn.setText("Add Folder Folder");

		addFolderBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IProject project = EclipseProject.getEclipseProject();
				if (project == null)
					return;
				ElementTreeSelectionDialog typeseledialog = new ElementTreeSelectionDialog(
						null, new WorkbenchLabelProvider(),
						new WorkbenchContentProvider());
				typeseledialog.setInput(project);
				typeseledialog.open();
				Object[] objs = typeseledialog.getResult();
				if (objs == null)
					return;
				for (Object obj : objs) {
					if (!(obj instanceof IResource))
						return;
					try {
						visit((IResource) obj);
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
				m_table.redraw();
			}

			private void visit(IResource resource) throws CoreException {
				resource.accept(new IResourceVisitor() {
					public boolean visit(IResource resource)
							throws CoreException {
						if (resource.getType() != IResource.FOLDER)
							return true;

						IFolder folder = (IFolder) resource;
						String classpath = "";
						try {
							classpath = "file:///"
									+ folder.getLocationURI().toURL()
											.toString().replaceFirst("file:/",
													"") + "/";
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						m_tableModel.addItemText(new String[] { classpath });
						m_table.redraw();
						return false;
					}

				}, IResource.DEPTH_INFINITE, IResource.FILE);
			}
		});

		// create del button for deling item
		final Button delBtn = new Button(m_group, SWT.PUSH);
		delBtn.setText("Del");
		delBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int[] selrows = m_table.getRowSelection();
				for (int i = 0; i < selrows.length; i++) {
					m_tableModel.removeItem(selrows[i] - 1);
				}
				m_table.redraw();
			}
		});

		// Btn layout
		addJarBtn.setBounds(10, 120, 100, 25);
		addFolderBtn.setBounds(120, 120, 100, 25);
		delBtn.setBounds(230, 120, 100, 25);

		// layout
		m_group.setBounds(5, 5, 450, 265);
		m_table.setBounds(10, 15, 400, 100);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				m_loadautnode.getLaunchData().setClassName(
						m_text_autname.getText());
				m_loadautnode.getLaunchData().setArgument(
						m_text_autarg.getText());
				StringBuilder classpaths = new StringBuilder("");
				for (int i = 0; i < m_tableModel.getItemCount(); i++) {
					classpaths.append(m_tableModel.getItemText(0, i));
					classpaths.append(";");
				}
				m_loadautnode.getLaunchData().setClassPath(
						classpaths.toString());

				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	/**
	 * @return
	 */
	private String getMainTypeClassFile() {
		IProject project = EclipseProject.getEclipseProject();
		if (project == null)
			return null;
		IJavaProject javaproject = JavaCore.create(project);
		IJavaSearchScope searchScope = SearchEngine
				.createJavaSearchScope(new IJavaElement[] { javaproject });
		IWorkbenchWindow[] windows = gttlipse.GTTlipse.getDefault()
				.getWorkbench().getWorkbenchWindows();
		SelectionDialog dialog = JavaUI.createMainTypeDialog(this.getShell(),
				windows[0], searchScope, 0, false);
		dialog.open();

		IType type = (IType) dialog.getResult()[0];

		IPath resultpath = null;
		try {
			resultpath = javaproject.getOutputLocation().removeFirstSegments(1);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		ResourceFinder finder = new ResourceFinder();
		int segcount = finder.getSourceFolderPath(
				type.getCompilationUnit().getPath()).segmentCount();
		IPath relativepath = type.getCompilationUnit().getPath()
				.removeFirstSegments(segcount);
		relativepath = relativepath.removeFileExtension().addFileExtension(
				"class");
		resultpath = resultpath.append(relativepath.toOSString());
		String filename = project.getLocation().toOSString() + "\\"
				+ resultpath.toOSString();

		// Incremental build source code
		try {
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return filename;
	}
}
