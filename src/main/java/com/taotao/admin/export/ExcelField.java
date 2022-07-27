package com.taotao.admin.export;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author JIANG
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
	/**
	 * excel排列顺序
	 * @return
	 */
	int order() default 0;
	/**
	 * excel表头
	 * @return
	 */
	String title();
	/**
	 * 自定义单元格写入
	 * @return
	 */
	Class<? extends ExcelFieldCallback<?>>[] writeCallback() default {};
	/**
	 * 自定义单元格读取
	 * @return
	 */
	Class<? extends ExcelFieldCallback<?>>[] readCallback() default {};
}
