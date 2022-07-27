package com.taotao.admin.controller;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.captcha.Captcha;
import com.taotao.admin.captcha.GifCaptcha;
import com.taotao.admin.common.R;
import com.taotao.admin.common.S;
import com.taotao.admin.entity.SysUser;
import com.taotao.admin.entity.form.LoginForm;
import com.taotao.admin.service.ShiroService;
import com.taotao.admin.service.SysUserService;

/**
 * 管理系统登陆验证码控制器
 * @author eden
 * @time 2022年7月22日 上午11:36:39
 */
@RestController
@RequestMapping("/sys")
public class SysLoginController extends BaseController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private ShiroService shiroService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 获取图片码
	 * @param response
	 * @param uuid
	 * @throws Exception
	 */
	@GetMapping("/captcha.jpg")
	public void captcha(HttpServletResponse response, @NotBlank(message = "uuid不能为空!") @RequestParam String uuid) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/gif");
		Captcha captcha = new GifCaptcha(138,38,4);
		//输出
		captcha.out(response.getOutputStream());

		redisTemplate.opsForValue().set(uuid, captcha.text().toLowerCase(), 5, TimeUnit.MINUTES);
	}

	/**
	 * 登陆
	 * @param form
	 * @return
	 */
	@PostMapping("/login")
	public R login(@RequestBody LoginForm form) {
		String captcha = redisTemplate.opsForValue().get(form.getUuid());
		if(captcha == null || captcha.isBlank()) {
			return R.error(S.CODE_EXPIRE);
		}
		if(!captcha.equalsIgnoreCase(form.getCaptcha())) {
			return R.error(S.CODE_ERROR);
		}
		SysUser user = sysUserService.getByUserName(form.getUsername());
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error(S.USER_PWD_ERROR);
		}

		if(user.getStatus()) {
			return R.error(S.USER_INACTIVE);
		}
		String token = shiroService.createToken(user);
		return R.ok(token);
	}

	/**
	 * 退出登陆
	 * @param request
	 * @return
	 */
	@PostMapping("/logout")
	public R logout(HttpServletRequest request) {
		String token = request.getHeader("token");
		shiroService.remove(token);
		SecurityUtils.getSubject().logout();
		return R.ok();
	}

	public static void main(String[] args) {
		String hex = new Sha256Hash("adminYzcmCZNvbXocrsz9dm8e").toHex();
		System.out.println(hex);
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NTY0MDg1MjYsIm9yaWdfaWF0IjoxNjU2NDA0OTI2LCJwZXJtcyI6IiwsLCwsLCxzeXM6c2NoZWR1bGU6bGlzdCxzeXM6c2NoZWR1bGU6aW5mbyxzeXM6c2NoZWR1bGU6c2F2ZSxzeXM6c2NoZWR1bGU6dXBkYXRlLHN5czpzY2hlZHVsZTpkZWxldGUsc3lzOnNjaGVkdWxlOnBhdXNlLHN5czpzY2hlZHVsZTpyZXN1bWUsc3lzOnNjaGVkdWxlOnJ1bixzeXM6dXNlcjpsaXN0LHN5czp1c2VyOmluZm8sc3lzOnVzZXI6c2F2ZSxzeXM6cm9sZTpzZWxlY3Qsc3lzOnVzZXI6dXBkYXRlLHN5czpyb2xlOnNlbGVjdCxzeXM6dXNlcjpkZWxldGUsc3lzOnJvbGU6bGlzdCxzeXM6cm9sZTppbmZvLHN5czpyb2xlOnNhdmUsc3lzOm1lbnU6bGlzdCxzeXM6cm9sZTp1cGRhdGUsc3lzOm1lbnU6bGlzdCxzeXM6cm9sZTpkZWxldGUsc3lzOm1lbnU6bGlzdCxzeXM6bWVudTppbmZvLHN5czptZW51OnNhdmUsc3lzOm1lbnU6c2VsZWN0LHN5czptZW51OnVwZGF0ZSxzeXM6bWVudTpzZWxlY3Qsc3lzOm1lbnU6ZGVsZXRlLHN5czpjb25maWc6bGlzdCxzeXM6Y29uZmlnOmluZm8sc3lzOmNvbmZpZzpzYXZlLHN5czpjb25maWc6dXBkYXRlLHN5czpjb25maWc6ZGVsZXRlLHN5czpsb2c6bGlzdCIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.FCvzAP02w5NDdhEb89w_8_Uzi4EYs_ff7UnyoBJqdtE";
		System.out.println(token.length());
	}
}
