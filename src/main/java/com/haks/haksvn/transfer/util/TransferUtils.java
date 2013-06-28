package com.haks.haksvn.transfer.util;

import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.model.Transfer;

public class TransferUtils {

	public static String createTransferCommitLog(Transfer transfer, String commitLogTemplate){
		commitLogTemplate = commitLogTemplate.replaceAll("#request-group-id#", "group-" + String.valueOf(transfer.getTransferGroup().getTransferGroupSeq()));
		commitLogTemplate = commitLogTemplate.replaceAll("#request-id#", "req-" + String.valueOf(transfer.getTransferSeq()));
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-id#", transfer.getRequestUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#request-user-name#", transfer.getRequestUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-id#", transfer.getApproveUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#approve-user-name#", transfer.getApproveUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#transfer-user-id#", transfer.getTransferGroup().getTransferUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#transfer-user-name#", transfer.getTransferGroup().getTransferUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#description#", transfer.getDescription());
		commitLogTemplate = commitLogTemplate.replaceAll("%n", System.getProperty("line.separator"));
		commitLogTemplate = commitLogTemplate.replaceAll("\r", "\n");
		return commitLogTemplate;
	}
	
	public static String createTaggingCommitLog(Tagging tagging, String commitLogTemplate){
		commitLogTemplate = commitLogTemplate.replaceAll("#tagging-id#", "tagging-" + String.valueOf(tagging.getTaggingSeq()));
		commitLogTemplate = commitLogTemplate.replaceAll("#tagging-user-id#", tagging.getTaggingUser().getUserId());
		commitLogTemplate = commitLogTemplate.replaceAll("#tagging-user-name#", tagging.getTaggingUser().getUserName());
		commitLogTemplate = commitLogTemplate.replaceAll("#description#", tagging.getDescription());
		commitLogTemplate = commitLogTemplate.replaceAll("%n", System.getProperty("line.separator"));
		commitLogTemplate = commitLogTemplate.replaceAll("\r", "\n");
		return commitLogTemplate;
	}
}
