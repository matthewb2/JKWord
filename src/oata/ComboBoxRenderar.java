package oata;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderar  extends JLabel implements ListCellRenderer {
	
    @Override
    public Component getListCellRendererComponent(JList list, 
                                                  Object value, 
                                                  int index, 
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
      int offset = ((Integer)value).intValue() - 1 ;
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String fontName[] = ge.getAvailableFontFamilyNames();
      String name = fontName[offset];
      setText(name);
      setFont(new Font(name,Font.PLAIN,20));
      return this;
    }
}
