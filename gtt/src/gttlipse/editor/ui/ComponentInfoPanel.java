package gttlipse.editor.ui;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;
import gttlipse.scriptEditor.views.GTTTestScriptView;

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

public class ComponentInfoPanel implements IComponentInfoPanel {
	private IComponent m_com = null;
	private Group m_group = null;

	private Text m_windowClassType;
	private Text m_windowTitle;
	private Combo m_componentClassType = null;
	private Combo m_historyComponent = null;
	private Text m_componentName;
	private Text m_text;
	private Text m_indexInWindow;
	private Text m_indexOfSameName;

	public ComponentInfoPanel(Composite parent, IComponent com) {
		m_com = com;
		initPanel(parent);
	}

	private void createComponentTypeCombo(final Composite area, GridData data) {
		final Label lbl_comptype = new Label(area, SWT.NULL);
		lbl_comptype.setText("Component type:");
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
		
		// 修正沒有 JComponent 和 Component 的問題
		if(GTTlipseConfig.testingOnWebPlatform()) {
			sset_coms.add("javax.swing.JComponent");
			sset_coms.add("java.awt.Component");
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
		// init group
		m_group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		m_group.setText( "Component Infomaction" );
		
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		m_group.setLayout(gridlayout);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;
		textFildData.heightHint = 20;

		// Window Class Type
		Label _windowClassType = new Label(m_group, SWT.NULL);
		_windowClassType.setText("Window Class Type: ");
		m_windowClassType = new Text(m_group, SWT.BORDER);
		m_windowClassType.setLayoutData(textFildData);
		if(m_com.getWinType() != null)
			m_windowClassType.setText(m_com.getWinType());
		
		// Window Title
		Label _windowTitle = new Label(m_group, SWT.NULL);
		_windowTitle.setText("Window Title: ");
		m_windowTitle = new Text(m_group, SWT.BORDER);
		m_windowTitle.setLayoutData(textFildData);
		if(m_com.getTitle() != null)
			m_windowTitle.setText(m_com.getTitle());

		// Component Class Type
		createComponentTypeCombo(m_group, textFildData);
		// Component Name
		Label _componentName = new Label(m_group, SWT.NULL);
		_componentName.setText("Component Name: ");
		m_componentName = new Text(m_group, SWT.BORDER);
		m_componentName.setLayoutData(textFildData);
		if(m_com.getName() != null)
			m_componentName.setText(m_com.getName());

		// Text
		Label _text = new Label(m_group, SWT.NULL);
		_text.setText("Text: ");
		m_text = new Text(m_group, SWT.BORDER);
		m_text.setLayoutData(textFildData);
		if(m_com.getText() != null)
			m_text.setText(m_com.getText());

		// Index in window
		Label _indexInWindow = new Label(m_group, SWT.NULL);
		_indexInWindow.setText("Index In Window: ");
		m_indexInWindow = new Text(m_group, SWT.BORDER);
		m_indexInWindow.setLayoutData(textFildData);
		m_indexInWindow.setText(String.valueOf(m_com.getIndex()));

		// index of same name
		Label _indexOfSameName = new Label(m_group, SWT.NULL);
		_indexOfSameName.setText("Index Of Same Name: ");
		m_indexOfSameName = new Text(m_group, SWT.BORDER);
		m_indexOfSameName.setLayoutData(textFildData);
		m_indexOfSameName.setText(String.valueOf(m_com.getIndexOfSameName()));
		
		// history component
		Label _historyComp = new Label(m_group, SWT.NULL);
		_historyComp.setText("History Components: ");
		m_historyComponent = new Combo(m_group, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_historyComponent.setLayoutData(textFildData);
		setupHistoryCompnent();
	}

	public Group getGroup() {
		return m_group;
	}

	public void update() {
		m_com.setWinType( m_windowClassType.getText() );
		m_com.setTitle( m_windowTitle.getText() );
		m_com.setType( m_componentClassType.getText() );
		m_com.setName( m_componentName.getText() );
		m_com.setText( m_text.getText() );
		m_com.setIndex( Integer.parseInt(m_indexInWindow.getText()) );
		m_com.setIndexOfSameName( Integer.parseInt(m_indexOfSameName.getText()) );
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
			m_componentName.setText(comp.getName());
		if(comp.getWinType() != null)
			m_windowClassType.setText(comp.getWinType());
		if(comp.getTitle() != null)
			m_windowTitle.setText(comp.getTitle());
		if(comp.getType() != null)
			m_componentClassType.setText(comp.getType());
		if(comp.getText() != null)
			m_text.setText(comp.getText());
		m_indexInWindow.setText(comp.getIndex()+"");
		m_indexOfSameName.setText(comp.getIndexOfSameName()+"");
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
							m_windowClassType.setText(comp.getWinType());
							m_windowTitle.setText(comp.getTitle());
							m_componentName.setText(comp.getName());
							m_text.setText(comp.getText());
							m_indexInWindow.setText(String.valueOf(comp.getIndex()));
							m_indexOfSameName.setText(String.valueOf(comp.getIndexOfSameName()));
						}
					}
				}
			}
		});
	}
}
