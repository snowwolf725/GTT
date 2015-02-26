package gttlipse.scriptEditor.actions;

import gtt.testscript.AbstractNode;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;


public class ClipBoard {

	private static ClipBoard instance = null;

	private State m_copyNode = null;
	private Dimension m_d = null;

	private ClipBoard() {

	}

	public static ClipBoard getInstance() {
		if (instance == null)
			instance = new ClipBoard();
		return instance;
	}

	public boolean isEmpty() {
		return (m_copyNode == null);
	}

	public void clearClipBoard() {
		m_copyNode = null;
	}

	public Node getCopyNode() {
		return m_copyNode;
	}

	public void copy(State node) {
		m_copyNode = recNodes(node);
	}

	public State cloneCopyNode() {
		return m_copyNode;
	}

	/* using recursive method to dig nested superstate */
	public State recNodes(State state) {
		/* recursive */
		if (state instanceof AndSuperState)
			return recCreateAnd(state);

		if (state instanceof AbstractSuperState)
			return recCreateSS(state);

		if (state instanceof Initial) {
			Initial st = new Initial();
			st.setName(new String(state.getName()));
			st.setLocation(new Point(state.getLocation()));
			st.setDimension(new Dimension(state.getDimension()));
			return st;
		}

		if (state instanceof Final) {
			Final st = new Final();
			st.setName(new String(state.getName()));
			st.setLocation(new Point(state.getLocation()));
			st.setDimension(new Dimension(state.getDimension()));
			return st;
		}

		// 至少也是一個 State
		State st = new State();
		st.setName(new String(state.getName()));
		st.setLocation(new Point(state.getLocation()));
		st.setDimension(new Dimension(state.getDimension()));
		return st;
	}

	/* recursive create AND SuperState */
	private State recCreateAnd(Node node) {
		AndSuperState ass = (AndSuperState) node;
		AndSuperState rec = new AndSuperState();
		rec.setName(new String(ass.getName()));
		rec.setLocation(new Point(ass.getLocation()));
		rec.setDimension(new Dimension(ass.getDimension().width, ass
				.getDimension().height));
		rec.setCollapsed(new Boolean(ass.getCollapsed()));
		/* recursive create sub superstate */
		for (int i = 0; i < ass.getAll().size(); i++) {
			if (ass.getAll().get(i) instanceof AndSuperState) {
				rec.addState(recNodes(ass.getAll().get(i)));
			} else
				rec.addState((recNodes(ass.getAll().get(i))));
		}
		/* connect connection */
		createConn(ass, rec);
		boundingBox(rec);
		return rec;
	}

	/* recursive create SuperState */
	private State recCreateSS(Node node) {
		AbstractSuperState xss = (AbstractSuperState) node;
		AbstractSuperState rec = new SuperState();
		rec.setName(new String(xss.getName()));
		rec.setLocation(new Point(xss.getLocation()));
		rec.setDimension(new Dimension(xss.getDimension().width, xss
				.getDimension().height));
		rec.setCollapsed(new Boolean(xss.getCollapsed()));
		/* recursive create superstate */
		for (int i = 0; i < xss.getAll().size(); i++) {
			if (xss.getAll().get(i) instanceof AndSuperState) {
				rec.addState(recNodes(xss.getAll().get(i)));
			} else
				rec.addState((recNodes(xss.getAll().get(i))));
		}
		/* connect connection */
		createConn(xss, rec);
		boundingBox(rec);
		return rec;
	}

	/* connect all nodes */
	private void createConn(AbstractSuperState ss, AbstractSuperState rec) {
		for (int i = 0; i < ss.getAll().size(); i++) {
			for (int j = 0; j < ss.getAll().get(i).getIncomingConnections()
					.size(); j++) {
				String sourceNodeName = new String(ss.getAll().get(i)
						.getIncomingConnections().get(j).getSource().getName());
				String targetNodeName = new String(ss.getAll().get(i)
						.getIncomingConnections().get(j).getTarget().getName());
				List<AbstractNode> eventnodelist = ss.getAll().get(i)
						.getIncomingConnections().get(j).getEventList();
				if ((rec.getChildrenByName(sourceNodeName) != null)
						&& (rec.getChildrenByName(targetNodeName) != null)) {
					Connection conn = Connection.create((rec
							.getChildrenByName(sourceNodeName)), (rec
							.getChildrenByName(targetNodeName)));
					conn.setEventList(eventnodelist);
				}
			}
		}
	}

	/* boundingBox */
	private void boundingBox(AbstractSuperState ss) {
		final int BOUND_SIZE = 2;
		final int LABEL_SIZE = 20;
		int boundX = 1000;
		int boundY = 1000;
		/* compute minimum boundX and boundY */
		for (int i = 0; i < ss.size(); i++) {
			if (ss.getAll().get(i).getLocation().x < boundX)
				boundX = ss.getAll().get(i).getLocation().x;
			if (ss.getAll().get(i).getLocation().y < boundY)
				boundY = ss.getAll().get(i).getLocation().y;
		}
		/* relocation ss */
		for (int i = 0; i < ss.size(); i++) {
			ss.getAll().get(i).setLocation(
					ss.getAll().get(i).getLocation().x - boundX + BOUND_SIZE,
					ss.getAll().get(i).getLocation().y - boundY + BOUND_SIZE);
		}
		/* compute maximum size */
		int maxX = 80;
		int maxY = 20;
		for (int i = 0; i < ss.size(); i++) {
			Node node = ss.getAll().get(i);
			int tempX = node.getLocation().x + node.getDimension().width;
			if (tempX > maxX)
				maxX = tempX;
			int tempY = node.getLocation().y + node.getDimension().height;
			if (tempY > maxY)
				maxY = tempY;
		}
		setBestDimension(new Dimension(maxX + (BOUND_SIZE * 2), maxY
				+ (BOUND_SIZE * 2) + LABEL_SIZE));
	}

	public void setBestDimension(Dimension d) {
		m_d = d;
	}

	public Dimension getBestDimension() {
		return m_d;
	}
}
