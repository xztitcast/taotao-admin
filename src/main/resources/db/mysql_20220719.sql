DROP TABLE IF EXISTS `tb_company`;
CREATE TABLE `tb_company`(
	`company_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '公司ID',
	`parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级公司ID, parent_id=0为总公司',
	`name` varchar(50) NOT NULL COMMENT '公司名称',
	`logo` varchar(255) DEFAULT NULL COMMENT '公司LOGO'
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='公司信息表';

DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept`(
	`dept_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
	`company_id` int(11) NOT NULL COMMENT '公司ID',
	`name` varchar(25) NOT NULL COMMENT '部门名称',
	INDEX(`company_id`)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='部门表';

DROP TABLE IF EXISTS `tb_company_relation`;
CREATE TABLE `tb_company_relation`(
	`id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`ancestor` int(11) NOT NULL COMMENT '根节点ID',
	`descendant` int(11) NOT NULL COMMENT '子节点ID',
	`distance` int(11) NOT NULL COMMENT '根节点到子节点的距离'
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='公司关系维护闭包表';