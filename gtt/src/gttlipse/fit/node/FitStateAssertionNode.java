package gttlipse.fit.node;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

import java.io.File;

public class FitStateAssertionNode extends AbstractMacroNode implements IHaveArgument{
	Arguments m_argmentList = null;
	File m_fitTableSource = null;
	String m_projectPath = null;
	String m_name = null;
	String m_windowType = null;
	String m_windowTitle = null;

	FitStateAssertionNode(String path, String name, String type, String title, Arguments list, File file) {
		m_projectPath = path;
		m_name = name;
		m_windowType = type;
		m_windowTitle = title;
		m_argmentList = list;
		m_fitTableSource = file;
	}

	public FitStateAssertionNode() {
		m_name = "";
		m_windowType = "";
		m_windowTitle = "";
	}

	public String toString() {
		return m_name + "(" + m_fitTableSource.getName() + ")";
	}
	
	public void setFitTableSource(File file) {
		m_fitTableSource = file;
	}
	
	public File getFitTableSource() {
		return m_fitTableSource;
	}
	
	public String getTableFilePath() {
		return checkFilePath(m_fitTableSource.getPath());
	}

	public void setWindowTitle(String title) {
		m_windowTitle = title;
	}

	public String getWindowTitle() {
		return m_windowTitle;
	}

	public void setWindowType(String type) {
		m_windowType = type;
	}

	public String getWindowType() {
		return m_windowType;
	}

	public void setProjectPath(String tablePath) {
		m_projectPath = tablePath;
	}
	
	public String getProjectPath() {
		return m_projectPath;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public String getName() {
		return m_name;
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
	public AbstractMacroNode clone() {
		return new FitStateAssertionNode(m_projectPath, m_name, m_windowType, m_windowTitle, m_argmentList.clone(), m_fitTableSource);
	}

	@Override
	public Arguments getArguments() {
		return m_argmentList;
	}

	@Override
	public void setArguments(Arguments list) {
		m_argmentList = list;
	}
	//在project path 去掉專案的名稱，只要root的path
	public String getProjectRoot() {
		String path = m_projectPath;
		path = path.substring(0, path.lastIndexOf("/") - 1);
		path = path.substring(0, path.lastIndexOf("/"));
		return path;
	}
	//因為file tostring的路徑會用\\來表示，因此將他換成/
	private String checkFilePath(String path) {
		return path.replace("\\", "/");
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_FIT_STATE_ASSERTION_NODE;
	}
}
