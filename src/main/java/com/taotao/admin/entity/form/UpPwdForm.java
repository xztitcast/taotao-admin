package com.taotao.admin.entity.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpPwdForm {
	
	@NotBlank(message = "原密码不能为空")
	private String password;
	
	@NotBlank(message = "新密码不能为空")
	private String newPassword;
}
