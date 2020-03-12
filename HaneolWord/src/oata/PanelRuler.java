package oata;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class PanelRuler extends JPanel implements MouseListener, MouseMotionListener{
	
	ConPanel centerp;
	Font font;
	Point size;
	int width;
	
	//Rectangle me =new Rectangle(0, 0, this.WIDTH, this.HEIGHT);
	Rectangle me; 
	Rectangle drawZone;
		
	//border color
    Color _strokeColor = Color.BLACK;
    //background color
    Color _baseColor = Color.WHITE;
    
  //items of the ruler
    ArrayList<Rectangle2D> items = new ArrayList();
    ArrayList<Rectangle2D> tabs = new ArrayList();
    
    int pos = -1;
    Boolean mCaptured = false;
    //indicates if margins are used
    Boolean noMargins = false;
    //index of the captured object
    int capObject = -1, capTab = -1;
    
    Boolean _tabsEnabled=false;
    Cursor Cursor;
    
  //margins and indents in pixels (that is why float)
    private int lMargin = 20, rMargin = 15, llIndent = 20, luIndent = 20, rIndent = 55;        
    float rmargin, lumargin, lmargin;
    
    float dotsPermm=3.78f;
    
    private enum ControlItems
    {
        LeftIndent,
        LeftHangingIndent,
        RightIndent,
        LeftMargin,
        RightMargin
    }

    Rectangle2D workArea;
	public Insets insets;

	PanelRuler(ConPanel centerp){
    	//this.width = container.getWidth();
    	this.centerp = centerp;
    	this.width = 800;
    	me =new Rectangle(0, 0, this.width, 450);
    	drawZone = new Rectangle(1, 3, me.width - 2, 14);
    	workArea = new Rectangle2D.Float((float)lMargin * dotsPermm, 3f, drawZone.width - ((float)rMargin * dotsPermm) - drawZone.x * 2, 14f);
    	
    	//System.out.println(dpi);    	
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	items.add(new Rectangle2D.Float(0f, 3f, lMargin * dotsPermm, 14f));
        items.add(new Rectangle2D.Float(drawZone.width - ((float)rMargin * dotsPermm) + 1f, 3f, rMargin * dotsPermm + 5f, 14f));
        
    	//icons
    	items.add(new Rectangle2D.Float((float)luIndent * dotsPermm - 4.5f, 0f, 9f, 8f));
        items.add(new Rectangle2D.Float((float)llIndent * dotsPermm - 4.5f, 8.2f, 9f, 11.8f));
        items.add(new Rectangle2D.Float((float)(drawZone.width - rIndent), 11f, 9f, 8f));
        
        //regions for moving left indentation marks
        items.add(new Rectangle2D.Float((float)llIndent * dotsPermm - 4.5f, 8.2f, 9f, 5.9f));
        items.add(new Rectangle2D.Float((float)llIndent * dotsPermm - 4.5f, 14.1f, 9f, 5.9f));
        this.rmargin = drawZone.width - rIndent;
        this.lumargin = luIndent * dotsPermm - 4.5f;
        this.lmargin = llIndent * dotsPermm - 4.5f;
    	//System.out.println(this.rmargin);
    	
    }
    
    @Override public void paint(Graphics g) {
        //g.drawOval(50, 50, 50, 50); // <-- draws an oval on the panel
        //System.out.println("painted");
    	DrawBackGround(g);
        DrawMargins(g);
        DrawTextAndMarks(g);
        DrawIndents(g);
        DrawTabs(g);
        
    }
    
    //#region Painting
    private void DrawBackGround(Graphics g)
    {            
        //clear background
        //p.Color = Color.Transparent;
    	   	
    	g.setColor(Color.WHITE);
        g.fillRect(me.x, me.y, me.width, me.height);
        
        //fill background
        g.setColor(_baseColor);
        g.fillRect(drawZone.x, 0, drawZone.width, drawZone.height);
        //fill margins        
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, (int) this.lumargin, drawZone.height);
        g.setColor(new Color(245, 245, 245));
        g.fillRect((int) this.rmargin, 0, drawZone.width, drawZone.height);
    }

    private void DrawMargins(Graphics g)
    {            
    	Graphics2D g2d = (Graphics2D) g;
    	
        //items[0] = new Rectangle2D.Float(0f, 3f, lMargin * dotsPermm, 14f);
    	
        g2d.setColor(Color.DARK_GRAY);
        //g2d.draw(items.get(0));
        //
        //g2d.FillRectangle(p.Brush, items[1]);
        //Pen p = new Pen();
        
        //g2d.draw(items.get(1));
        //g.PixelOffsetMode = PixelOffsetMode.None;
        //draw border
        g2d.setColor(_strokeColor);
        g.drawRect(0, 0, me.width - 1, 14);            
    }

    private void DrawTextAndMarks(Graphics g)
    {            
        int points = (int)(drawZone.width / dotsPermm) / 10;
        float range = 5 * dotsPermm;
        int i = 0;
        //p.Color = Color.BLACK;
        
        //SizeF sz;
        
        Point sz = new Point(10,10);
        g.setColor(Color.DARK_GRAY);
        for (i = 0; i <= points * 2 + 1; i++)
        {
            if (i % 2 == 0 && i != 0)
            {
                //sz = g.MeasureString((Convert.ToInt32(i / 2)).ToString(), this.Font);
                String szt = String.valueOf(i / 2);
                //System.out.println(i/2);
                //g.DrawString(sz, PointF((float)(i * range - (float)(sz.Width / 2)), (float)(me.Height / 2) - (float)(sz.Height / 2)));
                g.setFont(new Font("Arial", Font.PLAIN, 9));
                g.drawString(String.valueOf(i / 2), (int) (i * range - (sz.x / 2)), 10);
                
                
            }
            else
            {
                
            	g.drawLine((int)(i * range), 5, (int)(i * range), 10);
            }
        }
        
    }
    private void DrawIndents(Graphics g)
    {
        int adj = 3;
     
        ImageIcon icon = new ImageIcon(getClass().getResource("res/l_indet_pos_upper.png"));
        Image l_indent_pos_upper = icon.getImage();
        g.drawImage(l_indent_pos_upper, (int)this.lumargin-adj, (int)items.get(2).getY(), this);
        
        ImageIcon icon2 = new ImageIcon(getClass().getResource("res/l_indent_pos_lower.png"));
        Image l_indent_pos_lower = icon2.getImage();
        g.drawImage(l_indent_pos_lower, (int)this.lmargin-adj, (int)items.get(3).getY(), this);
        //
        ImageIcon icon3 = new ImageIcon(getClass().getResource("res/r_indent_pos.png"));
        Image r_indent_pos = icon3.getImage();
        //System.out.println(drawZone.width - ((float)rIndent * dotsPermm - 4.5f) - 7f);
        
        g.drawImage(r_indent_pos, (int)this.rmargin-adj, (int)items.get(4).getY(), this);
    }


    private void DrawTabs(Graphics g)
    {
    	    	
        if (_tabsEnabled == false)
            return;

        int i = 0;

        if (tabs.size() == 0)
            return;

        for (i = 0; i <= tabs.size() - 1; i++)
        {
        	ImageIcon icon4 = new ImageIcon(getClass().getResource("res/tab_pos.png"));
            Image tab_pos = icon4.getImage();

            g.drawImage(tab_pos, (int)tabs.get(i).getX(), (int)tabs.get(i).getY(), this);
        }            
    }
    //#endregion
    
    private void AddTab(float pos)
    {
        Rectangle2D rect = new Rectangle2D.Float(pos, 10f, 8f, 8f);
        tabs.add(rect);
        //if (TabAdded != null)
        //    TabAdded.Invoke(CreateTabArgs(pos));
    }

    /// <summary>
    /// Returns List which contains positions of the tabs converted to millimeters.
    /// </summary>
    public ArrayList<Integer> TabPositions()
    {
        //get {
            ArrayList<Integer> lst = new ArrayList();
            int i = 0;
            for (i = 0; i <= tabs.size() - 1; i++)
            {
                lst.add((int)(tabs.get(i).OUT_BOTTOM / dotsPermm));
            }
            Collections.sort(lst);
            //lst.sort();
            return lst;
        //}
    }
    
    public ArrayList<Integer> TabPositionsInPixels()
    {
        
        
            ArrayList<Integer> lst = new ArrayList<Integer>();
            int i = 0;
            for (i = 0; i <= tabs.size() - 1; i++)
            {                    
                lst.add((int)(tabs.get(i).getX()));
            }
            Collections.sort(lst);
            return lst;
        
    }

    /// <summary>
    /// Sets positions for tabs. It uses positions represented in pixels.
    //
    public void SetTabPositionsInPixels(int[] positions)
    {
        if (positions == null)
        {
            tabs.clear();
        }
        else
        {
            tabs.clear();
            int i = 0;                 
            for (i = 0; i <= positions.length - 1; i++)
            {                    
                Rectangle2D rect = new Rectangle2D.Float(positions[i], 10f, 8f, 8f);
                tabs.add(rect);                    
            }                
        }
        this.repaint();
    }
    
    public void SetTabPositionsInMillimeters(int[] positions)
    {
        if (positions == null)
        {
            tabs.clear();
        }
        else
        {
            tabs.clear();
            int i = 0;
            Rectangle2D rect;
            for (i = 0; i <= positions.length - 1; i++)
            {
                if (positions[i] != 0)
                {
                    rect = new Rectangle2D.Float(positions[i] * dotsPermm, 10f, 8f, 8f);
                    tabs.add(rect);
                }
            }
            this.repaint();
        }
    }
    private int GetValueInPixels(ControlItems item)
    {
        switch (item)
        {
            case LeftIndent:
                return (int)(luIndent * dotsPermm);
                
            case LeftHangingIndent:
                return (int)(llIndent * dotsPermm);
                
            case RightIndent:
                return (int)(rIndent * dotsPermm);
                
            case LeftMargin:
                return (int)(lMargin * dotsPermm);
                
            case RightMargin:
                return (int)(rMargin * dotsPermm);
                
            default:
                return 0;
                
        }
    }
    public float DotsPerMillimeter()
    {
        return dotsPermm;
    }
    
    public Color BorderColor()
    {
        return _strokeColor;
        //_strokeColor = value; this.Refresh(); 
    }
    public Color BaseColor()
    {
        return _baseColor;
        
    }
    
    public Boolean NoMargins()
    {
        return noMargins;
        /*
        set 
        { 
            noMargins = value;
            if (value == true)
            {
                this.lMargin = 1;
                this.rMargin = 1;
            }
            this.Refresh(); 
        }
        */
    }
    public int LeftMargin()
    {
        return lMargin;
        /*
        set 
        {
            if (noMargins != true)
            {
                lMargin = value;
            }
            this.Refresh(); 
        }
        */
    }
    public int RightMargin()
    {
        return rMargin; 
        /*
        set 
        {
            if (noMargins != true)
            {
                rMargin = value;
            }
            this.Refresh();
        }
        */
    }
    public int LeftHangingIndent()
    {
        return llIndent - 1;
        /*
        set 
        {
            llIndent = value + 1;
            this.Refresh(); 
        }
        */
    }
    public int LeftIndent()
    {
        return luIndent - 1;
        /*
        set 
        {
            luIndent = value + 1;
            this.Refresh(); 
        }
        */
    }
    public int RightIndent()
    {
        return rIndent - 1;
        /*
        set 
        {
            rIndent = value + 1; 
            this.Refresh();
        }
        */
    }
    public Boolean TabsEnabled()
    {
        return _tabsEnabled;
        /*
        set { _tabsEnabled = value; this.Refresh(); }
        */
    }
    
    


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//right margin
		if (mCaptured == true)
		{
	        switch (capObject)
		        {
		    	case 2:
			   	     
		    		this.centerp.linex = e.getX();
		    		this.centerp.liney = this.centerp.getHeight();
		    		//this.centerp.leftIndent = e.getX();
		    		this.centerp.repaint();
		    		
		    		
	   				this.lumargin = (float)e.getX();
	   				if (this.lumargin>0)
	   				{
	   					this.lmargin = lumargin;
	   					items.set(3, new Rectangle2D.Float(this.lmargin, 8.2f, 9f, 11.8f));
		   				
	   				}
	   				//System.out.println("eee");
	   				items.set(2, new Rectangle2D.Float(this.lumargin, 0f, 9f, 8f));
	   				
	   				this.repaint();
	   				break;
	        	case 3:
	   	     
	   				this.lmargin = (float)e.getX();
	   				//System.out.println("eee");
	   				items.set(3, new Rectangle2D.Float(this.lmargin, 8.2f, 9f, 11.8f));
	   				this.repaint();
	   				break;
	            case 4:
		     
					this.rmargin = (float)e.getX();
					//System.out.println("eee");
					items.set(4, new Rectangle2D.Float(this.rmargin, 11f, 9f, 8f));
					this.repaint();
				break;
	        }
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
     		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
				

		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

		
		int i = 0;
		mCaptured = false;
        //process left mouse button only
        if (e.getButton() != MouseEvent.BUTTON1)
            return;
        
        float xbounds = 3f;
        float ybounds = 8f;
        
        for (i = 2; i <= 4; i++)
        {
        	System.out.println(items.get(i).getY());
        	//System.out.println(e.getY());
            if (items.get(i).getX()+xbounds > (float)e.getX() && items.get(i).getX()-xbounds <= (float)e.getX() 
            		&& items.get(i).getY()+ybounds >= (float)e.getY() && items.get(i).getY() < (float)e.getY() ) //i must not be equal to 3, as this is region for whole image
            {
               if (noMargins == true && (i == 0 || i == 1))
                    break;

                capObject = i;
                mCaptured = true;
                System.out.println(capObject);
                break;
            }
        }

        if (mCaptured == true)
            return;
		
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("ddd");
		//this.centerp.leftMargin = e.getX();
		this.centerp.leftMargin = e.getX();
		this.centerp.linex = 0;
		this.centerp.repaint();

	}
    
    
}

