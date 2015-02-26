package gttlipse.vfsmEditor.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class RectangleBorderAnchor extends BorderAnchor {

	public RectangleBorderAnchor(IFigure figure) {
		super(figure);
	}

	@Override
	public Point getBorderPoint(Point reference) {
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getOwner().getBounds());
		getOwner().translateToAbsolute(r);
		
		double dx = 0.0, dy = 0.0;
		double tan = Math.atan2(r.height, r.width);
		if(angle >= -tan && angle <= tan) {
			dx = r.width >> 1;
			dy = dx * Math.tan(angle);
		} else if(angle >= tan && angle <= Math.PI - tan) {
			dy = r.height >> 1;
			dx = dy / Math.tan(angle);
		} else if(angle <= -tan && angle >= tan - Math.PI) {
			dy = -(r.height >> 1);
			dx = dy / Math.tan(angle);
		} else {
			dx = -(r.width >> 1);
			dy = dx * Math.tan(angle);
		}

		PrecisionPoint pp = new PrecisionPoint(r.getCenter());	
		pp.translate((int)dx, (int)dy);
		return new Point(pp);	
	}
}
