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
package gtt.macro.io;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.Assertion;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroContract;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.visitor.IMacroStructureVisitor;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class EBNFSaveVisitor implements IMacroStructureVisitor {

	StringWriter m_writer = new StringWriter();

	int m_indent = 0; // 縮排

	// Constructor
	public EBNFSaveVisitor() {

	}

	private void outIndent() {
		for (int i = 0; i < m_indent; i++)
			m_writer.write("\t");
	}

	public StringBuffer getStringBuffer() {
		return m_writer.getBuffer();
	}

	/**
	 * 將XML Document 實際寫到外部檔案中
	 */
	public boolean saveFile(String filepath) {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(filepath));
			out.println(m_writer.toString());
			out.flush();
			out.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void visit(ComponentEventNode node) {
		outIndent();
		m_writer.write(node.getName() + "." + node.getEventType());
		outputArgumentListValue(node.getArguments());
		m_writer.write(";\n");
	}

	public void visit(ComponentNode node) {
		outIndent();
		m_writer.write("Widget " + node.getName());
		m_writer.write("(\"" + node.getType() + "\", ");
		m_writer.write("\"" + node.getName() + "\");\n");
	}

	public void visit(MacroComponentNode node) {
		outIndent();
		m_writer.write("Component " + node.getName() + " {");
		if (node.size() == 0) {
			m_writer.write("}\n");
			return;
		} else {
			m_writer.write("\n");
			m_indent++;
			for (Iterator<AbstractMacroNode> ite = node.iterator(); ite
					.hasNext();) {
				AbstractMacroNode c = ite.next();
				c.accept(this);
			}
			m_indent--;
			outIndent();
			m_writer.write("}\n");
		}
	}

	public void visit(MacroEventNode node) {

		writeMacroContract(node.getContract());

		outIndent();
		m_writer.write("Event " + node.getName());
		outputArgumentList(node.getArguments());
		m_writer.write(" {");

		if (node.size() == 0) {
			m_writer.write("}\n");
			return;
		} else {
			m_writer.write("\n");
			m_indent++;
			for (Iterator<AbstractMacroNode> ite = node.iterator(); ite
					.hasNext();) {
				AbstractMacroNode c = ite.next();
				c.accept(this);
			}
			m_indent--;
			outIndent();
			m_writer.write("}\n");
		}

	}

	private void writeMacroContract(MacroContract c) {
		// write out contract
		if (!c.getPreCondition().equals("")) {
			outIndent();
			m_writer.write("//@pre " + c.getPreCondition());
			m_writer.write("\n");
		}
		if (!c.getAction().equals("")) {
			outIndent();
			m_writer.write("//@act " + c.getAction());
			m_writer.write("\n");
		}
		if (!c.getPostCondition().equals("")) {
			outIndent();
			m_writer.write("//@post " + c.getPostCondition());
			m_writer.write("\n");
		}
		outIndent();
		m_writer.write("//@level " + c.getLevel());
		m_writer.write("\n");
	}

	private void outputArgumentList(Arguments arglist) {
		m_writer.write("(");
		for (Iterator<Argument> ite = arglist.iterator(); ite.hasNext();) {
			Argument a = ite.next();
			// m_writer.write(a.getType() + " " + a.getName());
			// no type
			m_writer.write(a.getName());
			if (ite.hasNext())
				m_writer.write(", ");
		}
		m_writer.write(")");
	}

	private void outputArgumentListValue(Arguments arglist) {
		m_writer.write("(");
		for (Iterator<Argument> ite = arglist.iterator(); ite.hasNext();) {
			Argument a = ite.next();
			if (a.getValue().substring(0, 1).equals("@")) {
				// reference
				// string value
				m_writer
						.write(a.getValue().substring(1, a.getValue().length()));
			} else {
				// string value
				m_writer.write("\"" + a.getValue() + "\"");
			}
			if (ite.hasNext())
				m_writer.write(", ");
		}
		m_writer.write(")");
	}

	public void visit(ModelAssertNode node) {
	}

	public void visit(MacroEventCallerNode node) {
		outIndent();
		m_writer.write(node.getComponentName() + "." + node.getEventName());
		outputArgumentListValue(node.getArguments());
		m_writer.write(";\n");

	}

	public void visit(ViewAssertNode node) {
		outIndent();
		m_writer.write("Assert(");
		m_writer.write(node.getComponent().getName() + "."
				+ node.getAssertion().getMethodName());
		outputArgumentListValue(node.getArguments());
		// operator
		m_writer.write(Assertion.getOperatorString(node.getAssertion()
				.getCompareOperator()));
		// value
		if (node.getAssertion().getValue().substring(0, 1).equals("@")) {
			// reference
			// string value
			m_writer.write(node.getAssertion().getValue().substring(1,
					node.getAssertion().getValue().length()));
		} else {
			// string value
			m_writer.write("\"" + node.getAssertion().getValue() + "\"");
		}
		m_writer.write(");\n");

	}
	
	public void visit(ExistenceAssertNode node) {
		// 不知道要做什麼，以後再補 GSX
	}

	public void visit(gtt.macro.macroStructure.NDefComponentNode node) {
	}

	@Override
	public void visit(SleeperNode node) {

	}

	@Override
	public void visit(BreakerNode node) {

	}

	@Override
	public void visit(CommentNode node) {
		outIndent();
		m_writer.write("//" + node.getComment() + "\n");
	}

	
	@Override
	public void visit(OracleNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(LaunchNode node) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void visit(SplitDataNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(DynamicComponentNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(IncludeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
