package gttlipse.fit.view;

import java.util.HashMap;
import java.util.Map;

public class GTTFitViewDefinition {
	//table���e�T�C�O�ΨӦs��檺���
	final static public int TableInformationColumnSize = 1;
	final static public int TableInformationRowSize = 3;

	//table���A�U����x�s����m
	final static public int TableNameLabelColumn = 0;
	final static public int TableNameLabelRow = 0;
	final static public int FixtureTypeLabelColumn = 0;
	final static public int FixtureTypeLabelRow = 1;
	final static public int TableNameTextColumn = 1;
	final static public int TableNameTextRow = 0;
	final static public int FixtureTypeTextColumn = 1;
	final static public int FixtureTypeTextRow = 1;

	//ColumnFixture �M  RowFixture �ܼƦ�m�M��Ʀ�m�_�l��row
	final static public int VariableRowOfColumnFixture = 2;
	final static public int DataRowOfColumnFixture = 3;

	//ActionFixture��macro name��column position
	final static public int MacroNameOfActionFixture = 0;
	
	//ActionFixture���ܼƩM��Ƥ�column position
	final static public int FirstVariableOfActionFixture = 1;
	final static public int FirstDataOfActionFixture = 1;

	//�w�q�򥻪����j�p
	final static public int TableColumnWidth = 150;
	final static public int TableMaxRowSize = 100;
	final static public int InitTableColumnSize = 5;
	final static public int InitTableRowSize = 15;

	final static public int FixtureTypeDefaultSelectionValue = 0;
	final static public String TableName = "Table Name:";
	final static public String FixtureType = "Fixture Type:";
	final static public String FixtureTypeDefaultSelection = "--------------------  Fixture Type  --------------------";
	final static public String ColumnFixtureSelection = "Column Fixture";
	final static public String RowFixtureSelection = "Row Fixture";
	final static public String ActionFixtureSelection = "Action Fixture";
	final static public String FolderSimbol = "/";
	final static public String TableFolderName = "tables";
	final static public String ColumnTableFolderName = "ColumnTable";
	final static public String RowTableFolderName = "RowTable";
	final static public String ActionTableFolderName = "ActionTable";
	final static public String ColumnFixtureSubtitle = ".column";
	final static public String RowFixtureSubtitle = ".row";
	final static public String ActionFixtureSubtitle = ".Action";
	final static public String ScriptViewId = "GTTlipse.views.GTTTestScriptView";
	final static public String FitViewId = "GTTlipse.GTTFitView";
	final static public String MacroScriptViewId = "GTTlipse.views.macroScriptView";
	final static public Map<String, String> SelectFolder = new HashMap<String, String>();
	final static public Map<String, String> SelectSubtitle = new HashMap<String, String>();
	final static public Map<String, String> SubtitleToType = new HashMap<String, String>();

	static {
		SelectFolder.put(GTTFitViewDefinition.ColumnFixtureSelection, GTTFitViewDefinition.ColumnTableFolderName);
		SelectFolder.put(GTTFitViewDefinition.RowFixtureSelection, GTTFitViewDefinition.RowTableFolderName);
		SelectFolder.put(GTTFitViewDefinition.ActionFixtureSelection, GTTFitViewDefinition.ActionTableFolderName);
		SelectSubtitle.put(GTTFitViewDefinition.ColumnFixtureSelection, GTTFitViewDefinition.ColumnFixtureSubtitle);
		SelectSubtitle.put(GTTFitViewDefinition.RowFixtureSelection, GTTFitViewDefinition.RowFixtureSubtitle);
		SelectSubtitle.put(GTTFitViewDefinition.ActionFixtureSelection, GTTFitViewDefinition.ActionFixtureSubtitle);
		SubtitleToType.put(GTTFitViewDefinition.ColumnFixtureSubtitle, GTTFitViewDefinition.ColumnFixtureSelection);
		SubtitleToType.put(GTTFitViewDefinition.RowFixtureSubtitle, GTTFitViewDefinition.RowFixtureSelection);
		SubtitleToType.put(GTTFitViewDefinition.ActionFixtureSubtitle, GTTFitViewDefinition.ActionFixtureSelection);
	}
}
