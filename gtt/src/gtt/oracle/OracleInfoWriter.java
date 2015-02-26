/**
 * 
 */
package gtt.oracle;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.testscript.ViewAssertNode;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript.io
 * 
 */
public class OracleInfoWriter {
	protected org.w3c.dom.Document m_XmlDocument; /* XML Document */

	protected org.w3c.dom.Element m_XmlRoot; /* Root of document */

	public OracleInfoWriter(List<ViewAssertNode> va) {
		m_XmlDocument = new org.apache.xerces.dom.DocumentImpl();
		m_XmlRoot = m_XmlDocument.createElement("TestOracle");
		Iterator<ViewAssertNode> ite = va.iterator();
		while (ite.hasNext()) {
			saveNode(m_XmlRoot, ite.next());
		}
	}

	private void saveNode(org.w3c.dom.Element parent, ViewAssertNode node) {
		org.w3c.dom.Element ele = m_XmlDocument.createElement("ViewAssert");
		// for swing part
		processSwingComponentPart(ele, node.getComponent());
		// for assertion part
		processAssertionPart(ele, node.getAssertion());

		parent.appendChild(ele);
	}

	// swing component information
	private void processSwingComponentPart(org.w3c.dom.Element parent,
			IComponent component) {
		org.w3c.dom.Element ele = m_XmlDocument.createElement("Comp");
		ele.setAttribute(IOracleHandler.TAG_WindowType, component.getWinType());
		ele.setAttribute(IOracleHandler.TAG_WindowTitle, component.getTitle());
		ele.setAttribute(IOracleHandler.TAG_ComponentType, component.getType());
		ele.setAttribute(IOracleHandler.TAG_Name, component.getName());
		ele.setAttribute(IOracleHandler.TAG_Text, component.getText());
		ele.setAttribute(IOracleHandler.TAG_IdxInWindow, Integer
				.toString(component.getIndex()));
		ele.setAttribute(IOracleHandler.TAG_IdxOfSameName, Integer
				.toString(component.getIndexOfSameName()));

		parent.appendChild(ele);
	}

	// assertion information
	private void processAssertionPart(org.w3c.dom.Element parent, Assertion v) {
		org.w3c.dom.Element ele = m_XmlDocument.createElement("Assert");

		ele.setAttribute("value", v.getValue());
		// Oracle 裡的assert沒有msg
		// ele.setAttribute("msg", v.getMessage());
		try {
			ele.setAttribute("method", v.getMethod().toString());
		} catch (NullPointerException npe) {
			ele.setAttribute("method", "");
		}

		/*
		 * NOTE: Oracle 中的view assert 似乎不會用到參數
		 * zwshen 2009/12/23
		 */
		
		// 每個 argument of event 也佔一個 xml element
//		Iterator<Argument> ite = v.getArgumentList().iterator();
//		while (ite.hasNext()) {
//			Argument a = ite.next();
//			org.w3c.dom.Element xarg = m_XmlDocument.createElement("arg");
//			xarg.setAttribute("type", a.getType());
//			xarg.setAttribute("name", a.getName());
//			xarg.setAttribute("value", a.getValue());
//			ele.appendChild(xarg);
//		}

		parent.appendChild(ele);
	}

	/**
	 * 將XML Document 實際寫到外部檔案中
	 */
	public boolean saveFile(String filepath) {
		try {
			System.gc();
			// Serialize DOM
			m_XmlDocument.appendChild(m_XmlRoot);

			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat());
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(m_XmlDocument.getDocumentElement());
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
				m_XmlDocument);

		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}
}
