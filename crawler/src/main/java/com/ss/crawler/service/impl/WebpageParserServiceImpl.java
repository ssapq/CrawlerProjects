package com.ss.crawler.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by ss on 2014/10/20.
 */
@Service
public class WebpageParserServiceImpl implements WebpageParserService {

    // 正则表达式过滤：正则表达式，要替换成的文本
    private static Map<String,String> filterRegexList = new HashMap<String,String>();
    static {
        //去掉脚本，不区分大小写
        filterRegexList.put("(?i)<script.*?>.*?</script>","");
        //去掉样式，不区分大小写
        filterRegexList.put("(?i)<style.*?>.*?</style>","");
        //去掉注释
        filterRegexList.put("<!--.*?-->","");
    }

    /**
     * 监控正文提取是否超时
     *
     * @return
     */
    @Override
    public String extractContentWithTimeMonitor(final String html,long timeout) throws Exception{
        String rtn = "";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @SuppressWarnings("unchecked")
            public String call() throws Exception {
                return extractContent(html);
            }
        });
        try {
            executorService.execute(futureTask);
            rtn = futureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            futureTask.cancel(true);
        } finally {
            executorService.shutdown();
        }

        return rtn;
    }

    /**
     * 监控发布时间提取是否超时
     *
     * @return
     */
    @Override
    public Date extractPublishDateWithTimeMonitor(final String html,long timeout) throws Exception {

        Date rtn = null;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<Date> futureTask = new FutureTask<Date>(new Callable<Date>() {
            @SuppressWarnings("unchecked")
            public Date call() throws Exception {
                return extractPublishDate(html);
            }
        });
        try {
            executorService.execute(futureTask);
            rtn = futureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            futureTask.cancel(true);
        } finally {
            executorService.shutdown();
        }

        return rtn;
    }

    /**
     * 监控标题提取是否超时
     *
     * @return
     */
    @Override
    public String extractTitleWithTimeMonitor(final String html,long timeout)  throws Exception {

        String rtn = "";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @SuppressWarnings("unchecked")
            public String call() throws Exception {
                return extractTitle(html);
            }
        });
        try {
            executorService.execute(futureTask);
            rtn = futureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            futureTask.cancel(true);
        } finally {
            executorService.shutdown();
        }

        return rtn;
    }

    /**
     * 抽取正文
     * @return
     */
    @Override
    public String extractContent(String html)throws Exception{

        //统计正文的数字阀值，超过此阀值时，便认为已经进入到正文部分
        int contentThresholdValue = 180;
        int depth = 6;

        String content = "";
        if(StringUtils.isBlank(html)){
            return "";
        }

        //正文预处理
        String body = htmlPreparedAndFilter(html);
        if(StringUtils.isBlank(body)){
            return "";
        }

        //1、按照换行分段
        //splitedLinesNotFilter只是去除过<script><style><!---->的主体部分
        String[] linesWithoutFilted = body.split("\n");

        //2、将换行符，如：<br>转换为回车符号[hchhf],便于今后统一处理.
        String enterTagReplaceRegex = "(?i)</p>|<br.*?/>";
        String getAllTagsRegex = "(?i)<.*?>";
        String[] linesFilted = null;
        if(linesWithoutFilted == null && linesWithoutFilted.length <= 0){
            return "";
        }
        linesFilted = new String[linesWithoutFilted.length];
        int index = 0;
        String filtedLine = "";
        for (String line : linesWithoutFilted) {
            if(StringUtils.isBlank(line)){
                continue;
            }

            filtedLine = line.replaceAll(enterTagReplaceRegex, "[hchhf]");
            //3、去除所有的HTML标签并且去除前后空格
            linesFilted[index++] = filtedLine.replaceAll(getAllTagsRegex, "").trim();
        }
        if(linesFilted == null && linesFilted.length <= 0){
            return "";
        }

        //4、对正文的正式处理主体部分
        //文章的总行数
        int totalLineNum = linesFilted.length;
        int startPosition = -1;//正文的其实行号
        int preStatisticsPieceCharCount = 0;//上一次统计的字符数量
        StringBuilder contentAccum = new StringBuilder();

        for (int lineNum =0; lineNum < totalLineNum - depth; lineNum++ ) {
            int statisticsPieceCharCount = 0;//每一行加上深度的字符数量统计
            String line = linesFilted[lineNum];
            if(StringUtils.isBlank(line)){
                continue;
            }

            //4.1 首先、将块中的字数统计出来
            int statisticsLineCharCount = 0;
            for(int i=0; i < depth; i++){
                //
                String statisticsLine = linesFilted[lineNum + i];
                if(StringUtils.isBlank(statisticsLine)){
                    continue;
                }
                statisticsLineCharCount = statisticsLine.length();
                statisticsPieceCharCount += statisticsLineCharCount;
            }

            //4.2 开始通过阀值来查找正文
            if(startPosition == -1){
                //4.3 寻找正文的起始位置
                // 如果上次查找的文本数量超过了限定字数，且当前行数字符数不为0，则认为是开始位置
                if(preStatisticsPieceCharCount > contentThresholdValue && statisticsPieceCharCount > 0){
                    //开始查找开头，方法是如果查到连续两
                    int emptyLineCount = 0;
                    for(int i = lineNum -1; i > 0; i--){
                        if(StringUtils.isBlank(linesFilted[i])){
                            emptyLineCount ++;
                        }else{
                            emptyLineCount = 0;
                        }

                        //如果连续的两行为空则认为是文章开头
                        if(emptyLineCount == 2){
                            startPosition = i + 2;
                            break;
                        }
                    }

                    //填充其实位置至当前行的正文部分
                    for(int j=startPosition; j<=lineNum; j++){
                        contentAccum.append(linesFilted[j]);
                    }

                }
            }else{
                //4.4 寻找正文的结束位置
                if(preStatisticsPieceCharCount < 20 && statisticsPieceCharCount <= 20){
                    startPosition = -1;
                }
                contentAccum.append(linesFilted[lineNum]);
            }
            preStatisticsPieceCharCount = statisticsPieceCharCount;
        }
        String result = contentAccum.toString();
        if(StringUtils.isBlank(result)){
            return "";
        }
        content = result.replace("[hchhf]", "<br/>");
        return content;
    }

    /**
     * 抽取标题
     * @param html
     * @return
     */
    @Override
    public String extractTitle(String html)throws Exception{
        String titleRegex = "((?s)<title>)([\\s\\S]*?)(</title>)";
        String h1Regex = "(<h1.*?>)(.*?)(</h1>)";

        String title = "";
        if(StringUtils.isBlank(html)){
            return "";
        }

        String titleOriginal = RegexUtil.getRegexResult(html, titleRegex);
        if(StringUtils.isBlank(titleOriginal)){
            titleOriginal = RegexUtil.getRegexResult(html, h1Regex);
        }

        if(StringUtils.isBlank(titleOriginal)){
            return "";
        }

        String[] underlintSplits = titleOriginal.split("_");
        String[] dashSplits = titleOriginal.split("-");
        String[] verticalLineSplits = titleOriginal.split("|");

        if(underlintSplits != null && underlintSplits.length >= 2){
            title = underlintSplits[0];
        }else if(dashSplits != null && dashSplits.length >= 2){
            title = dashSplits[0];
        }else if(verticalLineSplits != null && verticalLineSplits.length >= 2){
            title = verticalLineSplits[0];
        }else{
            title = titleOriginal;
        }

        if(StringUtils.isNotBlank(title)){
            title = title.trim();
        }

        return title;
    }

    /**
     *  抽取发布日期
     * @param html
     * @return
     * @throws Exception
     */
    @Override
    public Date extractPublishDate(String html)throws Exception{
        //日期提取正则
        String dateRegex = "((\\d{4}|\\d{2})(-|/)\\d{1,2}\3\\d{1,2})(\\s?\\d{2}:\\d{2})?|(\\d{4}年\\d{1,2}月\\d{1,2}日)(\\s?\\d{2}:\\d{2})?";

        Date publishDate = new Date();
        if(StringUtils.isBlank(html)){
            return null;
        }
        try {
            String publishDateStr = RegexUtil.getRegexResult(html,dateRegex);
            if(StringUtils.isBlank(publishDateStr)){
                return null;
            }

            //对中文日期处理
            StringBuffer dateStr = new StringBuffer();
            if(publishDateStr.contains("年") || publishDateStr.contains("月") || publishDateStr.contains("日")){

                char[] dateCharArray = publishDateStr.toCharArray();
                if(dateCharArray == null){
                    return null;
                }
                for (char c : dateCharArray) {
                    if('年' == c || '月' == c){
                        dateStr.append("-");
                        continue;
                    }
                    if('日' == c){
                        dateStr.append(" ");
                        continue;
                    }
                    dateStr.append(c);
                }

            }
            publishDate = DateUtil.StringToDate(dateStr.toString(), "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
        }
        return publishDate;
    }

    @Override
    public String extractWebsiteName(String title)throws Exception{
        String websiteName = null;
        if (StringUtils.isBlank(title)) {
            return null;
        }

        try {
            if (title.contains("_") || title.contains("-") || title.contains("|")) {
                String[] underlintSplits = title.split("_");
                String[] dashSplits = title.split("-");
                String[] verticalLineSplits = title.split("|");

                if (underlintSplits != null && underlintSplits.length >= 2) {
                    websiteName = underlintSplits[underlintSplits.length - 1];
                } else if (dashSplits != null && dashSplits.length >= 2) {
                    websiteName = dashSplits[dashSplits.length - 1];
                } else if (verticalLineSplits != null && verticalLineSplits.length >= 2) {
                    websiteName = verticalLineSplits[verticalLineSplits.length - 1];
                }
            }

            if (StringUtils.isNotBlank(websiteName)) {
                websiteName = websiteName.trim();
            }

        }catch (Exception e){
            return "";
        }
        return websiteName;
    }


    /**
     * 对正文进行预处理和进行过滤,返回处理过的body
     */
    private String htmlPreparedAndFilter(String html)throws Exception{
        String bodyRegex = "(<body.*?>)(.*?)(</body>)";
        if(StringUtils.isBlank(html)){
            return "";
        }
        if(filterRegexList == null || filterRegexList.isEmpty()){
            return "";
        }

        int lineBreakCount = 0;
        //取得所有的换行符
        char[] array = html.toCharArray();

        for (char c : array) {
            if('\n'==c){
                lineBreakCount ++;
            }
        }

        //行数过少，则将每个标签加上换行
        if(lineBreakCount >= 0 && lineBreakCount < 10){
            html = html.replace(">", ">\n");
        }

        for (String src : filterRegexList.keySet()) {
            String dest = filterRegexList.get(src);
            if(dest == null){
                continue;
            }
            html = html.replace("</a>", "</a>\n");
            html = html.replace("&nbsp;", " ");
            html = RegexUtil.replaceAll(html, dest, src);
        }
        String body = RegexUtil.getRegexResult(html,bodyRegex);
        if(StringUtils.isBlank(body)){
            return "";
        }

        // 标签规整化处理，将标签属性格式化处理到同一行
        // 处理形如以下的标签：
        //  <a
        //   href='http://www.baidu.com'
        //   class='test'>
        // 处理后为
        //  <a href='http://www.baidu.com' class='test'>
        String tagsCleanupRex = "<[^<]*?[\\n]+[^<]*>";
        List<String> needCleanTags = RegexUtil.getAllResult(body, tagsCleanupRex);
        if(needCleanTags != null && !needCleanTags.isEmpty()){
            for (String needCleanupTag : needCleanTags) {
                if(StringUtils.isBlank(needCleanupTag)){
                    continue;
                }

                //将原来的标签记录下来
                String originalTag = needCleanupTag;
                needCleanupTag = needCleanupTag.replace("\\n", "");
                body = body.replace(originalTag, needCleanupTag);
            }
        }
        return body;
    }

    @Override
    public String extractChanneleName(String title)throws Exception{
        String channelName = null;
        if (StringUtils.isBlank(title)) {
            return null;
        }

        try{
            if(title.contains("_") || title.contains("-") || title.contains("|")){
                String[] underlintSplits = title.split("_");
                String[] dashSplits = title.split("-");
                String[] verticalLineSplits = title.split("|");

                if(underlintSplits != null && underlintSplits.length >= 3){
                    channelName = underlintSplits[underlintSplits.length-2];
                    return channelName;
                }else if(dashSplits != null && dashSplits.length >= 3){
                    channelName = dashSplits[dashSplits.length-2];
                    return channelName;
                }else if(verticalLineSplits != null && verticalLineSplits.length >= 3){
                    channelName = verticalLineSplits[verticalLineSplits.length-2];
                    return channelName;
                }
            }
        }catch (Exception e){
            return "";
        }

        return channelName;
    }

    @Override
    public String extractSummary(String html) throws Exception {
        String summary = "";

        try{
            List<String> metaList = RegexUtil.getAllResult(html, "(?i)<meta(.*?)>");
            if(metaList == null || metaList.isEmpty()){
                return "";
            }

            for(String meta : metaList){
                if(StringUtils.isBlank(meta)){
                    continue;
                }

                summary = RegexUtil.getRegexResult(meta,"(?i)<meta name=\"description\" content=\"(.*?)\"");
                if(StringUtils.isNotBlank(summary)){
                    return summary;
                }
            }
        }catch (Exception e){
            return "";
        }

        return summary;
    }

    @Override
    public String extractKeywords(String html) throws Exception {
        String keywords = "";
        try{
            List<String> metaList = RegexUtil.getAllResult(html, "(?i)<meta(.*?)>");
            if(metaList == null || metaList.isEmpty()){
                return "";
            }

            for(String meta : metaList){
                if(StringUtils.isBlank(meta)){
                    continue;
                }

                keywords = RegexUtil.getRegexResult(meta,"(?i)<meta name=\"keywords\" content=\"(.*?)\"");
                if(StringUtils.isNotBlank(keywords)){
                    return keywords;
                }
            }
        }catch (Exception e){
            return "";
        }

        return keywords;
    }

    @Override
    public String extractAuthor(String html) throws Exception {
        String author = "";
        try{
            List<String> metaList = RegexUtil.getAllResult(html, "(?i)<meta(.*?)>");
            if(metaList == null || metaList.isEmpty()){
                return "";
            }

            for(String meta : metaList){
                if(StringUtils.isBlank(meta)){
                    continue;
                }

                author = RegexUtil.getRegexResult(meta,"(?i)<meta name=\"author\" content=\"(.*?)\"");
                if(StringUtils.isNotBlank(author)){
                    return author;
                }
            }
        }catch (Exception e){
            return "";
        }

        return author;
    }

    @Override
    public Date extractPublishDateFromMeta(String html) throws Exception {
        Date publishDate = new Date();
        String publishDateString = "";
        try{
            List<String> metaList = RegexUtil.getAllResult(html, "(?i)<meta(.*?)>");
            if(metaList == null || metaList.isEmpty()){
                return null;
            }

            for(String meta : metaList){
                if(StringUtils.isBlank(meta)){
                    continue;
                }

                publishDateString = RegexUtil.getRegexResult(meta,"(?i)<meta name=\"publishdate\" content=\"(.*?)\"");
                if(StringUtils.isNotBlank(publishDateString)){

                }
            }
        }catch (Exception e){
            return publishDate;
        }

        return publishDate;
    }
}
