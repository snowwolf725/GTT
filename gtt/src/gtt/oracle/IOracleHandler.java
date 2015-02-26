package gtt.oracle;


public interface IOracleHandler {

	/**
	 *�b Application level ���ҼҦ��U TestOracle Node
	 * �|���������ε{�����U�ӵ����W������A�Ψ䤸��W���ݩʧ@��������Ҥ���
	 * 
	 * �b Windows level ���ҼҦ��U TestOracle Node �|�����D�����W������A�Ψ䤸��W���ݩʧ@��������Ҥ���
	 * 
	 * Component level ���ҼҦ��U TestOracle Node �|��ݸ� Node �e/��@�� EventNode
	 * �ҵo�e�ƥ�O�w����Ӥ���A�ñN�Ӥ���W���ݩʦ����U�ӧ@��������Ҥ���
	 * 
	 */
	public enum Level {
		APPLICATION_LEVEL, WINDOW_LEVEL, COMPONENT_LEVEL
	}

	public enum EventType {
		DEFAULT, USER_SELECTED, ALL
	}
	
	public boolean handle(OracleData data);
	
	// �i�H�M typesafe enum pattern 06/05
	public final static String TAG_WindowType = "wintype";

	public final static String TAG_WindowTitle = "title";

	public final static String TAG_ComponentType = "type";

	public final static String TAG_Name = "name";

	public final static String TAG_Text = "text";

	public final static String TAG_IdxInWindow = "idx1";

	public final static String TAG_IdxOfSameName = "idx2";
}