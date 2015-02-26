package gtt.macro.visitor;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.macro.view.BadSmellItem;
import gttlipse.macro.view.BadSmellListView;
import gttlipse.preferences.PreferenceConstants;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DuplicateScriptSmellVisitor extends MacroRefactorVisitor {
	private List<MacroEventNode> m_nodes = new ArrayList<MacroEventNode>();
	private List<AbstractMacroNode> m_duplicateNodes = new ArrayList<AbstractMacroNode>();
	private final String BS_DUPLICATE = "Duplicate Event";
	private final int IMPORTANCE_RED = 3;
	private final int IMPORTANCE_YELLOW = 2;
	private final int IMPORTANCE_GREEN = 1;
	
	@Override
	public void visit(ComponentEventNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ComponentNode node) {
		super.visit(node);
	}

	@Override
	public void visit(MacroComponentNode node) {
		super.visit(node);
		Iterator<AbstractMacroNode> ite = node.iterator();
		int usage = 0;
		int totalUsage = 0;
		
		// get child outer usage and total usage
		while (ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			usage += child.getBadSmellData().getBadSmellScore();
			totalUsage += child.getBadSmellData().getTotalBadSmellScore();
		}
		
		node.getBadSmellData().setBadSmellScore(usage);
		node.getBadSmellData().setTotalBadSmellScore(totalUsage);
		node.getBadSmellData().setRGB(22, 128, 128);
	}
	
	private int calLCS(MacroEventNode nodex, MacroEventNode nodey) {
		int LCSLength = 0;
		int M = nodex.size();
        int N = nodey.size();

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (nodex.get(i).toString().equals(nodey.get(j).toString()))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (nodex.get(i).toString().equals(nodey.get(j).toString())) {
            	m_duplicateNodes.add(nodex.get(i));
            	m_duplicateNodes.add(nodey.get(j));
            	LCSLength++;
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) i++;
            else                                 j++;
        }
        return LCSLength;
	}
	
	private boolean isDuplicateEvent(List<MacroEventNode> nodes, MacroEventNode node) {
		BadSmellListView view = GTTlipse.findBadSmellListView();
		boolean result = false;
		boolean isFirst = true;
		BadSmellItem item = null;
		if(nodes.size() == 0)
			return false;
			
		for(MacroEventNode node1:nodes) {
			m_duplicateNodes = new ArrayList<AbstractMacroNode>();
			int LCSLength = calLCS(node1, node);
			
			if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_RED)) {
				node1.getBadSmellData().setBadSmellScore(1);
				node1.getBadSmellData().setTotalBadSmellScore(1);
				node1.getBadSmellData().setRGB(node1.getBadSmellData().COLOR_RED);
				node.getBadSmellData().setBadSmellScore(1);
				node.getBadSmellData().setTotalBadSmellScore(1);
				node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_RED);
				result = true;
			} else if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_YELLOW)) {
				node1.getBadSmellData().setBadSmellScore(1);
				node1.getBadSmellData().setTotalBadSmellScore(1);
				node1.getBadSmellData().setRGB(node1.getBadSmellData().COLOR_YELLOW);
				node.getBadSmellData().setBadSmellScore(1);
				node.getBadSmellData().setTotalBadSmellScore(1);
				node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
				result = true;
			} else if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_GREEN)) {
				node1.getBadSmellData().setBadSmellScore(1);
				node1.getBadSmellData().setTotalBadSmellScore(1);
				node1.getBadSmellData().setRGB(node1.getBadSmellData().COLOR_GREEN);
				node.getBadSmellData().setBadSmellScore(1);
				node.getBadSmellData().setTotalBadSmellScore(1);
				node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_GREEN);
				result = true;
			}
			
			if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_GREEN)){
				if(isFirst) {
					isFirst = false;
					if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_RED))
						item = new BadSmellItem(BS_DUPLICATE, node.getPath().toString(), IMPORTANCE_RED);
					else if(LCSLength >= GTTlipse.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_DUPLICATEEVENT_YELLOW))
						item = new BadSmellItem(BS_DUPLICATE, node.getPath().toString(), IMPORTANCE_YELLOW);
					else
						item = new BadSmellItem(BS_DUPLICATE, node.getPath().toString(), IMPORTANCE_GREEN);
					item.addNode(node);
					view.addBadSmell(item);
				}
				item.addNode(node1);
				for(AbstractMacroNode dnode:m_duplicateNodes) {
					dnode.getBadSmellData().setBadSmellScore(1);
					dnode.getBadSmellData().setTotalBadSmellScore(1);
					dnode.getBadSmellData().setRGB(
							node1.getBadSmellData().getColorR(), 
							node1.getBadSmellData().getColorG(), 
							node1.getBadSmellData().getColorB());
					item.addNode(dnode);
				}
			}
		}
		return result;
	}

	@Override
	public void visit(MacroEventNode node) {
		if(isDuplicateEvent(m_nodes, node)) {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setTotalBadSmellScore(1);
		} else {
			m_nodes.add(node);
		}
	}

	@Override
	public void visit(ModelAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ViewAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(NDefComponentNode node) {
		super.visit(node);
	}

	@Override
	public void visit(BreakerNode node) {

	}

	@Override
	public void visit(CommentNode node) {

	}

	@Override
	public void visit(SleeperNode node) {

	}

	@Override
	public void visit(OracleNode node) {

	}

	@Override
	public void visit(ExistenceAssertNode node) {

	}

	@Override
	public void visit(LaunchNode node) {

	}

	@Override
	public void visit(EventTriggerNode node) {

	}

	@Override
	public void visit(FitStateAssertionNode node) {

	}

	@Override
	public void visit(FitNode node) {

	}

	@Override
	public void visit(SplitDataAsNameNode node) {

	}

	@Override
	public void visit(GenerateOrderNameNode node) {

	}

	@Override
	public void visit(FixNameNode node) {

	}

	@Override
	public void visit(FitAssertionNode node) {
		
	}

	@Override
	public void visit(SplitDataNode node) {
		
	}

	@Override
	public void visit(DynamicComponentNode node) {
		
	}

	@Override
	public void visit(IncludeNode node) {
		
	}
	

	@Override
	public void visit(DynamicComponentEventNode node) {
		
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}	
}
