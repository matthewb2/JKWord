package oata;

import java.util.*;

import javax.swing.text.*;

public class CustomDocument extends DefaultStyledDocument {
    public static final String ELEMENT_NAME_TABLE = "table";
    public static final String ELEMENT_NAME_ROW = "row";
    public static final String ELEMENT_NAME_CELL = "cell";
    public static final String PARAM_CELL_WIDTH = "cell-width";
    
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

    protected void insertTable(int offset, int rowCount, int[] colWidths) {
        try {
            SimpleAttributeSet attrs = new SimpleAttributeSet();

            ArrayList<ElementSpec> tableSpecs = new ArrayList<ElementSpec>();
            tableSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag

            SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
            tableAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_TABLE);
            ElementSpec tableStart = new ElementSpec(tableAttrs, ElementSpec.StartTagType);
            tableSpecs.add(tableStart); //start table tag

            fillRowSpecs(tableSpecs, rowCount, colWidths);

            ElementSpec tableEnd = new ElementSpec(tableAttrs, ElementSpec.EndTagType);
            tableSpecs.add(tableEnd); //end table tag

            tableSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new paragraph after table

            ElementSpec[] spec = new ElementSpec[tableSpecs.size()];
            tableSpecs.toArray(spec);

            this.insert(offset, spec);
        }
        catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    protected void fillRowSpecs(ArrayList<ElementSpec> tableSpecs, int rowCount, int[] colWidths) {
        SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
        rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
        for (int i = 0; i < rowCount; i++) {
            ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
            tableSpecs.add(rowStart);

            fillCellSpecs(tableSpecs, colWidths);

            ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
            tableSpecs.add(rowEnd);
        }

    }

    protected void fillCellSpecs(ArrayList<ElementSpec> tableSpecs, int[] colWidths) {
        for (int i = 0; i < colWidths.length; i++) {
            SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
            cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
            cellAttrs.addAttribute(PARAM_CELL_WIDTH, new Integer(colWidths[i]));
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
    
    public void insertRow(int offset) {
        Element root=getDefaultRootElement();
        int ind=root.getElementIndex(offset);

        Element table=getDeepesttableFromOffset(root, offset);
        if (table!=null) {
            int rowIndex=table.getElementIndex(offset)+1;
            insertRow(table, rowIndex);
        }
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

    public void insertRow(Element table, int rowNumber) {
        try {
            int[] colWidths=new int[table.getElement(0).getElementCount()];

            for (int i=0; i< colWidths.length; i++) {
                Element cell=table.getElement(0).getElement(i);
                colWidths[i]=(Integer)cell.getAttributes().getAttribute(PARAM_CELL_WIDTH);
            }

            int offset=table.getStartOffset();
            if (rowNumber>=table.getElementCount()) {
                //add after last row
                offset=table.getEndOffset();
            }
            else {
                Element row=table.getElement(rowNumber);

                offset=row.getStartOffset();
            }

            int depth=getTableDepth(table);
            SimpleAttributeSet attrs=new SimpleAttributeSet();
            ArrayList<ElementSpec> specs=new ArrayList<ElementSpec>();

            specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag
            specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close cell tag
            specs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close row tag

            fillRowSpecs(specs, 1, colWidths);

            specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new row
            specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new cell
            specs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new paragraph

            ElementSpec[] spec = new ElementSpec[specs.size()];
            specs.toArray(spec);

            insert(offset, spec);
        } catch (BadLocationException e) {
            e.printStackTrace();  
        }
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
}
