package com.taotao.admin.common.utils;

import org.springframework.context.ApplicationContext;

/**
 * spring 容器工具类
 * @author xiangzuotao
 * @date 2018年4月16日
 * @version 1.0.0
 */
public abstract class SpringContextUtil {

	private static ApplicationContext applicationContext;
	
	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
	
	/**
	 * 根据类型获取对象
	 * @param cls 
	 * @return T
	 */
	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}
	
	/**
	 * 根据id名称和类型获取对象
	 * @param name
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> cls) {
		return applicationContext.getBean(name, cls);
	}
	
	/**
	 * 根据id名称获取对象
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}
}
