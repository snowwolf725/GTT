package gttlipse.vfsmEditor.actions;

public interface ActionType {

	public final static int offset = 500;

	// toolbar action
	public static int NEWCONTEXT_ACTION = offset + 1;
	public static int ADDSTATE_ACTION = offset + 2;
	public static int ADDSUBGRAPH_ACTION = offset + 3;
	public static int TCGENERATION_ACTION = offset + 4;
	public static int OPENFILE_ACTION = offset + 5;
	public static int SAVEFILE_ACTION = offset + 6;
	public static int REMOVENODE_ACTION = offset + 7;
	public static int MOVEUP_ACTION = offset + 8;
	public static int MOVEDOWN_ACTION = offset + 9;
	public static int COPYNODE_ACTION = offset + 10;
	public static int CUTNODE_ACTION = offset + 11;
	public static int PASTENODE_ACTION = offset + 12;
	public static int EXTENDGRAPH_ACTION = offset + 13;
	public static int TRY_ACTION = offset + 14;

	public static int DECLARATION_ACTION = offset + 15;
	public static int INHERITANCE_ACTION = offset + 16;

	// mouse action
	public static int DOUBLECLICK_ACTION = offset - 1;
}
