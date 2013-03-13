package com.haks.haksvn.source.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SourceUtils {
	
	private static final String MARK_SRC = "-";
	private static final String MARK_TRG = "+";

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
			  srcStartLineNum = Integer.parseInt(line.substring(line.indexOf(MARK_SRC)+1, line.indexOf(",")));
			  targetStartLineNum = Integer.parseInt(line.substring(line.indexOf(MARK_TRG)+1, line.lastIndexOf(",")));
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
			isSource = MARK_SRC.equals(type);
			isTarget = MARK_TRG.equals(type);
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
	
	
	public static String diffToSideBySideHtml(String diff, String srcContent, String trgContent){
		
		Scanner scanner = new Scanner(diff);
		SourceUtils sourceUtils = new SourceUtils();
		
		scanner.nextLine();	//Index: sourceDetail.jsp
		scanner.nextLine();	//===================================================================
		scanner.nextLine();	//--- sourceDetail.jsp	(revision 96)
		scanner.nextLine();	//+++ sourceDetail.jsp	(revision 97)
		
		
		List<DiffLineSideBySide> srcDiffLineSideBySideTotalList = new ArrayList<DiffLineSideBySide>(0);
		List<DiffLineSideBySide> trgDiffLineSideBySideTotalList = new ArrayList<DiffLineSideBySide>(0);
		List<DiffLineSideBySide> srcDiffLineSideBySideList = new ArrayList<DiffLineSideBySide>(0);
		List<DiffLineSideBySide> trgDiffLineSideBySideList = new ArrayList<DiffLineSideBySide>(0);
		int index = 0;
		boolean currentBoundChanged = false;
		String befMark = "";
		int srcChangedLastLineNum = -1;
		int trgChangedLastLineNum = -1;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if( line.startsWith("@@") ){
				  int changedSrcStartLineNum = Integer.parseInt(line.substring(line.indexOf(MARK_SRC)+1, line.indexOf(",")));
				  int changedTrgStartLineNum = Integer.parseInt(line.substring(line.indexOf(MARK_TRG)+1, line.lastIndexOf(",")));
				  index = changedSrcStartLineNum > changedTrgStartLineNum ? changedSrcStartLineNum:changedTrgStartLineNum;
				  currentBoundChanged = false;
				  continue;
			}
		  
		  
			if( line.startsWith(MARK_SRC) ){
				if( befMark.equals(MARK_SRC) || (currentBoundChanged && befMark.trim().equals(""))) index++;
				DiffLineSideBySide diffLineSideBySide = sourceUtils.new DiffLineSideBySide();
				diffLineSideBySide.index = index;
				srcDiffLineSideBySideList.add(diffLineSideBySide);
				srcChangedLastLineNum = index;
				currentBoundChanged = true;
			}else if( line.startsWith(MARK_TRG) ){
				if( befMark.equals(MARK_TRG) || (currentBoundChanged && befMark.trim().equals(""))) index++;
				DiffLineSideBySide diffLineSideBySide = sourceUtils.new DiffLineSideBySide();
				diffLineSideBySide.index = index;
				trgDiffLineSideBySideList.add(diffLineSideBySide);
				trgChangedLastLineNum = index;
				currentBoundChanged = true;
			}else{
				index++;
				if( srcDiffLineSideBySideList.size() > 0 || trgDiffLineSideBySideList.size() > 0){
					int changedSrcLineCnt = srcDiffLineSideBySideList.size();
					int changedTrgLineCnt = trgDiffLineSideBySideList.size();
					if( changedSrcLineCnt > changedTrgLineCnt ){
						if( trgChangedLastLineNum < 1 ){
							trgChangedLastLineNum = srcChangedLastLineNum - changedSrcLineCnt;
						}
						for( int inx = 0 ; inx < changedSrcLineCnt-changedTrgLineCnt ; inx++ ){
							DiffLineSideBySide diffLineSideBySide = sourceUtils.new DiffLineSideBySide();
							diffLineSideBySide.index = ++trgChangedLastLineNum;
							diffLineSideBySide.isEmpty = true;
							trgDiffLineSideBySideList.add(diffLineSideBySide);
						}
					}else if( changedSrcLineCnt < changedTrgLineCnt ){
						if( srcChangedLastLineNum < 1 ){
							srcChangedLastLineNum = trgChangedLastLineNum - changedTrgLineCnt;
						}
						for( int inx = 0 ; inx < changedTrgLineCnt-changedSrcLineCnt ; inx++ ){
							DiffLineSideBySide diffLineSideBySide = sourceUtils.new DiffLineSideBySide();
							diffLineSideBySide.index = ++srcChangedLastLineNum;
							diffLineSideBySide.isEmpty = true;
							srcDiffLineSideBySideList.add(diffLineSideBySide);
						}
					}
					srcDiffLineSideBySideList.get(0).isFirst = true;
					srcDiffLineSideBySideList.get(srcDiffLineSideBySideList.size()-1).isLast = true;
					trgDiffLineSideBySideList.get(0).isFirst = true;
					trgDiffLineSideBySideList.get(trgDiffLineSideBySideList.size()-1).isLast = true;
					srcDiffLineSideBySideTotalList.addAll(srcDiffLineSideBySideList);
					trgDiffLineSideBySideTotalList.addAll(trgDiffLineSideBySideList);
					srcDiffLineSideBySideList = new ArrayList<DiffLineSideBySide>(0);
					trgDiffLineSideBySideList = new ArrayList<DiffLineSideBySide>(0);
					srcChangedLastLineNum = -1;
					trgChangedLastLineNum = -1;
				}
				
		  	}
			befMark = line.length() < 1 ? "":line.substring(0,1);
			
		}
		scanner.close();
		
		Map<Integer,DiffLineSideBySideCombined> diffLineSideBySideCombinedMap = new HashMap<Integer,DiffLineSideBySideCombined>(0);
		for( int inx = 0 ; inx < srcDiffLineSideBySideTotalList.size() ; inx++){
			DiffLineSideBySide srcDiffLineSideBySide = srcDiffLineSideBySideTotalList.get(inx);
			DiffLineSideBySide trgDiffLineSideBySide = trgDiffLineSideBySideTotalList.get(inx);
			DiffLineSideBySideCombined diffLineSideBySideCombined = sourceUtils.new DiffLineSideBySideCombined();
			diffLineSideBySideCombined.index = srcDiffLineSideBySide.index - 1;
			diffLineSideBySideCombined.isFirst = srcDiffLineSideBySide.isFirst;
			diffLineSideBySideCombined.isLast = srcDiffLineSideBySide.isLast;
			diffLineSideBySideCombined.isEmptySrc = srcDiffLineSideBySide.isEmpty;
			diffLineSideBySideCombined.isEmptyTrg = trgDiffLineSideBySide.isEmpty;
			diffLineSideBySideCombinedMap.put(diffLineSideBySideCombined.index, diffLineSideBySideCombined);
		}
		
		//
		System.out.println( "srcDiffLineSideBySideTotalList.size : " + srcDiffLineSideBySideTotalList.size() );
		System.out.println( "trgDiffLineSideBySideTotalList.size : " + trgDiffLineSideBySideTotalList.size() );

		for( int inx = 0 ; inx < srcDiffLineSideBySideTotalList.size() ; inx++){
			DiffLineSideBySide srcDiffLineSideBySide = srcDiffLineSideBySideTotalList.get(inx);
			DiffLineSideBySide trgDiffLineSideBySide = trgDiffLineSideBySideTotalList.get(inx);
			System.out.println( srcDiffLineSideBySide.index + " | " +srcDiffLineSideBySide.isFirst + " | "+srcDiffLineSideBySide.isLast + " | " + trgDiffLineSideBySide.index + " | " + trgDiffLineSideBySide.isFirst + " | " + trgDiffLineSideBySide.isLast);
		}
		//
		
		
		String[] srcContentList = srcContent.split("\\r?\\n");
		String[] trgContentList = trgContent.split("\\r?\\n");
		boolean isSrcContentLonger = srcContentList.length > trgContentList.length;
		int maxLine =  isSrcContentLonger ? srcContentList.length:trgContentList.length;
		
		StringBuffer html = new StringBuffer("<table>");
		int lineIndex = 0;
		int srcContentIndex = 0;
		int trgContentIndex = 0;
		while( lineIndex < maxLine ){
			StringBuffer tr = new StringBuffer("<tr>");
			if( diffLineSideBySideCombinedMap.containsKey(lineIndex)){
				DiffLineSideBySideCombined diffLineSideBySideCombined = diffLineSideBySideCombinedMap.get(lineIndex);

				String trCls = (diffLineSideBySideCombined.isFirst?" isFirst":"") + (diffLineSideBySideCombined.isLast?" isLast":"");
				if(trCls.length() > 1 ) tr = new StringBuffer("<tr class=\"" + trCls + "\">");
				if(diffLineSideBySideCombined.isEmptySrc){
					if( isSrcContentLonger ) maxLine++;
					tr.append("<td></td><td class=\"empty\"></td>");
					tr.append("<td>"+(trgContentIndex+1)+"</td><td class=\"target\">" + trgContentList[trgContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
				}else if( diffLineSideBySideCombined.isEmptyTrg ){
					if( !isSrcContentLonger ) maxLine++;
					tr.append("<td>"+(srcContentIndex+1)+"</td><td class=\"source\">" + srcContentList[srcContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
					tr.append("<td></td><td class=\"empty\"></td>");
				}else{
					tr.append("<td>"+(srcContentIndex+1)+"</td><td class=\"source\">" + srcContentList[srcContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
					tr.append("<td>"+(trgContentIndex+1)+"</td><td class=\"target\">" + trgContentList[trgContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
				}
			}else{
				tr.append("<td>"+(srcContentIndex+1)+"</td><td>" + srcContentList[srcContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
				tr.append("<td>"+(trgContentIndex+1)+"</td><td>" + trgContentList[trgContentIndex++].replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</td>");
			}
			lineIndex++;
			tr.append("</tr>");
			html.append(tr);
		}
		
		
		  
		html.append("<tr><td class=\"line\"></td><td></td><td class=\"line\"></td><td></td></tr></table>");
		
		return html.toString();
	}
	
	class DiffLineSideBySide{
		int index;
		boolean isFirst = false;
		boolean isLast = false;
		boolean isEmpty = false;
	}
	
	class DiffLineSideBySideCombined{
		int index;
		boolean isEmptySrc = false;
		boolean isEmptyTrg = false;
		boolean isFirst = false;
		boolean isLast = false;
	}
}
