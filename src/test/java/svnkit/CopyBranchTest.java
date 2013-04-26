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
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;

public class CopyBranchTest {

	public static void main(String[] args) throws Exception {

		//String url = "https://haksvn.googlecode.com/svn";
		//String name = "haks1999";
		//String password = "aW9fj8bm9Rt5";
		
		String url = "https://localhost/svn/main/haksvn";
		String name = "admin";
		String password = "admin";
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL
				.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);

		
		
		
		
		//SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, path), revision);
		
		String path = "/newdir/abc/def/.project";
		String[] paths = {
				"/src/main/webapp/WEB-INF/web.xml",
				"/src/main/java/com/haks/haksvn/H2Controller.java",
				"/pom.xml"
		};
		
		
		
		
		//transfer(repository,  paths);
		
		ISVNEditor editor = repository.getCommitEditor( "test test" , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );
		editor.openRoot(-1);
		editor.openDir("branches/production", -1);
		editor.addDir( "nodeA" , null , -1 );
		editor.addDir( "nodeAA" , null , -1 );
		editor.addFile(".project", "/haksvn/trunk/.project",5);
		editor.closeDir();
		editor.closeDir();
		editor.openDir( "nodeA" , -1 );
		editor.openDir( "nodeAA" ,  -1 );
		editor.addFile("pom.xml", "/haksvn/trunk/pom.xml",17);
		editor.closeEdit();
		
		
		/*
		final SVNClientManager svnClientManager = SVNClientManager.newInstance();
		final SVNCopyClient copyClient = svnClientManager.getCopyClient();
		final SVNURL location = repository.getLocation();
		
		final SVNURL srcURL1 = SVNURL.parseURIEncoded(location + "/trunk/.project");
		final SVNURL dstURL1 = SVNURL.parseURIEncoded(location + "/branches/production/.project");
		final SVNCopySource source1 = new SVNCopySource(SVNRevision.HEAD, SVNRevision.create(2), srcURL1);
		
		copyClient.doCopy(new SVNCopySource[] { source1 }, dstURL1, false, true,
				false, "copy 111", null);
		
		
		final SVNURL srcURL2 = SVNURL.parseURIEncoded(location + "/trunk/pom.xml");
		final SVNURL dstURL2 = SVNURL.parseURIEncoded(location + "/branches/production/pom.xml");
		final SVNCopySource source2 = new SVNCopySource(SVNRevision.HEAD,SVNRevision.create(131), srcURL2);
		copyClient.doCopy(new SVNCopySource[] { source2 }, dstURL2, false, true,
				false, "copy 222", null);
*/
	}
	
	
	
	public static void transfer( SVNRepository repository, String[] paths ) throws Exception{
		
		List<String> dirListToAdd = new ArrayList<String>(0);
		Map<String, String> pathMap = new HashMap<String,String>();
		Set<String> addedDirs = new HashSet<String>(0);
		
		for( String path : paths ){
			String branchesPath = "/branches/production" + path;
			System.out.println( "path: " + path);
			String dir = branchesPath.substring(0,branchesPath.lastIndexOf("/"));
			pathMap.put(path.substring(0,path.lastIndexOf("/")), path.substring(path.lastIndexOf("/")+1));
			
			SVNNodeKind nodeKind = repository.checkPath("/haksvn" + dir, -1);
			if( nodeKind == SVNNodeKind.NONE){
				String currentDir = "";
				String[] dirFrags = dir.split("/");
				for( String dirFrag : dirFrags ){
					currentDir += ("/" + dirFrag);
					currentDir = currentDir.replaceAll("//", "/");
					dirListToAdd.add(currentDir);
					if( repository.checkPath("/haksvn" + currentDir, -1) == SVNNodeKind.NONE ){
						//System.out.println("added: " + currentDir);
						//dirListToAdd.add(currentDir);
					}else{
						addedDirs.add(currentDir);
					}
				}
			}
		}
		
		
		ISVNEditor editor = repository.getCommitEditor( "test test" , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );
		editor.openRoot( -1 );
		String befParent = "!@#$";
		int depth = 0;
		for( String parent : dirListToAdd ){
			if( !parent.startsWith(befParent) ){
				for( int inx = 0 ; inx < depth ; inx++ ) editor.closeDir();
				if( depth < 1 ) editor.openDir(parent.substring(0, parent.lastIndexOf("/")),-1);
				//System.out.println("opened: " + parent.substring(0, parent.lastIndexOf("/")) );
				depth = 0;
			}
			
			if( !addedDirs.contains(parent) ){
				//System.out.println( "add new dir: " + parent.substring(parent.lastIndexOf("/")));
				editor.addDir(parent.substring(parent.lastIndexOf("/")), null, -1);
				addedDirs.add(parent);
			}else{
				//System.out.println( "open added dir: " + parent.substring(parent.lastIndexOf("/")));
				editor.openDir(parent.substring(parent.lastIndexOf("/")),-1);
			}
			
			String relPath = parent.replaceFirst("/branches/production", "");
			System.out.println( "-relPath: " + relPath + " -contains: "+ pathMap.containsKey(relPath) );
			if( pathMap.containsKey(relPath)){
				editor.addFile(pathMap.get(relPath), "/haksvn/trunk" + relPath + "/" + pathMap.get(relPath), relPath.endsWith("web.xml")?16:8);
				pathMap.remove(relPath);
			}
			depth++;
			befParent = parent;
		}
		
		for( int inx = 0 ; inx < depth ; inx++ ) editor.closeDir();
		/*
		for( int inx = 0 ; inx < depth ; inx++ ) editor.closeDir();
		
		for( String path: paths){
			String branchesPath = "/branches/production" + path;
			String trunkPath = "/trunk" + path;
			
			String branchesDir = branchesPath.substring(0, branchesPath.lastIndexOf("/"));
			depth = path.split("/").length-1;
			editor.openDir(path.substring(0, path.lastIndexOf("/")), -1);
			System.out.println("==============================");
			System.out.println ( "copy from : " + trunkPath);
			System.out.println ( "copy to : " + branchesPath);
			editor.addFile(path.substring(path.lastIndexOf("/")), "/haksvn" + trunkPath, trunkPath.endsWith("web.xml")?16:8);
		}
		*/
		
		
		
		editor.closeEdit();
	}





	public static void copyFiles( SVNRepository repository, ISVNEditor editor, String[] paths ) throws Exception{
		
		Set<String> addedDirs = new HashSet<String>(0);
		Set<String> checkedDirs = new HashSet<String>(0);
		
		for( String path : paths ){
			String branchesPath = "/branches/production" + path;
			
			String dir = branchesPath.substring(0,branchesPath.lastIndexOf("/"));
			
			SVNNodeKind nodeKind = repository.checkPath(dir, -1);
			if( nodeKind == SVNNodeKind.NONE){
				
				String currentDir = "";
				String[] dirFrags = dir.split("/");
				editor.openRoot( -1 );
				for( String dirFrag : dirFrags ){
					currentDir += ("/" + dirFrag);
					if( !addedDirs.contains(currentDir) &&
							!checkedDirs.contains(currentDir) && 
							repository.checkPath(currentDir, -1) == SVNNodeKind.NONE ){
						editor.addDir(dirFrag, null, -1);
						addedDirs.add(currentDir);
					}else{
						editor.openDir(dirFrag, -1);
					}
					checkedDirs.add(currentDir);
				}
			}else{
				editor.openDir(dir, -1);
			}
			
			editor.addFile(path, "/haksvn/trunk" + path, -1);
			
		}
		
		
		
	}

}
