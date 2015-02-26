/**
 * 
 */
package gttlipse.scriptEditor.util;

import gttlipse.scriptEditor.def.TestScriptTage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class MarkerManger {
	IFile m_file;

	public MarkerManger(IFile file) {
		m_file = file;
	}

	public void createMarker() {
		try {
			m_file.deleteMarkers(IMarker.BOOKMARK, true,
					IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(m_file);
		parser.setSource(unit);
		final ASTNode astroot = parser.createAST(null);
		final CompilationUnit munit = (CompilationUnit) astroot;
		astroot.accept(new ASTVisitor() {
			int doccount = 0;
			int methodcount = 0;
			String methodname = "";

			public boolean visit(MethodDeclaration node) {
				IMarker marker;
				int line = munit.getLineNumber(node.getStartPosition());
				try {
					String message;
					methodcount++;
					marker = m_file.createMarker(IMarker.BOOKMARK);
					marker.setAttribute(IMarker.LINE_NUMBER, line);
					message = "M" + methodcount + "_"
							+ node.getName().getFullyQualifiedName();
					marker.setAttribute(IMarker.MESSAGE, message);
					marker.setAttribute(IMarker.TEXT, node.getName()
							.getFullyQualifiedName());
					methodname = node.getName().getFullyQualifiedName();
					doccount = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}

			public boolean visit(ExpressionStatement node) {
				String line = node.toString();
				Pattern pattern = Pattern
						.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					// 分析出需要的 TestDocument 資訊
					String str = m.group();
					str = str.replaceAll(
							TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN, "");
					str = str.replaceAll("\"\\);", "");

					IMarker marker;
					int lineno = munit.getLineNumber(node.getStartPosition());
					try {
						String message = "";
						doccount++;
						marker = m_file.createMarker(IMarker.BOOKMARK);
						marker.setAttribute(IMarker.LINE_NUMBER, lineno);
						message = "D" + doccount + "_" + methodname + "_" + str;
						marker.setAttribute(IMarker.MESSAGE, message);
						marker.setAttribute(IMarker.LOCATION, message);
						marker.setAttribute(IMarker.TEXT, str);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}
}
