package com.taotao.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.admin.dao.CompanyDao;
import com.taotao.admin.entity.Company;
import com.taotao.admin.service.CompanyService;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyDao companyDao;
	
	@Override
	@Transactional
	public void saveOrUpdate(Company entity) {
		companyDao.saveAndFlush(entity);
	}
	
	@Override
	public void delete(Integer... ids) {
		companyDao.deleteAllById(Arrays.asList(ids));
	}
	
	@Override
	public Company getById(Integer id) {
		return companyDao.findById(id).orElseThrow();
	}

	@Override
	public List<Company> getCompanyList() {
		return companyDao.findAll();
	}

	@Override
	public List<Company> getCompanyList(Integer parentId) {
		return companyDao.findByParentId(parentId);
	}

}
