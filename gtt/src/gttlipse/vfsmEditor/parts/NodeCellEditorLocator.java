/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package gttlipse.vfsmEditor.parts;

import gttlipse.vfsmEditor.parts.figures.IVFSMFigure;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;


/**
 * CellEditorLocator for Activities.
 * @author Daniel Lee
 */
public class NodeCellEditorLocator implements CellEditorLocator {
    private IVFSMFigure m_Figure;
    /**
     * Creates a new ActivityCellEditorLocator for the given Label
     * @param nodeFigure the Label
     */
    public NodeCellEditorLocator(IVFSMFigure nodeFigure) {
        m_Figure = nodeFigure;
    }
    
    /**
     * @see CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
     */
    public void relocate(CellEditor celleditor) {
        Text text = (Text) celleditor.getControl();
        Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    	Rectangle rect = m_Figure.getTextBounds();
        text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
    }

}