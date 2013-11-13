package com.haks.haksvn.source.util;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.source.util.DiffMatchPatchUtils.Diff;

public class SourceUtilsTest {

	
	
	private String getDiffText() throws Exception{
		DAVRepositoryFactory.setup();
		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5--";
		String path="/trunk/src/main/webapp/WEB-INF/views/source/sourceDetail.jsp";
		long rev1 = 97;
		long rev2 = 96;
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		SVNURL repositorySVNURL = SVNURL.parseURIDecoded(url+path);
		SVNDiffClient diffClient = new SVNDiffClient(authManager,null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		diffClient.doDiff(repositorySVNURL, SVNRevision.create(96), repositorySVNURL, SVNRevision.create(97), SVNDepth.FILES, true, baos);
		String str = baos.toString();
		baos.close();
		return str;
	}
	
	
	public void diffToHtmlTest() throws Exception{
		String diff = getDiffText();
		System.out.println( SourceUtils.diffToHtml(diff).replaceAll("<tr","\r\n<tr"));
	}
	
	@Test
	public void diffTest(){
		//String str2 = "<td class=\"revision\"></td>";
		//String str1 = "<td class=\"revision\"><font class=\"path font12\"><a href=\"\"></a></font></td>";
		String str1 = "$(row).children(\".revision\").text(model[inx].revision)";
		String str2 = "//$(row).find(\".revision a\").text(model[inx].revision);" + "\r\n"
					+ "$(row).find(\".revision a\").text('r'+model[inx].revision).attr('href',(hrefRoot + \"/\" + repositorySeq + (path.length<1?\"\":\"/\") + path + \"?rev=\" + model[inx].revision).replace(\"//\", \"/\"));";
		DiffMatchPatchUtils dmp = new DiffMatchPatchUtils();
		LinkedList<Diff> diffs = dmp.diff_main(str1, str2);
		dmp.diff_cleanupSemantic(diffs);
		System.out.println( dmp.diff_prettyHtml(diffs));
		
	}
}
