package svnkit;
import java.io.ByteArrayOutputStream;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DoDiffTest {

	public static void main(String[] args) throws Exception {

		
		DAVRepositoryFactory.setup();
		
		
		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5--";
		String path = "/trunk/src/main/java/com/haks/haksvn/source/controller/SourceController.java";
		long rev1 = 97;
		long rev2 = 96;

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
		diffClient.doDiff(repositorySVNURL, SVNRevision.create(22), SVNRevision.create(0), SVNRevision.create(96), SVNDepth.FILES, true, baos);
		
		
		
		//System.out.println(baos.toString());
		
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
	
}
