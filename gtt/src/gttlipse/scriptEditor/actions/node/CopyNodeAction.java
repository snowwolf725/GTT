/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.util.ScrapBook;

import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class CopyNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public CopyNodeAction() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		Object [] objs = new Object[selection.size()];
		Object [] source = selection.toArray();
		for(int i=0;i<objs.length;i++){
			if(source[i] instanceof BaseNode){
				BaseNode node = (BaseNode)source[i];
				objs[i] = node.clone();
			}else if(source[i] instanceof TestScriptDocument){
				TestScriptDocument doc = (TestScriptDocument)source[i];
				objs[i] = doc.clone();
			}else if(source[i] instanceof AbstractNode){
				AbstractNode node = (AbstractNode)source[i];
				objs[i] = node.clone();
			}
		}
		ScrapBook.copy(objs);
	}
}
