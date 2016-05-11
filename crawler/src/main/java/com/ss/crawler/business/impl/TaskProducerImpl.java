package com.ss.crawler.business.impl;

import backtype.storm.tuple.Values;
import com.ss.crawler.business.TaskProducer;
import com.ss.crawler.model.Task;
import com.ss.crawler.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by ss on 2016/5/8.
 */
@Service("taskProducer")
@Scope("prototype")
public class TaskProducerImpl implements TaskProducer{

    @Autowired
    private MqService mqService;

    public void setMqService(MqService mqService) {
        this.mqService = mqService;
    }

    private void producTask() throws Exception {
        Task newTask = new Task();
        newTask.setUrl("http://www.csdn.net/");

        mqService.sendMessage(newTask);
    }

    @Override
    public void run() {
        try {
            System.out.println("任务线程开始--------------------");
            this.producTask();
            System.out.println("产生任务");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
