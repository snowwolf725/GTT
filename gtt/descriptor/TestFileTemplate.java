import junit.framework.TestCase;
import gttlipse.scriptEditor.interpreter.Interpreter;

public class TestFileTemplate extends TestCase {
	private Interpreter runner = new Interpreter(this);

	public String getMethodName(){
		StackTraceElement e[] = Thread.currentThread().getStackTrace();
		String method = e[2].getMethodName();
		return method;
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
