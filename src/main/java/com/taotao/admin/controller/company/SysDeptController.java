package com.taotao.admin.controller.company;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.common.R;

@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

	public R list() {
		return R.ok();
	}
}
