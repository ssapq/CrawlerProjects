package com.ss.crawler.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.ss.crawler.model.CrawlerRequest;
import com.ss.crawler.model.CrawlerResponse;
import com.ss.crawler.model.Task;
import com.ss.crawler.service.HttpService;
import com.ss.crawler.service.impl.HttpServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ss on 2016/1/19.
 */
@Service("downloadBolt")
public class DownloadBolt implements IRichBolt {

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

    }

    @Override
    public void execute(Tuple tuple) {
        Task task = (Task)tuple.getValue(0);
        if(task == null){
            return;
        }

        String url = task.getUrl();
        if(StringUtils.isBlank(url)){
            return;
        }

        HttpService httpService = new HttpServiceImpl();
        CrawlerRequest request = new CrawlerRequest();
        request.setUrl(url);
        try {
            CrawlerResponse response = httpService.doRequest(request);
            if(response == null){
                return;
            }

            String html = response.getResponse();
            System.out.print(html);
        }catch (Exception e){

        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
