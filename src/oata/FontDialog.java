package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

//Unchanged code from section 20.5

public class FontDialog extends JDialog
{
protected int m_option = JOptionPane.CLOSED_OPTION;
protected OpenList m_lstFontName;
protected OpenList m_lstFontSize;
protected MutableAttributeSet m_attributes;
protected JCheckBox m_chkBold;
protected JCheckBox m_chkItalic;
protected JCheckBox m_chkUnderline;
 
protected JCheckBox m_chkStrikethrough;
protected JCheckBox m_chkSubscript;
protected JCheckBox m_chkSuperscript;
 
protected JComboBox m_cbColor;
protected JLabel m_preview;

public FontDialog(NeoHaneol owner,  String[] names, String[] sizes) {
     super();
	 getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	
	 JPanel p = new JPanel(new GridLayout(1, 2, 10, 2));
	 p.setBorder(new TitledBorder(new EtchedBorder(), "글꼴"));
	 m_lstFontName = new OpenList(names, "이름:");
	 m_lstFontName.m_text.setText("돋움");
	 m_lstFontName.setSelected("돋움");
	 p.add(m_lstFontName);
	
	 m_lstFontSize = new OpenList(sizes, "크기:");
	 m_lstFontSize.m_text.setText("14");
	 m_lstFontSize.setSelected("14");
	 p.add(m_lstFontSize);
	 getContentPane().add(p);
	
	 p = new JPanel(new GridLayout(2, 3, 10, 5));
	 p.setBorder(new TitledBorder(new EtchedBorder(), "효과"));
	 m_chkBold = new JCheckBox("굵게");
	 p.add(m_chkBold);
	 m_chkItalic = new JCheckBox("기울임");
	 p.add(m_chkItalic);
	 m_chkUnderline = new JCheckBox("밑줄");
	 p.add(m_chkUnderline);
	 m_chkStrikethrough = new JCheckBox("선명한");
	 p.add(m_chkStrikethrough);
	 m_chkSubscript = new JCheckBox("아래첨자");
	 p.add(m_chkSubscript);
	 m_chkSuperscript = new JCheckBox("윗첨자");
	 p.add(m_chkSuperscript);
	 getContentPane().add(p);
	
	 getContentPane().add(Box.createVerticalStrut(5));
	 p = new JPanel();
	 p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	 p.add(Box.createHorizontalStrut(10));
	 p.add(new JLabel("색상:"));
	 p.add(Box.createHorizontalStrut(20));
	 m_cbColor = new JComboBox();
	
	 int[] values = new int[] { 0, 128, 192, 255 };
	 for (int r=0; r<values.length; r++) {
	   for (int g=0; g<values.length; g++) {
	     for (int b=0; b<values.length; b++) {
	       Color c = new Color(values[r], values[g], values[b]);
	       m_cbColor.addItem(c);
	     }
	   }
	 }
	
	 m_cbColor.setRenderer(new ColorComboRenderer());
	 p.add(m_cbColor);
	 p.add(Box.createHorizontalStrut(10));
	 getContentPane().add(p);
	
	 p = new JPanel(new BorderLayout());
	 p.setBorder(new TitledBorder(new EtchedBorder(), "미리보기"));
	 m_preview = new JLabel("미리보기 글꼴", JLabel.CENTER);
	 m_preview.setBackground(Color.white);
	 m_preview.setForeground(Color.black);
	 m_preview.setOpaque(true);
	 m_preview.setBorder(new LineBorder(Color.black));
	 m_preview.setPreferredSize(new Dimension(120, 40));
	 p.add(m_preview, BorderLayout.CENTER);
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
	 /*
	 Dimension d1 = getSize();
	 Dimension d2 = parent.getSize();
	 int x = Math.max((d2.width-d1.width)/2, 0);
	 int y = Math.max((d2.height-d1.height)/2, 0);
	 setBounds(x, y, d1.width, d1.height);
	 */
	 setLocationRelativeTo(this);
	 //setVisible(true);
	 
	 
	 ListSelectionListener lsel = new ListSelectionListener() {
	   public void valueChanged(ListSelectionEvent e) {
	     updatePreview();
	   }
	 };
	 m_lstFontName.addListSelectionListener(lsel);
	 m_lstFontSize.addListSelectionListener(lsel);
	
	 lst = new ActionListener() { 
	   public void actionPerformed(ActionEvent e) {
	     updatePreview();
	   }
	 };
	 m_chkBold.addActionListener(lst);
	 m_chkItalic.addActionListener(lst);
	 m_cbColor.addActionListener(lst);
}

public void setAttributes(AttributeSet a) {
	 m_attributes = new SimpleAttributeSet(a);
	 String name = StyleConstants.getFontFamily(a);
	 m_lstFontName.setSelected(name);
	 int size = StyleConstants.getFontSize(a);
	 m_lstFontSize.setSelectedInt(size);
	 m_chkBold.setSelected(StyleConstants.isBold(a));
	 m_chkItalic.setSelected(StyleConstants.isItalic(a));
	 m_chkUnderline.setSelected(StyleConstants.isUnderline(a));
	 m_chkStrikethrough.setSelected(
	   StyleConstants.isStrikeThrough(a));
	 m_chkSubscript.setSelected(StyleConstants.isSubscript(a));
	 m_chkSuperscript.setSelected(StyleConstants.isSuperscript(a));
	 m_cbColor.setSelectedItem(StyleConstants.getForeground(a));
	 updatePreview();
}

public AttributeSet getAttributes() {
	 if (m_attributes == null)
	   return null;
	 StyleConstants.setFontFamily(m_attributes, 
	   m_lstFontName.getSelected());
	 StyleConstants.setFontSize(m_attributes, 
	   m_lstFontSize.getSelectedInt());
	 StyleConstants.setBold(m_attributes, 
	   m_chkBold.isSelected());
	 StyleConstants.setItalic(m_attributes, 
	   m_chkItalic.isSelected());
	 StyleConstants.setUnderline(m_attributes, 
	   m_chkUnderline.isSelected());
	 StyleConstants.setStrikeThrough(m_attributes, 
	   m_chkStrikethrough.isSelected());
	 StyleConstants.setSubscript(m_attributes, 
	   m_chkSubscript.isSelected());
	 StyleConstants.setSuperscript(m_attributes, 
	   m_chkSuperscript.isSelected());
	 StyleConstants.setForeground(m_attributes, 
	   (Color)m_cbColor.getSelectedItem());
	 return m_attributes;
}

public int getOption() { return m_option; }

protected void updatePreview() {
	 String name = m_lstFontName.getSelected();
	 int size = m_lstFontSize.getSelectedInt();
	 if (size <= 0)
	   return;
	 int style = Font.PLAIN;
	 if (m_chkBold.isSelected())
	   style |= Font.BOLD;
	 if (m_chkItalic.isSelected())
	   style |= Font.ITALIC;
	
	 // Bug Alert! This doesn't work if only style is changed.
	 Font fn = new Font(name, style, size);
	 m_preview.setFont(fn);
	
	 Color c = (Color)m_cbColor.getSelectedItem();
	 m_preview.setForeground(c);
	 m_preview.repaint();
	}
}


class OpenList extends JPanel implements ListSelectionListener, ActionListener
{
  protected JLabel m_title;
  protected JTextField m_text;
  protected JList m_list;
  protected JScrollPane m_scroll;

  public OpenList(String[] data, String title) {
    setLayout(null);
    m_title = new JLabel(title, JLabel.LEFT);
    add(m_title);
    m_text = new JTextField();
    //m_text.setText(data[0]);
    m_text.addActionListener(this);
    add(m_text);
    m_list = new JList(data);
    m_list.setVisibleRowCount(4);
    m_list.addListSelectionListener(this);
    m_scroll = new JScrollPane(m_list);
    add(m_scroll);
  }

  public void setSelected(String sel) {
    m_list.setSelectedValue(sel, true);
    m_text.setText(sel);
  }

  public String getSelected() { return m_text.getText(); }

  public void setSelectedInt(int value) {
    setSelected(Integer.toString(value));
  }

  public int getSelectedInt() {
    try { 
      return Integer.parseInt(getSelected());
    }
    catch (NumberFormatException ex) { return -1; }
  }

  public void valueChanged(ListSelectionEvent e) {
    Object obj = m_list.getSelectedValue();
    if (obj != null)
      m_text.setText(obj.toString());
  }

  public void actionPerformed(ActionEvent e) {
    ListModel model = m_list.getModel();
    String key = m_text.getText().toLowerCase();
    for (int k=0; k<model.getSize(); k++) {
      String data = (String)model.getElementAt(k);
      if (data.toLowerCase().startsWith(key)) {
        m_list.setSelectedValue(data, true);
        break;
      }
    }
  }

  public void addListSelectionListener(ListSelectionListener lst) {
    m_list.addListSelectionListener(lst);
  }

  public Dimension getPreferredSize() {
    Insets ins = getInsets();
    Dimension d1 = m_title.getPreferredSize();
    Dimension d2 = m_text.getPreferredSize();
    Dimension d3 = m_scroll.getPreferredSize();
    int w = Math.max(Math.max(d1.width, d2.width), d3.width);
    int h = d1.height + d2.height + d3.height;
    return new Dimension(w+ins.left+ins.right, 
      h+ins.top+ins.bottom);
  }

  public Dimension getMaximumSize() {
    Insets ins = getInsets();
    Dimension d1 = m_title.getMaximumSize();
    Dimension d2 = m_text.getMaximumSize();
    Dimension d3 = m_scroll.getMaximumSize();
    int w = Math.max(Math.max(d1.width, d2.width), d3.width);
    int h = d1.height + d2.height + d3.height;
    return new Dimension(w+ins.left+ins.right, 
      h+ins.top+ins.bottom);
  }

  public Dimension getMinimumSize() {
    Insets ins = getInsets();
    Dimension d1 = m_title.getMinimumSize();
    Dimension d2 = m_text.getMinimumSize();
    Dimension d3 = m_scroll.getMinimumSize();
    int w = Math.max(Math.max(d1.width, d2.width), d3.width);
    int h = d1.height + d2.height + d3.height;
    return new Dimension(w+ins.left+ins.right, 
      h+ins.top+ins.bottom);
  }

  public void doLayout() {
    Insets ins = getInsets();
    Dimension d = getSize();
    int x = ins.left;
    int y = ins.top;
    int w = d.width-ins.left-ins.right;
    int h = d.height-ins.top-ins.bottom;

    Dimension d1 = m_title.getPreferredSize();
    m_title.setBounds(x, y, w, d1.height);
    y += d1.height;
    Dimension d2 = m_text.getPreferredSize();
    m_text.setBounds(x, y, w, d2.height);
    y += d2.height;
    m_scroll.setBounds(x, y, w, h-y);
  }
}

class ColorComboRenderer extends JPanel implements ListCellRenderer
{
  protected Color m_color = Color.black;
  protected Color m_focusColor = 
    (Color) UIManager.get("List.selectionBackground");
  protected Color m_nonFocusColor = Color.white;

  public Component getListCellRendererComponent(JList list,
   Object obj, int row, boolean sel, boolean hasFocus)
  {
    if (hasFocus || sel)
      setBorder(new CompoundBorder(
        new MatteBorder(2, 10, 2, 10, m_focusColor),
        new LineBorder(Color.black)));
    else
      setBorder(new CompoundBorder(
        new MatteBorder(2, 10, 2, 10, m_nonFocusColor),
        new LineBorder(Color.black)));

    if (obj instanceof Color) 
      m_color = (Color) obj;
    return this;
  }
    
  public void paintComponent(Graphics g) {
    setBackground(m_color);
    super.paintComponent(g);
  }
}