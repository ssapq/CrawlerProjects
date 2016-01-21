package com.ss.crawler.model;

/**
 * Created by ss on 2014/9/16.
 * 请求bean
 */
public class CrawlerResponse {

    private String response;
    private int status;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
