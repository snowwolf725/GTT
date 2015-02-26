/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.parts.policies.DiagramLayoutEditPolicy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;


/**
 * @author zhanghao
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	protected List<State> getModelChildren() {
		return ((Diagram) this.getModel()).getMainState().getAll();
	}

	public void activate() {
		super.activate();
		((Diagram) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((Diagram) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		/*
		 * add by jason in 2008/04/11 if property change equal to VFSMDef type,
		 * then refresh children.
		 */
		if (prop == VFSMDef.PROP_NODE || prop == VFSMDef.PROP_INITIAL
				|| prop == VFSMDef.PROP_STATE
				|| prop == VFSMDef.PROP_SUPERSTATE
				|| prop == VFSMDef.PROP_FINAL) {
			// List children = getChildren();
			refreshChildren();
		}
	}

	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutEditPolicy());
	}

	LayoutManager graphLayout = new GraphLayout(this);
	LayoutManager freeformLayout = new FreeformLayout();

	protected void refreshVisulas() {
//		Diagram diagram = (Diagram) getModel();
//		if (diagram.isAutoLayout()) {
//			getFigure().setLayoutManager(graphLayout);
//		} else {
			getFigure().setLayoutManager(freeformLayout);
//		}
	}

	protected void applyGraphResults(DirectedGraph graph, Map<?, ?> map) {
		for (int i = 0; i < getChildren().size(); i++) {
			StatePart node = (StatePart) getChildren().get(i);
			Node n = (Node) map.get(node);
			node.getFigure().setBounds(
					new Rectangle(n.x, n.y, n.width, n.height));
		}
	}

	@SuppressWarnings("unchecked")
	public void contributeNodesToGraph(DirectedGraph graph, Map<StatePart, Node> map) {
		for (int i = 0; i < getChildren().size(); i++) {
			StatePart node = (StatePart) getChildren().get(i);
			org.eclipse.draw2d.graph.Node n = new org.eclipse.draw2d.graph.Node(
					node);
			n.width = node.getFigure().getPreferredSize().width;
			n.height = node.getFigure().getPreferredSize().height;
			n.setPadding(new Insets(10, 8, 10, 12));
			map.put(node, n);
			graph.nodes.add(n);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void contributeEdgesToGraph(DirectedGraph graph, Map map) {
		for (int i = 0; i < getChildren().size(); i++) {
			StatePart node = (StatePart) children.get(i);
			List<ConnectionPart> outgoing = node.getSourceConnections();
			for (int j = 0; j < outgoing.size(); j++) {
				ConnectionPart conn = outgoing.get(j);
				Node source = (Node) map.get(conn.getSource());
				Node target = (Node) map.get(conn.getTarget());
				if (source == null)
					continue;
				if (target == null)
					continue;

				Edge e = new Edge(conn, source, target);
				e.weight = 2;
				graph.edges.add(e);
				map.put(conn, e);
			}
		}
	}


}