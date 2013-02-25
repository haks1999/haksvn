package com.haks.haksvn.common.format.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {
	
	public static String fileSize(long size){
		return fileSize( (double)size, 0 );
	}
	
	private static String fileSize(double size, int iteration) {
	    double d = ((long)size / 100) / 10.0;
	    if( size < 1000 ) return (long)size + " bytes";
	    boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
	    return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
	        ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
	         (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
	         ) + "" + sizeSuffix[iteration]) 
	        : fileSize(d, iteration+1));
	}
	private static final String[] sizeSuffix = new String[]{"KB", "MB", "GB", "TB"};
	/*
	public static String fileSize(long size) {
		double value = Long.valueOf(size).doubleValue();
		int power; 
		    //String suffix = " kmbt";
		    String formattedNumber = "";

		    NumberFormat formatter = new DecimalFormat("#,###.#");
		    power = (int)StrictMath.log10(value);
		    value = value/(Math.pow(10,(power/3)*3));
		    formattedNumber=formatter.format(value);
		    //formattedNumber = formattedNumber + suffix.charAt(power/3);
		    formattedNumber = formattedNumber + sizeSuffix[(power/3)];
		    return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;  
		}
		*/
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
	public static String simpleDate(Date date){
		return simpleDateFormat.format(date);
	}
}
