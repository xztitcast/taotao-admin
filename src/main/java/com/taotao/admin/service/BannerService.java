package com.taotao.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.admin.common.P;
import com.taotao.admin.entity.Banner;

public interface BannerService extends IService<Banner> {

	P<Banner> getBannerlList(int pageNum, int pageSize);
}
