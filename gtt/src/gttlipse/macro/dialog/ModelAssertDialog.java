package gttlipse.macro.dialog;

import gtt.macro.macroStructure.ModelAssertNode;
import gtt.util.refelection.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ModelAssertDialog extends TitleAreaDialog {

	private Button m_browseFile = null;
	private Button m_refreshList = null;

	private Group m_group = null;
	private Label m_name = null;
	private Text m_classURL = null;
	private Combo m_methodList = null;

	public ModelAssertDialog(Shell parentShell, ModelAssertNode node) {
		super(parentShell);
	}

	private boolean doRefresh() {
		if (m_classURL.getText().equals(""))
			return false;

		Class<?> c = ReflectionUtil.loadClass(m_classURL.getText());
		if (c == null)
			return false;
		loadMethodInComboBox(c);
		return true;
	}

	private void setURL() {
		String filename = acquireClassFile();

		if (filename == null)
			return;

		String clsPath = filename.substring(0, filename.lastIndexOf(".class"));
		Class<?> loadedCls = ReflectionUtil.loadClass(clsPath);
		if (loadedCls == null)
			return;

		loadMethodInComboBox(loadedCls);
		m_classURL.setText(clsPath);
	}

	private String acquireClassFile() {
		String[] extensions = { "*.class" };
		FileCellEditor f = new FileCellEditor(m_group, "class URL", extensions);
		return (String) f.openDialogBox(m_group.getShell());
	}

	private void loadMethodInComboBox(Class<?> c) {
		if (c == null)
			return;
		m_methodList.removeAll();
		addTestMethodToList(c);
		addTestSuiteMethodToList(c);
	}

	private void addTestSuiteMethodToList(Class<?> c) {
		final Class<?>[] returnType = { Test.class, TestSuite.class };
		List<Method> methods = ReflectionUtil.getMethodsReturnWith(c,
				returnType);

		for (int i = 0; i < methods.size(); i++) {
			Method m = methods.get(i);
			m_methodList.add(ReflectionUtil.getStringOfMethod(m));
		}
	}

	private void addTestMethodToList(Class<?> c) {
		List<Method> methods = ReflectionUtil.getMethodsStartsWith(c, "test");
		for (int i = 0; i < methods.size(); i++) {
			Method m = methods.get(i);
			m_methodList.add(ReflectionUtil.getStringOfMethod(m));
		}
	}

	private void initInfoGroup(Composite parent) {
		final GridLayout gridlayout1 = new GridLayout();
		final GridLayout gridlayout2 = new GridLayout();
		final GridLayout gridlayout3 = new GridLayout();
		gridlayout1.numColumns = 1;
		gridlayout2.numColumns = 2;
		gridlayout3.numColumns = 3;

		GridData gd = new GridData();
		GridData gd2 = new GridData();
		gd.widthHint = 450;
		gd2.widthHint = 250;

		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Informaction");
		m_group.setLayout(gridlayout1);

		Composite com2 = new Composite(m_group, SWT.NONE);
		com2.setLayout(gridlayout3);

		Label _name = new Label(com2, SWT.NULL);
		_name.setText("Name:");
		m_name = new Label(com2, SWT.NULL);
		m_name.setLayoutData(gd2);
		// Label _tmp = new Label(com2, SWT.NULL);

		Label _classURL = new Label(com2, SWT.NULL);
		_classURL.setText("Class URL: ");
		m_classURL = new Text(com2, SWT.NULL);
		m_classURL.setLayoutData(gd2);
		m_browseFile = new Button(com2, SWT.PUSH);
		m_browseFile.setText("Browse");

		Label _method = new Label(com2, SWT.NULL);
		_method.setText("Method: ");
		m_methodList = new Combo(com2, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_methodList.setLayoutData(gd2);
		m_refreshList = new Button(com2, SWT.PUSH);
		m_refreshList.setText("Refresh list");

		m_browseFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setURL();
			}
		});

		m_refreshList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doRefresh();
			}
		});
	}

	protected Control createDialogArea(Composite parent) {
		initInfoGroup(parent);
		return parent;
	}

	class FileCellEditor extends DialogCellEditor {
		private String[] extensions;
		private String title;

		public FileCellEditor(Composite parent, String title,
				String[] extensions) {
			super(parent);
			this.extensions = extensions;
			this.title = title;
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			return openDialogBox(cellEditorWindow.getShell());
		}

		private Object openDialogBox(Shell parent) {
			FileDialog dialog = new FileDialog(parent, SWT.OPEN);

			dialog.setFileName((String) getValue());
			dialog.setFilterExtensions(extensions);
			dialog.setText(title);

			String filename = dialog.open();
			setValueValid(filename != null);
			return filename;
		}
	}
}
