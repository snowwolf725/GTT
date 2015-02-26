/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.model;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.testscript.AbstractNode;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Color;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Connection extends ConnectionBase {

	private State m_Source;
	private State m_Target;

	/**
	 * Event [Condition]/ Action
	 */
	private String m_Condition = "[]";
	private String m_Action = "action";

	private List<AbstractNode> m_EventList = new ArrayList<AbstractNode>();
	
	private HashMap<AbstractMacroNode, Boolean> _coveredSituation = new HashMap<AbstractMacroNode, Boolean>();

	private boolean _isSelectedCovered = false;
	
	private boolean _isMarkedErrorPart = false; 
	
	public List<AbstractNode> getEventList() {
		return m_EventList;
	}
	
	public HashMap<AbstractMacroNode, Boolean> getCoveredSituation() {
		return _coveredSituation;
	}
	
	public void setIsSelectedCovered(boolean value) {
		_isSelectedCovered = value;
		if(value == true)
			_isMarkedErrorPart = false;
	}
	
	public boolean getIsSelectedCovered() {
		return _isSelectedCovered;
	}
	
	public void setIsMarkedErrorPart(boolean value) {
		_isMarkedErrorPart = value;
		if(value == true)
			_isSelectedCovered = false;
	}
	
	public boolean getIsMarkedErrorPart() {
		return _isMarkedErrorPart;
	}
	

	public void setEventList(List<AbstractNode> list) {
		// m_EventList.clear();
		// m_EventList.addAll(list);
		m_EventList = list;
	}
	
	public void setUpCoveredItem(AbstractMacroNode obj) {
		_coveredSituation.put(obj, false);
	}
	
	public void resetCoveredSituation() {
		_coveredSituation.clear();
	}
	
	public boolean setEventCovered(Object event) {
		Iterator<AbstractMacroNode> iter =  _coveredSituation.keySet().iterator();
		
		System.out.println(event.toString());
		System.out.println(event.getClass());
		
		
		while(iter.hasNext()) {
			AbstractMacroNode node = iter.next();
			System.out.println(node.toString());
			System.out.println(node.getClass());
			if(EventNodeComparator.compare(node, event)) {
				_coveredSituation.put(node, true);
				return true;
			}
		}
		
		return false;
	}
	
	public Color getCoveredColor() {
		int allCount, coveredCount = 0;
		Iterator<Boolean> iter = _coveredSituation.values().iterator();
		allCount = _coveredSituation.size();
		
		
		
		while(iter.hasNext()) {
			Boolean bool = iter.next();
			
			if(bool.equals(true))
				coveredCount++;
		}
		
		int rate = (int)((float)coveredCount/allCount*100);
		
		System.out.println("CoveredCount:" + coveredCount + " AllCount:" + allCount);
	
		if(rate >= 0 && rate < 11)
			return new Color(null, 255, 0, 0);
		else if(rate >= 11 && rate < 22)
			return new Color(null, 170, 0, 0);
		else if(rate >= 22 && rate < 33)
			return new Color(null, 85, 0, 0);
		else if(rate >= 33 && rate < 44)
			return new Color(null, 255, 255, 0);
		else if(rate >= 44 && rate < 55)
			return new Color(null, 170, 170, 0);
		else if(rate >= 55 && rate < 66)
			return new Color(null, 85, 85, 0);
		else if(rate >=66  && rate < 77)
			return new Color(null, 0, 85, 0);
		else if(rate >= 77 && rate < 88)
			return new Color(null, 0, 170, 0);
		else if(rate >= 88 && rate <= 100)
			return new Color(null, 0, 255, 0);
		
		return null;
	}

	public void setSource(State source) {
		this.m_Source = source;
	}

	public void setTarget(State target) {
		this.m_Target = target;
	}

	public void setCondition(String cond) {
		m_Condition = cond;
	}

	public String getCondition() {
		return m_Condition;
	}

	public void setAction(String action) {
		m_Action = action;
	}

	public String getAction() {
		return m_Action;
	}

	public State getTarget() {
		return m_Target;
	}

	public State getSource() {
		return m_Source;
	}

	Connection(State source, State target) {
		m_Source = source;
		m_Target = target;
		source.addOutput(this);
		target.addInput(this);
	}

	public void removeConnection(State source, State target) {
		m_Source = source;
		m_Target = target;
		source.removeOutput(this);
		target.removeInput(this);
	}

	@Override
	public String getEvent() {
		if (m_EventList.size() < 1)
			return "null";

		Iterator<AbstractNode> ite = m_EventList.iterator();
		StringBuilder sb = new StringBuilder("");
		while (ite.hasNext()) {
			String temp = ite.next().toDetailString();
			String[] st = temp.split(":{2}?");
			sb.append(st[st.length - 1]);
			if (ite.hasNext())
				sb.append("/");
		}
		return sb.toString();
	}

}