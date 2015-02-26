/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.ProxySuperState;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class VFSMPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = create(model);
		part.setModel(model);
		return part;
	}

	private EditPart create(Object model) {
		if (model instanceof Diagram)
			return new DiagramPart();
		if (model instanceof Connection)
			return new ConnectionPart();
		if (model instanceof Initial)
			return new InitialPart();
		if (model instanceof Final)
			return new FinalPart();
		if (model instanceof ProxySuperState)
			return new ProxySuperStatePart();
		if (model instanceof AndSuperState)
			return new AndSuperStatePart();
		if (model instanceof AbstractSuperState)
			return new SuperStatePart();
		
		// 至少也會是個 StatePart
		return new StatePart();
	}
}