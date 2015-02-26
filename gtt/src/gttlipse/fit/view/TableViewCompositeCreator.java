package gttlipse.fit.view;

import gttlipse.fit.table.FitTable;
import gttlipse.fit.table.TableItem;
import gttlipse.widget.table.TableModel;

import org.eclipse.swt.SWT;


public class TableViewCompositeCreator {
	static public void createTable(ViewInfo viewInfo) {
		int[] columnWidth;
		TableItem[] itemData;
		TableItem[] variable;
		TableItem[] tableName;
		TableItem[] fixtureType;

		columnWidth = new int[viewInfo.getColumnCount()];
		itemData = new TableItem[viewInfo.getColumnCount()];
		variable = new TableItem[viewInfo.getColumnCount()];
		tableName = new TableItem[viewInfo.getColumnCount()];
		fixtureType = new TableItem[viewInfo.getColumnCount()];
		
		//init all table data with a template
		for(int i = 0; i < viewInfo.getColumnCount(); i++) {
			itemData[i] = new TableItem("", SWT.COLOR_BLACK);
			variable[i] = new TableItem("Variable", SWT.COLOR_BLACK);
			columnWidth[i] = GTTFitViewDefinition.TableColumnWidth;
			if(i == GTTFitViewDefinition.TableNameTextColumn)
				tableName[i] = new TableItem(viewInfo.getTableName(), SWT.COLOR_BLACK);
			else if(i == GTTFitViewDefinition.TableNameLabelColumn)
				tableName[i] = new TableItem(GTTFitViewDefinition.TableName, SWT.COLOR_BLACK);
			else
				tableName[i] = new TableItem("", SWT.COLOR_BLACK);
			if(i == GTTFitViewDefinition.FixtureTypeTextColumn)
				fixtureType[i] = new TableItem(viewInfo.getFixtureType(), SWT.COLOR_BLACK);
			else if(i == GTTFitViewDefinition.FixtureTypeLabelColumn)
				fixtureType[i] = new TableItem(GTTFitViewDefinition.FixtureType, SWT.COLOR_BLACK);
			else
				fixtureType[i] = new TableItem("", SWT.COLOR_BLACK);
		}

		//init table infomation
		viewInfo.createTableModel().clear();
		viewInfo.createTableModel().setAllColumnWidth(columnWidth);
		viewInfo.createTableModel().setRowHeight(0, 25);
		
		//set size of column in table can be edited
		for(int i = 0; i < viewInfo.getColumnCount(); i++)
			viewInfo.createTableModel().setColumnEditable(i, TableModel.EDITMODE_EDIT);


		//setup table
		for(int i = 0; i < viewInfo.getRowCount() + GTTFitViewDefinition.TableInformationRowSize; i++) {
			if(i == GTTFitViewDefinition.TableNameTextRow)
				viewInfo.createTableModel().setItemObject(i, tableName);
			else if(i == GTTFitViewDefinition.FixtureTypeTextRow)
				viewInfo.createTableModel().setItemObject(i, fixtureType);
			else if(i == GTTFitViewDefinition.VariableRowOfColumnFixture)
				viewInfo.createTableModel().setItemObject(i, variable);
			else
				viewInfo.createTableModel().setItemObject(i, itemData);
		}
		viewInfo.createTableModel().setTableLinsterer(viewInfo.getKTable());
		viewInfo.createTableModel().initialize();
	}
	
	static public void refreshTable(FitTable fitTable, ViewInfo viewInfo) {
		int[] columnWidth;
		TableItem[] itemData;
		TableItem[] variable;
		TableItem[] tableName;
		TableItem[] fixtureType;

		columnWidth = new int[viewInfo.getColumnCount()];
		itemData = new TableItem[viewInfo.getColumnCount()];
		variable = new TableItem[viewInfo.getColumnCount()];
		tableName = new TableItem[viewInfo.getColumnCount()];
		fixtureType = new TableItem[viewInfo.getColumnCount()];
		
		//init all table data with a template
		for(int i = 0; i < viewInfo.getColumnCount(); i++) {
			columnWidth[i] = GTTFitViewDefinition.TableColumnWidth;
			if(i == GTTFitViewDefinition.TableNameTextColumn)
				tableName[i] = new TableItem(viewInfo.getTableName(), SWT.COLOR_BLACK);
			else if(i == GTTFitViewDefinition.TableNameLabelColumn)
				tableName[i] = new TableItem(GTTFitViewDefinition.TableName, SWT.COLOR_BLACK);
			else
				tableName[i] = new TableItem("", SWT.COLOR_BLACK);;
				if(i == GTTFitViewDefinition.FixtureTypeTextColumn)
					fixtureType[i] = new TableItem(viewInfo.getFixtureType(), SWT.COLOR_BLACK);
				else if(i == GTTFitViewDefinition.FixtureTypeLabelColumn)
					fixtureType[i] = new TableItem(GTTFitViewDefinition.FixtureType, SWT.COLOR_BLACK);
				else
					fixtureType[i] = new TableItem("", SWT.COLOR_BLACK);
		}

		for(int i = 0; i < fitTable.getColumnCount(); i++) {
			variable[i] = new TableItem(fitTable.getColumentElement().get(i).getText(), SWT.COLOR_BLACK);
		}
		//init table infomation
		viewInfo.createTableModel().clear();
		viewInfo.createTableModel().setAllColumnWidth(columnWidth);
		viewInfo.createTableModel().setRowHeight(0, 25);

		//setup table
		for(int i = 0; i < GTTFitViewDefinition.TableInformationRowSize + fitTable.getRowCount(); i++) {
			if(i == GTTFitViewDefinition.TableNameTextRow)
				viewInfo.createTableModel().setItemObject(i, tableName);
			else if(i == GTTFitViewDefinition.FixtureTypeTextRow)
				viewInfo.createTableModel().setItemObject(i, fixtureType);
			else if(i == GTTFitViewDefinition.VariableRowOfColumnFixture)
				viewInfo.createTableModel().setItemObject(i, variable);
			else
				viewInfo.createTableModel().setItemObject(i, itemData);
		}
		
		for(int i = 0; i < fitTable.getRowCount(); i++) {
			for(int j = 0; j < fitTable.getColumnCount(); j++)
				itemData[j] = new TableItem(fitTable.getRowElement(i).get(j).getText(), fitTable.getRowElement(i).get(j).getColor());
			viewInfo.createTableModel().setItemObject(i + GTTFitViewDefinition.TableInformationRowSize, itemData);
		}

		//set size of column in table can be edited
		for(int i = 0; i < viewInfo.getColumnCount(); i++)
			viewInfo.createTableModel().setColumnEditable(i, TableModel.EDITMODE_EDIT);

		viewInfo.createTableModel().setTableLinsterer(viewInfo.getKTable());
		viewInfo.createTableModel().initialize();
	}
}
