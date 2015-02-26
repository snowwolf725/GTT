package gttlipse.fit.node.processor;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;

public class StringPreprocessor implements IHaveArgument {

	Arguments m_argumentList;

	public StringPreprocessor() {
	}

	@Override
	public Arguments getArguments() {
		return m_argumentList;
	}

	@Override
	public void setArguments(Arguments list) {
		m_argumentList = list;
	}

	public void processEscapeCharacter(String[] target) {
		for(int i = 0; i < target.length; i++) {
			target[i].replaceAll("$", ",");
		}
	}
	
	public void processEscapeCharacter(String target) {
		target.replaceAll("#,#", "$");
	}
	
	public String process(String target) {
		int first = target.indexOf(ProcessorDefinition.STRINGPARAMENTER);
		int second = target.indexOf(ProcessorDefinition.STRINGPARAMENTER, first + 1);
		while(first != -1 && second != -1) {
			String variable = target.substring(first, second + 1);
			String result = target.replaceFirst(variable, findValue(variable));
			return process(result);
		}
		return target;
	}
	
	public String findValue(String variable) {
		variable = variable.substring(1, variable.length() - 1);
		return m_argumentList.getValue(variable);
	}
}
