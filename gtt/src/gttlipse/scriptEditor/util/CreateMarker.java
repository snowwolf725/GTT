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
 * created first in project GTTlipse.actions
 * 
 */
public class CreateMarker {
	IFile m_file;
	
	public CreateMarker(IFile file) {
		m_file=file;
	}

	public void init() {
		try {
			m_file.deleteMarkers(IMarker.BOOKMARK, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(m_file);
		parser.setSource(unit);
		final ASTNode astroot = parser.createAST(null);
		final CompilationUnit munit=(CompilationUnit)astroot;
		astroot.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				IMarker marker;				
				int line=munit.getLineNumber(node.getStartPosition());
				try {
					marker = m_file.createMarker(IMarker.BOOKMARK);
					marker.setAttribute(IMarker.LINE_NUMBER, line);
					marker.setAttribute(IMarker.MESSAGE, node.getName().getFullyQualifiedName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			
			public boolean visit(ExpressionStatement node) {
				String line = node.toString();
				Pattern pattern = Pattern.compile(TestScriptTage.TESTSCRIPTDOC_PATTERN);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					// 分析出需要的 TestDocument 資訊
					String str = m.group();
					str=str.replaceAll(TestScriptTage.TESTSCRIPTDOC_REPLACEPATTERN, "");
					str=str.replaceAll("\"\\);", "");
					
					IMarker marker;				
					int lineno=munit.getLineNumber(node.getStartPosition());
					try {
						marker = m_file.createMarker(IMarker.BOOKMARK);
						marker.setAttribute(IMarker.LINE_NUMBER, lineno);
						marker.setAttribute(IMarker.MESSAGE, str);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}
}
