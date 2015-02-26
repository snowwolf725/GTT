package gttlipse;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;

/**
 * @author SnowWolf
 * 
 * This class specifies the informations which are used in the testing.
 *          
 */

public interface ITestPlatformInfo {

	// "Web"
	public String getTestPlatformName();
	
	/* 1 => Java Swing
	 * 2 => Web
	 */
	public int getTestPlatformID();
	
	// "GTTlipse_1.0.0"
	public String getPluginFolder();
	
	// "descriptor/swing.xml"
	public String getDescriptionFilePath();
	
	// {"Jemmy.jar", "abot.jar"}
	public String[] getJarFiles();
	
	public IEventModel createEventModel();
	
	public IComponent createDefaultComponent();
	
	public void copyDescription();
	
	public void saveFile();
}
