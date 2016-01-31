package com.ss.crawler.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ss.crawler.service.MqService;
import com.ss.crawler.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2016/1/24.
 */
@Service("urlListExtractBolt")
public class UrlListExtractBolt  implements IRichBolt {

    private OutputCollector controller;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector outputCollector) {
        this.controller = outputCollector;
    }

    @Override
    public void execute(Tuple input) {
        String html = input.getString(0);
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
        MqService mqService = (MqService)SpringUtil.getBean("mqService");

        try {
            mqService.sender();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("urlList"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
