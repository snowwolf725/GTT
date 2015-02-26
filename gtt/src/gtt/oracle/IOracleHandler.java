package gtt.oracle;


public interface IOracleHandler {

	/**
	 *在 Application level 驗證模式下 TestOracle Node
	 * 會收集該應用程式中各個視窗上的元件，及其元件上的屬性作為日後驗證之用
	 * 
	 * 在 Windows level 驗證模式下 TestOracle Node 會收集主視窗上的元件，及其元件上的屬性作為日後驗證之用
	 * 
	 * Component level 驗證模式下 TestOracle Node 會察看該 Node 前/後一個 EventNode
	 * 所發送事件是針對哪個元件，並將該元件上的屬性收集下來作為日後驗證之用
	 * 
	 */
	public enum Level {
		APPLICATION_LEVEL, WINDOW_LEVEL, COMPONENT_LEVEL
	}

	public enum EventType {
		DEFAULT, USER_SELECTED, ALL
	}
	
	public boolean handle(OracleData data);
	
	// 可以套 typesafe enum pattern 06/05
	public final static String TAG_WindowType = "wintype";

	public final static String TAG_WindowTitle = "title";

	public final static String TAG_ComponentType = "type";

	public final static String TAG_Name = "name";

	public final static String TAG_Text = "text";

	public final static String TAG_IdxInWindow = "idx1";

	public final static String TAG_IdxOfSameName = "idx2";
}