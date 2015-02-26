package gttlipse.fit.table;

import gttlipse.fit.view.GTTFitViewDefinition;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;


public class TableLoader {
	static private org.w3c.dom.Node getTable(org.w3c.dom.Node gtt_node, String fixtureType) {
		// 取得 Table中，ColumnFixture 的部份 
		for (int i = 0; i < gtt_node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node cur = gtt_node.getChildNodes().item(i);
			// xml format 初步檢查
			if (cur.getNodeName().compareTo(GTTFitViewDefinition.SelectFolder.get(fixtureType)) == 0)
				return cur;
		}

		return null;
	}
	
	static public FitTable read(String filename, String fixtureType) {
		FitTable fitTable = null;
		try {
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			try {
				doc = builder.parse(new File(filename));
			} catch (FileNotFoundException fnfe) {
				// file doesn't exist
				return null;
			}

			org.w3c.dom.Node table = (Element)doc.getDocumentElement();

			// check file format is belong Fit
			if (table.getNodeName().compareTo("Table") != 0) {
				System.out.println("File:" + filename + " isn't Fit Table.");
				return null;
			}

			org.w3c.dom.Node targetTable = getTable(table, fixtureType);
			if (targetTable == null) {
				System.out.println("File:" + filename + " hasn't art.");
				return null;
			}
			
			for(int i = 0; i < targetTable.getChildNodes().getLength(); i++) {
				if(targetTable.getChildNodes().item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
					fitTable = createFitTable(targetTable.getChildNodes().item(i));
			}
			
			return fitTable;

		} catch (Exception e) {
			System.out.println("XmlFileLoader load file [ " + filename + " ] error.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	static public FitTable createFitTable(org.w3c.dom.Node node) {
		String columnList[];
		String column;
		String row[];
		String item[];
		FitTable fitTable = new FitTable();
		
		org.w3c.dom.Element element = (org.w3c.dom.Element)node;
		
		fitTable.setTableName(element.getNodeName());
		fitTable.setFixtureType(element.getAttribute("FixtureType"));
		column = element.getAttribute("Column");
		columnList = checkEmpty(column.split(FitTableDefinition.ITEM_SPLIT));
		row = element.getAttribute("Row").split(FitTableDefinition.ROW_SPLIT);
		
		for(int i = 0; i < columnList.length; i ++)
			fitTable.addColumnElement(new TableItem(columnList[i].split(FitTableDefinition.COLOR_SPLIT)[0], Integer.valueOf(columnList[i].split(FitTableDefinition.COLOR_SPLIT)[1])));

		for(int i = 0; i < row.length; i++) {
			TableRow temp = new TableRow();
			item = checkEmpty(row[i].split(FitTableDefinition.ITEM_SPLIT));
			if(item.length > 0) {
				for(int j = 0; j < item.length; j++)
					temp.add(new TableItem(item[j].split(FitTableDefinition.COLOR_SPLIT)[0], Integer.valueOf(item[j].split(FitTableDefinition.COLOR_SPLIT)[1])));
				fitTable.addRowElement(temp);
			}
		}
		return fitTable;
	}
	
	static private String[] checkEmpty(String temp[]) {
		for(int i = 0; i < temp.length; i++) {
			if(temp[i].indexOf(FitTableDefinition.EMPTY) != -1)
				temp[i]= temp[i].split(FitTableDefinition.EMPTY)[0] + temp[i].split(FitTableDefinition.EMPTY)[1];
		}
		return temp;
	}
}
