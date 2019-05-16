package com.shorturl.dao;

import com.shorturl.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 短连接dao层操作
 *
 */
public interface ShortUrlDao extends JpaRepository<ShortUrl, Integer> {
    /**
     * 根据短连接获取数据库信息
     *
     * @param tag 短连接
     * @return 数据记录
     */
    ShortUrl findByTag(String tag);

    List<ShortUrl> findByTagIn(List<String> tagList);

}
