package com.ss.crawler.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ss on 2016/1/18.
 */
@Service("taskSpout")
public class TaskSpout  implements IRichSpout {

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void fail(Object o) {

    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void nextTuple() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void close() {

    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {

    }
}
