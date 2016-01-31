package com.ss.crawler.service.impl;

import com.ss.crawler.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by ss on 2016/1/19.
 */
@Service("mqService")
public class MqServiceImpl implements MqService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sender() throws Exception{
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage("test");
                return message;
            }
        });
    }

    @Override
    public void receiver() throws Exception{
        Message message  = jmsTemplate.receive();
           System.out.println("reviced msg is:" + message);
    }

}
