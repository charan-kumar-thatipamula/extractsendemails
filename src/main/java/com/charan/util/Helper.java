package com.charan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

	public static List<String> ExtractPatters(String source, String prefix, String midSection, String suffix) {
		if (source == null || source.length() == 0) {
			return null;
		}
		List<String> valuesWithPattern = new ArrayList<String>();
		Matcher matcher1 = Pattern.compile(prefix + midSection + suffix).matcher(source);
		
		while (matcher1.find()) {
			String matchedValue = matcher1.group();
			matchedValue = matchedValue.substring(prefix.length(), matchedValue.length() - suffix.length());
			valuesWithPattern.add(matchedValue);
		}
		return valuesWithPattern;
	}
	
	public static List<String> ExtractPattersWithoutTrim(String source, String prefix, String midSection, String suffix) {
		if (source == null || source.length() == 0) {
			return null;
		}
		List<String> valuesWithPattern = new ArrayList<String>();
		Matcher matcher1 = Pattern.compile(prefix + midSection + suffix).matcher(source);
		
		while (matcher1.find()) {
			valuesWithPattern.add(matcher1.group());
		}
		return valuesWithPattern;
	}
}
