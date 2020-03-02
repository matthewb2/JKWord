package oata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MutableAttributeSet;



public class TableDialog extends JDialog
{

	protected int m_option = JOptionPane.CLOSED_OPTION;
	protected MutableAttributeSet m_attributes;
	protected JTextField m_lineSpacing;
	protected JTextField m_spaceAbove;
	protected JTextField m_spaceBelow;
	protected JTextField m_firstIndent;
	protected JTextField m_leftIndent;
	protected JTextField m_rightIndent;
	protected JToggleButton m_btLeft;
	protected JToggleButton m_btCenter;
	protected JToggleButton m_btRight;
	protected JToggleButton m_btJustified;


	public TableDialog(JFrame parent) {
		super(parent, "Paragraph", true);
		getContentPane().setLayout(new BoxLayout(getContentPane(), 
		 BoxLayout.Y_AXIS));
		
		JPanel p = new JPanel(new GridLayout(1, 2, 5, 2));
		
		JPanel ps = new JPanel(new GridLayout(3, 2, 10, 2));
		ps.setBorder(new TitledBorder(new EtchedBorder(), "간격"));
		ps.add(new JLabel("줄 간격:"));
		m_lineSpacing = new JTextField();
		ps.add(m_lineSpacing);
		ps.add(new JLabel("위쪽 간격:"));
		m_spaceAbove = new JTextField();
		ps.add(m_spaceAbove);
		ps.add(new JLabel("아래쪽 간격:"));
		m_spaceBelow = new JTextField();
		ps.add(m_spaceBelow);
		p.add(ps);
		
		JPanel pi = new JPanel(new GridLayout(3, 2, 10, 2));
		pi.setBorder(new TitledBorder(new EtchedBorder(), "들여쓰기"));
		pi.add(new JLabel("첫째 들여쓰기:"));
		m_firstIndent = new JTextField();
		pi.add(m_firstIndent);
		pi.add(new JLabel("왼쪽 들여쓰기:"));
		m_leftIndent = new JTextField();
		pi.add(m_leftIndent);
		pi.add(new JLabel("오른쪽 들여쓰기:"));
		m_rightIndent = new JTextField();
		pi.add(m_rightIndent);
		p.add(pi);
		getContentPane().add(p);
		
		getContentPane().add(Box.createVerticalStrut(5));
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalStrut(10));
		p.add(new JLabel("Alignment:"));
		p.add(Box.createHorizontalStrut(20));
		
		ButtonGroup bg = new ButtonGroup();
		ImageIcon img = new ImageIcon(getClass().getResource("res/justifyleft.png"));
		m_btLeft = new JToggleButton();
		m_btLeft.setIcon(img);
		//Icon underline = new ImageIcon(getClass().getResource("res/underline.png"));
		//underlineIcon.setIcon(underline);
		bg.add(m_btLeft);
		p.add(m_btLeft);
		img = new ImageIcon(getClass().getResource("res/justifycenter.png"));
		m_btCenter = new JToggleButton();
		m_btCenter.setIcon(img);
		bg.add(m_btCenter);
		p.add(m_btCenter);
		img = new ImageIcon(getClass().getResource("res/justifyright.png"));
		m_btRight = new JToggleButton();
		m_btRight.setIcon(img);
		bg.add(m_btRight);
		p.add(m_btRight);
		img = new ImageIcon(getClass().getResource("res/justifyblock.png"));
		m_btJustified = new JToggleButton();
		m_btJustified.setIcon(img);
		bg.add(m_btJustified);
		p.add(m_btJustified);
		getContentPane().add(p);
		
		p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(new EtchedBorder(), "Preview"));
		
		getContentPane().add(p);
		
		p = new JPanel(new FlowLayout());
		JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
		JButton btOK = new JButton("확인");
		ActionListener lst = new ActionListener() { 
		 public void actionPerformed(ActionEvent e) {
		   m_option = JOptionPane.OK_OPTION;
		   setVisible(false);
		 }
		};
		btOK.addActionListener(lst);
		p1.add(btOK);
		
		JButton btCancel = new JButton("취소");
		lst = new ActionListener() { 
		 public void actionPerformed(ActionEvent e) {
		   m_option = JOptionPane.CANCEL_OPTION;
		   setVisible(false);
		 }
		};
		btCancel.addActionListener(lst);
		p1.add(btCancel);
		p.add(p1);
		getContentPane().add(p);
		
		pack();
		setResizable(false);
		
		Dimension d1 = getSize();
		Dimension d2 = parent.getSize();
		int x = Math.max((d2.width-d1.width)/2, 0);
		int y = Math.max((d2.height-d1.height)/2, 0);
		setBounds(x, y, d1.width, d1.height);
	

	}
}
