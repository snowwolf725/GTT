package gttlipse.vfsmEditor.view;

import gttlipse.GTTlipse;

import org.eclipse.jface.resource.ImageRegistry;


public interface IVFSMEditor {

	public final static ImageRegistry IMAGE_REGISTRY = GTTlipse.getDefault()
			.getImageRegistry();

	public abstract void setPartName(String name);

}