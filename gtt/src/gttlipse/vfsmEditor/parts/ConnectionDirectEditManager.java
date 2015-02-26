package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.parts.figures.ConnectionFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;


public class ConnectionDirectEditManager extends DirectEditManager {

	private ConnectionFigure m_Figure;

	public ConnectionDirectEditManager(GraphicalEditPart source,
			Class<?> editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		m_Figure = (ConnectionFigure) source.getFigure();
	}

	protected void initCellEditor() {
		Font scaledFont = new Font(null, createFondData());
		setupCellValue(scaledFont);
	}

	private void setupCellValue(Font scaledFont) {
		Text text = (Text) getCellEditor().getControl();
		text.setFont(scaledFont);
		text.selectAll();
		getCellEditor().setValue(getModelValue());
	}
	
	private FontData createFondData() {
		IFigure figure = ((GraphicalEditPart) getEditPart()).getFigure();

		FontData fd = figure.getFont().getFontData()[0];
		Dimension fontSize = new Dimension(0, fd.getHeight());
		m_Figure.translateToAbsolute(fontSize);
		fd.setHeight(fontSize.height);
		return fd;
	}

	private String getModelValue() {
		// 取出Connection上標示的event
		return ((Connection) getEditPart().getModel()).getEvent();
	}
}
