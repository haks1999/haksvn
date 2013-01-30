package svnkit;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class AccessCheckTest {

	public static void main(String[] args) throws Exception {

		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5";

		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);
		
		System.out.println( "Repository Root: " + repository.getRepositoryRoot( true ) );
        System.out.println(  "Repository UUID: " + repository.getRepositoryUUID( true ) );
        
        ISVNEditor editor = null; 
        try { 
        	editor = repository.getCommitEditor("test", null);
            //editor = repos.getCommitEditor(...); 
            editor.openRoot(-1); 
        } catch (SVNAuthenticationException e) { 
        	e.printStackTrace();
            // userName has no write access to URL. 
        } catch (SVNException e) { 
        	e.printStackTrace();
           // another error. 
        } finally { 
            if (editor != null) { 
              editor.abortEdit(); 
            } 
        } 
	}
	
}
