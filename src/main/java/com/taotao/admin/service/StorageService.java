package com.taotao.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Storage;

/**
 * 存储接口
 * @author eden
 * @time 2022年7月22日 下午3:38:21
 */
public interface StorageService extends IService<Storage>{

	/**
	 * 根据name查询
	 * @param name
	 * @return
	 */
	Storage getByName(String name);
	
	/**
	 * 获取存储列表
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	P<Storage> getStorageList(int pageNum, int pageSize, Integer name);
}
