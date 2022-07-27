package com.taotao.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Version;
import com.taotao.admin.mapper.VersionMapper;
import com.taotao.admin.service.VersionService;

@Service("versionService")
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {

	@Override
	public P<Version> getVersionList(int pageNum, int pageSize) {
		IPage<Version> page = new Page<>(pageNum, pageSize);
		page(page);
		return new P<>(page.getTotal(), page.getRecords());
	}

	@Override
	public Version getNewVersion(Integer platform) {
		QueryWrapper<Version> query = new QueryWrapper<>();
		query.eq("platform", platform).orderByDesc("v").last("limit 1");
		return getOne(query);
	}

}
