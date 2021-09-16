/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : oauth2

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-01-06 16:42:43
*/

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `oauth2`;

CREATE DATABASE `oauth2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `oauth2`;

-- ----------------------------
-- Table structure for clientdetails
-- ----------------------------
DROP TABLE IF EXISTS `clientdetails`;
CREATE TABLE `clientdetails` (
  `app_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `app_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `grant_types` varchar(256) DEFAULT NULL,
  `redirect_url` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `auto_approve_scopes` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` longblob,
  `authentication_id` varchar(256) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` longblob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
  `user_id` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- oauth_client_details 中 client_id = messaging-client 的原始密码是 secret
-- ++++++++++++++++++++++++++++

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('messaging-client', null, '$2a$10$YMmBTVIDGs7JC.W3O9l4U.fR/xjFm.xlC6ij2z3hTbpEiyyx91aji', 'READ,WRITE', 'authorization_code,refresh_token', 'http://localhost', null, '25200', '25200', null, 'READ,WRITE');
INSERT INTO `oauth_client_details` VALUES ('client', null, '$2a$10$V8AI03CUWRQp9FF3PA9PYupmvIZ3KizZpTAQK41r.Vm7A2ZNMyImG', 'read,write', 'authorization_code,refresh_token', 'http://localhost:8080/authorized', null, '25200', '25200', null, 'read,write');

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` longblob,
  `authentication_id` varchar(256) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` longblob,
  `authentication` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- oauth_extend_authority, oauth_extend_user_authority, oauth_extend_user_details 是业务表，主要用于存储登录认证的账号、密码、用户角色
-- oauth_extend_user_details 中账号 admin 的原始密码是 admin
-- ++++++++++++++++++++++++++++

-- ----------------------------
-- Table structure for oauth_extend_authority
-- ----------------------------
DROP TABLE IF EXISTS `oauth_extend_authority`;
CREATE TABLE `oauth_extend_authority` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of oauth_extend_authority
-- ----------------------------
INSERT INTO `oauth_extend_authority` VALUES ('1', 'ADMIN');
INSERT INTO `oauth_extend_authority` VALUES ('2', 'USER');

-- ----------------------------
-- Table structure for oauth_extend_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `oauth_extend_user_authority`;
CREATE TABLE `oauth_extend_user_authority` (
   `id` int NOT NULL AUTO_INCREMENT,
   `user_id` int DEFAULT NULL,
   `authority_id` int DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of oauth_extend_user_authority
-- ----------------------------
INSERT INTO `oauth_extend_user_authority` VALUES ('1', '1', '1');
INSERT INTO `oauth_extend_user_authority` VALUES ('2', '1', '2');

-- ----------------------------
-- Table structure for oauth_extend_user_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_extend_user_details`;
CREATE TABLE `oauth_extend_user_details` (
 `id` int NOT NULL AUTO_INCREMENT,
 `username` varchar(255) DEFAULT NULL,
 `password` varchar(255) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of oauth_extend_user_details
-- ----------------------------
INSERT INTO `oauth_extend_user_details` VALUES ('1', 'admin', '$2a$10$qVF14zh7oxM6TT8blY6tN.G0zGG4MOHCNzhYAoEaGt/ZNYTydBWhi');
