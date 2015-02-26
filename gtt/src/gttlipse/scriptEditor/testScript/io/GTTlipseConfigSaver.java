/**
 * 
 */
package gttlipse.scriptEditor.testScript.io;

import gttlipse.GTTlipseConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.TestScript.io
 * 
 */
public class GTTlipseConfigSaver {
	protected org.w3c.dom.Document m_XmlDoc; /* XML Document */

	protected org.w3c.dom.Element m_ConfigXmlRoot; /* Root of document */
	
	protected org.w3c.dom.Element m_XmlParent;
	
	protected GTTlipseConfig m_gttconfig;

	public GTTlipseConfigSaver(GTTlipseConfig gttconfig) {
		m_gttconfig = gttconfig;
		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		m_ConfigXmlRoot = m_XmlDoc.createElement("GTT");
		m_XmlParent = m_XmlDoc.createElement("Configure");
		m_ConfigXmlRoot.appendChild(m_XmlParent);
	}
	
    // add by David Wu(2007-06-06)
	public GTTlipseConfigSaver( GTTlipseConfig gttconfig, org.w3c.dom.Document doc ) {
		m_gttconfig = gttconfig;
		m_XmlDoc = doc;
		m_XmlParent = m_XmlDoc.createElement("Configure");
	}
	
	// add by David Wu(2007-06-06)
	public org.w3c.dom.Element getConfigRoot()
	{
		return m_XmlParent;
	}

	// change by David Wu(2007-06-06)
	public void buildConfig(){
		/* sleep time */
		org.w3c.dom.Element sleeptime = m_XmlDoc.createElement("SleepTime");
		String time = null;
		time = m_gttconfig.getSleepTime() + "";
		time = "1000";
		sleeptime.setAttribute("time", time);
		m_XmlParent.appendChild(sleeptime);
		/* execute mode */
		org.w3c.dom.Element mode = m_XmlDoc.createElement("ExecuteMode");
		String str_mode = "";
		str_mode = m_gttconfig.getMode() + "";
		mode.setAttribute("mode", str_mode);
		m_XmlParent.appendChild(mode);
		/* type mode */
		org.w3c.dom.Element type = m_XmlDoc.createElement("Type");
		String str_type = "";
		str_type = m_gttconfig.getPlatformOfTesting() + "";
		type.setAttribute("mode", str_type);
		// Web driver
		String str_webDriver = "";
		str_webDriver = m_gttconfig.getWebDriverType() + "";
		type.setAttribute("webDriver", str_webDriver);
		m_XmlParent.appendChild(type);
	}
	
	public boolean saveFile(String filepath) {
		try {
			// test-script
			org.w3c.dom.Node ts_root = getXmlRootNode(filepath , "TestScript");
			if (ts_root != null) {
				m_ConfigXmlRoot.insertBefore(m_XmlDoc.importNode(ts_root, true),
						m_ConfigXmlRoot.getFirstChild());
			}

			// macro-structure
			ts_root = getXmlRootNode(filepath , "MacroStructure");
			if (ts_root != null) {
				m_ConfigXmlRoot.insertBefore(m_XmlDoc.importNode(ts_root, true),
						m_ConfigXmlRoot.getFirstChild());
			}
			
			buildConfig();
			
			/* config */
			m_XmlDoc.appendChild(m_ConfigXmlRoot);

			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat());
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(m_XmlDoc.getDocumentElement());

			PrintWriter writer = new PrintWriter(new FileOutputStream(filepath));
			writer.print(stringOut.toString());
			writer.flush();
			writer.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	private com.sun.org.apache.xml.internal.serialize.OutputFormat createXmlFormat() {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(
				m_XmlDoc);

		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}
	
	public org.w3c.dom.Node getXmlRootNode(String filepath,String tageName) {
		try {
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    doc = builder.parse( new File( filepath ) );
		    
		    org.w3c.dom.Node node0 = (Element)doc.getDocumentElement();
		    org.w3c.dom.Node childnode = node0.getFirstChild();
		    do{
		    	childnode = childnode.getNextSibling();
		    	if (childnode.getNodeName().compareTo(tageName) == 0)
		    		return childnode;
		    }while(childnode != null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
