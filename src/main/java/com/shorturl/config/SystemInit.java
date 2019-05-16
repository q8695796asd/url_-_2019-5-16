package com.shorturl.config;

import com.shorturl.dao.ShortUrlDao;
import com.shorturl.entity.ShortUrl;
import com.shorturl.service.ShortUrlOpsService;
import com.shorturl.service.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;

/**
 * 启动运行类
 * 将数据库中的数据同步到缓存,提高并发量
 */
@Component
public class SystemInit implements CommandLineRunner {
    @Autowired
    private ShortUrlOpsService shortUrlOpsService;
    @Autowired
    private ShortUrlDao shortUrlDao;
    @Autowired
    private ShortUrlService shortUrlService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void run(String... strings) {
        // 取出数据库记录数
        logger.info("准备数据库短网址信息到缓存...");
        long count = shortUrlDao.count();
        // 计算循环次数,5000条一次
        int times = (int) Math.ceil(count / 5000.0);
        // 短网址列表
        List<ShortUrl> shortUrlList = new ArrayList<>((int) count);
        logger.info(String.format("一共有%d条记录，需操作%d次...", count, times));

        for (int i = 0; i < times; i++) {
            shortUrlList.addAll(shortUrlDao.findAll(new PageRequest(i * 5000, (i + 1) * 5000)).getContent());
        }

        Map<String, String> counts = new HashMap<>(shortUrlList.size());
        Map<String, String> urls = new HashMap<>(shortUrlList.size());
        logger.info("组装缓存map信息...");
        // 放入map
        shortUrlList.forEach(
                url -> {
                    counts.put(url.getTag(), String.valueOf(url.getCount()));
                    urls.put(url.getTag(), url.getUrl());
                }
        );

        shortUrlOpsService.clearCount();
        shortUrlOpsService.clearUrl();

        // 放入缓存记录
        shortUrlOpsService.putCount(counts);
        shortUrlOpsService.putUrls(urls);
        logger.info("短网址信息存放缓存完毕...");
    }


    /**
     * 系统关闭前执行任务
     */
    @PreDestroy
    public void destroy() {
        // 保存访问计数
        shortUrlService.saveCount2Db();
    }
}
