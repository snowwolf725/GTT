/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class SelectProjectDialog extends TitleAreaDialog {
	private Text txtProjectName = null;

	public SelectProjectDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select Java Project");
		setTitle("Create association");
		setMessage("Create association between GTTlipse and Java Project.");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;

		final Composite area = new Composite(parent, SWT.NULL);
		area.setLayout(gridlayout);

		Label lbl_projectname = new Label(area, SWT.NULL);
		lbl_projectname.setText("JAVA Project:");

		txtProjectName = new Text(area, SWT.NULL);

		GridData data = new GridData();
		data.widthHint = 300;
		txtProjectName.setLayoutData(data);

		Button button = new Button(area, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		return area;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createDialogGridData(parent);
		createModifyButton(parent);
		createCancelButton(parent);
	}

	private void createDialogGridData(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
	}

	private void createModifyButton(Composite parent) {
		Button modifyBtn = createButton(parent, SWT.Modify, "Modify", true);
		modifyBtn.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				try {
					SelectProjectDialogModel dlgModel = new SelectProjectDialogModel(
							getShell(), txtProjectName);
					dlgModel.doModifyProject();
				} catch (JavaModelException e1) {
					e1.printStackTrace();
				} catch (CoreException e2) {
					e2.printStackTrace();
				}

				setReturnCode(SWT.Modify);
				close();
			}
		});
	}

	private void createCancelButton(Composite parent) {
		Button cancelBtn = createButton(parent, CANCEL, "Cancel", true);
		cancelBtn.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	private void handleBrowse() {
		ElementTreeSelectionDialog dlg = new ElementTreeSelectionDialog(
				getShell(), new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		dlg.setComparator(new ViewerComparator());
		dlg.setTitle("Select Java Source Container");
		dlg.setMessage("Select one of java source containers from workspace.");
		dlg.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IJavaProject)
					return true;
				if (!(element instanceof IPackageFragmentRoot))
					return false;
				try {
					return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
				} catch (JavaModelException e) {
					return false;
				}
			}
		});
		dlg.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
		if (dlg.open() != ElementTreeSelectionDialog.OK)
			return;

		Object[] result = dlg.getResult();
		if (result.length == 1) {
			txtProjectName.setText(((IJavaElement) result[0]).getResource()
					.getFullPath().toOSString());
		}
	}

}
