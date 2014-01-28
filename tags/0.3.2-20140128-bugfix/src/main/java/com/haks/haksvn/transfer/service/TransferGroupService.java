package com.haks.haksvn.transfer.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.service.CodeService;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.general.model.MailConfiguration;
import com.haks.haksvn.general.model.MailMessage;
import com.haks.haksvn.general.model.MailTemplate;
import com.haks.haksvn.general.service.GeneralService;
import com.haks.haksvn.general.util.MailTemplateUtils;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSourceTransfer;
import com.haks.haksvn.transfer.dao.TransferGroupDao;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferGroup;
import com.haks.haksvn.transfer.model.TransferSource;
import com.haks.haksvn.transfer.util.TransferUtils;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TransferGroupService {

	@Autowired
	private TransferGroupDao transferGroupDao;
	@Autowired
	private TransferService transferService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private UserService userService;
	@Autowired
	private SVNRepositoryService svnRepositoryService;
	@Autowired
	private GeneralService generalService;
	
	public Paging<List<TransferGroup>> retrieveTransferGroupList(Paging<TransferGroup> paging){
		repositoryService.checkRepositoryAccessRight(paging.getModel().getRepositoryKey());
		return transferGroupDao.retrieveTransferGroupList(paging);
	}
	
	public TransferGroup saveTransferGroup(TransferGroup transferGroup){
		transferGroupDao.releaseTransferTransferGroup(transferGroup);
		TransferGroup currentTransferGroup = transferGroupDao.retrieveTransferGroupByTransferGroupSeq(transferGroup.getTransferGroupSeq());
		if( currentTransferGroup == null ){
			currentTransferGroup = transferGroup;
		}
		List<Transfer> exsitingTransferList = new ArrayList<Transfer>(0);
		for( Transfer transfer : transferGroup.getTransferList() ){
			transfer.setTransferGroup(transferGroup);
			transfer.setRepositoryKey(transferGroup.getRepositoryKey());
			Transfer existingTransfer = checkStandbyableTransfer(transfer);
			existingTransfer.setTransferGroup(transferGroup);
			existingTransfer.setTransferStateCode(codeService.retrieveCode(CodeUtils.getTransferStandbyCodeId()));
			exsitingTransferList.add(existingTransfer);
		}
		TransferGroup.Builder.getBuilder(currentTransferGroup).transferGroupStateCode(codeService.retrieveCode(CodeUtils.getTransferGroupStandbyCodeId()))
			.transferDate(0).transferList(exsitingTransferList).transferUser(null).description(transferGroup.getDescription()).title(transferGroup.getTitle()).build();
		return transferGroupDao.saveTransferGroup(currentTransferGroup);
	}
	
	public TransferGroup deleteTransferGroup(TransferGroup transferGroup){
		repositoryService.checkRepositoryAccessRight(transferGroup.getRepositoryKey());
		TransferGroup currentTransferGroup = transferGroupDao.retrieveTransferGroupByTransferGroupSeq(transferGroup.getTransferGroupSeq());
		transferGroupDao.releaseTransferTransferGroup(currentTransferGroup);
		return transferGroupDao.deleteTransferGroup(currentTransferGroup);
	}
	
	public TransferGroup transferTransferGroup(TransferGroup transferGroup){
		Repository repository = repositoryService.checkRepositoryAccessRight(transferGroup.getRepositoryKey());
		TransferGroup currentTransferGroup = transferGroupDao.retrieveTransferGroupByTransferGroupSeq(transferGroup.getTransferGroupSeq());
		TransferGroup.Builder.getBuilder(currentTransferGroup).transferGroupStateCode(codeService.retrieveCode(CodeUtils.getTransferGroupTransferedCodeId()))
					.transferDate(System.currentTimeMillis()).transferUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId())).build();
		for( Transfer transfer : currentTransferGroup.getTransferList() ){
			checkTransferableTransfer(transfer);
			List<SVNSourceTransfer> svnSourceTransferList = new ArrayList<SVNSourceTransfer>(0);
			for( TransferSource transferSource : transfer.getSourceList() ){
				boolean isToDeleted = CodeUtils.isTransferSourceTypeDelete(transferSource.getTransferSourceTypeCode().getCodeId());
				boolean isToAdd = CodeUtils.isTransferSourceTypeAdd(transferSource.getTransferSourceTypeCode().getCodeId());
				boolean isToModify = CodeUtils.isTransferSourceTypeModify(transferSource.getTransferSourceTypeCode().getCodeId());
				String relPath = transferSource.getPath().replaceFirst(isToDeleted?repository.getBranchesPath():repository.getTrunkPath(), "");
				svnSourceTransferList.add(SVNSourceTransfer.Builder.getBuilder(new SVNSourceTransfer()).revision(transferSource.getRevision())
						.isToAdd(isToAdd).isToDelete(isToDeleted).isToModify(isToModify).relativePath(relPath).build());
				transferSource.setDestPath((repository.getBranchesPath() + "/" + relPath).replaceAll("//", "/"));
			}
			transfer.setRevision(svnRepositoryService.transfer(repository, svnSourceTransferList, TransferUtils.createTransferCommitLog(transfer, generalService.retrieveCommitLogTemplate(transfer.getRepositoryKey(), CodeUtils.getLogTemplateRequestCodeId()).getTemplate())));
			transfer.setTransferStateCode(codeService.retrieveCode(CodeUtils.getTransferTransferedCodeId()));
		}
		
		// 최종 revision 을 가져 온 후 최종 update
		transferGroupDao.saveTransferGroup(currentTransferGroup);
		if( Boolean.valueOf(codeService.retrieveCode(CodeUtils.getMailNoticeTransferCompleteCodeId()).getCodeValue())){
			sendTransferCompleteNotice(currentTransferGroup);
		}
		return currentTransferGroup;
	}
	
	private void sendTransferCompleteNotice(TransferGroup transferGroup){
		MailTemplate mailTemplate = generalService.retrieveMailTemplate(transferGroup.getRepositoryKey(), CodeUtils.getMailTemplateTransferCompleteCodeId());
		MailConfiguration mailConfiguration = generalService.retrieveMailConfiguration();
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(mailConfiguration.getReplyto());
		mailMessage.setSubject(MailTemplateUtils.createTransferCompleteSubject(transferGroup, mailTemplate.getSubject()));
		mailMessage.setText(MailTemplateUtils.createTransferCompleteText(transferGroup, mailTemplate.getText()));
		
		List<User> noticeUserList = new ArrayList<User>(0);
		noticeUserList.add(userService.retrieveUserByUserId(transferGroup.getTransferUser().getUserId()));
		for( Transfer transfer : transferGroup.getTransferList()){
			noticeUserList.add(userService.retrieveUserByUserId(transfer.getRequestUser().getUserId()));
			noticeUserList.add(userService.retrieveUserByUserId(transfer.getApproveUser().getUserId()));
		}
		Set<String> userMailSet = new HashSet<String>(0); 
		for( User noticeUser : noticeUserList ){
			userMailSet.add(noticeUser.getEmail());
		}
		mailMessage.setTo(userMailSet.toArray(new String[userMailSet.size()]));
		generalService.sendMail(mailConfiguration, mailMessage);
	}
	
	
	public TransferGroup retrieveTransferGroupDetail(TransferGroup transferGroup){
		repositoryService.checkRepositoryAccessRight(transferGroup.getRepositoryKey());
		return transferGroupDao.retrieveTransferGroupByTransferGroupSeq(transferGroup.getTransferGroupSeq());
	}
	
	public Transfer checkStandbyableTransfer(Transfer transfer){
		Transfer existingTransfer = transferService.retrieveTransferDetail(transfer);
		if( !CodeUtils.isStandbyableState(existingTransfer.getTransferStateCode().getCodeId()) ){
			throw new HaksvnException("[req-" + existingTransfer.getTransferSeq() + "] not in standbyale state.");
		}
		return existingTransfer;
	}
	
	public Transfer checkTransferableTransfer(Transfer transfer){
		Transfer existingTransfer = transferService.retrieveTransferDetail(transfer);
		if( !CodeUtils.isTransferableState(existingTransfer.getTransferStateCode().getCodeId()) ){
			throw new HaksvnException("[req-" + existingTransfer.getTransferSeq() + "] not in transferable state.");
		}
		return existingTransfer;
	}
	
	
}
