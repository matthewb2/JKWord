package oata;

import java.awt.Color;
import java.util.*;

import javax.swing.text.*;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

public class CustomDocument extends DefaultStyledDocument {
    public static final String ELEMENT_NAME_TABLE = "table";
    public static final String ELEMENT_NAME_ROW = "row";
    public static final String ELEMENT_NAME_CELL = "cell";
    public static final String PARAM_CELL_WIDTH = "cell-width";
    public static final String PARAM_ROW_HEIGHT = "row-height";
    public static final String PARAM_CELL_SPAN= "cell-span";
    
    public static final String ATTR_NAME_FONT_SIZE = "font-size";
	public static final String ATTR_NAME_FONT_FAMILY = "font-family";
	public static final String ATTR_NAME_BOLD = "bold";
	public static final String ATTR_NAME_ITALIC = "italic";
	public static final String ATTR_NAME_UNDERLINE = "underline";
	public static final String ATTR_NAME_JUNDERLINE = "junderline";
	public static final String ATTR_NAME_ALIGN = "align";
	public static final String ATTR_NAME_ABOVE = "above";
	public static final String ATTR_NAME_BELOW = "below";
	public static final String ATTR_NAME_LEFT = "left";
	public static final String ATTR_NAME_RIGHT = "right";
	public static final String ATTR_NAME_LINE_SPACING = "line-spacing";
	
	public static final String TAG_NAME_DOCUMENT = "doc";
	
	public static final String ATTR_NAME_FIRST = "first";
	
	public static final String JAGGED_UDERLINE_ATTRIBUTE_NAME="jagged-underline";
 

    public CustomDocument() {
    }
    

    public void setJaggedUnderline(int startOffset, int endOffset) {
        SimpleAttributeSet attrs=new SimpleAttributeSet();
        attrs.addAttribute(JAGGED_UDERLINE_ATTRIBUTE_NAME, Boolean.TRUE);
        setCharacterAttributes(startOffset, endOffset-startOffset, attrs, false);
    }
    
    public void setSizeWidth(int startoffset, int endoffset, int colNumber, int width)
    {
    	String getTextData = null;
    	int length = endoffset - startoffset;
    	if (length == 0) length =1;
    	//remove
    	try {
    		getTextData = getText(startoffset, endoffset-startoffset).replace("\n", "");
			getTextData += "\n";
    		//getTextData = getText(startoffset, endoffset-startoffset);
    		remove(startoffset, length);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// insert

   	 	ArrayList tableSpecs = new ArrayList();
   	 	SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
 	
   	 	SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
   	 	
   	 	ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
        ElementSpec cellEnd = new ElementSpec(cellAttrs, ElementSpec.EndTagType);
        
        cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
        cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(width));
        cellAttrs.addAttribute(PARAM_CELL_SPAN, new Integer(2));
        ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
        
        rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
        //cellAttrs.addAttribute(PARAM_ROW_HEIGHT, new Integer(height));
        ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
        ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
        
    	if (colNumber == 0){

            	tableSpecs.add(parEnd);
            	tableSpecs.add(cellEnd);
    	        tableSpecs.add(rowEnd);
                tableSpecs.add(rowStart);
                                
		        tableSpecs.add(cellStart);
		
		        ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
		        tableSpecs.add(parStart);
		        ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
		        tableSpecs.add(parContent);
		        tableSpecs.add(parEnd);
		    
		        tableSpecs.add(cellEnd);
		     
		        tableSpecs.add(rowEnd);

    		
    	} else {
    		            
            tableSpecs.add(parEnd);
            tableSpecs.add(cellEnd);
            
		    tableSpecs.add(cellStart);
		
		    ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
		    tableSpecs.add(parStart);
		    ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
		    tableSpecs.add(parContent);
		    tableSpecs.add(parEnd);
		
		    tableSpecs.add(cellEnd);
		 
		    tableSpecs.add(rowEnd);
            
    		
    	}
    	        
  
         SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
         tableAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_TABLE);
         
         ElementSpec tableEnd = new ElementSpec(tableAttrs, ElementSpec.EndTagType);
         //tableSpecs.add(tableEnd); //end table tag

         
         ElementSpec[] spec = new ElementSpec[tableSpecs.size()];
         tableSpecs.toArray(spec);
         
         
         try {
			insert(startoffset, spec);
			insertString(startoffset, getTextData, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
         
         
    	 
    }
    protected void mergeCells(int offset, int rowNumber, int[] colWidths, int[] rowHeights, int totalcol) {
    	
    	try {
    		SimpleAttributeSet attrs = new SimpleAttributeSet();

            ArrayList<ElementSpec> tableSpecs = new ArrayList<ElementSpec>();
            tableSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag

            SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
            tableAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_TABLE);
            ElementSpec tableStart = new ElementSpec(tableAttrs, ElementSpec.StartTagType);
            tableSpecs.add(tableStart); //start table tag
            
            for (int i=0; i<rowHeights.length; i++){
	            //fillRowSpecs(tableSpecs, rowHeights, colWidths);
	            SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
	            ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
	            //
	            tableSpecs.add(rowStart);
	            rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
	            rowAttrs.addAttribute(PARAM_ROW_HEIGHT, new Integer(rowHeights[i]));
	            SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
	            ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
	            ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
	            ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
	            ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	            ElementSpec cellEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	        
	            fillCellSpecs(tableSpecs, colWidths);
	            
	            ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
	            tableSpecs.add(rowEnd);
        	}
            
            ElementSpec tableEnd = new ElementSpec(tableAttrs, ElementSpec.EndTagType);
            tableSpecs.add(tableEnd); //end table tag           
            
            //System.out.println(tableSpecs.indexOf(cellStart));
            //remove elements to merge cells
            int last=0;
            for (int i = 0; i < tableSpecs.size(); i++) {
                if (tableSpecs.get(i).getAttributes().getAttribute(ElementNameAttribute) == ELEMENT_NAME_CELL){
                	//System.out.println(i);
                	last = i;
                }
            }
                       //
            for (int i= last; i < last+5; i++) {
            	tableSpecs.remove(last);
            }

            /*
            Object[] array = tableSpecs.toArray();
    		System.out.println(Arrays.toString(array));
    		*/

            ElementSpec[] spec = new ElementSpec[tableSpecs.size()];
            tableSpecs.toArray(spec);
		
            this.insert(offset, spec);
    	}
    	  catch (BadLocationException ex) {
              ex.printStackTrace();
          }
    	
    }
    
    
    protected void insertTable(int offset, int[] rowHeights, int[] colWidths) {
    	
        try {
            SimpleAttributeSet attrs = new SimpleAttributeSet();

            ArrayList<ElementSpec> tableSpecs = new ArrayList<ElementSpec>();
            tableSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag

            SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
            tableAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_TABLE);
            ElementSpec tableStart = new ElementSpec(tableAttrs, ElementSpec.StartTagType);
            tableSpecs.add(tableStart); //start table tag
            
            for (int i=0; i<rowHeights.length; i++){
	            //fillRowSpecs(tableSpecs, rowHeights, colWidths);
	            SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
	            ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
	            //
	            tableSpecs.add(rowStart);
	            rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
	            rowAttrs.addAttribute(PARAM_ROW_HEIGHT, new Integer(rowHeights[i]));
	            SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
	            ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
	            ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
	            ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
	            ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	            ElementSpec cellEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	        
	            fillCellSpecs(tableSpecs, colWidths);
	            
	            ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
	            tableSpecs.add(rowEnd);
        	}
            
            ElementSpec tableEnd = new ElementSpec(tableAttrs, ElementSpec.EndTagType);
            tableSpecs.add(tableEnd); //end table tag
            
            
            int last=0;
            for (int i = 0; i < tableSpecs.size(); i++) {
                if (tableSpecs.get(i).getAttributes().getAttribute(ElementNameAttribute) == ELEMENT_NAME_CELL){
                	//System.out.println(i);
                	last = i;
                }
            }
            //
            for (int i= last; i < last+5; i++) {
            	//tableSpecs.remove(last);
            }

           
            
            Object[] array = tableSpecs.toArray();
    	    //	
            ElementSpec[] spec = new ElementSpec[tableSpecs.size()];
            tableSpecs.toArray(spec);

            this.insert(offset, spec);
        }
        catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
    
    
    protected void fillRowSpecs(ArrayList<ElementSpec> tableSpecs, int[] rowHeights, int[] colWidths) {
        
        //
        //int[] rowHeights = new int[rowCount];
        int rowCount = rowHeights.length;
        
    	//for (int i=0; i<rowCount; i++) rowHeights[i] =30;
        //
        for (int i = 0; i < rowCount; i++) {
        	SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
            ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
            //
            tableSpecs.add(rowStart);
            rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
            rowAttrs.addAttribute(PARAM_ROW_HEIGHT, new Integer(rowHeights[i]));
            //fillCellSpecs(tableSpecs, colWidths);
            SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
            cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
            //cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(colWidths[i]));
            cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(100));
            cellAttrs.addAttribute(PARAM_CELL_SPAN, new Integer(2));
            ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
            tableSpecs.add(cellStart);
            ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
            tableSpecs.add(parStart);
            ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
            tableSpecs.add(parContent);
            ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
            tableSpecs.add(parEnd);
            ElementSpec cellEnd = new ElementSpec(cellAttrs, ElementSpec.EndTagType);
            tableSpecs.add(cellEnd);
            // second column
            cellAttrs = new SimpleAttributeSet();
            cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
            //cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(colWidths[i]));
            cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(100));
            cellAttrs.addAttribute(PARAM_CELL_SPAN, new Integer(2));
            cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
            tableSpecs.add(cellStart);
            parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
            tableSpecs.add(parStart);
            parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
            tableSpecs.add(parContent);
            parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
            tableSpecs.add(parEnd);
            cellEnd = new ElementSpec(cellAttrs, ElementSpec.EndTagType);
            tableSpecs.add(cellEnd);
                        
            ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
            tableSpecs.add(rowEnd);
        }

    }

    protected void mergeCellSpecs(ArrayList<ElementSpec> tableSpecs, int rowNumber, int[] colNumbers) {
    	
    	tableSpecs.get(rowNumber);
    	
    }
    protected void fillCellSpecs(ArrayList<ElementSpec> tableSpecs, int[] colWidths) {
    	       
       
        for (int i=0; i<colWidths.length; i++){
            //fillCellSpecs(tableSpecs, colWidths);
        	    SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
                cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
	            cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(colWidths[i]));
	            //cellAttrs.addAttribute(PARAM_CELL_SPAN, new Integer(2));
	            ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
	            tableSpecs.add(cellStart);
	            ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);     
	            tableSpecs.add(parStart);
	            ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
	            tableSpecs.add(parContent);
	            ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	            tableSpecs.add(parEnd);
	            ElementSpec cellEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
	            tableSpecs.add(cellEnd);
            }
    }
    protected int getTableDepth(Element table) {
        int res=1;
        Element parent=table.getParentElement();
        while (parent!=null) {
            if (parent.getName().equalsIgnoreCase(ELEMENT_NAME_TABLE)) {
                res++;
            }
            parent=parent.getParentElement();
        }
        return res;
    }
    
   

    public void insertCol(int offset) {
        Element root=getDefaultRootElement();
        int ind=root.getElementIndex(offset);

        Element table=getDeepesttableFromOffset(root, offset);
        if (table!=null) {
            int rowIndex=table.getElementIndex(offset);
            Element row=table.getElement(rowIndex);
            int colIndex=row.getElementIndex(offset);
            insertCol(table, colIndex);
        }
    }
    
    public void mergeCol(int offset) {
        Element root=getDefaultRootElement();
        int ind=root.getElementIndex(offset);

        Element table=getDeepesttableFromOffset(root, offset);
        if (table!=null) {
            int rowIndex=table.getElementIndex(offset);
            Element row=table.getElement(rowIndex);
            int colIndex=row.getElementIndex(offset);
            mergeCol(table, colIndex);
        }
    }
    
    protected Element getDeepesttableFromOffset(Element root, int offset) {
        int ind=root.getElementIndex(offset);
        Element table=root.getElement(ind);
        if (table.getName().equalsIgnoreCase(ELEMENT_NAME_TABLE)) {
            int rowIndex=table.getElementIndex(offset);
            Element row=table.getElement(rowIndex);
            int cellIndex=row.getElementIndex(offset);
            Element cell=row.getElement(cellIndex);

            int newInd=cell.getElementIndex(offset);
            Element newTable=cell.getElement(newInd);
            if (!newTable.getName().equalsIgnoreCase(ELEMENT_NAME_TABLE)) {
                return table;
            }
            else {
                return getDeepesttableFromOffset(cell, offset);
            }
        }

        return null;
    }

    
    public void insertCol(Element table, int colNumber) {
        try {
            int colWidth=(Integer)table.getElement(0).getElement(colNumber).getAttributes().getAttribute(PARAM_CELL_WIDTH);

            for (int rn=0; rn<table.getElementCount(); rn++) {
                Element row=table.getElement(rn);
                Element cell=row.getElement(colNumber);

                SimpleAttributeSet attrs=new SimpleAttributeSet();
                ArrayList<ElementSpec> specs=new ArrayList<ElementSpec>();
                	
                specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag
                specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close cell tag

                fillCellSpecs(specs, new int[] {colWidth});

                specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new row
                specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new cell

                ElementSpec[] spec = new ElementSpec[specs.size()];
                specs.toArray(spec);

                insert(cell.getEndOffset(), spec);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    public void mergeCol(Element table, int colNumber) {
        try {
            int colWidth=(Integer)table.getElement(0).getElement(colNumber).getAttributes().getAttribute(PARAM_CELL_WIDTH);

            for (int rn=0; rn<table.getElementCount(); rn++) {
                Element row=table.getElement(rn);
                Element cell=row.getElement(colNumber);

                SimpleAttributeSet attrs=new SimpleAttributeSet();
                ArrayList<ElementSpec> specs=new ArrayList<ElementSpec>();
                
                
                /*	
                specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag
                specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close cell tag

                
                fillCellSpecs(specs, new int[] {colWidth});

                specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new row
                specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new cell
				*/
                
                ElementSpec[] spec = new ElementSpec[specs.size()];
                specs.toArray(spec);

                insert(cell.getEndOffset(), spec);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
