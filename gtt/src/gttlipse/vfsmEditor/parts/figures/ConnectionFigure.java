package gttlipse.vfsmEditor.parts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;

public class ConnectionFigure extends PolylineConnection {

	private Label m_TextLabel;

	public ConnectionFigure() {
		m_TextLabel = new Label();
		m_TextLabel.setForegroundColor(ColorConstants.black);
		setForegroundColor(ColorConstants.darkBlue);
		setTargetDecoration(new PolygonDecoration());
	}

	public Rectangle getCellEditorRectangle() {
		return m_TextLabel.getBounds().getCopy();
	}

	public Label getLabel() {
		return m_TextLabel;
	}

	public String getText() {
		return m_TextLabel.getText();
	}

	public void setLabel(String name) {
		m_TextLabel.setText(name);
		repaint();
	}

	public Rectangle getTextBounds() {
		return m_TextLabel.getTextBounds();
	}
}
