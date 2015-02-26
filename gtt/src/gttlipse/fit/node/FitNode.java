package gttlipse.fit.node;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.CompositeMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

import java.io.File;

public class FitNode extends CompositeMacroNode {
	String m_projectPath;

	File m_fitTableSource;
	MacroEventCallerNode m_MacroEventCallerNode;
	String m_fixtureType;

	public FitNode() {
		super("FIT");
	}

	public FitNode(String projectPath, String name, File table,
			MacroEventCallerNode node, String fixtureType) {
		super(name);
		m_projectPath = projectPath;
		m_fitTableSource = table;
		m_MacroEventCallerNode = node;
		m_fixtureType = fixtureType;
	}

	public void setFitTableSource(File table) {
		m_fitTableSource = table;
	}

	public File getFitTableSource() {
		return m_fitTableSource;
	}

	public void setMacroEventCallerNode(MacroEventCallerNode node) {
		m_MacroEventCallerNode = node;
	}

	public MacroEventCallerNode getMacroEventCallerNode() {
		return m_MacroEventCallerNode;
	}

	public void setFixtureType(String fixtureType) {
		m_fixtureType = fixtureType;
	}

	public String getFixtureType() {
		return m_fixtureType;
	}

	public void setProjectPath(String name) {
		m_projectPath = name;
	}

	public String getProjectPath() {
		return m_projectPath;
	}

	public String getTableFilePath() {
		return checkFilePath(m_fitTableSource.getPath());
	}

	@Override
	public void accept(IMacroStructureVisitor v) {
		if(v instanceof IMacroFitVisitor)
			accept(((IMacroFitVisitor)v));
	}
	
	@Override
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return getName() + "(" + m_fitTableSource.getName() + ","
				+ m_MacroEventCallerNode.getReferencePath() + ","
				+ m_fixtureType + ")";
	}

	// 讀檔及複製貼上
	@Override
	public AbstractMacroNode clone() {
		FitNode node = new MacroNodeFactory().createFitNode();

		node.setProjectPath(m_projectPath);
		node.setName(getName());
		node.setFixtureType(m_fixtureType);
		node.setMacroEventCallerNode(m_MacroEventCallerNode);
		node.setFitTableSource(m_fitTableSource);
		for (int i = 0; i < getChildren().length; i++) {
			node.add(getChildren()[i].clone());
		}

		return node;
	}

	@Override
	protected boolean isAllowedAdd(AbstractMacroNode node) {
		if (node instanceof SplitDataAsNameNode
				|| node instanceof GenerateOrderNameNode
				|| node instanceof FixNameNode)
			return true;
		return false;
	}

	// 在project path 去掉專案的名稱，只要root的path
	public String getProjectRoot() {
		String path = m_projectPath;
		path = path.substring(0, path.lastIndexOf("/") - 1);
		path = path.substring(0, path.lastIndexOf("/"));
		return path;
	}

	// 因為file tostring的路徑會用\\來表示，因此將他換成/
	private String checkFilePath(String path) {
		return path.replace("\\", "/");
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_FIT_NODE;
	}
}
