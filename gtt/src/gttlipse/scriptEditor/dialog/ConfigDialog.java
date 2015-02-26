/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gttlipse.GTTlipseConfig;
import gttlipse.web.WebTestingConfig;

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
import org.eclipse.swt.widgets.Text;


/**
 * @author SnowWolf
 *
 */
public class ConfigDialog extends TitleAreaDialog {
	
	private Text m_txt_sleeptime;
	
	private GTTlipseConfig m_config;
	
	private Combo m_com_mode;
	
	private Combo m_web_driver;

	public ConfigDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_config = GTTlipseConfig.getInstance();
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		/* sleep time */
		Label lbl_sleeptime = new Label(area, SWT.NULL);
		lbl_sleeptime.setText("Sleep Time: ");
		m_txt_sleeptime = new Text(area, SWT.BORDER);
		m_txt_sleeptime.setLayoutData(data);
		m_txt_sleeptime.setText(m_config.getSleepTime() + "");
		
		/* execution mode */
		Label lbl_mode = new Label(area, SWT.NULL);
		lbl_mode.setText("Mode: ");
		m_com_mode = new Combo(area, SWT.NULL);
		m_com_mode.add("PlayBack");
		m_com_mode.add("Collect Component Info");
		m_com_mode.add("Collect Test Oracle");
		switch(m_config.getMode()) {
		case GTTlipseConfig.REPLAY_MODE:
			m_com_mode.select(0);
			break;
		case GTTlipseConfig.COLLECT_MODE:
			m_com_mode.select(1);
			break;
		case GTTlipseConfig.ORACLE_MODE:
			m_com_mode.select(2);
			break;
		}
		
		if(GTTlipseConfig.testingOnWebPlatform()) {
			Label lbl_driver = new Label(area, SWT.NULL);
			lbl_driver.setText("Web Driver: ");
			m_web_driver = new Combo(area, SWT.NULL);
			m_web_driver.add("HtmlUnit");
			m_web_driver.add("Firefox");
			m_web_driver.add("InternetExplorer");
			m_web_driver.add("Google Chrome");			
			switch(m_config.getWebDriverType()) {
			case WebTestingConfig.WEB_DRIVER_HTMLUNIT:
				m_web_driver.select(0);
				break;
			case WebTestingConfig.WEB_DRIVER_FIREFOX:
				m_web_driver.select(1);
				break;
			case WebTestingConfig.WEB_DRIVER_InternetExplorer:
				m_web_driver.select(2);
				break;
			case WebTestingConfig.WEB_DRIVER_CHROME:
				m_web_driver.select(3);
				break;				
			default:
				m_web_driver.select(1);
			}
		}

		return parent;
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
				/* sleep time */
				String str_sleeptime = m_txt_sleeptime.getText();
				int sleeptime = 1000;
				try{
					sleeptime = Integer.parseInt(str_sleeptime);
				} catch(NumberFormatException ex) {
					
				}
				m_config.setSleepTime(sleeptime);
				/* execution mode */
				m_config.setMode(m_com_mode.getSelectionIndex()+1);
				
				setReturnCode(SWT.Modify);
				//web Driver
				if(GTTlipseConfig.testingOnWebPlatform()) {
					m_config.set_WebDriverType(m_web_driver.getSelectionIndex() + 1);
				}
				
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
}
