package gttlipse.tabular.table;

import gttlipse.tabular.actions.Synchronize;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.provider.ArgumentProvider;
import gttlipse.tabular.provider.DropDownListProvider;
import gttlipse.tabular.provider.HeaderProvider;
import gttlipse.tabular.util.TabularUtil;
import gttlipse.tabular.view.SWTResourceManager;

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.KTableNoScrollModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class TableModel extends KTableNoScrollModel implements IEditorInput {

	public static int NON_ERROR = -1;
	
	private String _name = "";
	
	private int _colCount = TableConstants.DEFAULT_COLUMN_COUNT;
	private int _rowCount = TableConstants.DEFAULT_ROW_COUNT;
	
	private int _selectedCol = 0;
	private int _selectedRow = 0;
	
	private int _errorRow = NON_ERROR;
	
    private Hashtable<String, Object> _content = new Hashtable<String, Object>();
    
    private final TextCellRenderer _textRenderer = 
        new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS | TextCellRenderer.STYLE_PUSH);
	
    private Synchronize _syncAction = new Synchronize();

    private ArgumentProvider _argProvider = null;
    private DropDownListProvider _listProvider = null;
    private HeaderProvider _headerProvider = null;
    
    private boolean _isChangedByUser = false;
    private boolean _isMacro = true;
    
	public TableModel(KTable table) {
		super(table);
		
		// before initializing, you probably have to set some member values
        // to make all model getter methods work properly.
        initialize();
        
        // Initialize all providers
        _argProvider = new ArgumentProvider(this);
        _listProvider = new DropDownListProvider(this);
        _headerProvider = new HeaderProvider(this);
	}

	public TableModel() {
		super(null);
	}
	
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return _name;
	}
	
	// The following part belongs to KTableNoScrollModel
    public void setTableListener(KTable table) {
    	table.addCellSelectionListener(new KTableCellSelectionAdapter() {
    		public void cellSelected(int col, int row, int statemask) {
    			_selectedCol = col;
    			_selectedRow = row;
    			
    			// The selection event is triggered which means the editor is prepared
    			// So, it is allowed to auto synchronize
    			_isChangedByUser = true;
            }
    	});
    }
    
    public void setName(String name) {
    	_name = name;
    }
    
    @Override
    public Object doGetContentAt(int col, int row) {
    	Object item = _content.get(produceKey(col, row));
    	
    	if (item != null) {
    		return item;
    	}
    	
    	return "";
    }

    @Override
    public void doSetContentAt(int col, int row, Object value) {
    	// Identify the data is changed or not
    	if (!isChanged(col, row, value)) {
    		return;
    	}
    	
    	// Identify the argument tips are changed or not
    	if (isChangedArgTips(col, row, value)) {
    		return;
    	}
    	
    	// Insert the data into specific cell
    	_content.put(produceKey(col, row), value);
    	
    	// Check the flag to identify the changed request is triggered by user or not
    	if (_isChangedByUser) {
    		// Avoid too many synchronization
    		_isChangedByUser = false;
    		
    		// Do something, When the data is inserted into specific cell 
        	fillArguments(col, row);
        	synchronize();
        	refreshHeader();
    	}
    }
    
    @Override
    public KTableCellEditor doGetCellEditor(int col, int row) {
    	if (col < getFixedColumnCount() || row < getFixedRowCount()) {
    		return null;
    	}
    	
    	return getDropdownList(col, row);
    }

    @Override
    public int doGetRowCount() {
        return _rowCount + getFixedRowCount();
    }

    @Override
    public int doGetColumnCount() {
        return _colCount + getFixedColumnCount();
    }
    
    @Override
    public int getFixedHeaderRowCount() {
        return TableConstants.HEADER_ROW_COUNT;
    }

    @Override
    public int getFixedHeaderColumnCount() {
        return TableConstants.HEADER_COLUMN_COUNT;
    }
    
    @Override
    public int getFixedSelectableRowCount() {
        return 0;
    }

    @Override
    public int getFixedSelectableColumnCount() {
        return 0;
    }

    @Override
    public boolean isColumnResizable(int col) {
        return true;
    }

    @Override
    public boolean isRowResizable(int row) {
        return true;
    }

    public boolean isHeaderRow(int row) {
    	// Identify the row count
    	if (row < _rowCount) {
    		String secondCell = getContentAt(TableConstants.COMPONENT_NAME, row).toString();
    		
    		// Check the second cell is "Component Name"
    		if (secondCell.equals(TableConstants.HEADER_INDICATOR)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean isArgumentRow(int row) {
    	return isHeaderRow(row - 1);
    }
    
    public boolean isErrorRow(int row) {
    	return (row == _errorRow);
    }
    
    private boolean isChanged(int col, int row, Object value) {
    	String original = getContentAt(col, row).toString();
    	
    	if (original.equals(value)) {
    		return false;
    	}
    	
    	return true;
    }
    
    private boolean isChangedArgTips(int col, int row, Object value) {
    	if (isArgumentRow(row)) {
    		if (col == TableConstants.COMPONENT_NAME &&
    			!value.equals(TableTag.ARG_COMPONENT)) {
    			return true;
    		}
    		
    		if (col == TableConstants.ACTION_NAME &&
        		!value.equals(TableTag.ARG_ACTION)) {
        		return true;
        	}
    	}
    	
    	return false;
    }
    
    public void checkTableType() {
    	String firstCell = getContentAt(0, 0).toString();
    	
    	if (firstCell.equals(TableConstants.MACRO_HEADER_INDICATOR)) {
    		_isMacro = true;
    	}
    	else if (firstCell.equals(TableConstants.SCRIPT_HEADER_INDICATOR)) {
    		_isMacro = false;
    	}
    	else {
    		_isMacro = true;
    	}
    }
    
    private String produceKey(int col, int row) {
    	return col + "/" + row;
    }
    
    private void refreshHeader() {
    	String nodeName = TabularUtil.getNameFromTitle(_name);
    	
    	// Refresh the header when the user select a event or assertion function
    	_headerProvider.fillHeader(nodeName, _selectedRow);
    }
    
    private void fillArguments(int col, int row) {
    	// Auto fill with arguments to suggest the user
    	if (!isHeaderRow(row)) {
    		if ((_isMacro) && (col == TableConstants.ACTION_NAME)) {
    			_argProvider.fillArgumentsOfEvent(row);
    		}
    		
    		if ((_isMacro) && (col == TableConstants.COMPONENT_METHOD)) {
    			_argProvider.fillArgumentsOfMethod(row);
    		}
    	}
    }
    
    public void synchronize() {
    	// Synchronize the table and script
		_syncAction.run();
    }
    
    private KTableCellEditor getDropdownList(int col, int row) {
    	// Produce drop-down list when the row is not header or argument
    	if (!isHeaderRow(row) && !isArgumentRow(row)) {
    		if ((_isMacro) && (col == TableConstants.COMPONENT_NAME)) {
	            return _listProvider.getComponentList();
	        }
    		
	        if ((_isMacro) && (col == TableConstants.ACTION_NAME)) {
	            return _listProvider.getEventListOfMacro(row);
	        }
	        
	        if ((_isMacro) && (col == TableConstants.COMPONENT_METHOD)) {
	        	return _listProvider.getMethodListOfMacro(row);
	        }
	        
	        if ((!_isMacro) && (col == TableConstants.ACTION_NAME)) {
	            return _listProvider.getActionList();
	        }
	        
	        if ((!_isMacro) && (col == TableConstants.COMPONENT_TYPE)) {
	        	return _listProvider.getClassList();
	        }
	        
	        if ((!_isMacro) && (col == TableConstants.METHOD_NAME)) {
	        	return _listProvider.getEventOrMethodListOfScript(row);
	        }
    	}
    	
        return new KTableCellEditorText();
    }
    
    public HeaderProvider getHeaderProvider() {
    	return _headerProvider;
    }
    
    public int getRowHeightMinimum() {
        return TableConstants.ROW_HEIGHT;
    }
    
    public void markError(int index) {
    	if (index >= 0) {
    		_errorRow = index + TableConstants.FIRST_EVENT_ROW;
    	}
    	else {
    		_errorRow = NON_ERROR;
    	}
    }
    
    public KTableCellRenderer doGetCellRenderer(int col, int row) {
    	// The default style of the cell render
    	int fontSize = TableConstants.FONT_SIZE;
    	int alignment = SWTX.ALIGN_HORIZONTAL_LEFT | SWTX.ALIGN_VERTICAL_CENTER;
    	Color fgColor = TableConstants.COLOR_BLACK;
    	Color bgColor = TableConstants.COLOR_WHITE;
    	
    	// The attributes of the cell render for the header row
    	if (isHeaderRow(row)) {
    		fontSize = TableConstants.HEADER_FONT_SIZE;
    		alignment = SWTX.ALIGN_VERTICAL_CENTER | SWTX.ALIGN_HORIZONTAL_CENTER;
    		bgColor = TableConstants.COLOR_HEADER_ROW;
    	}
        
    	// The attributes of the cell render for the argument row
    	if (isArgumentRow(row)) {
    		if (col != TableConstants.MACRO_EVENT_NAME) {
    			fgColor = TableConstants.COLOR_ARG_ROW;
    		}
    	}
    	
    	// The attributes of the cell render for the error row
    	if (isErrorRow(row)) {
    		bgColor = TableConstants.COLOR_ERROR_ROW;
    	}
    	
        // Set attribute to the cell render
    	_textRenderer.setAlignment(alignment);
    	_textRenderer.setForeground(fgColor);
        _textRenderer.setBackground(bgColor);
    	_textRenderer.setFont(SWTResourceManager.getFont(TableConstants.FONT, fontSize, SWT.NONE));
    	
    	return _textRenderer;
    }

    public Point doBelongsToCell(int col, int row) {
        // no cell spanning:
        return null;
    }

    public int getInitialColumnWidth(int column) {
        // this is just a weight - and does not necessarily corresponds 
        // to the pixel size of the row!
        return TableConstants.COLUMN_WIDTH;
    }

    public int getInitialRowHeight(int row) {
    	if (isHeaderRow(row)) {
    		return TableConstants.HEADER_ROW_HEIGHT;
    	}
    	return TableConstants.ROW_HEIGHT;
    }

    public void setSelectedItem(Object item) {
    	if (!isHeaderRow(_selectedRow)) {
    		setContentAt(_selectedCol, _selectedRow, item);
    	}
    }
    
    public Object getSelectedItem() {
    	Object item = null;
    	
    	if (!isHeaderRow(_selectedRow)) {
    		item = getContentAt(_selectedCol, _selectedRow);
    	}
    	
    	return item;
    }
    
    public void addColumnItem(int col, Object[] data) {
    	// When the data size is greater than row size
    	while(data.length > _rowCount) {
    		addRow();
    	}
    	
    	// Copy data to table
    	for(int i = 0; i < data.length; i++) {
    		setContentAt(col + getFixedHeaderRowCount(), i, data[i]);
    	}
    	
    	// When the data size is smaller than row size, resupply enough data into table
    	for(int i = data.length; i < _rowCount; i++) {
    		setContentAt(col + getFixedHeaderRowCount(), i, "");
    	}
    }
    
    public void addRowItem(int row, Object[] data) {
    	// When the data size is greater than column size
    	while(data.length > _colCount) {
    		addColumn();
    	}
    	
    	// Copy data to table
    	for(int i = 0; i < data.length; i++) {
    		setContentAt(i, row + getFixedHeaderRowCount(), data[i]);
    	}
    	
    	// When the data size is smaller than column size, resupply enough data into table
    	for(int i = data.length; i < _colCount; i++) {
    		setContentAt(i, row + getFixedHeaderRowCount(), "");
    	}
    }
    
    private void removeColumnItem(int col) {
    	if (!canRemoveColumn(col)) {
    		return;
    	}
    	
    	// Remove the last column
    	for(int i = 0; i < _rowCount; i++) {
    		_content.remove(produceKey(col, i));
    	}
    	
    	// Decrease the column count
    	_colCount -= 1;
    }
    
    private boolean canRemoveColumn(int col) {
    	// Identify the index range
    	if (col <= 1 || col >= _colCount) {
    		return false;
    	}
    	
    	// Check the column contains a content or not
    	for(int i = 0; i < _rowCount; i++) {
    		if (isHeaderRow(i)) {
    			continue;
    		}
    		
    		String content = getContentAt(col, i).toString();
    		if (!content.equals("")) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    private void removeRowItem(int row) {
    	if (!canRemoveRow(row)) {
    		return;
    	}
    	
    	// Remove the last row
    	for(int i = 0; i < _colCount; i++) {
    		_content.remove(produceKey(i, row));
    	}
    	
    	// Decrease the row count
    	_rowCount -= 1;
    }
    
    private boolean canRemoveRow(int row) {
    	// Identify the index range
    	if (row <= 1 || row >= _rowCount) {
    		return false;
    	}
    	
    	if (isHeaderRow(row)) {
    		return false;
    	}
    	
    	// Check the row contains a macro event name or not
    	String meName = getContentAt(0, row).toString();
    	if (!meName.equals("")) {
    		return false;
    	}
    	
    	return true;
    }
    
    public String[] getRowItem(int row) {
    	String[] text = new String[_colCount];
    	
    	for(int i = 0; i < _colCount; i++) {
    		if (getContentAt(i, row) instanceof String) {
    			text[i] = getContentAt(i, row).toString();
    		}
    		else {
    			text[i] = "";
    		}
    	}
    	
    	return text;
    }
    
    public void setColumnCount(int colCount) {
    	_colCount = colCount;
    }
    
    public void setRowCount(int rowCount) {
    	_rowCount = rowCount;
    }
    
    public void addColumn() {
    	// Increase the column count
    	_colCount += 1;
    	
    	// Insert a array of empty string into the last position
    	addColumnItem(_colCount - 1, TabularUtil.createStringArray(_rowCount));
    }
    
    public void addColumn(Object[] data) {
    	// Increase the column count
    	_colCount += 1;
    	
    	// Insert a array of object into the last position
    	addColumnItem(_colCount - 1, data);
    }
    
    public void addRow() {
    	// Increase the row count
    	_rowCount += 1;
    	
    	// Insert a array of empty string into the last position
    	addRowItem(_rowCount - 1, TabularUtil.createStringArray(_colCount));
    }
    
    public void addRow(Object[] data) {
    	// Increase the row count
    	_rowCount += 1;
    	
    	// Insert a array of object into the last position
    	addRowItem(_rowCount - 1, data);
    }
    
    public void removeColumn() {
    	// Remove a column from the last position
    	removeColumnItem(_colCount - 1);
    }
    
    public void removeRow() {
    	// Remove a row from the last position
    	removeRowItem(_rowCount - 1);
    }
    
	public void moveUp() {
		int modelRow = _selectedRow + getFixedRowCount();
		
		if (!canMoveUp(modelRow)) {
			return;
		}
		
		// Special case (contains a name of macro event)
		String meName = getContentAt(0, modelRow - 1).toString();
		if (!meName.equals("")) {
			_content.put(produceKey(0, modelRow - 1), "");
			_content.put(produceKey(0, modelRow), meName);
		}
		
		// Copy the data of the source
		Object temp[] = getRowItem(modelRow);
		
		// Put the target to the position of the source
    	for(int i = 0; i < _colCount; i++) {
    		Object data = getContentAt(i, modelRow - 1);
    		_content.put(produceKey(i, modelRow), data);
    	}
    	
    	// Put the source to the position of the target
    	for(int i = 0; i < _colCount; i++) {
    		_content.put(produceKey(i, modelRow - 1), temp[i]);
    	}
    	
    	synchronize();
    	_selectedRow--;
    }
	
	public void moveDown() {
		int modelRow = _selectedRow + getFixedRowCount();
		
		if (!canMoveDown(modelRow)) {
			return;
		}
		
		// Special case (contains a name of macro event)
		String meName = getContentAt(0, modelRow).toString();
		if (!meName.equals("")) {
			_content.put(produceKey(0, modelRow), "");
			_content.put(produceKey(0, modelRow + 1), meName);
		}
		
		// Copy the data of the source
		Object temp[] = getRowItem(modelRow);
		
		// Put the target to the position of the source
    	for(int i = 0; i < _colCount; i++) {
    		Object data = getContentAt(i, modelRow + 1);
    		_content.put(produceKey(i, modelRow), data);
    	}
    	
    	// Put the source to the position of the target
    	for(int i = 0; i < _colCount; i++) {
    		_content.put(produceKey(i, modelRow + 1), temp[i]);
    	}
    	
    	synchronize();
    	_selectedRow++;
    }
	
	private boolean canMoveUp(int row) {
		if (row <= 0) {
	        return false;
		}
		
		if (isHeaderRow(row)) {
			return false;
		}
		
		if (isHeaderRow(row - 1)) {
			return false;
		}
		
		return true;
	}
	
	private boolean canMoveDown(int row) {
		if (row >= _rowCount - 1) {
	    	return false;
		}
		
		if (isHeaderRow(row)) {
			return false;
		}
		
		if (isHeaderRow(row + 1)) {
			return false;
		}
		
		return true;
	}
}
