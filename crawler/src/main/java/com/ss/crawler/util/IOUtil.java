package com.ss.crawler.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ss on 2014/9/16.
 */
public class IOUtil {
    /**
     * inputstream转换为String
     * @return
     * @throws java.io.IOException
     */
    public static String inputStream2String(InputStream is,String charset) throws IOException {
        if(is == null){
            return "";
        }

        if(StringUtils.isBlank(charset)){
            charset = "utf-8";
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is,charset));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }
}
