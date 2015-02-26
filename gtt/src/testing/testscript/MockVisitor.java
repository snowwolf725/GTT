package testing.testscript;

import gtt.testscript.BreakerNode;
import gtt.testscript.CommentNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.SleeperNode;
import gtt.testscript.ViewAssertNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.fit.node.ReferenceFitNode;

public class MockVisitor implements ITestScriptVisitor {

	public void visit(FolderNode node) {
		// TODO Auto-generated method stub
		
	}

	public void visit(EventNode node) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ViewAssertNode node) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ModelAssertNode node) {
		// TODO Auto-generated method stub
		
	}
	
	public void visit(LaunchNode node) {
		
	}

	public void visit(ReferenceMacroEventNode node) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see testscript.visitor.ITestScriptVisitor#visit(testscript.OracleNode)
	 */
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
