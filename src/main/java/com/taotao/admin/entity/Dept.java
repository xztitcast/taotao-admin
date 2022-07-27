package com.taotao.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_dept")
public class Dept implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dept_id", nullable = false, columnDefinition = "部门ID")
	private Long deptId;
	
	@Column(name = "company_id", nullable = false, columnDefinition = "公司ID")
	private Integer companyId;
	
	@Column(name = "`name`", nullable = false, columnDefinition = "部门名称")
	private String name;
}
