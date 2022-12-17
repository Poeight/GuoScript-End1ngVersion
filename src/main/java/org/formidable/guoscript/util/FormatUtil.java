package org.formidable.guoscript.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

	private static final Pattern pattern = Pattern.compile("[#](-?\\d+\\.?\\d*)[#]");
	private static final DecimalFormat decimalFormat = new DecimalFormat("######0.00");

	public static String format(String str){
		if (str.endsWith(".0")){
			str = str.replace(".0", "");
		}
		return str;
	}
	
	public static String round(double value) {
		String str = decimalFormat.format(value);
		if (str.endsWith(".00")) {
			str = str.replace(".00", "");
		}
		return str;
	}

	public static String roundAll(String msg){
		Matcher matcher = pattern.matcher(msg);
		while (matcher.find()){
			msg = msg.replace(matcher.group(), round(Double.valueOf(matcher.group(1))));
		}
		return msg;
	}

}