package com.taotao.admin.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author eden
 * @time 2022年7月24日 下午2:59:29
 */
@Getter
@Setter
@TableName("tb_version")
public class Version {
	
	@TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 版本号
     */
    private String v;

    /**
     * 是否为强制升级版本  1 是  2 否
     */
    private Boolean isCompel;

    
    /**
     * 状态是否上架 1 是  2 否
     */
    private Boolean status;

    /**
     * 下载链接
     */
    private String url;

    /**
     * 平台  1 安卓  2 IOS
     */
    private Integer platform;
    
    /**
     * 说明
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private Date created;
    
    public enum Platform {
    	/**
    	 * 安卓
    	 */
    	ANDROID (1),
    	/**
    	 * 苹果
    	 */
    	IOS (2);
    	private int value;
    	
    	Platform (int value) {
    		this.value = value;
    	}
    	
    	public int value () {
    		return this.value;
    	}
    }
    
    /**
     * 是否强制更新
     */
    public enum CompelStatus {
    	/**
    	 * 强制更新
    	 */
    	FORCE_UPDATE(1),
    	/**
    	 * 非强制更新
    	 */
    	NON_FORCE_UPDATE(2);
    	
    	private int value;
    	
    	CompelStatus (int value) {
    		this.value = value;
    	}
    	
    	public int value () {
    		return this.value;
    	}
    }
    
    /**
     * 状态枚举类
     * @author XiePeng
     *
     */
    public enum Status {
    	
    	/**
    	 * 未发布
    	 */
    	UNPUBLISH(1),
    	
    	/**
    	 * 已发布
    	 */
    	PUBLISTED(2);
    	
    	private int value;
    	
    	Status (int value) {
    		this.value = value;
    	}
    	
    	public int value() {
    		return this.value;
    	}
    	
    }

}