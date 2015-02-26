package gttlipse.macro.view;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.ViewAssertNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

class ToolTipHandler {
	private Shell parentShell;
	private Shell tipShell;
	private Label tipLabelImage, tipLabelText;
	private Widget tipWidget; // widget this tooltip is hovering over
	private Point tipPosition; // the position being hovered over

	public ToolTipHandler(Shell parent) {
		final Display display = parent.getDisplay();
		this.parentShell = parent;

		tipShell = new Shell(parent, SWT.ON_TOP | SWT.TOOL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 2;
		gridLayout.marginHeight = 2;
		tipShell.setLayout(gridLayout);

		tipShell.setBackground(display
				.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		tipLabelImage = new Label(tipShell, SWT.NONE);
		tipLabelImage.setForeground(display
				.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLabelImage.setBackground(display
				.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLabelImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_CENTER));

		tipLabelText = new Label(tipShell, SWT.NONE);
		tipLabelText.setForeground(display
				.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLabelText.setBackground(display
				.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_CENTER));
	}

	/**
	 * Enables customized hover help for a specified control
	 * 
	 * @control the control on which to enable hoverhelp
	 */
	public void activateHoverHelp(final Control control) {
		/*
		 * Get out of the way if we attempt to activate the control underneath
		 * the tooltip
		 */
		control.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (tipShell.isVisible())
					tipShell.setVisible(false);
			}
		});

		/*
		 * Trap hover events to pop-up tooltip
		 */
		control.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {
				if (tipShell.isVisible())
					tipShell.setVisible(false);
				tipWidget = null;
			}

			public void mouseHover(MouseEvent event) {
				Point pt = new Point(event.x, event.y);
				Widget widget = event.widget;
				if (widget instanceof ToolBar) {
					ToolBar w = (ToolBar) widget;
					widget = w.getItem(pt);
				}
				if (widget instanceof Table) {
					Table w = (Table) widget;
					widget = w.getItem(pt);
				}
				if (widget instanceof Tree) {
					Tree w = (Tree) widget;
					widget = w.getItem(pt);
				}
				if (widget == null) {
					tipShell.setVisible(false);
					tipWidget = null;
					return;
				}
				if (widget == tipWidget)
					return;
				tipWidget = widget;
				tipPosition = control.toDisplay(pt);
				// String text = (String) widget.getData("TIP_TEXT");
				String text = (String) widget.toString();
				if (widget instanceof TreeItem) {
					TreeItem item = (TreeItem) widget;
					if (item.getData() instanceof AbstractMacroNode) {
						AbstractMacroNode node = (AbstractMacroNode) item
								.getData();
						text = node.getPath().toString();

						if (node instanceof MacroEventCallerNode) {
							text = ((MacroEventCallerNode) node)
									.getReferencePath();
						}
						if (node instanceof ComponentEventNode) {
							text = ((ComponentEventNode) node)
									.getComponentPath();
						}
						if (node instanceof ViewAssertNode) {
							text = ((ViewAssertNode) node).getComponentPath();
						}
					}
				}
				Image image = (Image) widget.getData("TIP_IMAGE");
				tipLabelText.setText(text != null ? text : "");
				tipLabelImage.setImage(image); // accepts null
				tipShell.pack();
				setHoverLocation(tipShell, tipPosition);
				tipShell.setVisible(true);
			}
		});

		/*
		 * Trap F1 Help to pop up a custom help box
		 */
		control.addHelpListener(new HelpListener() {
			public void helpRequested(HelpEvent event) {
				if (tipWidget == null)
					return;
				ToolTipHelpTextHandler handler = (ToolTipHelpTextHandler) tipWidget
						.getData("TIP_HELPTEXTHANDLER");
				if (handler == null)
					return;
				String text = handler.getHelpText(tipWidget);
				if (text == null)
					return;

				if (tipShell.isVisible()) {
					tipShell.setVisible(false);
					Shell helpShell = new Shell(parentShell, SWT.SHELL_TRIM);
					helpShell.setLayout(new FillLayout());
					Label label = new Label(helpShell, SWT.NONE);
					label.setText(text);
					helpShell.pack();
					setHoverLocation(helpShell, tipPosition);
					helpShell.open();
				}
			}
		});
	}

	/**
	 * Sets the location for a hovering shell
	 * 
	 * @param shell
	 *            the object that is to hover
	 * @param position
	 *            the position of a widget to hover over
	 * @return the top-left location for a hovering box
	 */
	private void setHoverLocation(Shell shell, Point position) {
		Rectangle displayBounds = shell.getDisplay().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shellBounds.x = Math.max(Math.min(position.x, displayBounds.width
				- shellBounds.width), 0);
		shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height
				- shellBounds.height), 0);
		shell.setBounds(shellBounds);
	}

	/**
	 * ToolTip help handler
	 */
	protected interface ToolTipHelpTextHandler {
		/**
		 * Get help text
		 * 
		 * @param widget
		 *            the widget that is under help
		 * @return a help text string
		 */
		public String getHelpText(Widget widget);
	}
}
