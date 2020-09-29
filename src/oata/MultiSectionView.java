package oata;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.View;

public class MultiSectionView extends BoxView {
	protected static final int PAGE_INSET = 5;
	protected static final Insets PAGE_MARGIN = new Insets(50, 50, 50, 50);
	//
	protected static final Color BACKGROUND = new Color(245, 245, 245);
	private int pageNumber=1;
	private int pageWidth;
	protected int columnWidth=290;
	
	int majorTargetSpan=0;
	int[] majorOffsets=null;
	int[] majorSpans=null;

	int minorTargetSpan=0;
	int[] minorOffsets=null;
	int[] minorSpans=null;
	    
	//starting positions of paragraph views
	Point[] starts;

    //cont of columns
	int columnNumber=1;
	int pageCount=1;
	    
	/**
	* Creates a view from an element that spans the supplied axis
	* @param element
	* @param axis
	*/
	public MultiSectionView(Element element, int axis) {
		super(element, axis);
	
	}
	public int getPageCount(){
		return pageNumber;
	}
	
	protected int calculatePageBreak(int pageNumber) {
		int pageBreak = (pageNumber * getPageHeight()) - PAGE_INSET - getPageMargin().bottom;
		return pageBreak;
	}
	public double getZoomFactor() {
	    Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
	    if (scale != null) {
	        return scale.doubleValue();
	    }
	
	    return 1;
	}
	
	public int getPageWidth() {
	    int scale = (int) getDocument().getProperty("PAGE_WIDTH");
	    if (scale != 0) {
	        return scale;
	    }
	
	    return 1;
	}
	
	public int getPageHeight() {
	    int scale = (int) getDocument().getProperty("PAGE_HEIGHT");
	    if (scale != 0) {
	        return scale;
	    }
	
	    return 1;
	}
	
	public int getColumnWidth() {
		Insets margin = (Insets) getDocument().getProperty("PAGE_MARGIN");
	    int scale = (getPageWidth()-2*PAGE_INSET)/2 - margin.left*2;
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
	
	    return new Insets(20, 20, 20, 20);
	}
	 
	protected void layout(int width, int height) {
		//Insets margin = (Insets) getDocument().getProperty("PAGE_MARGIN");
		height = getPageHeight()-500;
		System.out.println(height);
	    super.layout(width,
	                 new Double(height * getZoomFactor()).intValue());
	} 
	 
	 
	public float getPreferredSpan(int axis) {
        if (axis==View.Y_AXIS) {
            return pageCount*getPageHeight();
        }
        else {
            return getPageWidth();
        }
    }
    public float getMinimumSpan(int axis) {
        if (axis==View.Y_AXIS) {
        	return pageCount*getPageHeight();
        }
        else {
        	return getPageWidth();
        }
    }
    public float getMaximumSpan(int axis) {
        if (axis==View.Y_AXIS) {
        	return pageCount*getPageHeight();
        }
        else {
        	return getPageWidth();
        }
    }
	
	protected void performPara(){
		
		MultiPagingParagraphView view;
		int verticalOffset=0;
		//
		starts=new Point[majorOffsets.length];
		int totalOffset=30;
		int pageBreak;
		//
		for (int i = 0; i < majorOffsets.length; i++) {
			//
			pageBreak = pageNumber*(getPageHeight()-PAGE_INSET-getPageMargin().top); 
			//
			view = (MultiPagingParagraphView) getView(i);
			starts[i]=new Point();
			view.vo = verticalOffset;
			view.columnNumber = columnNumber;
			view.totalOffset = totalOffset;
			view.performLine();
			//
			starts[i].y = (pageNumber-1)*(getPageHeight()-getPageMargin().top)+verticalOffset;
            starts[i].x = getPageMargin().left+(columnNumber-1)%2*columnWidth;
            System.out.println("starts: "+starts[i]);
            //
			verticalOffset= getPageHeight() -view.restHeight;
			//this is important
			columnNumber += view.cc-1;
			if ((majorSpans[i] + totalOffset) > pageBreak) {
				view.pageNumber = pageNumber;
				pageCount = view.pageNumber;
				if (view.cc == 2) {
					//
					view.adjustSpan = majorSpans[i]-30;
					//
				}
				
			}
			totalOffset = majorOffsets[i]+majorSpans[i];
			pageNumber += view.pn-1;
		}
        
	}
	 
	protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) 
	{
		super.layoutMajorAxis(targetSpan, axis, offsets, spans);
		majorOffsets = offsets;
		majorSpans = spans;
		//
	    performPara();
	    
	    MultiPagingParagraphView view;
	    
	    for (int i = 0; i < offsets.length; i++) {
	    	view = (MultiPagingParagraphView) getView(i);
	    	offsets[i] = (view.pageNumber-1)*getPageHeight()+PAGE_INSET+getPageMargin().top;
	    	spans[i] = getPageHeight()+view.adjustSpan;
			
	    }
	}
	
	protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
		super.layoutMinorAxis(targetSpan, axis, offsets, spans);
		minorOffsets = offsets;
		minorSpans = spans;
	    //
		MultiPagingParagraphView view;
	    for (int i = 0; i < offsets.length; i++) {
	    	view = (MultiPagingParagraphView) getView(i);
	    	offsets[i] = PAGE_INSET+(view.columnNumber-1)%2*(columnWidth+50*2);
	    	spans[i] = 330*2;
	    }
	}
	
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
	        //define child container
	        if (starts!=null) {
	            for (int i=starts.length-1; i>0; i--) {
	                if ((starts[i].x < x && starts[i].y+getPageMargin().top+5+PAGE_INSET < y  )){
	                	System.out.println(starts[i]);
	                    return getView(i).viewToModel(x, y  ,a,bias);
	                }
	            }
	        }
	        return getView(0).viewToModel(x, y, a,bias);
	}
	
	
	public void paint(Graphics g, Shape a) {
		//	super.paint(g, a);
	    Graphics2D g2d = (Graphics2D) g;
	    double zoomFactor = getZoomFactor();
	    AffineTransform old = g2d.getTransform();
	    g2d.scale(zoomFactor, zoomFactor);
	    //
		Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
		Rectangle page = new Rectangle(alloc.x, alloc.y, getPageWidth(), getPageHeight());
		 
		for (int i = 0; i < pageNumber; i++) {
			page.y = alloc.y + getPageHeight() * i;
			if (page.intersects(alloc)) paintPageFrame(g, page);
		}
		 
		// Fills in any unpainted areas
		if ((alloc.y + alloc.height) > (page.y + page.height)) {
			g.setColor(BACKGROUND);
			g.fillRect(page.x, page.y + page.height, page.width, alloc.height - page.height);
		}
		// Fills in any unpainted areas
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, alloc.x, page.y + page.height);
		g.fillRect(page.x + page.width, 0, page.x + page.width+alloc.x, page.y + page.height);
		g.fillRect(0, 0, page.x + page.width, alloc.y);
		//
		if (Math.round(getZoomFactor()*100) == 100){
		    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		super.paint(g2d, a);
		g2d.setTransform(old);
	}
	 
	 
	private void paintPageFrame(Graphics g, Rectangle page) {
		Color oldColor = g.getColor();
		//borders
		g.setColor(BACKGROUND);
		g.fillRect(page.x, page.y, page.width, PAGE_INSET);
		g.fillRect(page.x, page.y, PAGE_INSET, page.height);
		g.fillRect(page.x, page.y + page.height - PAGE_INSET, page.width, PAGE_INSET);
		g.fillRect(page.x + page.width - PAGE_INSET, page.y, PAGE_INSET, page.height);
		//frame
		g.setColor(Color.lightGray);
		g.drawRect(page.x + PAGE_INSET,
		page.y + PAGE_INSET,
		page.width - 2 * PAGE_INSET,
		page.height - 2 * PAGE_INSET);
		//shadow
		g.fillRect(page.x + page.width - PAGE_INSET, page.y + PAGE_INSET + 4, 
					4, page.height - 2 * PAGE_INSET);
		g.fillRect(page.x + PAGE_INSET + 4,
		page.y + page.height - PAGE_INSET,
		page.width - 2 * PAGE_INSET, 4);
		g.setColor(oldColor);
		//for multi column
		g.setColor(Color.darkGray);
        g.drawLine(PAGE_INSET+columnWidth+2*getPageMargin().left, page.y+PAGE_INSET+getPageMargin().top,
        		PAGE_INSET+columnWidth+2*getPageMargin().left, page.y +page.height-PAGE_INSET-getPageMargin().bottom);
	}
	
}

