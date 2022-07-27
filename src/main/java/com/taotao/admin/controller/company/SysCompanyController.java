package com.taotao.admin.controller.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.admin.common.R;
import com.taotao.admin.entity.Company;
import com.taotao.admin.service.CompanyService;

@RestController
@RequestMapping("/sys/company")
public class SysCompanyController {
	
	@Autowired
	private CompanyService companyService;

	@GetMapping("/list")
	public List<Company> list() {
		List<Company> list = companyService.getCompanyList();
		list.stream().forEach(c -> {
			if(c.getParentId() <= 0) {
				c.setParentName("总公司");
			}else{
				Company company = companyService.getById(c.getParentId());
				c.setParentName(company.getName());
			}
		});
		return list;
	}
	
	@GetMapping("/select")
	public List<Company> select(){
		List<Company> list = companyService.getCompanyList(0);
		Company c = new Company();
		c.setCompanyId(0);
		c.setName("总公司");
		c.setParentId(-1);
		list.add(c);
		return list;
	}
	
	@GetMapping("/info/{id}")
	public R info(@PathVariable Integer id) {
		Company company = companyService.getById(id);
		return R.ok(company);
	}
	
	@PostMapping("/save")
	public R save(@RequestBody Company company) {
		companyService.saveOrUpdate(company);
		return R.ok();
	}
	
	@PostMapping("/update")
	public R update(@RequestBody Company company) {
		companyService.saveOrUpdate(company);
		return R.ok();
	}
	
	@PostMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		companyService.delete(ids);
		return R.ok();
	}
}

