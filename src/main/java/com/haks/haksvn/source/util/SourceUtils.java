package com.haks.haksvn.source.util;

import java.util.Scanner;

public class SourceUtils {

	
	public static String diffToHtml(String diff){
		StringBuffer html = new StringBuffer("<table><tr><td>...</td><td></td><td></td><td></td></tr>");
		Scanner scanner = new Scanner(diff);
		boolean startDiff = false;
		int srcStartLineNum = -1;
		int targetStartLineNum = -1;
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  if( !startDiff ){
			  if( line.startsWith("@@") ){
				  startDiff = true;
				  srcStartLineNum = Integer.parseInt(line.substring(line.indexOf("-")+1, line.indexOf(",")));
				  targetStartLineNum = srcStartLineNum;
			  }
			  continue;
		  }
		  
		  DiffLine diffLine = new SourceUtils().new DiffLine(line, srcStartLineNum, targetStartLineNum);
		  if( line.startsWith("-")){
			  srcStartLineNum++;
		  }else if( line.startsWith("+")){
			  targetStartLineNum++;
		  }else{
			  srcStartLineNum++;targetStartLineNum++;
		  }
		  html.append(diffLine.toTableTr());
		}
		html.append("<tr><td>...</td><td></td><td></td><td></td></tr></table>");
		scanner.close();
		return html.toString();
	}
	
	public class DiffLine{
		private int srcLineNumber;
		private int targetLineNumber;
		private String type;
		private int spaces = 0;
		String line;
		boolean isSource = false;
		boolean isTarget = false;
		
		public DiffLine(String line, int srcLineNumber, int targetLineNumber){
			type = line.length() < 1 ? "":line.substring(0,1);
			isSource = "-".equals(type);
			isTarget = "+".equals(type);
			if( !isSource && !isTarget ){
				isSource = true;isTarget = true;
			}
			this.srcLineNumber = srcLineNumber;
			this.targetLineNumber = targetLineNumber;
			this.line = line.length() < 2 ? "":line.substring(1);
			while(line.length() < spaces && !Character.isWhitespace(line.charAt(spaces++)) );
		}
		
		public String toTableTr(){
			return "<tr><td>" + (isSource?String.valueOf(srcLineNumber):"")+"</td><td>"+(isTarget?String.valueOf(targetLineNumber):"")+"</td><td>"+type+"</td>"+
					//"<td style=\"padding-left:"+spaces*5+"px;\">"+line.trim()+"</td></tr>";
					"<td>"+line+"</td></tr>";
		}
	}
}
