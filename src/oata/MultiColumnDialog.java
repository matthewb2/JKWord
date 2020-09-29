package oata;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;



public class MultiColumnDialog extends JDialog
{

	protected JEditorPane m_owner;
	protected JTabbedPane m_tb;
	protected JTextField m_txtFind1;
	protected JTextField m_txtFind2;
	protected Document m_docFind;
	protected Document m_docReplace;
	protected ButtonModel m_modelWord;
	protected ButtonModel m_modelCase;
	protected ButtonModel m_modelUp;
	protected ButtonModel m_modelDown;
	public int m_colCount;
	
	protected PagePreview apple2;

	public MultiColumnDialog(JEditorPane owner) {
		 //super(owner, "다단", true);
		 //
		 m_owner = owner;
		 
		 getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		 JPanel p = new JPanel();
		 p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		 
		 JPanel lp = new JPanel();
		 JLabel lbb1 = new JLabel("단 개수");
		 lbb1.setPreferredSize(new Dimension(80,  20));
		 final JSpinner hours = new JSpinner(new SpinnerNumberModel(2, 1, 12, 1));
		 hours.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					apple2.setMCount((int) hours.getValue());
				}
	     });
		 lp.add(lbb1);
		 lp.add(hours);
		 
		 
		 p.add(lp);
		 
		 JPanel lp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 JCheckBox apple = new JCheckBox("구분선 넣기");
		 
		 apple.addItemListener(new ItemListener() {
			    @Override
			    public void itemStateChanged(ItemEvent e) {
			        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
			            //do something...
			        	apple2.setvline(true);
		                
		            }
			        else {//checkbox has been deselected
			            //do something...
			        	apple2.setvline(false);
			        }
			        apple2.repaint();
			    }
			});
		 lp2.add(apple);
		 
		 
		 p.add(lp2);
		 
		 JPanel lp3 = new JPanel();
		 apple2 = new PagePreview();
		 apple2.setBorder(new LineBorder(Color.darkGray));
		 apple2.setPreferredSize(new Dimension(100, 141));
		 lp3.add(apple2);
		 
		 
		 p.add(lp3);
		 
		 JPanel lp4 = new JPanel();
		 lp4.setPreferredSize(new Dimension(100, 14));
		 
		 p.add(lp4);
		 
		 JButton btn = new JButton("확인(O)");
		 
	     btn.setSize(new Dimension(200, 100));
		 btn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e){
		    	   	//m_rowCount = (int) hours.getValue();
		    	   	m_colCount = (int) hours.getValue();
		    	    
		            setVisible(false);
		       }
		 });
		 JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
		 p1.add(btn);
		 JButton btn2 = new JButton("취소(C)");
		 
		 btn2.setSize(new Dimension(200, 100));
		 btn2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		             dispose();
		    }
		 });
		 p1.add(btn2);
		 //getContentPane().add(p);
		 p.add(p1);
		 getContentPane().add(p);
		 
		 
		 pack();
		 setResizable(false);
		 setLocationRelativeTo(this);
		 //setSize(200, 100);
		 setVisible(true);	

	}

class PagePreview extends JPanel {
	
	int mcount;
	int width;
	int height;
	Boolean vline=false;
	
	PagePreview()
	{
		setMCount(2);
		setvline(false);
		repaint();
	}
	public void setMCount(int value){
		mcount = value;
		this.repaint();
	}
	
	public void setvline(Boolean value){
		vline = value;
		
	}
	
	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.lightGray);
		//g.drawLine(0, top/3, this.getWidth(), top/3);
		//g.drawLine(left/3, 0, left/3, this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        for (int i=0; i<14; i++){
        	if (mcount == 2){
        	  g2.draw(new Line2D.Float(5, 5+i*10, 45, 5+i*10));
        	  g2.draw(new Line2D.Float(55, 5+i*10, 95, 5+i*10));
        	} else if (mcount == 3){
        	  g2.draw(new Line2D.Float(5, 5+i*10, 30, 5+i*10));
          	  g2.draw(new Line2D.Float(38, 5+i*10, 63, 5+i*10));
          	  g2.draw(new Line2D.Float(70, 5+i*10, 95, 5+i*10));
        	}
        }
        if (vline){
          g2.setStroke(new BasicStroke(1));
          g.setColor(Color.darkGray);
          g.drawLine(50, 2, 50, 138);
        }
	}

}

}


