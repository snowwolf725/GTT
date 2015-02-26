package gttlipse;

import gtt.macro.io.XmlMacroFitSaveVisitor;
import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.io.GTTlipseConfigSaver;
import gttlipse.scriptEditor.testScript.io.XmlTestScriptSaveVisitor;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;


public class GTTFileSaver {
	
	public void doSave(Document xmlDocument, XmlTestScriptSaveVisitor scriptv, XmlMacroFitSaveVisitor macrov, ProjectNode testScript, AbstractMacroNode macroScript, String gttfile, GTTlipseConfig gttconfig) {
		Element  xmlRoot     = xmlDocument.createElement( "GTT" );
		xmlDocument.appendChild( xmlRoot );
		
		testScript.accept(scriptv);
		macroScript.accept(macrov);
		
		GTTlipseConfigSaver configSaver = new GTTlipseConfigSaver( gttconfig, xmlDocument );
		configSaver.buildConfig();
		
		xmlRoot.appendChild( configSaver.getConfigRoot() );
		xmlRoot.appendChild( scriptv.getScriptRoot() );
		xmlRoot.appendChild( macrov.getMacroRoot() );
//		savePluginConfig( xmlRoot, xmlDocument);
		
		saveFile( gttfile, xmlDocument );
		if(GTTlipse.getPlatformInfo()!= null)
			GTTlipse.getPlatformInfo().saveFile();
		
		Runtime.getRuntime().freeMemory();
		Runtime.getRuntime().gc();
	}
	
	/**
	 * @param testScript
	 * @param macroScript
	 * @param gttfile
	 * @param gttconfig
	 */
	public void doSave(ProjectNode testScript, AbstractMacroNode macroScript, String gttfile, GTTlipseConfig gttconfig) {
		// change by David Wu(2007-06-06)
		Document xmlDocument = new DocumentImpl();
		Element  xmlRoot     = xmlDocument.createElement( "GTT" );
		xmlDocument.appendChild( xmlRoot );
		
		XmlTestScriptSaveVisitor scriptv = null;
		scriptv = new XmlTestScriptSaveVisitor( xmlDocument );
		testScript.accept(scriptv);
		
		XmlMacroFitSaveVisitor macrov = null;
		macrov = new XmlMacroFitSaveVisitor( xmlDocument );
		macroScript.accept(macrov);
		
		GTTlipseConfigSaver configSaver = new GTTlipseConfigSaver( gttconfig, xmlDocument );
		configSaver.buildConfig();
		
		xmlRoot.appendChild( configSaver.getConfigRoot() );
		xmlRoot.appendChild( scriptv.getScriptRoot() );
		xmlRoot.appendChild( macrov.getMacroRoot() );
//		savePluginConfig( xmlRoot, xmlDocument);
		
		saveFile( gttfile, xmlDocument );
		if(GTTlipse.getPlatformInfo()!= null)
			GTTlipse.getPlatformInfo().saveFile();
		
		Runtime.getRuntime().freeMemory();
		Runtime.getRuntime().gc();
	}
	
//	private void savePluginConfig(final Element  xmlRoot, final Document xmlDocument) {
//		IConfigurationElement[] config = Platform.getExtensionRegistry()
//				.getConfigurationElementsFor(gttlipse.GTTlipse.EP_CONFIG_SAVER_ID);
//		try {
//			for (IConfigurationElement e : config) {
//				// Evaluating extension 
//				final Object o = e.createExecutableExtension("class");
//				if (o instanceof IConfigSaver) {
//					ISafeRunnable runnable = new ISafeRunnable() {
//						@Override
//						public void handleException(Throwable exception) {
//							// Handle exception in client
//						}
//
//						@Override
//						public void run() throws Exception {
//							((IConfigSaver) o).setDocument(xmlDocument);
//							((IConfigSaver) o).doSave();
//							xmlRoot.appendChild( ((IConfigSaver) o).getRoot() );
//						}
//					};
//					SafeRunner.run(runnable);
//				}
//			}
//		} catch (CoreException ex) {
//			System.out.println(ex.getMessage());
//		}
//	}
		
    // add by David Wu(2007-06-06)
	private boolean saveFile(String filepath, Document doc) {
		try {
			// Serialize DOM
			StringWriter stringOut = new StringWriter(); 
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat( doc ));
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(doc.getDocumentElement());
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
	
    // add by David Wu(2007-06-06)
	private com.sun.org.apache.xml.internal.serialize.OutputFormat createXmlFormat( Document doc ) {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat( doc );
		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}
}
