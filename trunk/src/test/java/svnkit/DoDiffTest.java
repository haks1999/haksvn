package svnkit;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.util.SourceUtils;
import com.haks.haksvn.source.util.SourceUtils.DiffLine;

public class DoDiffTest {

	public static void main(String[] args) throws Exception {

		
		DAVRepositoryFactory.setup();
		
		
		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5--";
		//String path = "/trunk/src/main/java/com/haks/haksvn/source/controller/SourceController.java";
		//String path="/trunk/src/main/java/com/haks/haksvn/repository/dao/SVNRepositoryDao.java";
		
		//String path="/trunk/src/main/webapp/WEB-INF/views/source/sourceDetail.jsp";
		//long rev1 = 97;
		//long rev2 = 96;
		
		//String path="/trunk/src/main/java/com/haks/haksvn/source/util/SourceUtils.java";
		//long rev1 = 104;
		//long rev2 = 103;
		
		//String path="/trunk/src/main/webapp/WEB-INF/resources/index.html";
		//long rev1 = 108;
		//long rev2 = 2;
		
		String path="/trunk/src/main/java/com/haks/haksvn/repository/service/SVNRepositoryService.java";
		long rev1 = 111;
		long rev2 = 107;

		//SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		//repository.setAuthenticationManager(authManager);
		
		
		//SVNURL svnUrl = repository.getLocation();
		//svnUrl.setPath("/trunk/src/main/java/com/haks/haksvn/source/controller/SourceController.java", true);
		
		SVNURL repositorySVNURL = SVNURL.parseURIDecoded(url+path);
		//long targetRevision = 96;
		//long revision = 97;
		//String target = "SourceController.java";
		//boolean ignoreAncestry = true;
		//boolean recursive = false;
		//boolean getContents = true;
		
		//repository.g
		
		
		//ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		//SVNClientManager clientManager = SVNClientManager.newInstance(options, repository.getAuthenticationManager());
		//SVNDiffClient diffClient = clientManager.getDiffClient();
		
		SVNDiffClient diffClient = new SVNDiffClient(authManager,null);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//diffClient.doDiff( svnUrl, SVNRevision.HEAD, svnUrl, SVNRevision.create(96), false, false, baos );
		//doDiff(SVNURL url1, SVNRevision rN, SVNURL url2, SVNRevision rM, SVNDepth depth, boolean useAncestry, java.io.OutputStream result)
		//System.out.println(baos.toString());
		
		
		// 아래 코드는 정상 작동함
		diffClient.doDiff(repositorySVNURL, SVNRevision.create(rev2), repositorySVNURL, SVNRevision.create(rev1), SVNDepth.FILES, true, baos);
		
		
		String str = baos.toString();
		
		System.out.println(str);
		//diffToHtml(str);
		
		
		//SVNSource svnSourceSrc = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(srcRev).build();
		//SVNSource svnSourceTrg = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(trgRev).build();
		//SourceUtils.diffToSideBySideHtml(str);
		
		baos.close();
		
		
		
		//repository.closeSession();
		
		/*
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); 
        final SvnDiffGenerator diffGenerator = new SvnDiffGenerator(); 
        //diffGenerator.setBasePath(new File("c:\\")); 
        
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory(); 
		svnOperationFactory.setAuthenticationManager(authManager);
		 final SvnDiff diff = svnOperationFactory.createDiff(); 
		 diff.setSource(SvnTarget.fromURL(repositorySVNURL), SVNRevision.create(96), SVNRevision.create(97));
		
		 diff.setDiffGenerator(diffGenerator); 
         diff.setOutput(byteArrayOutputStream); 
         diff.run(); 

         final String actualDiffOutput = byteArrayOutputStream.toString(); 
         System.out.println(actualDiffOutput);
         svnOperationFactory.dispose(); 
		*/
	}
	
	
	
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
		  
		  DiffLine diffLine = new DoDiffTest().new DiffLine(line, srcStartLineNum, targetStartLineNum);
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
