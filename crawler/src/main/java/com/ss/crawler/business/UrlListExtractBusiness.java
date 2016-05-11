package com.ss.crawler.business;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by ss on 2016/5/8.
 */
public interface UrlListExtractBusiness extends MessageListener {

    @Override
    void onMessage(Message message);
}
