/**
 * 
 */
package gttlipse.scriptEditor.testScript.io;

import gttlipse.GTTlipseConfig;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.TestScript.io
 * 
 */
public class LoadConfig {
	
	private GTTlipseConfig m_gttconfig;

	public void readConfig(String filename) {
		try {
			// init GTTConfig
			m_gttconfig = GTTlipseConfig.getInstance();
			// init XML
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    doc = builder.parse( new File( filename ) );
			
			org.w3c.dom.Node node0 = (Element)doc.getDocumentElement();
			if( node0 == null ){
				System.out.println("File:" + filename + " isn't correct.");
				return;
			}
			/* Load Configure */
			org.w3c.dom.Node node1 = ( (Element)node0 ).getElementsByTagName("Configure").item(0);
			if( node1 == null ){
			    System.out.println("File:" + filename + " isn't correct.");
				return;
			}
			processSleepTimeNode(node1);
			processExecuteModeNode(node1);
			processTypeNode(node1);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param node1
	 * @throws NumberFormatException
	 */
	private void processSleepTimeNode(org.w3c.dom.Node node1) throws NumberFormatException {
		org.w3c.dom.Node sleeptimenode = ( (Element)node1 ).getElementsByTagName("SleepTime").item(0);
		if(sleeptimenode != null){
			Element e = (org.w3c.dom.Element)sleeptimenode;
			String str_sleeptime = e.getAttribute("time");
			m_gttconfig.setSleepTime(Integer.parseInt(str_sleeptime));
		}
	}
	
	private void processExecuteModeNode(org.w3c.dom.Node node1) throws NumberFormatException {
		org.w3c.dom.Node modenode = ( (Element)node1 ).getElementsByTagName("ExecuteMode").item(0);
		if(modenode != null){
			Element e = (org.w3c.dom.Element)modenode;
			String str_mode = e.getAttribute("mode");
			m_gttconfig.setMode(Integer.parseInt(str_mode));
		}
	}
	
	private void processTypeNode(org.w3c.dom.Node node1) throws NumberFormatException {
		org.w3c.dom.Node modenode = ( (Element)node1 ).getElementsByTagName("Type").item(0);
		if(modenode != null){
			Element e = (org.w3c.dom.Element)modenode;
			String str_mode = e.getAttribute("mode");
			//web driver
			if(e.hasAttribute("webDriver")) {
				String str_webDriver = "";
				str_webDriver =  e.getAttribute("webDriver");
				m_gttconfig.set_WebDriverType(Integer.parseInt(str_webDriver));
			}
			
			m_gttconfig.setPlatformOfTesting(Integer.parseInt(str_mode));
		}
	}
	
	public GTTlipseConfig getConfig(){
		return m_gttconfig;
	}
}
