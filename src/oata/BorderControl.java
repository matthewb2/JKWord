package oata;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Contains border attributes properties. BorderColor and values.
 *
 * @author Stanislav Lapitsky
 */
public class BorderControl extends JPanel {

    public JComboBox colorCombo;

    public JCheckBox checkLeft=new JCheckBox("Left",true);
    public JCheckBox checkRight=new JCheckBox("Right",true);
    public JCheckBox checkTop=new JCheckBox("Top",true);
    public JCheckBox checkBottom=new JCheckBox("Bottom",true);
    public JCheckBox checkHorizontal=new JCheckBox("Horizontal",true);
    public JCheckBox checkVertical=new JCheckBox("Vertical",true);

    /**
     * Constructs new instance
     */
    public BorderControl() {
        super();
        setBorder(new TitledBorder(new EtchedBorder(), "Borders:"));

        colorCombo=new JComboBox(new Object[] {Color.black,Color.red,Color.green,Color.blue,Color.white});
        colorCombo.setRenderer(new ColorComboRenderer());
        setLayout(new GridBagLayout());
        add(new JLabel("Color"),new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
        add(colorCombo,new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));

        add(checkLeft,new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        add(checkRight,new GridBagConstraints(0,2,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        add(checkTop,new GridBagConstraints(0,3,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        add(checkBottom,new GridBagConstraints(0,4,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        add(checkHorizontal,new GridBagConstraints(0,5,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        add(checkVertical,new GridBagConstraints(0,6,2,1,1,0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        setVisible(true);
    }

    /**
     * Constructs new instance with defined border attributes.
     * @param ba Border attributes.
     */
    public BorderControl(BorderAttributes ba) {
        this();
        setBorderAttributes(ba);
    }

    /**
     * Sets inner controls to reflect specified attributes.
     * @param ba Border attributes.
     */
    public void setBorderAttributes(BorderAttributes ba) {
        colorCombo.setSelectedItem(ba.lineColor);

        checkLeft.setSelected(ba.borderLeft!=0);
        checkRight.setSelected(ba.borderRight!=0);
        checkTop.setSelected(ba.borderTop!=0);
        checkBottom.setSelected(ba.borderBottom!=0);
        checkHorizontal.setSelected(ba.borderHorizontal!=0);
        checkVertical.setSelected(ba.borderVertical!=0);
    }

    /**
     * Gets user choice. Creates and returns Border attributes according to
     * user's selection.
     * @return
     */
    public BorderAttributes getBorderAttributes() {
        BorderAttributes ba=new BorderAttributes();
        ba.setBorders(0);
        ba.lineColor=(Color)colorCombo.getSelectedItem();

        if (checkLeft.isSelected())
            ba.borderLeft=1;
        if (checkRight.isSelected())
            ba.borderRight=1;
        if (checkTop.isSelected())
            ba.borderTop=1;
        if (checkBottom.isSelected())
            ba.borderBottom=1;
        if (checkHorizontal.isSelected())
            ba.borderHorizontal=1;
        if (checkVertical.isSelected())
            ba.borderVertical=1;
        return ba;
    }

}