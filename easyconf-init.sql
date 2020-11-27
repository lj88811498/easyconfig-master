/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : easyconf

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2020-09-16 15:25:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL COMMENT '所属工程id',
  `env_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '环境名称，建议英文',
  `env_type_id` bigint(20) NOT NULL COMMENT '所属环境类型id',
  `content` text COLLATE utf8_bin,
  `last_modify_uid` bigint(20) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `delete_flag` smallint(6) NOT NULL DEFAULT '0' COMMENT '1 删除, 0 未删除',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建者uid',
  `format` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '配置内容格式 properties , yml等',
  `version` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of config
-- ----------------------------

-- ----------------------------
-- Table structure for config_history
-- ----------------------------
DROP TABLE IF EXISTS `config_history`;
CREATE TABLE `config_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_id` bigint(20) NOT NULL COMMENT '配置表的id',
  `version` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '版本号',
  `format` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '格式',
  `content` text COLLATE utf8_bin,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of config_history
-- ----------------------------

-- ----------------------------
-- Table structure for config_user
-- ----------------------------
DROP TABLE IF EXISTS `config_user`;
CREATE TABLE `config_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'MD5加密',
  `fullname` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `is_revise` smallint(6) DEFAULT NULL COMMENT '0: 初始化密码   1: 修改后密码',
  `email` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱地址，接收验证码等',
  `is_admin` smallint(6) NOT NULL DEFAULT '0' COMMENT '1是管理员，0不是管理员',
  `enabled` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of config_user
-- ----------------------------
INSERT INTO `config_user` VALUES ('1', 'admin', '96e79218965eb72c92a549dd5a330112', '系统管理员', null, null, '0', '450416064@qq.com', '1', '');

-- ----------------------------
-- Table structure for env_type
-- ----------------------------
DROP TABLE IF EXISTS `env_type`;
CREATE TABLE `env_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8_bin NOT NULL,
  `public_flag` smallint(6) NOT NULL DEFAULT '1' COMMENT '1开放环境，0机密环境',
  `secret` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '获取环境配置需要的安全密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of env_type
-- ----------------------------
INSERT INTO `env_type` VALUES ('3', '测试环境', '1', '');
INSERT INTO `env_type` VALUES ('4', '开发环境', '1', '');
INSERT INTO `env_type` VALUES ('6', '预发布环境', '0', '12345678');
INSERT INTO `env_type` VALUES ('7', '生产环境', '0', 'youedata');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '创作者id',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '工程英文名称',
  `owner` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人',
  `contact` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人联系方式',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '工程说明',
  `label` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '标签信息，可用于分组查询',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('17', '3', 'moneky', null, 'moneky', 'test', 'moneky', '2019-08-05 09:40:11');
INSERT INTO `project` VALUES ('18', '3', 'my-monkey', null, '18011111111', '测试', '测试moneky', '2019-06-20 16:18:46');
INSERT INTO `project` VALUES ('19', '3', 'paas', null, '18030000000', 'paas后台', 'paas_V1.0', '2019-07-31 16:43:53');
INSERT INTO `project` VALUES ('21', '3', 'paas-cloud', null, 'moneky', 'paas云服务', 'moneky', '2019-07-26 15:06:50');
INSERT INTO `project` VALUES ('22', '3', 'dataVisualization', null, '', 'dataVisualization', '', '2019-11-14 14:41:16');
INSERT INTO `project` VALUES ('23', '3', 'operation', null, '', '运维管理平台', '', '2019-12-09 18:23:09');
INSERT INTO `project` VALUES ('24', '3', 'dataos', null, '', '数据操作系统', '', '2019-12-10 14:10:48');
INSERT INTO `project` VALUES ('25', '3', 'DB', null, '', '升级数据库', '', '2020-01-15 09:53:58');

-- ----------------------------
-- Table structure for project_member
-- ----------------------------
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE `project_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `project_id` bigint(20) NOT NULL COMMENT '工程id',
  `is_creator` smallint(6) NOT NULL DEFAULT '0' COMMENT '1 创建者，0 非创建者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of project_member
-- ----------------------------
