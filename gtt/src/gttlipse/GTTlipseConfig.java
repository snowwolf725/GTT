/**
 * 
 */
package gttlipse;

import gttlipse.web.WebTestingConfig;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript 這個 Class
 *         負責存放 GTTlipse 所會用到的全域設定值
 * 
 */
public class GTTlipseConfig {

	private static GTTlipseConfig m_singleton = null;

	public static final int REPLAY_MODE = 1;

	public static final int COLLECT_MODE = 2;

	public static final int ORACLE_MODE = 3;

	public static final int SWING_PLATFORM = 1;

	public static final int WEB_PLATFORM = 2;

	private int m_sleepTime = 1000;

	private int m_executeMode = REPLAY_MODE;

	private int platformOfTesting = SWING_PLATFORM;

	private int m_WebDriverType = WebTestingConfig.WEB_DRIVER_FIREFOX;

	private GTTlipseConfig() {
	}

	public int getWebDriverType() {
		return m_WebDriverType;
	}

	public void set_WebDriverType(int mWebDriverType) {
		m_WebDriverType = mWebDriverType;
	}

	public int getPlatformOfTesting() {
		return platformOfTesting;
	}

	public void setPlatformOfTesting(int mode) {
		platformOfTesting = mode;
	}

	public int getSleepTime() {
		return m_sleepTime;
	}

	public void setSleepTime(int time) {
		m_sleepTime = time;
	}

	public static void setToTestingOnWebPlatform() {
		getInstance().setPlatformOfTesting(WEB_PLATFORM);
	}

	public static void setToTestingOnSwingPlatform() {
		getInstance().setPlatformOfTesting(SWING_PLATFORM);
	}
		
	public static boolean testingOnWebPlatform() {
			return getInstance().getPlatformOfTesting() == WEB_PLATFORM;
	}

	public static boolean testingOnSwingPlatform() {
		return getInstance().getPlatformOfTesting() == SWING_PLATFORM;
	}

	public static GTTlipseConfig getInstance() {
		if (m_singleton == null)
			m_singleton = new GTTlipseConfig();
		return m_singleton;
	}

	public int getMode() {
		return m_executeMode;
	}

	// 設定測試時的mode
	public void setMode(int _mode) {
		this.m_executeMode = _mode;
	}
}
