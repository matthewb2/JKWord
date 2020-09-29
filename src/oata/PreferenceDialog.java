package oata;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PreferenceDialog extends JDialog{
	
	protected NeoHaneol m_owner;
	protected String lf;
	
	public String getlf(){
		return this.lf;
	}
	
	public PreferenceDialog(NeoHaneol owner){
		 super();
		 m_owner = owner;
		 JPanel lb = new JPanel();
		 lb.setBorder(BorderFactory.createTitledBorder(""));
		 lb.setLayout(new BoxLayout(lb, BoxLayout.Y_AXIS));
	     JPanel lp = new JPanel(new FlowLayout());
	     JLabel lbb1 = new JLabel("테마");
		 lbb1.setPreferredSize(new Dimension(80,  20));
		 final JComboBox zoomCombo = new JComboBox(new String[] {"윈도우", "메탈", "모티프",  "GTK", "200%"});
		 lp.add(lbb1);
		 lp.add(zoomCombo);
		 JCheckBox apple = new JCheckBox("쪽 그림자");
		 lb.add(lp);
		 JPanel lp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 //
		 lp2.add(apple);
		 lb.add(lp2);
		 JPanel pagePanel = new JPanel();
		 pagePanel.setLayout(new GridLayout(1,2));
		 pagePanel.setPreferredSize(new Dimension(100,  20));
		 JButton btn = new JButton("확인(O)");
		 btn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e)
		          {
		    	   	if((String) zoomCombo.getSelectedItem() =="윈도우") {
						lf = UIManager.getSystemLookAndFeelClassName();
					}
	            	else if ((String) zoomCombo.getSelectedItem() == "모티프")	{
	            		lf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	            	}
	            	else if ((String) zoomCombo.getSelectedItem() == "메탈")	{
	            		lf = "javax.swing.plaf.metal.MetalLookAndFeel";
	            	}
	            	else if ((String) zoomCombo.getSelectedItem() == "GTK"){
	            		lf = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	            	}
		    	   	
		    	   	Properties prop = new Properties();
		    	    
		        	try {
		        	//set the properties value
		        	prop.setProperty("theme", lf);
		        	FileOutputStream output = new FileOutputStream(new File(getClass().getResource("res/config.txt").toURI()));
		        	//save properties to project root folder
		        	prop.store(output, null);
		        	} catch (IOException ex) {
		        	ex.printStackTrace();
		            } catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            setVisible(false);
		          }
		 });
		 pagePanel.add(btn);
		 JButton btn2 = new JButton("취소(C)");
		 //
		 btn2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		             dispose();
		    }
		 });
		 pagePanel.add(btn2);
		 lb.add(pagePanel);
		 add(lb);
		 //
	     pack();
		 setResizable(false);
		 setLocationRelativeTo(this);
		 setSize(200, 150);
		 setVisible(true);
	}

}
