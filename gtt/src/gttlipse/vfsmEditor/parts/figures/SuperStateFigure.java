/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SuperStateFigure extends StateFigure {
	// Light Sky Blue
	final static Color color = new Color(null, 135, 206, 250);
	private NodesFigure m_Figure = new NodesFigure();

	public SuperStateFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setBorder(new MarginBorder(1));
		setBackgroundColor(color);
		setOpaque(true);
		add(m_Figure);
	}

	protected void _setPartBounds(Rectangle rect) {
		// nothing to do
//		m_Rectangle.setBounds(rect);
//		m_name.setBounds(rect);
	}

	public NodesFigure getFigure() {
		return m_Figure;
	}

	class NodesFigure extends Figure {
		public NodesFigure() {
			setLayoutManager(new XYLayout());
			setBorder(new NodesFigureBorder());
			setOpaque(true);
		}
	}

	class NodesFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
					getPaintRectangle(figure, insets).getTopRight());
		}
	}
}