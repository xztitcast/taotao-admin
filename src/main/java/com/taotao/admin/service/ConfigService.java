package com.taotao.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysConfig;

public interface ConfigService extends IService<SysConfig> {

	P<SysConfig> getConfigList(int pageNum, int pageSize, String paramKey);
}
