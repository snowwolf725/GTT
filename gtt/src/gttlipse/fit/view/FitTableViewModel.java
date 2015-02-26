/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 
 Author: Friederich Kupzog  
 fkmk@kupzog.de
 www.kupzog.de/fkmk
 */
package gttlipse.fit.view;

import gttlipse.fit.table.TableItem;
import gttlipse.macro.dialog.ComponentDialog;
import gttlipse.widget.table.TableModelListener;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellDoubleClickAdapter;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.editors.KTableCellEditorCheckbox2;
import de.kupzog.ktable.editors.KTableCellEditorComboText;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.CheckableCellRenderer;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * @author Friederich Kupzog
 */
public class FitTableViewModel extends KTableDefaultModel //KTableSortedModel
{
	final static public int EDITMODE_NONE     = 0;
	final static public int EDITMODE_EDIT     = 1;
	final static public int EDITMODE_COMBO    = 2;
	final static public int EDITMODE_CHECKED  = 3;

	private int             m_colSize            = 0;
	private int             m_rowSize            = 4;
	private int             m_initRowSize        = 4;
	private int             m_columnHeaderCount  = 0;
	private int             m_rowHeaderCount     = 1;
	private int             m_rowHeightMinimum   = 20;
	private int             m_rowHeight          = 20;
	
	private HashMap<String, Object>         m_content            = new HashMap<String, Object>();
	private List<Integer>   m_columnWidth        = new Vector<Integer>();
	private List<Integer>   m_columnEditable     = new Vector<Integer>();
	private List<Boolean>   m_columnResizable    = new Vector<Boolean>();
	private List<String>    m_headerList         = new Vector<String>();
	private List<String>    m_comboList          = new Vector<String>();
	private List<TableModelListener> m_linstener = new Vector<TableModelListener>();
	
	private final FixedCellRenderer m_fixedRenderer = 
		      new FixedCellRenderer( FixedCellRenderer.INDICATION_CLICKED | 
		    		                 TextCellRenderer.INDICATION_FOCUS_ROW );
    
    private final TextCellRenderer m_textRenderer = 
    	      new TextCellRenderer( TextCellRenderer.INDICATION_FOCUS_ROW );
    
    private final KTableCellRenderer m_CheckableRenderer = 
              new CheckableCellRenderer( CheckableCellRenderer.INDICATION_CLICKED | 
                                         CheckableCellRenderer.INDICATION_FOCUS );

    /**
     * Initialize the base implementation.
     * Needs the table instance since it must compute the available
     * space and adapt appropriately.
     */
    public FitTableViewModel( KTable table, int colSize ) 
    {
//        super( table );
        setTableLinsterer( table );
        // before initializing, you probably have to set some member values to make 
        // all model getter methods work properly.  
        m_colSize = colSize;
        initColumnEditable();
        initColumnWidth();
        initColumnResizable();      
        initialize();
    }
    
    public FitTableViewModel( KTable table, int colSize, int rowSize ) 
    {
    	this( table, colSize ); 
    	m_initRowSize = rowSize;
    	m_rowSize     = rowSize;
    }

    /*
     *  operator function
     */
    public void addModelLinstener( TableModelListener linstener )
    {
    	m_linstener.add( linstener );
    }
    
    public void setTableLinsterer( KTable table )
    { 
//    	final KTable ktable = table; 	 
    	table.addCellSelectionListener( new KTableCellSelectionAdapter()
        {
            public void cellSelected( int col, int row, int statemask ) 
            {
//    			System.out.println(row + ":" + getItemCount());
//                if( row > getItemCount() ) 
//                {
// 	       		    ktable.clearSelection();
// 	       		    ktable.redraw();                
//                }
            }
        } );
    	table.addCellDoubleClickListener( new KTableCellDoubleClickAdapter()
    	{ 
    		public void cellDoubleClicked( int col, int row, int statemask ) 
            {
//    			System.out.println(row + ":" + getItemCount());
//    			if( row > getItemCount() ) 
//    			{
//    				ktable.clearSelection();
//    				ktable.redraw();          	
//    			}
            }
    	});
    }
    
    public void setColumnHeaderText( String text[] )
    {  
    	if( text.length != m_colSize )
    		return;
    	
    	for( int i = 0; i < m_colSize; i++ )
    	{
    		setContentAt( i, 0, text[i] );
    		m_headerList.add( text[i] );
    	}
    }
    
    public void setAllColumnWidth( int width[] )
    {
        for( int i = 0; i < m_colSize; i++ )
           	m_columnWidth.set( i, width[i] );
    }
    
    public void setColumnWidth( int col, int width )
    {
    	if( col < m_colSize )
    	    m_columnWidth.set( col, width );
    }
    
    public void setColumnEditable( int col, int mode )
    {
        if( col < m_colSize )
    	    m_columnEditable.set( col, mode );
    }
    
    public void setColumnResizable( int col, boolean resize )
    {
        m_columnResizable.set( col, resize );
    }
    
    public void setComboList( String list[] )
    {
    	m_comboList = new Vector<String>();
    	
    	for( int i = 0; i < list.length; i++  )
    	{
    	    m_comboList.add( list[i] );
    	}
    }
    
    public int getColumnEditMode( int col )
    {
    	return (int)m_columnEditable.get( col );
    }
    
    public void addItemText( String text[] )
    {  
    	int itemCount = getItemCount();
    	
    	if( text.length != m_colSize )
    		return;
    	
    	if( m_rowSize < itemCount + m_rowHeaderCount )
    		m_rowSize++;
    	    	
    	for( int i = 0; i < m_colSize; i++ )
    		setContentAt( i, itemCount + m_rowHeaderCount , text[i] ); 
    }
    
    public void addItemObject( Object obj[] )
    {  
    	int itemCount = getItemCount();
    	
    	if( obj.length != m_colSize )
    		return;
    	
    	if( m_rowSize < itemCount + m_rowHeaderCount )
    		m_rowSize++;
    	    	
    	for( int i = 0; i < m_colSize; i++ )
    		setContentAt( i, itemCount + m_rowHeaderCount , obj[i] ); 
    }
    
    public void setItemChecked( int col, int row, boolean checked )
    {
    	setContentAt( col, row + m_rowHeaderCount , checked );
    }
    
    public void setItemText( int col, int row, String text )
    {
    	setContentAt( col, row + m_rowHeaderCount , text );
    }
    
    public void setItemText( int row, String text[] )
    {  
    	if( text.length != m_colSize )
    		return;
    	
    	if( m_rowSize < getItemCount() + m_rowHeaderCount )
    		m_rowSize++;
    	    	
    	for( int i = 0; i < m_colSize; i++ )
    		setContentAt( i, row + m_rowHeaderCount , text[i] ); 
    }
    
    public void setItemObject( int row, Object object[] )
    {
    	if( object.length != m_colSize )
    		return;
    	
    	if( m_rowSize < getItemCount() + m_rowHeaderCount )
    		m_rowSize++;
    	    	
    	for( int i = 0; i < m_colSize; i++ )
    		setContentAt( i, row + m_rowHeaderCount , object[i] ); 
    }
    
    public void insertItemText( int row, String text[] )
    {  
    	resetInsertHash( row );
    	//for( int i = 0; i < m_linstener.size(); i++ )
		    //m_linstener.get( i ).itemRemoved( row, text );
    }
    
    public Object getItemObject( int col, int row )
    {  
        return getContentAt( col, row + m_rowHeaderCount );
    }
    
    public String getItemText( int col, int row )
    {  
        if( getContentAt( col, row + m_rowHeaderCount ) instanceof String ) {
        	System.out.println("String");
    	    return (String)getContentAt( col, row + m_rowHeaderCount );
        }
        else if(getContentAt( col, row + m_rowHeaderCount ) instanceof TableItem) {
        	return ((TableItem)getContentAt( col, row + m_rowHeaderCount )).getText();
        }
        else 
        	return "";
    }
    
    public String[] getItemText( int row )
    {  
    	String text[] = new String[m_colSize];
    	for( int i = 0; i < m_colSize; i++ )
    	{
    		if( getContentAt( i, row + m_rowHeaderCount ) instanceof String )
    		    text[i] = (String)getContentAt( i, row + m_rowHeaderCount );
    		else
    			text[i] = "";
    	}
    	return text;
    }
    
    public int getItemCount()
    {
    	return ( m_content.size() / m_colSize );
    }
    
	public void removeItem( int row )
    {		
		if( row >= getItemCount() )
    	    return;
		if( getItemCount() > m_initRowSize )
	        m_rowSize--;
    
		//String temp[] = getItemText( row );
		int modelRow = row + getFixedRowCount();
		resetRemoveHash( modelRow );
    	
    	int modelCount = getItemCount() + getFixedRowCount();  	
    	for( int i = 0; i < m_colSize; i++ )
    	    m_content.remove( hashKey( i, modelCount - 1 ) );
       
    	//for( int i = 0; i < m_linstener.size(); i++ )
		   //m_linstener.get( i ).itemRemoved( row, temp );
    }
	
	public void moveUp( int row )
    {
		//不能控制table info的欄位
		if( row <= GTTFitViewDefinition.TableInformationColumnSize )
	        return;
			
		int    modelRow = row + getFixedRowCount();
		Object temp[]   = new Object[m_colSize];
			
    	for( int i = 0; i < m_colSize; i++ )
    		temp[i] = m_content.get( hashKey( i, modelRow ) );            
    	
    	for( int i = 0; i < m_colSize; i++ )
    	    m_content.put( hashKey( i, modelRow ), m_content.get( hashKey( i, modelRow - 1 ) )  );
    	
    	for( int i = 0; i < m_colSize; i++ )
    	    m_content.put( hashKey( i, modelRow - 1 ), temp[i] );
    }
	
	public void moveDown( int row )
    {
		if( row >= getItemCount() - 1 )
	    	return;
		
		int    modelRow = row + getFixedRowCount();
		Object temp[]   = new Object[m_colSize];
		
    	for( int i = 0; i < m_colSize; i++ )
    		temp[i] = m_content.get( hashKey( i, modelRow ) );            
    	
    	for( int i = 0; i < m_colSize; i++ )
    	    m_content.put( hashKey( i, modelRow ), m_content.get( hashKey( i, modelRow + 1 ) )  );
    	
    	for( int i = 0; i < m_colSize; i++ )
    	    m_content.put( hashKey( i, modelRow + 1 ), temp[i] );
    }

	public void clear()
	{
		m_content.clear();	
		for( int i = 0; i < m_headerList.size(); i++ )
			m_content.put( hashKey( i, 0 ), m_headerList.get( i ) );
	}

    /*
     * private function
     */
	private void resetRemoveHash( int row )
	{
	  	int modelCount = getItemCount() + getFixedRowCount();
	  	
	  	for( int i = row + 1; i < modelCount; i++ )
	   	{
	   		for( int x = 0; x < m_colSize; x++ )
	   		    m_content.put( hashKey( x, i - 1 ) , m_content.get( hashKey( x, i ) ) ); 
	   	}
	}
    
    private void initColumnWidth()
    {
    	for( int i = 0; i < m_colSize; i++ )
           	m_columnWidth.add( 90 );
    }
    
    private void initColumnEditable()
    {
    	for( int i = 0; i < m_colSize; i++ )
            m_columnEditable.add( EDITMODE_NONE );
    }
    
    private void initColumnResizable()
    {
    	for( int i = 0; i < m_colSize; i++ )
    	    m_columnResizable.add( false );
    }
    
    private void resetInsertHash( int row )
    { 	
    }
    
    private String hashKey( int col, int row )
    {
    	return col + "." + row;
    }
    
    /*
     * overridden from superclass
     */
    // Content:
    public Object doGetContentAt( int col, int row ) 
    {            	
    	Object obj = m_content.get( hashKey( col, row ) );
          	
    	if( obj != null )
            return obj;
    	else
    		return "";
    }
    
    // Editor setting
    public KTableCellEditor doGetCellEditor( int col, int row ) 
    {
        if( row > getItemCount() || row < getFixedRowCount() || 
        	m_columnEditable.get( col ) == EDITMODE_NONE )
        	return null;

    	else if( m_columnEditable.get( col ) == EDITMODE_EDIT )
            return new KTableCellEditorText();
    	
    	else if( m_columnEditable.get( col ) == EDITMODE_CHECKED )
    	{
            Rectangle imgBounds = CheckableCellRenderer.IMAGE_CHECKED.getBounds();
            Point sensible = new Point( imgBounds.width, imgBounds.height );
            return new KTableCellEditorCheckbox2( sensible, SWTX.ALIGN_HORIZONTAL_CENTER, SWTX.ALIGN_VERTICAL_CENTER );
    	}
    	else if( m_columnEditable.get( col ) == EDITMODE_COMBO ) 
        {          
    		KTableCellEditorComboText combo = new KTableCellEditorComboText();
    		String list[] = null;
    		
    		if( m_comboList.size() == 0 )
    			list = new String[]{ "" };
    		else
    		{
    			list = new String[m_comboList.size()];
    		    for( int i = 0; i < m_comboList.size(); i++ )
    			    list[i] = m_comboList.get( i );
    		}
    		
    		combo.setItems( list );
            return combo;
        }
        else
            return null;
    }
    
    // if the content is set not on initial, check the data that have changed or not. 
    public void doSetContentAt( int col, int row, Object value ) 
    {
    	Object oldStr = m_content.get( hashKey( col, row ) );
    	Object newStr = value;
    	
    	if( oldStr instanceof TableItem && oldStr != null &&  ( (String)newStr).equals( ((TableItem)oldStr).getText() ) != true ) {
    		for( int i = 0; i < m_linstener.size(); i++ )
    		    m_linstener.get( i ).cellDataChanged( col , row , ((TableItem)newStr).getText() );
    	}
    	if(value instanceof String)
    		m_content.put( hashKey( col, row ), new TableItem((String)value, SWT.COLOR_DARK_YELLOW) );
    	else if(value instanceof Object)
    		m_content.put( hashKey( col, row ), value);
    }

    // Table size:
    public int doGetRowCount()
    {
   	    return m_rowSize + getFixedRowCount() ;
    }

    public int getFixedHeaderRowCount()
    {
        return m_rowHeaderCount;
    }

    public int doGetColumnCount() 
    {
        return m_colSize + getFixedColumnCount();
    }

    public int getFixedHeaderColumnCount() 
    {
        return m_columnHeaderCount;
    }
    
    public int getFixedSelectableRowCount() 
    {
        return 0;
    }

    public int getFixedSelectableColumnCount() 
    {
        return 0;
    }

    public boolean isColumnResizable( int col )
    {
        return true;
    }

    public boolean isRowResizable( int row )
    {
        return false;
    }

    public int getRowHeightMinimum() 
    {
        return m_rowHeightMinimum;
    }
    

    private Object m_renderHook = null;
    public void setBGHook(Object o) {
    	m_renderHook = o;
    }
    
    private void changeBackgroundColor(int col, int row) {
    	if( m_renderHook == null ) return;
    	if( m_renderHook instanceof ComponentDialog) {
    		ComponentDialog d = (ComponentDialog)m_renderHook;
    		m_textRenderer.setBackground( d.getRenderColor( getItemText(col, row-1) ) );
    		return;
    	}
    }

    // Rendering
    public KTableCellRenderer doGetCellRenderer( int col, int row ) 
    {
    	changeBackgroundColor(col, row);
    	if( isFixedCell( col, row ) )
            return m_fixedRenderer;
      
        else if( m_columnEditable.get( col ) == EDITMODE_CHECKED && row <= getItemCount() )
        	return m_CheckableRenderer; 
       
        else {
        	TableItem item = (TableItem)doGetContentAt(col, row);
        	m_textRenderer.setForeground(Display.getCurrent().getSystemColor(item.getColor()));
        	m_textRenderer.setFont(SWTResourceManager.getFont("Times New Roman", 14, SWT.NONE));
        	
            return m_textRenderer;
        }
    }

    public Point doBelongsToCell( int col, int row ) 
    {
        // no cell spanning:
        return null;
    }

    public int getInitialColumnWidth( int column ) 
    {
        // this is just a weight - and does not necessarily corresponds to the pixel size of the row!
  	    return m_columnWidth.get( column );
    }

    public int getInitialRowHeight( int row ) 
    {
    	if( row == 0 ) 
    		return m_rowHeight + 5;
    	else
    	    return m_rowHeight;
    }

    public void addColumnSize() {
    	m_colSize += 1;
    }

    public void addRowSize() {
    	m_rowSize += 1;
    }

    public void subColumnSize() {
    	m_colSize -= 1;
    }

    public void subRowSize() {
    	System.out.println(getItemCount());
    	removeItem(getItemCount() - 1);
    }
}