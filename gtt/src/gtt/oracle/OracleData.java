package gtt.oracle;

import java.util.UUID;

public class OracleData {

	private IOracleHandler.Level m_Level = IOracleHandler.Level.WINDOW_LEVEL;

	private IOracleHandler.EventType m_Type = IOracleHandler.EventType.DEFAULT;

	private UUID m_uuid = UUID.randomUUID();

	public UUID getUUID() {
		return m_uuid;
	}

	public String filename() {
		StringBuilder sb = new StringBuilder(m_dir);
		sb.append("/");
		sb.append(m_uuid.toString());
		sb.append(".oracle");
		return sb.toString();
	}

	private String m_dir = ""; // ¤l¥Ø¿ý

	public void setSubDir(String dir) {
		if (dir == null)
			return;
		m_dir = dir;
	}
	
	public String getSubDir() {
		return m_dir;
	}

	public void setUUID(UUID id) {
		m_uuid = id;
	}

	public IOracleHandler.Level getLevel() {
		return m_Level;
	}

	public void setLevel(IOracleHandler.Level lv) {
		m_Level = lv;
	}

	public IOracleHandler.EventType getType() {
		return m_Type;
	}

	public void setType(IOracleHandler.EventType type) {
		m_Type = type;
	}

	@Override
	public OracleData clone() {
		OracleData d = new OracleData();
		d.setLevel(m_Level);
		d.setType(m_Type);
		d.setUUID(m_uuid);
		return d;
	}

	public void setType(int type) {
		switch (type) {
		case 0:
			m_Type = IOracleHandler.EventType.DEFAULT;
			break;
		case 1:
			m_Type = IOracleHandler.EventType.USER_SELECTED;
			break;
		default:
			m_Type = IOracleHandler.EventType.ALL;
		}
	}

	public void setLevel(int level) {
		switch (level) {
		case 0:
			m_Level = IOracleHandler.Level.APPLICATION_LEVEL;
			break;
		case 1:
			m_Level = IOracleHandler.Level.WINDOW_LEVEL;
			break;
		default:
			m_Level = IOracleHandler.Level.COMPONENT_LEVEL;
		}
	}
}
