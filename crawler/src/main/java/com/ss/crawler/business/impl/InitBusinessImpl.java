package com.ss.crawler.business.impl;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.ss.crawler.business.InitBusiness;
import com.ss.crawler.storm.bolt.DownloadBolt;
import com.ss.crawler.storm.bolt.UrlListExtractBolt;
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
    @Autowired
    private UrlListExtractBolt urlListExtractBolt;

    public void setDownloadBolt(DownloadBolt downloadBolt) {
        this.downloadBolt = downloadBolt;
    }

    public void setTaskSpout(TaskSpout taskSpout) {
        this.taskSpout = taskSpout;
    }

    public void setUrlListExtractBolt(UrlListExtractBolt urlListExtractBolt) {
        this.urlListExtractBolt = urlListExtractBolt;
    }

    @Override
    public void initCrawler() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("taskSender", taskSpout);
        builder.setBolt("downloadWeb", downloadBolt).shuffleGrouping("taskSender");
        builder.setBolt("urlListExtractBolt", urlListExtractBolt).shuffleGrouping("downloadWeb");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Crawler-Topo", conf, builder.createTopology());
//        cluster.shutdown();
    }
}
