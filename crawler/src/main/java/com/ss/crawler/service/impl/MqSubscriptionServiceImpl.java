package com.ss.crawler.service.impl;

import com.ss.crawler.service.MqSubscriptionService;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by ss on 2016/4/19.
 */
@Service("mqSubscriptionService")
public class MqSubscriptionServiceImpl implements MqSubscriptionService {

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                System.out.println(((TextMessage) message).getText());
            }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }
}
