package com.ss.crawler.business.impl;

import backtype.storm.tuple.Values;
import com.ss.crawler.business.DownloadBusiness;
import com.ss.crawler.model.CrawlerRequest;
import com.ss.crawler.model.CrawlerResponse;
import com.ss.crawler.model.Task;
import com.ss.crawler.service.HttpService;
import com.ss.crawler.service.MqService;
import com.ss.crawler.service.MqSubscriptionService;
import com.ss.crawler.service.impl.HttpServiceImpl;
import com.ss.crawler.service.impl.MqSubscriptionServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 * Created by ss on 2016/5/8.
 */
public class DownloadBusinessImpl implements DownloadBusiness {

    @Autowired
    private MqService mqService;
    @Autowired
    private HttpService httpService;

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void setMqService(MqService mqService) {
        this.mqService = mqService;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                Task task = (Task)((ObjectMessage) message).getObject();
                if(task == null){
                    return;
                }

                String url = task.getUrl();
                if(StringUtils.isBlank(url)){
                    return;
                }

                CrawlerRequest request = new CrawlerRequest();
                request.setUrl(url);
                try {
                    CrawlerResponse response = httpService.doRequest(request);
                    if(response == null){
                        return;
                    }

                    String html = response.getResponse();
                    mqService.sendMessage(html);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
