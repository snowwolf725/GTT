package gttlipse.vfsmCoverageAnalyser.model;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;


public class TestCaseInformation {
	// 用來儲存input資源的Vector
	private Vector<BaseNode> _sourceNodes = null;
	
	//用來儲存輸出的LinkedList，其中儲存的是Node的LinkedList
	private LinkedList<LinkedList<Object>> _eventsList = null;
	
	private HashMap<LinkedList<Object>, TestMethodNode> _parentMap = null;
	
	private HashMap<LinkedList<Object>, Boolean> _usefulSituation = null;
	
	/* 
	 * 在AbstractNode中ReferenceMacroEventNode需要MacroDocument才能夠取得MacroEventNode，
	 * 因此必須設定MacroDocument
	 */
	private MacroDocument _macroDoc;
	
	/*
	 * 標準Constructor，必須要使用到BaseNodes當作input的Constructor
	 */
	public TestCaseInformation(Vector<BaseNode> sources, MacroDocument macroDoc) {
		_sourceNodes = sources;
		setMacroDocument(macroDoc);
		parseSource();
	}
	
	/*
	 * 測試用Constructor，直接Assign測試資料用的Constructor
	 */
	public TestCaseInformation(LinkedList<LinkedList<Object>> eventsList) {
		_eventsList = eventsList;
	}
	
	public void setMacroDocument(MacroDocument macroDoc) {
		_macroDoc = macroDoc;
	}
	
	public LinkedList<LinkedList<Object>> getEventsList() {
		LinkedList<Object> tempList;
		
		/*
		 * 檢測是否只有EventNode或ComponentEventNode兩種型態
		 * 若一碰到非上述兩種型態就回傳null
		 */
		for(int i = 0; i < _eventsList.size(); i++) {
			tempList = _eventsList.get(i);
			for(int j = 0; j < tempList.size(); j++) {
				if(!(tempList.get(j) instanceof EventNode || tempList.get(j) instanceof ComponentEventNode)) {
					return null;
				}
			}
		}
		
		return _eventsList;
	}
	
	public int getEventsListSize() {
		if(_eventsList != null)
			return _eventsList.size();
		else
			return 0;
	}
	
	public LinkedList<Object> getEventsListByIndex(int index) {
		if(_eventsList != null) {
			if(index < 0 || index >= _eventsList.size())
				return null;
			else {
				for(int i = 0; i < _eventsList.get(index).size(); i++) {
					if(!(_eventsList.get(index).get(i) instanceof EventNode 
						 || _eventsList.get(index).get(i) instanceof ComponentEventNode)) {
						return null;
					}
				}
				return _eventsList.get(index);
			}
		}
		else
			return null;
	}
	
	public void parseSource() {
		_eventsList = new LinkedList<LinkedList<Object>>();
		_parentMap = new HashMap<LinkedList<Object>, TestMethodNode>();
		
		LinkedList<BaseNode> stackOfBaseNodes = new LinkedList<BaseNode>();
		LinkedList<TestMethodNode> methodNodes = new LinkedList<TestMethodNode>();
		
		int childrenSize;
		
		childrenSize = _sourceNodes.size();
		
		
		
		for(int i = childrenSize-1; i > -1; i--) {
			stackOfBaseNodes.push(_sourceNodes.get(i));
		}
		
		while(!stackOfBaseNodes.isEmpty()) {
			BaseNode tempBaseNode;
			
			tempBaseNode = stackOfBaseNodes.pop();
			if(tempBaseNode instanceof TestMethodNode) {
				methodNodes.add((TestMethodNode)tempBaseNode);
			}
			else if(tempBaseNode instanceof CompositeNode) {
				BaseNode[] children = ((CompositeNode)tempBaseNode).getChildren();
				childrenSize = children.length;
				for(int i = childrenSize-1; i > -1; i--) {
					stackOfBaseNodes.push(children[i]);
				}
			}
		}
		
		for(int i = 0; i < methodNodes.size(); i++) {
			TestScriptDocument[] tempTestScriptDocs;
			tempTestScriptDocs = (methodNodes.get(i)).getDocuments();
			LinkedList<Object> eventNodes = new LinkedList<Object>();
			LinkedList<AbstractNode> stackOfAbstractNodes = new LinkedList<AbstractNode>();
			
			if(tempTestScriptDocs != null) {
				for(int j = tempTestScriptDocs.length - 1; j > -1; j--) {
					AbstractNode tempAbstractNode;
					
					tempAbstractNode = (tempTestScriptDocs[j]).getScript();
					stackOfAbstractNodes.push(tempAbstractNode);
				}	
				while(!stackOfAbstractNodes.isEmpty()) {
					AbstractNode tempAbstractNode;
					
					tempAbstractNode = stackOfAbstractNodes.pop();
					if(tempAbstractNode instanceof EventNode) {
						eventNodes.add((EventNode)tempAbstractNode);
						
					}
					else if(tempAbstractNode instanceof ReferenceMacroEventNode) {
						AbstractMacroNode abstractMacroNode = _macroDoc.findByPath(((ReferenceMacroEventNode)tempAbstractNode).getRefPath());
						handleMacroEventNode((MacroEventNode)abstractMacroNode, eventNodes);
					}
					else if(tempAbstractNode instanceof FolderNode) {
						AbstractNode[] children = tempAbstractNode.getChildren();
						for(int k = children.length - 1; k > -1; k--) {
							stackOfAbstractNodes.push(children[k]);
						}
					}
					else {
						
					}
				}
				if(eventNodes.size() > 0) {
					_eventsList.add(eventNodes);
					_parentMap.put(eventNodes, methodNodes.get(i));
				}
			}
		}
		
		buildDefaultUsefulSituation();	
	}
	
	public TestMethodNode getParentMethodNode(LinkedList<Object> eventNodes) {
		return _parentMap.get(eventNodes);
	}
	
	private void handleMacroEventNode(AbstractMacroNode macroEventNode, LinkedList<Object> eventNodes) {
		Iterator<AbstractMacroNode> childrenIter;
		
		childrenIter = macroEventNode.iterator();
		
		while(childrenIter.hasNext()) {
			AbstractMacroNode child = childrenIter.next();
			
			if(child instanceof ComponentEventNode) {
				eventNodes.add((ComponentEventNode)child);
			}
			else if(child instanceof MacroEventCallerNode) {
				handleMacroEventNode(((MacroEventCallerNode)child).getReference(), eventNodes);
			}
		}
	}
	
	private void buildDefaultUsefulSituation() {
		_usefulSituation = new HashMap<LinkedList<Object>, Boolean>();
		
		for(int index = 0; index < _eventsList.size(); index++) {
			_usefulSituation.put(_eventsList.get(index), true);
		}
	}
	
	private void rebuildEventsList() {
		for(int index = 0; index < _eventsList.size(); index++) {
			if(_usefulSituation.get(_eventsList.get(index)).equals(false)) {
				_eventsList.remove(index);
				index--;
			}
		}
	}
	
	public boolean setEventsUsefulSituation(LinkedList<Object> events, Boolean value) {
		if(_usefulSituation.containsKey(events) == false) {
			return false;
		}
		else {
			_usefulSituation.put(events, value);
			rebuildEventsList();
			return true;
		}
	}
	
	public void printData() {
		LinkedList<Object> eventNodes;
		String line;
		
		for(int i =0; i < _eventsList.size(); i++) {
			eventNodes = _eventsList.get(i);
			if(_usefulSituation.get(eventNodes).equals(true)) {
				line = new String();
				for(int j = 0; j < eventNodes.size(); j++) {
					if(eventNodes.get(j) instanceof EventNode) {
						line += ((EventNode)eventNodes.get(j)).toDetailString();
					}
					else if(eventNodes.get(j) instanceof ComponentEventNode) {
						line += ((ComponentEventNode)eventNodes.get(j)).toString();
					}
					else {
						System.out.println("Error");
						return;
					}
				}
				System.out.println(line);
			}
		}
	}
}
