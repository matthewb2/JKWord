package oata;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;

public class DefaultContextMenu extends JPopupMenu
{
    private Clipboard clipboard;

    private UndoManager undoManager;

    private JMenuItem undo;
    private JMenuItem redo;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem delete;
    private JMenuItem selectAll;
    private Boolean isImage=false, onImage=false;
    private String imageNumber=null;
    
    boolean enableCut = false;
    boolean enableCopy = false;
    boolean enablePaste = false;
    boolean enableDelete = false;
    boolean enableSelectAll = false;

    public static DefaultContextMenu defaultContextMenu;
    
    private JTextComponent textComponent;

    public DefaultContextMenu()
    {
    	
        undoManager = new UndoManager();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        addPopupMenuItems();
    }

    private void addPopupMenuItems()
    {
        undo = new JMenuItem("Undo");
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	//event -> undoManager.undo());	
        });
        add(undo);

        redo = new JMenuItem("Redo");
        redo.setEnabled(false);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //redo.addActionListener(event -> undoManager.redo());
        redo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	//event -> undoManager.undo());	
        });
        add(redo);

        add(new JSeparator());

        cut = new JMenuItem("Cut");
        cut.setEnabled(false);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //cut.addActionListener(event -> textComponent.cut());
        add(cut);

        copy = new JMenuItem("Copy");
        copy.setEnabled(false);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //copy.addActionListener(event -> textComponent.copy());
        copy.addActionListener(new ActionListener(){
     			@Override
     			public void actionPerformed(ActionEvent arg0) {
     			   // TODO Auto-generated method stub
     				isImage=false;
     				if (textComponent.getSelectedText().contains("img:")){
     				   isImage=true;
     				   StyledDocument dDoc = (StyledDocument)textComponent.getDocument();
         			   Enumeration list = ((DefaultStyledDocument)dDoc).getStyleNames();
         			
         			   String styleName = null;
         			   Icon ai = null;
         			   while (list.hasMoreElements()) {
         					//System.out.println("e의 요소 : " + e.nextElement());
         					styleName = (String)list.nextElement();
         					//System.out.println(styleName);
         				    Style style2 = dDoc.getStyle(styleName);
         				    ai = StyleConstants.getIcon(style2);
         					if (styleName.contains("Icon")) break;
         			   }
         			   if (ai !=null){
    	     			   System.out.println(ai.toString());
    	     			   Image image = Toolkit.getDefaultToolkit ().createImage(ai.toString());
    	     		       ClipboardImage.write( image );
         			   } else if (textComponent.getSelectedText().length()>0){
         				  textComponent.copy();
         			   }
     				}
     				if (textComponent.getSelectedText().length()>0 && isImage==false){
       				//  textComponent.copy();
       				//String myString = "This text will be copied into clipboard";
     					StringSelection stringSelection = new StringSelection(textComponent.getSelectedText());
     					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
     					clipboard.setContents(stringSelection, null);
       			   } 
     				System.out.println(isImage);
     			}
     				
     			
             	//event -> undoManager.undo());	
             });
        add(copy);

        paste = new JMenuItem("Paste");
        paste.setEnabled(false);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        paste.addActionListener(new ActionListener(){

 			@Override
 			public void actionPerformed(ActionEvent arg0) {
 				/*
 				ImageIcon icon = new javax.swing.ImageIcon( ClipboardImage.read() );
 				Style style1 = ((StyledDocument)textComponent).addStyle("Icon0", null);
 			    StyleConstants.setIcon(style1, icon);
 			    //final StyledDocument doc = m_monitor.getStyledDocument();
 			    StyledDocument doc=(StyledDocument)textComponent.getDocument();
 				try{
 				    doc.insertString(0, "\\img:0", style1);
 			    }catch (Exception e){ }
 			    */
 				StyledDocument doc = (StyledDocument)textComponent.getDocument();
 				try {
					doc.insertString(doc.getLength(), getClipboardContents(), null);
				} catch (BadLocationException | UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			}
        });

        add(paste);

        delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //delete.addActionListener(event -> textComponent.replaceSelection(""));
        delete.addActionListener(new ActionListener(){

 			@Override
 			public void actionPerformed(ActionEvent e) {
 				StyledDocument dDoc = (StyledDocument)textComponent.getDocument();
 				String content = textComponent.getText();
 				//StringSelection ss = new StringSelection("\\img:0");
 				try {
					dDoc.remove(content.indexOf("\\img:"+imageNumber), 6+imageNumber.length());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

 			}
        });
        
        add(delete);

        add(new JSeparator());

        selectAll = new JMenuItem("Select All");
        selectAll.setEnabled(false);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //selectAll.addActionListener(event -> textComponent.selectAll());
        add(selectAll);
    }
    
    public String getClipboardContents() throws UnsupportedFlavorException, IOException {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
          (contents != null) &&
          contents.isDataFlavorSupported(DataFlavor.stringFlavor)
        ;
        if (hasTransferableText) {
          
            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
          
          
        }
        return result;
      }

    public void addTo(final JTextComponent textComponent)
    {
        textComponent.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent pressedEvent)
            {
                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Z)
                        && ((pressedEvent.getModifiersEx() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0))
                {
                    if (undoManager.canUndo())
                    {
                        undoManager.undo();
                    }
                }

                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Y)
                        && ((pressedEvent.getModifiersEx() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0))
                {
                    if (undoManager.canRedo())
                    {
                        undoManager.redo();
                    }
                }
            }
        });

        textComponent.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent releasedEvent)
            {
                
                Point pt = releasedEvent.getPoint();
				//
			    int pos = textComponent.viewToModel(pt);
			    String str=null;
			    try {
					str = textComponent.getText(pos, pos+6);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    if (str.contains("img:")){
			    	System.out.println("delete action ready");
			    	enableCut = true;
			        enableCopy = true;
			        enableDelete = true;
			        // for copy
			        final String str2 = str.replaceAll("[^0-9]", "");
			        System.out.println(str2);
			        imageNumber = str.replaceAll("[^0-9]", "");
			        //
			        StyledDocument dDoc = (StyledDocument)textComponent.getDocument();
      			    Enumeration list = ((DefaultStyledDocument)dDoc).getStyleNames();
      			
      			    String styleName = null;
      			    Icon ai = null;
      			    while (list.hasMoreElements()) {
      					//System.out.println("e의 요소 : " + e.nextElement());
      					styleName = (String)list.nextElement();
      					//System.out.println(styleName);
      				    Style style2 = dDoc.getStyle(styleName);
      				    ai = StyleConstants.getIcon(style2);
      					if (styleName.contains("Icon"+str2)) break;
      			    }
      			    System.out.println(styleName);
      			 
			    }
			    //
			    handleContextMenu(releasedEvent);
            }

            @Override
            public void mouseReleased(MouseEvent releasedEvent)
            {
                handleContextMenu(releasedEvent);
            }
        });

        //textComponent.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
    }

    private void handleContextMenu(MouseEvent releasedEvent)
    {
        if (releasedEvent.getButton() == MouseEvent.BUTTON3)
        {
            processClick(releasedEvent);
        }
    }

    private void processClick(MouseEvent event)
    {
        textComponent = (JTextComponent) event.getSource();
        textComponent.requestFocus();

        boolean enableUndo = undoManager.canUndo();
        boolean enableRedo = undoManager.canRedo();
      

        String selectedText = textComponent.getSelectedText();
        String text = textComponent.getText();
        //
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasStringText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        /*
        if (hasStringText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex); ex.printStackTrace();
        }
        */
        
        if (text != null)
        {
            if (text.length() > 0)
            {
                enableSelectAll = true;
            }
        }

        if (selectedText != null)
        {
            if (selectedText.length() > 0)
            {
                enableCut = true;
                enableCopy = true;
                enableDelete = true;
            }
        }

        if (hasStringText)
        {
            enablePaste = true;
        }
        
       
        undo.setEnabled(enableUndo);
        redo.setEnabled(enableRedo);
        cut.setEnabled(enableCut);
        copy.setEnabled(enableCopy);
        paste.setEnabled(enablePaste);
        delete.setEnabled(enableDelete);
        selectAll.setEnabled(enableSelectAll);

        // Shows the popup menu
        show(textComponent, event.getX(), event.getY());
        //remove(textComponent);
    }
    
    public static void remove(){
    	//show(textComponent, event.getX(), event.getY());
    	defaultContextMenu.removeAll();
    }

    public static void addDefaultContextMenu(JTextComponent component)
    {
        defaultContextMenu = new DefaultContextMenu();
        defaultContextMenu.addTo(component);
        //defaultContextMenu.removeAll();
    }
}