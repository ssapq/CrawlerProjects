package com.ss.crawler.service;

import java.util.Date;

/**
 * Created by ss on 2014/10/20.
 */
public interface WebpageParserService {

    /**
     * 带超时监控的抽取正文方法
     * @param html
     * @return
     * @throws Exception
     */
    public String extractContentWithTimeMonitor(final String html, long timeout) throws Exception;

    /**
     * 带超时监视器的发布日期抽取
     * @param html
     * @return
     * @throws Exception
     */
    public Date extractPublishDateWithTimeMonitor(final String html, long timeout) throws Exception;


    /**
     * 带超时监视器的标题抽取
     * @param html
     * @return
     * @throws Exception
     */
    public String extractTitleWithTimeMonitor(final String html, long timeout)  throws Exception;


    /**
     * 抽取正文
     * @return
     */
    public String extractContent(String html)throws Exception;


    /**
     * 抽取标题
     * @param html
     * @return
     */
    public String extractTitle(String html)throws Exception;


    /**
     *  抽取发布日期
     * @param html
     * @return
     * @throws Exception
     */
    public Date extractPublishDate(String html)throws Exception;

    /**
     *
     * @param html
     * @return
     * @throws Exception
     */
    public Date extractPublishDateFromMeta(String html) throws Exception;

    /**
     *
     * @param html
     * @return
     * @throws Exception
     */
    public String extractWebsiteName(String html)throws Exception;

    /**
     *
     * @param html
     * @return
     * @throws Exception
     */
    public String extractChanneleName(String html)throws Exception;

    /**
     * 抽取摘要
     * @param html
     * @return
     * @throws Exception
     */
    public String extractSummary(String html)throws Exception;

    /**
     * 抽取关键词
     * @param html
     * @return
     * @throws Exception
     */
    public String extractKeywords(String html)throws Exception;


    /**
     * 抽取作者
     * @param html
     * @return
     * @throws Exception
     */
    public String extractAuthor(String html) throws Exception;
}
