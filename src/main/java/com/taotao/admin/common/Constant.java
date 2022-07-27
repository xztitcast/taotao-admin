package com.taotao.admin.common;

public interface Constant {

	/**
	 * 系统
	 * @author eden
	 * @time 2022年7月22日 下午3:48:58
	 */
	public interface Sys {
		
		int SUPER_ADMIN = 1;
	}
	
	/**
	 * 菜单类型
	 * @author eden
	 * @time 2022年7月22日 下午3:48:53
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * @author eden
     * @time 2022年7月22日 下午3:48:41
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * 云服务商
     * @author eden
     * @time 2022年7月22日 下午3:48:37
     */
    public interface Cloud {

    	/**
    	 * 阿里云
    	 */
    	int ALIYUN = 1;
    	
    	/**
    	 * 腾讯云
    	 */
    	int QCLOUD = 2;
    	
    	/**
    	 * 七牛云
    	 */
    	int QINIU = 3;
    	
    	/**
    	 * 华为云
    	 */
    	int HUAWEI = 4;
    	
    	/**
    	 * 百度云
    	 */
    	int BAIDU = 5;
    	
    }
}
