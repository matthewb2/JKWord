package oata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

public class SpinSlider extends JPanel{
	final JSpinner spinner = new JSpinner();
    final JSlider slider = new JSlider();
    
	//JTextPane owner;
	public void update(double value) {
		spinner.setValue((int)(value*100));
		slider.setValue((int)(value*100));
		//System.out.println(value);
	}
	public SpinSlider(final NeoHaneol owner) {
		//owner = owner;
		int MIN = 0;
		int MAX = 200;
		
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(120, 20));
        this.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
        
        slider.setMinimum(MIN);
        slider.setValue(100);
        slider.setMaximum(MAX);
        //
        slider.setPreferredSize(new Dimension(115, 20));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider s = (JSlider) e.getSource();
                spinner.setValue(s.getValue());
                //
                //String tmp = zoomlabel.getText();
     	        double scale =  (double)s.getValue()/100.;
     	        System.out.println(scale);
                //
     	        owner.zoomCombo.setSelectedItem(Integer.toString((int) (scale*100))+"%");
                owner.m_monitor.getDocument().putProperty("ZOOM_FACTOR", scale);
                try {
					owner.adjustmainTextPane();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                // update ruler
                owner.updateVar();
                owner.repaint();
    	            
            }
        });
        //this.add(slider);
        spinner.setModel(new SpinnerNumberModel(100, MIN, MAX, 1));
        //
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "0'%'"));
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner s = (JSpinner) e.getSource();
                slider.setValue((Integer) s.getValue());
            }
        });
        
        //
    	JButton b=new JButton(new ImageIcon(getClass().getResource("res/minus-white-icon.png")));
		b.setSize(20, 20);
		b.setPreferredSize(new Dimension(20, 20));
		b.setOpaque(false);
		JButton c=new JButton(new ImageIcon(getClass().getResource("res/plus-white-icon.png")));
		c.setSize(20, 20);
		c.setPreferredSize(new Dimension(20, 20));
		c.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				slider.setValue(slider.getValue()+1);
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
		b.setBorderPainted(false);
		b.setFocusPainted(false);		
		b.setContentAreaFilled(false);
		b.addMouseListener(new MouseListener(){
	    		@Override
	    		public void mouseClicked(MouseEvent evt) {
	    			// TODO Auto-generated method stub
	    			slider.setValue(slider.getValue()-1);
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
		 
		c.setContentAreaFilled(false);
		Box row = Box.createHorizontalBox();
		row.add(b);
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
        JPanel inner = new JPanel(new BorderLayout());
		inner.add(b, BorderLayout.WEST);
		inner.add(slider, BorderLayout.CENTER);
        //
		inner.add(c, BorderLayout.EAST);
		container.add(inner, BorderLayout.CENTER);
        container.add(spinner, BorderLayout.EAST);
        add(container);
    }
}
