/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Node extends Element implements IPropertySource {

	// property of VFSM model
	final public static String PROP_LOCATION = "LOCATION";
	final public static String PROP_NAME = "NAME";
	final public static String PROP_VISIBLE = "VISIBLE";
	final public static String PROP_INPUTS = "INPUTS";
	final public static String PROP_OUTPUTS = "OUTPUTS";
	final public static String PROP_STATES = "STATES";
	final public static String PROP_SIZE = "SIZE";
	final public static String PROP_PARENT = "PARENT";

	protected Point m_location = new Point(0, 0);
	protected Dimension m_Size = new Dimension(100, 150);
	private boolean m_bVisible = true;

	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
			new TextPropertyDescriptor(PROP_NAME, "Name"),
			new TextPropertyDescriptor(PROP_PARENT, "Parent"),
			new TextPropertyDescriptor(PROP_LOCATION, "Location"),
			new TextPropertyDescriptor(PROP_SIZE, "Size"),
			new TextPropertyDescriptor(PROP_INPUTS, "Input"),
			new TextPropertyDescriptor(PROP_OUTPUTS, "Output"),
			new ComboBoxPropertyDescriptor(PROP_VISIBLE, "Visible",
					new String[] { "true", "false" }) };

	final public void setDimension(Dimension d) {
		if (m_Size.equals(d)) {
			return;
		}
		m_Size = d;
		firePropertyChange(PROP_SIZE, null, d);
	}

	final public void setDimension(int w, int h) {
		if (this.m_Size.equals(w, h)) {
			return;
		}
		this.m_Size.height = h;
		this.m_Size.width = w;
		firePropertyChange(PROP_SIZE, null, new Dimension(w, h));
	}

	/* set collapse flag */
	public void setCollapsed(boolean collapsed) {
			// nothing
	}
	public boolean getCollapsed() {
		return false;
	}
	
	
	// override-able
	// 子類別可以複寫此函式，以計算更佳的layout
	public Dimension getDimension() {
		return this.m_Size;
	}

	final public boolean isVisible() {
		return m_bVisible;
	}

	final public void setVisible(boolean visible) {
		if (this.m_bVisible == visible) {
			return;
		}
		m_bVisible = visible;
		firePropertyChange(PROP_VISIBLE, null, Boolean.valueOf(visible));
	}

	final public void setLocation(Point p) {
		if (this.m_location.equals(p)) {
			return;
		}
		this.m_location = p;
		firePropertyChange(PROP_LOCATION, null, p);
	}

	final public void setLocation(int x, int y) {
		if (this.m_location.equals(new Point(x, y))) {
			return;
		}
		this.m_location.x = x;
		this.m_location.y = y;
		firePropertyChange(PROP_LOCATION, null, new Point(x, y));
	}

	final public Point getLocation() {
		return m_location;
	}

	// ------------------------------------------------------------------------
	// Abstract methods from IPropertySource

	final public Object getEditableValue() {
		return this;
	}

	final public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	final public Object getPropertyValue(Object id) {
		if (PROP_NAME.equals(id))
			return ((State) this).getName();
		if (PROP_PARENT.equals(id))
			return ((State) this).getParent();
		if (PROP_LOCATION.equals(id))
			return getLocation();
		if (PROP_SIZE.equals(id))
			return getDimension();
		if (PROP_INPUTS.equals(id))
			return ((State) this).getIncomingConnections();
		if (PROP_OUTPUTS.equals(id))
			return ((State) this).getOutgoingConnections();
		if (PROP_VISIBLE.equals(id))
			return isVisible() ? new Integer(0) : new Integer(1);
		return null;
	}

	final public boolean isPropertySet(Object id) {
		if (PROP_NAME.equals(id))
			return true;
		if (PROP_PARENT.equals(id))
			return true;
		if (PROP_LOCATION.equals(id))
			return true;
		if (PROP_SIZE.equals(id))
			return true;
		if (PROP_INPUTS.equals(id))
			return true;
		if (PROP_OUTPUTS.equals(id))
			return true;
		if (PROP_VISIBLE.equals(id))
			return true;
		
		return false;
	}

	final public void resetPropertyValue(Object id) {
		if (PROP_NAME.equals(id))
			((State) this).setName("");
		if (PROP_PARENT.equals(id))
			((State) this).setParent(null);
		if (PROP_LOCATION.equals(id))
			setLocation(new Point(0, 0));
		if (PROP_SIZE.equals(id))
			setDimension(new Dimension(10, 10));
		if (PROP_INPUTS.equals(id))
			((State) this).getIncomingConnections().clear();
		if (PROP_OUTPUTS.equals(id))
			((State) this).getOutgoingConnections().clear();
		if (PROP_VISIBLE.equals(id))
			setVisible(true);
	}

	final public void setPropertyValue(Object id, Object value) {
		if (PROP_NAME.equals(id))
			((State) this).setName((String) value);
		if (PROP_PARENT.equals(id))
			((State) this).setParent((State) value);
		if (PROP_LOCATION.equals(id))
			setLocation((Point) value);
		if (PROP_SIZE.equals(id))
			setDimension((Dimension) value);
		if (PROP_VISIBLE.equals(id))
			setVisible(((Integer) value).intValue() == 0);
		if (PROP_INPUTS.equals(id))
			((State) this).addInput((Connection) value);
		if (PROP_OUTPUTS.equals(id))
			((State) this).addOutput((Connection) value);
	}
	
}