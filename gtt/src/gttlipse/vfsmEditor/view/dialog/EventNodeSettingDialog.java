package gttlipse.vfsmEditor.view.dialog;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gttlipse.scriptEditor.dialog.EditEventNodeDialog;
import gttlipse.widget.table.TableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.TrayDialog;
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
import org.eclipse.swt.widgets.Shell;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

/**
 * @author Jason Wang
 * note : the purpose of this dialog is to setting event node and save it 
 * 		in the event node container (history). when connection is created, 
 * 		show the list to be selection.
 * date : 2008/06/28 
 */
public class EventNodeSettingDialog extends TrayDialog {

	private KTable           m_selPinTable      = null;
	private KTable           m_pinTable         = null;
	private TableModel       m_pinTableModel    = null;
	private Vector<String[]> m_pinPool          = new Vector<String[]>();
	private static List<EventNode>  m_eventNodeList	= new ArrayList<EventNode>();
	private List<EventNode>	transEventNodeList = new ArrayList<EventNode>();
	
	public EventNodeSettingDialog(Shell shell) {
		super(shell);
	}
	
	public EventNodeSettingDialog( Shell shell, String title ) {
		super(shell);
		this.getShell().setText( title );
	}
	
	public EventNodeSettingDialog( Shell shell, List<EventNode> eventNodeList) {
		super(shell);
		transEventNodeList = eventNodeList;
	}
	protected Control createDialogArea( Composite parent ) {
		Composite area			= new Composite( parent, SWT.NULL );
		GridLayout areaLayout	= new GridLayout();
		areaLayout.numColumns = 1;
		area.setLayout( areaLayout );
		
		GridData areaLayoutdata = new GridData();
		areaLayoutdata.horizontalAlignment	= GridData.CENTER;
		areaLayoutdata.verticalAlignment	= GridData.CENTER;
		area.setLayoutData( areaLayoutdata );
		createEventNodeConatinerPane( area );
		createTransitionEventListPane( area );
		return area;
	}
	
	private void createEventNodeConatinerPane( Composite parent ) {
		// create state list group
		Group group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		group.setText( "Event Node Container (History List) " );	
			
		// create state list table
		final KTable     table      = new KTable( group, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWTX.EDIT_ON_KEY );
		final TableModel tableModel = new TableModel( table, 2 );
        table.setModel( tableModel );  
        tableModel.setColumnHeaderText( new String[] { "component name", "event name" } );
        tableModel.setAllColumnWidth( new int[]{ 200, 200 } );		
        tableModel.initialize();
        tableModel.clear();
        for ( int i = 0; i < m_eventNodeList.size(); i++) {
			String componentName	= m_eventNodeList.get(i).getComponent().getName();
			String eventName 		= m_eventNodeList.get(i).getEvent().getName();
			tableModel.addItemText( new String[] { componentName, eventName } );
		}
        table.redraw();
        // add button		
		Button addBtn = new Button( group, SWT.PUSH );
		addBtn.setText( "add" );
		addBtn.addSelectionListener( new SelectionAdapter(){
			// when add pin into the pin list, the pin will be removed from selected list, and the pin pool also.
			public void widgetSelected( SelectionEvent e )
			{
				int selection[] = m_selPinTable.getRowSelection();
				for( int i = 0; i < selection.length; i++) {
					int 	sel			= selection[i];
					int		itemIndex	= selection[i] - tableModel.getFixedRowCount();
					int 	pinIndex	= m_pinTableModel.getItemCount() + 1;
					String 	pinText[]	= tableModel.getItemText( itemIndex );
					tableModel.removeItem( itemIndex );
					table.redraw();
					
					if(tableModel.getItemCount() == 0)
						table.clearSelection();
					else if ( itemIndex < tableModel.getItemCount())
						table.setSelection(0, sel, true);
					else 
						table.setSelection(0, sel - 1, true);
					m_pinTableModel.addItemText( new String[]{ "" + pinIndex, pinText[0] + "." + pinText[1]});
				}
				m_pinTable.redraw();
			}
		});
        
		Button createBtn = new Button( group, SWT.PUSH );
		createBtn.setText( "create" );
		createBtn.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				TrayDialog	editDialog	= null;
				IComponent comp = SwingComponent.createComponent(
						"javax.swing.JButton", "javax.swing.JFrame", "Calculator", "1", "1", 0, 0);
				IEvent event = SwingEvent.create(1, "PUSH_NO_BLOCK");
				NodeFactory nodefactory = new NodeFactory();
				editDialog = new EditEventNodeDialog( null, nodefactory.createEventNode(comp, event), "name");
				if ( editDialog != null)
					editDialog.open();
				if( editDialog.getReturnCode() == SWT.Modify ) {
					m_eventNodeList.add(((EditEventNodeDialog) editDialog).getEventNode());
					tableModel.clear();
					for ( int i = 0; i < m_eventNodeList.size(); i++) {
						String componentName	= m_eventNodeList.get(i).getComponent().getName();
						String eventName 		= m_eventNodeList.get(i).getEvent().getName();
						tableModel.addItemText( new String[] { componentName, eventName } );
					}
					table.redraw();
				}
			}
		});
		
		// my layout
	    group.setBounds( 5, 5 ,465, 250 ); 
	    table.setBounds( 5, 20, 400, 145 ); 
	    addBtn.setBounds( 10, 170, 50, 25 );	
	    createBtn.setBounds( 70, 170, 50, 25 );
        // Assign the reference of the table to member variable which is on this panel in order to handle the data exchanged 
		// from the two table in this application.
	    m_selPinTable      = table;
	}
	
	private void createTransitionEventListPane( Composite parent ) {
		// create transition list group
		Group group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		group.setText(" Transition Event List ");
		
		// create transition list table
		final KTable	table 		= new KTable( group, SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		final TableModel tableModel	= new TableModel( table, 2 );
		table.setModel( tableModel );
		tableModel.setColumnHeaderText( new String[] { "index", "event" } );
		tableModel.setAllColumnWidth( new int[]{ 50, 350 } );
		tableModel.initialize();
		
		for ( int i = 0; i < transEventNodeList.size(); i++) {
			String componentName	= transEventNodeList.get(i).getComponent().getName();
			String eventName 		= transEventNodeList.get(i).getEvent().getName();
			String combinedName		= componentName + "." + eventName;
			tableModel.addItemText( new String[] {  Integer.toString(i+1), combinedName } );
		}
		table.redraw();
		
		// remove button
		Button removeBtn = new Button( group, SWT.PUSH );
		removeBtn.setText("remove");
		removeBtn.addSelectionListener( new SelectionAdapter() {
		
			public void widgetSelected( SelectionEvent e ) {
				int    sel          = table.getRowSelection()[0];
				int    itemIndex    = sel - tableModel.getFixedRowCount(); 
                
				// when the item is removed from pin list, add it to the selected list when the component 
				// name of selection group matchs.
				String removeTemp[] = tableModel.getItemText( itemIndex );
				String compText[] = parsePinCellData( removeTemp[1] );
				m_pinPool.add( compText );
				
                // remove table item and handle
				tableModel.removeItem( itemIndex );
				table.redraw();
				
				if( tableModel.getItemCount() == 0 )
				    table.clearSelection();
				else if( itemIndex < tableModel.getItemCount() )
				    table.setSelection( 0, sel, true );
				else
					table.setSelection( 0, sel - 1, true );	
				
			}
		});
		
		group.setBounds( 5,5,450,250);
		table.setBounds(10,15,400,145);
		removeBtn.setBounds(10,170,50,25);
		
		m_pinTable		= table;
		m_pinTableModel	= tableModel;
	}

	private String[] parsePinCellData( String pinStr )
    {
    	String token[] = new String[2];   	
    	int    index  = pinStr.indexOf( "." );
    	token[0] = pinStr.substring( 0, index );		
    	token[1] = pinStr.substring( index + 1 );
    	return token;
    }
	
	protected void createButtonsForButtonBar( Composite parent ) {
	
		// set layout of buttonbar
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace	= true;
		griddata.horizontalAlignment		= GridData.CENTER;
		griddata.verticalAlignment			= GridData.END;
		parent.setLayoutData( griddata );
		
		// create button and handle event
		Button okBtn	= createButton( parent, SWT.OK, "OK", true);
		okBtn.addSelectionListener( new SelectionAdapter(){
			public void widgetSelected( SelectionEvent e ) {
				insertListContentToModel();
				setReturnCode( SWT.OK );
				close();
			}
		});
		
		Button cancelBtn	= createButton( parent, CANCEL, "Cancel", true);
		cancelBtn.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				setReturnCode( CANCEL );
				close();
			}
		});
	}
	
	// after push "ok", perform
	private void insertListContentToModel() {
		int itemCount = m_pinTableModel.getItemCount();
		transEventNodeList.clear();
		for ( int i = 0; i < itemCount; i++) {
			String eventCell 	= m_pinTableModel.getItemText(1, i);
			String content[]	= parsePinCellData(eventCell);
			for ( int j = 0; j < m_eventNodeList.size(); j++) {
				String cname = m_eventNodeList.get(j).getComponent().getName();
				String ename = m_eventNodeList.get(j).getEvent().getName();
				if ( ( content[0].equals(cname) ) && ( content[1].equals(ename) ) )
					transEventNodeList.add(m_eventNodeList.get(j));
			}
		}
	}
	
	public List<EventNode> getTransEventNodeList() {
		return transEventNodeList;
	}
}
