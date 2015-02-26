/**
 * Copyright (C) 2000, 2001 Maynard Demmon, maynard@organic.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the 
 * following conditions are met:
 * 
 *  - Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 * 
 *  - Redistributions in binary form must reproduce the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided 
 *    with the distribution. 
 * 
 *  - Neither the names "Java Outline Editor", "JOE" nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
 
package com.organic.maynard.outliner.menus.window;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;

public class StackMenuItem extends AbstractOutlinerMenuItem implements ActionListener, GUITreeComponent {
	
	// Constants
	private static final int STACK_X_START = 5;
	private static final int STACK_Y_START = 5;
	private static final int STACK_X_STEP = 30;
	private static final int STACK_Y_STEP = 30;
	
	private static final int STACK_X_COLUMN_STEP = 45;
	
	
	// GUITreeComponent interface
	public void startSetup(Attributes atts) {
		super.startSetup(atts);
		setName("StackMenuItem");
		addActionListener(this);
	}
	
	
	// ActionListener Interface
	public void actionPerformed(ActionEvent e) {
		if (!Outliner.desktop.isMaximized()) {
			Point p = new Point(STACK_X_START,STACK_Y_START);
			int rowCount = 1;
			int columnCount = 1;
			
			int upperBound = getUpperScreenBoundary();
			
			OutlinerDocument mostRecentDocumentTouched = (OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched();
			
			for (int i = 0; i < Outliner.documents.openDocumentCount(); i++) {
				OutlinerDocument doc = (OutlinerDocument) Outliner.documents.getDocument(i);
				
				// Restore Size
				doc.restoreWindowToInitialSize();
				
				// Set Location
				doc.setLocation(p);
				p.x += STACK_X_STEP;
				p.y += STACK_Y_STEP;
				
				// Set Z Order
				doc.toFront();
				
				// If we've gone down to far, then reset for a new column
				if ((p.y + OutlinerDocument.INITIAL_HEIGHT) >= upperBound ) {
					p.y = STACK_Y_START;
					p.x = STACK_X_START + STACK_X_COLUMN_STEP * columnCount;
					columnCount++;
					rowCount = 0;
				}
				
				rowCount++;
			}
			
			if (mostRecentDocumentTouched != null) {
				mostRecentDocumentTouched.toFront();
			}
		}
	}
	
	private static int getUpperScreenBoundary() {
		return Outliner.desktop.getHeight() - STACK_Y_START;
	}
}