package gttlipse.vfsmEditor.view.dialog;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DeclarationDialog extends TrayDialog {

	private Text	m_nameText			= null;
	private String	m_declarationName	= "";
	
	// constructor
	public DeclarationDialog(Shell  parentShell, String initName) {
		super( parentShell );
		m_declarationName = initName;
		this.create();
	}
	
	// constructor - initial a title string for dialog
	public DeclarationDialog(Shell  parentShell, String title, String initName) {
		super( parentShell );
		m_declarationName = initName;
		this.create();
		this.getShell().setText( title );
	}
	
	public String getDeclarationName() {
		return m_declarationName;
	}
	
	protected Control createDialogArea( Composite parent ) 
    {
    	final Composite  area       = new Composite( parent, SWT.NULL );
		final GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;
		area.setLayout( areaLayout );
		
		final GridData layoutdata = new GridData();
		layoutdata.horizontalAlignment = GridData.CENTER;
		layoutdata.verticalAlignment   = GridData.CENTER;
		area.setLayoutData( layoutdata );

		final Group group = new Group( area, SWT.SHADOW_ETCHED_IN );
	    group.setText( "Type a Declaration Name" );
	    
	 // create label and textbox
	    final Label nameLabel = new Label( group, SWT.NULL );
		m_nameText  = new Text( group, SWT.BORDER | SWT.SINGLE );
		nameLabel.setText( "Name:" );
		m_nameText.setText( m_declarationName );
			
		// my layout
		group.setBounds( 5, 5, 350, 20 );
		nameLabel.setBounds( 10, 30, 60 ,20 );
		m_nameText.setBounds( 80, 30, 250, 20 );
		
    	return area;
    }
	
	
	//handle event of the button on the buttonbar
    protected void createButtonsForButtonBar( Composite parent ) 
    {
	    // set layout of buttonbar 
	    GridData griddata = new GridData();
	    griddata.grabExcessVerticalSpace = true;
	    griddata.horizontalAlignment = GridData.CENTER;
	    griddata.verticalAlignment = GridData.END;
	    parent.setLayoutData(griddata);
	
	    // create button and handel event
	    Button btnAdd = createButton( parent, SWT.OK, "OK", true );
	    btnAdd.addSelectionListener( new SelectionAdapter() 
	    {
		    public void widgetSelected( SelectionEvent e ) 
		    {
		    	modifyNodeName();	    	
		    }
	    });

	    Button btn_cancel = createButton( parent, CANCEL, "Cancel", true );
	    btn_cancel.addSelectionListener( new SelectionAdapter() 
	    {
		    public void widgetSelected( SelectionEvent e ) 
		    {
			    setReturnCode( CANCEL );
			    close();
		    }
	    });
    }

    private void modifyNodeName()
    {
    	if( m_nameText.getText().matches( "" ) == true )
    	{
    		MessageBox messageBox = new MessageBox( getShell(), SWT.CANCEL | SWT.ICON_ERROR );
    		messageBox.setText( "Waring" );
    		messageBox.setMessage( "Can not input null name" );
    		messageBox.open();
    	}
    	else
    	{
    	    // refactor ?
    		m_declarationName = m_nameText.getText();
    	    setReturnCode( SWT.OK );
    	    close();
    	}	
    }
}
