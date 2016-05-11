package com.ss.crawler.service.impl;

import com.ss.crawler.service.MqService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by ss on 2016/1/19.
 */
@Service("mqService")
public class MqServiceImpl implements MqService {

    @Autowired
    @Qualifier("htmlJmsTemplate")
    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage(String payload) throws Exception{
        if(StringUtils.isBlank(payload)){
            throw new Exception("payload is null");
        }

        try {
            jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    Message message = session.createTextMessage("eee");
                    return message;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(final Serializable payload) throws Exception {
        if(payload == null){
            throw new Exception("object is null");
        }

        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createObjectMessage(payload);
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
