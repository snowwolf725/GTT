package gttlipse.editor.ui;

import gtt.eventmodel.IComponent;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;

public interface IComponentInfoPanel {
	
	public Group getGroup();
	public void update();
	public IComponent getComponent();
	public void setListener(ModifyListener listener);
	public void setComponent(IComponent comp);
	public Combo getComponentClassType();
	
}
