package oata;

import java.awt.*;
import javax.swing.text.*;

public class CellView extends BoxView {
	public Rectangle alloc;
	public int rowNumber, colNumber;
	public Point taborigin;
	public int[] rowHeights, colWidths;
	public Boolean isrowHeights=false;
	
	
    public CellView(Element elem) {
        super(elem, View.Y_AXIS);
        setInsets((short)2,(short)2,(short)2,(short)2);
    }
    
    public float getPreferredSpan(int axis) {
        if (axis==View.X_AXIS) {
        	//System.out.println(getCellSpan());
            return getCellWidth();
           
        }
               
        return super.getPreferredSpan(axis);
        //return getCellWidth();
    }
    
    public float getMinimumSpan(int axis) {
        return getPreferredSpan(axis);
    }
    public float getMaximumSpan(int axis) {
        return getPreferredSpan(axis);
    	
    }
    public float getAlignment(int axis) {
        return 0;
    }
    public void paint(Graphics g, Shape a){
    	super.paint(g, a);
    	Rectangle alloc = (Rectangle)a;
    	int alpha = 50; // 50% transparent
		//
    	Color selc = new Color(68, 140, 203, alpha);
    	g.setColor(selc);
    	
    	if(isrowHeights){
    	if ( this.getParent().getParent().getViewIndex(getStartOffset(),  Position.Bias.Forward )== this.rowNumber){
    		if ( this.getParent().getViewIndex(getStartOffset(),  Position.Bias.Forward )== this.colNumber){
    			g.fillRect(alloc.x, alloc.y, alloc.width, this.rowHeights[this.rowNumber]);
    			
    			
    		}
    	//System.out.println(alloc.x);
    	
    	}
    	}
    	this.alloc = (Rectangle)a;
    	
    }
  
    public int getCellWidth() {
        int width=100;
        Integer i=(Integer)getAttributes().getAttribute(CustomDocument.PARAM_CELL_WIDTH);
        if (i!=null) {
            width=i.intValue();
        }
        return width;
    }
    
    public void setRowNumber(int rownumber){
    	//this.colNumber = colnumber;
    	this.rowNumber = rownumber;
    }
    public void setColNumber(int colnumber){
    	//this.colNumber = colnumber;
    	this.colNumber = colnumber;
    }
    public void setRowValue(int[] rowheights){
    	
    	this.rowHeights = rowheights;
    }
    public void isRowValue(Boolean bool){
    	
    	this.isrowHeights = bool;
    }

	
     
    
}
