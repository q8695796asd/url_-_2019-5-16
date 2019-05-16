package com.shorturl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 短连接表
 *
 */
@Entity
public class ShortUrl {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private int id;
    /**
     * 短网址标记
     */
    @Column(nullable = false, unique = true, length = 16)
    private String tag;
    /**
     * 指向url
     */
    @Column(nullable = false, columnDefinition = "text")
    private String url;
    /**
     * 访问计数
     */
    @Column(nullable = false)
    private int count;
    /**
     * 创建ip
     */
    private String createIp;

    /**
     * 创建时间
     */
    private Date createDate;


    public ShortUrl() {
    }

    public ShortUrl(String tag, String url, int count, String createIp, Date createDate) {
        this.tag = tag;
        this.url = url;
        this.count = count;
        this.createIp = createIp;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ShortUrl{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", url='" + url + '\'' +
                ", count=" + count +
                ", createIp=" + createIp +
                ", createDate=" + createDate +
                '}';
    }
}
