package com.ss.crawler.model;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ss on 2014/9/16.
 */
public class CrawlerRequest {

    private String url;
    private Long timeout;
    private int method;
    private String urlEncode;
    private Map<String,String> parameters = new LinkedHashMap<String,String>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getUrlEncode() {
        return urlEncode;
    }

    public void setUrlEncode(String urlEncode) {
        this.urlEncode = urlEncode;
    }

    /**
     * 设置请求参数
     * @param key
     * @param value
     */
    public void setParameter(String key,String value){
        if(StringUtils.isBlank(key)){
            return;
        }
        if(StringUtils.isBlank(value)){
            return;
        }
        this.parameters.put(key,value);
    }

    public String buildUrlParameter(String urlEncode) throws Exception {
        if(this.parameters == null || this.parameters.isEmpty()){
            return "";
        }

        StringBuffer accum = new StringBuffer();
        for (String key : parameters.keySet()){
            if(StringUtils.isBlank(key)){
                continue;
            }

            String value = parameters.get(key);

            if(StringUtils.isBlank(value)){
                continue;
            }

            String _temp = accum.toString();
            try {
                if (StringUtils.isNotBlank(_temp) && !_temp.endsWith("?")){
                    accum.append("&");
                }else{
                    accum.append("");
                }
                accum.append(key.toString()+"="+ URLEncoder.encode(value.toString(),urlEncode));
            } catch (UnsupportedEncodingException e) {
                throw new Exception("URL参数编码异常"+e.getMessage());
            }

        }
        return accum.toString();
    }

}
