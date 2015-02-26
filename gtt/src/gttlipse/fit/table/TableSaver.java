package gttlipse.fit.table;

import gttlipse.fit.FixtureDefinition;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;


public class TableSaver {
	protected org.w3c.dom.Document m_XmlDoc; /* XML Document */
	protected org.w3c.dom.Element m_XmlTable; /* Root of document */
	protected org.w3c.dom.Element m_XmlColumnTable; /* child of table */
	protected org.w3c.dom.Element m_XmlRowTable; /* child of table */
	protected org.w3c.dom.Element m_XmlActionTable; /* child of table */

	public TableSaver() {
		m_XmlDoc = new org.apache.xerces.dom.DocumentImpl();
		m_XmlTable = m_XmlDoc.createElement("Table");
		m_XmlColumnTable = m_XmlDoc.createElement("ColumnTable");
		m_XmlRowTable = m_XmlDoc.createElement("RowTable");
		m_XmlActionTable = m_XmlDoc.createElement("ActionTable");
	}

	/**
	 * 將XML Document 實際寫到外部檔案中
	 */
	private boolean saveFile(String filepath) {
		try {
			// Serialize DOM
			m_XmlDoc.appendChild(m_XmlTable);

			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat());
			// As a DOM Serializer
			serial.asDOMSerializer();
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

	/**
	 * 設定xml document之格式
	 * 
	 * @return
	 */
	private com.sun.org.apache.xml.internal.serialize.OutputFormat createXmlFormat() {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(
				m_XmlDoc);
		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}

	public void save(FitTable fitTable, String filepath) {
		try {
			org.w3c.dom.Element xe = m_XmlDoc.createElement(fitTable
					.getTableName());
			xe.setAttribute("FixtureType", fitTable.getFixtureType());
			xe.setAttribute("Column", fitTable.columnToString());
			xe.setAttribute("Row", fitTable.rowToString());

			if (fitTable.getFixtureType().compareTo(
					FixtureDefinition.ActionFixture) == 0) {
				m_XmlTable.appendChild(m_XmlActionTable);
				m_XmlActionTable.appendChild(xe);
			} else if (fitTable.getFixtureType().compareTo(
					FixtureDefinition.ColumnFixture) == 0) {
				m_XmlTable.appendChild(m_XmlColumnTable);
				m_XmlColumnTable.appendChild(xe);
			} else if (fitTable.getFixtureType().compareTo(
					FixtureDefinition.RowFixture) == 0) {
				m_XmlTable.appendChild(m_XmlRowTable);
				m_XmlRowTable.appendChild(xe);
			}
			
			saveFile(filepath);

		} catch (Exception e) {
			System.err.println("Column Fit Table vistor Exception");
		}
	}
}
