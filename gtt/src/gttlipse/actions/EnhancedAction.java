/**
 * 
 */
package gttlipse.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class EnhancedAction extends Action {
	protected TreeViewer m_TreeViewer;

	public EnhancedAction() {
		super();
	}

	public EnhancedAction(String text) {
		super(text);
	}

	public EnhancedAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public EnhancedAction(String text, int style) {
		super(text, style);
	}

	public void setViewer(TreeViewer viewer) {
		this.m_TreeViewer = viewer;
	}
	
}
