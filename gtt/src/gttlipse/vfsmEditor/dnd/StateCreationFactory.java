/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.dnd;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.SuperState;

import org.eclipse.gef.requests.CreationFactory;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class StateCreationFactory implements CreationFactory {

	private Object template;

	public StateCreationFactory(Object template) {
		this.template = template;
	}

	public Object getNewObject() {
		try {
			/* if template is a SuperState, than recursive create superstate. */
			if (template instanceof SuperState
					&& ((SuperState) template).getName() != VFSMDef.FSM_MAIN) {
				ProxySuperState pss = new ProxySuperState((SuperState) template);
				return pss;
			}

			return ((Class<?>) template).newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public Object getObjectType() {
		return template;
	}
}