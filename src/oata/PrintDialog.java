package oata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

public class PrintDialog extends JDialog{
	
	 protected PrinterJob printJob = PrinterJob.getPrinterJob();
	 protected PageFormat pageFormat = printJob.defaultPage();
	 protected JEditorPane content;
	 
	 public PrintDialog(final JEditorPane owner){
	    setTitle("인쇄");
	    this.content = owner;
		// 
		JPanel jp1 = new JPanel();
	    jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
		
	    JPanel lb = new JPanel();
		lb.setLayout(new BoxLayout(lb, BoxLayout.Y_AXIS));
		lb.setAlignmentY(Component.TOP_ALIGNMENT);
		//
		JPanel pagetextPanel = new JPanel();
      	pagetextPanel.setBorder(BorderFactory.createTitledBorder("프린터 선택"));
     	//
     	pagetextPanel.setMaximumSize(new Dimension(450, 50));
     	pagetextPanel.setPreferredSize(new Dimension(450, 50));
     	
     	ArrayList<String> dt2 = new ArrayList<>();
     	PrintService[] printServices = PrinterJob.lookupPrintServices();
		for (PrintService printService : printServices) {
	            String name = printService.getName();
	            //System.out.println("Name = " + name);
	            dt2.add(name);
	           
	    }
		String[] dt = dt2.toArray(new String[dt2.size()]);
		JComboBox pt = new JComboBox(dt);
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
		pt.setSelectedItem(service.getName());
		pagetextPanel.add(pt);
		lb.add(pagetextPanel);
		JPanel pageNumPanel = new JPanel();
		pageNumPanel.setBorder(BorderFactory.createTitledBorder("인쇄매수"));
		pageNumPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pageNumPanel.setMaximumSize(new Dimension(450, 50));
		pageNumPanel.setPreferredSize(new Dimension(450, 50));
     	//
		JLabel lbb1 = new JLabel("매수");
		//Add the first label-spinner pair.
	    JSpinner hours = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
	    pageNumPanel.add(lbb1);
	    pageNumPanel.add(hours);
		lb.add(pageNumPanel);
		JPanel pagePanel = new JPanel();
      	pagePanel.setBorder(BorderFactory.createTitledBorder("인쇄범위"));
      	JPanel pageInner = new JPanel();
     	pageInner.setLayout(new GridLayout(0,3));
     	pageInner.setPreferredSize(new Dimension(400, 90));
     	pageInner.setAlignmentX(Component.LEFT_ALIGNMENT);
     	//
     	pagePanel.setMaximumSize(new Dimension(450, 120));
		pagePanel.setPreferredSize(new Dimension(450, 120));
     	//Create the radio buttons.
        JRadioButton birdButton = new JRadioButton("문서전체(T)");
        birdButton.setMnemonic(KeyEvent.VK_T);
        //
        birdButton.setSelected(true);
        birdButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel empty = new JLabel();
        JLabel empty2 = new JLabel();
        JLabel empty3 = new JLabel();
        JLabel empty4 = new JLabel();
        JRadioButton curButton = new JRadioButton("현재쪽(C)");
        curButton.setMnemonic(KeyEvent.VK_C);
        curButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        //
	    JRadioButton partButton = new JRadioButton("일부분(R)");
	    partButton.setMnemonic(KeyEvent.VK_R);
	    partButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	    JPanel wrapper = new JPanel();
	    wrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
	    JTextField tm = new JTextField();
	    tm.setPreferredSize(new Dimension(80, 20));
	    tm.setAlignmentX(Component.LEFT_ALIGNMENT);
	    //
	    JLabel ex = new JLabel("예) 1-9");
	    wrapper.add(tm);
	    wrapper.add(ex);
       
        ButtonGroup group = new ButtonGroup();
        group.add(birdButton);
        group.add(curButton);
        group.add(partButton);
        //
        pageInner.add(birdButton);
        pageInner.add(empty);
	    pageInner.add(empty2);
	    pageInner.add(curButton);
	    pageInner.add(empty3);
	    pageInner.add(empty4);
	    pageInner.add(partButton);
	    pageInner.add(wrapper);
	    pagePanel.add(pageInner);
	    lb.add(pagePanel);
		JPanel methodPanel = new JPanel();
	    methodPanel.setBorder(BorderFactory.createTitledBorder("인쇄방식"));
		lb.add(methodPanel);
			
        JButton btn1 = new JButton("인쇄(D)");
	    btn1.setSize(new Dimension(200, 150));
	    btn1.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e)
	          {
	    	   
	    	   	PrinterJob pjob = PrinterJob.getPrinterJob();
	    	    PageFormat pageFormat = new PageFormat();
	    	    Paper paper = pageFormat.getPaper();
	    	    final double MM_TO_PAPER_UNITS = 72.0/25.4;
	    	    final double widthA4 = 210*MM_TO_PAPER_UNITS;
	    	    final double heightA4 = 297*MM_TO_PAPER_UNITS;
	    	    paper.setSize(widthA4, heightA4); 
	    	    //
	    	    pageFormat.setPaper(paper);
							
			    try {
			    	CustomDocument doc = (CustomDocument) owner.getDocument();
			    	doc.putProperty("show textborder", null);
			    	doc.putProperty("show paragraphs", null);
			    	doc.putProperty("MODE_PRINTER", Boolean.TRUE);
					pjob.setPrintable(new PaginationPrinter(pageFormat, owner));
				} catch (PrinterException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					pjob.print();
					CustomDocument doc = (CustomDocument) owner.getDocument();
					doc.putProperty("show textborder", "A");
					doc.putProperty("show paragraphs", "A");
					doc.putProperty("MODE_PRINTER", Boolean.FALSE);
					setVisible(false);
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	          }
	    });
	    
	    JButton btn2 = new JButton("취소(C)");
	    btn2.setSize(new Dimension(200, 150));
	    btn2.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e)
	          {
	             dispose();
	          }
	    });
	       
	    JPanel rb = new JPanel();
	    rb.setLayout(new BoxLayout(rb, BoxLayout.Y_AXIS));
	    rb.setAlignmentY(Component.TOP_ALIGNMENT);
	    //	    
	    jp1.add(lb);
	    rb.add(btn1);
	    rb.add(btn2);
	    jp1.add(rb);
	    //
	    Vector<Component> order = new Vector<Component>(7);
	    order.add(lb);
	    order.add(btn1);
	    order.add(btn2);
	    order.add(rb);
	    MyOwnFocusTraversalPolicy newPolicy = new MyOwnFocusTraversalPolicy(order);
	    
	    this.add(jp1);
	    this.setSize(500, 600);
	    this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
   }
}

