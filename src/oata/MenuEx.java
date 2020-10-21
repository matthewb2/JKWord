package oata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.inet.jortho.SpellChecker;

public class MenuEx {
	NeoHaneol owner;
	JEditorPane pane;
	//
	MenuEx(NeoHaneol owner){
		this.owner = owner;
		createMenu();
	}
	
	public void createMenu(){
		JMenuItem   new_blank, open, exit, close, find, replace, about, random, editpaper, printPreview; 
      	JMenuItem   deletefile, resize, save, saveas, print;
        JMenuItem   cut_text, paste_text, fullscreen, delete, previous, next;
      	JMenuBar menuBar = new JMenuBar();
      	JMenu menu1    = new JMenu("파일(F)");
        menu1.setMnemonic(KeyEvent.VK_F);
        ImageIcon icon_new = new ImageIcon(getClass().getResource("res/new.png"));
        new_blank     = new JMenuItem("새문서(N)", icon_new);
        new_blank.setMnemonic(KeyEvent.VK_N);
        //
        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask());
        //set the accelerator
        new_blank.setAccelerator(ctrlN);
        new_blank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	try {
					new NeoHaneol();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        menu1.add(new_blank);
        ImageIcon icon_open = new ImageIcon(getClass().getResource("res/folder.png"));
        open     = new JMenuItem("열기(O)", icon_open);
        open.setMnemonic(KeyEvent.VK_O);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask());
        //set the accelerator
        open.setAccelerator(ctrlO);
        open.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  try {
					owner.openEx();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}                        	    
              }
        });
        menu1.add(open);
        //
        ImageIcon icon_save = new ImageIcon(getClass().getResource("res/save.png"));
        save     = new JMenuItem("저장(S)", icon_save);
        save.setMnemonic(KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            		//save
          	    try {
						owner.save(owner.currentFile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
              }
        });
        menu1.add(save);
        ImageIcon icon_saveas = new ImageIcon(getClass().getResource("res/save.png"));
        saveas     = new JMenuItem("다른 이름으로 저장(A)", icon_saveas);
        saveas.setMnemonic(KeyEvent.VK_A);
        saveas.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  //
                  JFileChooser chooser = new JFileChooser();
            	  chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            	  chooser.setSelectedFile(new File("이름 없는 문서.hdf"));
            	  chooser.setFileFilter(new FileNameExtensionFilter("Haneol Document Format","hdf"));
                  int returnVal = chooser.showSaveDialog(null);
                  if (returnVal != chooser.APPROVE_OPTION) return;
                  if(returnVal == 0) {
                      File file = chooser.getSelectedFile();
                 	  	//save
                      try {
                    	    	owner.currentFile = file;
                    	    	owner.save(file);
					  } catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
					  } catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
				      }
                  }
              }
          });
          menu1.add(saveas);
          //    
          editpaper = new JMenuItem("편집용지(J)");
          editpaper.setMnemonic(KeyEvent.VK_J);
          KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
          //set the accelerator
          editpaper.setAccelerator(f7);
          editpaper.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  owner.show_editPaper();
            	  
              }
          });
          menu1.add(editpaper);
          //
          ImageIcon icon_printPreview = new ImageIcon(getClass().getResource("res/preview-rtl.png"));
          printPreview = new JMenuItem("미리보기(V)", icon_printPreview);
          printPreview.setMnemonic(KeyEvent.VK_V);
          printPreview.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  Thread runner = new Thread() {
			          public void run() {
			        	  owner.setCursor(Cursor.getPredefinedCursor(
			              Cursor.WAIT_CURSOR));
			            new PrintPreview(owner, 800, 1131);
			            owner.setCursor(Cursor.getPredefinedCursor(
			              Cursor.DEFAULT_CURSOR));
			          }
			        };
			        runner.start();
              }
          });
          menu1.add(printPreview);
          //
          ImageIcon icon_print = new ImageIcon(getClass().getResource("res/print.png"));
          print = new JMenuItem("인쇄(P)", icon_print);
          print.setMnemonic(KeyEvent.VK_P);
          print.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  final PrintDialog pd = new PrintDialog(owner.m_monitor);
            	  pd.addComponentListener(new ComponentListener() {
 					    public void componentHidden(ComponentEvent e)
 					    {
 					    	pd.dispose();
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
          menu1.add(print);
          //
          close = new JMenuItem("문서 닫기(C)");
          //
          close.setMnemonic('C');
          close.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
                      System.exit(0);
             }
          });
          menu1.add(close);
          ImageIcon icon_exit = new ImageIcon(getClass().getResource("res/close.png"));
          exit     = new JMenuItem("종료(X)", icon_exit);
          //
          exit.setMnemonic('X');
          exit.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
                      //System.exit(0);
            	  	  //owner.handleClosing();
             }
          });
          menu1.add(exit);
          menuBar.add(menu1);
          JMenu menu2    = new JMenu("편집(E)");
          menu2.setMnemonic(KeyEvent.VK_E);
          cut_text     = new JMenuItem("잘라내기(T)");
          cut_text.setMnemonic(KeyEvent.VK_T);
          menu2.add(cut_text);
          JMenuItem copy_text     = new JMenuItem("복사(C)");
          copy_text.setMnemonic(KeyEvent.VK_C);
          menu2.add(copy_text);
          paste_text     = new JMenuItem("붙여널기(P)");
          paste_text.setMnemonic(KeyEvent.VK_P);
          menu2.add(paste_text);
          find     = new JMenuItem("찾기(F)");
          find.setMnemonic(KeyEvent.VK_F);
          KeyStroke ctrlf = KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask());
          find.setAccelerator(ctrlf);
          find.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
                  if (owner.m_findDialog==null)
                	  owner.m_findDialog = new FindDialog(owner, 0);
                  else
                	  owner.m_findDialog.setSelectedIndex(0);
                  Dimension d1 = owner.m_findDialog.getSize();
                  Dimension d2 = owner.getSize();
                  int x = Math.max((d2.width-d1.width)/2, 0);
                  int y = Math.max((d2.height-d1.height)/2, 0);
                  owner.m_findDialog.setBounds(x + owner.getX(),
                    y + owner.getY(), d1.width, d1.height);
                  owner.m_findDialog.setVisible(true);            
              }
          });
          menu2.add(find);
          menuBar.add(menu2);
          //
          JMenu menu3    = new JMenu("보기(V)");
          menu3.setMnemonic(KeyEvent.VK_V);
          final JCheckBoxMenuItem chbShow = new JCheckBoxMenuItem("문단부호");
          chbShow.setSelected(true);
          chbShow.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
              	 if (chbShow.isSelected()) {
                       Document doc = owner.m_monitor.getDocument();
                       doc.putProperty("show paragraphs","");
                   }
                   else {
                       Document doc = owner.m_monitor.getDocument();
                       doc.putProperty("show paragraphs", null);
                   }
              	   owner.m_monitor.repaint();
              }
          });
          menu3.add(chbShow);
          
          //
          final JCheckBoxMenuItem toolbars = new JCheckBoxMenuItem("도구모음");
          toolbars.setSelected(true);
          toolbars.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
              	 if (toolbars.isSelected()) {
              		owner.tb.setVisible(true);
              		owner.tb2.setVisible(true);
                   }
                   else {
                	   owner.tb.setVisible(false);
                	   owner.tb2.setVisible(false);
                   }
              	 owner.m_monitor.repaint();
              }
          });
          menu3.add(toolbars);
          //
          menuBar.add(menu3);
          final JCheckBoxMenuItem statusmode = new JCheckBoxMenuItem("상태표시줄(B)");
          statusmode.setMnemonic(KeyEvent.VK_B);
          statusmode.setSelected(true);
          statusmode.addActionListener(new ActionListener() {
      	     @Override
      	     public void actionPerformed(ActionEvent e) {
      	    	if (statusmode.isSelected()) {
      	    		owner.statusp.setVisible(true);
      	    	} else {
      	    		owner.statusp.setVisible(false);
      	    	}
      	    	
      	     }
          });
          menu3.add(statusmode);
          
          final JCheckBoxMenuItem fullscreenmode = new JCheckBoxMenuItem("전체화면(U)");
          fullscreenmode.setMnemonic(KeyEvent.VK_U);
          fullscreenmode.setSelected(false);
          fullscreenmode.addActionListener(new ActionListener() {
     	     @Override
     	     public void actionPerformed(ActionEvent e) {
     	    	if (fullscreenmode.isSelected()) {
     	    	    // full screen action
     	    	    owner.dispose();
     	    	    owner.setExtendedState( owner.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		    	    owner.setUndecorated (true);
		    	    owner.setResizable(false);
		    	    owner.setVisible (true);
     	    	} else{
     	    		owner.dispose();
     	    		owner.setUndecorated (false);
     	    		owner.setResizable(true);
     	    		owner.setVisible (true);
     	    	}
     	     }
          });
          menu3.add(fullscreenmode);
          
          ImageIcon icon_zoominout = new ImageIcon(getClass().getResource("res/zoominout.png"));
          JMenuItem zoominout     = new JMenuItem("확대/축소(Z)", icon_zoominout);
          zoominout.setMnemonic(KeyEvent.VK_Z);
          zoominout.addActionListener(new ActionListener() {
     	     @Override
     	     public void actionPerformed(ActionEvent e) {
   	                // do something.
     	            
     	  }
          });
          menu3.add(zoominout);
          
          JMenu menu4 = new JMenu("입력(D)");
          menu4.setMnemonic(KeyEvent.VK_D);
          JMenuItem table     = new JMenuItem("표(T)");
          table.setMnemonic(KeyEvent.VK_T);
          table.addActionListener(new ActionListener() {
        	     @Override
        	     public void actionPerformed(ActionEvent e) {
      	                // do something.
        	            
        	  }
      	  });
          menu4.add(table);
          JMenuItem picture     = new JMenuItem("그림(P)");
          picture.setMnemonic(KeyEvent.VK_T);
          picture.addActionListener(new ActionListener() {
	     	     @Override
	     	     public void actionPerformed(ActionEvent e) {
	     	    	owner.loadImage(); 
	         	  }
          });
          menu4.add(picture);
          menuBar.add(menu4);
          JMenu menu5 = new JMenu("서식(T)");
          JMenuItem letterform = new JMenuItem("글자모양(L)");
          letterform.setMnemonic(KeyEvent.VK_L);
          KeyStroke altl = KeyStroke.getKeyStroke(KeyEvent.VK_L, java.awt.event.InputEvent.ALT_DOWN_MASK);
          letterform.setAccelerator(altl);
          letterform.addActionListener(new ActionListener() {
        	  public void actionPerformed(ActionEvent ev) {
        		    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        	    	String[] fontNames = gEnv.getAvailableFontFamilyNames();
        	    	String[] fontSize =  { "8", "9", "10", "11", "12", "13", "14", "15", "16", "18", "22", "26", "30", "34", "36", "40", "44", "48", "52" };
        	    	//
        	    	owner.m_fontDialog.setVisible(true);
        	  }
          });
          menu5.setMnemonic(KeyEvent.VK_T);
          menu5.add(letterform);
          //
          JMenu menu6    = new JMenu("쪽(W)");
          menu6.setMnemonic(KeyEvent.VK_W);
          JMenuItem numbering     = new JMenuItem("쪽번호매기기");
          numbering.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  new NumberingDialog();
              }
          });
          menu6.add(numbering);
          //
          JMenu menu9    = new JMenu("표(A)");
          menu9.setMnemonic(KeyEvent.VK_A);
          JMenuItem tableProperty     = new JMenuItem("표속성(P)");
          //
          tableProperty.setMnemonic(KeyEvent.VK_P);
          tableProperty.addActionListener(new ActionListener() {
	     	     @Override
	     	     public void actionPerformed(ActionEvent e) {
	     	          new BorderControl(); 
	         	  }
          });
          menu9.add(tableProperty);
          JMenu menu7    = new JMenu("도구(K)");
          menu7.setMnemonic(KeyEvent.VK_K);
          //
          JMenuItem pref     = new JMenuItem("옵션(O)");
          pref.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
            	  	final PreferenceDialog pfd = new PreferenceDialog(owner);
            	  	//
    	    	    pfd.addComponentListener(new ComponentListener() {
					    public void componentHidden(ComponentEvent e)
					    {
					    	try {
								//
					    		UIManager.setLookAndFeel(pfd.getlf());
					    		SwingUtilities.updateComponentTreeUI(owner);
					    		owner.pack();
							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
									| UnsupportedLookAndFeelException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    	pfd.dispose();
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
          final JCheckBoxMenuItem spellcheck = new JCheckBoxMenuItem("맞춤법 검사(S)");
          spellcheck.setMnemonic(KeyEvent.VK_S);
          spellcheck.setSelected(false);
          spellcheck.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ev) {
              	 if (spellcheck.isSelected()) {
              		 SpellChecker.goAutoSpell(owner.m_monitor);
              	 }
                 else {
                       Document doc = owner.m_monitor.getDocument();
                       doc.putProperty("show spellcheck", null);
                       SpellChecker.unregister(owner.m_monitor);
                       //
                 }
              	owner.m_monitor.repaint();
              }
          });
          menu7.add(spellcheck);
          menu7.add(pref);
          JMenu menu8    = new JMenu("도움말(H)");
          menu8.setMnemonic(KeyEvent.VK_H);
          about     = new JMenuItem("정보(A)");
          about.setMnemonic(KeyEvent.VK_A);
          about     = new JMenuItem("정보(A)");
          about.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                //
              	AboutDialog ab = new AboutDialog();
              	ab.version = "버전 "+owner.version[0]+"."+owner.version[1]+"."+owner.version[2]+" 엠케이솔루션 제공";
              	ab.showDialog();
              	ab.setVisible(true);
              }
          });
          menu8.add(about);
          menuBar.add(menu4);
          menuBar.add(menu5);
          menuBar.add(menu9);
          menuBar.add(menu7);
          menuBar.add(menu6);
          menuBar.add(menu8);
          //
          owner.setJMenuBar(menuBar);	
        	  
	  }
	  
}
