package gttlipse.fit.node.processor;

import gttlipse.fit.node.FixNameNode;

public class FixNameNodeProcessor implements GenerationProcessor {
	FixNameNode m_fixNameNode;

	public FixNameNodeProcessor(FixNameNode node) {
		m_fixNameNode = node;
	}

	@Override
	public String[] generate() {
		return m_fixNameNode.getComponentName().split(ProcessorDefinition.DATASPLIT);
	}
}
