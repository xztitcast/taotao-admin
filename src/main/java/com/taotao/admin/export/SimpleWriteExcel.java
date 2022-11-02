package com.taotao.admin.export;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * @author eden
 * @time 2022年7月26日 下午6:21:14
 * @param <T>
 */
public class SimpleWriteExcel<T> {
	
	private int row = 0;
	private final Workbook workbook;
    private final Sheet sheet;
	private final List<Entry<Object,ExcelField>> excelFields = new ArrayList<>();
	
	public SimpleWriteExcel (String sheetName) {
		this(sheetName,500);
	}
	
	public SimpleWriteExcel (String sheetName,int rowAccessWindowSize) {
		Assert.hasLength(sheetName, "this sheetName is required; it must not be null");
		this.workbook = new SXSSFWorkbook(rowAccessWindowSize);
		this.sheet = this.workbook.createSheet(sheetName);
	}
	
	public SimpleWriteExcel<T> addRows(List<T> data){
		if(null != data && !data.isEmpty()){
			if(excelFields.isEmpty()){
				Class<?> clazz = data.get(0).getClass();
				this.excelFields.addAll(getExcelFields(clazz));
				Assert.notEmpty(this.excelFields, String.join("", clazz.toString()," Not ExcelField."));
			    CellStyle titleCellStyle = this.workbook.createCellStyle();
			    titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
			    titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			    Font titleFont = this.workbook.createFont();    
			    titleFont.setBold(true);
			    titleCellStyle.setFont(titleFont);
			    sheet.setDefaultColumnWidth(20);
			    sheet.setDefaultRowHeightInPoints(20);
			    Row titleRow = this.sheet.createRow(this.row);
			    titleRow.setHeightInPoints((short)24);
				for(int i=0,size=this.excelFields.size();i<size;i++) {
					Cell cell = titleRow.createCell(i);
					cell.setCellStyle(titleCellStyle);
					cell.setCellValue(excelFields.get(i).getValue().title());
				}
			}
			int lastRowNum = this.sheet.getLastRowNum();
			for(int i=0,size=data.size();i<size;i++){
				T target = data.get(i);
				Row row = this.sheet.createRow(i + lastRowNum + 1);
				for (int j=0,length=this.excelFields.size();j<length; j++){
					Cell cell = row.createCell(j);
					Object object = this.excelFields.get(j).getKey();
					if(object instanceof Method){
						Object value = ReflectionUtils.invokeMethod((Method)object, target);
						if(null != value){
							cell.setCellValue(value.toString());
						}
					}else{
						ExcelFieldCallback<?> excelFieldCallback = (ExcelFieldCallback<?>)object;
						Method callMethod = ExcelFieldCallback.class.getMethods()[0];
                        ReflectionUtils.invokeMethod(callMethod,excelFieldCallback, this.workbook,row,cell,target);
					}
				}
			}
		}
		return this;
	}
	
	/**
	 * 重载添加统计行
	 * @time 2022年11月02号 15:25
	 * @param countMap
	 * @return
	 */
	public  SimpleWriteExcel<T> addRows(Map<String, Object> countMap){
		CellStyle titleCellStyle = this.workbook.createCellStyle();
	    titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
	    Font titleFont = this.workbook.createFont();    
	    titleFont.setBold(true);
	    titleCellStyle.setFont(titleFont);
	    Row countRow = this.sheet.createRow(this.row++);
	    countRow.setRowStyle(titleCellStyle);
	    countRow.setHeightInPoints((short)25);
	    int index = 0;
	    for(String key : countMap.keySet()) {
	    	Cell cell = countRow.createCell(index++);
	    	cell.setCellStyle(titleCellStyle);
	    	cell.setCellValue(key);
	    	Cell valueCell = countRow.createCell(index++);
	    	valueCell.setCellStyle(titleCellStyle);
	    	valueCell.setCellValue(countMap.get(key).toString());
	    }
		return this;
	}
	
	private List<Entry<Object,ExcelField>> getExcelFields(Class<?> clazz){
		Map<Object,ExcelField> map = new HashMap<>();
		ReflectionUtils.doWithLocalFields(clazz, (field) -> {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if(null != excelField){
				Class<? extends ExcelFieldCallback<?>>[] callbackClazzs = excelField.writeCallback();
				if(callbackClazzs.length != 0){
					try {
						map.put(callbackClazzs[0].getConstructor().newInstance(),excelField);
					} catch (Exception e) {
						throw new IllegalArgumentException(e);
					}
				}else{
					String fieldName = field.getName();
					PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Assert.notNull(propertyDescriptor, String.join("", clazz.toString()," Field ",fieldName," Not gett sett."));
					map.put(propertyDescriptor.getReadMethod(),excelField);
				}
			}
		});
		List<Entry<Object,ExcelField>> list = new ArrayList<Entry<Object,ExcelField>>(map.entrySet());
		Collections.sort(list, (entry1,entry2) -> {
			return Integer.valueOf(entry1.getValue().order()).compareTo(entry2.getValue().order());
		});
		return list;
	}
	
	
	public void write(OutputStream stream) {
		try {
			this.workbook.write(stream);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}finally{
			try {
				if(null != this.workbook){
					this.workbook.close();
				}
				if(null != stream){
					stream.close();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
}
