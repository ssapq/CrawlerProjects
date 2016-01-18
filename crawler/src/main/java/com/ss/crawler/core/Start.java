package com.ss.crawler.core;

import com.ss.crawler.business.InitBusiness;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ss on 2016/1/17.
 */
public class Start {

    public static void main(String[] args){

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        InitBusiness initBusiness = (InitBusiness)context.getBean("initBusiness");

        try {
            initBusiness.initCrawler();
        }catch (Exception e){
            //TODO:补充异常
        }
    }


}
