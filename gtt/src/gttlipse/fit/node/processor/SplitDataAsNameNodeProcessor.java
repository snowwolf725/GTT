package gttlipse.fit.node.processor;

import gttlipse.fit.node.SplitDataAsNameNode;

public class SplitDataAsNameNodeProcessor implements GenerationProcessor {
	SplitDataAsNameNode m_splitDataNameNode;
	
	public SplitDataAsNameNodeProcessor(SplitDataAsNameNode node) {
		m_splitDataNameNode = node;
	}

	public String[] generate() {
		return m_splitDataNameNode.getData().split(ProcessorDefinition.DATASPLIT);
	}
}
