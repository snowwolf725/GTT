/**
 * 
 */
package gttlipse.scriptEditor.testScript.visitor;

import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

/**
 * @author ¤ýºaÄQ
 * 
 * created first in project GTTlipse.scriptEditor.TestScript.visitor
 * 
 */
public interface IGTTlipseScriptVisitor extends ITestScriptVisitor {
	public void visit(ProjectNode node);
	
	public void visit(SourceFolderNode node);

	public void visit(PackageNode node);

	public void visit(TestCaseNode node);

	public void visit(TestMethodNode node);

}
