package gttlipse.vfsmEditor.builder;

import gtt.testscript.AbstractNode;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;


public class RecursiveCopySuperState {
	final int BOUND_SIZE = 2;
	final int LABEL_SIZE = 20;

	public State parse(State node) {
		return recNodes(node);
	}
	
	/* using recursive method to dig nested superstate */
    public State recNodes(State node){ 
    	if ( node instanceof AndSuperState ) {
    		return recCreateAnd(node);
    	}
    	else if ( node instanceof AbstractSuperState ) {
    		return recCreateSS(node);
    	}
    	else if ( node instanceof Initial ) {
    		Initial i = new Initial();
    		i.setName( new String( node.getName() ) );
    		i.setLocation( new Point( node.getLocation().x, node.getLocation().y ) );
    		i.setDimension( new Dimension( node.getDimension().width, node.getDimension().height ) );
    		return i;
    	}
    	else if ( node instanceof Final ) {
    		Final f = new Final();
    		f.setName( new String( node.getName() ) );
    		f.setLocation( new Point( node.getLocation().x, node.getLocation().y ) );
    		f.setDimension( new Dimension( node.getDimension().width, node.getDimension().height ) );
    		return f;
    	}
    	else if ( node instanceof State ) {
    		State s = new State();
    		s.setName( new String( node.getName() ) );
    		s.setLocation( new Point( node.getLocation().x, node.getLocation().y ) );
    		s.setDimension( new Dimension( node.getDimension().width, node.getDimension().height ) );
    		return s;
    	}
    	return null;
    }
    /* recursive create AND SuperState */
    private State recCreateAnd(State node) {
    	AndSuperState ass = (AndSuperState)node;
		AndSuperState rec = new AndSuperState();
		rec.setName( new String( ass.getName() ) );
		rec.setLocation( new Point( ass.getLocation() ) );
		rec.setDimension( new Dimension( ass.getDimension().width, ass.getDimension().height ) );
		rec.setCollapsed( new Boolean(ass.getCollapsed() ) );
		/* recursive create sub superstate */
		for ( int i = 0; i < ass.getAll().size(); i ++) {
			if ( ass.getAll().get(i) instanceof AndSuperState ) {
				rec.addState( recNodes( ass.getAll().get(i) ) );
			}
			else 
				rec.addState( (recNodes(ass.getAll().get(i))) );
		} 
		/* connect connection */
		createConn( ass, rec );
		return rec;
    }
    /* recursive create SuperState */
    private State recCreateSS(State node) {
    	AbstractSuperState xss = (AbstractSuperState)node;
		AbstractSuperState rec = new SuperState();
		rec.setName( new String( xss.getName() ) );
		rec.setLocation( new Point( xss.getLocation() ) );
		rec.setDimension(new Dimension(xss.getDimension() ) );
		rec.setCollapsed( xss.getCollapsed());
		
		List<AndSuperState> andList = getAndList(xss);
		int andNum = getAndList(xss).size();
		int maxW = 80;
		int maxH = 20;
		/* recursive create superstate */
		for ( int i = 0; i < xss.getAll().size(); i ++) {
			if ( xss.getAll().get(i) instanceof AndSuperState) {
				if( getBoundingBox( (AbstractSuperState)xss.getAll().get(i) ).width > maxW )
					maxW = getBoundingBox( (AbstractSuperState)xss.getAll().get(i)).width;
				if( getBoundingBox( (AbstractSuperState)xss.getAll().get(i) ).height > maxH )
					maxH = getBoundingBox( (AbstractSuperState)xss.getAll().get(i)).height;
				rec.addState( (AndSuperState)recNodes( xss.getAll().get(i) ) );
			}
			else 
				rec.addState((recNodes(xss.getAll().get(i))));
		} 
		for ( int i = 0; i < andNum; i++ ) {
			andList.get(i).setDimension( maxW, maxH + LABEL_SIZE );
			andList.get(i).setLocation((maxW * i) + BOUND_SIZE , 0);
		}
		/* connect connection */
		createConn( xss, rec );
		rec.setDimension(maxW + (BOUND_SIZE * 2) , maxH + (BOUND_SIZE * 2) + LABEL_SIZE);
		return rec;
    }
    /* connect all nodes */
    private void createConn(AbstractSuperState ss, AbstractSuperState rec) {
    	for (int i = 0; i < ss.getAll().size(); i++) {
			for (int j = 0; j < ss.getAll().get(i).getIncomingConnections().size(); j++) {
				String sourceNodeName = new String( ss.getAll().get(i).getIncomingConnections().get(j).getSource().getName() );
				String targetNodeName = new String( ss.getAll().get(i).getIncomingConnections().get(j).getTarget().getName() );
				List<AbstractNode> eventnodelist = ss.getAll().get(i).getIncomingConnections().get(j).getEventList();
				if ((rec.getChildrenByName(sourceNodeName) != null) &&
					(rec.getChildrenByName(targetNodeName) != null)) {
				Connection conn = Connection.create(
						(rec.getChildrenByName(sourceNodeName)),
						(rec.getChildrenByName(targetNodeName)));
				conn.setEventList(eventnodelist);
				}
			}
		}
    }
    /* get all of AndSuperState from compositeNode */
	private List<AndSuperState> getAndList( AbstractSuperState ss) {
		List<AndSuperState> andList = new ArrayList<AndSuperState>();
		for(int i = 0; i < ss.size(); i++) {
			if (ss.getAll().get(i) instanceof AndSuperState) {
				andList.add((AndSuperState)ss.getAll().get(i));
			}
		}
		return andList;
	}
    /* boundingBox */
    private Dimension getBoundingBox(AbstractSuperState ss) {
    	int boundX = 1000;
    	int boundY = 1000;
    	/* compute minimum boundX and boundY */
    	for ( int i = 0; i < ss.size(); i++) {
    		if (ss.getAll().get(i).getLocation().x < boundX )
    			boundX = ss.getAll().get(i).getLocation().x;
    		if (ss.getAll().get(i).getLocation().y < boundY )
    			boundY = ss.getAll().get(i).getLocation().y;
    	}
    	/* relocation ss*/
    	for ( int i = 0; i < ss.size(); i++) {
    		ss.getAll().get(i).setLocation(
    				ss.getAll().get(i).getLocation().x - boundX + BOUND_SIZE,
    				ss.getAll().get(i).getLocation().y - boundY + BOUND_SIZE);
    	}
    	/* compute maximum size */
    	int maxX = 80;
    	int maxY = 20;
    	for ( int i = 0; i < ss.size(); i++) {
    		Node node = ss.getAll().get(i);
    		int tempX = node.getLocation().x + node.getDimension().width;
    		if ( tempX > maxX )
    			maxX = tempX;
    		int tempY = node.getLocation().y + node.getDimension().height;
    		if ( tempY > maxY )
    			maxY = tempY;
    	}
    	return new Dimension(maxX, maxY);
    }
}
