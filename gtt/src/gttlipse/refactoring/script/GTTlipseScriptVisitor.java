package gttlipse.refactoring.script;

import gtt.testscript.AbstractNode;
import gtt.testscript.BreakerNode;
import gtt.testscript.CommentNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.SleeperNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

public abstract class GTTlipseScriptVisitor implements IGTTlipseScriptVisitor {

	@Override
	public void visit(ProjectNode node) {
		BaseNode[] nodes = node.getChildren();
		for(int index = 0; index < nodes.length; index++) {
			nodes[index].accept(this);
		}
	}

	@Override
	public void visit(SourceFolderNode node) {
		BaseNode[] nodes = node.getChildren();
		for(int index = 0; index < nodes.length; index++) {
			nodes[index].accept(this);
		}
	}

	@Override
	public void visit(PackageNode node) {
		BaseNode[] nodes = node.getChildren();
		for(int index = 0; index < nodes.length; index++) {
			nodes[index].accept(this);
		}
	}

	@Override
	public void visit(TestCaseNode node) {
		BaseNode[] nodes = node.getChildren();
		for(int index = 0; index < nodes.length; index++) {
			nodes[index].accept(this);
		}
	}

	@Override
	public void visit(TestMethodNode node) {		
		TestScriptDocument[] doc = node.getDocuments();
		if(doc != null) {
			for(int index = 0; index < doc.length; index++) {				
				AbstractNode[] nodes = doc[index].getChildren();
				for(int i = 0; i < nodes.length; i++) {
					nodes[i].accept(this);
				}
			}
		}
	}

	@Override
	public void visit(FolderNode node) {
		AbstractNode[] nodes = node.getChildren();
		for(int index = 0; index < nodes.length; index++) {
			nodes[index].accept(this);
		}
	}

	@Override
	public void visit(EventNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ViewAssertNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModelAssertNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LaunchNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReferenceMacroEventNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(OracleNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SleeperNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BreakerNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CommentNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReferenceFitNode node) {
		// TODO Auto-generated method stub

	}
}
