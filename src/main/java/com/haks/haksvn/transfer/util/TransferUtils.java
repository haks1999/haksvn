package com.haks.haksvn.transfer.util;

import com.haks.haksvn.transfer.model.Transfer;

public class TransferUtils {

	public static String createTransferCommitLog(Transfer transfer, String commitLogTemplate){
		commitLogTemplate = commitLogTemplate.replaceAll("#request-id#", "req-" + String.valueOf(transfer.getTransferSeq()));
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-id#", transfer.getRequestUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-name#", transfer.getRequestUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-id#", transfer.getTransferUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-name#", transfer.getTransferUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#description#", transfer.getDescription());
		commitLogTemplate = commitLogTemplate.replaceAll("%n", System.getProperty("line.separator"));
		commitLogTemplate = commitLogTemplate.replaceAll("\r", "\n");
		return commitLogTemplate;
	}
	
	/*
	public static String createTaggingCommitLog(Tagging tagging, String commitLogTemplate){
		commitLogTemplate = commitLogTemplate.replaceAll("#request-id#", "req-" + String.valueOf(transfer.getTransferSeq()));
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-id#", transfer.getRequestUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-name#", transfer.getRequestUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-id#", transfer.getTransferUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-name#", transfer.getTransferUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#description#", transfer.getDescription());
		commitLogTemplate = commitLogTemplate.replaceAll("%n", System.getProperty("line.separator"));
		commitLogTemplate = commitLogTemplate.replaceAll("\r", "\n");
		return commitLogTemplate;
	}
	*/
}
