package oata;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;

public class CustomCaret extends DefaultCaret{

	    NeoHaneol owner;
	    
	    /**
	     * The last SelectionPreservingCaret that lost focus
	     */
	    private static CustomCaret last = null;

	    /**
	     * The last event that indicated loss of focus
	     */
	    private static FocusEvent lastFocusEvent = null;
	    
	    private boolean isInsertMode=false;
	    Color oldCaretColor;
	    Color insertCaretColor=new Color(254, 254, 254);

	 
	    /**
	     * Called when the component containing the caret gains focus. 
	     * DefaultCaret does most of the work, while the subclass checks
	     * to see if another instance of SelectionPreservingCaret previously
	     * had focus.
	     *
	     * @param e the focus event
	     * @see java.awt.event.FocusListener#focusGained
	     */
	    public void focusGained(FocusEvent evt) {
	        super.focusGained(evt);

	        // If another instance of SelectionPreservingCaret had focus and
	        // we defered a focusLost event, deliver that event now.
	        if ((last != null) && (last != this)) {
	            last.hide();
	        }
	    }

	    /**
	     * Called when the component containing the caret loses focus. Instead
	     * of hiding both the caret and the selection, the subclass only 
	     * hides the caret and saves a (static) reference to the event and this
	     * specific caret instance so that the event can be delivered later
	     * if appropriate.
	     *
	     * @param e the focus event
	     * @see java.awt.event.FocusListener#focusLost
	     */
	    public void focusLost(FocusEvent evt) {
	        setVisible(false);
	        last = this;
	        lastFocusEvent = evt;
	    }

	    /**
	     * Delivers a defered focusLost event to this caret.
	     */
	    protected void hide() {
	        if (last == this) {
	            super.focusLost(lastFocusEvent);
	            last = null;
	            lastFocusEvent = null;
	        }
	    }
	    
	    public boolean isInsertMode() {
	        return isInsertMode;
	    }
	 
	    public void setInsertMode(boolean insertMode) {
	        isInsertMode = insertMode;
	        processMode();
	    }
	    
	    private void processMode() {
	        if (isInsertMode()) {
	            processCaretWidth();
	            owner.m_monitor.setCaretColor(insertCaretColor);
	        }

	        else {
	            owner.m_monitor.setCaretColor(Color.black);
	        	owner.m_monitor.putClientProperty("caretWidth",  1);
	        }
	    }
	 
	    private void processCaretWidth() {
	        try {
	            int pos=owner.m_monitor.getCaretPosition();
	            Rectangle rPos=owner.m_monitor.modelToView(pos)!=null ? owner.m_monitor.modelToView(pos).getBounds() :new Rectangle();
	            //
	            int caretX=rPos.x;
	            int caretEndX=rPos.x;
	            if (pos< owner.m_monitor.getDocument().getLength()) {
	                Rectangle rNextPos= owner.m_monitor.modelToView(pos+1)!=null ? owner.m_monitor.modelToView(pos+1).getBounds(): new Rectangle();
	              //  System.out.println("rNextPos:"+rNextPos);	
	                if (rPos.y==rNextPos.y) {
	                    caretEndX=rNextPos.x;
	                }
	            }
	            owner.m_monitor.putClientProperty("caretWidth", Math.max(1, caretEndX-caretX+1));
	        } catch (BadLocationException e) {
	            e.printStackTrace();
	        }
	    }
		
	    public void replaceSelection(String content) {
	    	if (content == null) return;
	        if (owner.m_monitor.isEditable() && isInsertMode() && owner.m_monitor.getSelectionStart()== owner.m_monitor.getSelectionEnd()) {
	            int pos=owner.m_monitor.getCaretPosition();
	            
	            int lastPos=Math.min(owner.m_monitor.getDocument().getLength(), pos+content.length());
	            owner.m_monitor.select(pos, lastPos);
	        }
	        //super.replaceSelection(content);
	    }
	    
	    public CustomCaret(NeoHaneol owner) {
		    //setBlinkRate(500); // half a second
		    this.owner = owner;
		    
		    int blinkRate = 500;
	        
	        setBlinkRate(blinkRate);
	        //
	        setInsertMode(false);
        
	  }
	    protected synchronized void damage(Rectangle r) {
	        if (r == null)
	          return;

	        // give values to x,y,width,height (inherited from java.awt.Rectangle)
	        x = r.x;
	        y = r.y;
	        height = r.height;
	        // A value for width was probably set by paint(), which we leave alone.
	        // But the first call to damage() precedes the first call to paint(), so
	        // in this case we must be prepared to set a valid width, or else
	        // paint()
	        // will receive a bogus clip area and caret will not get drawn properly.
	        if (width <= 0)
	          width = getComponent().getWidth();

	        repaint(); // calls getComponent().repaint(x, y, width, height)
	      }

	  
	  @Override
	    public void paint(Graphics g) {
		    int currentpos = owner.m_monitor.getCaretPosition();
		    CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
		    AttributeSet currentattr = doc.getCharacterElement(currentpos).getAttributes();
		    String font_family = StyleConstants.getFontFamily(currentattr);
		    int font_size = StyleConstants.getFontSize(currentattr);
		    //
		    Font f = new Font(font_family, Font.PLAIN, font_size);
		    g.setFont(f);
		    FontMetrics metrics = g.getFontMetrics();
        	//
		    double scale = (double) owner.m_monitor.getDocument().getProperty("ZOOM_FACTOR");
            // 
            int hgt = metrics.getHeight();
            //
            JTextComponent comp = getComponent();
            if (comp == null) {
                return;
            }
            //
            int pos=owner.m_monitor.getCaretPosition();
            Rectangle rPos = null;
			try {
				rPos = owner.m_monitor.modelToView(pos)!=null ? owner.m_monitor.modelToView(pos).getBounds() :new Rectangle();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //
            Rectangle r = rPos;
            //
            if (r == null) {
			    return;
			}

		  if ((x != r.x) || (y != r.y)) {
		      // paint() has been called directly, without a previous call to
		      // damage(), so do some cleanup. (This happens, for example, when
		      // the text component is resized.)
		      repaint(); 
		      // erase previous location of caret
		      x = r.x; 
		      y = r.y;
		      height = r.height;
		    }

	        if (isVisible()) {
	        	 if (isInsertMode()) {
	                 //we should shift to half width because of DefaultCaret rendering algorithm
	                 AffineTransform old=((Graphics2D)g).getTransform();
	                 int w=(Integer) owner.m_monitor.getClientProperty("caretWidth");
	                 g.setXORMode(Color.black);
	                 g.translate(w/2,0);
	                 //
	                 super.paint(g);
	                 ((Graphics2D)g).setTransform(old);
	             } else {
		                if (isVisible()) {
			            	//
			            	Graphics2D g2d = (Graphics2D) g;
			            	Rectangle2D r2d = new Rectangle2D.Float(r.x, (float)(r.y+(2*scale)), 1, (float) ((hgt)*scale));
			            	g2d.draw(r2d);
			            	//
			            }
	           }
	        }
	    }
}
