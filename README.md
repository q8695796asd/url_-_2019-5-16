## 主要参考  https://github.com/Cheivin/short-url.git

## 使用框架
Springboot+SpringDataJpa+Redis+Mysql+LayUi

## 部署方式
Redis,Mysql默认本地,主要配置放在application.yml


## mysql
    DROP TABLE IF EXISTS `short_url`;
    CREATE TABLE `short_url` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `count` int(11) NOT NULL COMMENT '访问计数',
      `create_date` datetime DEFAULT NULL COMMENT '创建时间',
      `create_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建ip',
      `tag` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短链接',
      `url` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '长链接',
      PRIMARY KEY (`id`),
      UNIQUE KEY `UK_c8xk0tec9hqixart0gq8ngfog` (`tag`)
    )
## redis
    shortUrl        hash
    shortUrlCount   hash

