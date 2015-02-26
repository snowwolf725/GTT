package gttlipse.vfsmEditor.parts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

public class FinalFigure extends Ellipse implements IVFSMFigure{
    private Ellipse ellipse_big;
    private Ellipse ellipse_small;
    
    private Label label;

    public FinalFigure() {
        this.ellipse_big = new Ellipse();
        this.ellipse_small = new Ellipse();
        
    	this.label = new Label();
    	this.add(ellipse_big);
    	this.add(ellipse_small);
    	this.ellipse_small.setBackgroundColor(ColorConstants.black);
    	//this.add(label);
    }

    public String getText() {
        return this.label.getText();
    }

    public Rectangle getTextBounds() {
        return this.label.getTextBounds();
    }

    public void setName(String name) {
        this.label.setText(name);
        this.repaint();
    }

    //------------------------------------------------------------------------
    // Overridden methods from Figure
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
        Rectangle rect_small = new Rectangle();
        rect_small.x = rect.x + 5;
        rect_small.y = rect.y + 5;
        rect_small.height = rect.height - 10;
        rect_small.width = rect.width - 10;
        this.ellipse_small.setBounds(rect_small);
        this.ellipse_big.setBounds(rect);
        this.label.setBounds(rect);
    }
}
