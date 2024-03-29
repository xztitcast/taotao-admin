package com.taotao.admin.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("tb_banner")
public class Banner {

	@TableId(type = IdType.AUTO)
	private Integer id;
    /**
     * 图片地址
     */
    private String url;
    
    /**
     * 图片内容显示
     */
    private String content;
    
    /**
     * 是否展示：0 否，1 是
     */
    private Boolean isShow;
    
    /**
     * 排序
     */
    private Integer sorted;
    /**
     * 创建时间
     */
    private Date created;
}
