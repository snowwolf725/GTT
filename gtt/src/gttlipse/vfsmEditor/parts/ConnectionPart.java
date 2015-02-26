/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gttlipse.GTTlipse;
import gttlipse.vfsmCoverageAnalyser.view.CoverageView;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.ConnectionBendpoint;
import gttlipse.vfsmEditor.parts.figures.ConnectionFigure;
import gttlipse.vfsmEditor.parts.figures.MidpointOffsetLocator;
import gttlipse.vfsmEditor.parts.policies.ConnectionBendPointEditPolicy;
import gttlipse.vfsmEditor.parts.policies.ConnectionEditPolicy;
import gttlipse.vfsmEditor.view.dialog.MacroEventNodeSettingDialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.MouseMotionListener.Stub;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;


public class ConnectionPart extends AbstractConnectionEditPart implements
		PropertyChangeListener {

	private boolean isSelected = false;
	private ConnectionFigure m_Figure;

	private BorderAnchor m_sourceAnchor;
	private BorderAnchor m_targetAnchor;
	protected int m_anchorX;
	protected int m_anchorY;
	protected int m_dx;
	protected int m_dy;

	protected DirectEditManager manager;

	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
			if (manager == null) {
				ConnectionFigure figure = (ConnectionFigure) getFigure();
				manager = new ConnectionDirectEditManager(this,
						TextCellEditor.class, new ConnectionCellEditorLocator(
								figure));
			}
			manager.show();
		}
	}

	private Connection getConnectionModel() {
		return ((Connection) getModel());
	}

	public void activate() {
		super.activate();
		getConnectionModel().addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		getConnectionModel().removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (Connection.PROP_BENDPOINT.equals(property))
			refreshBendpoints();
	}

	protected IFigure createFigure() {
		m_Figure = new ConnectionFigure();

		initRouter();

		m_Figure.add(m_Figure.getLabel(),
				new MidpointOffsetLocator(m_Figure, 0));
		m_Figure.getLabel().addMouseListener(createLabelMouseListener());
		m_Figure.getLabel().addMouseMotionListener(
				createLabelMouseMotionListener());

		updateEventVisual();
		return m_Figure;
	}

	private Stub createLabelMouseMotionListener() {
		return new MouseMotionListener.Stub() {
			@Override
			public void mouseDragged(MouseEvent me) {
				// TODO Auto-generated method stub
				m_dx += me.x - m_anchorX;
				m_dy += me.y - m_anchorY;
				m_anchorX = me.x;
				m_anchorY = me.y;
				Object constraint = m_Figure.getLayoutManager().getConstraint(
						m_Figure.getLabel());
				if (constraint instanceof MidpointOffsetLocator) {
					((MidpointOffsetLocator) constraint).setOffset(new Point(
							m_dx, m_dy));
					m_Figure.getLabel().revalidate();
				}
				me.consume();
			}
		};
	}

	private MouseListener createLabelMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				Connection conn = (Connection) getModel();
				MacroEventNodeSettingDialog editDialog = new MacroEventNodeSettingDialog(
						null, conn.getEventList());
				editDialog.open();

				if (editDialog.getReturnCode() == SWT.OK) {
					conn.setEventList(editDialog.getSelectEventList());
					refreshVisuals();
				}
			}

			@Override
			public void mousePressed(MouseEvent me) {
				m_anchorX = me.x;
				m_anchorY = me.y;
				me.consume();
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				me.consume();
			}
		};
	}

	private void initRouter() {
		switch (getConnectionModel().getRouterId()) {
		case VFSMDef.NULL_ROUTER:
			break;
		case VFSMDef.BENDPOINT_CONNECTION_ROUTER:
			m_Figure.setConnectionRouter(new BendpointConnectionRouter());
			break;
		case VFSMDef.MANHATTAN_ROUTER:
			m_Figure.setConnectionRouter(new ManhattanConnectionRouter());
			break;
		case VFSMDef.SHORTEST_PATH_ROUTER:
			m_Figure.setConnectionRouter(new ShortestPathConnectionRouter(
					m_Figure));
			break;
		case VFSMDef.SINGLE_BENDPOINT_ROUTER:
			break;
		default:
			break;
		}
	}

	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
//				new ConnectionDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy());
		switch (getConnectionModel().getRouterId()) {
		case VFSMDef.NULL_ROUTER:
			installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
					new ConnectionEndpointEditPolicy());
			break;
		case VFSMDef.BENDPOINT_CONNECTION_ROUTER:
			installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
					new ConnectionBendPointEditPolicy());
			break;
		case VFSMDef.MANHATTAN_ROUTER:
			installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
					new ConnectionEndpointEditPolicy());
			break;
		case VFSMDef.SHORTEST_PATH_ROUTER:
			installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
					new ConnectionEndpointEditPolicy());
			break;
		case VFSMDef.SINGLE_BENDPOINT_ROUTER:
			installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
					new ConnectionEndpointEditPolicy());
			break;
		default:
			installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
					new ConnectionEndpointEditPolicy());
			break;
		}
	}

	protected void refreshVisuals() {
		refreshBendpoints();
		
		if(((Connection)getConnectionModel()).getIsMarkedErrorPart() == true) {
			((PolylineConnection) getFigure()).setLineWidth(1);
			((PolylineConnection) getFigure())
					.setForegroundColor(new Color(null, 0, 254, 0));
		}
		else if(((Connection)getConnectionModel()).getIsSelectedCovered() == true) {
			((PolylineConnection) getFigure()).setLineWidth(2);
			((PolylineConnection) getFigure())
					.setForegroundColor(((Connection)getConnectionModel()).getCoveredColor());
		}
		else {
			((PolylineConnection) getFigure()).setLineWidth(1);
			((PolylineConnection) getFigure())
				.setForegroundColor(ColorConstants.darkBlue);
		}
		updateEventVisual();
	}

	private void updateEventVisual() {
		if (isSelected == true)
			beingSelected();
		else {
			if(getConnectionModel() instanceof Connection)
				if(((Connection)getConnectionModel()).getIsSelectedCovered() == true)
					beingCovered();
				else
					beingNotSelected();
			else
				beingNotSelected();
		}
	}

	private void checkInitalState() {
		/* initial is a fake state, do not have output event */
		ConnectionBase conn = getConnectionModel();
		if (conn.getSource().getStateType() == VFSMDef.TYPE_INITIAL)
			m_Figure.getLabel().setText("");
	}

	private void beingNotSelected() {
		// 沒有被選中，應該要簡化顯示
		ConnectionBase conn = getConnectionModel();
		m_Figure.getLabel().setForegroundColor(ColorConstants.black);
		m_Figure.getLabel().setText(conn.getEvent().length() > 10?conn.getEvent().substring(0, 9)+"...":conn.getEvent());
		
//		if (conn.getEventList().size() > 1)	// 2 個以上的事件
//			m_Figure.getLabel().setText("event(s)");
//		else if (conn.getEventList().size() ==1)	// 1 個事件
//			m_Figure.getLabel().setText(conn.getEvent());
//		else	// 沒有事件
//			m_Figure.getLabel().setText("null");
		checkInitalState();
	}
	
	private CoverageView getGTTCoverageView() throws PartInitException {
		return (CoverageView) GTTlipse.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().showView(
						"GTTlipse.views.GTTCoverageView");
	}
	
	private void beingCovered() {
		ConnectionBase conn = getConnectionModel();
		m_Figure.getLabel().setForegroundColor(((Connection)conn).getCoveredColor());
		
		if (conn.getEventList().size() > 1)
			m_Figure.getLabel().setText("event(s)");
		else if (conn.getEventList().size() ==1)	// 1 個事件
			m_Figure.getLabel().setText(conn.getEvent());
		else	// 沒有事件
			m_Figure.getLabel().setText("null");
		checkInitalState();
	}
	
	private void showCoveredSituation(){
		try {
			Tree coverageTree = getGTTCoverageView().getCoverageTree();
			Connection conn = (Connection)getConnectionModel();
			
			HashMap<AbstractMacroNode, Boolean> coveredSituation = conn.getCoveredSituation();
			Set<AbstractMacroNode> eventList = coveredSituation.keySet();
			Iterator<AbstractMacroNode> iter = eventList.iterator();
			
			coverageTree.removeAll();
			
			while(iter.hasNext()) {
				ComponentEventNode node = (ComponentEventNode)iter.next();
				TreeItem treeItem = new TreeItem(coverageTree, SWT.NONE);
			
				treeItem.setText(node.getEventType() + "(" + node.getName() + ")");
				
				if(coveredSituation.get(node).equals(true)) {
					treeItem.setForeground(new Color(null, 0, 255, 0));
				}
				else {
					treeItem.setForeground(new Color(null, 255, 0, 0));
				}
			}
			
			coverageTree.redraw();
			
			
			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void beingSelected() {
		if(((Connection)getConnectionModel()).getIsSelectedCovered() == true) {
			showCoveredSituation();
		}
		ConnectionBase conn = getConnectionModel();
//		m_Figure.getLabel().setForegroundColor(ColorConstants.red);
		m_Figure.getLabel().setForegroundColor(ColorConstants.white);
		m_Figure.getLabel().setText(conn.getEvent());
		checkInitalState();
	}

	protected void refreshBendpoints() {
		Connection conn = getConnectionModel();

		List<ConnectionBendpoint> modelConstraint = conn.getBendpoints();
		List<RelativeBendpoint> figureConstraint = new ArrayList<RelativeBendpoint>();

		for (int i = 0; i < modelConstraint.size(); i++) {
			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
			ConnectionBendpoint cbp = modelConstraint.get(i);
			rbp.setRelativeDimensions(cbp.firstRelativeDim(), cbp
					.secondRelativeDim());
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}

		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}

	public void setSelected(int value) {
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE) {
			// if the connection is selected, increase line width and change
			// color to XredX ---> white. by Charles Ku
			if(((Connection)getConnectionModel()).getIsSelectedCovered() == true)
				((PolylineConnection) getFigure()).setLineWidth(3);
			else 
				((PolylineConnection) getFigure()).setLineWidth(2);
			((PolylineConnection) getFigure())
					.setForegroundColor(ColorConstants.white);

			isSelected = true;
		} else {
			// if not selected, reduce line width to 1 and change color back to
			// black.
			if(((Connection)getConnectionModel()).getIsMarkedErrorPart() == true) {
				((PolylineConnection) getFigure()).setLineWidth(1);
				((PolylineConnection) getFigure())
						.setForegroundColor(new Color(null, 0, 254, 0));
			}
			else if(((Connection)getConnectionModel()).getIsSelectedCovered() == true) {
				((PolylineConnection) getFigure()).setLineWidth(2);
				((PolylineConnection) getFigure())
						.setForegroundColor(((Connection)getConnectionModel()).getCoveredColor());
			}
			else {
				((PolylineConnection) getFigure()).setLineWidth(1);
				((PolylineConnection) getFigure())
						.setForegroundColor(ColorConstants.darkBlue);
			}
			isSelected = false;
		}
		updateEventVisual();
	}

	public BorderAnchor getSourceAnchor() {
		return m_sourceAnchor;
	}

	public void setSourceAnchor(BorderAnchor sourceAnchor) {
		m_sourceAnchor = sourceAnchor;
	}

	public BorderAnchor getTargetAnchor() {
		return m_targetAnchor;
	}

	public void setTargetAnchor(BorderAnchor targetAnchor) {
		m_targetAnchor = targetAnchor;
	}
}