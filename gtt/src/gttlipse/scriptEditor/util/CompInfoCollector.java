/**
 * 
 */
package gttlipse.scriptEditor.util;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.util.IResource
 * 
 */
public class CompInfoCollector extends ASTVisitor {
	private ComponentStorage storage = null;
	
	public CompInfoCollector() {
		storage = new ComponentStorage();
	}
	
	public Vector<IComponent> getResult(){
		storage.analysis();
		return storage.getComponet();
	}
	
	public void addFile(IFile file){
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		parser.setSource(unit);
		ASTNode astroot = parser.createAST(null);
		astroot.accept(this);
	}
	
	public boolean visit(ExpressionStatement node) {
		String line = node.toString();
		proccessSetCompomentName(line);
		proccessSetCompomentText(line);
		proccessSetTitle(line);
		proccessAddCompomentName(line);
		return true;
	}
	
	public boolean visit(ImportDeclaration node) {
		System.out.println(node.getName());
		storage.addImportInfo(node.getName().toString());
		return true;
	}
	
	public boolean visit(TypeDeclaration node) {
		System.out.println("typed:"+node.getName()+","+node.getSuperclassType());
		if(node.getSuperclassType() == null)return true;
		SwingComponent comp=SwingComponent.createDefault();
		comp.setType(node.getSuperclassType().toString());
		storage.addComponent(node.getName().toString(),comp);
		storage.setIswindowclass(true);
		storage.setClassname(node.getName().toString());
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FieldDeclaration node) {
		for (Iterator<VariableDeclarationFragment> iter = node.fragments().iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
			// ... store these fragments somewhere
//			System.out.println("========FieldDeclaration============\n"+node.getType()+","+fragment.getName()+"\n======================\n");
			SwingComponent comp=SwingComponent.createDefault();
			comp.setType(node.getType().toString());
			storage.addComponent(fragment.getName().toString(),comp);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(VariableDeclarationStatement node) {
		for (Iterator<VariableDeclarationFragment> iter = node.fragments().iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
			// ... store these fragments somewhere
//			System.out.println("========VariableDeclarationFragment============\n"+node.getType()+","+fragment.getName()+"\n======================\n");
			SwingComponent comp=SwingComponent.createDefault();
			comp.setType(node.getType().toString());
			storage.addComponent(fragment.getName().toString(),comp);
		}
		return false; // prevent that SimpleName is interpreted as reference
	}
	
	public void proccessSetCompomentName(String line){
		Pattern pattern = Pattern.compile(".*?\\.setName.*");
		Matcher m = pattern.matcher(line);
		if (m.find()) {
			// 分析出需要的 Set Component 資訊
			String str = m.group();
			String compname = str.replaceAll("\\.setName.*", "");
			String realname = str.replaceAll(".*?\\(\"", "");
			realname = realname.replaceAll("\"\\);", "");
//			System.out.println("========setName============\n"+compname+","+realname+"\n======================\n");
			storage.addCompNameInfo(compname, realname);
		}
	}
	
	public void proccessSetCompomentText(String line){
		Pattern pattern = Pattern.compile(".+?\\.setText.*");
		Matcher m = pattern.matcher(line);
		if (m.find()) {
			// 分析出需要的 Set Component 資訊
			String str = m.group();
			String comp = str.replaceAll("\\.setText.*", "");
			String comptext = str.replaceAll(".*?\\(\"", "");
			comptext = comptext.replaceAll("\"\\);", "");
//			System.out.println("========setName============\n"+compname+","+realname+"\n======================\n");
			storage.addCompTextInfo(comp, comptext);
		}
	}
	
	public void proccessSetTitle(String line){
		if(storage.getIswindowclass() == false)return;
		Pattern pattern = Pattern.compile("^setTitle\\(.*");
		Matcher m = pattern.matcher(line);
		if (m.find()) {
			// 分析出需要的 Set Component 資訊
			String str = m.group();
			String title = str.replaceAll("setTitle\\(\"", "");
			title = title.replaceAll("\".*", "");
			storage.setWinTitle(title);
		}
	}
	
	public void proccessAddCompomentName(String line){
		Pattern pattern = Pattern.compile("^add\\(.*?\\);");
		Matcher m = pattern.matcher(line);
		if (m.find()) {
			String str = m.group();
			String comp = str.replaceAll("add\\(", "");
			comp = comp.replaceAll("\\);", "");
			storage.addCompParentInfo(comp);
			return;
		}
		pattern = Pattern.compile("^getContentPane\\(\\)\\.add\\(.*?\\);");
		m = pattern.matcher(line);
		if (m.find()) {
			String str = m.group();
			String comp = str.replaceAll(".*?add\\(", "");
			comp = comp.replaceAll("\\);", "");
			storage.addCompParentInfo(comp);
			return;
		}
		pattern = Pattern.compile(".+?\\.add\\(.*?\\);");
		m = pattern.matcher(line);
		if (m.find()) {
			// 分析出需要的 Add Component 資訊,一般
			String str = m.group();
			String compparent = str.replaceAll("\\.add\\(.*", "");
			String comp = str.replaceAll(".*?\\(", "");
			comp = comp.replaceAll("\\);", "");
//			System.out.println("========add============\n"+compparent+","+comp+"\n======================\n");
			storage.addCompParentInfo(compparent, comp);
		}
	}
}
