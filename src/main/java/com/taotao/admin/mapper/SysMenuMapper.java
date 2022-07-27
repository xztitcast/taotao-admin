package com.taotao.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.admin.entity.SysMenu;

public interface SysMenuMapper extends BaseMapper<SysMenu>{

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @return
     */
	List<SysMenu> selectListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenu> selectNotButtonList();
}