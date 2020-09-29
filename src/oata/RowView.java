package oata;

import java.awt.*;
import javax.swing.text.*;

public class RowView extends BoxView {
	public Rectangle alloc;
	
    public RowView(Element elem) {
        super(elem, View.X_AXIS);
    }

    public float getPreferredSpan(int axis) {
    	 if (axis==View.Y_AXIS) {
             return getRowHeight();
            
         }
        return super.getPreferredSpan(axis);
    }
    
    protected void layout(int width, int height) {
        super.layout(width, height);
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

    protected void paintChild(Graphics g, Rectangle alloc, int index) {
        super.paintChild(g, alloc, index);
        g.setColor(Color.black);
        int h = (int) getPreferredSpan(View.Y_AXIS) - 1;
        g.drawLine(alloc.x, alloc.y, alloc.x, alloc.y + h);
        g.drawLine(alloc.x + alloc.width, alloc.y, alloc.x + alloc.width, alloc.y + h);
        g.setColor(Color.lightGray);
        //g.fillRect(alloc.x , alloc.y, alloc.width, h);
        this.alloc = alloc;
        //System.out.println(getCellSpan());
    }
    public Rectangle getAllocation()
    {
    	return this.alloc;
    }
    public int getRowHeight() {
        int height=30;
        Integer i=(Integer)getAttributes().getAttribute(CustomDocument.PARAM_ROW_HEIGHT);
        if (i!=null) {
            height=i.intValue();
            
        }
        return height;
    }
    public int getCellSpan() {
        int span=0;
        Integer i=(Integer)getAttributes().getAttribute(CustomDocument.PARAM_CELL_SPAN);
        if (i!=null) {
            span=i.intValue();
            
        }
        return span;
    }
}
