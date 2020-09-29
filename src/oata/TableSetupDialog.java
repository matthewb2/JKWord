package oata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

public class TableSetupDialog extends JDialog{

public Insets m_margin;


public int m_rowCount = -1;
public int m_colCount = -1;


public JButton btOK;
public static int TOP=50, LEFT=50, BOTTOM=50, RIGHT=50;

	public TableSetupDialog(final JEditorPane owner){
		 //m_owner = owner;
		 JPanel lb = new JPanel();
		 lb.setBorder(BorderFactory.createTitledBorder(""));
		 lb.setLayout(new BoxLayout(lb, BoxLayout.Y_AXIS));
		 JPanel lp = new JPanel(new FlowLayout());
	   
		 JLabel lbb1 = new JLabel("줄수");
		 lbb1.setPreferredSize(new Dimension(80,  20));
		 final JSpinner hours = new JSpinner(new SpinnerNumberModel(2, 1, 12, 1));
		 lp.add(lbb1);
		 lp.add(hours);
		 JPanel lp2 = new JPanel(new FlowLayout());
		 JLabel lbb2 = new JLabel("칸수");
		 lbb2.setPreferredSize(new Dimension(80,  20));

		 final JSpinner hours2 = new JSpinner(new SpinnerNumberModel(2, 1, 12, 1));
		 lp2.add(lbb2);
		 lp2.add(hours2);
		 lb.add(lp);
		 lb.add(lp2);
		 //
		 JPanel pagePanel = new JPanel();
		 pagePanel.setLayout(new GridLayout(1,2));
		 pagePanel.setPreferredSize(new Dimension(100,  20));
		 
		 JButton btn = new JButton("확인(O)");
		 
	     btn.setSize(new Dimension(200, 100));
		 btn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e){
		    	   	m_rowCount = (int) hours.getValue();
		    	   	m_colCount = (int) hours2.getValue();
		            setVisible(false);
		       }
		 });
		 pagePanel.add(btn);
		 JButton btn2 = new JButton("취소(C)");
		 
		 btn2.setSize(new Dimension(200, 100));
		 btn2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		             dispose();
		    }
		 });
		 pagePanel.add(btn2);
		 lb.add(pagePanel);
		 this.add(lb);
	     pack();
		 setResizable(false);
		 setLocationRelativeTo(this);
		 //setSize(200, 150);
		 setVisible(true);		 
		 
		}

}
