package gttlipse.vfsmEditor.model;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * AND/XOR State Plane Layout algorithm
 * 
 * @author zwshen
 * 
 */
public class PlaneLayout {

	public static final int BOUND_SIZE = 2;
	public static final int LABEL_SIZE = 20;

	/* boundingBox */
	public static Dimension getBoundingBox(State ss) {
		setupBounding(ss.getAll());
		/* compute maximum size */
		int maxX = 80;
		int maxY = 20;
		for (int i = 0; i < ss.size(); i++) {
			State s = ss.getAll().get(i);
			int tempX = s.getLocation().x + s.getDimension().width;
			if (tempX > maxX)
				maxX = tempX;
			int tempY = s.getLocation().y + s.getDimension().height;
			if (tempY > maxY)
				maxY = tempY;
		}
		return new Dimension(maxX, maxY);
	}

	public static Dimension calDimension(List<State> slist) {
		setupBounding(slist);
		int maxW = 80;
		int maxH = 20;

		Iterator<State> ite = slist.iterator();
		while (ite.hasNext()) {
			State s = ite.next();
			int w = s.getLocation().x + s.getDimension().width;
			if (w > maxW)
				maxW = w;
			int h = s.getLocation().y + s.getDimension().height;
			if (h > maxH)
				maxH = h;
		}

		final int EXTRA = 30;
		return new Dimension(maxW + EXTRA, maxH + EXTRA);
	}

	public static void setupBounding(List<State> slist) {
		int boundX = 1000;
		int boundY = 1000;
		/* compute minimum boundX and boundY */
		for (int i = 0; i < slist.size(); i++) {
			if (slist.get(i).getLocation().x < boundX)
				boundX = slist.get(i).getLocation().x;
			if (slist.get(i).getLocation().y < boundY)
				boundY = slist.get(i).getLocation().y;
		}
		/* relocation ss */
		for (int i = 0; i < slist.size(); i++) {
			slist.get(i).setLocation(
					slist.get(i).getLocation().x - boundX + BOUND_SIZE,
					slist.get(i).getLocation().y - boundY + BOUND_SIZE);
		}
	}

	static final int ORIGIN = 0;
	static final int LINE_WIDTH = 1;
	static final int LABEL_WIDTH = 17;

	public void layoutAndState(List<AbstractSuperState> andList, Dimension area) {
		if (area.height > area.width)
			layoutHorizontal(andList, area);
		else
			layoutVertical(andList, area);
	}

	private void layoutHorizontal(List<AbstractSuperState> andList, Dimension area) {

		int andNum = andList.size();
		for (int idx = 0; idx < andNum; idx++) {
			andList.get(idx).setCollapsed(false);
			if (idx == 0) {
				andList.get(idx).setLocation(ORIGIN,
						((area.height / andNum) * idx));
				andList.get(idx).setDimension(area.width,
						(area.height / andNum) - LABEL_WIDTH);
			} else {
				andList.get(idx).setLocation(ORIGIN,
						((area.height / andNum) * idx) - LABEL_WIDTH);

				andList.get(idx).setDimension(area.width,
						(area.height / andNum));
			}
		}
	}

	private void layoutVertical(List<AbstractSuperState> andList, Dimension area) {
		int andNum = andList.size();
		for (int v = 0; v < andNum; v++) {
			andList.get(v).setLocation(
					((area.width / andNum) * v) - LINE_WIDTH, ORIGIN);
			andList.get(v).setDimension((area.width / andNum),
					(area.height) - 17);
			andList.get(v).setCollapsed(false);
		}
	}
}
