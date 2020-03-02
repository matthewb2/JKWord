package oata;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class ToolbarEx extends JToolBar{
	
	public void ToolbarEx(){
	    //tb = new JToolBar();
		Icon tableIcon;
	    Action tableAction;
	   
	        
	    //Icon someIcon = new ImageIcon(getClass().getResource("res/table.png"));
	    tableIcon = new ImageIcon(getClass().getResource("res/table.png"));
		   //Action action = new AbstractAction("someActionCommand", someIcon) {
	    tableAction = new AbstractAction("someActionCommand", tableIcon) {
	   	     @Override
	 	    public void actionPerformed(ActionEvent e) {
	 	        // do something.
	 	    }
	 	};
	    
	 	this.add(tableAction);
	    
			
	    JSeparator separator = new JSeparator();
	    
			
	}
    

}
