package oata;

import java.awt.*;
import javax.swing.text.*;

public class CellView extends BoxView {
    public CellView(Element elem) {
        super(elem, View.Y_AXIS);
        setInsets((short)2,(short)2,(short)2,(short)2);
    }
    public float getPreferredSpan(int axis) {
        if (axis==View.X_AXIS) {
            return getCellWidth();
        }
        
        return super.getPreferredSpan(axis);
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

    public int getCellWidth() {
        int width=100;
        Integer i=(Integer)getAttributes().getAttribute(CustomDocument.PARAM_CELL_WIDTH);
        if (i!=null) {
            width=i.intValue();
        }
        return width;
    }
}
