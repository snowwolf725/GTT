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
package gtt.eventmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Arguments implements Cloneable {

	private List<Argument> arguments = new Vector<Argument>();

	public boolean add(Argument a) {
		return arguments.add(a.clone());
	}

	public boolean remove(int index) {
		if (index < 0)
			return false;
		if (arguments.size() <= index)
			return false;

		arguments.remove(index);
		return true;
	}

	public void clear() {
		arguments.clear();
	}

	public Iterator<Argument> iterator() {
		return arguments.iterator();
	}

	public int size() {
		return arguments.size();
	}

	public Argument get(int idx) {
		if (idx < 0)
			return null;
		if (arguments.size() <= idx)
			return null;
		return (Argument) arguments.get(idx);
	}

	public boolean setValue(int idx, String value) {
		Argument a = get(idx);
		if (null == a)
			return false;

		a.setValue(value);
		return true;
	}

	public Argument find(String name) {
		Iterator<Argument> ite = iterator();
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public String getValue(String name) {
		try {
			Argument a = find(name);
			return a.getValue();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	// uhsing 2011/01/15
	public List<String> names() {
		List<String> list = new ArrayList<String>();

		Iterator<Argument> it = arguments.iterator();
		while (it.hasNext()) {
			Argument arg = it.next();
			list.add(arg.getName());
		}
		return list;
	}
	
	// 由argument type組成的串列
	public List<String> types() {
		List<String> list = new ArrayList<String>();

		Iterator<Argument> it = arguments.iterator();
		while (it.hasNext()) {
			Argument arg = it.next();
			list.add(arg.getType());
		}
		return list;
	}

	// 由argument value 組成的串列
	public List<String> values() {
		List<String> list = new ArrayList<String>();

		Iterator<Argument> it = arguments.iterator();
		while (it.hasNext()) {
			Argument arg = it.next();
			list.add(arg.getValue());
		}
		return list;
	}

	public Arguments clone() {
		Arguments arguments = new Arguments();
		Iterator<Argument> ite = iterator();
		while (ite.hasNext()) {
			arguments.add(ite.next());
		}
		return arguments;
	}

	public String toString() {
		StringBuilder signature = new StringBuilder("(");
		Iterator<Argument> ite = arguments.iterator();

		while (ite.hasNext()) {
			Argument arg = ite.next();
			// 有value 就顯示value，否則就顯示 argument name
			if (arg.getValue() == null || arg.getValue().equals(""))
				signature.append(arg.getName());
			else
				signature.append(arg.getValue());

			if (ite.hasNext())
				signature.append(", ");
		}
		signature.append(")");
		return signature.toString();
	}

	public String formSignature() {
		StringBuilder signature = new StringBuilder("(");
		Iterator<Argument> iterator = arguments.iterator();

		while (iterator.hasNext()) {
			Argument arg = iterator.next();

			signature.append(arg.getName());
			if (iterator.hasNext())
				signature.append(", ");
		}
		signature.append(")");

		return signature.toString();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Arguments))
			return false;

		Arguments args = (Arguments) o;

		return (checkSizeOfArguments(args) && checkEachArgument(args));
	}

	private boolean checkEachArgument(Arguments args) {
		for (int i = 0; i < arguments.size(); i++) {
			Argument mine = arguments.get(i);
			Argument target = args.get(i);

			if (!mine.getType().equals(target.getType()))
				return false;
			if (!mine.getName().equals(target.getName()))
				return false;
			if (!mine.getValue().equals(target.getValue()))
				return false;
		}
		return true;
	}

	private boolean checkSizeOfArguments(Arguments args) {
		return arguments.size() == args.size();
	}
}
