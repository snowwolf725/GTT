package gttlipse.macro.dialog;

import java.util.Iterator;
import java.util.List;

import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gttlipse.editor.ui.ArgumentPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class DynamicComponentEventPanel {

	private Group _group = null;
	private Combo _eventList = null;
	
	private DynamicComponentNode _selectNode = null;
	private DynamicComponentEventNode _editNode = null;
	
	private ArgumentPanel _argsPanel = null;
	
	private IEventModel _model = EventModelFactory.getDefault();
	
	public DynamicComponentEventPanel(Composite parent, DynamicComponentEventNode node) {
		_editNode = node;
		_selectNode = _editNode.getComponent();
		
		initPanel(parent);
		updateEventList();
		updateArgument();
	}
	
	private void initPanel(Composite parent) {
		_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		_group.setText("Select Event");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		_group.setLayout(gridlayout);

		_eventList = new Combo(_group, SWT.READ_ONLY | SWT.DROP_DOWN);
		_eventList.setVisibleItemCount(10);

		GridData gd = new GridData();
		gd.widthHint = 250;
		_eventList.setLayoutData(gd);

		_eventList.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				updateArgument();
			}

			public void widgetSelected(SelectionEvent e) {
				updateArgument();
			}
		});

		_argsPanel = new ArgumentPanel(parent, false, ArgumentPanel.ALL_BUTTON);
	}
	
	private void updateEventList() {
		if (_editNode == null || _selectNode == null)
			return;
		
		_eventList.removeAll();

		String def = "";
		DynamicComponentNode ref = null;

		if (_editNode.getComponent() != null) {
			ref = _editNode.getComponent();
			if (ref == null)
				return;

			// 要編輯的node與選到的node是同一個
			if (ref.getPath().toString().equals(
					_selectNode.getPath().toString()))
				def = _editNode.getEventType();
		}

		ref = _selectNode;

		List<IEvent> events = _model.getEvents(ref.getComponent());

		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (def == "")
				def = event.getName();
			_eventList.add(event.getName());
		}

		_eventList.setText(def);
	}
	
	private void updateArgument() {
		if (_editNode == null || _selectNode == null)
			return;
		
		_argsPanel.clearArgument();
		
		DynamicComponentNode ref = null;
		if (_editNode.getComponent() != null) {
			ref = _editNode.getComponent();

			// 要編輯的node與選到的node是同一個
			if (ref.getPath().toString().equals(
					_selectNode.getPath().toString())) {
				// 未定義參數表示是一個新插入的節點，所以參數要為預設的
				if (_editNode.getArguments().size() != 0) {
					if (_editNode.getEventType().equals(_eventList.getText())) {
						for (int i = 0; i < _editNode.getArguments().size(); i++) {
							_argsPanel.addArgument(_editNode.getArguments().get(i));
						}
						return;
					}
				}
			}
		}

		ref = _selectNode;
		if (ref == null)
			return;

		List<IEvent> events = _model.getEvents(ref.getComponent());
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (event.getName().equals(_eventList.getText())) {
				for (int i = 0; i < event.getArguments().size(); i++) {
					_argsPanel.addArgument(event.getArguments().get(i));
				}
			}
		}
	}
	
	public void updateSelectNode(DynamicComponentNode selectnode) {
		_selectNode = selectnode;
		updateEventList();
		updateArgument();
	}
	
	public int getArgCount() {
		return _argsPanel.getItemCount();
	}

	public Argument getArgument(int index) {
		return Argument.create(_argsPanel.getType(index), _argsPanel
				.getName(index), _argsPanel.getValue(index));
	}
	
	public String getSelectEventType() {
		return _eventList.getText();
	}

	public int getSelectEventID() {
		DynamicComponentNode ref = null;

		if (_editNode.getComponent() != null) {
			ref = _editNode.getComponent();
			// 要編輯的node與選到的node是不同個
			if (!ref.getPath().toString().equals(
					_selectNode.getPath().toString()))
				ref = null;
		}

		if (ref == null)
			ref = _selectNode;

		List<IEvent> events = _model.getEvents(ref.getComponent());
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (event.getName().equals(_eventList.getText()))
				return event.getEventId();
		}

		return 0;
	}
}
