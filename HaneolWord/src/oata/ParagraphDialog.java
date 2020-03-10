package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;

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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import oata.ParagraphDialog.ParagraphPreview;

//Unchanged code from section 20.6

public class ParagraphDialog extends JDialog
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

protected ParagraphPreview m_preview;

public ParagraphDialog(JFrame parent) {
	super(parent, "Paragraph", true);
	getContentPane().setLayout(new BoxLayout(getContentPane(), 
	 BoxLayout.Y_AXIS));
	
	JPanel p = new JPanel(new GridLayout(1, 2, 5, 2));
	
	JPanel ps = new JPanel(new GridLayout(3, 2, 10, 2));
	ps.setBorder(new TitledBorder(new EtchedBorder(), "border"));
	ps.add(new JLabel(":"));
	m_lineSpacing = new JTextField();
	ps.add(m_lineSpacing);
	ps.add(new JLabel("space above:"));
	m_spaceAbove = new JTextField();
	ps.add(m_spaceAbove);
	ps.add(new JLabel("space below:"));
	m_spaceBelow = new JTextField();
	ps.add(m_spaceBelow);
	p.add(ps);
	
	JPanel pi = new JPanel(new GridLayout(3, 2, 10, 2));
	pi.setBorder(new TitledBorder(new EtchedBorder(), "border"));
	pi.add(new JLabel(":"));
	m_firstIndent = new JTextField();
	pi.add(m_firstIndent);
	pi.add(new JLabel(":"));
	m_leftIndent = new JTextField();
	pi.add(m_leftIndent);
	pi.add(new JLabel(":"));
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
	m_preview = new ParagraphPreview();
	p.add(m_preview, BorderLayout.CENTER);
	getContentPane().add(p);
	
	p = new JPanel(new FlowLayout());
	JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
	JButton btOK = new JButton("ok");
	ActionListener lst = new ActionListener() { 
	 public void actionPerformed(ActionEvent e) {
	   m_option = JOptionPane.OK_OPTION;
	   setVisible(false);
	 }
	};
	btOK.addActionListener(lst);
	p1.add(btOK);
	
	JButton btCancel = new JButton("cancel");
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
	

	FocusListener flst = new FocusListener() { 
	 public void focusGained(FocusEvent e) {}
	
	 public void focusLost(FocusEvent e) { updatePreview(); }
	};
	m_lineSpacing.addFocusListener(flst);
	m_spaceAbove.addFocusListener(flst);
	m_spaceBelow.addFocusListener(flst);
	m_firstIndent.addFocusListener(flst);
	m_leftIndent.addFocusListener(flst);
	m_rightIndent.addFocusListener(flst);
	
	lst = new ActionListener() { 
		 public void actionPerformed(ActionEvent e) {
		   updatePreview();
		 }
	};
	m_btLeft.addActionListener(lst);
	m_btCenter.addActionListener(lst);
	m_btRight.addActionListener(lst);
	m_btJustified.addActionListener(lst);
}

public void setAttributes(AttributeSet a) {
		m_attributes = new SimpleAttributeSet(a);
		m_lineSpacing.setText(Float.toString(
		 StyleConstants.getLineSpacing(a)));
		m_spaceAbove.setText(Float.toString(
		 StyleConstants.getSpaceAbove(a)));
		m_spaceBelow.setText(Float.toString(
		 StyleConstants.getSpaceBelow(a)));
		m_firstIndent.setText(Float.toString(
		 StyleConstants.getFirstLineIndent(a)));
		m_leftIndent.setText(Float.toString(
		 StyleConstants.getLeftIndent(a)));
		m_rightIndent.setText(Float.toString(
		 StyleConstants.getRightIndent(a)));
		
		int alignment = StyleConstants.getAlignment(a);
		if (alignment == StyleConstants.ALIGN_LEFT)
		 m_btLeft.setSelected(true);
		else if (alignment == StyleConstants.ALIGN_CENTER)
		 m_btCenter.setSelected(true);
		else if (alignment == StyleConstants.ALIGN_RIGHT)
		 m_btRight.setSelected(true);
		else if (alignment == StyleConstants.ALIGN_JUSTIFIED)
		 m_btJustified.setSelected(true);
		
		updatePreview();
}

public AttributeSet getAttributes() {
		if (m_attributes == null)
		 return null;
		float value;
		try { 
		 value = Float.parseFloat(m_lineSpacing.getText());
		 StyleConstants.setLineSpacing(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		try { 
		 value = Float.parseFloat(m_spaceAbove.getText());
		 StyleConstants.setSpaceAbove(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		try { 
		 value = Float.parseFloat(m_spaceBelow.getText());
		 StyleConstants.setSpaceBelow(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		try { 
		 value = Float.parseFloat(m_firstIndent.getText());
		 StyleConstants.setFirstLineIndent(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		try { 
		 value = Float.parseFloat(m_leftIndent.getText());
		 StyleConstants.setLeftIndent(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		try { 
		 value = Float.parseFloat(m_rightIndent.getText());
		 StyleConstants.setRightIndent(m_attributes, value); 
		} catch (NumberFormatException ex) {}
		
		StyleConstants.setAlignment(m_attributes, getAlignment());
		
		return m_attributes;
}

public int getOption() {
	return m_option;
}

protected void updatePreview() {
	m_preview.repaint();
}

protected int getAlignment() {
	if (m_btLeft.isSelected())
	 return StyleConstants.ALIGN_LEFT;
	if (m_btCenter.isSelected())
	 return StyleConstants.ALIGN_CENTER;
	else if (m_btRight.isSelected())
	 return StyleConstants.ALIGN_RIGHT;
	else
	 return StyleConstants.ALIGN_JUSTIFIED;
}






class ParagraphPreview extends JPanel
{
	protected Font m_fn = new Font("Monospace", Font.PLAIN, 6);
	protected String m_dummy = "abcdefghjklm";
	protected float  m_scaleX = 0.25f;
	protected float  m_scaleY = 0.25f;
	protected Random m_random = new Random();
	
	public ParagraphPreview() {
		 setBackground(Color.white);
		 setForeground(Color.black);
		 setOpaque(true);
		 setBorder(new LineBorder(Color.black));
		 setPreferredSize(new Dimension(120, 56));
	}
	
	public void paintComponent(Graphics g) {
	 super.paintComponent(g);
	 float lineSpacing = 0;
	 float spaceAbove  = 0;
	 float spaceBelow  = 0;
	 float firstIndent = 0;
	 float leftIndent  = 0;
	 float rightIndent = 0;
	
	 try { 
	   lineSpacing = Float.parseFloat(m_lineSpacing.getText());
	 } catch (NumberFormatException ex) {}
	 try { 
	   spaceAbove = Float.parseFloat(m_spaceAbove.getText()); 
	 } catch (NumberFormatException ex) {}
	 try { 
	   spaceBelow = Float.parseFloat(m_spaceBelow.getText()); 
	 } catch (NumberFormatException ex) {}
	 try { 
	   firstIndent = Float.parseFloat(m_firstIndent.getText()); 
	 } catch (NumberFormatException ex) {}
	 try { 
	   leftIndent = Float.parseFloat(m_leftIndent.getText()); 
	 } catch (NumberFormatException ex) {}
	 try { 
	   rightIndent = Float.parseFloat(m_rightIndent.getText()); 
	 } catch (NumberFormatException ex) {}
	
	 m_random.setSeed(1959);    // Use same seed every time
	
	 g.setFont(m_fn);
	 FontMetrics fm = g.getFontMetrics();
	 int h = fm.getAscent();
	 int s  = Math.max((int)(lineSpacing*m_scaleY), 1);
	 int s1 = Math.max((int)(spaceAbove*m_scaleY), 0) + s;
	 int s2 = Math.max((int)(spaceBelow*m_scaleY), 0) + s;
	 int y = 5+h;
	
	 int xMarg = 20;
	 int x0 = Math.max((int)(firstIndent*m_scaleX)+xMarg, 3);
	 int x1 = Math.max((int)(leftIndent*m_scaleX)+xMarg, 3);
	 int x2 = Math.max((int)(rightIndent*m_scaleX)+xMarg, 3);
	 int xm0 = getWidth()-xMarg;
	 int xm1 = getWidth()-x2;
	       
	 int n = (int)((getHeight()-(2*h+s1+s2-s+10))/(h+s));
	 n = Math.max(n, 1);
	
	 g.setColor(Color.lightGray);
	 int x = xMarg;
	 drawLine(g, x, y, xm0, xm0, fm, StyleConstants.ALIGN_LEFT);
	 y += h+s1;
	
	 g.setColor(Color.gray);
	 int alignment = getAlignment();
	 for (int k=0; k<n; k++) {
	   x = (k==0 ? x0 : x1);
	   int xLen = (k==n-1 ? xm1/2 : xm1);
	   if (k==n-1 && alignment==StyleConstants.ALIGN_JUSTIFIED)
	     alignment = StyleConstants.ALIGN_LEFT;
	   drawLine(g, x, y, xm1, xLen, fm, alignment);
	   y += h+s;
	 }
	
	 y += s2-s;
	 x = xMarg;
	 g.setColor(Color.lightGray);
	 drawLine(g, x, y, xm0, xm0, fm, StyleConstants.ALIGN_LEFT);
	}
	
	protected void drawLine(Graphics g, int x, int y, int xMax, int xLen, FontMetrics fm, int alignment)
	{
		 if (y > getHeight()-3)
		   return;
		 StringBuffer s = new StringBuffer();
		 String str1;
		 int xx = x;
		 while (true) {
		   int m = m_random.nextInt(10)+1;
		   str1 = m_dummy.substring(0, m)+" ";
		   int len = fm.stringWidth(str1);
		   if (xx+len >= xLen)
		     break;
		   xx += len;
		   s.append(str1);
		 }
		 String str = s.toString();
		
		 switch (alignment) {
		   case StyleConstants.ALIGN_LEFT:
		     g.drawString(str, x, y);
		     break;
		   case StyleConstants.ALIGN_CENTER:
		     xx = (xMax+x-fm.stringWidth(str))/2;
		     g.drawString(str, xx, y);
		     break;
		   case StyleConstants.ALIGN_RIGHT:
		     xx = xMax-fm.stringWidth(str);
		     g.drawString(str, xx, y);
		     break;
		   case StyleConstants.ALIGN_JUSTIFIED:
		     while (x+fm.stringWidth(str) < xMax)
		       str += "a";
		     g.drawString(str, x, y);
		     break;
		 }
		}
	}
}