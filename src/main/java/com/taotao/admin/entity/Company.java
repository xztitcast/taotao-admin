package com.taotao.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_company")
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id", nullable = false, columnDefinition = "公司ID")
	private Integer companyId;
	
	@Column(name = "parent_id", nullable = false, columnDefinition = "上级ID")
	private Integer parentId;
	
	@Column(name = "`name`", nullable = false, columnDefinition = "公司名称")
	private String name;
	
	@Column(name = "logo", nullable = false, columnDefinition = "公司LOGO")
	private String logo;
	
	@Transient
	private String parentName;

}
