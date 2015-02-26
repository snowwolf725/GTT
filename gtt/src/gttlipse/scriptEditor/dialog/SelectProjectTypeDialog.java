package gttlipse.scriptEditor.dialog;

import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;
import gttlipse.ITestPlatformInfo;

import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class SelectProjectTypeDialog extends TitleAreaDialog {
	
	private Vector<Integer> m_platformIDs = new Vector<Integer>();
	
	private Vector<ITestPlatformInfo> m_platformsInfos = new Vector<ITestPlatformInfo>();
	
	public SelectProjectTypeDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText( "Select Project Type" );
		this.setTitle("Select Project Type");
		this.setMessage("What type project you want to test?");
		
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;

		return area;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		
		/* execution mode */
		Label lbl_platform = new Label(parent, SWT.NULL);
		lbl_platform.setText("Test Platform: ");
		final Combo com_platform = new Combo(parent, SWT.NULL);
		// add Default Platform
		com_platform.add("JAVA");
		m_platformIDs.add(1);
		com_platform.add("Web");
		m_platformIDs.add(2);
		com_platform.select(0);
		// add test platforms which provides by other plug-in
		createPlatformSelectDialog(com_platform);		
		
		Button btn_ok = createButton(parent, SWT.Modify, "OK", true);
		btn_ok.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			
			}
			public void widgetSelected(SelectionEvent e) {
				int platformID = m_platformIDs.get(com_platform.getSelectionIndex());
				GTTlipseConfig.getInstance().setPlatformOfTesting(platformID);
				for(ITestPlatformInfo info: m_platformsInfos) {
					if(info.getTestPlatformID() == platformID) {
						GTTlipse.setPlatformInfo(info);
					}
				}
				close();
			}
		});
		
		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
		
	}

	private void createPlatformSelectDialog(final Combo com_platform) {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_TESTPLATFORMINFO_ID);
		try {
			for (IConfigurationElement e : config) {
				// Evaluating extension 
				final Object o = e.createExecutableExtension("class");
				if (o instanceof ITestPlatformInfo) {
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							// Handle exception in client
						}

						@Override
						public void run() throws Exception {
							String platformName = ((ITestPlatformInfo) o).getTestPlatformName();
							com_platform.add(platformName);
							m_platformIDs.add(((ITestPlatformInfo) o).getTestPlatformID());
							m_platformsInfos.add((ITestPlatformInfo)o);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
