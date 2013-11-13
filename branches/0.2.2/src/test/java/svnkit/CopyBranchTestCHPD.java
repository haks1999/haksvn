package svnkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;

public class CopyBranchTestCHPD {

	public static void main(String[] args) throws Exception {

		//String url = "https://haksvn.googlecode.com/svn";
		//String name = "haks1999";
		//String password = "aW9fj8bm9Rt5";
		
        //DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        //FSRepositoryFactory.setup();
        
		//String url = "svn://156.147.165.232/svn/CHPD";
		//String name = "admin";
		//String password = "admin";
		
		String url = "https://localhost/svn/main/haksvn";
		String name = "admin";
		String password = "admin";
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL
				.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);
		
		ISVNEditor editor = repository.getCommitEditor( "test test" , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );
		editor.openRoot(-1);
		editor.openDir("/",-1);
		editor.openDir("/branches",-1);
		//editor.openDir("CHPD", -1);
		//editor.openDir("branches", -1);
		editor.addDir("/branches/production3",null, -1);
		//editor.addDir( "/testA" , null , -1 );
		//editor.addFile("/branches/production3/pom.xml", "/CHPD/trunk/.classpath",9);
		editor.addFile("/branches/production3/pom.xml", "/haksvn/trunk/pom.xml",17);
		editor.closeDir();
		//editor.closeDir();
		//editor.closeDir();
		//editor.closeDir();
		//editor.closeDir();
		//editor.openDir( "nodeA" , -1 );
		//editor.openDir( "nodeAA" ,  -1 );
		//editor.addFile("pom.xml", "/haksvn/trunk/pom.xml",17);
		editor.closeEdit();
		
	}
	

}
