package com.ss.crawler.business.impl;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.ss.crawler.business.InitBusiness;
import com.ss.crawler.storm.bolt.DownloadBolt;
import com.ss.crawler.storm.spout.TaskSpout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ss on 2016/1/18.
 */
@Service("initBusiness")
public class InitBusinessImpl implements InitBusiness {

    @Autowired
    private DownloadBolt downloadBolt;
    @Autowired
    private TaskSpout taskSpout;

    public void setDownloadBolt(DownloadBolt downloadBolt) {
        this.downloadBolt = downloadBolt;
    }

    public void setTaskSpout(TaskSpout taskSpout) {
        this.taskSpout = taskSpout;
    }

    @Override
    public void initCrawler() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("taskSender", taskSpout);
        builder.setBolt("downloadWeb", downloadBolt).shuffleGrouping("taskSender");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Crawler-Topo", conf, builder.createTopology());
//        cluster.shutdown();
    }
}
