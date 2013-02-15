package com.haks.haksvn.repository.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	}
	
	private void addUserListToPasswd( com.haks.haksvn.repository.model.Repository repository, List<User> userToAddList ) throws HaksvnException{
		FileWriter fw = null;
		BufferedWriter bw = null;
		Map<String,String> accountList = retrieveSvnAccountList(repository);
		for( User user : userToAddList ){
			if( accountList.containsKey(user.getUserId()) ) throw new HaksvnException("[" + user.getUserId() + "] is already exist in passwd file.");
		}
		try{
			synchronized(this.getClass()){
				fw = new FileWriter(repository.getPasswdPath(), true);
				bw = new BufferedWriter(fw);
				for( User user : userToAddList ){
					bw.write(user.getUserId() + repository.getPasswdFileDelimeter() + repository.encryptPasswd(user.getUserPasswd()) );
					bw.newLine();
				}
			}
		}catch(Exception e){
			throw new HaksvnException(e.getMessage());
		}finally{
			try{
				bw.flush();
				bw.close();
				fw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
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
	}
	
	private void deleteUserListToPasswd( com.haks.haksvn.repository.model.Repository repository, List<User> userToDeleteList ) throws HaksvnException{
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		Set<String> userIdToDeleteSet = new HashSet<String>();
		for( User user : userToDeleteList ){
			userIdToDeleteSet.add( user.getUserId());
		}
		synchronized(this.getClass()){
			try{
				fr = new FileReader(repository.getPasswdPath());
				br = new BufferedReader(fr);
				fw = new FileWriter(repository.getPasswdPath() + ".haksvn.temp");
				bw = new BufferedWriter(fw);
				String line = "";
				while( (line = br.readLine()) != null ){
					String[] id_passwd = line.split(repository.getPasswdFileDelimeter());
					if( id_passwd.length == 2 && userIdToDeleteSet.contains(id_passwd[0].trim())) {
						continue;
					}
					bw.write(line);
					bw.newLine();
				}
					
			}catch(Exception e ){
				throw new HaksvnException(e.getMessage());
			}finally{
				try{
					br.close();
					fr.close();
					bw.flush();
					bw.close();
					fw.close();
					new File(repository.getPasswdPath()).delete();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createAccountFile(com.haks.haksvn.repository.model.Repository repository){
		
	}
	
	public void backupAccountFile(com.haks.haksvn.repository.model.Repository repository) throws HaksvnException{
		
		backupFile(repository.getAuthUserPasswd());
		backupFile(repository.getPasswdPath());
		
	}
	
	private void backupFile(String path) throws HaksvnException{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
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
