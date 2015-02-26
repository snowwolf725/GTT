package gttlipse.vfsmEditor;

public interface VFSMDef {
	final public static String ACTION_AUTOMATIC_LAYOUT = "AUTOMATIC_LAYOUT";
	// VFSM property type definition
	final public static String PROP_NODE = "NODE";
	final public static String PROP_INITIAL = "INITIAL";
	final public static String PROP_STATE = "STATE";
	final public static String PROP_SUPERSTATE = "SUPERSTATE";
	final public static String PROP_FINAL = "FINAL";

	final public static String TYPE_NODE = "NODE";
	final public static String TYPE_INITIAL = "INITIAL";
	final public static String TYPE_STATE = "STATE";
	final public static String TYPE_SUPERSTATE = "SUPERSTATE";
	final public static String TYPE_FINAL = "FINAL";
	final public static String TYPE_ANDSTATE = "ANDSTATE";
	final public static String TYPE_PROXYSTATE = "PROXYSTATE";

	// FOR XML TAGs
	final public static String TAG_VFSM = "VFSM";
	final public static String TAG_NAME = "NAME";
	final public static String TAG_LOCATION_X = "LOCATION_X";
	final public static String TAG_LOCATION_Y = "LOCATION_Y";
	final public static String TAG_DIMENSION_W = "DIMENSION_W";
	final public static String TAG_DIMENSION_H = "DIMENSION_H";
	final public static String TAG_COLLAPSE = "COLLAPSE";
	
	final public static String TAG_TRANSITIONS = "TRANSITIONS";
	final public static String TAG_TRANSITION = "TRANSITION";
	final public static String TAG_CONDITION = "CONDITION";
	final public static String TAG_TARGET = "TARGET";
	final public static String TAG_EVENT = "EVENT";
	final public static String TAG_ACTION = "ACTION";
	final public static String TAG_REFPATH = "REFPATH";
	final public static String TAG_PROXY_REALSTATE = "REALSTATE";
	final public static String TAG_PROXY_EXTRASTATE = "EXTRASTATE";

	final public static String TAG_BENDPOINT = "BEND";
	final public static String TAG_DIMENSION1_W = "DIMENSION1_W";
	final public static String TAG_DIMENSION1_H = "DIMENSION1_H";
	final public static String TAG_DIMENSION2_W = "DIMENSION2_W";
	final public static String TAG_DIMENSION2_H = "DIMENSION2_H";

	// Tree Viewer Label definition
	final public static String FSM_FSM = "FSM";
	final public static String FSM_SUPERSTATE = "SuperState";
	final public static String FSM_MAIN = "Main";
	final public static String FSM_OUTPUT = "OutPut";
	// SuperState COLLAPSED image definition
	final public static String IMG_FOLDER = "IMG_FOLDER";
	final public static String IMG_FILE = "IMG_FILE";
	// VFSM declaration SuperState type definition
	final public static int DECLAR_XOR = 0;
	final public static int DECLAR_AND = 1;
	/* connection router Tag */
	final public static int NULL_ROUTER = 0;
	final public static int BENDPOINT_CONNECTION_ROUTER = 1;
	final public static int MANHATTAN_ROUTER = 2;
	final public static int SHORTEST_PATH_ROUTER = 3;
	final public static int SINGLE_BENDPOINT_ROUTER = 4;

}
