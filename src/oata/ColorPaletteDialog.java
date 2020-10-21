package oata;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorPaletteDialog extends JDialog implements MouseListener
{
	byte max = 40;	
	JPanel[] panel = new JPanel[40];	
	JPanel container = new JPanel(new FlowLayout());
	
	Color selectColor;
	
	//Color[] c2 = {new Color(255,0,
	Color[] color = {
			new Color(0,0,0), new Color(153,51,0), new Color(51,51,0), new Color(0,51,0),
			new Color(0,51,102), new Color(0,0,128), new Color(51,51,153), new Color(51,51,51),
			
			//row 2
			new Color(128,0,0), new Color(255,102,0), new Color(128,128,0), new Color(0,128,0),
			new Color(0,128,128), new Color(0,0,255), new Color(102,102,153), new Color(128,128,128),
			
			//row 3
			new Color(255,0,0), new Color(255,153,0), new Color(153,204,0), new Color(51,153,102),
			new Color(51,204,204), new Color(51,102,255), new Color(128,0,128), new Color(153,153,153),
			
			//row 4
			new Color(255,0,255), new Color(255,204,0), new Color(255,255,0), new Color(0,255,0),
			new Color(0,255,255), new Color(0,204,255), new Color(153,51,102), new Color(192,192,192),
			
			//row 5
			new Color(255,153,204), new Color(255,204,153), new Color(255,255,153), new Color(204,255,204),
			new Color(204,255,255), new Color(153,204,255), new Color(204,153,255), new Color(255,255,255)						
	};	
	
	String[] colorName = {
		"Black", "Brown", "Olive Green", "Dark Green", "Dark Teal", "Dark Blue", "Indigo", "Gray-80%",
		"Dark Red", "Orange", "Dark Yellow", "Green", "Teal", "Blue", "Blue-Gray", "Gray-50%",
		"Red", "Light Orange", "Lime", "Sea Green", "Aqua", "Light Blue", "Violet", "Gray-40%",
		"Pink", "Gold", "Yellow", "Bright Green", "Turquoise", "Sky Blue", "Plum", "Gray-25%",
		"Rose", "Tan", "Light Yellow", "Light Green", "Light Turquoise", "Pale Blue", "Lavender", "White"
	};	
	
	JButton moreColorsButton = new JButton();
	JButton cancelButton = new JButton();
	
	
	public ColorPaletteDialog(int x, int y)
	{
		setSize(158, 132);				
		//
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.darkGray));
		setResizable(false);
		 
		  
        setLayout( new BorderLayout() );  
        
		//
		setLocation(x, y);	
		
		BuildPalette();	
		//

		moreColorsButton.setText("기타 색 ...");			
		moreColorsButton.setSize(142, 22);
		moreColorsButton.setPreferredSize(new Dimension(142, 22));
		moreColorsButton.setLocation(5, 99);		
		//
		//moreColorsButton.setBorderPainted(false);
		//moreColorsButton.setFocusPainted(false);
		moreColorsButton.setContentAreaFilled(false);
		moreColorsButton.addMouseListener(new MouseListener() {
           

            @Override

            public void mouseClicked(MouseEvent e) {

            	Color jColor = selectColor;
   	         // open color dialog and select Color
   	         if ((jColor = JColorChooser.showDialog(null, "색상 선택"
   	         		+ "", jColor)) != null) {
   	             selectColor = jColor;
   	             // 

   	         }

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



		//moreColorsButton.FlatStyle = FlatStyle.Popup;
		add(container, BorderLayout.CENTER);
		add(moreColorsButton, BorderLayout.SOUTH);	
		
		//"invisible" button to cancel at Escape
		cancelButton.setPreferredSize(new Dimension(5, 5));
		//cancelButton.setLocation(-10, -10);		
		//cancelButton.Click += new EventHandler(cancelButton_Click);	
		cancelButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//Close();
				dispose();
				
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
		
		//this.add(cancelButton);	
		
	}
	
	
	
	void BuildPalette()
	{
		byte pwidth = 16;
		byte pheight = 16;
		byte pdistance = 2;		
		byte border = 5;		
		int x = border, y = border;	
		//ToolTip toolTip = new ToolTip();		
		
		for(int i = 0; i < max; i++)
		{
			panel[i] = new JPanel();			
			panel[i].setPreferredSize(new Dimension(pwidth, pheight));			
			panel[i].setLocation(x, y);
			//toolTip.SetToolTip(panel[i], colorName[i]);
					
			//this.Controls.Add(panel[i]);			
			container.add(panel[i]);
			
			if(x < ( 7 * (pwidth + pdistance)))
				x += pwidth + pdistance;
			else
			{
				x = border;
				y += pheight + pdistance;
			}	
				
			panel[i].setBackground(color[i]);
			panel[i].addMouseListener(this);
			
			//panel[i].Paint += new PaintEventHandler(OnPanelPaint);						
		}
	}
	/*
	void moreColorsButton_Click(object sender, System.EventArgs e)
    {    
    	ColorDialog colDialog = new ColorDialog();
    	colDialog.FullOpen = true;
    	if(colDialog.ShowDialog() == DialogResult.OK)    	
    	{
    		selectedColor = colDialog.Color;
    		DialogResult = DialogResult.OK;
    	}
    	colDialog.Dispose();
    	
    	Close();
    }
    
    void cancelButton_Click(object sender, System.EventArgs e)
    {        	
    	Close();
    }
		
	void OnMouseEnterPanel(object sender, EventArgs e)
	{			
		DrawPanel(sender, 1);
	}
	
	void OnMouseLeavePanel(object sender, EventArgs e)
	{			
		DrawPanel(sender, 0);	
	}	
	
	void OnMouseDownPanel(object sender, MouseEventArgs e)
	{				
		DrawPanel(sender, 2);				
	}
	
	void OnMouseUpPanel(object sender, MouseEventArgs e)
	{			
		Panel panel = (Panel)sender;		
		selectedColor = panel.BackColor;
		DialogResult = DialogResult.OK;
		Close();
	}
	*/
	
	void DrawPanel(MouseEvent e, byte state)
	{		
		JPanel panel = (JPanel)e.getSource();			
		
		Graphics g = panel.getGraphics();
		
		//Pen pen1, pen2;
		
		if(state == 1) 		//mouse over
		{
			//pen1 = new Pen( SystemColors.ControlLightLight ); 				
			//pen2 = new Pen( SystemColors. ControlDarkDark);		
		}
		else if(state == 2)	//clicked
		{
			//pen1 = new Pen( SystemColors.ControlDarkDark ); 				
			//pen2 = new Pen( SystemColors.ControlLightLight );						
		}
		else				//neutral
		{
			//pen1 = new Pen( SystemColors.ControlDark ); 				
			//pen2 = new Pen( SystemColors.ControlDark );
			
		}	
		
		Rectangle r = panel.getBounds();
		/*
		Point p1 = new Point( r.Left, r.Top ); 				//top left
		Point p2 = new Point( r.Right -1, r.Top );			//top right
		Point p3 = new Point( r.Left, r.Bottom -1 );		//bottom left
		Point p4 = new Point( r.Right -1, r.Bottom -1 );	//bottom right
		
		g.DrawLine( pen1, p1, p2 ); 		
		g.DrawLine( pen1, p1, p3 ); 		
		g.DrawLine( pen2, p2, p4 ); 		
		g.DrawLine( pen2, p3, p4 );
		*/ 				
	}
	
	void Paint(Graphics g)
	{					
		//DrawPanel(sender, 0);		
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		DrawPanel(e, (byte)2);
		setVisible(false);
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		DrawPanel(e, (byte)1);
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		DrawPanel(e, (byte)0);	
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		DrawPanel(e, (byte)2);
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//Close();
		JPanel panel = (JPanel)e.getSource();	
		
		selectColor = panel.getBackground();
		//
		
		//setVisible(false);
		//panel.setVisible(false);
		//System.out.println("fff");
	}



}
