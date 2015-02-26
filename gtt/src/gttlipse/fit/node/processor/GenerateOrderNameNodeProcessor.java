package gttlipse.fit.node.processor;

import gttlipse.fit.node.GenerateOrderNameNode;

public class GenerateOrderNameNodeProcessor implements GenerationProcessor {
	GenerateOrderNameNode m_generationNode;
	StringPreprocessor m_stringProcessor;

	public GenerateOrderNameNodeProcessor(GenerateOrderNameNode node) {
		m_generationNode = node;
		m_stringProcessor = new StringPreprocessor();
	}

	public String[] generate() {
		int start = Integer.valueOf(m_generationNode.getStart());
		int end = Integer.valueOf(m_generationNode.getEnd());
		String[] nameList = new String[(end - start) + 1];
		String name = "";

		m_stringProcessor.setArguments(m_generationNode.getArguments());
		
		for(int i = 0; i < (end - start) + 1; i++) {
			name += m_stringProcessor.process(m_generationNode.getPrefix());
			name += String.valueOf(start + i);
			name += m_stringProcessor.process(m_generationNode.getSuffix());
			nameList[i] = name;
			name = "";
		}
		return nameList;
	}
}
