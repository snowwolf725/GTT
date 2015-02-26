package gttlipse.fit.node;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IHaveArgument;
import gtt.eventmodel.swing.SwingModel;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventTriggerNode extends AbstractMacroNode implements IHaveArgument{
	String m_data;
	String m_generationKey;
	IComponent m_iComponent;
	Arguments m_argmentList;
	List<IEvent> m_eventList;

	public EventTriggerNode(){
		m_data = "";
		m_generationKey = "";
		m_argmentList = new Arguments();
		m_eventList = new ArrayList<IEvent>();
		m_iComponent = new SwingModel().createDefaultComponent();
		m_iComponent.setType("");
		m_iComponent.setTitle("");
		m_iComponent.setWinType("");
	}
	
	public EventTriggerNode(String data, String generationKey, IComponent ic, List<IEvent> eventList){
		m_iComponent = ic.clone();
		m_data = data;
		m_generationKey = generationKey;
		m_argmentList = new Arguments();
		m_eventList = eventList;
	}
	
	public void setData(String data){
		m_data = data;
	}
	
	public String getData(){
		return m_data;
	}
	
	public void setGenerationKey(String key){
		m_generationKey = key;
	}
	
	public String getGenerationKey(){
		return m_generationKey;
	}
	
	public void setWindowTitle(String title) {
		m_iComponent.setTitle(title);
	}

	public String getWindowTitle() {
		return m_iComponent.getTitle();
	}

	public void setWindowType(String type) {
		m_iComponent.setWinType(type);
	}

	public String getWindowType() {
		return m_iComponent.getWinType();
	}

	public void setComponentType(String compType){
		m_iComponent.setType(compType);
	}
	
	public String getComponentType(){
		return m_iComponent.getType();
	}

	public Arguments getArguments(){
		return m_argmentList;
	}

	public void setArguments(Arguments list){
		m_argmentList = list;
	}
	
	@Override
	public void accept(IMacroStructureVisitor v) {
		if(v instanceof IMacroFitVisitor)
			accept(((IMacroFitVisitor)v));
	}

	@Override
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	@Override
	public AbstractMacroNode clone() {
		List<IEvent> eventList = new ArrayList<IEvent>();
		Iterator<IEvent> ite = m_eventList.iterator();
		while(ite.hasNext()) {
			IEvent ie = (IEvent)ite.next();
			IEvent newIE = ie.clone();
			eventList.add(newIE);
		}
		
		return new EventTriggerNode(m_data, m_generationKey, m_iComponent, eventList);
	}
	
	public String toString() {
		return m_data + "." + m_eventList.toString();
	}
	
	public IComponent getIComponent() {
		return m_iComponent;
	}
	
	public void setIComponent(IComponent ic) {
		m_iComponent = ic;
	}
	
	public void setEventList(List<IEvent> list) {
		m_eventList = list;
	}
	
	public List<IEvent> getEventList() {
		return m_eventList;
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_EVENT_TRIGGER_NODE;
	}
}
