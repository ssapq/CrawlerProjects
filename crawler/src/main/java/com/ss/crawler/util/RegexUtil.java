package com.ss.crawler.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.*;

import java.util.ArrayList;
import java.util.List;

public class RegexUtil {

	private static final Log LOG = LogFactory.getLog(RegexUtil.class);// LOG日志

	public static List<String> getAllResult(String source, String regexp) {

		List<String> result = new ArrayList<String>();
		PatternCompiler compiler = new Perl5Compiler();
		PatternMatcher matcher = new Perl5Matcher();
		Pattern pattern;
		try {
			pattern = compiler.compile(regexp, Perl5Compiler.SINGLELINE_MASK);
		} catch (MalformedPatternException e) {
			return result;
		}
		PatternMatcherInput input = new PatternMatcherInput(source);

		while (matcher.contains(input, pattern)) {
			MatchResult matchResult = matcher.getMatch();
			result.add(matchResult.toString());
		}
		return result;
	}

	public static String getRegexResult(String source, String regexp) {
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			Pattern softPattern = compiler.compile(regexp,
					Perl5Compiler.SINGLELINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return "";
			// 如果批配结果正确,返回取出的批配结果
			List<String> tempList = new ArrayList<String>();
			MatchResult matchResult = matcher.getMatch();
			for (int i = 0; i < matchResult.length()
					&& matchResult.group(i) != null; i++)
				tempList.add(i, matchResult.group(i));
			if (tempList.size() <= 0)
				return "";
			String result = tempList.get(tempList.size() / 2);
			if (StringUtils.isBlank(result))
				result = "";
			result = result.trim();
			return result;
		} catch (MalformedPatternException e) {
			return "";
		}
	}

	public static String getRegexResult(String source, String regexp, int pos) {
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			Pattern softPattern = compiler.compile(regexp,
					Perl5Compiler.MULTILINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return "";
			// 如果批配结果正确,返回取出的批配结果
			List<String> tempList = new ArrayList<String>();
			MatchResult matchResult = matcher.getMatch();
			for (int i = 0; i < matchResult.length()
					&& matchResult.group(i) != null; i++)
				tempList.add(i, matchResult.group(i));
			if (tempList.size() <= 0)
				return "";
			if (pos >= tempList.size())
				return "";
			String result = tempList.get(pos);
			if (StringUtils.isBlank(result))
				result = "";
			result = result.trim();
			return result;
		} catch (MalformedPatternException e) {
			return "";
		}
	}

	public static String removeHead(String source, String regexp) {
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			String regexStr = "(" + regexp + ")(.*)";
			Pattern softPattern = compiler.compile(regexStr,
					Perl5Compiler.SINGLELINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return source;
			// 如果批配结果正确,返回取出的批配结果
			List<String> tempList = new ArrayList<String>();
			MatchResult matchResult = matcher.getMatch();
			for (int i = 0; i < matchResult.length()
					&& matchResult.group(i) != null; i++)
				tempList.add(i, matchResult.group(i));
			if (tempList.size() < 3)
				return source;
			String result = tempList.get(2);
			if (StringUtils.isBlank(result))
				result = "";
			result = result.trim();
			return result;
		} catch (MalformedPatternException e) {
			LOG.error("正则匹配出错，错误信息：" + e.getMessage());

		}
		return source;
	}

	public static String removeAll(String source, String regexp) {
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			String regexStr = regexp;
			Pattern softPattern = compiler.compile(regexStr,
					Perl5Compiler.SINGLELINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return source;
			
			while(matcher.contains(source, softPattern)){
				// 如果批配结果正确,返回取出的匹配结果
				MatchResult matchResult = matcher.getMatch();
				for (int i = 0; i < matchResult.length()
						&& StringUtils.isNotBlank(matchResult.group(i)); i++)
					source = source.replace(matchResult.group(i), "");
			}
		
			return source;
		} catch (MalformedPatternException e) {
			LOG.error("正则匹配出错，错误信息：" + e.getMessage());

		}
		return source;
	}
	
	public static String replaceAll(String source, String dest, String regexp) {
		if(StringUtils.isBlank(source)){
			return source;
		}
		if(StringUtils.isBlank(source)){
			return source;
		}
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			String regexStr = regexp;
			Pattern softPattern = compiler.compile(regexStr,
					Perl5Compiler.SINGLELINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return source;
			
			while(matcher.contains(source, softPattern)){
				// 如果批配结果正确,返回取出的批配结果
				MatchResult matchResult = matcher.getMatch();
				for (int i = 0; i < matchResult.length()
						&& StringUtils.isNotBlank(matchResult.group(i)); i++)
					source = source.replace(matchResult.group(i), dest);
			}
		
			return source;
		} catch (MalformedPatternException e) {
			LOG.error("正则匹配出错，错误信息：" + e.getMessage());

		}
		return source;
	}

	// 如果regexp是要移除那一部分
	public static String removeTail(String source, String regexp) {
		try {
			// 用于定义正规表达式对象模板类型
			PatternCompiler compiler = new Perl5Compiler();
			// 正规表达式比较批配对象
			PatternMatcher matcher = new Perl5Matcher();
			// 实例不区分大小写的正规表达式模板
			String regexStr = "(.*)(" + regexp + ")";
			Pattern softPattern = compiler.compile(regexStr,
					Perl5Compiler.SINGLELINE_MASK);

			if (false == matcher.contains(source, softPattern))
				return source;
			// 如果批配结果正确,返回取出的批配结果
			List<String> tempList = new ArrayList<String>();
			MatchResult matchResult = matcher.getMatch();
			for (int i = 0; i < matchResult.length()
					&& matchResult.group(i) != null; i++)
				tempList.add(i, matchResult.group(i));
			if (tempList.size() < 3)
				return source;
			String result = tempList.get(2);
			if (StringUtils.isBlank(result))
				result = "";
			result = result.trim();
			return result;
		} catch (MalformedPatternException e) {
			LOG.error("正则匹配出错，错误信息：" + e.getMessage());

		}
		return source;
	}


	public static void main(String[] args) {
		String input = "<p class=\"adf\">wegwegew</p><p style=\"margin:10px;  maringin-top:9px;top:10px;\">fewfwefewfew</p>";
//		String input = "CONTENT:北京";
		String all = RegexUtil.replaceAll(input, "<>", "<p[^>]*>");
		String rtn = input.replace("<p[^>]*>","<p>");
		System.out.println(all);
	}
}
