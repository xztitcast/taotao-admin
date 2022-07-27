package com.taotao.admin.export;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 
 * @author eden
 * @time 2022年7月26日 下午6:21:26
 */
@Slf4j
public class SimpleReadExcel implements Closeable{

	private final Workbook workbook;

	private final DecimalFormat df = new DecimalFormat("#");

	public SimpleReadExcel (InputStream in,Format format){
		Assert.notNull(in,"InputStream is required; it must not be null");
		Assert.notNull(format,"Format is required; it must not be null");
		try {
			switch (format) {
				case XLSX: this.workbook = new XSSFWorkbook(in); break;
				default: this.workbook = new HSSFWorkbook(in);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public <T> List<T> getData(Class<T> clazz) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Assert.notNull(clazz,"clazz is required; it must not be null");
		Map<String, Object> excelFields = this.getExcelFields(clazz);
		Assert.notEmpty(excelFields, String.join("", clazz.toString()," Not ExcelField."));
		try {
			Sheet sheet = workbook.getSheetAt(0);
			if(null != sheet){
				Row titleRow = sheet.getRow(sheet.getFirstRowNum());
				if(null != titleRow) {
					String[] titles = new String[(int)titleRow.getLastCellNum()];
					int i = 0;
					for(Cell cell:titleRow) {
						CellType cellTypeEnum = cell.getCellType();
						if(cellTypeEnum == CellType.STRING) {
							titles[i] = cell.getStringCellValue();
						}else if(cellTypeEnum == CellType.NUMERIC) {
							titles[i] = df.format(cell.getNumericCellValue());
						}else{
							log.error(String.format("表头第  %d 列，类型无法转换，属性类型： String，单元格类型：%s", cell.getRowIndex(),cellTypeEnum));
							titles[i] = "";
						}
						i++;
					}
					sheet.removeRow(titleRow);
					// titles
					if(titles.length != 0){
						int lastRowNum = sheet.getLastRowNum();
						List<T> datas = new ArrayList<>(lastRowNum + lastRowNum / 2);
						for(Row row:sheet){
							T target = clazz.getConstructor().newInstance();
							int lastCellNum = titles.length;
							for(int j=0;j<lastCellNum;j++){
								Cell cell = row.getCell(j);
								if(null != cell){
									Object object = excelFields.get(titles[j]);
									if(object instanceof Method) {
										Method method = (Method)object;
										ReflectionUtils.invokeMethod(method,target,getCellValue(cell,method));
									}else if(object != null) {
										ExcelFieldCallback<?> excelFieldCallback = (ExcelFieldCallback<?>)object;
										Method callMethod = ExcelFieldCallback.class.getMethods()[0];
										ReflectionUtils.invokeMethod(callMethod,excelFieldCallback, this.workbook,row,cell,target);
									}
								}
							}
							datas.add(target);
						}
						return datas;
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
		return Collections.emptyList();
	}


	public List<Object[]> getRows(){
		Sheet sheet = workbook.getSheetAt(0);
		if(null != sheet){
			int lastRowNum = sheet.getLastRowNum();
			List<Object[]> datas = new ArrayList<>(lastRowNum + lastRowNum / 2);
			for(Row row:sheet){
				Object[] objects = new Object[(int)row.getLastCellNum()];
				int i = 0;
				for(Cell cell:row){
					CellType cellType = cell.getCellType();
					if(CellType.BOOLEAN == cellType){
						objects[i] = cell.getBooleanCellValue();
					}else if(CellType.NUMERIC == cellType){
						objects[i] = cell.getNumericCellValue();
					}else{
						objects[i] = cell.getStringCellValue();
					}
					i++;
				}
				datas.add(objects);
			}
			return datas;
		}
		return Collections.emptyList();
	}

	private Object getCellValue(Cell cell,Method method){
		Class<?> returnType = method.getParameterTypes()[0];
		CellType cellType = cell.getCellType();
		if((Integer.class.isAssignableFrom(returnType)||int.class.isAssignableFrom(returnType))&&cellType==CellType.NUMERIC){
			return Double.valueOf(cell.getNumericCellValue()).intValue();
		}else if((long.class.isAssignableFrom(returnType) || Long.class.isAssignableFrom(returnType))&&cellType==CellType.NUMERIC){
			return Long.parseLong(cell.getStringCellValue());
		}else if((float.class.isAssignableFrom(returnType)||Float.class.isAssignableFrom(returnType))&&cellType==CellType.NUMERIC){
			return Double.valueOf(cell.getNumericCellValue()).floatValue();
		}else if((double.class.isAssignableFrom(returnType)||Double.class.isAssignableFrom(returnType))&&cellType==CellType.NUMERIC){
			return cell.getNumericCellValue();
		}else if(BigDecimal.class.isAssignableFrom(returnType)&&cellType==CellType.NUMERIC){
			return BigDecimal.valueOf(cell.getNumericCellValue());
		}else if(Date.class.isAssignableFrom(returnType)&&cellType==CellType.NUMERIC){
			return cell.getDateCellValue();
		}else if(Boolean.class.isAssignableFrom(returnType)&&cellType==CellType.BOOLEAN){
			return cell.getBooleanCellValue();
		}else if(CharSequence.class.isAssignableFrom(returnType) && cellType == CellType.NUMERIC) {
			return df.format(cell.getNumericCellValue());
		}else if(!CharSequence.class.isAssignableFrom(returnType) || cellType != CellType.STRING) {
			log.error(String.format("单元格第  %d 行，第 %d 列，数据类型无法转换,属性类型： %s，单元格类型： %s", cell.getRowIndex(),cell.getColumnIndex(),returnType.getSimpleName(),cellType));
			return null;
		}
		return cell.getStringCellValue();
	}

	private Map<String,Object> getExcelFields(Class<?> clazz) {
		Map<String,Object> map = new HashMap<>();
		ReflectionUtils.doWithLocalFields(clazz, (field) -> {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if(null != excelField){
				Class<? extends ExcelFieldCallback<?>>[] callbackClazzs = excelField.readCallback();
				if(callbackClazzs.length != 0){
					try {
						map.put(excelField.title(), callbackClazzs[0].getConstructor().newInstance());
					} catch (Exception e) {
						throw new IllegalArgumentException(e);
					}
				}else{
					String fieldName = field.getName();
					PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Assert.notNull(propertyDescriptor, String.join("", clazz.toString()," Field ",fieldName," Not gett sett."));
					map.put(excelField.title(),propertyDescriptor.getWriteMethod());
				}
			}
		});
		return map;
	}

	public static enum Format {
		XLT,XLS,XLSX;

		public static Format nameOf(String name){
			if(StringUtils.isNotBlank(name)){
				return valueOf(name.toUpperCase());
			}
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		if(null != workbook){
			workbook.close();
		}
	}

}
