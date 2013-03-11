package com.haks.haksvn.source.util;

import java.util.Scanner;

public class SourceUtils {

	//TODO 여기 좀 이쁘게
	public static String diffToHtml(String diff){
		StringBuffer html = new StringBuffer("<table>");
		Scanner scanner = new Scanner(diff);
		int srcStartLineNum = -1;
		int targetStartLineNum = -1;
		
		scanner.nextLine();	//Index: sourceDetail.jsp
		scanner.nextLine();	//===================================================================
		scanner.nextLine();	//--- sourceDetail.jsp	(revision 96)
		scanner.nextLine();	//+++ sourceDetail.jsp	(revision 97)
		
		boolean befHasType = false;
		DiffLine befDiffLine = null;
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  if( line.startsWith("@@") ){
			  befHasType = false;
			  html.append("<tr><td>...</td><td></td><td></td><td></td></tr>");
			  srcStartLineNum = Integer.parseInt(line.substring(line.indexOf("-")+1, line.indexOf(",")));
			  targetStartLineNum = srcStartLineNum;
			  continue;
		  }
		  
		  DiffLine diffLine = new SourceUtils().new DiffLine(line, srcStartLineNum, targetStartLineNum);
		  if( diffLine.isTarget()){
			  targetStartLineNum++;
		  }else if( diffLine.isSource()){
			  srcStartLineNum++;
		  }else{
			  srcStartLineNum++;targetStartLineNum++;
		  }
		  boolean curHasType = diffLine.isTarget()||diffLine.isSource();
		  boolean isFirst = !befHasType && curHasType;
		  boolean isLast = befHasType && !curHasType;
		  diffLine.setIsFirst(isFirst);
		  if(befDiffLine !=null ){
			  befDiffLine.setIsLast(isLast);
			  html.append(befDiffLine.toTableTr());
		  }
		  befHasType = curHasType;
		  befDiffLine = diffLine;
		}
		 if(befDiffLine !=null ) html.append(befDiffLine.toTableTr());
		html.append("<tr><td class=\"line\">...</td><td class=\"line\"></td><td class=\"mark\"></td><td></td></tr></table>");
		scanner.close();
		return html.toString();
	}
	
	public class DiffLine{
		private int srcLineNumber;
		private int targetLineNumber;
		private String type;
		String line;
		boolean isSource = false;
		boolean isTarget = false;
		boolean isNoneType = false;
		boolean isFirst = false;
		boolean isLast = false;
		
		public DiffLine(String line, int srcLineNumber, int targetLineNumber){
			type = line.length() < 1 ? "":line.substring(0,1);
			isSource = "-".equals(type);
			isTarget = "+".equals(type);
			isNoneType = !isSource && !isTarget;
			this.srcLineNumber = srcLineNumber;
			this.targetLineNumber = targetLineNumber;
			this.line = line.length() < 2 ? "":line.substring(1);
		}
		
		public boolean isSource(){
			return isSource;
		}
		
		public boolean isTarget(){
			return isTarget;
		}
		
		public void setIsFirst(boolean isFirst){
			this.isFirst = isFirst;
		}
		
		public  void setIsLast(boolean isLast){
			this.isLast = isLast;
		}
		
		public String toTableTr(){
			String rowCls = isFirst? (" class=\"isFirst"+( isLast?" isLast":"")+"\""): isLast? " class=\"isLast\"":"";
			String lineTdCls = isSource? " class=\"source\"" : isTarget? " class=\"target\"":"";
			String srcLineNumberText = isSource||isNoneType?String.valueOf(srcLineNumber):"";
			String targetLineNumberText = isTarget||isNoneType?String.valueOf(targetLineNumber):"";
			return "<tr" + rowCls + "><td>" + srcLineNumberText+"</td><td>"+targetLineNumberText+"</td><td>"+type+"</td>"+
					"<td" + lineTdCls + ">"+line.replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</td></tr>";
		}
	}
	
	
	public static String diffToSideBySideHtml(String diff){
		StringBuffer html = new StringBuffer("<table>");
		Scanner scanner = new Scanner(diff);
		int srcStartLineNum = -1;
		int targetStartLineNum = -1;
		
		scanner.nextLine();	//Index: sourceDetail.jsp
		scanner.nextLine();	//===================================================================
		scanner.nextLine();	//--- sourceDetail.jsp	(revision 96)
		scanner.nextLine();	//+++ sourceDetail.jsp	(revision 97)
		
		boolean befHasType = false;
		DiffLine befDiffLine = null;
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  if( line.startsWith("@@") ){
			  befHasType = false;
			  html.append("<tr><td>...</td><td></td><td></td><td></td></tr>");
			  srcStartLineNum = Integer.parseInt(line.substring(line.indexOf("-")+1, line.indexOf(",")));
			  targetStartLineNum = srcStartLineNum;
			  continue;
		  }
		  
		  DiffLine diffLine = new SourceUtils().new DiffLine(line, srcStartLineNum, targetStartLineNum);
		  if( diffLine.isTarget()){
			  targetStartLineNum++;
		  }else if( diffLine.isSource()){
			  srcStartLineNum++;
		  }else{
			  srcStartLineNum++;targetStartLineNum++;
		  }
		  boolean curHasType = diffLine.isTarget()||diffLine.isSource();
		  boolean isFirst = !befHasType && curHasType;
		  boolean isLast = befHasType && !curHasType;
		  diffLine.setIsFirst(isFirst);
		  if(befDiffLine !=null ){
			  befDiffLine.setIsLast(isLast);
			  html.append(befDiffLine.toTableTr());
		  }
		  befHasType = curHasType;
		  befDiffLine = diffLine;
		}
		 if(befDiffLine !=null ) html.append(befDiffLine.toTableTr());
		html.append("<tr><td class=\"line\">...</td><td class=\"line\"></td><td class=\"mark\"></td><td></td></tr></table>");
		scanner.close();
		return html.toString();
	}
}
