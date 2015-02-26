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
 
package com.organic.maynard.outliner.menus.script;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.menus.popup.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.util.preferences.*;
import com.organic.maynard.outliner.dom.*;
import com.organic.maynard.outliner.event.*;
import bsh.Interpreter;
import bsh.NameSpace;
import javax.swing.*;
import java.awt.event.*;
import org.xml.sax.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2004/02/02 10:17:42 $
 */

public class RunAsBSHScriptMenuItem extends AbstractOutlinerMenuItem implements DocumentRepositoryListener, ActionListener, GUITreeComponent {
	
	// DocumentRepositoryListener Interface
	public void documentAdded(DocumentRepositoryEvent e) {}
	
	public void documentRemoved(DocumentRepositoryEvent e) {}
	
	public void changedMostRecentDocumentTouched(DocumentRepositoryEvent e) {
		if (e.getDocument() == null) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
	
	
	// GUITreeComponent interface
	public void startSetup(Attributes atts) {
		super.startSetup(atts);
		setName("RunAsBSHScriptMenuItem");
		setEnabled(false);
		
		addActionListener(this);
		Outliner.documents.addDocumentRepositoryListener(this);
	}
	
	
	// ActionListener Interface
	public void actionPerformed(ActionEvent e) {
		MacroPopupMenu.startWaitCursor();
		runAsScript();
		MacroPopupMenu.endWaitCursor();
	}
	
	private void runAsScript() {
		// Get the script from the document.
		OutlinerDocument doc = (OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched();
		
		if (doc == null) {
			return;
		}
		
		StringBuffer textBuffer = new StringBuffer();
		doc.tree.getRootNode().getRecursiveValue(textBuffer, Preferences.LINE_END_STRING, false);
		String script = textBuffer.toString();
		
		// Abort if script is empty
		if (script.equals("")) {
			return;
		}
		
		try {
			Interpreter bsh = new Interpreter();
			NameSpace nameSpace = new NameSpace(bsh.getClassManager(), "outliner");
			nameSpace.importPackage("com.organic.maynard.outliner");
			bsh.setNameSpace(nameSpace);
			bsh.eval(script);
		} catch (Exception ex) {
			System.out.println("BSH Exception: " + ex.getMessage());
			JOptionPane.showMessageDialog(doc, "BSH Exception: " + ex.getMessage());
			return;
		}
	}
}
