package com.parkbobo.utils;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelCellFactory {
	public Label generatCell(int column,int row,String value)
	{
		Label label=new Label(column,row,value);
		try { 		
		    WritableCellFormat cellFormat=new WritableCellFormat();
		    cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
		    cellFormat.setWrap(true);
		    cellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
		    label.setCellFormat(cellFormat);
	    
		} catch (RowsExceededException e) {   
			e.printStackTrace();   
		} catch (WriteException e) {   
			e.printStackTrace();   
		}    
		return label;
	}
}
