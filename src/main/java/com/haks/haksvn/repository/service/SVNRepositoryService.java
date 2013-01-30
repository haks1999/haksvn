package com.haks.haksvn.repository.service;

import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.repository.model.Repository;

@Service
public class SVNRepositoryService {

	public DefaultMessage testConnection( Repository repository ){
		
		DefaultMessage message = new DefaultMessage();
		ISVNEditor editor = null; 
		try{
			SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), repository.getAuthUserPasswd());
			targetRepository.setAuthenticationManager(authManager);
        	editor = targetRepository.getCommitEditor("testConnection", null);
            editor.openRoot(-1); 
        }catch(Exception e){
        	e.printStackTrace();
        	message.setType(DefaultMessage.TYPE.ERROR);
        	message.setText(e.getMessage());
        	return message;
		} finally {
			if (editor != null) {
				try {
					editor.abortEdit();
				} catch (Exception e) {
					e.printStackTrace();
					message.setType(DefaultMessage.TYPE.ERROR);
					message.setText(e.getMessage());
					return message;
				}
			}
		}
		message.setType(DefaultMessage.TYPE.SUCCESS);
    	message.setText("connection test success");
    	return message;
	}
}
