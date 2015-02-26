package gtt.macro.macroStructure;

import gtt.eventmodel.Argument;
import gttlipse.macro.dialog.EditDialogFactory;

import java.util.ArrayList;

public class SystemNode extends MacroComponentNode {
	public SystemNode(String name) {
		super(name);
	}
	
	public static SystemNode create(String name) {
		return new SystemNode(name);
	}
	
	public ArrayList<String> getEventList() {
		//set and get system events list
		ArrayList<String> eventList = new ArrayList<String>();
		eventList.add("SplitBySign");
		eventList.add("SplitByWeight");
		eventList.add("LoadTesting");
		
		return eventList;
		
	}
	
	public ArrayList<Argument> getEventArgList(String eventName) {
		ArrayList<Argument> argList = new ArrayList<Argument>();
		if(eventName.equalsIgnoreCase("SplitBySign")) {
			argList.add(new Argument("String", "Data"));
			argList.add(new Argument("String", "Sign"));
			argList.add(new Argument("String", "Target"));
		}
		
		if(eventName.equalsIgnoreCase("SplitByWeight")) {
			argList.add(new Argument("String", "Data"));
			argList.add(new Argument("String", "Weight"));
			argList.add(new Argument("String", "Target"));
		}
		
		if(eventName.equalsIgnoreCase("LoadTesting")) {
			argList.add(new Argument("String", "MacroEvent"));
			argList.add(new Argument("String", "SimulationNumber"));			
		}
		
		return argList;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_SYSTEM_NODE;
	}
	
}
