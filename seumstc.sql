/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : seumstc

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2019-03-08 17:31:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(64) DEFAULT NULL,
  `location` varchar(64) DEFAULT NULL,
  `time` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `is_top` tinyint(4) DEFAULT NULL,
  `sponsor` varchar(128) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `content` text,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for `activity_comment`
-- ----------------------------
DROP TABLE IF EXISTS `activity_comment`;
CREATE TABLE `activity_comment` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `content` text,
  `status` tinyint(4) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `activity_like_dislike`
-- ----------------------------
DROP TABLE IF EXISTS `activity_like_dislike`;
CREATE TABLE `activity_like_dislike` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `is_like` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity_like_dislike
-- ----------------------------

-- ----------------------------
-- Table structure for `blog`
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `blog_abstract` text,
  `content` text,
  `image_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `key_word` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  `is_top` tinyint(4) DEFAULT NULL,
  `hot_score` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------

-- ----------------------------
-- Table structure for `blog_comment`
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) NOT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `content` text,
  `status` tinyint(4) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `blog_like_dislike`
-- ----------------------------
DROP TABLE IF EXISTS `blog_like_dislike`;
CREATE TABLE `blog_like_dislike` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `is_like` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_like_dislike
-- ----------------------------

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `entity_id` int(11) NOT NULL,
  `entity_type` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `entity_index` (`entity_id`,`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for `login_ticket`
-- ----------------------------
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_UNIQUE` (`ticket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_ticket
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` text,
  `created_date` datetime DEFAULT NULL,
  `has_read` int(11) DEFAULT NULL,
  `conversation_id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `conversation_index` (`conversation_id`),
  KEY `created_date` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `my_collection`
-- ----------------------------
DROP TABLE IF EXISTS `my_collection`;
CREATE TABLE `my_collection` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(32) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of my_collection
-- ----------------------------

-- ----------------------------
-- Table structure for `news`
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(64) DEFAULT NULL,
  `news_abstract` text,
  `content` text,
  `created_time` datetime DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_top` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------

-- ----------------------------
-- Table structure for `news_comment`
-- ----------------------------
DROP TABLE IF EXISTS `news_comment`;
CREATE TABLE `news_comment` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `content` text,
  `status` tinyint(4) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `news_like_dislike`
-- ----------------------------
DROP TABLE IF EXISTS `news_like_dislike`;
CREATE TABLE `news_like_dislike` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `is_like` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news_like_dislike
-- ----------------------------

-- ----------------------------
-- Table structure for `programming`
-- ----------------------------
DROP TABLE IF EXISTS `programming`;
CREATE TABLE `programming` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `ideas` text,
  `answer` text,
  `image_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  `is_top` tinyint(4) DEFAULT NULL,
  `hot_score` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of programming
-- ----------------------------

-- ----------------------------
-- Table structure for `programming_comment`
-- ----------------------------
DROP TABLE IF EXISTS `programming_comment`;
CREATE TABLE `programming_comment` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) NOT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `content` text,
  `status` tinyint(4) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of programming_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `programming_like_dislike`
-- ----------------------------
DROP TABLE IF EXISTS `programming_like_dislike`;
CREATE TABLE `programming_like_dislike` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `is_like` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of programming_like_dislike
-- ----------------------------

-- ----------------------------
-- Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` text,
  `image_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `key_word` varchar(255) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  `is_top` tinyint(4) DEFAULT NULL,
  `hot_score` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question
-- ----------------------------

-- ----------------------------
-- Table structure for `question_comment`
-- ----------------------------
DROP TABLE IF EXISTS `question_comment`;
CREATE TABLE `question_comment` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) NOT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `content` text,
  `status` tinyint(4) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `dislike_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `question_like_dislike`
-- ----------------------------
DROP TABLE IF EXISTS `question_like_dislike`;
CREATE TABLE `question_like_dislike` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  `is_like` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_like_dislike
-- ----------------------------

-- ----------------------------
-- Table structure for `system_message`
-- ----------------------------
DROP TABLE IF EXISTS `system_message`;
CREATE TABLE `system_message` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `has_read` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `entity_id` int(32) DEFAULT NULL,
  `entity_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_message
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(128) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `head_url` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `hometown` varchar(255) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL,
  `department` varchar(64) DEFAULT NULL,
  `degree` varchar(64) DEFAULT NULL,
  `hobby` varchar(255) DEFAULT NULL,
  `qq` varchar(64) DEFAULT NULL,
  `wechat` varchar(64) DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `token` varchar(128) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `follown_count` int(16) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `rank_score` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `user_follower`
-- ----------------------------
DROP TABLE IF EXISTS `user_follower`;
CREATE TABLE `user_follower` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `relationship_id` varchar(32) DEFAULT NULL,
  `is_friend` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_follower
-- ----------------------------

-- ----------------------------
-- Table structure for `user_message`
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` varchar(256) DEFAULT NULL,
  `has_read` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `conversation_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_message
-- ----------------------------
