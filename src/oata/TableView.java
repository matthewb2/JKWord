package oata;

import java.awt.*;
import javax.swing.text.*;

public class TableView extends BoxView {
	
    public TableView(Element elem) {
        super(elem, View.Y_AXIS);
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
        g.drawLine(alloc.x, alloc.y, alloc.x + alloc.width, alloc.y);
        int lastY = alloc.y + alloc.height;
        if (index == getViewCount() - 1) {
            lastY--;
        }
        g.drawLine(alloc.x, lastY, alloc.x + alloc.width, lastY);
        
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
