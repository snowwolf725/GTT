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
package gtt.macro.mfsm;

import gtt.macro.mfsm.predicate.Predicate;
import gtt.macro.mfsm.predicate.AddPredicate;
import gtt.macro.mfsm.predicate.AndPredicate;
import gtt.macro.mfsm.predicate.ExistPredicate;
import gtt.macro.mfsm.predicate.NotPredicate;
import gtt.macro.mfsm.predicate.OrPredicate;
import gtt.macro.mfsm.predicate.PredicateFactory;
import gtt.macro.mfsm.predicate.RemovePredicate;

import java.util.Stack;
import java.util.Vector;

class ContractParser {
	public ContractParser() {
	}

	private Vector<String> doSplit(String str) {
		Vector<String> v = new Vector<String>();

		char charArray[] = str.toCharArray();
		String tmp = "";
		boolean keyword = false;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '(') {
				if (tmp.equals("add") || tmp.equals("remove")
						|| tmp.equals("exist")) {
					keyword = true;
					tmp += "(";
					continue;
				}

				// else if( !tmp.equals("") ) return null;
				if (!tmp.equals("")) {
					v.add(tmp);
					tmp = "";
				}

				v.add("(");

				continue;
			}

			if (charArray[i] == ')') {
				if (keyword) {
					v.add(tmp + ")");
					tmp = "";
					keyword = false;
					continue;
				}

				// if( tmp.equals("") ) return null;
				if (!tmp.equals("")) {
					v.add(tmp);
					tmp = "";
				}

				v.add(")");
				continue;
			}

			if (charArray[i] == '!') {
				// if( !tmp.equals("") ) return null;

				if (!tmp.equals("")) {
					v.add(tmp);
					tmp = "";
				}

				v.add("!");
				continue;
			}

			if (charArray[i] == '&') {
				if (!tmp.equals("")) {
					if (tmp.equals("&")) {
						tmp = "";
						v.add("&&");
					} else {
						v.add(tmp);
						tmp = "&";
					}
				} else
					tmp += "&";
				continue;
			}

			if (charArray[i] == '|') {
				if (!tmp.equals("")) {
					if (tmp.equals("|")) {
						tmp = "";
						v.add("||");
					} else {
						v.add(tmp);
						tmp = "|";
					}
				} else
					tmp += "|";
				continue;
			}

			tmp += charArray[i];
		}

		if (!tmp.equals(""))
			v.add(tmp);

		return v;
	}

	// 數字越大，優先權越高
	private int getInStackPriority(String symbol) {
		if (symbol.equals("("))
			return 4;
		if (symbol.equals("&&"))
			return 3;
		if (symbol.equals("||"))
			return 3;
		if (symbol.equals("!"))
			return 2;
		return -1;
	}

	private int getInComingPriority(String symbol) {
		if (symbol.equals("("))
			return 1;
		if (symbol.equals("&&"))
			return 3;
		if (symbol.equals("||"))
			return 3;
		if (symbol.equals("!"))
			return 1;
		return -1;
	}

	private boolean isKeyword(String symbol) {
		if (symbol.equals("("))
			return true;
		if (symbol.equals(")"))
			return true;
		if (symbol.equals("&&"))
			return true;
		if (symbol.equals("||"))
			return true;
		if (symbol.equals("!"))
			return true;
		return false;
	}

	private Vector<String> doPostfix(Vector<String> syntax) {

		Vector<String> postfix = new Vector<String>();
		Stack<String> stack = new Stack<String>();

		for (int i = 0; i < syntax.size(); i++) {
			String s = (String) syntax.get(i);
			int p = getInComingPriority(s);

			if (!isKeyword(s)) {
				postfix.add(s);
				continue;
			}

			if (s.equals(")")) {
				while (!stack.empty()
						&& !((String) stack.lastElement()).equals("(")) {
					String pop = (String) stack.pop();
					postfix.add(pop);
				}
				if (!stack.empty())
					stack.pop();

			} else {
				while (!stack.empty()
						&& getInStackPriority((String) stack.lastElement()) <= p) {
					String pop = (String) stack.pop();
					postfix.add(pop);
				}
				stack.push(s);
			}
		}

		while (!stack.empty())
			postfix.add((String) stack.pop());

		return postfix;
	}

	private Predicate buildPredicate(Vector<String> postfix) {
		PredicateFactory pb = new PredicateFactory();

		Stack<Predicate> stack = new Stack<Predicate>();

		for (int i = 0; i < postfix.size(); i++) {
			Predicate p1, p2;
			String s = (String) postfix.get(i);

			if (s.equals("&&")) {
				if (stack.size() < 2)
					return null;
				p2 = (Predicate) stack.pop();
				p1 = (Predicate) stack.pop();
				stack.push(pb.createAndPredicate(p1, p2));
				continue;
			}

			if (s.equals("||")) {
				if (stack.size() < 2)
					return null;
				p2 = (Predicate) stack.pop();
				p1 = (Predicate) stack.pop();
				stack.push(pb.createOrPredicate(p1, p2));
				continue;
			}

			if (s.equals("!")) {
				if (stack.size() < 1)
					return null;
				p1 = (Predicate) stack.pop();
				stack.push(pb.createNotPredicate(p1));
				continue;
			}

			if (s.startsWith("add(") && s.endsWith(")")) {
				String t = s.substring(4, s.length() - 1);
				stack.push(pb.createAddPredicate(t));
				continue;
			}

			if (s.startsWith("remove(") && s.endsWith(")")) {
				String t = s.substring(7, s.length() - 1);
				stack.push(pb.createRemovePredicate(t));
				continue;
			}

			if (s.startsWith("exist(") && s.endsWith(")")) {
				String t = s.substring(6, s.length() - 1);
				stack.push(pb.createExistPredicate(t));
				continue;
			}
		}

		if (stack.size() == 1)
			return (Predicate) stack.pop();
		return null;
	}

	public void printPredicate(Predicate p, String tab) {
		if (p instanceof AddPredicate) {
			AddPredicate add = (AddPredicate) p;
			System.out.println(tab + "Add(" + add.getPredicate() + ")");
		}

		if (p instanceof RemovePredicate) {
			RemovePredicate remove = (RemovePredicate) p;
			System.out.println(tab + "Remove(" + remove.getPredicate() + ")");
		}

		if (p instanceof ExistPredicate) {
			ExistPredicate exist = (ExistPredicate) p;
			System.out.println(tab + "Exist(" + exist.getPredicate() + ")");
		}

		if (p instanceof AndPredicate) {
			AndPredicate and = (AndPredicate) p;
			System.out.println(tab + "And");
			printPredicate(and.getLeftPredicate(), tab + "  ");
			printPredicate(and.getRightPredicate(), tab + "  ");
		}

		if (p instanceof OrPredicate) {
			OrPredicate or = (OrPredicate) p;
			System.out.println(tab + "Or");
			printPredicate(or.getLeftPredicate(), tab + "  ");
			printPredicate(or.getRightPredicate(), tab + "  ");
		}

		if (p instanceof NotPredicate) {
			NotPredicate not = (NotPredicate) p;
			printPredicate(not.getPredicate(), "!" + tab);
		}
	}

	public Predicate doParse(String condition) {
		if (condition == null || condition == "") {
			PredicateFactory pb = new PredicateFactory();
			ExistPredicate dft = pb.createExistPredicate("start");
			return dft;
		}

		// 依運算元與運算子拆成vector中的element
		String tmp[] = condition.split(" ");
		Vector<String> result = new Vector<String>();
		int i;
		for (i = 0; i < tmp.length; i++) {
			Vector<String> v = doSplit(tmp[i]);
			if (v == null)
				return null;
			for (int j = 0; j < v.size(); j++) {
				String t = ((String) v.get(j)).replaceAll(" ", "");
				result.add(t);
			}
		}

		// infix
		// System.out.println("infix:");
		// for(i=0;i<result.size();i++) {
		// System.out.println(result.get(i));
		// }

		// postfix
		Vector<String> postfix = doPostfix(result);
		// System.out.println("postfix:");
		// for(i=0;i<postfix.size();i++) {
		// System.out.println(postfix.get(i));
		// }

		// if( p != null ) printPredicate(p, "");

		return buildPredicate(postfix);
	}
}
