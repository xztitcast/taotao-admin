package com.taotao.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taotao.admin.entity.Dept;

public interface DeptDao extends JpaRepository<Dept, Long> {

}
