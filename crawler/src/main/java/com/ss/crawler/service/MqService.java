package com.ss.crawler.service;

/**
 * Created by ss on 2016/1/19.
 */
public interface MqService {

    public void sender() throws Exception;

    public void receiver() throws Exception;
}
