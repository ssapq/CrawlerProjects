package com.ss.crawler.service.impl;

import com.ss.crawler.constant.CrawlerConstant;
import com.ss.crawler.model.CrawlerRequest;
import com.ss.crawler.model.CrawlerResponse;
import com.ss.crawler.service.HttpService;
import com.ss.crawler.util.IOUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.storm.netty.util.CharsetUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ss on 2016/1/21.
 * 提供HTTP请求服务
 */
@Service("httpService")
public class HttpServiceImpl implements HttpService {
    /**
     * 从网上下载HTML页面
     *
     * @param
     */
    @Override
    public CrawlerResponse doRequest(CrawlerRequest crawlerRequest) throws Exception {
        CrawlerResponse crawlerResponse = new CrawlerResponse();
        if (crawlerRequest == null) {
            throw new Exception("请求异常");
        }
        if (StringUtils.isBlank(crawlerRequest.getUrl())) {
            throw new Exception("请求异常");
        }

        String url = crawlerRequest.getUrl();
        if(StringUtils.isBlank(url)){
            throw new Exception("URL为空");
        }

        String encode = crawlerRequest.getUrlEncode();
        if(StringUtils.isBlank(encode)){
            encode = "GBK";
        }
        url = url + "?" + crawlerRequest.buildUrlParameter(encode);

        int method = crawlerRequest.getMethod();
        if(method == 0){
            method = 1;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        String html = "";
        HttpEntity entity = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = null;
            if(method == CrawlerConstant.GET){
                HttpGet httpGet  = new HttpGet(url);
                httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                httpGet.setHeader("Connection", "keep-alive");
                httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125");
                response = httpclient.execute(httpGet);
            }else{
                HttpPost httpPost  = new HttpPost(url);
                httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125");
                response = httpclient.execute(httpPost);
            }

            entity = response.getEntity();
            if (entity == null) {
                throw new Exception("响应异常");
            }
            entity.writeTo(outStream);
            byte[] bytes1 = outStream.toByteArray();
//            CharsetUtil charsetUtil = new CharsetUtil();
//            String charset = charsetUtil.detectChineseCharset(outStream);

            String charset = "";
            if (StringUtils.isBlank(charset)) {
                charset = "utf-8";
            }
            InputStream sbs = new ByteArrayInputStream(bytes1);
            html = IOUtil.inputStream2String(sbs, charset);
            crawlerResponse.setResponse(html);
        } catch (Exception e) {
            throw new Exception("下载页面异常");
        } finally {
            if(httpclient != null){
                httpclient.close();
            }

            if (outStream != null) {
                outStream.close();
            }
        }
        return crawlerResponse;
    }
}
