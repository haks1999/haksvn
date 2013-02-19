package com.haks.haksvn.repository.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.user.model.User;

@Repository
public class LocalRepositoryFileDao {

	private boolean hasWriteAuth(String filePath){
		File file = new File(filePath);
		return file.exists() && file.canRead() && file.canWrite();
	}
	
	public boolean hasFileAuth(com.haks.haksvn.repository.model.Repository repository){
		return hasWriteAuth(repository.getAuthzPath()) && hasWriteAuth(repository.getPasswdPath());
	}
	
	public void addAccount(com.haks.haksvn.repository.model.Repository repository, List<User> userToAddList) throws HaksvnException{
		addUserListToPasswd(repository, userToAddList);
		addUserListToAuthz(repository, userToAddList);
	}
	
	private void addUserListToPasswd( com.haks.haksvn.repository.model.Repository repository, List<User> userToAddList ) throws HaksvnException{
		RandomAccessFile raf = null;
		FileChannel channel = null;
		FileLock fileLock = null;
		Map<String,String> accountList = retrieveSvnAccountList(repository);
		for( User user : userToAddList ){
			if( accountList.containsKey(user.getUserId()) ) throw new HaksvnException("[" + user.getUserId() + "] is already exist in passwd file.");
		}
		try{
			raf = new RandomAccessFile(new File(repository.getPasswdPath()), "rw");
			channel = raf.getChannel();
			fileLock = channel.tryLock();
			
			for( User user : userToAddList ){
				raf.seek(raf.length());
				raf.write(new String( "\r\n"+user.getUserId() + repository.getPasswdFileDelimeter() + repository.encryptPasswd(user.getUserPasswd())).getBytes());
			}
		}catch(Exception e){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				fileLock.release();
				channel.close();
				raf.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void addUserListToAuthz( com.haks.haksvn.repository.model.Repository repository, List<User> userToAddList ) throws HaksvnException{
		//TODO
		// 일단 사용자 추가 시 authz 초기화 하도록 코딩했음
		createAuthzFile(repository);
	}
	
	private Map<String,String> retrieveSvnAccountList(com.haks.haksvn.repository.model.Repository repository ) throws HaksvnException{
		FileReader fr = null;
		BufferedReader br = null;
		Map<String,String> accountList = new HashMap<String,String>();
		try{
			fr = new FileReader(repository.getPasswdPath());
			br = new BufferedReader(fr);
			String line = "";
			while( (line = br.readLine()) != null ){
				if( line.trim().length() < 1) continue;
				String[] id_passwd = line.split(repository.getPasswdFileDelimeter());
				if( id_passwd.length < 2) continue;
				accountList.put(id_passwd[0], id_passwd[1]);
			}
		}catch(Exception e ){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				br.close();
				fr.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return accountList;
	}
	
	public void deleteAccount(com.haks.haksvn.repository.model.Repository repository, List<User> userToDeleteList) throws HaksvnException{
		deleteUserListToPasswd(repository, userToDeleteList);
		deleteUserListToAuthz(repository, userToDeleteList);
	}
	
	private void deleteUserListToPasswd( com.haks.haksvn.repository.model.Repository repository, List<User> userToDeleteList ) throws HaksvnException{
		RandomAccessFile raf = null;
		FileChannel channel = null;
		FileLock fileLock = null;
		Set<String> userIdToDeleteSet = new HashSet<String>();
		for( User user : userToDeleteList ){
			userIdToDeleteSet.add( user.getUserId());
		}
		try{
			raf = new RandomAccessFile(new File(repository.getPasswdPath()), "rw");
			channel = raf.getChannel();
			fileLock = channel.tryLock();
			
			long position = 0;
			String line = "";
			while( (line = raf.readLine()) != null ){
				String[] id_passwd = line.split(repository.getPasswdFileDelimeter());
				if( id_passwd.length == 2 && userIdToDeleteSet.contains(id_passwd[0].trim())) {
					byte[] remainingBytes = new byte[(int) (raf.length() - raf.getFilePointer())];
					raf.read(remainingBytes);
					channel.truncate(position);
					raf.seek(position);
					raf.write(remainingBytes);
					raf.seek(position);
					continue;
				}
				position = raf.getFilePointer();
			}
		}catch(Exception e ){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				fileLock.release();
				channel.close();
				raf.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void deleteUserListToAuthz( com.haks.haksvn.repository.model.Repository repository, List<User> userToDeleteList) throws HaksvnException{
		//TODO
		// 일단 사용자 삭제 시 authz 초기화 하도록 코딩했음
		createAuthzFile(repository);
	}
	
	
	public void createAccountFile(com.haks.haksvn.repository.model.Repository repository) throws HaksvnException{
		//createPasswdFile <-- 필요할려나...
		createAuthzFile(repository);
	}
	
	private void createAuthzFile(com.haks.haksvn.repository.model.Repository repository) throws HaksvnException{
		if( !validateAuthzTemplate(repository.getAuthzTemplate()) ) return;
		String content = transAuthzTemplateToContent(repository, repository.getAuthzTemplate());
		RandomAccessFile raf = null;
		FileChannel channel = null;
		FileLock fileLock = null;
		try{
			raf = new RandomAccessFile(new File(repository.getAuthzPath()), "rw");
			channel = raf.getChannel();
			fileLock = channel.tryLock();
			
			channel.truncate(0);
			raf.write(content.getBytes());
			
		}catch(Exception e){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				fileLock.release();
				channel.close();
				raf.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private String transAuthzTemplateToContent(com.haks.haksvn.repository.model.Repository repository, String template){
		
		String formattedTemplate = String.format(template);
		List<String> systemAdminIds = new ArrayList<String>();
		List<String> reviewerIds = new ArrayList<String>();
		List<String> commiterIds = new ArrayList<String>();
		systemAdminIds.add(repository.getAuthUserId());
		for( User user : repository.getUserList() ){
			if( user.isSystemAdmin() ){
				systemAdminIds.add(user.getUserId());
			}else if( user.isReviewer() ){
				reviewerIds.add(user.getUserId());
			}else{
				commiterIds.add(user.getUserId());
			}
		}
		String startNode = repository.getRepositoryLocation().replace(repository.getSvnRoot(), "");
		formattedTemplate = formattedTemplate.replaceAll("#svn_name#", repository.getSvnName())
				.replaceAll("#trunk_path#", startNode + repository.getTrunkPath())
				.replaceAll("#branches_path#", startNode + repository.getBranchesPath())
				.replaceAll("#tags_path#", startNode + repository.getTagsPath())
				.replaceAll("#system-admin#",StringUtils.join(systemAdminIds.toArray(new String[systemAdminIds.size()]),","))
				.replaceAll("#commiter#",StringUtils.join(commiterIds.toArray(new String[commiterIds.size()]),","))
				.replaceAll("#reviewer#",StringUtils.join(reviewerIds.toArray(new String[reviewerIds.size()]),","));
		return formattedTemplate;
	}
	
	private boolean validateAuthzTemplate(String template) throws HaksvnException{
		String[] requireVariables = {"#svn_name#","#trunk_path#","#branches_path#","#tags_path#","#system-admin#","#commiter#","#reviewer#"};
		String[] requireKeywords = {"[groups]","system-admin","commiter","reviewer"};
		for( String requireVariable : requireVariables ){
			if( template.indexOf(requireVariable) < 0 ) throw new HaksvnException("[" + requireVariable + "] is required in authz template.");
		}
		for( String requireKeyword : requireKeywords ){
			if( template.indexOf(requireKeyword) < 0 ) throw new HaksvnException("[" + requireKeyword + "] is required in authz template.");
		}
		return true;
	}
	
	
	/*
	private String getAuthzTemplate(com.haks.haksvn.repository.model.Repository repository) throws HaksvnException{
		InputStreamReader isr = null;
		BufferedReader br = null;
		String template = "";
		try{
			isr = new InputStreamReader(getClass().getResourceAsStream("/authz.template"));
			br = new BufferedReader(isr);
			String line = "";
			while( (line = br.readLine()) != null ){
				template += line + "\r\n";
			}
		}catch(Exception e ){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				br.close();
				isr.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return template;
	}
	*/
	
	public void backupAccountFile(com.haks.haksvn.repository.model.Repository repository) throws HaksvnException{
		
		backupFile(repository.getAuthzPath());
		backupFile(repository.getPasswdPath());
		
	}
	
	private void backupFile(String path) throws HaksvnException{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			new File(path + ".haksvn.backup").delete();
			fis = new FileInputStream(path);
			fos = new FileOutputStream(path + ".haksvn.backup");
			byte[] buffer = new byte[1024];
			while( fis.read(buffer) > -1 ){
				fos.write(buffer);
			}
		}catch(Exception e){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				fis.close();
				fos.flush();
				fos.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
