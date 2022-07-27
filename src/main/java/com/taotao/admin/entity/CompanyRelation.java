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
@Table(name = "tb_company_relation")
public class CompanyRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "主键ID")
	private Long id;
	
	@Column(name = "ancestor", nullable = false, columnDefinition = "根节点ID")
	private Integer ancestor;
	
	@Column(name = "descendant", nullable = false, columnDefinition = "子孙节点ID")
	private Integer descendant;
	
	@Column(name = "distance", nullable = false, columnDefinition = "根节点到子节点的距离")
	private Integer distance;

}
