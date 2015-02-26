package gttlipse.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class GTTlipsePropertyPage extends PropertyPage {

	private static final String SLEEP_TIME_TITLE = "&Sleep Time(ms):";
	public static final String SLEEP_TIME_PROPERTY = "SLEEP_TIME";
	private static final String DEFAULT_SLEEP_TIME = "1000";
	
	private IProject m_project;

	private static final int TEXT_FIELD_WIDTH = 50;

	private Text m_text_sleeptime;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public GTTlipsePropertyPage() {
		super();
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addSleepTimeSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for SleepTime field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText(SLEEP_TIME_TITLE);

		// SleepTime text field
		m_text_sleeptime = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		m_text_sleeptime.setLayoutData(gd);

		// Populate SleepTime text field
		try {
			String sleeptime = m_project.getPersistentProperty(	new QualifiedName("", SLEEP_TIME_PROPERTY));
			m_text_sleeptime.setText((sleeptime != null) ? sleeptime : DEFAULT_SLEEP_TIME);
		} catch (CoreException e) {
			m_text_sleeptime.setText(DEFAULT_SLEEP_TIME);
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		
		setElement();

		addSeparator(composite);
		addSleepTimeSection(composite);
		return composite;
	}
	
	private void setElement(){
		if( getElement() instanceof IProject)
			m_project = (IProject)getElement();
		else if(getElement() instanceof IJavaProject)
			m_project = ((IJavaProject)getElement()).getProject();
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		// Populate the owner text field with the default value
		m_text_sleeptime.setText(DEFAULT_SLEEP_TIME);
	}
	
	public boolean performOk() {
		// store the value in the owner text field
		try {
			m_project.setPersistentProperty(
				new QualifiedName("", SLEEP_TIME_PROPERTY),
				m_text_sleeptime.getText());
		} catch (CoreException e) {
			return false;
		}
		return true;
	}

}