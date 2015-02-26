package gttlipse.vfsmEditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class OpenOutlineViewAction extends Action {

//	public OpenOutlineViewAction(GraphicalViewer viewer) {
//		super(null, viewer);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void update(IStructuredSelection sel) {
//		// TODO Auto-generated method stub
//	}
	public OpenOutlineViewAction() {
		
	}
	
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		try{
			window.getActivePage().showView("org.eclipse.ui.views.ContentOutline");
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
