package gttlipse.fit.node.processor;

import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ComponentNamePool {
	Arguments m_argumentList;
	ArrayList<AbstractMacroNode> m_nodeList;
	Map<String, String[]> m_dataPool;

	public ComponentNamePool() {
		m_nodeList = new ArrayList<AbstractMacroNode>();
		m_dataPool = new HashMap<String, String[]>();
	}

	public void putNode(AbstractMacroNode node) {
		m_nodeList.add(node);
	}

	public void setArgumentList(Arguments list) {
		m_argumentList = list;
	}

	public void generate() {
		Iterator<AbstractMacroNode> ite = m_nodeList.iterator();
		while(ite.hasNext()) {
			AbstractMacroNode node = (AbstractMacroNode)ite.next();
			if(node instanceof SplitDataAsNameNode) {
				SplitDataAsNameNode generationNode = (SplitDataAsNameNode)node;
				if(m_argumentList.getValue(generationNode.getVariable()) != null) {
					generationNode.setData(m_argumentList.getValue(generationNode.getVariable()));
					m_dataPool.put(generationNode.getName(), new SplitDataAsNameNodeProcessor(generationNode).generate());
				}
			}
			else if(node instanceof GenerateOrderNameNode) {
				GenerateOrderNameNode generationNode = (GenerateOrderNameNode)node;
				if(m_argumentList.getValue(generationNode.getVariableNameOfStart()) != null && m_argumentList.getValue(generationNode.getVariableNameOfEnd()) != null) {
					generationNode.setStart(m_argumentList.getValue(generationNode.getVariableNameOfStart()));
					generationNode.setEnd(m_argumentList.getValue(generationNode.getVariableNameOfEnd()));
//					initArgument(generationNode.getArgumentList());
					generationNode.setArguments(m_argumentList.clone());
					m_dataPool.put(generationNode.getName(), new GenerateOrderNameNodeProcessor(generationNode).generate());
				}
			}
			else if(node instanceof FixNameNode) {
				FixNameNode generationNode = (FixNameNode)node;
				m_dataPool.put(generationNode.getName(), new FixNameNodeProcessor(generationNode).generate());
			}
		}
	}
	
	public String[] getComponentNameList(String key) {
		return m_dataPool.get(key);
	}
	
//	private void initArgument(ArgumentList target) {
//		for(int i = 0; i < target.size(); i++) {
//			for(int j = 0; j < m_argumentList.size(); j++) {
//				if(target.get(i).getName().compareTo(m_argumentList.get(j).getName()) == 0) {
//					target.get(i).setValue(m_argumentList.get(j).getValue());
//				}
//			}
//		}
//	}
}