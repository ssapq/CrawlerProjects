package com.ss.crawler.business.impl;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.ss.crawler.business.InitBusiness;
import com.ss.crawler.business.TaskProducer;
import com.ss.crawler.storm.bolt.DownloadBolt;
import com.ss.crawler.storm.bolt.UrlListExtractBolt;
import com.ss.crawler.storm.spout.TaskSpout;
import com.ss.crawler.util.SpringUtil;
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
        this.initCrawlerV1_0();
    }

    /**
     * 初始化第一版本的爬虫 此版本爬虫不基于Storm
     * @return
     * @throws Exception
     */
    private boolean initCrawlerV1_0() throws Exception{
        TaskProducer taskProducer = (TaskProducer)SpringUtil.getBean("taskProducer");
        taskProducer.run();
        return false;
    }

    private boolean initCrawlerV2_0()throws Exception{

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("taskSender", taskSpout);
        builder.setBolt("downloadWeb", downloadBolt).shuffleGrouping("taskSender");
        builder.setBolt("urlListExtractBolt", urlListExtractBolt).shuffleGrouping("downloadWeb");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Crawler-Topo", conf, builder.createTopology());
        cluster.shutdown();
        return false;
    }
}
