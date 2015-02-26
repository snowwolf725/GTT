package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.parts.figures.ConnectionFigure;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;


public class ConnectionCellEditorLocator implements CellEditorLocator {

	private ConnectionFigure m_Figure;

	public ConnectionCellEditorLocator(ConnectionFigure connFigure) {
		m_Figure = connFigure;
	}

	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = m_Figure.getTextBounds();
		text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
	}
}
