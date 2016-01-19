package com.ss.crawler.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.ss.crawler.model.Task;
import com.ss.crawler.service.MqService;
import com.ss.crawler.service.impl.MqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ss on 2016/1/19.
 */
@Service("downloadBolt")
public class DownloadBolt implements IRichBolt {
//
//    @Autowired
//    private MqService mqService;
//
//    public void setMqService(MqService mqService) {
//        this.mqService = mqService;
//    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

    }

    @Override
    public void execute(Tuple tuple) {
        Task task = (Task)tuple.getValue(0);
        try {
            MqService mqService = new MqServiceImpl();
            mqService.subMessage();
        }catch (Exception e){

        }
        System.out.print(task);
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
