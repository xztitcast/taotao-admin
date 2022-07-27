package com.taotao.admin.controller;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.annotation.Log;
import com.taotao.admin.common.Constant;
import com.taotao.admin.common.R;
import com.taotao.admin.entity.SysMenu;
import com.taotao.admin.service.ShiroService;
import com.taotao.admin.service.SysMenuService;

/**
 * 关系系统菜单控制器
 * @author eden
 * @time 2022年7月22日 上午11:37:38
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private ShiroService shiroService;
	
	/**
	 * 菜单权限导航
	 * @return
	 */
	@GetMapping("/nav")
	public R nav() {
		List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}
	
	/**
	 * 菜单列表配合前台代码转换成Tree树
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenu> list(){
		List<SysMenu> menuList = sysMenuService.list();
		menuList.forEach(m -> {
			SysMenu menu = sysMenuService.getById(m.getParentId());
			if(menu == null && m.getParentId() == 0) {
				m.setParentName("一级菜单");
			}else {
				m.setParentName(menu.getName());
			}
		});
		return menuList;
	}
	
	/**
	 * 过滤权限按钮只获取菜单与目录列表配合前台转换Tree树
	 * @return
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select() {
		List<SysMenu> menuList = sysMenuService.getNotButtonList();
		
		SysMenu root = new SysMenu();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	

	/**
	 * 单个菜单信息
	 * @param menuId
	 * @return
	 */
	@GetMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenu menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}
	
	/**
	 * 保存或者更新
	 * (必须同时具有保存和更新权限才可以调用此方法)
	 * @param menu
	 * @return
	 */
	@Log("保存或修改菜单")
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions({"sys:menu:save", "sys:menu:update"})
	public R saveOrUpdate(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
		
		sysMenuService.saveOrUpdate(menu);
		
		return R.ok();
	}
	
	/**
	 * 删除菜单
	 * (根菜单id<=31为系统默认框架的菜单不允许删除)
	 * @param menuId
	 * @return
	 */
	@Log("删除菜单")
	@PostMapping("/delete/{menuId}")
	@RequiresPermissions("sys:menu:delete")
	public R delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return R.error("系统框架菜单不能被删除!");
		}

		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.getListParentId(menuId);
		if(menuList.size() > 0){
			return R.error("请删除对应的菜单或者按钮再删除主菜单!");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenu menu){
		if(menu.getName() == null || menu.getName().isBlank()){
			throw new RuntimeException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new RuntimeException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(menu.getUrl() == null || menu.getUrl().isBlank()){
				throw new RuntimeException("菜单URL不能为空");
			}
		}
		
		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RuntimeException("上级菜单只能为目录类型");
			}
			return ;
		}
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RuntimeException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
	
}
