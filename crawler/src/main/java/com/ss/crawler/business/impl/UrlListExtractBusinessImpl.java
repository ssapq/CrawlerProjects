package com.ss.crawler.business.impl;

import com.ss.crawler.business.UrlListExtractBusiness;
import com.ss.crawler.model.CrawlerRequest;
import com.ss.crawler.model.CrawlerResponse;
import com.ss.crawler.model.Task;
import com.ss.crawler.service.MqService;
import com.ss.crawler.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 2016/5/8.
 */
public class UrlListExtractBusinessImpl implements UrlListExtractBusiness {

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                String html = (String)((ObjectMessage) message).getObject();
                if(StringUtils.isBlank(html)){
                    return;
                }

                Document doc = Jsoup.parse(html);
                if(doc == null){
                    return;
                }

                List<String> linkHrefList = new ArrayList<String>();
                List<String> linkTextList = new ArrayList<String>();

                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    if(StringUtils.isBlank(linkHref)){
                        continue;
                    }
                    linkHrefList.add(linkHref);

                    String linkText = link.text();
                    if(StringUtils.isNoneBlank(linkText)){
                        linkTextList.add(linkText);
                    }
                }

                System.out.print(linkHrefList.size());
                MqService mqService = (MqService) SpringUtil.getBean("mqService");

                try {
                    mqService.sendMessage("");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }catch (Exception e){

            }
        }
        else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }

}
