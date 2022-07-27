package com.taotao.admin.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author JIANG
 */
public interface ExcelFieldCallback<T> {
	
	
	/**
	 * 回调
	 * @param book    excel文本对象
	 * @param row     行
	 * @param cell    行内单元格<p>列</p>
	 * @param object  单行数据对象
	 */
	void call(Workbook book,Row row,Cell cell,T object);
}
