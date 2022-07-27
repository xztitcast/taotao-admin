package com.taotao.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taotao.admin.entity.Company;

public interface CompanyDao extends JpaRepository<Company, Integer> {

	List<Company> findByParentId(Integer parentId);
}
