package com.ss.crawler.model;

import java.io.Serializable;

/**
 * Created by ss on 2016/1/17.
 * 采集任务
 */
public class Task implements Serializable{

    private Long id;
    private Integer type;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
