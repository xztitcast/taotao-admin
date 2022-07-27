package com.taotao.admin.service;

import java.util.List;

import com.taotao.admin.entity.Company;

public interface CompanyService {
	
	void saveOrUpdate(Company entity);
	
	void delete(Integer... ids);
	
	Company getById(Integer id);

	List<Company> getCompanyList();
	
	List<Company> getCompanyList(Integer parentId);
}
