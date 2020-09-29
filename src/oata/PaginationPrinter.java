package oata;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;

import oata.CustomEditorKit.SectionView;


/**
 *
 *
 * @author Stanislav Lapitsky
 * @version 1.0
 */
public class PaginationPrinter implements Printable, Pageable {
    private PageFormat pageFormat;
    private JEditorPane editorPane;
    boolean isPaginated = false;
    CustomEditorKit kit = null;
   

   
    public PaginationPrinter(PageFormat pageFormat, JEditorPane pane) throws PrinterException {
        this.pageFormat = pageFormat;
        this.editorPane = pane;
        this.isPaginated = true;

        kit = (CustomEditorKit) pane.getEditorKit();
        kit.setPageWidth( (int) pageFormat.getWidth() - CustomEditorKit.PAGE_INSET );
        kit.setPageHeight( (int) pageFormat.getHeight() - CustomEditorKit.PAGE_INSET );
        pane.revalidate();
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
    	 // We have only one page, and 'page'
    	Graphics2D g2d = (Graphics2D) g;
    	
        if (isPaginated) {
        	
            g2d.translate(0, -kit.getPageHeight()*pageIndex);
        	//set minus is important
            //translate origin to page start
            g2d.translate(-kit.PAGE_INSET, -kit.PAGE_INSET);
            
            Shape oldClip=g2d.getClip();
            //
            g2d.setClip(0, kit.getPageHeight()*pageIndex, kit.getPageWidth(), kit.getPageHeight());
            //
            double ZOOM_FACTOR = (double)(595-kit.PAGE_INSET)/(800-kit.PAGE_INSET);
            g2d.scale(ZOOM_FACTOR, ZOOM_FACTOR);
            //
            editorPane.printAll(g2d);
            //
            g2d.setClip(oldClip);
            //                      
            if (pageIndex < getPageCount()) {
                return PAGE_EXISTS;
            }
        }
        else {
            if (pageIndex == 0) {
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                editorPane.printAll(g);
                return PAGE_EXISTS;
            }
        }
        return NO_SUCH_PAGE;
    }
    public int getPageCount() {
        if (isPaginated) {
            return ( (SectionView) editorPane.getUI().getRootView(editorPane).getView(0)).getPageCount();
        }
        return 1;
    }

    public int getNumberOfPages() {
        return getPageCount();
    }

    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return pageFormat;
    }

    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }

    
}
