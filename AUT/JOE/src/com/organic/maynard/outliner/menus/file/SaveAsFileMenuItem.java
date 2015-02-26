/**
 * Portions copyright (C) 2000, 2001 Maynard Demmon, maynard@organic.com
 * Portions copyright (C) 2002   Stan Krute <Stan@StanKrute.com>
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

package com.organic.maynard.outliner.menus.file;

import com.organic.maynard.outliner.model.DocumentInfo;
import com.organic.maynard.outliner.model.propertycontainer.*;
import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.io.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.dom.*;
import com.organic.maynard.outliner.event.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2004/01/30 00:12:42 $
 */

public class SaveAsFileMenuItem extends AbstractOutlinerMenuItem implements ActionListener {
	
	private FileProtocol protocol = null;
	
	// Constructors
	public SaveAsFileMenuItem(FileProtocol protocol) {
		setProtocol(protocol);
		addActionListener(this);
	}
	
	
	// Accessors
	public FileProtocol getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(FileProtocol protocol) {
		this.protocol = protocol;
		setText(protocol.getName());
	}
	
	
	// ActionListener Interface
	public void actionPerformed(ActionEvent e) {
		saveAsOutlinerDocument((OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched(), getProtocol());
	}
	
	public static void saveAsOutlinerDocument(OutlinerDocument document, FileProtocol protocol) {
		// grab the current doc info
		DocumentInfo currentDocInfo = document.getDocumentInfo() ;
		
		// clone it
		DocumentInfo cloneDocInfo = null;
		try {
			cloneDocInfo = (DocumentInfo) currentDocInfo.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
		
		// attach the clone to the document 
		document.setDocumentInfo(cloneDocInfo);
		
		// if the user backs out on the operation
		boolean alreadyUsingDocumentSettings = document.settings.useDocumentSettings();
		
		// record the current use document settings state
		if (!alreadyUsingDocumentSettings) {
			document.settings.setUseDocumentSettings(true);
		}
		
		if (!protocol.selectFileToSave(document, FileProtocol.SAVE)) {
			
			// restore the current doc info
			document.setDocumentInfo(currentDocInfo);
			
			// restore the use document settings state
			if (!alreadyUsingDocumentSettings) {
				document.settings.setUseDocumentSettings(false);
			}
			return;
		}
		
		// go ahead and try the save
		FileMenu.saveFile(PropertyContainerUtil.getPropertyAsString(document.getDocumentInfo(), DocumentInfo.KEY_PATH), document, protocol, true);
	}
}