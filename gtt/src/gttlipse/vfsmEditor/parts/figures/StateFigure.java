/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class StateFigure extends RoundedRectangle implements IVFSMFigure {

	public static Color color = new Color(null, 240, 128, 128); /* Light Coral */

	protected RoundedRectangle m_Rectangle;
	protected Label m_name;

	public StateFigure() {
		m_Rectangle = new RoundedRectangle();
		m_name = new Label();
		add(m_Rectangle);
		add(m_name);
		setBackgroundColor(color);
	}

	final public String getText() {
		return m_name.getText();
	}

	final public Rectangle getTextBounds() {
		return m_name.getTextBounds();
	}

	final public void setName(String name) {
		this.m_name.setText(name);
		repaint();
	}

	final public void setName(Label name) {
		this.m_name = name;
		repaint();
	}

	final public String getName() {
		return m_name.getText();
	}

	final public void setIcon(Image icon) {
		m_name.setIcon(icon);
	}

	protected void _setPartBounds(Rectangle rect) {
		m_Rectangle.setBounds(rect);
		m_name.setBounds(rect);
	}

	final public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		_setPartBounds(rect);
	}
}