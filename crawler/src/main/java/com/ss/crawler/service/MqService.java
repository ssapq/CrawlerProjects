package com.ss.crawler.service;

import java.io.Serializable;

/**
 * Created by ss on 2016/1/19.
 */
public interface MqService {

    public void sendMessage(String payload) throws Exception;

    public void receiver() throws Exception;

    public void sendMessage(Serializable payload) throws Exception;
}
