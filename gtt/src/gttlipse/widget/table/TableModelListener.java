package gttlipse.widget.table;

public interface TableModelListener 
{
    public void cellDataChanged( int col, int row, String data );
    public void itemInserted( int row, String text[] );
    public void itemRemoved( int row, String text[] );
}
