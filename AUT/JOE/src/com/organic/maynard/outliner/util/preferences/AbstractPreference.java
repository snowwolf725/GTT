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
 
package com.organic.maynard.outliner.util.preferences;

import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.*;
import org.xml.sax.*;

public abstract class AbstractPreference implements Preference, GUITreeComponent {
	
	// Constants
	public static final String A_ID = "id";
	public static final String A_DEFAULT = "default";


	// Instance Fields
	private String command = null;
	private Validator validator = null;

	
	// GUITreeComponent Interface
	private String id = null;
	public String getGUITreeComponentID() {return this.id;}
	public void setGUITreeComponentID(String id) {this.id = id;}
	
	public void startSetup(Attributes atts) {
		String id = atts.getValue(A_ID);

		setCommand(id);

		Outliner.prefs.addPreference(id, this);
	}
	
	public void endSetup(Attributes atts) {
		String def = atts.getValue(AbstractPreference.A_DEFAULT);
		String cur = Outliner.prefs.getTempValue(getCommand());
				
		setDef(def);
		if (cur != null) {
			setCur(cur);
			setTmp(cur);
		} else {
			setCur(def);
			setTmp(def);
		}
	}	
	
			
	// Preference Interface
	public String getCommand() {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	public Validator getValidator() {
		return this.validator;
	}

	// Abstract methods
	public abstract void restoreCurrentToDefault();
	public abstract void restoreTemporaryToDefault();

	public abstract void setCur(String s);
	public abstract void setDef(String s);
	public abstract void setTmp(String s);

}