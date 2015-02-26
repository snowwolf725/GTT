package gttlipse.vfsmEditor.io;

import gttlipse.vfsmEditor.model.AbstractSuperState;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.w3c.dom.Document;


public class VFSMXmlSave {

	public VFSMXmlSave() {
	}

	public void doSave(AbstractSuperState vfsm, String filename) {
		// 利用 Visitor 建立 xml file
		VFSMSaveVisitor visitor = new VFSMSaveVisitor();
		vfsm.accept(visitor);
		Document xmldoc = visitor.getXmlDocument();

		// 將檔案寫出去
		saveXml(filename, xmldoc);

		// 釋放資源
		releaseResource();
	}

	// 釋放資源
	private void releaseResource() {
		Runtime.getRuntime().freeMemory();
		Runtime.getRuntime().gc();
	}

	private boolean saveXml(String filepath, Document xmldoc) {
		try {
			/* Serialize DOM */
			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, formatting(xmldoc));
			
			serial.asDOMSerializer(); /* As a DOM Serializer */
			serial.serialize(xmldoc.getDocumentElement());
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

	private com.sun.org.apache.xml.internal.serialize.OutputFormat formatting(Document doc) {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(
				doc);
		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}
}
