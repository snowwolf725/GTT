package gttlipse.macro.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class WebPageToComponentDialog  extends TitleAreaDialog{
	private Text webUrl = null;
	WebDriver driver = null;
	String sourcePage = null;
	
	public WebPageToComponentDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	public String getSourcePage() {
		return sourcePage;
	}
	
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText( "WEB Page convert to Components" );
		this.setTitle("Enter URL");
		this.setMessage("What Page you want to convert?");
		
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		final Label lbl_projectname = new Label(area, SWT.NULL);
		lbl_projectname.setText("Init URL:");
		webUrl = new Text(area, SWT.NULL);
		webUrl.setLayoutData(data);
		return area;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_Start = createButton(parent, SWT.Modify, "Start", true);
		btn_Start.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
			//進入gui測試
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				driver = new FirefoxDriver();
				driver.get(webUrl.getText());
			}
		});
		Button btn_Convert = createButton(parent, SWT.Modify, "Convert", true);
		btn_Convert.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			//進入web測試
			public void widgetSelected(SelectionEvent e) {
				sourcePage = driver.getPageSource();
				driver.close();
				setReturnCode(0);
				close();
				// TODO Auto-generated method stub
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
				driver.close();
				close();
			}
		});
	}
	
}
