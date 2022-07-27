package com.taotao.admin.ueditor.define;

import java.util.Map;

public class MIMEType {
	
	public static final Map<String, String> types = Map.of("image/gif", ".gif", "image/jpeg", ".jpg", "image/jpg", ".jpg", "image/png", ".png", "image/bmp", ".bmp");
	
	public static String getSuffix ( String mime ) {
		return MIMEType.types.get( mime );
	}
	
}
