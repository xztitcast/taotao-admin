package com.taotao.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Banner;
import com.taotao.admin.mapper.BannerMapper;
import com.taotao.admin.service.BannerService;

@Service("bannerService")
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

	@Override
	public P<Banner> getBannerlList(int pageNum, int pageSize) {
		IPage<Banner> page = new Page<>(pageNum, pageSize);
		QueryWrapper<Banner> query = new QueryWrapper<>();
		query.orderByDesc("sort_num");
		page(page, query);
		return new P<>(page.getTotal(), page.getRecords());
	}

}
