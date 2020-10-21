package oata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.*;
import javax.swing.text.AbstractDocument.BranchElement;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.Timer;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

import oata.TableView;
import oata.CustomEditorKit.SectionView;

public class NeoHaneol extends JFrame implements ComponentListener, Printable{
    protected JLabel label;
    protected String defaultFont;
    protected static JTextPane m_monitor;
    protected CustomDocument doc;
    protected PrintView m_printView;
    static int[] version= {0, 5, 14};
    //
    protected JPanel noWrapPanel;
    protected JPanel leftspacecolumn;
    protected JScrollPane jsp;
    protected int leftSpace;
    protected int PageMarginUp, PageMarginLeft;
    protected int PageInletUp, PageInletLeft=37;
    protected JLabel rowtextlabel, columntextlabel, pagetextlabel, typemodelabel;
    
    protected int stylePos, pos;
    protected int multicol=1;
    protected JPanel statusp= new JPanel();
    protected JLabel isLabel = new JLabel();
    //
    protected Double viewScale;
    
    protected JToolBar tb, tb2;
    
    
    protected JComboBox zoomCombo;
    protected int height_max, width_max;
    protected JToggleButton leftalignIcon, bothalignIcon, rightalignIcon, centeralignIcon;
    protected JToggleButton italicIcon, boldIcon, underlineIcon;
    protected static JComboBox jcbSelectSize;
	protected static JComboBox jcbSelectSpace;
	protected static JComboBox jcbFont;
	protected Color selectColor;
    protected TableButton tableButton1;
    protected ColorButton colorButton1 = new ColorButton();
    protected JComboBox m_cbStyles;
    public boolean m_skipUpdate;
    protected int m_xStart = -1;
    protected int m_xFinish = -1;
    protected String styleName;
    //
	int px_r_indent, px_l_indent, dx_r=0, dx_l=0;
    protected static int PAGE_INSET=5;
    private int lMargin, rMargin;        
    protected static int rmargin=PAGE_INSET+50, lumargin, lmargin=PAGE_INSET+50;
    protected float dotsPermm=3.78f;
    protected Rectangle me;
    protected Rectangle drawZone;
    protected Rectangle2D.Float workArea;
    private JLabel columnheader;
    protected FontDialog m_fontDialog;
	protected ParagraphDialog m_paragraphDialog;
	protected FindDialog m_findDialog;
	
	
    protected File currentFile;
    protected ArrayList<ImageBucket> imgBucket;
    public JTextPane getTextPane() { return m_monitor; }
    public String newString, oldString="";
    public Boolean isChanged;
    public int lastNumber;
    
    
    protected int st, colNumber, rowNumber;
    protected Rectangle taborigin;
    
	protected int colCount = 3;
	protected int rowCount = 3;
	
	protected int[] colwidths, rowheights;
	
	public static final String PARAM_CELL_WIDTH = "cell-width";
	public static final String PARAM_ROW_HEIGHT = "row-height";
	
	protected String[] celltext = new String[colCount*rowCount];
	
	
	protected Boolean isF5=false;
	
	protected Point clickOffset;
	protected int oldw, oldh;
	protected Boolean isHC=false, isVC=false;
	
	protected ScheduledExecutorService service;
	protected Runnable runnable;
	
    protected SpinSlider spinSlider = new SpinSlider(NeoHaneol.this);
    protected void showStyles() {
	    m_skipUpdate = true;
	    if (m_cbStyles.getItemCount() > 0){
	      m_cbStyles.removeAllItems();
	    }
	    CustomDocument doc = (CustomDocument) m_monitor.getDocument();
	    Enumeration en = doc.getStyleNames();
	    while (en.hasMoreElements()) {
	      String str = en.nextElement().toString();
	      m_cbStyles.addItem(str);
	    }
	}
 
    public void setSelection(int xStart, int xFinish, boolean moveUp) {
      if (moveUp) {
        m_monitor.setCaretPosition(xFinish);
        m_monitor.moveCaretPosition(xStart);
      }
      else
        m_monitor.select(xStart, xFinish);
        m_xStart = m_monitor.getSelectionStart();
        m_xFinish = m_monitor.getSelectionEnd();
    }
    
    public void checkDiff() throws BadLocationException{
    	CustomDocument doc = (CustomDocument)m_monitor.getDocument();
    	newString =  doc.getText(0, doc.getLength());
        if (oldString.equals(newString) == true){
    		isChanged = false;
    	} else {
    		oldString=doc.getText(0, doc.getLength());
    		isChanged = true;
    	}
    }
        
    private static Image fitimage(Image img , int w , int h){
	    BufferedImage resizedimage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = resizedimage.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(img, 0, 0,w,h,null);
	    g2.dispose();
	    return resizedimage;
	}
    
    public int getLastPicNum(){
    	String str1 = m_monitor.getText();
    	Pattern pat = Pattern.compile("(img:)\\d");
    	Matcher mat = pat.matcher(str1);
    	lastNumber=0;
    	while (mat.find()){
    	    int getNumber = Integer.parseInt(mat.group().replace("img:", ""));
    	    if (getNumber > lastNumber) lastNumber = getNumber;
    	}
    	return lastNumber;
    }
    
	public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
	
	public static Icon extractImageNumber(int pos, int end) throws BadLocationException{
		//extract image number
		DefaultStyledDocument dDoc = (DefaultStyledDocument)m_monitor.getStyledDocument();
		final String str = m_monitor.getText(pos, end).replaceAll("[^0-9]", "");
		Enumeration list = dDoc.getStyleNames();
	    Icon ai = null;
	    Style style2=null;
		while (list.hasMoreElements()) {
			String styleName = (String)list.nextElement();
		    style2 = dDoc.getStyle(styleName);
		    ai = StyleConstants.getIcon(style2);
			if (styleName.contains("Icon"+str)) break;
		}
		return ai;
	}
	
	public static String extractStyleName(int pos, int end) throws BadLocationException{
		//extract image number
		DefaultStyledDocument dDoc = (DefaultStyledDocument)m_monitor.getStyledDocument();
		final String str = m_monitor.getText(pos, end).replaceAll("[^0-9]", "");
		Enumeration list = dDoc.getStyleNames();
	    Icon ai = null;
	    Style style2=null;
	    String styleName=null;
		while (list.hasMoreElements()) {
			styleName = (String)list.nextElement();
		    style2 = dDoc.getStyle(styleName);
		    ai = StyleConstants.getIcon(style2);
			if (styleName.contains("Icon"+str)) break;
		}
		return styleName;
	}
	
	public static void imageTransferText(int pos, Point pt){
		DefaultStyledDocument dDoc = (DefaultStyledDocument)m_monitor.getStyledDocument();
		//
		if (pos <= dDoc.getLength()){
			try {
				int end = dDoc.getLength()-pos;
				if (end>7) end = 7;
				if(m_monitor.getText(pos, end).contains("\\img:")){
					final Icon ai = extractImageNumber(pos, end);
					final String styleName = extractStyleName(pos, end); 
					final Image image = iconToImage(ai);
				    final JLabel label6 = new JLabel(ai){
				        public void paintComponent(Graphics g)
				        {
				            super.paintComponent(g);
				            g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null), null);
				        }
				    };
				    final Resizable rs = new Resizable(label6);
				    rs.addMouseListener(new MouseListener(){
						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							//perform at double-clicking
							if (e.getClickCount() == 2 && !e.isConsumed()) {
								 Point pt =new Point(rs.getLocation().x+rs.getWidth()/2,
								 rs.getLocation().y+rs.getHeight()/2 );
							     int pos = m_monitor.viewToModel(pt);
							     int end = pos+6;
							     DefaultStyledDocument dDoc = (DefaultStyledDocument)m_monitor.getStyledDocument();
							     Enumeration list = dDoc.getStyleNames();
								 Image image = iconToImage(ai);
							     //resize image
								 Style style2 = m_monitor.addStyle(styleName, null);
								 Image resizeImage = image.getScaledInstance(rs.getWidth(), rs.getHeight(), Image.SCALE_SMOOTH);
								 StyleConstants.setIcon(style2, new ImageIcon(resizeImage));
								 try {
									m_monitor.getDocument().insertString(pos, "\\img:"+styleName.substring(4, 5),style2);
									
								 } catch (BadLocationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								 rs.dispose();
							}
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
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
				    });
				    rs.setBounds(pt.x, pt.y, image.getWidth(null), image.getHeight(null));
				    m_monitor.add(rs);
				    //
				    m_monitor.select(pos, pos+6);
				    m_monitor.replaceSelection("");
				    rs.requestFocus();
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
    public void updateVar(){
    	  Document doc = m_monitor.getDocument();
    	  int page_width = (int) doc.getProperty("PAGE_WIDTH");
    	  page_width -= (PAGE_INSET+5);
    	  double scale = (double) doc.getProperty("ZOOM_FACTOR");
	  	  me =new Rectangle(0, 0, (int)(page_width*scale), 30);
	  	  drawZone = new Rectangle(10, 3, me.width+5-PAGE_INSET, 14);
	  	  rMargin = (int)(rmargin*scale);
	  	  lMargin = (int)(lmargin*scale);
    }
    
    byte[] saveImages(ArrayList<ImageBucket> imgBucket, byte[] fileContent) throws IOException{
  	  	byte[] imgtag = "[[img]]".getBytes();
  	    byte[] imgsapar = ":app".getBytes();
  	    int totallength =0;
  	    int finallength =0;
  	    for(int i = 0; i < imgBucket.size(); i++){
  	    	//
  	    	BufferedImage bImage = ImageIO.read(new File(imgBucket.get(i).file));
  	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
  	        ImageIO.write(bImage, imgBucket.get(i).type, bos );
  	        imgBucket.get(i).data = bos.toByteArray();
  	    	//
  	        finallength  += imgBucket.get(i).data.length+imgsapar.length;
  	    }
  	    byte[] finalout = new byte[fileContent.length + imgtag.length+finallength];
  	    //
  	    System.arraycopy(fileContent, 0, finalout, 0, fileContent.length);
  	    System.arraycopy(imgtag, 0, finalout, fileContent.length, imgtag.length);
  	    totallength = fileContent.length+imgtag.length;
  	    for(int i = 0; i < imgBucket.size(); i++){
  		    System.arraycopy(imgBucket.get(i).data, 0, finalout, totallength, imgBucket.get(i).data.length);
  		    totallength +=imgBucket.get(i).data.length;
  		    if (i !=imgBucket.size()-1){ System.arraycopy(imgsapar, 0, finalout, totallength, imgsapar.length);
  		    totallength +=imgsapar.length;
  		    }
  		    //
  	    }
  	    return finalout;
    }
    
    public static boolean isMatch(byte[] pattern, byte[] input, int pos) {
  	    for(int i=0; i< pattern.length; i++) {
  	        if(pattern[i] != input[pos+i]) return false;
  	    }
  	    return true;
  	}

  	public static List<byte[]> split(byte[] pattern, byte[] input) {
  	    List<byte[]> l = new LinkedList<byte[]>();
  	    int blockStart = 0;
  	    for(int i=0; i<input.length; i++) {
  	       if(isMatch(pattern,input,i)) {
  	          l.add(Arrays.copyOfRange(input, blockStart, i));
  	          blockStart = i+pattern.length;
  	          i = blockStart;
  	       }
  	    }
  	    l.add(Arrays.copyOfRange(input, blockStart, input.length ));
  	    return l;
  	}
  	
  	static void insertImage(JTextPane editor, List<byte[]> imgresult) throws IOException{
  		  for(int i = 0; i < imgresult.size(); i++){
  			    Style style1 = editor.addStyle("Icon"+i, null);
  			    InputStream in = new ByteArrayInputStream(imgresult.get(i));
  			    BufferedImage image = ImageIO.read(in);
  			    try{
  			    	StyleConstants.setIcon(style1, new ImageIcon(image));
  			    }catch(Exception e2){}
  			    StyledDocument doc = editor.getStyledDocument();
  			    String alltext = null;
  				try {
  					alltext = doc.getText(0, doc.getLength());
  					
  				} catch (BadLocationException e) {
  					e.printStackTrace();
  				}
  			    //
  				int stpos = alltext.indexOf("img:"+i);
  				try{
  					doc.remove(stpos-1,  6);
  				    doc.insertString(stpos-1, " img:"+i, style1);
  			    }catch (Exception e){}
  			}
  			//end of image   
  	}
  	
    public static void openFile(JTextPane hf, File file) throws UnsupportedEncodingException{
        byte[] bytesArray = new byte[(int) file.length()]; 
        FileInputStream fis = null;
        try {
			fis = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
        }
        try {
			fis.read(bytesArray);
        } catch (IOException e1) {
		// TODO Auto-generated catch block
    		e1.printStackTrace();
        } //read file into bytes[]
        try {
			fis.close();
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
        }
        byte[] imgtag = "[[img]]".getBytes();
        List<byte[]> result = split(imgtag, bytesArray);
        String converts = new String(result.get(0), "UTF-8");
        // 
        try {
          InputStream inputrtf = new ByteArrayInputStream(result.get(0));
          //
          hf.getEditorKit().read(inputrtf, hf.getDocument(), 0);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
          //
        } catch (BadLocationException e) {
        }
        //image part
        List<byte[]> imgresult = split(":app".getBytes(), result.get(1));
        //
        try {
			insertImage(hf, imgresult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    public void showAttribute(){
      //get attributes
      AttributeSet currentattr = new SimpleAttributeSet();
      currentattr = m_monitor.getCharacterAttributes();
      String font_family = StyleConstants.getFontFamily(currentattr);
      if (font_family !=null) jcbFont.setSelectedItem(font_family);
      Color font_color = StyleConstants.getForeground(currentattr);
      if (font_color != null) colorButton1.setSelectColor(font_color);
      //
      String font_size = Integer.toString(StyleConstants.getFontSize(currentattr));
      if (font_size != null) jcbSelectSize.setSelectedItem(font_size);
      String line_spacing = Float.toString(StyleConstants.getLineSpacing(currentattr));
      if (line_spacing != null) jcbSelectSpace.setSelectedItem(line_spacing);
      
      Boolean isBold = StyleConstants.isBold(currentattr);
      if (isBold) boldIcon.setSelected(true);
  		else boldIcon.setSelected(false);    
      Boolean isItalic = StyleConstants.isItalic(currentattr);
      if (isItalic) italicIcon.setSelected(true);
		else italicIcon.setSelected(false);
      Boolean isUnderline = StyleConstants.isUnderline(currentattr);
      if (isUnderline) underlineIcon.setSelected(true);
		else underlineIcon.setSelected(false);
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
      CustomDocument doc = (CustomDocument)m_monitor.getDocument();
      Style style = doc.getLogicalStyle(stylePos);
      //
      m_skipUpdate = false;
      //
	}
    
    void updateCellText() throws BadLocationException{
		Element root = m_monitor.getDocument().getDefaultRootElement();
    	ElementIterator it = new ElementIterator(m_monitor.getDocument());
    	Element elem = null;
    	BranchElement current = null;
    	while ( (elem = it.next()) != null )
        {
    		if (elem.getName().toString() == "table") {
    			current = (BranchElement) elem;
    			break;
    		}
        }
		int k=0;
		//
		for (int i=0; i<current.getElementCount(); i++){
			BranchElement child = (BranchElement) current.getElement(i);
			//
			for (int j=0; j<child.getElementCount(); j++){
				BranchElement chcld = (BranchElement) child.getElement(j);
				CustomDocument doc = (CustomDocument) m_monitor.getDocument();
				celltext[k] = doc.getText(chcld.getStartOffset(), chcld.getEndOffset()-chcld.getStartOffset());
				k++;
			}
		}
	}
    
    public Rectangle findTablePosition(JEditorPane src){
    	Element root = src.getDocument().getDefaultRootElement();
        //System.out.println(root.toString());
        BranchElement elem = (BranchElement) root.getElement(0);
        //System.out.println(elem.toString());
        
        BranchElement child = (BranchElement) elem.getElement(0);
        //System.out.println(child.getName().toString());
        //System.out.println(child.toString());
		//
		int start = child.getStartOffset();
		Rectangle offset = null;
		try {
			offset = src.modelToView(start);
			//System.out.println(offset);
			return offset;
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		return offset;
		
    }
    
        
    protected void setAttributeSet(AttributeSet attr) {
    	    int xStart = m_monitor.getSelectionStart();
    	    int xFinish = m_monitor.getSelectionEnd();
    	    if (!m_monitor.hasFocus()) {
    	    }
    	    if (xStart != xFinish) {
    	      CustomDocument doc = (CustomDocument) m_monitor.getDocument();
    	      doc.setCharacterAttributes(xStart, xFinish - xStart, 
    	        attr, false);
    	    } 
    	    else {}
    }
    
    public void openEx() throws BadLocationException{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("Haneol Document Format","hdf"));
		int returnVal = chooser.showOpenDialog(null);
	    if (returnVal != chooser.APPROVE_OPTION){
	  	  return;
	    } 
        //check current document
    	CustomDocument doc = (CustomDocument) m_monitor.getDocument();
    	if ( doc.getText(0,  doc.getLength()).length() == 0 ){
    	   File file = chooser.getSelectedFile();
		   //
		   try {
		  	  currentFile = file;
	      	  openFile(m_monitor, file);
		  	  setTitle(file.toString());
		   } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
		  	  	e.printStackTrace();
		   }
    	} else {
    		//new frame
    		NeoHaneol hf = new NeoHaneol();
            //read the hdf file
		    File file = chooser.getSelectedFile();
		    //
		    try {
		  	  currentFile = file;
	          //
	          openFile(hf.m_monitor, file);
		  	  hf.setTitle(file.toString());
		    } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
		  	  	e.printStackTrace();
		    } 
    	}
    }

        
    protected void loadImage(){
    	JFileChooser chooser = new JFileChooser();
  	  chooser.setDialogType(JFileChooser.OPEN_DIALOG);
  	  chooser.setFileFilter(new FileNameExtensionFilter(
  			    "Image files", ImageIO.getReaderFileSuffixes()));
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == chooser.APPROVE_OPTION)
        {
            //add new image
      	    //image position
            Style style2 = m_monitor.addStyle("Icon"+Integer.toString(getLastPicNum()+1), null);
            File file = chooser.getSelectedFile();
            //for an inner frame type
            CustomDocument doc = (CustomDocument) m_monitor.getDocument();
            final ImageIcon img = new ImageIcon(file.getAbsolutePath());
            StyleConstants.setIcon(style2, img);
        	//
            final Image image = iconToImage(img);
			final JLabel label6 = new JLabel(img){
				        public void paintComponent(Graphics g)
				        {
				            super.paintComponent(g);
				            g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(null), image.getHeight(null), null);
				        }
			};
			final Resizable rs = new Resizable(label6);	
			//
			Rectangle pt = null;
			try {
				pt = m_monitor.modelToView(m_monitor.getCaretPosition());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs.setBounds(pt.x, pt.y, image.getWidth(null), image.getHeight(null));
			m_monitor.add(rs);
			// 
	    	return;
        }
    }
    
    public void setPageVariable(){
    	//set page vaiables
         m_monitor.getDocument().putProperty("PAGE_WIDTH", new Integer(800));
         m_monitor.getDocument().putProperty("PAGE_MARGIN", new Insets(50, 50, 50, 50));
         m_monitor.getDocument().putProperty("PAGE_HEIGHT", new Integer(1131));
         m_monitor.getDocument().putProperty("ZOOM_FACTOR", new Double(1.0));
         m_monitor.getDocument().putProperty("show paragraphs","A");
         m_monitor.getDocument().putProperty("show textborder","A");
         m_monitor.getDocument().putProperty("PAGE_BORDER","A");
         m_monitor.getDocument().putProperty("show shadow", null);
         m_monitor.getDocument().putProperty("MODE_PRINTER", Boolean.FALSE);
    }

    protected void save(File file) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		BufferedOutputStream out = new BufferedOutputStream(new  FileOutputStream("c:\\tmp.rtf"));
	    try {
	      //
	      OutputStream outputStream = new FileOutputStream("c:\\tmp.rtf");
	      final StyledDocument doc = m_monitor.getStyledDocument();
	      m_monitor.getEditorKit().write(out, doc, 0, doc.getLength());
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      out.close();
	    }
	    File filetmp= new File("c:\\tmp.rtf");
	    //
	    byte[] fileContent = Files.readAllBytes(filetmp.toPath());
	    // save images to fileContent and produce output hdf           	    
	    try (FileOutputStream fos = new FileOutputStream(file)) {
	    	  fos.write(saveImages(imgBucket, fileContent));
	    }
	}
    void show_editPaper(){
    	final PageSetupDialog psd = new PageSetupDialog(NeoHaneol.this, "용지설정",true);
  	    psd.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
					 // TODO Auto-generated method stub
					 //
				     if (psd.m_margin != null){
				    	 
						 m_monitor.getDocument().putProperty("PAGE_MARGIN", 
									 new Insets(psd.m_margin.top, psd.m_margin.left, psd.m_margin.bottom, psd.m_margin.right));
						 m_monitor.repaint();
						 //
					 }
				     if (psd.pageGaroSero == 1){
				    	 int page_width = (int) m_monitor.getDocument().getProperty("PAGE_WIDTH");
				    	 int page_height = (int) m_monitor.getDocument().getProperty("PAGE_HEIGHT");
				    	
				    	 m_monitor.getDocument().putProperty("PAGE_WIDTH", page_height);
				    	 m_monitor.getDocument().putProperty("PAGE_HEIGHT", page_width);
				    	 m_monitor.repaint();
				    	 //update jscrollpane to adjust main text
				    	 try {
							adjustmainTextPane();
						 } catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						 }
				     }
					 psd.dispose();
					
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

    void createStausBar(){
      	statusp = new JPanel();
      	statusp.setPreferredSize(new Dimension(this.getWidth(), 30));
      	statusp.setLayout(new FlowLayout(FlowLayout.RIGHT));
      	statusp.setBorder(BorderFactory.createEmptyBorder(5 , 0 , 5 , 0));
      	int caretPos = m_monitor.getCaretPosition();
      	int colNum = caretPos;
      	int rowNum = caretPos;
      	//
      	statusp.setLayout(new BoxLayout(statusp, BoxLayout.X_AXIS));
      	statusp.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
      	//      	
      	pagetextlabel = new JLabel();
      	columntextlabel = new JLabel();
      	rowtextlabel = new JLabel();
      	typemodelabel = new JLabel();
      	JPanel rowtextPanel = new JPanel();
      	JPanel columntextPanel = new JPanel();
      	JPanel pagetextPanel = new JPanel();
      	JPanel typemodePanel = new JPanel();
      	
      	pagetextPanel.setBorder(BorderFactory.createTitledBorder(""));
      	pagetextPanel.setLayout(new GridLayout(1,1));
      	rowtextPanel.setBorder(BorderFactory.createTitledBorder(""));
      	rowtextPanel.setLayout(new GridLayout(1,1));
      	columntextPanel.setBorder(BorderFactory.createTitledBorder(""));
      	columntextPanel.setLayout(new GridLayout(1,1));
      	typemodePanel.setBorder(BorderFactory.createTitledBorder(""));
      	typemodePanel.setLayout(new GridLayout(1,1));
        rowtextPanel.add(rowtextlabel);
        columntextPanel.add(columntextlabel);
        pagetextPanel.add(pagetextlabel);
        typemodePanel.add(typemodelabel);
        statusp.add(pagetextPanel);
        statusp.add(rowtextPanel);
        statusp.add(columntextPanel);
        statusp.add(typemodePanel);
        statusp.add(spinSlider);
    	//
    	this.getContentPane().add(statusp, BorderLayout.SOUTH);
    }
   	
    private void DrawIndents(Graphics g, int leftSpace)
    {
        int lumargin = leftSpace+lMargin;
        Graphics2D g2d = (Graphics2D) g;
        // obtain the graphics context
        ImageIcon icon = new ImageIcon(getClass().getResource("res/l_indet_pos_upper.png"));
        Image l_indent_pos_upper = icon.getImage();
        //
        g2d.drawImage(l_indent_pos_upper, lumargin, 2, this);
        ImageIcon icon2 = new ImageIcon(getClass().getResource("res/l_indent_pos_lower.png"));
        Image l_indent_pos_lower = icon2.getImage();
        g2d.drawImage(l_indent_pos_lower, leftSpace+lMargin+dx_l, 12, this);
        //
        ImageIcon icon3 = new ImageIcon(getClass().getResource("res/r_indent_pos.png"));
        Image r_indent_pos = icon3.getImage();
        g2d.drawImage(r_indent_pos, leftSpace+me.width-(rMargin-dx_r), 14, this);
        //
    }
    
    private void DrawTextAndMarks(Graphics g, int leftSpace){
		CustomDocument doc = (CustomDocument) m_monitor.getDocument();
		double scale = (double) doc.getProperty("ZOOM_FACTOR");
		int points = (int)(drawZone.width / (3.78f*scale)) / 10;
		int spoints = (int)(drawZone.width / (dotsPermm*scale));
	    float range = (float)(5 * dotsPermm*scale);
	    float srange = (float)(5/4 * dotsPermm*scale);
	    int ls = leftSpace+PAGE_INSET;
	    Point sz = new Point(5,5);
	    //fill background
	    g.setColor(Color.white);
	    g.fillRect(ls, 2, drawZone.width, 23);
	    //fill left zone
	    g.setColor(new Color(176, 196, 222));
	    //left
	    g.fillRect(ls,  2, lMargin, 18);
	    //right
	    g.fillRect(ls+drawZone.width-rMargin,  2,  rMargin, 18);
	    //
	    g.setColor(Color.DARK_GRAY);
	    for (int i = 0; i <= points * 2 + 1; i++){
	        if (i % 2 == 0 && i != 0)
	        {
	          String szt = String.valueOf(i / 2);
	          g.setFont(new Font("Arial", Font.PLAIN, 9));
	          g.drawString(String.valueOf(i / 2), (int) (ls + i * range - (sz.x / 2)), 15);
	        }
	        else {
	        	if (i*range<drawZone.width) g.drawLine((int)(ls + i * range), 16, (int)(ls + i * range), 20);
	        }
	    }
	    for (int i = 0; i <= spoints * 2 + 1; i++){
	        if (i % 2 == 0 && i != 0)
	        {
	        }
	        else {
	        	if (i*srange<drawZone.width) g.drawLine((int)(ls + i * srange), 18, (int)(ls + i * srange), 20);
	        }
	    }
	    //draw border
	    g.setColor(Color.lightGray);
	    g.drawRect(ls, 2, drawZone.width, 18);
	}

    private void VerticalDrawTextAndMarks(Graphics g, int height)
	{            
		CustomDocument doc = (CustomDocument) m_monitor.getDocument();
		double scale = (double) doc.getProperty("ZOOM_FACTOR");
	    int points = (int)(height / dotsPermm*scale) / 10;
	    int spoints = (int)(height / dotsPermm*scale);
	    float range = (float) (5 * dotsPermm*scale);
	    float srange = (float) (5/4 * dotsPermm*scale);
	    Point sz = new Point(5,7);
	    g.setColor(Color.white);
	    g.fillRect(0, 0, 26, height);
	    //fill inlets
	    g.setColor(new Color(176, 196, 222));
	    g.fillRect(2,  0,  18, PageInletUp);
	    g.setColor(Color.DARK_GRAY);
	    for (int i = 0; i <= points * 2 + 1; i++){
	        if (i % 2 == 0 && i != 0){
	          String szt = String.valueOf(i / 2);
	          g.setFont(new Font("Arial", Font.PLAIN, 9));
	          Graphics2D g2 = (Graphics2D) g; 
	          // obtain the graphics context
	          Font font = new Font("Arial", Font.PLAIN, 9);    
	          AffineTransform affineTransform = new AffineTransform();
	          affineTransform.rotate(Math.toRadians(-90), 0, 0);
	          Font rotatedFont = font.deriveFont(affineTransform);
	          g2.setFont(rotatedFont);              
	          g2.drawString(String.valueOf(i / 2), 15, (int) ( i * range + (sz.y / 2)));
	        }
	        else        	  
	        {
	     		g.drawLine(19, (int)( i * range), 27, (int)( i * range));
	        }
	    }
	    for (int i = 0; i <= spoints * 2 + 1; i++){
	        if (i % 2 == 0 && i != 0)
	        {
	        }
	        else {
	        	if (i*srange<height) g.drawLine(22, (int)(i * srange), 27, (int)(i * srange));
	        }
	    }
	    //draw border
	    g.setColor(Color.lightGray);
	    //g.drawRect(2, 0, 18, height);
	}

    //Read file content into string with - Files.readAllBytes(Path path)
    private static String readAllBytesJava7(String filePath) 
    {
        String content = "";
        try
        {
        	//read file as the UTF-8 type!!
            content = new String ( Files.readAllBytes( Paths.get(filePath) ),"UTF-8" );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return content;
    }
    
    private int showWarningMessage() {
        String[] buttonLabels = new String[] {"예(S)", "아니오(D)", "취소"};
        String defaultOption = buttonLabels[0];
        Icon icon = null;
         
        return JOptionPane.showOptionDialog(this,
                "저장되지 않은 내용이 있습니다.\n" +
                "끝내기 전에 문서를 저장하시겠습니까",
                "알림",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                icon,
                buttonLabels,
                defaultOption);    
    }
 
    private boolean hasUnsaveData() {
        // checks if there's still something unsaved
        // this method always return true for demo purpose
        //return true;
    	return false;
    }
    
    private void handleClosing() {
        if (hasUnsaveData()) {
            int answer = showWarningMessage();
             
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    System.out.println("Save and Quit");
                    dispose();
                    break;
                     
                case JOptionPane.NO_OPTION:
                    System.out.println("Don't Save and Quit");
                    dispose();
                    break;
                     
                case JOptionPane.CANCEL_OPTION:
                    System.out.println("Don't Quit");
                    break;
            }
        } else {
            dispose();
        }      
    }
    
    public NeoHaneol() throws BadLocationException {
        //
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleClosing();
            }
        });
        //createMenu();
        MenuEx mu = new MenuEx(NeoHaneol.this);
        //createToolbar();
        ToolBarEx tb = new ToolBarEx(NeoHaneol.this);
        init();
        showStyles();
        currentFile = new File("이름 없는 문서1.hdf");
        createStausBar();
        this.setSize(1100, 750);
        this.setTitle("이름 없는 문서 1 - 한얼워드프로세서");
        // main icon
    	ImageIcon img = new ImageIcon(getClass().getResource("res/new_icon.png"));
    	this.setIconImage(img.getImage());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        //
        CustomDocument doc = (CustomDocument) m_monitor.getDocument();
        int page_width = (int) doc.getProperty("PAGE_WIDTH");
        int whiteSpace = (jsp.getWidth()-55-page_width)/2;
    	// adjust vertical scroll bar
        jsp.setViewportBorder(BorderFactory.createEmptyBorder(0, whiteSpace, 0, whiteSpace));
        //
        jsp.setBackground(new Color(245, 245, 245));
        leftSpace = (int) (jsp.getWidth()-55-page_width)/2;
    	drawColHeader();
    	jsp.setColumnHeaderView(columnheader);
    	//
    	final JComboBox zoomCombo = new JComboBox(new String[] {"50%", "75%", "100%", "125%", "150%", "200%"});	
	    zoomCombo.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e) {
	           String s = (String) zoomCombo.getSelectedItem();
	           s = s.substring(0, s.length() - 1);
	           double scale = new Double(s).doubleValue() / 100;
	           m_monitor.getDocument().putProperty("ZOOM_FACTOR",new Double(scale));
	           int page_width = (int) m_monitor.getDocument().getProperty("PAGE_WIDTH");
	           int page_height = (int) m_monitor.getDocument().getProperty("PAGE_HEIGHT");
	           View v=m_monitor.getUI().getRootView(m_monitor);
		       int i=0;
		       int pos = m_monitor.getCaretPosition();
		       while (v!=null) {
			    	  // 
			       i=v.getViewIndex(pos, Position.Bias.Forward);
			       v=v.getView(i);
			       	
			       if ( v instanceof SectionView){
				       int pageNumber = ((SectionView) v).getPageCount();
				       noWrapPanel.setPreferredSize(new Dimension((int) (page_width*scale), (int) (page_height*pageNumber*scale)));
				       break;
			       }
		       }
		       try {
					adjustmainTextPane();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	           //update ruler
	           updateVar();
	           m_monitor.revalidate();
	           m_monitor.repaint();
	           //
	           revalidate();
	           repaint();
	       }
	    });
	    zoomCombo.setSelectedItem("100%");
	    //
    	//for resizing a frame
    	this.addComponentListener(this);
    	
    	//auto save procedure
    	runnable = new Runnable(){
    		public void run(){
    			System.out.println("auto save");
    		}
    	};
    	service = Executors.newSingleThreadScheduledExecutor();
        
    	service.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.SECONDS);
    	

        
    }
	public static int getCaretRowPosition(JTextComponent src) throws BadLocationException {
	    
	    int caretPos = m_monitor.getCaretPosition();
	    int rowNum = (caretPos == 0) ? 1 : 0;
	    for (int offset = caretPos; offset > 0;) {
	        offset = Utilities.getRowStart(m_monitor, offset) - 1;
	        rowNum++;
	    }
	    return rowNum;
	}
	
	public static int getCaretColPosition(JTextComponent src) throws BadLocationException {
		int caretPos = m_monitor.getCaretPosition();
		int offset = Utilities.getRowStart(m_monitor, caretPos);
		int colNum = caretPos - offset + 1;
	    return colNum;
	}
	
	public static int getCaretPagePosition(JTextComponent src) {
	          try {
	               int y = src.modelToView(src.getCaretPosition()).y;
	               CustomDocument doc = (CustomDocument) m_monitor.getDocument();
	               int page_hight = (int) doc.getProperty("PAGE_HEIGHT");
	               int xline = y/page_hight;
	               return xline+1;
	          } catch (BadLocationException e) {
	          }
	          return -1;
	}
	
    private void init() throws BadLocationException {
    	m_monitor = new JTextPane(){
    		   public void repaint(int x, int y, int width, int height) {
    		        super.repaint(0, 0, getWidth(), getHeight());
    		    }
    	};
    	
    	//m_monitor.setEditorKit(new CustomEditorKit(2));
    	m_monitor.setEditorKit(new CustomEditorKit());
    	//
        CustomDocument doc = (CustomDocument) m_monitor.getDocument();
        m_monitor.setCaretColor(Color.black);
        m_monitor.setCaret(new CustomCaret(NeoHaneol.this));
        /*
        try{
        	//before table
    		//CustomDocument doc = (CustomDocument) m_monitor.getDocument();
        	st = doc.getLength();
        	colwidths = new int[] {250, 170, 100};
        	rowheights = new int[] {200, 150, 200};
        	celltext = new String[]{
			"so here we have created an array with a memory space of 3... this is how it looks actually \n",
			"Cell 2 within a table\n",
			"The length will be always 15",
			"","","","","",""
        	};
        	doc.insertTable(st, rowheights, colwidths);
     		for (int icell=0; icell<celltext.length; icell++)
        	{
	        	doc.insertString(st+icell, celltext[icell], null);
	        	st += celltext[icell].length();
        	}
        }catch (BadLocationException ex) {
        }
        */
        //Element root = m_monitor.getDocument().getDefaultRootElement();
        //System.out.println(root.toString());
        //BranchElement elem = (BranchElement) root.getElement(0);
        //System.out.println(elem.toString());
        
        //BranchElement child = (BranchElement) elem.getElement(0);
        //System.out.println(child.getName().toString());
    	//System.out.println(elem.getName().toString());
        ElementIterator it = new ElementIterator(m_monitor.getDocument());
    	Element elem = null;
    	BranchElement current = null;
    	while ( (elem = it.next()) != null )
        {
    		//elem = (BranchElement) root.getElement(1);
    		//System.out.println(elem.getName().toString());
    		if (elem.getName().toString() == "table") {
    			current = (BranchElement) elem;
    			//System.out.println(elem.getName().toString());
    			break;
    		}
        }
        
    	//updateCellText();
    	
        //
        //FILE LOCATION OF DICTIONARY
        String userDictionaryPath = "res/";
        //SET DICTIONARY PROVIDER FROM DICTIONARY PATH
        SpellChecker.setUserDictionaryProvider(new FileUserDictionary(userDictionaryPath));
        //REGISTER DICTIONARY
        SpellChecker.registerDictionaries(getClass().getResource(userDictionaryPath), "ko");
        SpellChecker.register(m_monitor);
        final SimpleAttributeSet attrs=new SimpleAttributeSet();
        //
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	String[] fontNames = gEnv.getAvailableFontFamilyNames();
    	String[] fontSize =  { "8", "9", "10", "11", "12", "13", "14", "15", "16", "18", "22", "26", "30", "34", "36", "40", "44", "48", "52" };
    	m_fontDialog = new FontDialog(NeoHaneol.this, fontNames, fontSize);
    	m_fontDialog.addComponentListener(new ComponentListener() {
		    public void componentHidden(ComponentEvent e)
		    {
		    	String font = m_fontDialog.m_lstFontName.getSelected();
		    	int fontsize =  Integer.parseInt(m_fontDialog.m_lstFontSize.getSelected());
		    	//
		    	MutableAttributeSet attr = new SimpleAttributeSet();
		        StyleConstants.setFontFamily(attr, font);
		        StyleConstants.setFontSize(attr, fontsize);
		        setAttributeSet(attr);
		    	m_fontDialog.dispose();
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
	    StyleContext sc = new StyleContext();
	    Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
	    Style mainStyle = sc.addStyle("MainStyle", defaultStyle);
	    //
        viewScale=new Double(1.0);
        //set page variables
        setPageVariable();
        //
        m_monitor.addMouseMotionListener(new MouseMotionListener(){
    		JEditorPane src;
    		int i;
    		int bound = 3;
    		View v;
    		Rectangle a;
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				CustomDocument doc = (CustomDocument) m_monitor.getDocument();
				JEditorPane src = (JEditorPane)e.getSource();
				Element root = m_monitor.getDocument().getDefaultRootElement();
		        BranchElement elem = (BranchElement) root.getElement(0);
		        BranchElement current = (BranchElement) elem.getElement(0);
		        
		        try {
					doc.remove(current.getStartOffset(), current.getEndOffset()-current.getStartOffset());
				} catch (BadLocationException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
		    	//st = doc.getLength(); // this is important
		        st = 0;

				if (clickOffset != null && isHC==true) {
					int newX = e.getPoint().x - clickOffset.x;
	   		        colwidths[colNumber] = oldw+newX;
	   		        doc.insertTable(st, rowheights, colwidths);
			        
				} else if(clickOffset != null && isVC==true) {
				    int newY = e.getPoint().y - clickOffset.y;
			        rowheights[rowNumber] = oldh+newY;
			    	doc.insertTable(st, rowheights, colwidths);
				}
				 
		    	for (int icell=0; icell<celltext.length; icell++)
		    	{
		    	try {
					doc.insertString(st+icell, celltext[icell], null);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		    	st += celltext[icell].length();
		    	} 
								
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				if (colNumber>0 && rowNumber>0){
				int bound = 4;
				src = (JEditorPane)e.getSource();
					
				pos = src.viewToModel(e.getPoint());
				v=src.getUI().getRootView(src);
				//
				m_monitor.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				//find position of the table
				Rectangle offset = findTablePosition(src);
				
				while (v!=null && !(v instanceof RowView)) {
			    	  // i is cell number
			         i=v.getViewIndex(pos, Position.Bias.Forward);
			         v=v.getView(i);
			         taborigin = a;
				}
				if(offset!=null){
					if (v instanceof RowView){
						rowNumber = i;
						Rectangle a = ((RowView) v).getAllocation();
						int rowHeight=(Integer)v.getAttributes().getAttribute(PARAM_ROW_HEIGHT);
						if ((a.y+rowHeight +bound >= e.getY()) && (a.y+rowHeight -bound <= e.getY())){
							m_monitor.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
	    					isHC=false;
	    					isVC=true;
	    				}
					}
				}
				while (v!=null && !(v instanceof CellView)) {
			    	  // i is cell number
			         i=v.getViewIndex(pos, Position.Bias.Forward);
			         v=v.getView(i);
				}
				if(offset!=null){
					if (v instanceof CellView){
						colNumber = i;
						int cellwidth=0;
						for (int j=0; j<=colNumber; j++) {
							cellwidth += colwidths[j];
						}
						if ((offset.x+cellwidth +bound >= e.getX()) && (offset.x+cellwidth -bound <= e.getX())){
							m_monitor.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
	    					isHC=true;
	    					isVC=false;
	    				}
					}
				}
				
			
			}
			}
			
    	});
    	
        //add images
        /*
        imgBucket = new ArrayList<>();
        imgBucket.add(new ImageBucket("c:\\rose.jpg","jpg"));
        imgBucket.add(new ImageBucket("c:\\youinna.jpg","jpg"));
        //
        */
        noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.setBorder(null);
        noWrapPanel.setBackground(new Color(245, 245, 245));
    	noWrapPanel.add(m_monitor);
    	
    	jsp = new JScrollPane(){
    		@Override
        	public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.lightGray);
                if (px_r_indent>0){
                	g.drawLine(leftSpace+me.width-rMargin+30+dx_r, 15, leftSpace+me.width-rMargin+30+dx_r, this.getHeight());
                }
                if (px_l_indent>0){
                	g.drawLine(leftSpace+lMargin+30+dx_l, 15, leftSpace+lMargin+30+dx_l, this.getHeight());
                }
            
            }
    	};
    	jsp.setViewportView(noWrapPanel);
    	jsp.getVerticalScrollBar().setUnitIncrement(30);
    	
    	jsp.addMouseListener(new MouseListener(){
		
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
				if (e.getX() > leftSpace+me.width-rMargin+25 && e.getX()< leftSpace+me.width-rMargin+35){
					px_r_indent = e.getX();
				}else if (e.getX() > leftSpace+lMargin+25 && e.getX()< leftSpace+lMargin+35){
					px_l_indent = e.getX();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				//m_monitor.getCaret().setVisible(true);
				if (px_r_indent>0){
					rMargin = leftSpace+me.width+29-e.getX();
					dx_r=0;
					px_r_indent=0;
					Graphics g = getGraphics();
					Insets tmp = (Insets) m_monitor.getDocument().getProperty("PAGE_MARGIN");
					m_monitor.getDocument().putProperty("PAGE_MARGIN",  new Insets(tmp.top, tmp.left, tmp.bottom, rMargin));
					//
					jsp.revalidate();
					jsp.repaint();
				}
				if (px_l_indent>0){
					lMargin = e.getX()-leftSpace-29;
					dx_l=0;
					px_l_indent=0;
					Graphics g = getGraphics();
					Insets tmp = (Insets) m_monitor.getDocument().getProperty("PAGE_MARGIN");
					//
					m_monitor.getDocument().putProperty("PAGE_MARGIN",  new Insets(tmp.top, lMargin, tmp.bottom, tmp.right));
					jsp.revalidate();
					jsp.repaint();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
    	});
    	jsp.addMouseMotionListener(new MouseMotionListener(){
    		@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if (px_r_indent>0){
					dx_r = e.getX()-px_r_indent;
					Graphics g = getGraphics();
					DrawIndents(g, leftSpace);
					jsp.repaint();
				}else if (px_l_indent>0){
					dx_l = e.getX()-px_l_indent;
					Graphics g = getGraphics();
					DrawIndents(g, leftSpace);
					jsp.repaint();
				}
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	//insert table
    	//int[] colwidths = new int[] {250, 170, 100};
    	//doc.insertTable(doc.getLength(), 2, colwidths);
    	
    	//StyleConstants.setFontFamily(attrs, defaultFont);
    	
    	
    	
    	//set default font
       	m_monitor.setFont(new Font("바탕체", 0, 14));
    	//
       	MutableAttributeSet set = new SimpleAttributeSet(m_monitor.getParagraphAttributes());
    	//
    	StyleConstants.setLineSpacing(set, 0.6f);
        int xStart = m_monitor.getSelectionStart();
        int xFinish = m_monitor.getSelectionEnd();
        //
        doc.setParagraphAttributes(xStart, xFinish-xStart, set, true);
        String filePath = "c://multicdata.txt";
        /*
        int st = doc.getLength();
    	int[] colwidths = new int[] {250, 170, 100};
    	int[] rowheights = new int[] {50, 50, 100};
    	String[] celltext = new String[]{
		"so here we have created an array with a memory space of 3... this is how it looks actually \n",
		"Cell 2 within a table\n",
		"The length will be always 15",
		"","","","","",""
    	};
    	doc.insertTable(st, rowheights, colwidths);
    	*/
 		
        String str = readAllBytesJava7(filePath);
        //remove first 1 byte trash
        str = str.substring(1);
        
        try{
    	    doc.insertString(doc.getLength(), str, attrs);
        }catch (Exception e){
        }
        
        //
        
        Style style1 = m_monitor.addStyle("Icon0", null);
        StyleConstants.setIcon(style1, new ImageIcon("C:\\youinna.jpg"));
        //
    	try{
    	    doc.insertString(doc.getLength(), "\\img:0", style1);
        }catch (Exception e){ }
    	
        
        m_monitor.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
        		 // TODO Auto-generated method stub
				 JEditorPane src=(JEditorPane)e.getSource();
				 show_status_info(src);
		         //
				 int pos = m_monitor.viewToModel(e.getPoint());
			     //
			     imageTransferText(pos, e.getPoint());
			     //
			     try {
					checkDiff();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
					if (colNumber>0){
					clickOffset = new Point(e.getPoint().x, e.getPoint().y);
					oldw = colwidths[colNumber];
					//System.out.println(oldw);
					oldh = rowheights[rowNumber];
					//System.out.println(rowheights[rowNumber]);
					}
			}
	
			@Override
			public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					isHC=false;
			}
        });
        m_monitor.addCaretListener(new CaretListener() { 
        	public void caretUpdate(CaretEvent e){
        		showAttribute();
        	}
        	
        });
        m_monitor.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				service.shutdown();
				
				
				
				JEditorPane src=(JEditorPane)e.getSource();
				show_status_info(src);
				//
				if ( (e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
			    	m_fontDialog.setVisible(true);
				}
				else if ( (e.getKeyCode() == KeyEvent.VK_F7)) {
					//
					show_editPaper();
					
				}
				else if ( (e.getKeyCode() == KeyEvent.VK_K) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
					//
				}
				else if ( (e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                      //
	                  if (m_findDialog==null)
	                  	m_findDialog = new FindDialog(NeoHaneol.this, 0);
	                  else m_findDialog.setSelectedIndex(0);
	                  //
	                  Dimension d1 = m_findDialog.getSize();
	                  Dimension d2 = NeoHaneol.this.getSize();
	                  int x = Math.max((d2.width-d1.width)/2, 0);
	                  int y = Math.max((d2.height-d1.height)/2, 0);
	                  m_findDialog.setBounds(x + NeoHaneol.this.getX(),
	                    y + NeoHaneol.this.getY(), d1.width, d1.height);
	                  m_findDialog.setVisible(true);            
	            }
				try {
					checkDiff();
				} catch (BadLocationException e1) {
					// TODO Au80721to-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				service = Executors.newSingleThreadScheduledExecutor();
		        //
		    	service.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.SECONDS);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
        //
        
        
        //image 2
        /*
        Style style2 = m_monitor.addStyle("Icon1", null);
        StyleConstants.setIcon(style2, new ImageIcon("c:\\rose.jpg"));
    	try{
    	    doc.insertString(doc.getLength(), "\\img:1", style2);
    	    //
        }catch (Exception e){
        
        }
        */
    	//get last image number
    	getLastPicNum();
    	drawRowHeader();
    	//
        Icon acrossLogo = new ImageIcon(getClass().getResource("res/tab_pos.png"));
        JLabel[] corners = new JLabel[4]; 
        for(int i = 0; i < 4 ;i++) {
          corners[i] = new JLabel();
          corners[i].setBackground(Color.WHITE);
          corners[i].setOpaque(true);
          corners[i].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(3,3,3,3),
            BorderFactory.createLineBorder(Color.black, 1)));
        }
        JLabel cornerLabel = new JLabel(acrossLogo);
        //
        jsp.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLabel);
        jsp.getViewport().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				m_monitor.revalidate();
    	     	m_monitor.repaint();
			}
        });
        jsp.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener(){ 
	        @Override
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            // here the control if vertical scroll bar has reached the maximum value
	        	     	m_monitor.revalidate();
	        	     	m_monitor.repaint();
	        }
	    });
        //
        
        //this is extremely important
        getContentPane().add(jsp);
        //
        
    	

   }
    
   void show_status_info(JEditorPane src){

       int row = 0;
		try {
			row = getCaretRowPosition(src);
		} catch (BadLocationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
       int column = 0;
		try {
			column = getCaretColPosition(src);
		} catch (BadLocationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
       int pageNumber = getCaretPagePosition(src);
       rowtextlabel.setText(row+"줄");
       columntextlabel.setText(column+"칸");
       pagetextlabel.setText(pageNumber+"쪽");
       typemodelabel.setText("삽입");
   }
   
   void drawColHeader(){
    		columnheader = new JLabel() {
	            Font f = new Font("Arial",Font.PLAIN, 10);
	            public void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                g.setFont(f);
	                //
	                DrawTextAndMarks(g, leftSpace);
	                //
	                g.setColor(Color.darkGray);
	                DrawIndents(g, leftSpace);
	            }
	            public Dimension getPreferredSize() {
	                return new Dimension((int)m_monitor.getPreferredSize().getWidth(),25);
	            } 
            };
            //
            columnheader.setBackground(new Color(245, 245, 245));
            columnheader.setBorder(new EtchedBorder());
    }
    
    void drawRowHeader(){
        JLabel rowheader = new JLabel() {
        Font f = new Font("Arial",Font.PLAIN, 10);
        public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        //
	        Dimension r = getPreferredSize();
	        g.setFont(f);
	        g.setColor(new Color(245, 245, 245));
	        //
	        int height_max=10;
	        if (r.height>height_max){
	        	height_max = r.height;
	        	double scale = (double) m_monitor.getDocument().getProperty("ZOOM_FACTOR");
	        	int page_width = (int) m_monitor.getDocument().getProperty("PAGE_WIDTH");
		        int page_height = (int) m_monitor.getDocument().getProperty("PAGE_HEIGHT");
		        View v=m_monitor.getUI().getRootView(m_monitor);
		        int i=0;
		        int pos = m_monitor.getCaretPosition();
		        //
		        while (v!=null) {
			    	 //
			         i=v.getViewIndex(pos, Position.Bias.Forward);
			         v=v.getView(i);
			         if ( v instanceof SectionView){
				        	//System.out.println("pos :"+pos);
				        	int pageNumber = ((SectionView) v).getPageCount();
				        	noWrapPanel.setPreferredSize(new Dimension((int) (page_width*scale), (int) (page_height*pageNumber*scale)));
				        	break;
			         }
    			}
		        
	        }
	        g.fillRect(0, 0, 25, height_max);
	        VerticalDrawTextAndMarks(g, this.getHeight());
	        g.setColor(Color.darkGray);
	        }

	        public Dimension getPreferredSize() {
	            return new Dimension(26,(int)m_monitor.getPreferredSize().getHeight());
	    } 
        };
        rowheader.setBackground(Color.white);
        rowheader.setOpaque(true);
        rowheader.setBorder(new EtchedBorder());//라벨에 적용시킨다.
        rowheader.addComponentListener(new ComponentAdapter() {//Waits for window to be resized by user
            public void componentResized(ComponentEvent e) {
            	repaint();
            }
        });
        jsp.setRowHeaderView(rowheader);
    }
    @Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		 Window[] children = this.getWindows();
         for (Window win : children) {
             if (win instanceof TablePaletteDialog){
                 win.setVisible(false);
             } else if (win instanceof ColorPaletteDialog){
                 win.setVisible(false);
             }
         }
		
	}
	

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
			repaint();
	}
    
    public void componentResized(ComponentEvent e) {
    	try {
			adjustmainTextPane();
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//
    }
    
    public void adjustmainTextPane() throws BadLocationException{
    	CustomDocument doc = (CustomDocument) m_monitor.getDocument();
  	  	int page_width = (int) doc.getProperty("PAGE_WIDTH");
  	  	double scale = (double) doc.getProperty("ZOOM_FACTOR");
    	//
    	 if (jsp.getWidth()-55> (int)(page_width*scale)){
    		 jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    		 //	
    		 int whiteSpace = (jsp.getWidth()-55 - (int)(page_width*scale))/2;
    		 leftSpace = (jsp.getWidth()-50 - (int)(page_width*scale))/2;
	         jsp.setViewportBorder(BorderFactory.createEmptyBorder(0, whiteSpace, 0, whiteSpace));
	         jsp.setBackground(new Color(245, 245, 245));
	         //
	         columnheader.repaint();
	         m_monitor.revalidate();
	            
	        } else{
	        	jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	           	jsp.setViewportBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	           	m_monitor.revalidate();
	        }
    }
    
    @Override
	public int print(Graphics pg, PageFormat pageFormat, int pageIndex) throws PrinterException {
			// TODO Auto-generated method stub
			pg.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
			//
			Insets pageMargin = (Insets) m_monitor.getDocument().getProperty("PAGE_MARGIN");
			int pageWidth = (int) m_monitor.getDocument().getProperty("PAGE_WIDTH");
			int pageHeight = (int) m_monitor.getDocument().getProperty("PAGE_HEIGHT");
			int wPage = pageWidth-2*PAGE_INSET-pageMargin.left-pageMargin.right;
			int hPage = pageHeight-2*PAGE_INSET-pageMargin.top-pageMargin.bottom;;
			//
			pg.setClip(0, 0, wPage, hPage+10);
			//page index
			String text = "- "+Integer.toString(pageIndex+1)+" -";
			pg.drawString(text,  (int)wPage/2-text.length(),  (int)hPage);
			//
			CustomDocument m_doc = (CustomDocument) m_monitor.getDocument();
			m_doc.putProperty("show paragraphs", null);
			// Only do this once per print
			if (m_printView == null) {
			      BasicTextUI btui = (BasicTextUI)m_monitor.getUI();
			      View root = btui.getRootView(m_monitor);
			      //
			      m_printView = new PrintView(m_doc.getDefaultRootElement(), root, wPage, hPage);
			      
			}
			boolean bContinue = m_printView.paintPage(pg, hPage, pageIndex);
			//
			m_doc.putProperty("show paragraphs", "A");
			    System.gc();
			    if (bContinue)
			      return PAGE_EXISTS;
			    else {
			      m_printView = null;
			      return NO_SUCH_PAGE;
			    }
			//
			
		
	}
    
    public static void main(String[] args) throws UnsupportedEncodingException, BadLocationException {
    	Config cfg = new Config();
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		//UIManager.setLookAndFeel(cfg.getProperty("theme"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        if (args.length == 0) {
        	new NeoHaneol();
        } else {
        	NeoHaneol th = new NeoHaneol();
            openFile(th.m_monitor, new File(args[0]));
        }
    }

	
	
}

/******************************************************************************/
class CustomEditorKit extends StyledEditorKit {
	
	protected final static int PAGE_INSET = 5;
	protected static final Color BACKGROUND = new Color(245, 245, 245);
	
	protected int pageWidth, pageHeight;
	protected Insets pageMargin;
	
	protected int multi=1;
	ViewFactory defaultFactory;
	//
	CustomEditorKit(){
		defaultFactory = new TableFactory();
	}
	CustomEditorKit(int value){
	if (value == 2){
			defaultFactory = new MultiFactory();
		} else {
			defaultFactory = new TableFactory();
		}
	}
	
	public void setPageWidth(int pageWidth){
    	this.pageWidth = pageWidth;
    }
    
    public void setPageHeight(int pageHeight){
    	this.pageHeight = pageHeight;
    }
    
    public int getPageWidth(){
    	return this.pageWidth;
    }
    
    public int getPageHeight(){
    	return this.pageHeight;
    }
    
    
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    public Document createDefaultDocument() {
        return new CustomDocument();
    }
    


    class TableFactory implements ViewFactory {
	    public View create(Element elem) {
	        String kind = elem.getName();
	        
	        if (kind != null) {
	            if (kind.equals(AbstractDocument.ContentElementName)) {
	                return new ShowParLabelView(elem);
	            	//return new LabelView(elem);
	            }
	            else if (kind.equals(AbstractDocument.ParagraphElementName)) {
	                return new PagingParagraphView(elem);
	            }
	            else if (kind.equals(AbstractDocument.SectionElementName)) {
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

    class MultiFactory implements ViewFactory {
	    public View create(Element elem) {
	        String kind = elem.getName();
	        
	        if (kind != null) {
	            if (kind.equals(AbstractDocument.ContentElementName)) {
	                return new ShowParLabelView(elem);
	            }
	            else if (kind.equals(AbstractDocument.ParagraphElementName)) {
	            	return new MultiPagingParagraphView(elem);
	            }
	            else if (kind.equals(AbstractDocument.SectionElementName)) {
	            	return new MultiSectionView(elem, View.Y_AXIS);
	            	
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
	/**
	* Creates a view from an element that spans the supplied axis
	* @param element
	* @param axis
	*/
	public class SectionView extends BoxView {
		public int width, height;
		public Insets margin;
		private int pageNumber;
		private int pageWidth;
		/**
		* Creates a view from an element that spans the supplied axis
		* @param element
		* @param axis
		*/
		public SectionView(Element element, int axis) {
			super(element, axis);
		}
		
		public int getPageCount(){
			return pageNumber;
		}
		public Boolean printMode(){
			return (Boolean) getDocument().getProperty("MODE_PRINTER");
		}
		
		protected int calculatePageBreak(int pageNumber) {
			int pageBreak = (pageNumber * getPageHeight()) - PAGE_INSET - getPageMargin().bottom;
			return pageBreak;
		}
		
		public double getZoomFactor() {
		    Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
		    if (scale != null) {
		        return scale.doubleValue();
		    }
		
		    return 1;
		}
		
		public int getPageWidth() {
		    int scale = (int) getDocument().getProperty("PAGE_WIDTH");
		    if (scale != 0) {
		        return scale;
		    }
		
		    return 1;
		}
		
		public int getPageHeight() {
		    int scale = (int) getDocument().getProperty("PAGE_HEIGHT");
		    if (scale != 0) {
		        return scale;
		    }
		
		    return 1;
		}
		
		public Insets getPageMargin() {
		    Insets scale = (Insets) getDocument().getProperty("PAGE_MARGIN");
		    if (scale != null) {
		        return scale;
		    }
		
		    return new Insets(20, 20, 20, 20);
		}
		
		protected void layout(int width, int height) {
			width = getPageWidth() - 2*PAGE_INSET -getPageMargin().left -getPageMargin().right;
			height = (int) (getPageHeight());
		    super.layout(width,
		                 new Double(height * getZoomFactor()).intValue());
		}
		 
		public float getPreferredSpan(int axis) {
			float span = 0;
			if (axis == View.X_AXIS) {
			span = getPageWidth();
			} else {
			span = (float)((pageNumber * getPageHeight())*getZoomFactor());
			}
			return span;
		}
		 
		public float getMinimumSpan(int axis) {
			return getPreferredSpan(axis);
		}
		
		public float getMaximumSpan(int axis) {
			return getPreferredSpan(axis);
		}
		
		protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans){
			super.layoutMajorAxis(targetSpan, axis, offsets, spans);
			int totalOffset = PAGE_INSET + getPageMargin().top;
			int pageBreak;
			pageNumber = 1;
			PagingParagraphView view;
			View v;
			//
			for (int i = 0; i < offsets.length; i++) {
				offsets[i] = totalOffset;
				pageBreak = calculatePageBreak(pageNumber);
				if (getView(i) instanceof TableView){
					//table height
					v = getView(i);
					//Rectangle a = ((TableView) v).getAllocation();
					//System.out.println(a.height);
					float a = v.getPreferredSpan(View.Y_AXIS);
					//System.out.println(a);
					totalOffset = (int) (a+80);
				}  else {
					view = (PagingParagraphView) getView(i);
					view.setPargraphOffset(totalOffset);
					view.setStartPage(pageNumber);
					if ((spans[i] + offsets[i]) > pageBreak) {
						view.layout(view.getWidth(), getHeight());
						pageNumber = view.getEndPage();
						spans[i] += view.getAdjustedSpan();
					}
					totalOffset = offsets[i] + spans[i];
				}
				
			}
		}
		
		protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
			super.layoutMinorAxis(targetSpan, axis, offsets, spans);
			 for (int i = 0; i < offsets.length; i++) {
				 offsets[i]=PAGE_INSET+getPageMargin().left;
	             spans[i] = getPageWidth()-2*PAGE_INSET-getPageMargin().left-getPageMargin().right;
	    	 }
		}
		
		public void paint(Graphics g, Shape a) {
			//	super.paint(g, a);
		    Graphics2D g2d = (Graphics2D) g;
		    double zoomFactor = getZoomFactor();
		    AffineTransform old = g2d.getTransform();
		    g2d.scale(zoomFactor, zoomFactor);
			Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
			Rectangle page = new Rectangle(alloc.x, alloc.y, getPageWidth(), getPageHeight());
			for (int i = 0; i < pageNumber; i++) {
				page.y = alloc.y + getPageHeight() * i;
				if (page.intersects(alloc)) {
					//write page index
					g.setColor(Color.black);
					String pageStr = "- "+Integer.toString(i+1)+" -";
					g.drawString(pageStr, page.x+page.width/2, page.y + page.height - PAGE_INSET -50);
					if (!printMode()) // only text mode not print mode
					paintPageFrame(g, page);
				}
			}
			// Fills in any unpainted areas
			g.setColor(new Color(245, 245, 245));
			g.fillRect(0, 0, alloc.x, page.y + page.height);
			g.fillRect(page.x + page.width, 0, page.x + page.width+alloc.x, page.y + page.height);
			g.fillRect(0, 0, page.x + page.width, alloc.y);
			//}
			/*
			if ((alloc.y + alloc.height) > (page.y + page.height)) {
				g.setColor(new Color(245, 245, 245));
				g.fillRect(page.x, page.y + page.height, page.width, alloc.height - page.height);
			}
			*/
			if (Math.round(getZoomFactor()*100) == 100){
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				//
			} else {
				g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			}
			super.paint(g2d, a);
			g2d.setTransform(old);
		}
		
		private void paintPageFrame(Graphics g, Rectangle page) {
			if ( getDocument().getProperty("PAGE_BORDER") !=null){
				Color oldColor = g.getColor();
				//borders
				g.setColor(BACKGROUND);
				g.fillRect(page.x, page.y, page.width, PAGE_INSET);
				g.fillRect(page.x, page.y, PAGE_INSET, page.height);
				g.fillRect(page.x, page.y + page.height - PAGE_INSET, page.width, PAGE_INSET);
				g.fillRect(page.x + page.width - PAGE_INSET, page.y, PAGE_INSET, page.height);
				//frame
				g.setColor(Color.lightGray);			
				g.drawRect(page.x + PAGE_INSET, page.y + PAGE_INSET,
						page.width - 2 * PAGE_INSET, page.height - 2 * PAGE_INSET);
				
				//shadow
				if (getDocument().getProperty("show shadow") != null){
				g.fillRect(page.x + page.width - PAGE_INSET, page.y + PAGE_INSET + 4, 
							4, page.height - 2 * PAGE_INSET);
				g.fillRect(page.x + PAGE_INSET + 4, page.y + page.height - PAGE_INSET,
						page.width - 2 * PAGE_INSET, 4);
				}
				//draw page indicator
				g.setColor(Color.lightGray);
				//
				if ( getDocument().getProperty("show textborder") != null){
					//left-top
					g.drawLine(page.x+PAGE_INSET+getPageMargin().left, page.y+PAGE_INSET+getPageMargin().top, page.x+PAGE_INSET+getPageMargin().left-20, page.y+PAGE_INSET+getPageMargin().top);
					g.drawLine(page.x+PAGE_INSET+getPageMargin().left, page.y+PAGE_INSET+getPageMargin().top, page.x+PAGE_INSET+getPageMargin().left, page.y+PAGE_INSET+getPageMargin().top-20);
					//right-top
					g.drawLine(page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+PAGE_INSET+getPageMargin().top, page.x+page.width-PAGE_INSET-getPageMargin().right+20, page.y+PAGE_INSET+getPageMargin().top);
					g.drawLine(page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+PAGE_INSET+getPageMargin().top, page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+PAGE_INSET+getPageMargin().top-20);
					//left-bottom
					g.drawLine(page.x+PAGE_INSET+getPageMargin().left, page.y+page.height-PAGE_INSET-getPageMargin().bottom, page.x+PAGE_INSET+getPageMargin().left-20, page.y+page.height-PAGE_INSET-getPageMargin().bottom);
					g.drawLine(page.x+PAGE_INSET+getPageMargin().left, page.y+page.height-PAGE_INSET-getPageMargin().bottom, page.x+PAGE_INSET+getPageMargin().left, page.y+page.height-PAGE_INSET-getPageMargin().bottom-20);
					//right-bottom
					g.drawLine(page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+page.height-PAGE_INSET-getPageMargin().bottom, page.x+page.width-PAGE_INSET-getPageMargin().right+20, page.y+page.height-PAGE_INSET-getPageMargin().bottom);
					g.drawLine(page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+page.height-PAGE_INSET-getPageMargin().bottom, page.x+page.width-PAGE_INSET-getPageMargin().right, page.y+page.height-PAGE_INSET-getPageMargin().bottom-20);
				}
				//
				g.setColor(oldColor);
			}
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

	class PagingParagraphView extends ParagraphView {
		private int startPage, endPage;
		private int adjustedSpan;
		private int paragraphOffset;
		public PagingParagraphView(Element elem) {
			super(elem);
			startPage = 1;
			endPage = 1;
			adjustedSpan = 0;
			paragraphOffset = 0;
		}
		public int getPageHeight() {
		    int scale = (int) getDocument().getProperty("PAGE_HEIGHT");
		    if (scale != 0) {
		        return scale;
		    }
		
		    return 1;
		}
		public Insets getPageMargin() {
		    Insets scale = (Insets) getDocument().getProperty("PAGE_MARGIN");
		    if (scale != null) {
		        return scale;
		    }
		
		    return new Insets(0, 0, 0, 0);
		}
		 
		protected int calculatePageBreak(int pageNumber) {
			int pageBreak = (pageNumber * getPageHeight()) - PAGE_INSET - getPageMargin().bottom;
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
		 
		protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
			super.layoutMajorAxis(targetSpan, axis, offsets, spans);
			 
			if (paragraphOffset != 0) {
				endPage = startPage;
				int relativeBreak = calculatePageBreak(endPage) - paragraphOffset;
				int correctedOffset;
				adjustedSpan = 0;
				for (int i = 0; i < offsets.length; i++) {
					// determine the location of the page break
					if (offsets[i] + spans[i] > relativeBreak) {
					correctedOffset = relativeBreak
					+ getPageMargin().bottom + (2 * PAGE_INSET) + getPageMargin().top
					- offsets[i];
					 
					for (int j = i; j < offsets.length; j++) {
					offsets[j] += correctedOffset;
					}
					adjustedSpan += correctedOffset;
					endPage++;
					relativeBreak = calculatePageBreak(endPage) - paragraphOffset;
					}
				}
			}
		}
	}

}
