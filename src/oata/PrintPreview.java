package oata;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.awt.print.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class PrintPreview extends JFrame implements MouseListener{

	  protected static int FRAME_WIDTH=900;
	  protected static int FRAME_HEIGHT=700;
	  protected int m_wPage;
	  protected int m_hPage;
	  protected Printable m_target;
	  protected JComboBox m_cbScale;
	  protected PreviewContainer m_preview;
      //
	  
	  public void fitEx(){
		  int h = m_preview.getParent().getHeight()-25;
   	   	  int w = (int) (h/1.414f);
          //
          Component[] comps = m_preview.getComponents();
          for (int k=0; k<comps.length; k++) {
             if (!(comps[k] instanceof PagePreview))
               continue;
             PagePreview pp = (PagePreview)comps[k];
             pp.setScaledSize(w, h);
          }
          int fit = 100*w/m_wPage;
          m_cbScale.setSelectedItem(Integer.toString(fit)+" %");
          //
          m_preview.doLayout();
          m_preview.getParent().getParent().validate();
          m_preview.setBorder(BorderFactory.createEmptyBorder(0, (m_preview.getParent().getWidth()-w)/2, 0, (m_preview.getParent().getWidth()-w)/2));
	       
			
	  }
	  public PrintPreview(Printable target, int width, int height) {
	    super("미리보기");
	    m_target = target;
	    m_wPage = width;
	    m_hPage = height;

	    JToolBar tb = new JToolBar();
	    //print button
	    JButton bt = new JButton("인쇄", new ImageIcon(getClass().getResource("res/print.png")));
	    ActionListener lst = new ActionListener() { 
	      public void actionPerformed(ActionEvent e) { 
	    	  try {
		          // Use default printer, no dialog
		          PrinterJob prnJob = PrinterJob.getPrinterJob();
		          prnJob.setPrintable(m_target);
		          setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		          prnJob.print();
		          setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		          dispose();
		        }
		        catch (PrinterException ex) {
		          ex.printStackTrace();
		        }
	      }
	    };
	    bt.addActionListener(lst);
	    bt.setAlignmentY(0.5f);
	    bt.setMargin(new Insets(4,6,4,6));
	    tb.add(bt);

	    bt = new JButton("닫기", new ImageIcon(getClass().getResource("res/close.png")));
	    lst = new ActionListener() { 
	      public void actionPerformed(ActionEvent e) { 
	        dispose();
	      }
	    };
	    bt.addActionListener(lst);
	    bt.setAlignmentY(0.5f);
	    bt.setMargin(new Insets(2,6,2,6));
	    tb.add(bt);
	    //
	    JButton fit = new JButton("쪽맞춤", new ImageIcon(getClass().getResource("res/fit-icon.png")));
	    fit.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	  
		          fitEx();   
		    	   
		       }
	    });
	    tb.add(fit);
	    //

	    
	    JButton pair = new JButton("맞쪽", new ImageIcon(getClass().getResource("res/book-icon.png")));
	    pair.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   m_preview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		    	   int h = m_preview.getParent().getHeight()-25;
		    	   int w = (int) (h/1.414f);
		           //
		           Component[] comps = m_preview.getComponents();
		           for (int k=0; k<comps.length; k++) {
		              if (!(comps[k] instanceof PagePreview))
		                continue;
		              PagePreview pp = (PagePreview)comps[k];
		              pp.setScaledSize(w, h);
		           }
		           if (m_preview.getParent().getWidth() > 2*w+200){
		           m_preview.setBorder(BorderFactory.createEmptyBorder(0, (m_preview.getParent().getWidth()-2*w)/2, 0, (m_preview.getParent().getWidth()-2*w)/2));
		           } else {
		        	 w = (m_preview.getParent().getWidth() -55)/2;
		        	 h = (int) (w*1.414f);
		        	 comps = m_preview.getComponents();
			           for (int k=0; k<comps.length; k++) {
			              if (!(comps[k] instanceof PagePreview))
			                continue;
			              PagePreview pp = (PagePreview)comps[k];
			              //
			              pp.setScaledSize(w, h);
			           }
		           }
		           int fit = 100*w/m_wPage;
		           m_cbScale.setSelectedItem(Integer.toString(fit)+" %");
		           m_preview.doLayout();
		           m_preview.getParent().getParent().validate();
		    	   
		       }
	    });
	    tb.add(pair);
	    
	    JButton fullscreen = new JButton("전체화면", new ImageIcon(getClass().getResource("res/book-icon.png")));
	    fullscreen.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   // full screen action
		    	   setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
		    	   //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		    	   dispose();
		    	   setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
		    	   setUndecorated (true);
		    	   setResizable(false);
		    	   setVisible (true);
		       }
	    });
	    tb.add(fullscreen);
	    
	    String[] scales = { "10 %", "25 %", "35 %", "50 %", "65 %", "80 %", "100 %" };
	    m_cbScale = new JComboBox(scales);
	    m_cbScale.setSelectedItem("100 %");
	    revalidate();
	    repaint();
	    lst = new ActionListener() { 
	      public void actionPerformed(ActionEvent e) { 
	        Thread runner = new Thread() {
	          public void run() {
	        	//selection action 
	            String str = m_cbScale.getEditor().getItem().toString();
	            if (str.endsWith("%"))
	              str = str.substring(0, str.length()-1);
	            str = str.trim();
	            int scale = 0;
	              
	            try { scale = Integer.parseInt(str); }
	            catch (NumberFormatException ex) { return; }
	            int w = (int)(m_wPage*scale/100);
	            int h = (int)(m_hPage*scale/100);

	            Component[] comps = m_preview.getComponents();
	            //
	            for (int k=0; k<comps.length; k++) {
	              if (!(comps[k] instanceof PagePreview))
	                continue;
	              PagePreview pp = (PagePreview)comps[k];
	                pp.setScaledSize(w, h);
	            }
	            //m_preview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		        m_preview.doLayout();
	            m_preview.getParent().getParent().validate();
	            
	          }
	        };
	        runner.start();
	      }
	    };
	    m_cbScale.addActionListener(lst);
	    m_cbScale.setMaximumSize(m_cbScale.getPreferredSize());
	    m_cbScale.setEditable(true);

	    tb.addSeparator();
	    tb.add(m_cbScale);
	    
	    JButton zoomIn = new JButton(new ImageIcon(getClass().getResource("res/zoom-out-icon.png")));
	    zoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				 // TODO Auto-generated method stub
				 String str = m_cbScale.getEditor().getItem().toString();
		         if (str.endsWith("%"))
		            str = str.substring(0, str.length()-1);
		         str = str.trim();
		         int scale = 0;
		              
		         try { scale = Integer.parseInt(str)-10; }
		            catch (NumberFormatException ex) { return; }
				
	             int w = (int)(m_wPage*scale/100);
	             int h = (int)(m_hPage*scale/100);

	             Component[] comps = m_preview.getComponents();
	             //
	             for (int k=0; k<comps.length; k++) {
	              if (!(comps[k] instanceof PagePreview))
	                continue;
	              PagePreview pp = (PagePreview)comps[k];
	                pp.setScaledSize(w, h);
	             }
	             //m_preview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		         m_preview.doLayout();
	             m_preview.getParent().getParent().validate();
	             m_cbScale.setSelectedItem(scale+" %");
	             m_preview.setBorder(BorderFactory.createEmptyBorder(0, (m_preview.getParent().getWidth()-w)/2, 0, (m_preview.getParent().getWidth()-w)/2));
			       
			}
	    	
	    });
	    
	    tb.add(zoomIn);
	    
	    
	    JButton zoomOut = new JButton(new ImageIcon(getClass().getResource("res/zoom-in-icon.png")));
	    zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String str = m_cbScale.getEditor().getItem().toString();
	            if (str.endsWith("%"))
	              str = str.substring(0, str.length()-1);
	            str = str.trim();
	            int scale = 0;
	              
	            try { scale = Integer.parseInt(str)+10; }
	            catch (NumberFormatException ex) { return; }
			
	            int w = (int)(m_wPage*scale/100);
	            int h = (int)(m_hPage*scale/100);

	            Component[] comps = m_preview.getComponents();
	            //
	            for (int k=0; k<comps.length; k++) {
	            	if (!(comps[k] instanceof PagePreview))
	            		continue;
	            	PagePreview pp = (PagePreview)comps[k];
	            	pp.setScaledSize(w, h);
	            }
	            //m_preview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	            m_preview.doLayout();
	            m_preview.getParent().getParent().validate();
	            m_cbScale.setSelectedItem(scale+" %");
	            m_preview.setBorder(BorderFactory.createEmptyBorder(0, (m_preview.getParent().getWidth()-w)/2, 0, (m_preview.getParent().getWidth()-w)/2));
			       
			}
	    	
	    });
	    
	    
	    tb.add(zoomOut);
	    //
	    getContentPane().add(tb, BorderLayout.NORTH);
	    
	    
	    //class
	    //create print preview dialog
	    m_preview = new PreviewContainer();
	    
	    
	   	//margin control
	    PageFormat pageFormat = new PageFormat();
	    Paper paper = pageFormat.getPaper();
	    final double MM_TO_PAPER_UNITS = 72.0/25.4;
	    final double widthA4 = 210*MM_TO_PAPER_UNITS;
	    final double heightA4 = 297*MM_TO_PAPER_UNITS;
	    paper.setSize(widthA4, heightA4); //paper.setSize(595, 842); // A4
	   	//
	    PrinterJob prnJob = PrinterJob.getPrinterJob();
	    //
	    
	    if (pageFormat.getHeight()==0 || pageFormat.getWidth()==0) {
	      System.err.println("Unable to determine default page size");
	        return;
	    }
	    
	     
	    // inital scale fit to page
	    int h = FRAME_HEIGHT-25;
 	   	int w = (int) (h/1.414f);
        
        int scale = 100*w/m_wPage;
        m_preview.setBorder(BorderFactory.createEmptyBorder(0, (FRAME_WIDTH-w)/2, 0, (FRAME_WIDTH-w)/2));
        int fit2 = 100*w/m_wPage;
        m_cbScale.setSelectedItem(Integer.toString(fit2)+" %");
        

	    int pageIndex = 0;
	    try {
	      while (true) {
	        BufferedImage img = new BufferedImage(m_wPage, m_hPage, BufferedImage.TYPE_INT_RGB);
	        Graphics g = img.getGraphics();
	        //
	        g.setColor(Color.white);
	        g.fillRect(0, 0, m_wPage, m_hPage);
	        g.setColor(Color.BLACK);
	        if (target.print(g, pageFormat, pageIndex) != Printable.PAGE_EXISTS)
	          break;
	        PagePreview pp = new PagePreview(w, h, img);
	        m_preview.add(pp);
	        pageIndex++;
	      }
	    }
	    catch (PrinterException e) {
	      e.printStackTrace();
	      System.err.println("Printing error: "+e.toString());
	    }
	    // create scroll pane and add print preview dilaog 
	    JScrollPane ps = new JScrollPane(m_preview);
	    getContentPane().add(ps, BorderLayout.CENTER);
	    ps.getVerticalScrollBar().setUnitIncrement(30);
	    //
	    addMouseListener(this);
	    
	    
	    // 
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    setSize(FRAME_WIDTH, FRAME_HEIGHT);
	    setLocationRelativeTo(null);
	    setVisible(true);
	    
	    //fitEx();
	  }
	  
	  public void createCursor(){
		
		  Toolkit toolkit = Toolkit.getDefaultToolkit();
		    final Image cursor = new ImageIcon(getClass().getResource("res/zoom-in.png")).getImage();
		    //toolkit.getBestCursorSize(16, 16);
		    Cursor mycursor=toolkit.createCustomCursor(cursor, new Point(0,0), "cursor");
		    //setCursor(mycursor);
		    m_preview.setCursor(mycursor);
		
		
		    
	  }
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}



class PreviewContainer extends JPanel{
	protected int H_GAP = 16;
    protected int V_GAP = 10;

    public Dimension getPreferredSize() {
      int n = getComponentCount();
      if (n == 0)
        return new Dimension(H_GAP, V_GAP);
      Component comp = getComponent(0);
      Dimension dc = comp.getPreferredSize();
      //
      int w = dc.width;
      int h = dc.height;
      //
      Dimension dp = getParent().getSize();
      int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
      int nRow = n/nCol;
      if (nRow*nCol < n)
        nRow++;

      int ww = nCol*(w+H_GAP) + H_GAP;
      int hh = nRow*(h+V_GAP) + V_GAP;
      //
      Insets ins = getInsets();
      return new Dimension(ww+ins.left+ins.right, hh+ins.top+ins.bottom);
    }

    public void setCursor(int crosshairCursor) {
		// TODO Auto-generated method stub
		
	}

	public Dimension getMaximumSize() {
      return getPreferredSize();
    }

    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    public void doLayout() {
      Insets ins = getInsets();
      int x = ins.left + H_GAP;
      int y = ins.top + V_GAP;

      int n = getComponentCount();
      if (n == 0)
        return;
      Component comp = getComponent(0);
      Dimension dc = comp.getPreferredSize();
      int w = dc.width;
      int h = dc.height;
            
      Dimension dp = getParent().getSize();
      int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
      int nRow = n/nCol;
      if (nRow*nCol < n)
        nRow++;

      int index = 0;
      for (int k = 0; k<nRow; k++) {
        for (int m = 0; m<nCol; m++) {
          if (index >= n)
            return;
          comp = getComponent(index++);
          comp.setBounds(x, y, w, h);
          x += w+H_GAP;
        }
        y += h+V_GAP;
        x = ins.left + H_GAP;
      }
    }
}



/******************************************************/
class PagePreview extends JPanel{
	    protected int m_w;
	    protected int m_h;
	    protected Image m_source;
	    protected Image m_img;

	    public PagePreview(int w, int h, Image source) {
	      m_w = w;
	      m_h = h;
	      //
	      m_source= source;
	      m_img = m_source.getScaledInstance(m_w, m_h, Image.SCALE_SMOOTH);
	      m_img.flush();
	      //
	      setBackground(Color.white);
	      setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
	    }

	    public void setScaledSize(int w, int h) {
	      m_w = w;
	      m_h = h;
	      m_img = m_source.getScaledInstance(m_w, m_h, Image.SCALE_SMOOTH);
	      repaint();
	    }

	    public Dimension getPreferredSize() {
	      Insets ins = getInsets();
	      return new Dimension(m_w+ins.left+ins.right, 
	        m_h+ins.top+ins.bottom);
	    }

	    public Dimension getMaximumSize() {
	      return getPreferredSize();
	    }

	    public Dimension getMinimumSize() {
	      return getPreferredSize();
	    }

	    public void paint(Graphics g) {
		      g.setColor(getBackground());
		      g.fillRect(0, 0, getWidth(), getHeight());
		      //
		      g.drawImage(m_img, -10, 0, this);
		            
		      paintBorder(g);
	    }
}
