package android4gtt.macro.node.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gttlipse.GTTlipse;
import gttlipse.editor.ui.IComponentInfoPanel;
import gttlipse.scriptEditor.views.GTTTestScriptView;

public class AndroidComponentInfoPanel implements IComponentInfoPanel {
	private IComponent m_com = null;
	private Group group = null;
	private Combo m_componentClassType = null;
	private Combo m_historyComponent = null;
	private Text _activityName;
	private Text _componentName;
	private Text _text;
	private Text _index;
	private Text _indexOfSame;
	
	public AndroidComponentInfoPanel(Composite parent, IComponent com) {
		m_com = com;
		initPanel(parent);
	}

	private void createComponentTypeCombo(final Composite area, GridData data) {
		final Label lblComptype = new Label(area, SWT.NULL);
		lblComptype.setText("Component type:");
		m_componentClassType = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_componentClassType.setVisibleItemCount(10);
		
		final IEventModel model = EventModelFactory.getDefault();
		List<IComponent> coms = model.getComponents();
		TreeSet<String> sset_coms = new TreeSet<String>();
		Iterator<?> ite = coms.iterator();
		while (ite.hasNext()) {
			IComponent c = (IComponent) ite.next();
			c.setName("name");
			sset_coms.add(c.getType());
		}
		
		ite = sset_coms.iterator();
		boolean firstmethod = true;
		while (ite.hasNext()) {
			String com = (String)ite.next();
			m_componentClassType.add(com);
			if(firstmethod) {
				m_componentClassType.setText(com);
				firstmethod = false;
			}
		}
						
		m_componentClassType.setLayoutData(data);
		if( m_com != null ) {
			m_componentClassType.setText( m_com.getType() );
		}
	}

	
	private void initPanel(Composite parent) {
		// layout
		final GridLayout componentLayout = new GridLayout();
		componentLayout.numColumns = 2;
		GridData textFildData = new GridData();
		textFildData.widthHint = 320;
		GridData labelData = new GridData();
		labelData.widthHint = 115;
		// group
		group = new Group(parent, SWT.SHADOW_ETCHED_IN );
		group.setLayout(componentLayout);
		group.setText("Component Information");	
		// Activity Name
		Label lblActivityName = new Label(group, SWT.NULL);
		lblActivityName.setText("Activity Name: ");
		lblActivityName.setLayoutData(labelData);
		_activityName = new Text(group, SWT.BORDER);
		_activityName.setLayoutData(textFildData);
		if(m_com.getTitle() != null)
			_activityName.setText(m_com.getTitle());
		// Component Class Type
		createComponentTypeCombo(group, textFildData);
		// Component Name
		Label lblComponentName = new Label(group, SWT.NULL);
		lblComponentName.setText("Component Name: ");
		lblComponentName.setLayoutData(labelData);
		_componentName = new Text(group, SWT.BORDER);
		_componentName.setLayoutData(textFildData);
		if(m_com.getName() != null)
			_componentName.setText(m_com.getName());
		// Text
		Label lblText = new Label(group, SWT.NULL);
		lblText.setText("Text: ");
		lblText.setLayoutData(labelData);
		_text = new Text(group, SWT.BORDER);
		_text.setLayoutData(textFildData);
		if(m_com.getText() != null)
			_text.setText(m_com.getText());
		// Index in window
		Label lblIndex = new Label(group, SWT.NULL);
		lblIndex.setText("Index: ");
		lblIndex.setLayoutData(labelData);
		_index = new Text(group, SWT.BORDER);
		_index.setLayoutData(textFildData);
		_index.setText(String.valueOf(m_com.getIndex()));
		// index of same name
		Label lblIndexOfSame = new Label(group, SWT.NULL);
		lblIndexOfSame.setText("Index Of Same Text: ");
		lblIndexOfSame.setLayoutData(labelData);
		_indexOfSame = new Text(group, SWT.BORDER);
		_indexOfSame.setLayoutData(textFildData);
		_indexOfSame.setText(String.valueOf(m_com.getIndexOfSameName()));		
		// history component
		Label lblHistoryComp = new Label(group, SWT.NULL);
		lblHistoryComp.setText("History Components: ");
		lblHistoryComp.setLayoutData(labelData);
		m_historyComponent = new Combo(group, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_historyComponent.setLayoutData(textFildData);
		setupHistoryCompnent();

	}

	public Group getGroup() {
		return group;
	}

	public void update() {
		m_com.setTitle( _activityName.getText() );
		m_com.setType( m_componentClassType.getText() );
		m_com.setName( _componentName.getText() );
		m_com.setText( _text.getText() );
		m_com.setIndex( Integer.parseInt(_index.getText()) );
		m_com.setIndexOfSameName( Integer.parseInt(_indexOfSame.getText()) );
		GTTTestScriptView view = GTTlipse.showScriptView();
		view.addComponent(m_com);
	}
	
	public IComponent getComponent() {
		update();
		return m_com;
	}

	public void setListener(ModifyListener listener) {
		m_componentClassType.addModifyListener(listener);
	}
	
	public void setComponent(IComponent comp){
		if(comp.getName() != null)
			_componentName.setText(comp.getName());
		if(comp.getTitle() != null)
			_activityName.setText(comp.getTitle());
		if(comp.getType() != null)
			m_componentClassType.setText(comp.getType());
		if(comp.getText() != null)
			_text.setText(comp.getText());
		_index.setText(comp.getIndex()+"");
		_indexOfSame.setText(comp.getIndexOfSameName()+"");
	}
	
	public Combo getComponentClassType(){
		return m_componentClassType;
	}
	
	private void setupHistoryCompnent(){
		GTTTestScriptView view = GTTlipse.showScriptView();
		Queue<IComponent> comps = view.getHistoryComponents();
		Iterator<IComponent> ite = comps.iterator();
		while(ite.hasNext()){
			IComponent comp = (IComponent)ite.next();
			m_historyComponent.add(comp.getType() + " - " + comp.getName());
		}
		
		m_historyComponent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = m_historyComponent.getSelectionIndex();
				String compinfo = m_historyComponent.getItem(index);
				int separateIndex = compinfo.indexOf("-");
				if(separateIndex != -1){
					String compType = compinfo.substring(0,separateIndex-1);
					String compName = compinfo.substring(separateIndex+2, compinfo.length());
					
					GTTTestScriptView view =  GTTlipse.showScriptView();
					Queue<IComponent> comps = view.getHistoryComponents();
					Iterator<IComponent> ite = comps.iterator();
					while(ite.hasNext()){
						IComponent comp = (IComponent)ite.next();
						if(comp.getName().matches(compName) &&
						   comp.getType().matches(compType)){
							m_componentClassType.setText( comp.getType() );
							_activityName.setText(comp.getTitle());
							_componentName.setText(comp.getName());
							_text.setText(comp.getText());
							_index.setText(String.valueOf(comp.getIndex()));
							_indexOfSame.setText(String.valueOf(comp.getIndexOfSameName()));
						}
					}
				}
			}
		});
	}
}
