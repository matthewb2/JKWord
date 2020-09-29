package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MutableAttributeSet;

public class PageSetupDialog extends JDialog {

public Insets m_margin;
public int pageGaroSero;

public JButton btOK;
public static int TOP=50, LEFT=50, BOTTOM=50, RIGHT=50;

public Preview pageDes;
public JPanel pageContainer;

public PageSetupDialog(NeoHaneol mf,String title,boolean modal)
{
	 super(mf,title,modal);
 	 getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	 JPanel p = new JPanel();
	 p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	 pageContainer = new JPanel(new GridBagLayout());
	 //
	 pageContainer.setBorder(new LineBorder(Color.black));
	 pageContainer.setPreferredSize(new Dimension(170,170));
	 pageContainer.setAlignmentY(Component.CENTER_ALIGNMENT);
	 
	 pageDes = new Preview(TOP, LEFT, BOTTOM, RIGHT);
	 pageDes.setBorder(new LineBorder(Color.darkGray));
	 pageDes.setAlignmentY(Component.CENTER_ALIGNMENT);
	 
	 p.setBorder(BorderFactory.createTitledBorder("용지종류"));
	 //gridlayout is important
  	 JPanel tt1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
	 JLabel papert = new JLabel("종류");
	 JComboBox paperType = new JComboBox(new String[] {"A4(국배판) [210 x 297 mm]", "75%",
             "100%", "125%", "150%", "200%"});	
	 tt1.add(papert);
	 tt1.add(paperType);
	 p.add(tt1);
	 JPanel tt2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
	 JLabel lbb1 = new JLabel("폭");
     //Add the first label-spinner pair.
	 JSpinner hours = new JSpinner(new SpinnerNumberModel(210, 100, 300, 1));
	 JLabel lbb2 = new JLabel("너비");
	 JSpinner hours2 = new JSpinner(new SpinnerNumberModel(297, 100, 400, 1));
	 tt2.add(lbb1);
	 tt2.add(hours);
	 tt2.add(lbb2);
	 tt2.add(hours2);
	 p.add(tt2);
	 getContentPane().add(p);
	 //
	 JPanel pageDirection = new JPanel(new GridLayout(2, 4));
   	 pageDirection.setBorder(BorderFactory.createTitledBorder("용지방향"));
   	 pageDirection.setPreferredSize(new Dimension(this.getWidth(), 100));
   	 //
  	 Icon horizontalIcon = new ImageIcon(getClass().getResource("res/File-Horizontal-icon.png"));
   	 JButton horizontal = new JButton(horizontalIcon);
   	 horizontal.setAlignmentX(Component.CENTER_ALIGNMENT);
   	 horizontal.setBorderPainted(false);
   	 horizontal.setFocusPainted(false);
   	 horizontal.setContentAreaFilled(false);
   	 Icon verticalIcon = new ImageIcon(getClass().getResource("res/File-icon.png"));
   	 JButton vertical = new JButton(verticalIcon);
   	 vertical.setAlignmentX(Component.CENTER_ALIGNMENT);
   	 vertical.setBorderPainted(false);
   	 vertical.setFocusPainted(false);
   	 vertical.setContentAreaFilled(false);
  	
   	 pageDirection.add(vertical);
   	 pageDirection.add(horizontal);
   	 JPanel tt4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
   	 JRadioButton seroButton = new JRadioButton("세로(V)");
   	 seroButton.setMnemonic('V');
     seroButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	
     seroButton.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
			        // Your selected code here.
					pageDes.setGaroSero(0);
					pageGaroSero = 0;
					//
					pageDes.setPreferredSize(new Dimension(100, 141));
					pageContainer.repaint();
					pageContainer.revalidate();
			    }
				
			}
     });
     JRadioButton garoButton = new JRadioButton("가로(H)");
     garoButton.setMnemonic('H');

     garoButton.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
			        // Your selected code here.
					pageDes.setGaroSero(1);
					pageGaroSero = 1;
					pageDes.setPreferredSize(new Dimension(141, 100));
					pageContainer.revalidate();
					pageContainer.repaint();
			    }
				
			}
     });
     JPanel tt5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
     garoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
     tt4.add(seroButton);
     tt5.add(garoButton);
     //
     ButtonGroup group = new ButtonGroup();
     group.add(seroButton);
     group.add(garoButton);
     seroButton.setSelected(true);
     
     //
     pageDirection.add(vertical);
     pageDirection.add(horizontal);
   	 pageDirection.add(new JLabel());
     pageDirection.add(tt4);
     pageDirection.add(tt5);
     pageDirection.add(new JLabel());
  	 getContentPane().add(pageDirection);
	 // preview
	 p = new JPanel();
	 GridBagLayout grid = new GridBagLayout();
	 p.setLayout(grid);
	 p.setBorder(BorderFactory.createTitledBorder("용지여백"));
	 GridBagConstraints constraints = new GridBagConstraints();
	
	 JPanel top = new JPanel();
	 final JSpinner hours3 = new JSpinner(new SpinnerNumberModel(TOP, 0, 300, 1));
	 hours3.setPreferredSize(new Dimension(100, 20));
	 JComponent comp = hours3.getEditor();
	 JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
	 DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
	 formatter.setCommitsOnValidEdit(true);
	 top.add(hours3);
	 
	 JPanel left = new JPanel();
	 final JSpinner hours4 = new JSpinner(new SpinnerNumberModel(LEFT, 0, 300, 1));
	 hours4.setPreferredSize(new Dimension(100, 20));
	 JComponent comp2 = hours4.getEditor();
	 JFormattedTextField field2 = (JFormattedTextField) comp2.getComponent(0);
	 DefaultFormatter formatter2 = (DefaultFormatter) field2.getFormatter();
	 formatter2.setCommitsOnValidEdit(true);
	 left.add(hours4);
	 //
	 JPanel bottom = new JPanel();
	 final JSpinner hours5 = new JSpinner(new SpinnerNumberModel(BOTTOM, 0, 300, 1));
	 hours5.setPreferredSize(new Dimension(100, 20));
	 JComponent comp3 = hours5.getEditor();
	 JFormattedTextField field3 = (JFormattedTextField) comp3.getComponent(0);
	 DefaultFormatter formatter3 = (DefaultFormatter) field3.getFormatter();
	 formatter3.setCommitsOnValidEdit(true);
	 bottom.add(hours5);
	 //
	 JPanel right = new JPanel();
	 final JSpinner hours6 = new JSpinner(new SpinnerNumberModel(RIGHT, 0, 300, 1));
	 hours6.setPreferredSize(new Dimension(100, 20));
	 JComponent comp4 = hours6.getEditor();
	 JFormattedTextField field4 = (JFormattedTextField) comp4.getComponent(0);
	 DefaultFormatter formatter4 = (DefaultFormatter) field4.getFormatter();
	 formatter4.setCommitsOnValidEdit(true);
	 right.add(hours6);
	 pageContainer.add(pageDes);
	 //
	 hours3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				pageDes.setTop((int) hours3.getValue());
			}
     });
	 hours4.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			pageDes.setLeft((int) hours4.getValue());
		}
     });
	 hours5.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			pageDes.setRight((int) hours5.getValue());
		}
     });

	 hours6.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			pageDes.setBottom((int) hours6.getValue());
		}
     });
	 //Adding the button1 to 0th x-coordinate of 0th column, at WEST
	 constraints.gridx=1; 
	 constraints.gridy=0;
	 constraints.fill = GridBagConstraints.HORIZONTAL;
	 grid.setConstraints(top,constraints);
	 constraints.gridx=0; 
	 constraints.gridy=1;
	 grid.setConstraints(left,constraints);
	 constraints.gridx=1; 
	 constraints.gridy=1;
	 //
	 constraints.ipady = 0;      //make this component tall
	 grid.setConstraints(pageContainer,constraints);
	 //
	 constraints.gridx=2; 
	 constraints.gridy=1;
	 constraints.ipadx = 0;      //reset to default
	 constraints.ipady = 0;      //reset to default
	 grid.setConstraints(bottom,constraints);
	 constraints.gridx=1; 
	 constraints.gridy=2;
	 //
	 grid.setConstraints(right,constraints);
	 p.add(new JLabel());
	 p.add(top);
	 p.add(new JLabel());
	 p.add(left);
	 p.add(pageContainer);
	 p.add(bottom);
	 //
	 p.add(new JLabel());
	 p.add(right);
	 p.add(new JLabel());
	 getContentPane().add(p);
	 p = new JPanel(new FlowLayout());
	 JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
	 btOK = new JButton("확인");
		 ActionListener lst = new ActionListener() { 
		   public void actionPerformed(ActionEvent e) {
		 	 m_margin = new Insets(pageDes.top, pageDes.left, pageDes.bottom, pageDes.right);
		     setVisible(false);
		   }
		 };
	 btOK.addActionListener(lst);
	 p1.add(btOK);
	 //
	 JButton btCancel = new JButton("취소");
		 lst = new ActionListener() { 
		   public void actionPerformed(ActionEvent e) {
		     //
		 	 dispose();
		   }
		 };
	 btCancel.addActionListener(lst);
	 p1.add(btCancel);
	 //
	 p.add(p1);
	 getContentPane().add(p);
	 //
	 pack();
	 setSize(450, 650);
	 setResizable(false);
	 setLocationRelativeTo(this);
	 setVisible(true);
	 
	}
}

class Preview extends JPanel {
	int top;
	int left;
	int right;
	int bottom;
	int width;
	int height;
	Image docimg;
	int garosero;
	
	Preview(int top, int left, int right, int bottom)
	{
				
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		
	}
	public void setTop(int value){
		top = value;
		height = this.getHeight()-top-bottom;
		this.repaint();
	}
	public void setLeft(int value){
		left = value;
		this.repaint();
	}
	public void setRight(int value){
		right = value;
		this.repaint();
	}
	public void setBottom(int value){
		bottom = value;
		height = this.getHeight()-top-bottom;
		this.repaint();
	}
	public void setGaroSero(int value){
		garosero = value;
		if (garosero == 1){
			ImageIcon icon = new ImageIcon(getClass().getResource("res/docpage2.png"));
		    this.docimg = icon.getImage();
		    setPreferredSize(new Dimension(141, 100));
		}
		this.repaint();
	}
	
	public void paint(Graphics g){
		if (garosero == 0){

			ImageIcon icon = new ImageIcon(getClass().getResource("res/docpage.png"));
		    this.docimg = icon.getImage();
			
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.lightGray);
			//
			g.drawLine(0, top/3, this.getWidth(), top/3);
			g.drawLine(left/3, 0, left/3, this.getHeight());
			g.drawLine(this.getWidth()-right/3, 0, this.getWidth()-right/3, this.getHeight());
			g.drawLine(0, this.getHeight()-bottom/3, this.getWidth(), this.getHeight()-bottom/3);
			g.drawImage(docimg, left/3+1, top/3+1, this.getWidth()-right/3-left/3-1, this.getHeight()-bottom/3-top/3-1, null);
		} else if (garosero ==1){
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.lightGray);
			ImageIcon icon = new ImageIcon(getClass().getResource("res/docpage2.png"));
		    this.docimg = icon.getImage();
			//
			g.drawLine(0, top/3, this.getWidth(), top/3);
			g.drawLine(left/3, 0, left/3, this.getHeight());
			g.drawLine(this.getWidth()-right/3, 0, this.getWidth()-right/3, this.getHeight());
			g.drawLine(0, this.getHeight()-bottom/3, this.getWidth(), this.getHeight()-bottom/3);
			g.drawImage(docimg, left/3+1, top/3+1, this.getWidth()-right/3-left/3-1, this.getHeight()-bottom/3-top/3-1, null);
		}
	}

}
