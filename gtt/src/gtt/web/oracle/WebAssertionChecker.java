/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.web.oracle;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.web.HtmlBaseElement;
import gtt.oracle.AbstractAssertionChecker;
import gtt.runner.web.WebController;

import java.util.Iterator;
import java.util.List;

public class WebAssertionChecker extends AbstractAssertionChecker {

	private void logging(String msg) {
		System.out.println("[WebAsserter ]" + msg);
	}

	public boolean check(IComponent comp, Assertion assertion) {
		HtmlBaseElement component = null;
		try {
			component = WebController.instance().find(comp);
		} catch (Exception e) {
			logging(e.getMessage());
			return false;
		}

		if (component == null) {
			logging("[" + comp + "] can't be found.");
			return false;
		}

		if (!(component instanceof HtmlBaseElement)) {
			logging("[" + comp + "] isn't a WebElement");
			return false;
		}

		return checkAcutalObject(component, assertion);
	}

	public boolean checkMultipleAssertions(IComponent comp, Assertion assertion) {
		List<HtmlBaseElement> components = null;
		try {
			components = WebController.instance().findComponents(comp);
		} catch (Exception e) {
			logging(e.getMessage());
			return false;
		}

		if (components == null) {
			logging("[" + comp + "] can't be found.");
			return false;
		}

		int successes = getSizeOfSuccessCheck(assertion, components);
		return successes == assertion.getExpectedSizeOfCheck();
	}

	private int getSizeOfSuccessCheck(Assertion assertion,
			List<HtmlBaseElement> components) {
		int count = 0;
		Iterator<HtmlBaseElement> iter = components.iterator();
		while (iter.hasNext()) {
			HtmlBaseElement component = iter.next();
			if (checkAcutalObject(component, assertion) == true)
				count++;
		}
		return count;
	}
}
