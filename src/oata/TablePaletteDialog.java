package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class TablePaletteDialog extends JDialog implements MouseListener, MouseMotionListener
{
	byte max = 42;	
	JPanel[] panel = new JPanel[max];	
	int pwidth, pheight, pdistance, border;
	int col, row;
	JPanel container;
	
	JButton moreColorsButton = new JButton();
	
	public TablePaletteDialog(int x, int y)
	{
		setSize(155, 155);				
		//
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.black));
		setResizable(false);
        setLayout( new BorderLayout() );  
		setLocation(x, y);
		
		container = new JPanel();
		container.setLayout(new FlowLayout());
		FlowLayout layout = (FlowLayout)container.getLayout();
	    layout.setVgap(0);
	    layout.setHgap(0);
		
		BuildPalette();
		//
		moreColorsButton.setText("기타 ...");			
		moreColorsButton.setSize(142, 22);
		moreColorsButton.setPreferredSize(new Dimension(142, 22));
		moreColorsButton.setLocation(5, 99);		
		//
		moreColorsButton.setContentAreaFilled(false);
		moreColorsButton.addMouseListener(new MouseListener() {
        
            @Override

            public void mouseClicked(MouseEvent e) {

             //	Color jColor = selectColor;

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

			});
		container.setBackground(Color.white);
		add(container, BorderLayout.CENTER);
		add(moreColorsButton, BorderLayout.SOUTH);
		
		container.addMouseMotionListener(this);
		
	}
	
	
	
	void BuildPalette()
	{
		pwidth = 21;
		pheight = 21;
		pdistance = 0;		
		border = 0;		
		int x = border, y = border;	
		
		for(int i = 0; i < max; i++)
		{
			panel[i] = new JPanel();			
			panel[i].setPreferredSize(new Dimension(pwidth, pheight));			
			
			if(x < ( 7 * (pwidth + pdistance))){
				x += pwidth + pdistance-1;
				System.out.println(x);
			}
			else
			{
				x = border;
				y += pheight + pdistance;
			}	
				
			panel[i].setBackground(Color.white);
			panel[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
			//
			panel[i].addMouseListener(this);
			panel[i].addMouseMotionListener(this);
			
			container.add(panel[i]);
			panel[i].setLocation(x, y);
			
			
			//						
		}
	}
	
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		setVisible(false);
		
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
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		setVisible(false);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
		int x = e.getLocationOnScreen().x-container.getLocationOnScreen().x;
		int y = e.getLocationOnScreen().y-container.getLocationOnScreen().y;
		//
		for (int i=0; i<max; i++){
			//
			if (panel[i].getLocation().x <= x && panel[i].getLocation().y <= y){
				//
				panel[i].setBackground(Color.blue);
				int col = i%7+1;
				int row = i/7+1;
				moreColorsButton.setText(row+"X"+col);
				panel[i].setBorder(BorderFactory.createLineBorder(Color.white));
			} else {
				panel[i].setBackground(Color.white);
				panel[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
			}
			
		}
		
	}



	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



}