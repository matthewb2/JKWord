package oata;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oata.CustomEditorKit.StyledViewFactory;



public class HaneolWord extends JFrame implements MouseListener, KeyListener{
	
		static int WIDTH = 800, HEIHGT=1200;
				
		protected JInternalFrame internalFrame;
		
		protected JDesktopPane desktop = new JDesktopPane();;
		//protected JDesktopPane container = new JDesktopPane();
		protected JPanel container = new JPanel();
		
		protected JPanel statusBar = new JPanel();
	    //
		protected MainTextPane maintext;
		protected CustomDocument doc;
		
		
		protected Color selectColor;
		
		protected FontDialog m_fontDialog;
		protected ParagraphDialog m_paragraphDialog;
		protected FindDialog m_findDialog;
		protected TableDialog m_tableDialog;
		protected MultiColumnDialog m_multiColumnDialog;
		
		
		JScrollPane scrollPane= new JScrollPane();
		
	    //
	    StyleContext sc = new StyleContext();
	    Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
	    Style mainStyle = sc.addStyle("MainStyle", defaultStyle);
	    //    
	    JToolBar tb, tb2;
	    Icon tableIcon, imageIcon, newIcon, openIcon, saveIcon, colorIcon, letterIcon, paragraphIcon, multicolumnIcon;
	    JToggleButton italicIcon, boldIcon, underlineIcon, leftalignIcon, bothalignIcon, rightalignIcon, centeralignIcon;
	    JButton tablebutton, imagebutton, letterbutton, paragraphbutton, multicolumnbutton;
	    Action tableAction, imageAction, newAction, openAction, saveAction, italicAction, boldAction, underlAction, colorAction, letterAction, paragraphAction;
	    JCheckBoxMenuItem parsym;
	    
	    JComboBox jcbFont, jcbSelectSize, jcbSelectZoom, jcbSelectSpace;
	    
	    Element current;
	    
	    float scale = 1;
	    
	    //
	    protected UndoManager m_undo = new UndoManager();
	   
	    
    public static void main(String[] args) throws IOException {
        new HaneolWord();
    }
    

    
    void checkUpdate() throws IOException{
    	
    	URL url=new URL("http://swave.woobi.co.kr/ART/update.xml");

    	BufferedReader bf;

		String line;

		bf = new BufferedReader(new InputStreamReader(url.openStream()));

		int[] current = {0, 1, 2};
		//int num;
		while((line = bf.readLine())!= null){
			if (line.contains(" ")) break;
			//System.out.println(line);
			String[] tmp = line.split("-");
			//System.out.println(tmp.length);
			
			if (Integer.parseInt(tmp[1])>current[1]) JOptionPane.showMessageDialog(null, "");
			else if (Integer.parseInt(tmp[1])==current[1] && Integer.parseInt(tmp[2])>current[2]) JOptionPane.showMessageDialog(null, "");
			else if (Integer.parseInt(tmp[1])==current[1] && Integer.parseInt(tmp[2])==current[2] &&Integer.parseInt(tmp[3])>current[3]) 	JOptionPane.showMessageDialog(null, "");
			
			 
		}

		bf.close();
		
		

    }

    
    public void saveEx(){
    	try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        JFileChooser file = new JFileChooser();
        //TextFilter filter = new TextFilter();
        //file.setFileFilter(filter);
        String fileName = "";
        // show save file dialog 
   	 //saveEx();
        if (file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // get full path of selected file
            fileName = file.getSelectedFile().getAbsolutePath();
            // get meta of text
            try {
                
            } catch (Exception e) {
                System.out.println("Err:" + e.toString());
            }

        } else {
            return;
        }
    }
    
    private void openEx() {
    	try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        JFileChooser file = new JFileChooser();
       // TextFilter filter = new TextFilter();
       // file.setFileFilter(filter);
        String fileName = "";
        // show open file dialog
        if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileName = file.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }
        // using richtext format
        RTFEditorKit rtf = new RTFEditorKit();
        try {
            // load file into jTextPane
            FileInputStream fi = new FileInputStream(fileName);
            rtf.read(fi, maintext.getDocument(), 0);
            fi.close();
        } catch (Exception e) {
            System.out.println("err:" + e.toString());
        }
    }
    
    
    protected void setAttributeSet(AttributeSet attr) {
    	//if (m_skipUpdate)
    	      //return;
    	    int xStart = maintext.getSelectionStart();
    	    int xFinish = maintext.getSelectionEnd();
    	    if (!maintext.hasFocus()) {
    	      //xStart = m_xStart;
    	      //xFinish = m_xFinish;
    	    }
    	    if (xStart != xFinish) {
    	      doc.setCharacterAttributes(xStart, xFinish - xStart, 
    	        attr, false);
    	    } 
    	    else {
    	    	//doc.setCharacterAttributes(xStart, xFinish - xStart,
    	    }
      }
    
    

    
private void loadFont() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // get all font name 
        String[] fontNames = gEnv.getAvailableFontFamilyNames();
            // load to combobox
        ComboBoxModel model = new DefaultComboBoxModel(fontNames);
        jcbFont.setModel(model);
}
    
@SuppressWarnings("null")
public void createToolbar(){
    tb = new JToolBar();
    
    tb.setLayout(new FlowLayout(FlowLayout.LEFT));
        
    //tb.setAlignmentX(1);
    //wrapper.add(tb); 
    
	    
  //Icon someIcon = new ImageIcon(getClass().getResource("res/table.png"));
    newIcon = new ImageIcon(getClass().getResource("res/new.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    newAction = new AbstractAction("someActionCommand", newIcon) {
   	     @Override
 	    public void actionPerformed(ActionEvent e) {
 	        // do something.
 	    }
 	};
    tb.add(newAction);
    //tb.setAlignmentX(1);
    //wrapper.add(tb); 
    
    
    openIcon = new ImageIcon(getClass().getResource("res/open.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    openAction = new AbstractAction("someActionCommand", openIcon) {
	     @Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	 openEx();
	    }
	};
	tb.add(openAction);
 //tb.setAlignmentX(1);
 // 
 

    saveIcon = new ImageIcon(getClass().getResource("res/save.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    saveAction = new AbstractAction("someActionCommand", saveIcon) {
	     @Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	saveEx();
	    }
	};
	tb.add(saveAction);
	//
	tb.add(new JSeparator(SwingConstants.VERTICAL));
	
	//
    letterIcon = new ImageIcon(getClass().getResource("res/letter.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    letterbutton = new JButton(letterIcon);
    //button.setActionCommand(SOMETHING_ELSE);
    letterbutton.setToolTipText("글자모양");
    
    letterbutton.addActionListener(new AbstractAction("someActionCommand", paragraphIcon) {
    	@Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	 
	    	 m_fontDialog.setVisible(true);
	    }
	});
	
	tb.add(letterbutton);
	

    paragraphIcon = new ImageIcon(getClass().getResource("res/paragraph.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    paragraphbutton = new JButton(paragraphIcon);
    //button.setActionCommand(SOMETHING_ELSE);
    paragraphbutton.setToolTipText("단락모양");
    
    
    paragraphbutton.addActionListener(new AbstractAction("someActionCommand", paragraphIcon) {
	     @Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	 m_paragraphDialog.setVisible(true);
	    }
	});
	tb.add(paragraphbutton);
	
	multicolumnIcon = new ImageIcon(getClass().getResource("res/multicolumn.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
	multicolumnbutton = new JButton(multicolumnIcon);
 //button.setActionCommand(SOMETHING_ELSE);
	multicolumnbutton.setToolTipText("다단");
 
 
	multicolumnbutton.addActionListener(new AbstractAction("someActionCommand", multicolumnIcon) {
	     @Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	 System.out.println("ddd");
	    	 m_multiColumnDialog.setVisible(true);
	    }
	});
	tb.add(multicolumnbutton);


	
	
    ////second toolbar
    
    tb2 = new JToolBar();
    tb2.setLayout(new FlowLayout(FlowLayout.LEFT));

    //zoom
    JLabel jLabel3 = new JLabel();
	jLabel3.setText("크기");
    tb2.add(jLabel3);

    
    jcbSelectZoom = new javax.swing.JComboBox();
    
    jcbSelectZoom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50%", "80%", "100%", "120%", "150%", "200%" }));
    jcbSelectZoom.setSelectedItem("100%");
    
    

    tb2.add(jcbSelectZoom);
    
    JLabel jLabel2 = new JLabel();
	//jLabel2.setText("?���������������");
    //
    
    //load fonts
    jcbFont = new javax.swing.JComboBox();
    jcbFont.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    loadFont();
    jcbFont.setSelectedItem("돋움");
    
    jcbSelectSize = new javax.swing.JComboBox();
    
    jcbSelectSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "9", "10", "12", "14", "16", "18", "22", "26", "30", "34", "36", "40", "44", "48", "52" }));
    jcbSelectSize.setSelectedItem("18");
    
    
    jcbSelectSpace = new javax.swing.JComboBox();
    
    jcbSelectSpace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50%", "80%", "100%", "120%", "140%", "160%", "180%","200%", "250%", "300%"}));
    jcbSelectSpace.setSelectedItem("160%");


    tb2.add(jcbFont);
    tb2.add(jcbSelectSize);
    tb2.add(jcbSelectSpace);
    //
    colorIcon = new ImageIcon(getClass().getResource("res/textcolor.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    
    
    


  //chracter set icons
    boldIcon = new JToggleButton();
    
    //Insets is = new Insets(0, 0, 0, 0);
    		
    
    Icon bold = new ImageIcon(getClass().getResource("res/bold.png"));
    boldIcon.setIcon(bold);
    boldIcon.setMargin(new Insets(0, 0, 0, 0));
    boldIcon.setToolTipText("굵게");
    
	 
    //tb.add(boldIcon);
    //
    italicIcon = new JToggleButton();
    Icon italic = new ImageIcon(getClass().getResource("res/italic.png"));
    italicIcon.setIcon(italic);
    italicIcon.setToolTipText("기울임");
    italicIcon.setMargin(new Insets(0, 0, 0, 0));
    
     
    //tb.add(italicIcon);
    
    underlineIcon = new JToggleButton();
    Icon underline = new ImageIcon(getClass().getResource("res/underline.png"));
    underlineIcon.setIcon(underline);
    underlineIcon.setToolTipText("밑줄");
    underlineIcon.setMargin(new Insets(0, 0, 0, 0));
    
    
    //tb.add(underlineIcon);
    
    JPanel chtools = new JPanel();
    chtools.setLayout(new GridLayout(1, 3));
    
    chtools.add(boldIcon);
    chtools.add(italicIcon);
    chtools.add(underlineIcon);
    
    tb2.add(chtools);

  //pharagraph alignment icon set
    leftalignIcon = new JToggleButton();
    
    //Insets is = new Insets(0, 0, 0, 0);
    		
    
    Icon leftalign = new ImageIcon(getClass().getResource("res/justifyleft.png"));
    leftalignIcon.setIcon(leftalign);
    leftalignIcon.setMargin(new Insets(0, 0, 0, 0));
    

	 
    //tb.add(boldIcon);
    //
    bothalignIcon = new JToggleButton();
    Icon bothalign = new ImageIcon(getClass().getResource("res/justifyblock.png"));
    bothalignIcon.setIcon(bothalign);
    bothalignIcon.setMargin(new Insets(0, 0, 0, 0));
    
   
	 
    //tb.add(italicIcon);
    
    rightalignIcon = new JToggleButton();
    Icon rightalign = new ImageIcon(getClass().getResource("res/justifyright.png"));
    rightalignIcon.setIcon(rightalign);
    rightalignIcon.setMargin(new Insets(0, 0, 0, 0));
    
    
    //tb.add(underlineIcon);
    centeralignIcon = new JToggleButton();
    Icon centeralign = new ImageIcon(getClass().getResource("res/justifycenter.png"));
    centeralignIcon.setIcon(centeralign);
    centeralignIcon.setMargin(new Insets(0, 0, 0, 0));
    
    boldIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {

				MutableAttributeSet attr = new SimpleAttributeSet();
		        StyleConstants.setBold(attr, boldIcon.isSelected());
		        setAttributeSet(attr);
		}
	});
	
	italicIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			MutableAttributeSet attr = new SimpleAttributeSet();
	        StyleConstants.setItalic(attr, italicIcon.isSelected());
	        setAttributeSet(attr);    					
			
		}
	});
	
	underlineIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			MutableAttributeSet attr = new SimpleAttributeSet();
	        StyleConstants.setUnderline(attr, underlineIcon.isSelected());
	        setAttributeSet(attr);  
		}
	});
    
	colorAction = new AbstractAction("someActionCommand", colorIcon) {
	     @Override
	    public void actionPerformed(ActionEvent e) {
	        // do something.
	    	 Color jColor = selectColor;
	         // open color dialog and select Color
	         if ((jColor = JColorChooser.showDialog(null, "?������?������ ?���������?������"
	         		+ "", jColor)) != null) {
	             selectColor = jColor;
	             // set text color
	             //maintext.setForeground(selectColor);
	             SimpleAttributeSet attr = new SimpleAttributeSet();
	             StyleConstants.setForeground(attr, jColor);
	             setAttributeSet(attr);

	         }
	    }
	};
	tb2.add(colorAction);
	
    leftalignIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (leftalignIcon.isSelected()) {
				MutableAttributeSet attr = new SimpleAttributeSet();
		        
		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
		        //setAttributeSet(attr);
		        int xStart = maintext.getSelectionStart();
	            int xFinish = maintext.getSelectionEnd();
	            doc.setParagraphAttributes(xStart, xFinish-xStart, attr, false);
			}
		}
	});
    
    rightalignIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (rightalignIcon.isSelected()) {
				MutableAttributeSet attr = new SimpleAttributeSet();
		        
		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
		        //setAttributeSet(attr);
		        int xStart = maintext.getSelectionStart();
	            int xFinish = maintext.getSelectionEnd();
	            doc.setParagraphAttributes(xStart, xFinish-xStart, attr, false);
			}
		}
	});
    
    centeralignIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (centeralignIcon.isSelected()) {
				centeralignIcon.setIcon(new ImageIcon(getClass().getResource("res/justifycenter.png")));
				MutableAttributeSet attr = new SimpleAttributeSet();
		        
		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
		        int xStart = maintext.getSelectionStart();
	            int xFinish = maintext.getSelectionEnd();
		        doc.setParagraphAttributes(xStart, xFinish-xStart, attr, false);
			}
		}
	});
    
    bothalignIcon.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (bothalignIcon.isSelected()) {
				bothalignIcon.setIcon(new ImageIcon(getClass().getResource("res/justifycenter.png")));
				MutableAttributeSet attr = new SimpleAttributeSet();
		        
		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_JUSTIFIED);
		        int xStart = maintext.getSelectionStart();
	            int xFinish = maintext.getSelectionEnd();
		        doc.setParagraphAttributes(xStart, xFinish-xStart, attr, false);
			}
		
		}
	});

    
    
    JPanel paratools = new JPanel();
    paratools.setLayout(new GridLayout(1, 3));
    
    
    paratools.add(bothalignIcon);
    paratools.add(leftalignIcon);
    paratools.add(centeralignIcon);
    paratools.add(rightalignIcon);
    
    
    tb2.add(paratools);
    
    //
	
    
    tb.add(new JSeparator(SwingConstants.VERTICAL));

    tableIcon = new ImageIcon(getClass().getResource("res/table.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    tableAction = new AbstractAction("someActionCommand", tableIcon) {
   	     @Override
 	    public void actionPerformed(ActionEvent e) {
 	        // do something.
   	    	 m_tableDialog.setVisible(true);
 	    }
 	};
    tb.add(tableAction);
    

    imageIcon = new ImageIcon(getClass().getResource("res/image.png"));
	   //Action action = new AbstractAction("someActionCommand", someIcon) {
    imageAction = new AbstractAction("someActionCommand", imageIcon) {
   	     @Override
 	    public void actionPerformed(ActionEvent e) {
 	        // do something.
 	    }
 	};
    tb.add(imageAction);
    





    

	
}
	public void showAttribute(){
    	//get attributes
	  	  
	      AttributeSet currentattr = new SimpleAttributeSet();
	      currentattr = maintext.getCharacterAttributes();
	      
	      Boolean isBold = StyleConstants.isBold(currentattr);
	      Boolean isItalic = StyleConstants.isItalic(currentattr);
	      Boolean isUnderline = StyleConstants.isUnderline(currentattr);
	      //
	      int alignment = StyleConstants.getAlignment(currentattr);
	      	    	        
	      if (alignment == StyleConstants.ALIGN_JUSTIFIED) bothalignIcon.setSelected(true);
	      	else bothalignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_CENTER) centeralignIcon.setSelected(true);
	      	else centeralignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_RIGHT) rightalignIcon.setSelected(true);
	      	else rightalignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_LEFT) leftalignIcon.setSelected(true);
	      	else leftalignIcon.setSelected(false);
	}

	public void createNew(){
		MainTextPane maintext = new MainTextPane();
		ConPanel centerp = new ConPanel(maintext);
		centerp.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		centerp.setLayout(new BorderLayout());
	    centerp.setBackground(Color.white);
	                    
		PanelRuler ruler = new PanelRuler(centerp);
	   	ruler.setPreferredSize(new Dimension(WIDTH, 19));
	
	   	JScrollPane scrollPane = new JScrollPane();
	   	scrollPane.setViewportView(maintext);
		   
	  	centerp.add(ruler, BorderLayout.NORTH);
	  	centerp.add(scrollPane, BorderLayout.CENTER);
	  	
		JInternalFrame internalFrame = new JInternalFrame("제목없음",true,true,true,true);
		
		
	  	//
	  	internalFrame.setVisible(true);
		
	  	internalFrame.add(centerp);
	  	desktop.add(internalFrame);
	  	
	  	internalFrame.setSize(internalFrame.getMaximumSize());
	  	internalFrame.pack();
	  	
	
	}
    
    public void createMenu(){
    	JMenuItem   new_blank, open, exit, close, find, replace, about, random, deletefile, resize, save, saveas;
	    JMenuItem   cut_text, paste_text, fullscreen, delete, previous, next;
	    
    	
    	JMenuBar menuBar = new JMenuBar();
        
        //JMenu menu1    = new JMenu("File");
    	JMenu menu1    = new JMenu("파일(F)");
        menu1.setMnemonic(KeyEvent.VK_F);
        
        ImageIcon icon_new = new ImageIcon(getClass().getResource("res/new.png"));
        new_blank     = new JMenuItem("새문서(N)", icon_new);
        new_blank.setMnemonic(KeyEvent.VK_N);
        new_blank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
              	//
              	createNew();
	
	            }
	        });


        menu1.add(new_blank);
        //     	
      	
        ImageIcon icon_open = new ImageIcon(getClass().getResource("res/folder.png"));
        open     = new JMenuItem("열기(O)", icon_open);
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    //System.exit(0);
            		openEx();
            }
        });

        menu1.add(open);
        
        ImageIcon icon_save = new ImageIcon(getClass().getResource("res/save.png"));
        save     = new JMenuItem("편집(S)", icon_save);
        //close     = new JMenuItem("Close");
        save.setMnemonic(KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    //System.exit(0);
            		//saveEx();

                    try {
                    	FileOutputStream output = new FileOutputStream("C:\\My3.xml");
                    	maintext.getEditorKit().write(output, maintext.getDocument(), 0, maintext.getDocument().getLength());
                         
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
        });
    	
        
        menu1.add(save);
        //
        ImageIcon icon_close = new ImageIcon(getClass().getResource("res/close.png"));
        close     = new JMenuItem("닫기(C)", icon_close);
        //close     = new JMenuItem("Close");
        close.setMnemonic('C');
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    System.exit(0);
            }
        });
    	      
    	      
        menu1.add(close);
        
       

        menuBar.add(menu1);
              
     
        
        JMenu menu2    = new JMenu("편집(E)");
                 
            
        menu2.setMnemonic(KeyEvent.VK_E);
        
        cut_text     = new JMenuItem("잘라내기");
              
        menu2.add(cut_text);
        
        paste_text     = new JMenuItem("붙여널기");
        
        menu2.add(paste_text);
        
        
        find     = new JMenuItem("닫기(C)", icon_close);
        //close     = new JMenuItem("Close");
        find.setMnemonic(KeyEvent.VK_C);
                
        
        find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    //System.exit(0);
            	//repaint();
                if (m_findDialog==null)
                	m_findDialog = new FindDialog(maintext, 0);
                else
                  m_findDialog.setSelectedIndex(0);

                Dimension d1 = m_findDialog.getSize();
                Dimension d2 = HaneolWord.this.getSize();
                int x = Math.max((d2.width-d1.width)/2, 0);
                int y = Math.max((d2.height-d1.height)/2, 0);
                m_findDialog.setBounds(x + HaneolWord.this.getX(),
                  y + HaneolWord.this.getY(), d1.width, d1.height);

                m_findDialog.show();
            }
        });
        menu2.add(find);
        menuBar.add(menu2);
                
        
        
       //
        JMenu menu3    = new JMenu("보기(V)");
        menu3.setMnemonic(KeyEvent.VK_V);
        
        parsym = new JCheckBoxMenuItem("view para sym");
        menu3.add(parsym);
        
        
        
        menuBar.add(menu3);
        fullscreen     = new JMenuItem("전체화면");
        menu3.add(fullscreen);
        
        JMenu menu4 = new JMenu("보통(D)");
        menu4.setMnemonic(KeyEvent.VK_D);
        
        JMenu menu5 = new JMenu("도구(T)");
        menu5.setMnemonic(KeyEvent.VK_T);
        
        JMenu menu6    = new JMenu("도움말(H)");
        menu6.setMnemonic('H');
        about     = new JMenuItem("정보(A)");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
               //     editor__.paste();
         	   //JOptionPane.showMessageDialog(null, null);
            	//AboutDialog ab = new AboutDialog();
            	//ab.show();
            }
        });
    	      

        about.setMnemonic(KeyEvent.VK_A);
        menu6.add(about);
        
        menuBar.add(menu4);
        menuBar.add(menu5);
        menuBar.add(menu6);
        
        setJMenuBar(menuBar);	
        

    }
      
    
      
    HaneolWord() throws IOException{
    	
    	
    	//checkUpdate();
    	
    	this.setTitle("한얼워드프로세서");
    	this.setSize(800, 600); // this is important
    	
    	ImageIcon img = new ImageIcon(getClass().getResource("res/new_logo.png"));

    	//Then set it to your JFrame with setIconImage():

    	this.setIconImage(img.getImage());
    	
    	//
    	//iinialize Dialogs
    	m_paragraphDialog = new ParagraphDialog(this);
    	
    	GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	String[] fontNames = gEnv.getAvailableFontFamilyNames();
    	String[] fontSize =  { "8", "9", "10", "12", "14", "16", "18", "22", "26", "30", "34", "36", "40", "44", "48", "52" };
    	m_fontDialog = new FontDialog(this, fontNames, fontSize);
    	
    	m_multiColumnDialog = new MultiColumnDialog(this);
    	m_tableDialog = new TableDialog(this);
    	
    	//
    	StyleContext context = new StyleContext();
	    StyledDocument document = new DefaultStyledDocument(context);

	    Style style = context.getStyle(StyleContext.DEFAULT_STYLE);

	    StyleConstants.setLeftIndent(style, 0);
	  //JPanel centerp = new JPanel();
    	
	    
        maintext = new MainTextPane();
	    maintext.setEditorKit(new CustomEditorKit());
        //StyledDocument doc=(StyledDocument)maintext.getDocument();
	    doc = (CustomDocument) maintext.getDocument();
        //doc.setCharacterAttributes(0,1,attrs,true);
        //
	    
	    //initialize attributes
	    SimpleAttributeSet attr=new SimpleAttributeSet();
	    StyleConstants.setFontSize(attr, 18);
	    StyleConstants.setFontFamily(attr, "돋움");
	    StyleConstants.setLineSpacing(attr, 0.6f);
	    //
	    attr.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
	    attr.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.FALSE);
       	
	    
        try {
            StyleConstants.setFontFamily(attr,"Lucida Sans");
            doc.insertString(0, "Lusida Sans font test\n", attr);

            StyleConstants.setFontFamily(attr,"Lucida Bright");
            doc.insertString(doc.getLength(), "Lucida Bright font test\n", attr);

            StyleConstants.setFontFamily(attr,"돋움");
            doc.insertString(doc.getLength(), "돋움체 데트스 문자열입니다", attr);
            
            //
        }
        catch (BadLocationException ex) {
        }
        

        doc.setParagraphAttributes(0, doc.getLength(), attr, true);
        doc.setCharacterAttributes(0, doc.getLength(), attr, true);
        
        //TableDocument doc2 = (TableDocument) maintext.getDocument();
        /*
        try{
        	int st = doc.getLength();
        	doc.insertTable(st, 2, new int[] {250, 70, 100});
        	doc.insertString(st+3, "Cell within a table\n", null);

        }catch (BadLocationException ex) {
        }
	    */
    	maintext.setMargin(new Insets(0, (int) (20*3.78f), 0, 0));
    	maintext.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
    	
	    //  	

    	createMenu();
    	createToolbar();
    	
    	    	    	
    	//top wrapper
    	
    	container.setBackground(Color.WHITE);
    	//container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
    	container.setLayout(new BorderLayout());
    	
    	ConPanel centerp = new ConPanel(maintext);
    	centerp.setLayout(new BorderLayout());
        centerp.setBackground(Color.white);
        
        
        // textpane size
        centerp.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
    	    
    	PanelRuler ruler = new PanelRuler(centerp);
       	ruler.setPreferredSize(new Dimension(this.getWidth(), 19));
    	 	
    	//
    	JPanel toolbarwrap = new JPanel();
    	toolbarwrap.setLayout(new BoxLayout(toolbarwrap, BoxLayout.PAGE_AXIS));
    	toolbarwrap.add(tb);
    	toolbarwrap.add(tb2);
    	//toolbarwrap.add(ruler);
    	//
 	    //scroll pane
    	
    	//scrollPane = new JScrollPane(maintext);
        scrollPane.setViewportView(maintext);
 	   
      	centerp.add(ruler, BorderLayout.NORTH);
      	centerp.add(scrollPane, BorderLayout.CENTER);
      	
      	
      	 
    	//wrapper
    	JPanel left = new JPanel();
    	left.setBackground(new Color(245, 245, 245));
    	left.setPreferredSize(new Dimension(30, this.getHeight()));
    	
    	// left blank space
    	//container.add(left, BorderLayout.WEST);
    	//
    	    	
    	internalFrame = new JInternalFrame("제목없음",true,true,true,true);
    	internalFrame.setVisible(true);
    	
      	internalFrame.add(centerp);
      	desktop.add(internalFrame);
      	
      	internalFrame.setSize(internalFrame.getMaximumSize());
      	internalFrame.pack();
      	
      	
      	this.add(toolbarwrap, BorderLayout.NORTH);
 	    this.add(desktop);
 	    statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
 	    JLabel jl1 = new JLabel("빈문서1");
	    jl1.setPreferredSize(new Dimension(100, 20));
 	    statusBar.add(jl1);
 	    
 	    this.add(statusBar, BorderLayout.SOUTH);
      	
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
    	
    	//
    	this.setVisible(true);
    		
    	addMouseListener(this);
    	
    	//write
    	   try {
           	FileOutputStream output = new FileOutputStream("C:\\Myruler.xml");
				try {
					maintext.getEditorKit().write(output, maintext.getDocument(), 0, maintext.getDocument().getLength());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   
    	   //caret listener   	   
    	   
    	    maintext.addCaretListener(new CaretListener() {

    	        public void caretUpdate(CaretEvent e) {
    	          //System.out.println(e);
    	          // toolbar state update by current attributes
    	          showAttribute();
    	    
	    	        
	    	    

    	        }
    	      });
    		//default font and size
    	    
    	    
    	    jcbFont.addActionListener(new java.awt.event.ActionListener() {
    	        public void actionPerformed(java.awt.event.ActionEvent evt) {
    	            jcbFontActionPerformed(evt);
    	        }

    			private void jcbFontActionPerformed(ActionEvent evt) {
    				//Change font of text
    				String getName = jcbFont.getSelectedItem().toString();
    				//MutableAttributeSet attr = new SimpleAttributeSet();
    		        //StyleConstants.setFontFamily(attr, getName);
    		       	//setAttributeSet(attr);
    		       	
    		        
    			}
    	    });
    		
    	    jcbSelectSize.addActionListener(new java.awt.event.ActionListener() {
    	        public void actionPerformed(java.awt.event.ActionEvent evt) {
    	            jcbSelectSizeActionPerformed(evt);
    	        }

    			private void jcbSelectSizeActionPerformed(ActionEvent evt) {
    				// TODO Auto-generated method stub

    		        //String getSize = jcbSelectSize.getSelectedItem().toString();
    		        //StyleConstants.setFontSize(attr, Integer.parseInt(getSize));
    		        //StyledDocument doc = (StyledDocument) maintext.getDocument();
    		    	//doc.setCharacterAttributes(0, doc.getLength(), attr, false);
    		    	//setAttributeSet(attr);
    				
    			}
    	    });
    	    
    		jcbSelectSpace.addActionListener(new java.awt.event.ActionListener() {
    	        public void actionPerformed(java.awt.event.ActionEvent evt) {
    	        	MutableAttributeSet set = new SimpleAttributeSet(maintext.getParagraphAttributes());
    	        	String s = (String) jcbSelectSpace.getSelectedItem();
    	        	s = s.substring(0, s.length() - 1);
    	        	float factor =  (float) (Integer.parseInt(s)-100) / 100;
    	        	//System.out.println(factor);
    	        	StyleConstants.setLineSpacing(set, factor);
    	            int xStart = maintext.getSelectionStart();
    	            int xFinish = maintext.getSelectionEnd();
    	            doc.setParagraphAttributes(xStart, xFinish-xStart, set, true);
    	            //setAttributeSet(set);
    	        }
    		});
    		
    		//zoom view action
    		jcbSelectZoom.addActionListener(new java.awt.event.ActionListener() {
    	        public void actionPerformed(java.awt.event.ActionEvent evt) {
    	            //jcbSelectSizeActionPerformed(evt);
    	        	String s = (String) jcbSelectZoom.getSelectedItem();
    	            s = s.substring(0, s.length() - 1);
    	            double scale = new Double(s).doubleValue() / 100;
    	            maintext.getDocument().putProperty("ZOOM_FACTOR",new Double(scale));
    	            AttributeSet attr = maintext.getCharacterAttributes();
    	            try {
    	                StyledDocument doc=(StyledDocument)maintext.getDocument();
    	            	//TableDocument doc=(TableDocument)maintext.getDocument();
    	                doc.setCharacterAttributes(0,1,attr,true);
    	                doc.insertString(0, "", null); //refresh
    	            }
    	            catch (Exception ex) {
    	                ex.printStackTrace();
    	            }

    	        }
    		});
    		

    		
    		   	    
    		
    		maintext.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if ( (e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
	                    //System.out.println("woot!");
						 m_fontDialog.setVisible(true);
					}
					else if ( (e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
	                    //System.out.println("woot!");
						 m_findDialog.setVisible(true);
					}
	                
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
    			
    			
    			
    		});
    		
    		
    		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(
    		            KeyEvent.VK_Z, Event.CTRL_MASK);
    		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(
    		            KeyEvent.VK_Y, Event.CTRL_MASK);
    		    
    		//
    		
    		
    	    doc.addUndoableEditListener(new UndoableEditListener() {
    	        @Override
    	        public void undoableEditHappened(UndoableEditEvent e) {
    	            m_undo.addEdit(e.getEdit());
    	        }
    	    });
    	    //
    	    // Map undo action
    	    maintext.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    	            .put(undoKeyStroke, "undoKeyStroke");
    	    maintext.getActionMap().put("undoKeyStroke", new AbstractAction() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	            try {
    	                m_undo.undo();
    	             } catch (CannotUndoException cue) {}
    	        }
    	    });

    	 // setting center screen for this form
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(size.width / 2 - this.getWidth() / 2,
                    size.height / 2 - this.getHeight() / 2);
            //loadFont();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	
    	
    }

    
	@Override
	public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
		
		
			
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
	}
	
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//JPanel btnPanel = (JPanel)e.getSource();
	       System.out.println("ddd");
	       
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	    
    
}



class MainTextPane extends JTextPane{
	public Insets insets;
	int linex;
	int liney;
	int leftIndent;
	int leftMargin;
	//JTextPane maintext;
	StyledDocument doc;
	
	float scale;
	
	MainTextPane(){
		this.linex = 0;
		this.liney = 0;
		this.leftIndent = (int) (20*3.78);
		this.leftMargin = (int) (20*3.78);
		//this.doc = doc;
		//this.setPreferredSize(new Dimension(200, 200));
		//this.maintext.setPreferredSize(new Dimension(200, 300));
    	
					
	}
		
	@Override 
	public void paint (Graphics g){
		super.paint(g);
	
	}

	
	
}

class ConPanel extends JPanel{
	public Insets insets;
	int linex;
	int liney;
	int leftIndent;
	int leftMargin;
	JTextPane maintext;
	
	float scale;
	
	ConPanel(JTextPane maintext){
		this.linex = 0;
		this.liney = 0;
		this.leftIndent = (int) (20*3.78);
		this.leftMargin = (int) (20*3.78);
		this.maintext = maintext;
		//this.setPreferredSize(new Dimension(200, 200));
		this.maintext.setPreferredSize(new Dimension(200, 300));
    	
					
	}
		
	@Override 
	public void paint (Graphics g){
		super.paint(g);
		
		//setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		g.setColor(new Color(245, 245, 245));
		g.fillRect(800, 0, 1000, this.getHeight());
		
	     // right top corner
        g.setColor(Color.darkGray);
        g.drawRect(800, 0, 1000, 14);
				
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		
		float[] dashingPattern1 = {2f, 2f};
		//Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
		 //       BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
		g2d.setStroke(new BasicStroke(0.2f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 1f, dashingPattern1, 0.2f));
		g2d.draw(new Line2D.Float(linex, 5, linex, liney));
		//refresh maintext indents
		//maintext.getStyledDocument();
		maintext.setMargin(new Insets(0, leftMargin, 0, 0));
		
		
		
	}
	
}



class CustomEditorKit extends StyledEditorKit {
    
    ViewFactory defaultFactory = new StyledViewFactory();
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    public Document createDefaultDocument() {
        return new CustomDocument();
    }

    class StyledViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                }
                else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    
                	return new PagingParagraphView(elem);
                }
                else if (kind.equals(AbstractDocument.SectionElementName)) {
                    //return new ScaledView(elem, View.Y_AXIS);
                	return new SectionView(elem, View.Y_AXIS);
                	
                }
                else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                }
                else if (kind.equals(CustomDocument.ELEMENT_NAME_TABLE)) {
                    return new TableView(elem);
                }
                else if (kind.equals(CustomDocument.ELEMENT_NAME_ROW)) {
                    return new RowView(elem);
                }
                else if (kind.equals(CustomDocument.ELEMENT_NAME_CELL)) {
                    return new CellView(elem);
                }
                else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }

    }
}

//-----------------------------------------------------------------
class ScaledView extends BoxView {
	
	
    public ScaledView(Element elem, int axis) {
        super(elem, axis);
    }

    public double getZoomFactor() {
        Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
        if (scale != null) {
            return scale.doubleValue();
        }

        return 1;
    }

    public void paint(Graphics g, Shape allocation) {
        Graphics2D g2d = (Graphics2D) g;
        double zoomFactor = getZoomFactor();
        AffineTransform old = g2d.getTransform();
        g2d.scale(zoomFactor, zoomFactor);
        super.paint(g2d, allocation);
        g2d.setTransform(old);
    }

    public float getMinimumSpan(int axis) {
        float f = super.getMinimumSpan(axis);
        f *= getZoomFactor();
        return f;
    }

    public float getMaximumSpan(int axis) {
        float f = super.getMaximumSpan(axis);
        f *= getZoomFactor();
        return f;
    }

    public float getPreferredSpan(int axis) {
        float f = super.getPreferredSpan(axis);
        f *= getZoomFactor();
        return f;
    }

    protected void layout(int width, int height) {
        super.layout(new Double(width / getZoomFactor()).intValue(),
                     new Double(height *
                                getZoomFactor()).intValue());
    }

    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        double zoomFactor = getZoomFactor();
        Rectangle alloc;
        alloc = a.getBounds();
        Shape s = super.modelToView(pos, alloc, b);
        alloc = s.getBounds();
        alloc.x *= zoomFactor;
        alloc.y *= zoomFactor;
        alloc.width *= zoomFactor;
        alloc.height *= zoomFactor;

        return alloc;
    }

    public int viewToModel(float x, float y, Shape a,
                           Position.Bias[] bias) {
        double zoomFactor = getZoomFactor();
        Rectangle alloc = a.getBounds();
        x /= zoomFactor;
        y /= zoomFactor;
        alloc.x /= zoomFactor;
        alloc.y /= zoomFactor;
        alloc.width /= zoomFactor;
        alloc.height /= zoomFactor;

        return super.viewToModel(x, y, alloc, bias);
    }

}

/**
* Creates a view from an element that spans the supplied axis
* @param element
* @param axis
*/
class  SectionView extends BoxView{
	int pageNumber=0;

	protected static final int PAGE_WIDTH = 800;
	protected static final int PAGE_HEIGHT = 1200;
	
	protected static final int PAGE_INSET = 20;
	protected static final Insets PAGE_MARGIN = new Insets(10, 10, 10, 10);
	protected static final Color BACKGROUND = Color.GRAY;
	
	
	public SectionView(Element element, int axis) {
	super(element, axis);
	 
	// apply insets to width but not top/bottom as it distorts
	// breaking calculations
	setInsets((short) (0),
	(short) (PAGE_INSET + PAGE_MARGIN.left),
	(short) (0),
	(short) (PAGE_INSET + PAGE_MARGIN.right));
	}
	 
	 
	protected void layout(int width, int height) {
	width = PAGE_WIDTH - 2 * PAGE_INSET - PAGE_MARGIN.left - PAGE_MARGIN.right;
	super.layout(width, height);
	}
	
	protected int calculatePageBreak(int pageNumber) {
		int pageBreak = (pageNumber * PAGE_HEIGHT) - PAGE_INSET - PAGE_MARGIN.bottom;
		return pageBreak;
	}
	 
	 
	public float getPreferredSpan(int axis) {
		float span = 0;
		if (axis == View.X_AXIS) {
		span = PAGE_WIDTH;
		} else {
		span = pageNumber * PAGE_HEIGHT;
		}
		return span;
	}
	 
	 
	public float getMinimumSpan(int axis) {
		return getPreferredSpan(axis);
	}
	 
	 
	public float getMaximumSpan(int axis) {
		return getPreferredSpan(axis);
	}
	 
	 
	protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) 
	{
	super.layoutMajorAxis(targetSpan, axis, offsets, spans);
		 
		int totalOffset = PAGE_INSET + PAGE_MARGIN.top;
		int pageBreak;
		pageNumber = 1;
		PagingParagraphView view;
	 
		for (int i = 0; i < offsets.length; i++) {
			//System.out.println(offsets[i]+" "+spans[i]);
		}
		for (int i = 0; i < offsets.length; i++) {
			offsets[i] = totalOffset;
			pageBreak = calculatePageBreak(pageNumber);
			 
			view = (PagingParagraphView) getView(i);
			//totalOffset is equal to paragraphOffset
			view.setPargraphOffset(totalOffset);
			view.setStartPage(pageNumber);
			 
			if ((spans[i] + offsets[i]) > pageBreak) {
				view.layout(view.getWidth(), getHeight());
				pageNumber = view.getEndPage();
				spans[i] += view.getAdjustedSpan();
			}
			//System.out.println(spans[i]);
			 
			totalOffset = offsets[i] + spans[i];
			System.out.println(totalOffset);
		}
	}
	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
		 
		Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
		Rectangle page = new Rectangle(alloc.x, alloc.y, PAGE_WIDTH, PAGE_HEIGHT);
		 
		for (int i = 0; i < pageNumber; i++) {
			page.y = alloc.y + PAGE_HEIGHT * i;
			//if (page.intersects(alloc))
			//paintPageFrame(g, page);
	
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
				//g.drawLine(page.x, page.y, page.x+ page.width, page.y);
			float[] dashingPattern1 = {2f, 2f};
			//Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
			 //       BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
			g2d.setStroke(new BasicStroke(0.2f, BasicStroke.CAP_BUTT,
			        BasicStroke.JOIN_MITER, 1f, dashingPattern1, 0.2f));
			g2d.draw(new Line2D.Float(page.x, page.y, page.x+ page.width, page.y));
			//g.drawLine(page.x, page.y, page.x+ page.width, page.y);
			 String sN = Integer.toString(i + 1);             
	         String pageStr = "페이지:" + sN;
	         //pageStr += " of " + sC;
	         if (i == pageNumber-1){
	        	 g.drawString(pageStr, page.width - 50,
	                      				page.y + page.height - 18);
	         } else g.drawString(pageStr, page.width - 50, page.y + page.height - 3);
	        
		}
	}
}


class PagingParagraphView extends ParagraphView {
	private int startPage, endPage;
	private int adjustedSpan;
	private int paragraphOffset;
	
	protected static final int PAGE_WIDTH = 800;
	protected static final int PAGE_HEIGHT = 1200;
	
	protected static final int PAGE_INSET = 20;
	protected static final Insets PAGE_MARGIN = new Insets(10, 10, 10, 10);
	protected static final Color BACKGROUND = Color.GRAY;
	
	 
	public PagingParagraphView(Element elem) {
		super(elem);
			 
			startPage = 1;
			endPage = 1;
			adjustedSpan = 0;
			paragraphOffset = 0;
		}
	
		protected int calculatePageBreak(int pageNumber) {
			int pageBreak = (pageNumber * PAGE_HEIGHT) - PAGE_INSET - PAGE_MARGIN.bottom;
			return pageBreak;
		}
		 
		 
		public void setStartPage(int sp) {
			startPage = sp;
		}
		 
		 
		public int getEndPage() {
			return endPage;
		}
		 
		 
		public int getAdjustedSpan() {
			return adjustedSpan;
		}
		 
		 
		public void setPargraphOffset(int po) {
			paragraphOffset = po;
		}
		 
		 
		public void layout(int width, int height) {
			super.layout(width, height);
		}
		 
		 
		protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) 
		{
			super.layoutMajorAxis(targetSpan, axis, offsets, spans);
			//System.out.println(endPage);
		    // initial paragraph is 30
			if (paragraphOffset != 0) {
				endPage = startPage;
				// initial relative break
				int relativeBreak = calculatePageBreak(endPage) - paragraphOffset;
				int correctedOffset;
				adjustedSpan = 0;
				//System.out.println(endPage);
				for (int i = 0; i < offsets.length; i++) {
				// determine the location of the page break
				 
					if (offsets[i] + spans[i] > relativeBreak) {
						correctedOffset = relativeBreak
								+ PAGE_MARGIN.bottom + (2 * PAGE_INSET) + PAGE_MARGIN.top
								- offsets[i];
						 
						for (int j = i; j < offsets.length; j++) {
							offsets[j] += correctedOffset;
							//
							
						}
						System.out.println("corrected offset: "+correctedOffset);
						adjustedSpan = correctedOffset;
						endPage++;
						//relativeBreak = calculatePageBreak(endPage) - paragraphOffset;
						relativeBreak = endPage * PAGE_HEIGHT - 20 - 10 - paragraphOffset;
						
						System.out.println("paragraph offset: "+paragraphOffset);
						System.out.println("adjusted Span: "+adjustedSpan);
						System.out.println("end page: "+endPage);
						System.out.println("relative break: "+relativeBreak);
						
					}
				}
			}
		}
	
}


