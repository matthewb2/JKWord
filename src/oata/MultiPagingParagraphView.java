package oata;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.View;

public class MultiPagingParagraphView   extends ParagraphView {
	
	
	protected static final int PAGE_INSET = 5;
	protected static final Insets PAGE_MARGIN = new Insets(50, 50, 50, 50);
	
	protected int verticalStartOffset=0;
	
	protected int restHeight;
	public int adjustSpan;
	public int totalOffset;
	
	int majorTargetSpan=0;
	int[] majorOffsets=null;
	int[] majorSpans=null;

	int minorTargetSpan=0;
	int[] minorOffsets=null;
	int[] minorSpans=null;
	
	private int columnWidth = 290;
	
	public int columnCount=1;
	public int columnNumber=0;
	public int pageNumber=1;
	
	public int cc = 0;
	public int vo = 0;
	public int pn = 0;
	public int pageCount = 1;
	
	protected Point[] starts;

		 
	public MultiPagingParagraphView(Element elem) {
		super(elem);
		
	}
	
	public int getColumnCount(){
		return this.cc;
	}
	public int getPageHeight() {
	    int scale = (int) getDocument().getProperty("PAGE_HEIGHT");
	    if (scale != 0) {
	        return scale;
	    }
	
	    return 1;
	}
	
	public Insets getPageMargin() {
	    Insets scale = (Insets) getDocument().getProperty("PAGE_MARGIN");
	    if (scale != null) {
	        return scale;
	    }
	
	    return new Insets(50, 50, 50, 50);
	}
	 
	protected int calculatePageBreak(int pageNumber) {
		int pageBreak = (pageNumber * getPageHeight()) - PAGE_INSET - getPageMargin().bottom;
		return pageBreak;
	}
	 
	
	public void layout(int width, int height) {
		super.layout(columnWidth, height);
	}
	
	protected void performLine(){
		cc = 1;
		pn = 1;
		int pc=1;
		int adjustx=0;
		int verticalOffset = vo;
		starts=new Point[majorOffsets.length];
		int relativeBreak = pageNumber*getPageHeight()-PAGE_INSET-totalOffset;
		//
		for (int i=0; i<majorOffsets.length; i++){
			starts[i]=new Point();
			if ( verticalOffset + majorSpans[i] > (getPageHeight() -getPageMargin().top - getPageMargin().bottom- 2*PAGE_INSET) ){
				if (columnNumber%2 == 0 && (majorSpans[i] + majorOffsets[i]) > relativeBreak ) {
					pn++;
					pc++;
					adjustx = (getPageMargin().left+columnWidth+getPageMargin().right)*2;
					relativeBreak = pn*getPageHeight()-PAGE_INSET-totalOffset;
				}
				cc++;
				verticalOffset = 0;
			}
			// a paragraph moves across 1 page
			if (relativeBreak < -getPageHeight() + adjustSpan){
				majorOffsets[i] = (pn)*(getPageHeight())+verticalOffset;
            }
			else {
				majorOffsets[i] = (pn-1)*(getPageHeight())+verticalOffset;
            }
			minorOffsets[i] = getPageMargin().left+(cc-1)*(getPageMargin().left+columnWidth+getPageMargin().right) -adjustx;
			//
			starts[i].y = (pageNumber+pn-2)*getPageHeight()+verticalOffset;
            starts[i].x = (columnNumber+cc)%2*(columnWidth+2*getPageMargin().left);
            //this is important
            verticalOffset += majorSpans[i];
			restHeight = getPageHeight()-verticalOffset;
			//
		}
	}
	 
	protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
		super.layoutMajorAxis(targetSpan, axis, offsets, spans);
		
		majorOffsets = offsets;
		majorSpans = spans;
		//	 
		performLine();
	}
	
	protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
		super.layoutMinorAxis(targetSpan, axis, offsets, spans);
		minorOffsets = offsets;
		minorSpans = spans;
		
	}
	
    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
        int ind=getViewIndexAtPoint((int)x,(int)y,a.getBounds());
        View v=getViewAtPoint((int)x,(int)y,a.getBounds());
        Shape childAlloc=getChildAllocation(ind,a);
        return v.viewToModel(x,y,childAlloc,bias);
    }

	protected int getViewIndexAtPoint(int x, int y, Rectangle alloc) {
	        if (starts!=null) {
	            for (int i=starts.length-1; i>0; i--) {
	                if ((starts[i].x < x && starts[i].y+getPageMargin().top +5+PAGE_INSET< y) 
	                		|| (starts[i].x+columnWidth<x && starts[i].y+getPageMargin().top +5+PAGE_INSET < y)
	                		){
	                	System.out.println(starts[i]);
	                	System.out.println(i);
	                    return i;
	                }
	            }
	        }
	        return 0;
	}
	
	protected View getViewAtPoint(int x, int y, Rectangle alloc) {
	        if (starts!=null) {
	            for (int i=starts.length-1 ; i > 0 ; i--) {
	            	if ((starts[i].x < x && starts[i].y+getPageMargin().top +5+PAGE_INSET < y)  
	                		|| (starts[i].x+columnWidth<x && starts[i].y+getPageMargin().top +5+PAGE_INSET < y)
	                	 )
	                {
	                    return getView(i);
	                }
	                
	            }
	        }
	        return getView(0);
	}
	   public Shape getChildAllocation(int index, Shape a) {
	        Rectangle r=super.getChildAllocation(index,a).getBounds();
	        r.x=starts[index].x+3+getPageMargin().left+PAGE_INSET;
	        r.y=starts[index].y+3+getPageMargin().top+PAGE_INSET;
	        return r;
	    }
		
}

