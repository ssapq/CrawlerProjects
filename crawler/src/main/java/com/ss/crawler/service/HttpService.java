package com.ss.crawler.service;

import com.ss.crawler.model.CrawlerRequest;
import com.ss.crawler.model.CrawlerResponse;

/**
 * Created by ss on 2016/1/21.
 */
public interface HttpService {

    /**
     * HTTP请求
     * @param crawlerRequest
     * @return
     * @throws Exception
     */
    public CrawlerResponse doRequest(CrawlerRequest crawlerRequest) throws Exception;
}
