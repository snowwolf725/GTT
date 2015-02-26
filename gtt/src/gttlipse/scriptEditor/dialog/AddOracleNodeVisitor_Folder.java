/**
 * 
 */
package gttlipse.scriptEditor.dialog;

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
import gtt.testscript.ViewAssertNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.fit.node.ReferenceFitNode;

import java.util.Iterator;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class AddOracleNodeVisitor_Folder implements ITestScriptVisitor {
	
	private Combo m_com_strategy;
	
	private Tree m_tree;
	
	public AddOracleNodeVisitor_Folder(Combo _com_strategy, Tree _tree){
		m_tree = _tree;
		m_com_strategy = _com_strategy;
	}

	public void visit(FolderNode node) {
		if( m_com_strategy.getSelectionIndex() == 0){
			/* aftare */
			for(int i = 0 ; i< m_tree.getItemCount() ; i++){
				TreeItem treeitem = m_tree.getItem(i);
				OracleNode oraclenode = (OracleNode)treeitem.getData();
				node.add(oraclenode.clone() , node.size());
			}
		} else {
			/* before */
			for(int i = 0 ; i< m_tree.getItemCount() ; i++){
				TreeItem treeitem = m_tree.getItem(i);
				OracleNode oraclenode = (OracleNode)treeitem.getData();
				node.add(oraclenode.clone() , 0);
			}
		}
		
		/* iterator */
		Iterator<AbstractNode> ite = node.iterator();
		while(ite.hasNext()) {
			AbstractNode anode = (AbstractNode)ite.next();
			anode.accept(this);
		}
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
		// TODO Auto-generated method stub
		
	}

	public void visit(ReferenceMacroEventNode node) {
		// TODO Auto-generated method stub
		
	}

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
