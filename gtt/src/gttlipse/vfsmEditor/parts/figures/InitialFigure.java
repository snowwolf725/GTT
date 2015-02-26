package gttlipse.vfsmEditor.parts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

public class InitialFigure extends Ellipse implements IVFSMFigure{
    private Ellipse ellipse;
    
    private Label label;

    public InitialFigure() {
        this.ellipse = new Ellipse();
    	this.label = new Label();
    	this.add(ellipse);
    	this.ellipse.setBackgroundColor(ColorConstants.black);
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
        this.ellipse.setBounds(rect);
        this.label.setBounds(rect);
    }
}
