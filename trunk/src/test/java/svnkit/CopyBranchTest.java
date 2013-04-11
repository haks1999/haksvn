package svnkit;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class CopyBranchTest {

	public static void main(String[] args) throws Exception {

		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5";
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL
				.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);

		ISVNEditor editor = repository.getCommitEditor( "create dir" , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );
		
		editor.openRoot( -1 );
		editor.openDir("/branches/production", -1);
		
		//editor.addDir( "/branches/production/newdir/.project" , "/trunk/.project" , -1 );
		editor.addDir( "nodeA" , null , -1 );
		editor.addDir( "nodeAA" , null , -1 );
		editor.closeDir( );
		editor.closeDir();
		editor.closeDir();
		editor.closeEdit();
		
		/*
		final SVNClientManager svnClientManager = SVNClientManager.newInstance();
		final SVNCopyClient copyClient = svnClientManager.getCopyClient();
		final SVNURL location = repository.getLocation();
		
		final SVNURL srcURL = SVNURL.parseURIEncoded(location + "/trunk/.project");
		final SVNURL dstURL = SVNURL.parseURIEncoded(location + "/branches/production/newdir/.project");
		final SVNCopySource source = new SVNCopySource(SVNRevision.HEAD,SVNRevision.HEAD, srcURL);
		
		copyClient.doCopy(new SVNCopySource[] { source }, dstURL, false, false,
				true, "copy 222", null);
				*/

	}

}
