package com.taotao.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Version;

/**
 * app 版本控制接口
 * @author eden
 * @time 2022年7月24日 下午3:02:41
 */
public interface VersionService extends IService<Version> {

	/**
	 * 获取所有的版本控制列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	P<Version> getVersionList(int pageNum, int pageSize);
	
	/**
	 * 根据平台获取新的版本
	 * @param platform
	 * @return
	 */
	Version getNewVersion(Integer platform);
}
