package com.ss.crawler.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.ss.crawler.model.Task;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ss on 2016/1/18.
 */
@Service("taskSpout")
public class TaskSpout  implements IRichSpout {

    private SpoutOutputCollector collector;
    private boolean completed = false;
    private TopologyContext context;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("taskField"));
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
        if(completed) {
            return;
        }
        Task newTask = new Task();
        newTask.setUrl("www.baidu.com");
        this.collector.emit(new Values(newTask));
        completed = true;
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
        this.context = topologyContext;
        this.collector = spoutOutputCollector;
    }
}
