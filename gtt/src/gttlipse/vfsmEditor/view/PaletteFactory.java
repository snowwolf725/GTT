/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.view;

import gttlipse.GTTlipse;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.State;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;


/**
 * @author zhanghao
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public final class PaletteFactory {
	/** Preference ID used to persist the palette location. */
	private static final String PALETTE_DOCK_LOCATION = "ShapesEditorPaletteFactory.Location";
	/** Preference ID used to persist the palette size. */
	private static final String PALETTE_SIZE = "ShapesEditorPaletteFactory.Size";
	/** Preference ID used to persist the flyout palette's state. */
	private static final String PALETTE_STATE = "ShapesEditorPaletteFactory.State";

	private static ImageRegistry image_registry = IVFSMEditor.IMAGE_REGISTRY;

	private PaletteFactory() {
	}

	// factory method
	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createControlGroup(palette));
		return palette;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group");
		ToolEntry tool = new PanningSelectionToolEntry();
		root.setDefaultEntry(tool);

		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		entries.add(tool);

		ToolEntry tool_connection = new ConnectionCreationToolEntry("Transition",
				"Create a transition", null, image_registry
						.getDescriptor("transition"), image_registry
						.getDescriptor("transition"));
		ToolEntry tool_initial = new CombinedTemplateCreationEntry("Initial",
				"Create a new Initial", Initial.class, new SimpleFactory(
						Initial.class),
				image_registry.getDescriptor("initial"), image_registry
						.getDescriptor("initial"));

		ToolEntry tool_final = new CombinedTemplateCreationEntry("Final",
				"Create a new Final", Final.class, new SimpleFactory(
						Final.class), image_registry.getDescriptor("final"),
				image_registry.getDescriptor("final"));

		ToolEntry tool_state = new CombinedTemplateCreationEntry("State",
				"Create a new State", State.class, new SimpleFactory(
						State.class), image_registry.getDescriptor("state"),
				image_registry.getDescriptor("state"));

//		ToolEntry tool_superstate = new CombinedTemplateCreationEntry("XOR",
//				"Create a new XOR SuperState", SuperState.class,
//				new SimpleFactory(SuperState.class), image_registry
//						.getDescriptor("superstate"), image_registry
//						.getDescriptor("superstate"));

		ToolEntry tool_andsuperstate = new CombinedTemplateCreationEntry("AND",
				"Create a new AND SuperState", AndSuperState.class,
				new SimpleFactory(AndSuperState.class), image_registry
						.getDescriptor("andsuperstate"), image_registry
						.getDescriptor("andsuperstate"));

		entries.add(tool_connection);
		entries.add(tool_initial);
		entries.add(tool_final);
		entries.add(tool_state);
		// entries.add(tool_superstate);
		entries.add(tool_andsuperstate);

		/* Add a marquee tool to the group */
		controlGroup.add(new MarqueeToolEntry());
		/* Add a (unnamed) separator to the group */
		controlGroup.add(new PaletteSeparator());
		controlGroup.addAll(entries);
		return controlGroup;
	}

//	private static PaletteContainer createComponentsDrawer() {
//		PaletteDrawer drawer = new PaletteDrawer("Components");
//
//		List<ToolEntry> entries = new ArrayList<ToolEntry>();
//		ImageRegistry image_registry = GTTlipse.getDefault().getImageRegistry();
//
//		ToolEntry tool_initial = new CombinedTemplateCreationEntry("Initial",
//				"Create a new Initial", Initial.class, new SimpleFactory(
//						Initial.class),
//				image_registry.getDescriptor("initial"), image_registry
//						.getDescriptor("initial"));
//		ToolEntry tool_state = new CombinedTemplateCreationEntry("State",
//				"Create a new State", State.class, new SimpleFactory(
//						State.class), image_registry.getDescriptor("state"),
//				image_registry.getDescriptor("state"));
//		ToolEntry tool_superstate = new CombinedTemplateCreationEntry("XOR",
//				"Create a new XOR SuperState", SuperState.class,
//				new SimpleFactory(SuperState.class), image_registry
//						.getDescriptor("superstate"), image_registry
//						.getDescriptor("superstate"));
//		ToolEntry tool_andsuperstate = new CombinedTemplateCreationEntry("AND",
//				"Create a new AND SuperState", AndSuperState.class,
//				new SimpleFactory(AndSuperState.class), image_registry
//						.getDescriptor("andsuperstate"), image_registry
//						.getDescriptor("andsuperstate"));
//		ToolEntry tool_final = new CombinedTemplateCreationEntry("Final",
//				"Create a new Final", Final.class, new SimpleFactory(
//						Final.class), image_registry.getDescriptor("final"),
//				image_registry.getDescriptor("final"));
//		entries.add(tool_initial);
//		entries.add(tool_final);
//		entries.add(tool_state);
//		entries.add(tool_superstate);
//		entries.add(tool_andsuperstate);
//		drawer.addAll(entries);
//		return drawer;
//	}

	public static FlyoutPreferences createPalettePreferences() {
		return new FlyoutPreferences() {
			private IPreferenceStore getPreferenceStore() {
				return GTTlipse.getDefault().getPreferenceStore();
			}

			public int getDockLocation() {
				return getPreferenceStore().getInt(PALETTE_DOCK_LOCATION);
			}

			public int getPaletteState() {
				return getPreferenceStore().getInt(PALETTE_STATE);
			}

			public int getPaletteWidth() {
				return getPreferenceStore().getInt(PALETTE_SIZE);
			}

			public void setDockLocation(int location) {
				getPreferenceStore().setValue(PALETTE_DOCK_LOCATION, location);
			}

			public void setPaletteState(int state) {
				getPreferenceStore().setValue(PALETTE_STATE, state);
			}

			public void setPaletteWidth(int width) {
				getPreferenceStore().setValue(PALETTE_SIZE, width);
			}
		};
	}
}