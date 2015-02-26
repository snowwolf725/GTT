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
 
package com.organic.maynard.outliner.menus.edit;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.dom.*;
import com.organic.maynard.outliner.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;
import com.organic.maynard.outliner.actions.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2004/02/02 10:17:41 $
 */

public class CopyMenuItem extends AbstractOutlinerMenuItem implements TreeSelectionListener, DocumentRepositoryListener, ActionListener, GUITreeComponent {
	
	public CopyMenuItem(){
		setName("CopyMenuItem");
	}
	
	// TreeSelectionListener Interface
	public void selectionChanged(TreeSelectionEvent e) {
		JoeTree tree = e.getTree();
		Document doc = tree.getDocument();
		
		if (doc == Outliner.documents.getMostRecentDocumentTouched()) {
			calculateEnabledState(tree);
		}
	}
	
	private void calculateEnabledState(JoeTree tree) {
		if (tree.getComponentFocus() == OutlineLayoutManager.ICON) {
			setEnabled(true);
		} else {
			if (tree.getCursorPosition() == tree.getCursorMarkPosition()) {
				setEnabled(false);
			} else {
				setEnabled(true);
			}
		}
	}
	
	
	// DocumentRepositoryListener Interface
	public void documentAdded(DocumentRepositoryEvent e) {}
	
	public void documentRemoved(DocumentRepositoryEvent e) {}
	
	public void changedMostRecentDocumentTouched(DocumentRepositoryEvent e) {
		if (e.getDocument() == null) {
			setEnabled(false);
		} else {
			calculateEnabledState(e.getDocument().getTree());
		}
	}
	
	
	// GUITreeComponent interface
	public void startSetup(Attributes atts) {
		super.startSetup(atts);
		setName("CopyMenuItem");
		setEnabled(false);
		
		addActionListener(this);
		Outliner.documents.addTreeSelectionListener(this);
		Outliner.documents.addDocumentRepositoryListener(this);
	}
	
	
	// ActionListener Interface
	public void actionPerformed(ActionEvent e) {
		OutlinerDocument doc = (OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched();
		OutlinerCellRendererImpl textArea = doc.panel.layout.getUIComponent(doc.tree.getEditingNode());
		
		if (textArea == null) {
			return;
		}
		
		Node node = textArea.node;
		JoeTree tree = node.getTree();
		OutlineLayoutManager layout = tree.getDocument().panel.layout;
		
		if (doc.tree.getComponentFocus() == OutlineLayoutManager.TEXT) {
			CopyAction.copyText(textArea, tree, layout);
		} else if (doc.tree.getComponentFocus() == OutlineLayoutManager.ICON) {
			CopyAction.copy(tree, layout);
		}
	}
}