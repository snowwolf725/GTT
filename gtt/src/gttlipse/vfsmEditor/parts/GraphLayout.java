package gttlipse.vfsmEditor.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Node;

public class GraphLayout extends AbstractLayout {

	private DiagramPart m_diagramPart;

	GraphLayout(DiagramPart diagram) {
		m_diagramPart = diagram;
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int hint,
			int hint2) {
		container.validate();
		List<?> children = container.getChildren();
		Rectangle result = new Rectangle().setLocation(container
				.getClientArea().getLocation());
		for (int i = 0; i < children.size(); i++) {
			result.union(((IFigure) children.get(i)).getBounds());
			result.resize(container.getInsets().getWidth(), container
					.getInsets().getHeight());
		}
		return result.getSize();
	}

	@Override
	public void layout(IFigure container) {
		DirectedGraph graph = new DirectedGraph();
		Map<StatePart, Node> partsToNodes = new HashMap<StatePart, Node>();
		m_diagramPart.contributeNodesToGraph(graph, partsToNodes);
		m_diagramPart.contributeEdgesToGraph(graph, partsToNodes);

		new DirectedGraphLayout().visit(graph);
		m_diagramPart.applyGraphResults(graph, partsToNodes);
	}
}
