package com.taotao.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.SysConfig;
import com.taotao.admin.mapper.SysConfigMapper;
import com.taotao.admin.service.ConfigService;

@Service("sysConfigService")
public class ConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ConfigService {

	@Override
	public P<SysConfig> getConfigList(int pageNum, int pageSize, String paramKey) {
		IPage<SysConfig> page = new Page<>(pageNum, pageSize);
		QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
		wrapper.eq(paramKey !=null && !paramKey.isBlank(), "param_key", paramKey).orderByDesc("created");
		page(page, wrapper);
		return new P<>(page.getTotal(), page.getRecords());
	}

}
