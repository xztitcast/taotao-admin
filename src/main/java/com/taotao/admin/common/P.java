package com.taotao.admin.common;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页
 * @author eden
 * 2022年5月11日
 * @param <T>
 */
@Getter
@Setter
public class P<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long total;
	
	private List<T> pageList;

	public P() {
		super();
	}

	public P(long total, List<T> pageList) {
		super();
		this.total = total;
		this.pageList = pageList;
	}
	
}
