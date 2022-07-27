package com.taotao.admin.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("tb_storage")
public class Storage {

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	
	/**
	 * 存储商(1: 阿里云, 2:腾讯云, 3: 七牛云, 4: 华为云, 5:百度云)
	 */
	private String name;
	
	/**
	 * 域名
	 */
	private String domain;
	
	/**
	 * 端点(阿里云:EndPoint,腾讯云:Region, 七牛云:Region 华为云、百度云需要扩展)
	 */
	private String point;
	
	/**
	 * 桶
	 */
	private String bucket;
	
	/**
	 * 默认bucket桶中的前缀位置
	 */
	private String prefix;
	
	/**
	 * accessKey
	 */
	private String accessKey;
	
	/**
	 * secretKey
	 */
	private String secretKey;
	
	/**
	 * 创建时间
	 */
	private Date created;
	
}
