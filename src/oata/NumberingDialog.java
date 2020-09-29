package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberingDialog  extends JDialog implements ItemListener{
	protected JRadioButton upleft, upcenter, upright;
	protected JRadioButton downleft, downcenter, downright;
	protected JRadioButton nonepage;
	
	protected PreviewNumber pageDes;
	
	public NumberingDialog(){
		 setTitle("쪽 번호 매기기");
		 //
	     JPanel container = new JPanel();
	     container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
     	 //
		 JPanel p1 = new JPanel();
		 GridBagLayout grid = new GridBagLayout();
		 p1.setLayout(grid);
		 
		 GridBagConstraints c = new GridBagConstraints();
		 
		 pageDes = new PreviewNumber();
		 pageDes.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		 pageDes.setPreferredSize(new Dimension(100, 141));
		 
		 
		 upleft = new JRadioButton();
		 upleft.addItemListener(this);
		 upcenter = new JRadioButton();
		 upcenter.addItemListener(this);
		 upright = new JRadioButton();
		 upright.addItemListener(this);
		 downleft = new JRadioButton();
		 downleft.addItemListener(this);
		 downcenter = new JRadioButton();
		 downcenter.addItemListener(this);
		 downright = new JRadioButton();
		 downright.addItemListener(this);
		 
		 nonepage = new JRadioButton();
		 nonepage.addItemListener(this);
		 
		 ButtonGroup group = new ButtonGroup();
	     group.add(upleft);
	     group.add(upcenter);
	     group.add(upright);
	     group.add(downleft);
	     group.add(downcenter);
	     
	     downcenter.setSelected(true);
	     group.add(downright);
	     group.add(nonepage);
	     
		 c.gridx=0; 
		 c.gridy=0;
		 c.ipadx = 30;      //make this component tall
		 grid.setConstraints(upleft,c);
		 c.gridx=1; 
		 c.gridy=0;
		 c.ipadx = 30;      //make this component tall
		 grid.setConstraints(upcenter,c);
		 
		 c.gridx=2; 
		 c.gridy=0;
		 c.ipadx = 0;      //make this component tall
		 grid.setConstraints(upright,c);
		 
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.ipady = 40;      //make this component tall
		 c.weightx = 0.0;
		 c.gridwidth = 3;
		 c.gridx = 0;
		 c.gridy = 1;
		 grid.setConstraints(pageDes,c);
		 
		 c.gridx=0; 
		 c.gridy=2;
		 c.ipadx = 30;      //make this component tall
		 //
		 c.gridwidth = 1;   
		 c.ipady = 0;       //reset to default
		 grid.setConstraints(downleft,c);
		 
		 c.gridx=1; 
		 c.gridy=2;
		 c.ipadx = 30;      //make this component tall
		 c.ipady = 0;       //reset to default
		 //
		 grid.setConstraints(downcenter,c);
		 
		 c.gridx=2; 
		 c.gridy=2;
		 c.ipady = 0;       //reset to default
		 c.ipadx = 0;      //make this component tall
		 //
		 grid.setConstraints(downright,c);
		 
		 JPanel bottom = new JPanel();
		 //
		 JLabel tm = new JLabel("쪽 번호 없음");
		 
		 bottom.add(nonepage);
		 bottom.add(tm);
		
		 JPanel po = new JPanel();
		 po.setBorder(BorderFactory.createTitledBorder("번호위치"));
		 po.setLayout(new BoxLayout(po, BoxLayout.Y_AXIS));
	 
		 p1.add(upleft);
		 p1.add(upcenter);
		 p1.add(upright);
		 p1.add(pageDes);
		 p1.add(downleft);
		 p1.add(downcenter);
		 p1.add(downright);
		 
		 
		 //
		 bottom.setAlignmentX(Component.LEFT_ALIGNMENT);
		 po.add(p1);
		 //po.add(bottom);
		 //
		 JPanel p3 = new JPanel();
		 JButton btOK = new JButton("확인");
		 ActionListener lst = new ActionListener() { 
		   public void actionPerformed(ActionEvent e) {
		     setVisible(false);
		   }
		 };
		 btOK.addActionListener(lst);
		 p3.add(btOK);
		 //
		 JButton btCancel = new JButton("취소");
			 lst = new ActionListener() { 
			   public void actionPerformed(ActionEvent e) {
			     //
			 	 dispose();
			   }
			 };
			 btCancel.addActionListener(lst);
	     p3.add(btCancel);
	     //
	     
		 container.add(po);
		 //
		 
		 JPanel p = new JPanel();
		 p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		 p.setBorder(BorderFactory.createTitledBorder("번호모양"));
		 //gridlayout is important
	 	 JPanel tt1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 JLabel papert = new JLabel("종류");
		 final JComboBox paperType = new JComboBox(new String[] {"1", "-1-", "1쪽", "1페이지", "쪽 번호 없음"});
		 paperType.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e)
		          {
		    	   pageDes.setType(paperType.getSelectedIndex());
		    	   pageDes.repaint();
		          }
		 });
		 
		 tt1.add(papert);
		 tt1.add(paperType);
		 p.add(tt1);
		 
		 container.add(p);
		 container.add(p3);
		 getContentPane().add(container);
		 //
		 pack();
		 setSize(400, 450);
		 setResizable(false);
		 setLocationRelativeTo(this);
		 setVisible(true);
	
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		
		// TODO Auto-generated method stub
		if(upleft.isSelected()) pageDes.setPos(0);
		else if (upcenter.isSelected()) pageDes.setPos(1);
		else if (upright.isSelected()) pageDes.setPos(2);
		else if (downleft.isSelected()) pageDes.setPos(3);
		else if (downcenter.isSelected()) pageDes.setPos(4);
		else if (downright.isSelected()) pageDes.setPos(5);
		else if (nonepage.isSelected()) pageDes.setPos(6);

	    pageDes.repaint();
	}
	
}

class PreviewNumber extends JPanel {
	
	int pos;
	int type;
	//
	public void setPos(int pos){
		this.pos = pos;
	}
	public void setType(int type){
		this.type = type;
		System.out.println(this.type);
	}
	
	PreviewNumber()
	{}
	
	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		//
		g.setColor(Color.black);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
				
		String str = null;
		if (this.type == 0)	str = "1";
		else if (this.type == 1) str = "-1-";
		else if (this.type == 2) str = "1쪽";
		else if (this.type == 3) str = "1페이지";
		else if (this.type == 4) str = "";
		
		switch(pos){
			case 0:
				g.drawString(str,  5,  10);
				break;
			case 1:
				g.drawString(str,  this.getWidth()*2/3-23-str.length()*3,  10);
				break;
			case 2:
				g.drawString(str,  this.getWidth()-13-str.length()*3,  10);
				break;
			case 3:
				g.drawString(str,  5-str.length()*3, this.getHeight()-10);
				break;
			case 4:
				g.drawString(str,  this.getWidth()*2/3-23-str.length()*3,  this.getHeight()-10);
				break;
			case 5:
				g.drawString(str,  this.getWidth()-12-str.length()*3,  this.getHeight()-10);
				break;	
		}
		
			
	}

}
