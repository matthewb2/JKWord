package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ToolBarEx {
	NeoHaneol owner;
	JEditorPane pane;
	//
    //protected JToolBar tb, tb2;
    
    //protected JComboBox zoomCombo;
	protected JComboBox jcbSelectSize;
	protected JComboBox jcbSelectSpace;
	protected JComboBox jcbFont;
    protected int height_max, width_max;
    
	protected Color selectColor;
    protected TableButton tableButton1;
    protected ColorButton colorButton1 = new ColorButton();
    
    public boolean m_skipUpdate;
    protected int m_xStart = -1;
    protected int m_xFinish = -1;
    protected String styleName;
    
    protected JToggleButton leftalignIcon, bothalignIcon, rightalignIcon, centeralignIcon;
    protected JToggleButton italicIcon, boldIcon, underlineIcon;
    
    String fontName[];
    //
	
	ToolBarEx(NeoHaneol owner){
		this.owner = owner;
	    //load fonts
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontName = gEnv.getAvailableFontFamilyNames();
        
		createToolbar();
	}
	
	public void createToolbar(){
		
		Icon tableIcon, imageIcon, newIcon, openIcon, saveIcon, printIcon, previewIcon, colorIcon, letterIcon, paragraphIcon, multicolumnIcon;
    	JButton tablebutton, imagebutton, letterbutton, paragraphbutton;
		final JButton multicolumnbutton;
        Action tableAction, imageAction, newAction, openAction, saveAction, printAction, previewAction, findAction, italicAction, boldAction, underlAction, colorAction, letterAction, paragraphAction;
        final JToolBar tb = new JToolBar();
        tb.setLayout(new FlowLayout(FlowLayout.LEFT));
        newIcon = new ImageIcon(getClass().getResource("res/new.png"));
        newAction = new AbstractAction("someActionCommand", newIcon) {
       	     @Override
       	     public void actionPerformed(ActionEvent e) {
     	        // do something.
     	    }
     	};
        tb.add(newAction);
        //
        openIcon = new ImageIcon(getClass().getResource("res/open.png"));
        openAction = new AbstractAction("someActionCommand", openIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // do something.
    	    	 try {
    	    		 owner.openEx();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    	    }
    	};
    	tb.add(openAction);
        saveIcon = new ImageIcon(getClass().getResource("res/save.png"));
        saveAction = new AbstractAction("someActionCommand", saveIcon) {
    	        @Override
        	    public void actionPerformed(ActionEvent e) {
     	        // do something.
    	    	//save
    	    		try {
						owner.save(owner.currentFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	    }
    	};
    	tb.add(saveAction);
    	tb.add(new JSeparator(SwingConstants.VERTICAL));
    	
    	printIcon = new ImageIcon(getClass().getResource("res/print.png"));
        JButton printbutton = new JButton(printIcon);
        printbutton.setToolTipText("인쇄");
        
        printbutton.addActionListener(new AbstractAction("someActionCommand", printIcon) {
        	@Override
    	    public void actionPerformed(ActionEvent e) {
    	
        	}
        });
        tb.add(printbutton);
        
        previewIcon = new ImageIcon(getClass().getResource("res/preview-rtl.png"));
        JButton previewbutton = new JButton(previewIcon);
        previewbutton.setToolTipText("미리보기");
        
        previewbutton.addActionListener(new AbstractAction("someActionCommand", previewIcon) {
        	@Override
    	    public void actionPerformed(ActionEvent e) {
    	
        	}
        });
        tb.add(previewbutton);
        
        letterIcon = new ImageIcon(getClass().getResource("res/letter.png"));
        letterbutton = new JButton(letterIcon);
        letterbutton.setToolTipText("글자모양");
        letterbutton.addActionListener(new AbstractAction("someActionCommand", letterIcon) {
        	@Override
    	    public void actionPerformed(ActionEvent e) {
    	         // do something.
    	    	 owner.m_fontDialog.setVisible(true);
    	    }
    	});
    	tb.add(letterbutton);
        paragraphIcon = new ImageIcon(getClass().getResource("res/paragraph.png"));
        paragraphbutton = new JButton(paragraphIcon);
        paragraphbutton.setToolTipText("단락모양");
        paragraphbutton.addActionListener(new AbstractAction("someActionCommand", paragraphIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // do something.
    	    }
    	});
    	tb.add(paragraphbutton);
    	multicolumnIcon = new ImageIcon(getClass().getResource("res/column.png"));
    	multicolumnbutton = new JButton(multicolumnIcon);
     	multicolumnbutton.setToolTipText("다단");
    	multicolumnbutton.addActionListener(new AbstractAction("someActionCommand", multicolumnIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // do something.
    	    	 final MultiColumnDialog mcd = new MultiColumnDialog(owner.m_monitor);
    	    	 mcd.addComponentListener(new ComponentListener() {
		 		    public void componentHidden(ComponentEvent e) {
		 		    	String str= owner.m_monitor.getText();
		 		    	String font = jcbFont.getSelectedItem().toString();
		 		    	int fontsize =  Integer.parseInt(jcbSelectSize.getSelectedItem().toString());
    			    	//
    			    	MutableAttributeSet attr = new SimpleAttributeSet();
    			        StyleConstants.setFontFamily(attr, font);
    			        StyleConstants.setFontSize(attr, fontsize);
    			        StyleConstants.setLineSpacing(attr, .6f);
    	 		       
    			        owner.m_monitor.setEditorKit(new CustomEditorKit(mcd.m_colCount));
    	 		    	CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
    	 		    	owner.setPageVariable();
    	 		        //
    	 		        try {
							doc.insertString(0,  str,  attr);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
    	 		        mcd.dispose();
    	 		    }

    	 			@Override
    	 			public void componentMoved(ComponentEvent arg0) {
    	 				// TODO Auto-generated method stub
    	 				
    	 			}

    	 			@Override
    	 			public void componentResized(ComponentEvent arg0) {
    	 				// TODO Auto-generated method stub
    	 				
    	 			}

    	 			@Override
    	 			public void componentShown(ComponentEvent arg0) {
    	 				// TODO Auto-generated method stub
    	 				
    	 			}
    	     	});
    	    	 
    	    }
    	});
    	
    	tb.add(multicolumnbutton);
        //
        final JToolBar tb2 = new JToolBar();
        tb2.setLayout(new FlowLayout(FlowLayout.LEFT));
        //zoom
        JLabel jLabel3 = new JLabel();
    	jLabel3.setText("크기");
        tb2.add(jLabel3);
        //owner.zoomCombo = new javax.swing.JComboBox();
        owner.zoomCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50%", "80%", "100%", "120%", "150%", "200%" }));
        owner.zoomCombo.setEditable(true);
        owner.zoomCombo.setSelectedItem("100%");
        owner.zoomCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) owner.zoomCombo.getSelectedItem();
                s = s.substring(0, s.length() - 1);
                owner.viewScale = new Double(s).doubleValue() / 100;
                owner.m_monitor.getDocument().putProperty("ZOOM_FACTOR", owner.viewScale);
                // update statusbar
                owner.spinSlider.update(owner.viewScale);
                try {
					owner.adjustmainTextPane();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                // update ruler
                owner.updateVar();
                owner.repaint();
            }
        });
        
        
        
        tb2.add(owner.zoomCombo);
        JLabel jLabel2 = new JLabel();
       
    
        Integer array[] = new Integer[fontName.length];
        for(int i=1;i<=fontName.length;i++) {
          array[i-1] = i;
        }
        
        jcbFont = new JComboBox(array);
        ComboBoxRenderar renderar = new ComboBoxRenderar();
        jcbFont.setRenderer(renderar);
        //set default font              
        for (int i=0; i<fontName.length; i++){
        	if (fontName[i].matches("^바탕체+$")) {
        			//System.out.println("ddd");
        		jcbFont.setSelectedIndex(i);
        		owner.defaultFont = fontName[i];
        		break;
        	}
        }
        
        //
        jcbFont.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	//Change font of text
				int getIndex = jcbFont.getSelectedIndex();
				//fontName[getIndex];
				System.out.println(fontName[getIndex]);
				MutableAttributeSet attr = new SimpleAttributeSet();
		        StyleConstants.setFontFamily(attr, fontName[getIndex]);
		        owner.setAttributeSet(attr);
	        }
	    });
        jcbSelectSize = new javax.swing.JComboBox();
        jcbSelectSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "22", "24", "26", "28", "30", "34", "36", "40", "44", "48", "52" }));
        jcbSelectSize.setSelectedItem("14");
        jcbSelectSize.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
	        	int fontsize =  Integer.parseInt(jcbSelectSize.getSelectedItem().toString());
	        	//
	        	MutableAttributeSet attr = new SimpleAttributeSet();
	        	StyleConstants.setFontSize(attr,  fontsize);
	        	owner.setAttributeSet(attr);
	       }
        });
        jcbSelectSpace = new javax.swing.JComboBox();
        jcbSelectSpace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50%", "80%", "100%", "120%", "140%", "160%", "180%","200%", "250%", "300%"}));
        jcbSelectSpace.setSelectedItem("160%");
        jcbSelectSpace.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	MutableAttributeSet set = new SimpleAttributeSet(owner.m_monitor.getParagraphAttributes());
	        	String s = (String) jcbSelectSpace.getSelectedItem();
	        	s = s.substring(0, s.length() - 1);
	        	float factor =  (float) (Integer.parseInt(s)-100) / 100;
	        	StyleConstants.setLineSpacing(set, factor);
	            int xStart = owner.m_monitor.getSelectionStart();
	            int xFinish = owner.m_monitor.getSelectionEnd();
	            CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
	            doc.setParagraphAttributes(xStart, xFinish-xStart, set, true);
	        }
		});
        tb2.add(jcbFont);
        tb2.add(jcbSelectSize);
        tb2.add(jcbSelectSpace);
        //
        colorIcon = new ImageIcon(getClass().getResource("res/textcolor.png"));
        boldIcon = new JToggleButton();
        Icon bold = new ImageIcon(getClass().getResource("res/bold.png"));
        boldIcon.setIcon(bold);
        boldIcon.setMargin(new Insets(0, 0, 0, 0));
        boldIcon.setToolTipText("굵게");
        //
        owner.italicIcon = new JToggleButton();
        Icon italic = new ImageIcon(getClass().getResource("res/italic.png"));
        owner.italicIcon.setIcon(italic);
        owner.italicIcon.setToolTipText("기울임");
        owner.italicIcon.setMargin(new Insets(0, 0, 0, 0));
        owner.underlineIcon = new JToggleButton();
        Icon underline = new ImageIcon(getClass().getResource("res/underline.png"));
        owner.underlineIcon.setIcon(underline);
        owner.underlineIcon.setToolTipText("밑줄");
        owner.underlineIcon.setMargin(new Insets(0, 0, 0, 0));
        JPanel chtools = new JPanel();
        chtools.setLayout(new GridLayout(1, 3));
        //
        chtools.add(boldIcon);
        chtools.add(owner.italicIcon);
        chtools.add(owner.underlineIcon);
        tb2.add(chtools);
        //pharagraph alignment icon set
        leftalignIcon = new JToggleButton();
        Icon leftalign = new ImageIcon(getClass().getResource("res/justifyleft.png"));
        leftalignIcon.setIcon(leftalign);
        leftalignIcon.setMargin(new Insets(0, 0, 0, 0));
        //
        bothalignIcon = new JToggleButton();
        Icon bothalign = new ImageIcon(getClass().getResource("res/justifyblock.png"));
        bothalignIcon.setIcon(bothalign);
        bothalignIcon.setMargin(new Insets(0, 0, 0, 0));
        rightalignIcon = new JToggleButton();
        Icon rightalign = new ImageIcon(getClass().getResource("res/justifyright.png"));
        rightalignIcon.setIcon(rightalign);
        rightalignIcon.setMargin(new Insets(0, 0, 0, 0));
        centeralignIcon = new JToggleButton();
        Icon centeralign = new ImageIcon(getClass().getResource("res/justifycenter.png"));
        centeralignIcon.setIcon(centeralign);
        centeralignIcon.setMargin(new Insets(0, 0, 0, 0));
        boldIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    				MutableAttributeSet attr = new SimpleAttributeSet();
    		        StyleConstants.setBold(attr, boldIcon.isSelected());
    		        owner.setAttributeSet(attr);
    		}
    	});
        owner.italicIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			MutableAttributeSet attr = new SimpleAttributeSet();
    	        StyleConstants.setItalic(attr, owner.italicIcon.isSelected());
    	        owner.setAttributeSet(attr);    					
    		}
    	});
        owner.underlineIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			MutableAttributeSet attr = new SimpleAttributeSet();
    	        StyleConstants.setUnderline(attr, owner.underlineIcon.isSelected());
    	        owner.setAttributeSet(attr);  
    		}
    	});
    	colorAction = new AbstractAction("someActionCommand", colorIcon) {
            @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // do something.
    	    	 Color jColor = selectColor;
    	         // open color dialog and select Color
    	         if ((jColor = JColorChooser.showDialog(null, "색상 선택"
    	         		+ "", jColor)) != null) {
    	             selectColor = jColor;
    	             // set text color
    	             SimpleAttributeSet attr = new SimpleAttributeSet();
    	             StyleConstants.setForeground(attr, jColor);
    	             owner.setAttributeSet(attr);
    	         }
    	    }
    	};
    	ImageIcon superscriptIcon = new ImageIcon(getClass().getResource("res/superscript.png"));
    	JButton superscriptbutton = new JButton(superscriptIcon);
    	superscriptbutton.setToolTipText("윗첨자");
    	superscriptbutton.addActionListener(new AbstractAction("someActionCommand", superscriptIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
        
    	     }
        });
    	tb2.add(superscriptbutton);
    	ImageIcon subscriptIcon = new ImageIcon(getClass().getResource("res/subscript.png"));
    	JButton subscriptbutton = new JButton(subscriptIcon);
    	subscriptbutton.setToolTipText("아래첨자");
    	subscriptbutton.addActionListener(new AbstractAction("someActionCommand", subscriptIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
        
    	     }
        });
    	tb2.add(subscriptbutton);
    	// color button size
        colorButton1.setPreferredSize(new Dimension(40, 21));
        colorButton1.setLocation(new Point(70, 50));
        //colorButton1.setToolTipText("아래첨자");
        colorButton1.addMouseListener(new MouseListener(){
    		//@SuppressWarnings("deprecation")
			@Override
    		public void mouseClicked(MouseEvent evt) {
    			// TODO Auto-generated method stub
				 for (Window w : JDialog.getWindows()) {
			            //
			            if (w.getClass().toString().contains("TablePaletteDialog"))
			                  	w.dispose();
				 }
    			//
    			final ColorButton callingButton = (ColorButton)evt.getSource();
    	        Point p = new Point(callingButton.getX(), callingButton.getY() + callingButton.getHeight());
    	        SwingUtilities.convertPoint(callingButton, p, tb2);
    	        SwingUtilities.convertPointToScreen(p,  tb2);
   		        final ColorPaletteDialog clDlg = new ColorPaletteDialog((int)p.getX(), (int)p.getY());
   		        clDlg.setUndecorated(true);
   		        clDlg.setVisible(true);
   		        clDlg.addComponentListener(new ComponentListener() {
				    public void componentHidden(ComponentEvent e)
				    {
				    	
				    	callingButton.centerColor = clDlg.selectColor;
   		            	callingButton.invalidate();
   		            	callingButton.repaint();
   		            	//
   		            	//SimpleAttributeSet attr = new SimpleAttributeSet();
                        //StyleConstants.setForeground(attr, clDlg.selectColor);
    		            //setAttributeSet(attr);
    		            clDlg.dispose();
				    }

					@Override
					public void componentMoved(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					public void componentResized(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void componentShown(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
   		         });
   		        //
    		    owner.invalidate();
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
    		public void mousePressed(MouseEvent arg0) {
    			// TODO Auto-generated method stub
    		}
    		@Override
    		public void mouseReleased(MouseEvent arg0) {
    			// TODO Auto-generated method stub
    		}
        });
        tb2.add(colorButton1);
        leftalignIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (leftalignIcon.isSelected()) {
    				MutableAttributeSet attr = new SimpleAttributeSet();
    		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
    		        int xStart = owner.m_monitor.getSelectionStart();
    	            int xFinish = owner.m_monitor.getSelectionEnd();
    	            CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
    	            doc.setParagraphAttributes(xStart, xFinish-xStart, attr, false);
    			}
    		}
    	});
        rightalignIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (rightalignIcon.isSelected()) {
    				MutableAttributeSet attr = new SimpleAttributeSet();
    		        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
    		        int xStart = owner.m_monitor.getSelectionStart();
    	            int xFinish = owner.m_monitor.getSelectionEnd();
    	            CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
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
    		        int xStart = owner.m_monitor.getSelectionStart();
    	            int xFinish = owner.m_monitor.getSelectionEnd();
    	            CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
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
    		        int xStart = owner.m_monitor.getSelectionStart();
    	            int xFinish = owner.m_monitor.getSelectionEnd();
    	            CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
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
        tb.add(new JSeparator(SwingConstants.VERTICAL));
        /*
        tableIcon = new ImageIcon(getClass().getResource("res/table.png"));
        tablebutton = new JButton(tableIcon);
        tablebutton.setToolTipText("표");
        tablebutton.addActionListener(new AbstractAction("someActionCommand", tableIcon) {
    	     @Override
    	    public void actionPerformed(ActionEvent e) {
            	 //
    	    	 final TableSetupDialog tsd = new TableSetupDialog(owner.m_monitor);
    	    	 tsd.addComponentListener(new ComponentListener() {
					@Override
					public void componentHidden(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
						//insert table
						int st = doc.getLength();
						rowheights = new int[tsd.m_rowCount];
				    	colwidths = new int[tsd.m_colCount];
				    	for (int i=0; i<tsd.m_rowCount; i++){
				    		rowheights[i] = 200/tsd.m_rowCount;
				    		colwidths[i] = 500/tsd.m_colCount;
				    	}
				    	
				    	owner.m_monitor.setText("\r\n");
				    	
				    	doc.insertTable(st+1, rowheights, colwidths);
				    	
				        tsd.dispose();
						
					}

					@Override
					public void componentMoved(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void componentResized(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void componentShown(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
    	    		
    	     	});
    	    	 
    	    }
    	});
    	
        tb.add(tablebutton);
        */
        tableButton1 = new TableButton();
                
        tableButton1.setPreferredSize(new Dimension(35, 21));
        tableButton1.setLocation(new Point(70, 50));
        tableButton1.addMouseListener(new MouseListener(){
    		@Override
    		public void mouseClicked(MouseEvent evt) {
    			for (Window w : JDialog.getWindows()) {
		            //
		            if (w.getClass().toString().contains("ColorPaletteDialog"))
		                  	w.dispose();
			 }
    			final TableButton selectButton = (TableButton)evt.getSource();
    			Point p = new Point(selectButton.getX(), selectButton.getY() + selectButton.getHeight());
    	        SwingUtilities.convertPoint(selectButton, p, tb);
    	        SwingUtilities.convertPointToScreen(p,  tb);
    	        final TablePaletteDialog tlDlg = new TablePaletteDialog(owner, (int)p.getX(), (int)p.getY());
   		        tlDlg.setUndecorated(true);
   		        tlDlg.setVisible(true);
   		        tlDlg.addComponentListener(new ComponentListener() {
				    public void componentHidden(ComponentEvent e)
				    {
				    	//
		            	tlDlg.dispose();
				    }

					@Override
					public void componentMoved(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
						
					}
					public void componentResized(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void componentShown(ComponentEvent arg0) {
						// TODO Auto-generated method stub
						
					}
		         });
		        
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
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        tb.add(tableButton1);
        
        //
        imageIcon = new ImageIcon(getClass().getResource("res/image.png"));
        imageAction = new AbstractAction("someActionCommand", imageIcon) {
       	    @Override
     	    public void actionPerformed(ActionEvent e) {
     	        // do something.
       	    	 owner.loadImage();
     	    }
     	};
        tb.add(imageAction);
        // style combo box
        tb2.addSeparator();
     	owner.m_cbStyles = new JComboBox();
     	owner.m_cbStyles.setMaximumSize(owner.m_cbStyles.getPreferredSize());
     	owner.m_cbStyles.setEditable(true);
     	owner.m_cbStyles.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			// TODO Auto-generated method stub
    			if (m_skipUpdate || owner.m_cbStyles.getItemCount()==0)
    		          return;
    		        styleName = (String)owner.m_cbStyles.getSelectedItem();
    		        int index = owner.m_cbStyles.getSelectedIndex();
    		        int stylePos = owner.m_monitor.getCaretPosition();
    		        // New name entered
    		        if (index == -1) {
    		          owner.m_cbStyles.addItem(styleName);
    		          CustomDocument doc = (CustomDocument) owner.m_monitor.getDocument();
    		          Style style = doc.addStyle(styleName, null);
    		          AttributeSet a = doc.getCharacterElement(stylePos).
    		          getAttributes();
    		          style.addAttributes(a);
    		          return;
    		        }
    		}
        });
        tb2.add(owner.m_cbStyles);
        //tb2.setAlwaysOnTop(false);
        JPanel toolbarwrap = new JPanel();
    	toolbarwrap.setLayout(new BoxLayout(toolbarwrap, BoxLayout.PAGE_AXIS));
    	toolbarwrap.add(tb);
    	toolbarwrap.add(tb2);
    	//
    	owner.getContentPane().add(toolbarwrap, BorderLayout.NORTH);
    	
   	}

	public void showAttribute(int p){
		  owner.f_skipUpdate = true;
	      //get attributes
	      AttributeSet currentattr = new SimpleAttributeSet();
	      //AttributeSet a = w_doc.getCharacterElement(p).getAttributes();
	      StyledDocument w_doc = (StyledDocument) owner.m_monitor.getDocument();
	      
	      currentattr = w_doc.getCharacterElement(p).getAttributes();
	      
	      String font_family = StyleConstants.getFontFamily(currentattr);
	      
	      int font_num = Arrays.asList(fontName).indexOf(font_family);
	      //
	      if (font_family !=null) jcbFont.setSelectedIndex(font_num);
	      //if (font_family !=null) jcbFont.setSelectedItem(font_family);
	      Color font_color = StyleConstants.getForeground(currentattr);
	      if (font_color != null) owner.colorButton1.setSelectColor(font_color);
	      //
	      String font_size = Integer.toString(StyleConstants.getFontSize(currentattr));
	      //System.out.println(font_size);
	      if (font_size != null) jcbSelectSize.setSelectedItem(font_size);
	      
	      String line_spacing = Float.toString(StyleConstants.getLineSpacing(currentattr));
	      if (line_spacing != null) jcbSelectSpace.setSelectedItem(line_spacing);
	      
	      Boolean isBold = StyleConstants.isBold(currentattr);
	      if (isBold) boldIcon.setSelected(true);
	  		else boldIcon.setSelected(false);    
	      Boolean isItalic = StyleConstants.isItalic(currentattr);
	      if (isItalic) owner.italicIcon.setSelected(true);
			else owner.italicIcon.setSelected(false);
	      Boolean isUnderline = StyleConstants.isUnderline(currentattr);
	      if (isUnderline) owner.underlineIcon.setSelected(true);
			else owner.underlineIcon.setSelected(false);
	      //
	      int alignment = StyleConstants.getAlignment(currentattr);
	      //	    	        
	      if (alignment == StyleConstants.ALIGN_JUSTIFIED) bothalignIcon.setSelected(true);
	      	else bothalignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_CENTER) centeralignIcon.setSelected(true);
	      	else centeralignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_RIGHT) rightalignIcon.setSelected(true);
	      	else rightalignIcon.setSelected(false);
	      if (alignment == StyleConstants.ALIGN_LEFT) leftalignIcon.setSelected(true);
	      	else leftalignIcon.setSelected(false);
	      //
	      //CustomDocument doc = (CustomDocument)owner.m_monitor.getDocument();
	      //Style style = doc.getLogicalStyle(owner.stylePos);
	      //
	      owner.f_skipUpdate = false;
	      
	      //
		}


}
