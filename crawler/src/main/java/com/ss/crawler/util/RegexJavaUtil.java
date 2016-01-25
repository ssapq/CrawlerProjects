package com.ss.crawler.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexJavaUtil {
	/**
	 * 判断整型
	 */
	public static boolean isInteger(String str) {
		return str.matches("^-?\\d+$");
	}

	/**
	 * 判断整型、小数
	 */
	public static boolean isNumber(String str) {
		return str.matches("^-?\\d+(\\.\\d+)?$");
	}
	/**
	 * 正则方式取xml标签attributeName的value
	 * @param elementString, attributeName
	 * @return
	 */
	public static String getXmlAttribute(String elementString, String attributeName) {
		String attributeValue = "";
		
		Pattern p = Pattern.compile("(\\w+)\\s*=\\s*'([^\"]+)'");
		Matcher m = p.matcher(elementString);
		
		while(m.find()) {
			if(m.group(1).trim().equals(attributeName)) {
				attributeValue += (StringUtils.isNotBlank(attributeValue) ? "," : "") + m.group(2).trim().split("'")[0];
			}
		}
		
		if (StringUtils.isBlank(attributeValue)) {
			
			p = Pattern.compile("(\\w+)\\s*=\\s*\"([^\"]+)\"");
			m = p.matcher(elementString);
			
			while(m.find()) {
				if(m.group(1).trim().equals(attributeName)) {
					attributeValue += (StringUtils.isNotBlank(attributeValue) ? "," : "") + m.group(2).trim().split("\"")[0];
				}
			}
		}
		
		return attributeValue;
	}
	
	/**
	 * 正则方式取xml标签elementName的value
	 * @param elementString
	 * @param elementName
	 * @return
	 */
	public static String getXmlElementText(String elementString, String elementName) {
		String elementValue = "";
		
		Pattern p = Pattern.compile("<" + elementName + ".*?>(<!\\[CDATA\\[(?:[\\s\\S]*?)\\]\\]\\>)</" + elementName);
		Matcher m = p.matcher(elementString);
		
		if(m.find()) {
			elementValue = m.group(1).trim();
			Pattern p1 = Pattern.compile("<!\\[CDATA\\[(.*?)\\]\\]\\>");
			Matcher m1 = p1.matcher(m.group(1).trim());
			if(m1.find()) {
				elementValue = m1.group(1).trim();
			}
		}
		
		return elementValue;
	}

    /**
     * 去掉内容中的HTML标签
     * <p>
     * 将内容中的jsp页面标签等替换为 '目标字符窜';
     *
     * @param content
     *            含有页面标签的内容

     *
     * @param replacement
     *            要替换成的 '目标字符窜'
     * @return
     */
    public static String replaceHtmlTag(String content, String replacement) {
        String rtn = content;
        Matcher htmlTag = Pattern.compile("<([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
        while (htmlTag.find()) {
            rtn = htmlTag.replaceAll(replacement);
        }
        return rtn;
    }

    /**
     * 过滤HTML标签,除Img标签外

     *
     * @param content
     * @param replacement
     * @return
     */
    public static String replaceHtmlTagOutImg(String content, String replacement) {
        if(StringUtils.isBlank(content) )
            return content;
        String rtn = content.replaceAll("<br[^>]*>", "\t");
        Matcher htmlTag = Pattern.compile("<(?!IMG)([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
        while (htmlTag.find()) {
            rtn = htmlTag.replaceAll(replacement);
        }
        return rtn;
    }
    /**
     * 过滤HTML标签,除Img标签外

     *
     * @param content
     * @param replacement
     * @return
     */
    public static String replaceHtmlTagOutImgWithBr(String content, String replacement) {
        if(StringUtils.isBlank(content) )
            return content;
        String rtn = content.replaceAll("<style>.*?</style>", "");
        Matcher htmlTag = Pattern.compile("<(?!IMG|BR)([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
        while (htmlTag.find()) {
            rtn = htmlTag.replaceAll(replacement);
        }
        rtn = rtn.replaceAll("<br[^>]*>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
        rtn ="&nbsp;&nbsp;&nbsp;&nbsp;"+rtn;
        return rtn;
    }

    public static String replaceHtmlTagOutImgWithP(String content, String replacement) {
        if(StringUtils.isBlank(content) )
            return content;
        String rtn = content.replaceAll("<style>.*?</style>", "");
        Matcher htmlTag = Pattern.compile("<(?!IMG|/P)([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
        while (htmlTag.find()) {
            rtn = htmlTag.replaceAll(replacement);
        }
        rtn = rtn.replaceAll("</p>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
        rtn ="&nbsp;&nbsp;&nbsp;&nbsp;"+rtn;
        return rtn;
    }

    public static String replaceHtmlTagOutImgWithPWithBR(String content, String replacement) {
        if(StringUtils.isBlank(content) )
            return content;
        String rtn = content.replaceAll("<style>.*?</style>", "");
        Matcher htmlTag = Pattern.compile("<(?!IMG|/P|BR)([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
        while (htmlTag.find()) {
            rtn = htmlTag.replaceAll(replacement);
        }
        rtn = rtn.replaceAll("</p>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
        rtn ="&nbsp;&nbsp;&nbsp;&nbsp;"+rtn;
        return rtn;
    }
}
