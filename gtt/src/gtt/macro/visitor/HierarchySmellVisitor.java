package gtt.macro.visitor;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.macro.view.BadSmellItem;
import gttlipse.macro.view.BadSmellListView;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class HierarchySmellVisitor extends MacroRefactorVisitor{
	private MacroComponentNode m_actualScript;
	private List<Component> m_allJavaComps;
	private MacroComponentNode m_cnode;
	private BadSmellListView m_view = GTTlipse.findBadSmellListView();
	private BadSmellItem m_badSmellItem;
	
	public void setActualScript(MacroComponentNode actualScript) {
		m_actualScript = actualScript;
	}
	
	public void setAllJavaComp(List<Component> allJavaComps) {
		m_allJavaComps = allJavaComps;
	}
	
	private Component ComponentNodeToJavaCompoenet(ComponentNode node) {
		if(node.getComponentName() == null)
			return null;
		for(Component comp : m_allJavaComps) {
			if(comp.getName() == null)
				continue;
			if(comp.getName().equals(node.getComponentName()))
				return comp;
		}
		return null;
	}
	
	boolean TwoLineIsIntersect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3)
	{ 
		double x, y;
		double Minx01 = Math.min(x0, x1);
		double Miny01 = Math.min(y0, y1);
		double Minx23 = Math.min(x2, x3);
		double Miny23 = Math.min(y2, y3);
		double Maxx01 = Math.min(x0, x1);
		double Maxy01 = Math.min(y0, y1);
		double Maxx23 = Math.min(x2, x3);
		double Maxy23 = Math.min(y2, y3);
	  
	   if(x1!=x0 && x2!=x3)
	   {
		   double k1 = (y1-y0)/(x1-x0);
		   double k2 = (y3-y2)/(x3-x2);
		   double Den = (y1-y0)*(x3-x2) - (y3-y2)*(x1-x0);
	       if(k1==k2)
	       { 
	    	   double d1 = Math.abs(y0*(x1-x0)-x0*(y1-y0)-y2*(x3-x2)+x2*(y3-y2)); 
	          if(d1==0)
	          {
	             if((x2>Minx01 && x2<Maxy01 && y2>Miny01 && y2<Maxy01) || (x3>Minx01 && x3<Maxy01 && y3>Miny01 && y3<Maxy01)
	             || (x0>Minx23 && x0<Maxy23 && y0>Miny23 && y0<Maxy23) || (x1>Minx23 && x1<Maxy23 && y1>Miny23 && y1<Maxy23))
	             {  
	                return true;
	             }
	             else
	             {
	                return false;
	             }
	          }
	          else
	          {
	             return false;
	          }  
	       }
	       x = ((y2-y0)*(x1-x0)*(x3-x2)+(y1-y0)*(x3-x2)*x0-(y3-y2)*(x1-x0)*x2)/Den;
	       y = ((y1-y0)*(x-x0))/(x1-x0) + y0;

	       if(Minx01<=x && x<=Maxx01 && Miny01<=y && y<=Maxy01 && Minx23<=x && x<=Maxx23 && Miny23<=y && y<=Maxy23)
	       {
	          return true;
	       }
	   }
	   else if(x1==x0 && x2!=x3)
	   {
	       x = x0;
	       y = ((y3-y2)*(x0-x2))/(x3-x2) + y2;
	       if(Minx01<=x && x<=Maxx01 && Miny01<=y && y<=Maxy01 && Minx23<=x && x<=Maxx23 && Miny23<=y && y<=Maxy23)
	       {
	          return true;
	       }
	   }
	   else if(x1!=x0 && x2==x3)
	   {
	       x = x2;
	       y = ((y1-y0)*(x2-x0))/(x1-x0) + y0;
	       if(Minx01<=x && x<=Maxx01 && Miny01<=y && y<=Maxy01 && Minx23<=x && x<=Maxx23 && Miny23<=y && y<=Maxy23)
	       {
	          return true;
	       }      
	   }
	   return false;
	}
	
	private Point getAbsoluteLoc(Component comp) {
		Point point = new Point();
		Point parentPoint = new Point();
		point = comp.getLocation();
		if(comp.getParent() != null) {
			comp = comp.getParent();
			if(comp instanceof Window || comp instanceof Dialog || comp instanceof Frame);
			else {
				parentPoint = getAbsoluteLoc(comp);
				point.x += parentPoint.x;
				point.y += parentPoint.y;
			}
		}
		return point;
	}
	
	private double calTriArea(Point p1, Point p2, Point p3) {
		// △ABC=｜(x1y2+x2y3+x3y1)-(x2y1+x3y2+x1y3)｜/2
		final double HALF = 0.5;
		double area = HALF * Math.abs((p1.x*p2.y + p2.x*p3.y + p3.x*p1.y) - (p2.x*p1.y + p3.x*p2.y + p1.x*p3.y));
		return area;
	}
	
	private double calRectArea(Point a, Point b, Point c, Point d) {
		final double HALF = 0.5;
		double area = HALF*(Math.abs((b.x-a.x)*(c.y-a.y)-(b.y-a.y)*(c.x-a.x))+Math.abs((b.x-d.x)*(c.y-d.y)-(b.y-d.y)*(c.x-d.x)));
		return area;
	}
	
	private boolean isInsideRect(Point p, Point a, Point b, Point c, Point d) {
		double rectArea = calRectArea(a, b, c, d);
		double triArea1 = calTriArea(p , a, b);
		double triArea2 = calTriArea(p , a, c);
		double triArea3 = calTriArea(p , b, d);
		double triArea4 = calTriArea(p , c, d);
		double delta = 1.5 ;
		if(rectArea >= (triArea1 + triArea2 + triArea3 + triArea4 - delta))
			return true;
		else return false;
	}
	
	private boolean analysisHierarchy(List<Component> childComps) {
		boolean result = false;
		// initial connect map
		final int CONNECT = 1;
		final int DISCONNECT = 2;
		int connectMap[][] = new int[childComps.size()][childComps.size()];
		for(int i = 0;i < childComps.size();i++)
			for(int j = 0;j < childComps.size();j++)
				connectMap[j][i] = DISCONNECT;
		for(int i = 0;i < childComps.size();i++)
			connectMap[i][i] = CONNECT;
		
		// identify interact
		for(int i = 0;i < childComps.size();i++)
			for(int j = 0;j < childComps.size();j++) {
				if(childComps.get(i) == childComps.get(j))
					continue;
				boolean isInter = false;
				Point comp1Point = getAbsoluteLoc(childComps.get(i));
				Point comp2Point = getAbsoluteLoc(childComps.get(j));
				
				for(Component comp : m_allJavaComps) {
					if(childComps.get(i).getName().matches(comp.getName()) || childComps.get(j).getName().matches(comp.getName()))
						continue;
					Point compPoint = getAbsoluteLoc(comp);
					// 兩線相交
					isInter |= TwoLineIsIntersect(comp1Point.x, comp1Point.y, comp2Point.x, comp2Point.y, 
							compPoint.x, compPoint.y,
							compPoint.x + comp.getWidth(), compPoint.y);
					isInter |= TwoLineIsIntersect(comp1Point.x, comp1Point.y, comp2Point.x, comp2Point.y, 
							compPoint.x, compPoint.y + comp.getHeight(),
							compPoint.x + comp.getWidth(), compPoint.y + comp.getHeight());
					isInter |= TwoLineIsIntersect(comp1Point.x, comp1Point.y, comp2Point.x, comp2Point.y, 
							compPoint.x, compPoint.y,
							compPoint.x, compPoint.y + comp.getHeight());
					isInter |= TwoLineIsIntersect(comp1Point.x, comp1Point.y, comp2Point.x, comp2Point.y, 
							compPoint.x + comp.getWidth(), compPoint.y,
							compPoint.x + comp.getWidth(), compPoint.y + comp.getHeight());
					// inside rect
					if(comp1Point.x >= comp2Point.x && comp1Point.y >= comp2Point.y) {
						//  [comp2]
						//         [comp1]
						isInter |= isInsideRect(compPoint, 
								new Point(comp2Point.x,comp2Point.y + childComps.get(j).getHeight()), new Point(comp2Point.x+childComps.get(j).getWidth(),comp2Point.y),
								new Point(comp1Point.x,comp1Point.y + childComps.get(i).getHeight()), new Point(comp1Point.x+childComps.get(i).getWidth(),comp1Point.y));
					} else if(comp1Point.x >= comp2Point.x && comp1Point.y < comp2Point.y) {
						//         [comp1]
						//  [comp2]
						isInter |= isInsideRect(compPoint, 
								comp1Point, new Point(comp1Point.x+childComps.get(i).getWidth(),comp1Point.y + childComps.get(i).getHeight()),
								comp2Point, new Point(comp2Point.x+childComps.get(j).getWidth(),comp2Point.y+ childComps.get(j).getHeight()));
					} else if(comp1Point.x < comp2Point.x && comp1Point.y >= comp2Point.y) {
						//         [comp2]
						//  [comp1]
						isInter |= isInsideRect(compPoint, 
								comp2Point, new Point(comp2Point.x+childComps.get(j).getWidth(),comp2Point.y + childComps.get(j).getHeight()),
								comp1Point, new Point(comp1Point.x+childComps.get(i).getWidth(),comp1Point.y+ childComps.get(i).getHeight()));
					} else if(comp1Point.x < comp2Point.x && comp1Point.y < comp2Point.y) {
						//  [comp1]
						//         [comp2]
						isInter |= isInsideRect(compPoint, 
								new Point(comp1Point.x,comp1Point.y + childComps.get(i).getHeight()), new Point(comp1Point.x+childComps.get(i).getWidth(),comp1Point.y),
								new Point(comp2Point.x,comp2Point.y + childComps.get(j).getHeight()), new Point(comp2Point.x+childComps.get(j).getWidth(),comp2Point.y));
					}
					
				}
				if(isInter == false) {
					connectMap[j][i] = CONNECT;
					connectMap[i][j] = CONNECT;
				}
			}
		
		// Floyd-wallshell
		for(int i=0;i<childComps.size();i++)
            for(int k=0;k<childComps.size();k++)
                for(int j=0;j<childComps.size();j++)
                {
                    if(connectMap[i][j]>=connectMap[i][k]+connectMap[k][j])
                    {
                    	connectMap[i][j]=CONNECT;
                    }
                }
		// identify connect graph
		for(int i = 0;i < childComps.size();i++)
			for(int j = 0;j < childComps.size();j++)
				if(i > j && connectMap[j][i] == DISCONNECT) {
					result = true;
					BadSmellItem item = new BadSmellItem("Inconsistent Hierarchy", childComps.get(i).getName()+"_"+childComps.get(j).getName(), 2);
					item.addNode(m_cnode);
					m_cnode.getBadSmellData().setBadSmellScore(1);
					m_cnode.getBadSmellData().setTotalBadSmellScore(1);
					m_cnode.getBadSmellData().setRGB(m_cnode.getBadSmellData().COLOR_YELLOW);
					m_view.addBadSmell(item);
				}
		return result;
	}
	
	public AbstractMacroNode findComp(MacroComponentNode parent,String name) {
		AbstractMacroNode node = null;
		for(int i = 0;i < parent.size();i++) {
			if(parent.get(i).getName()!= null && parent.get(i).getName().equals(name)) {
				return parent.get(i);
			} else if(parent.get(i) instanceof MacroComponentNode){
				node = findComp((MacroComponentNode)parent.get(i), name);
				if(node != null)
					return node;
			}
		}
		return null;
	}
	
	public boolean isWinTitleTheSame(MacroComponentNode parent,String title) {
		boolean isTheSame = true;
		for(int i = 0;i < parent.size();i++) {
			if(parent.get(i) instanceof ComponentNode) {
				ComponentNode cnode = (ComponentNode)parent.get(i);
				if(cnode.getTitle().equals(title) == false) {
					m_badSmellItem.addNode(cnode);
					cnode.getBadSmellData().setBadSmellScore(1);
					cnode.getBadSmellData().setTotalBadSmellScore(1);
					cnode.getBadSmellData().setRGB(cnode.getBadSmellData().COLOR_YELLOW);
					return false;
				}
			} else if(parent.get(i) instanceof MacroComponentNode){
				isTheSame = isWinTitleTheSame((MacroComponentNode)parent.get(i), title);
			}
		}
		return isTheSame;
	}
	
	@Override
	public void visit(ComponentEventNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ComponentNode node) {
		// rule 1. menu hierarchy must be the same as AUT's hierarchy
		if(node.getType().toString().equals("javax.swing.JMenu")){
			AbstractMacroNode actualnode = findComp(m_actualScript, node.getComponentName());
			if(actualnode != null){
				MacroComponentNode parent = (MacroComponentNode)node.getParent();
				for(int i = 0;i < actualnode.size();i++) {
					MacroComponentNode actualChild = (MacroComponentNode)actualnode.get(i);
					AbstractMacroNode abstractChild = findComp(parent, actualChild.getName());
					if(abstractChild == null) {
						BadSmellItem item = new BadSmellItem("Inconsistent Hierarchy", actualChild.getName() + "menu item not exist.", 2);
						item.addNode(node);
						node.getBadSmellData().setBadSmellScore(1);
						node.getBadSmellData().setTotalBadSmellScore(1);
						node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
						m_view.addBadSmell(item);
						break;
					}
				}
			}
		}
		super.visit(node);
	}

	@Override
	public void visit(MacroComponentNode node) {
		List<Component> childComps = new Vector<Component>();
		for(AbstractMacroNode child : node.getChildren()){
			if(child instanceof ComponentNode) {
				Component comp = ComponentNodeToJavaCompoenet((ComponentNode)child);
				if(comp != null)
					childComps.add(comp);
			}
		}
		for(Iterator<Component> it=m_allJavaComps.iterator();it.hasNext();){
			Component comp = it.next();
			if(comp.getClass().toString().startsWith("class javax.swing.JPanel")||
			   comp.getClass().toString().startsWith("class javax.swing.JRootPane")||
			   comp.getClass().toString().startsWith("class javax.swing.JLayeredPane")||
			   comp.getClass().toString().startsWith("class javax.swing.JMenu")||
			   comp.getClass().toString().startsWith("class javax.swing.JMenuItem")) {
				it.remove();
				continue;
			}
			if(comp instanceof Window || comp instanceof Dialog || comp instanceof Frame) {
				it.remove();
				continue;
			}
			if(comp.getName() == null || comp.getName().matches("null")) {
				it.remove();
				continue;
			}
		}
		m_cnode = node;
		analysisHierarchy(childComps);
		
		String winTitle = null;
		// rule 2. The components under the same window must have the same Window Title
		for(int i = 0;i < node.size();i++) {
			if(node.get(i) instanceof ComponentNode) {
				ComponentNode compNode = (ComponentNode)node.get(i);
				winTitle = compNode.getTitle();
				break;
			}
		}
		// have component, search all of components under this macro component
		// make sure that all of components under this macro component have the same window title
		if(winTitle != null){
			if(isWinTitleTheSame(node, winTitle) == false) {
				m_badSmellItem = new BadSmellItem("Inconsistent Hierarchy","The components under the " + 
						                             node.getName() + "must have the same Window Title", 2);
				node.getBadSmellData().setBadSmellScore(m_badSmellItem.getChildren().length);
				node.getBadSmellData().setTotalBadSmellScore(m_badSmellItem.getChildren().length);
				node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
				m_badSmellItem.addNode(node);
				m_view.addBadSmell(m_badSmellItem);
			}
		}
		
		// get child bad smell score and total bad smell score
		Iterator<AbstractMacroNode> ite = node.iterator();
		int badSmellScore = 0;
		int totalBadSmellScore = 0;
		
		while (ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			child.accept(this);
			badSmellScore += child.getBadSmellData().getBadSmellScore();
			totalBadSmellScore += child.getBadSmellData().getTotalBadSmellScore();
		}
		
		node.getBadSmellData().setBadSmellScore(badSmellScore);
		node.getBadSmellData().setTotalBadSmellScore(totalBadSmellScore);
		node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
	}

	@Override
	public void visit(MacroEventNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ModelAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ViewAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(NDefComponentNode node) {
		super.visit(node);
	}

	@Override
	public void visit(BreakerNode node) {

	}

	@Override
	public void visit(CommentNode node) {

	}

	@Override
	public void visit(SleeperNode node) {

	}

	@Override
	public void visit(OracleNode node) {

	}

	@Override
	public void visit(ExistenceAssertNode node) {

	}

	@Override
	public void visit(LaunchNode node) {

	}

	@Override
	public void visit(EventTriggerNode node) {

	}

	@Override
	public void visit(FitStateAssertionNode node) {

	}

	@Override
	public void visit(FitNode node) {

	}

	@Override
	public void visit(SplitDataAsNameNode node) {

	}

	@Override
	public void visit(GenerateOrderNameNode node) {

	}

	@Override
	public void visit(FixNameNode node) {

	}

	@Override
	public void visit(FitAssertionNode node) {
		
	}

	@Override
	public void visit(IncludeNode node) {
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}

}
