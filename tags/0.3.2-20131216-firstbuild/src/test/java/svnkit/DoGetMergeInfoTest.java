package svnkit;
import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DoGetMergeInfoTest {

	public static void main(String[] args) throws Exception {

		
		DAVRepositoryFactory.setup();
		String url = "https://haksvn.googlecode.com/svn";
		String name = "haks1999";
		String password = "aW9fj8bm9Rt5--";
		
		String path = "/trunk/src/main/java/com/haks/haksvn/repository/dao/SVNRepositoryDao.java";
		
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		SVNURL repositorySVNURL = SVNURL.parseURIDecoded(url+path);
		
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		
		SVNNodeKind nodeKind = repository.checkPath("", -1);
        if (nodeKind == SVNNodeKind.NONE) {
            System.err.println("There is no entry at '" + url + "'.");
            System.exit(1);
        } else if (nodeKind == SVNNodeKind.FILE) {
            System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
            System.exit(1);
        }
        System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
        System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
        System.out.println("");
        listEntries(repository, "");
        
        
		
		
		
	}
	
	
	
	public static void listEntries(SVNRepository repository, String path)
            throws SVNException {
        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            System.out.println("/" + (path.equals("") ? "" : path + "/")
                    + entry.getName() + " (author: '" + entry.getAuthor()
                    + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
            if (entry.getKind() == SVNNodeKind.DIR) {
                listEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName());
            }
        }
    }
	
	public static String[] targetSrcPathArr = {"src","main","java","com","haks","haksvn","repository","dao","SVNRepositoryDao.java"};
	public static void findEntry(SVNRepository repository, String path, int matchingPathIdx)
            throws SVNException {
        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            
            String currentPath = (path.equals("") ? "" : path + "/") + entry.getName();
            int currentMatchingPathIdx = matchingPathIdx;
            if( currentMatchingPathIdx < 0 || entry.getName().equals(targetSrcPathArr[currentMatchingPathIdx])){
            	System.out.println("/" + currentPath + " (author: '" + entry.getAuthor()
                        + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
            	if (entry.getKind() == SVNNodeKind.DIR ) {
            		findEntry(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName(),++currentMatchingPathIdx);
                }
            }
        }
    }
	
}
