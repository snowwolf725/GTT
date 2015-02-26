package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.builder.GraphBuilder;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

public class GraphBuilderTest extends TestCase {

	GraphBuilder builder;
	protected void setUp() throws Exception {
		super.setUp();
		builder = new GraphBuilder();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBuild() {
		SuperState ss = new SuperState();
		State s = new State();
		ss.addState(s);
		builder.build(ss);
		assertEquals( 1, builder.getM_graph().vertices().size() );
	}

	public void testTraceVertices_IitialFinal() {
		SuperState ss = new SuperState();
		Initial i = new Initial();
		Final f = new Final();
		
		ss.addState(i);
		ss.addState(f);
		
		builder.traceVertices(ss);
		assertEquals( 1, builder.getM_graph().vertices().size() );
	}
	
	public void testTraceVertices_State() {
		SuperState ss = new SuperState();
		State s1 = new State();
		State s2 = new State();
		
		ss.addState(s1);
		ss.addState(s2);
		
		builder.traceVertices(ss);
		assertEquals( 2, builder.getM_graph().vertices().size() );
	}
	
	public void testTraceVertices_AbstracetSuperState() {
		SuperState ss = new SuperState();
		AbstractSuperState ass = new SuperState();
		Initial i = new Initial();
		State s1 = new State();
		State s2 = new State();
		ass.addState(i);
		ass.addState(s1);
		ass.addState(s2);
		Connection.create(i, s1);
		Connection.create(s1, s2);
		
		ss.addState(ass);
		builder.traceVertices(ss);
		assertEquals( 2, builder.getM_graph().vertices().size() );
	}
	
	public void testTraceVertices_AbstractSuperState_noInitial() {
		SuperState ss = new SuperState();
		AbstractSuperState ass2 = new SuperState();
		State ss1 = new State();
		State ss2 = new State();
		ass2.addState(ss1);
		ass2.addState(ss2);
		
		Connection.create(ss1, ss2);
		ss.addState(ass2);
		builder.traceVertices(ss);
		assertEquals( 2, builder.getM_graph().vertices().size() );
	}
	
	public void testTraceVertices_AndSuperState() {
		SuperState ss = new SuperState();
		AndSuperState and = new AndSuperState();
		State s1 = new State();
		State s2 = new State();
		Connection.create(s1, s2);
		and.addState(s1);
		and.addState(s2);
		
		ss.addState(and);
		builder.traceVertices(ss);
//		assertEquals( 3, builder.getM_graph().vertices().size() );
	}

	public void testTraceEdges() {
//		fail("Not yet implemented");
	}

	public void testPrintresult() {
//		fail("Not yet implemented");
	}

}
