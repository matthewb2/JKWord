package oata;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TableButton extends JButton implements MouseListener
{
    Color centerColor=Color.black;

    public TableButton()
    {
    	addMouseListener(this);
    	
    	
    }
    
    public void setSelectColor(Color selcolor){
    	centerColor = selcolor;
    	repaint();
    }
    
    @Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
    	invalidate();

		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		invalidate();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		invalidate();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		invalidate();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		invalidate();
	}
	public void paint(Graphics g){
		super.paint(g);
    	//
        Rectangle r = this.getBounds();
        //
        byte border = 4;
        byte right_border = 15;
        Rectangle rc = new Rectangle((int)(r.getX() + border), (int)(r.getY() + border),
        		(int)(r.getWidth() - border - right_border - 1), (int)(r.getHeight() - border * 2 - 1));

        // 
        g.setColor(Color.white);
        g.fillRect(border, border, rc.width, rc.height);
        Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        /* Underlining is easy */
        map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 11);
        font = font.deriveFont(map);
        g.setColor(centerColor);
        g.setFont(font);
        //g.drawString("가", 10, 15);
        try {
			g.drawImage(ImageIO.read(getClass().getResource("res/table.png")), 5, 2, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //
        g.setColor(Color.black);
        //draw the arrow
        Point p1 = new Point((int)(r.getWidth() - 9), (int)(r.getHeight() / 2 - 1));
        Point p2 = new Point((int)(r.getWidth() - 5), (int)(r.getHeight() / 2 - 1));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        p1 = new Point((int)(r.getWidth() - 8), (int)(r.getHeight() / 2));
        p2 = new Point((int)(r.getWidth() - 6), (int)(r.getHeight() / 2));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        p1 = new Point((int)(r.getWidth() - 7), (int)(r.getHeight() / 2));
        p2 = new Point((int)(r.getWidth() - 7), (int)(r.getHeight() / 2 + 1));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        //draw the divider line
        //
        g.setColor(Color.DARK_GRAY);
        p1 = new Point(r.width - 12, 4);
        p2 = new Point(r.width - 12, r.height - 5);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        //
        g.setColor(Color.lightGray);
        p1 = new Point(r.width - 11, 4);
        p2 = new Point(r.width - 11, r.height - 5);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
        
        
    }
}


