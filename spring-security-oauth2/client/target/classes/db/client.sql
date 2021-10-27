/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : client

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-01-06 16:42:43
*/

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `client`;

CREATE DATABASE `client` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `client`;

-- ----------------------------
-- Table structure for client_authority
-- ----------------------------
DROP TABLE IF EXISTS `client_authority`;
CREATE TABLE `client_authority` (
    `id` int NOT NULL AUTO_INCREMENT,
    `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of client_authority
-- ----------------------------
INSERT INTO `client_authority` VALUES ('1', 'ADMIN');
INSERT INTO `client_authority` VALUES ('2', 'USER');

-- ----------------------------
-- Table structure for client_user
-- ----------------------------
DROP TABLE IF EXISTS `client_user`;
CREATE TABLE `client_user` (
   `id` int NOT NULL AUTO_INCREMENT,
   `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
   `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of client_user
-- ----------------------------
INSERT INTO `client_user` VALUES ('1', 'user', '$2a$10$OrIAbKrGOkpyNcGqTb1uyOV6nqqJlsfcrLhslbcBvelMuBYP9lYym');

-- ----------------------------
-- Table structure for client_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `client_user_authority`;
CREATE TABLE `client_user_authority` (
     `id` int NOT NULL AUTO_INCREMENT,
     `user_id` int DEFAULT NULL,
     `authority_id` int DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of client_user_authority
-- ----------------------------
INSERT INTO `client_user_authority` VALUES ('1', '1', '1');
INSERT INTO `client_user_authority` VALUES ('2', '1', '2');
