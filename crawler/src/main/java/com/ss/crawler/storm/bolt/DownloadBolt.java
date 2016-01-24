package com.ss.crawler.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
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

    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
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
            collector.emit(tuple, new Values(html));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("mainHtml"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
