package com.haks.haksvn.general.util;

import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferGroup;

public class MailTemplateUtils {
	
	public static String createReviewRequestSubject(ReviewId reviewId, String mailTemplate){
		mailTemplate = mailTemplate.replaceAll("#repository-key#", reviewId.getRepositoryKey());
		mailTemplate = mailTemplate.replaceAll("#revision#",String.valueOf(reviewId.getRevision()));
		mailTemplate = mailTemplate.replaceAll("%n", "\n");
		return mailTemplate;
	}

	public static String createReviewRequestText(ReviewId reviewId, String mailTemplate){
		return createReviewRequestSubject(reviewId, mailTemplate);
	}
	
	public static String createTransferRequestSubject(Transfer transfer, String mailTemplate){
		mailTemplate = mailTemplate.replaceAll("#repository-key#", transfer.getRepositoryKey());
		mailTemplate = mailTemplate.replaceAll("#request-seq#", String.valueOf(transfer.getTransferSeq()));
		mailTemplate = mailTemplate.replaceAll("%n", "\n");
		return mailTemplate;
	}

	public static String createTransferRequestText(Transfer transfer, String mailTemplate){
		return createTransferRequestSubject(transfer, mailTemplate);
	}
	
	public static String createTransferRejectSubject(Transfer transfer, String mailTemplate){
		return createTransferRequestSubject(transfer, mailTemplate);
	}

	public static String createTransferRejectText(Transfer transfer, String mailTemplate){
		return createTransferRequestSubject(transfer, mailTemplate);
	}
	
	public static String createTransferApproveSubject(Transfer transfer, String mailTemplate){
		return createTransferRequestSubject(transfer, mailTemplate);
	}

	public static String createTransferApproveText(Transfer transfer, String mailTemplate){
		return createTransferRequestSubject(transfer, mailTemplate);
	}
	
	public static String createTransferCompleteSubject(TransferGroup transferGroup, String mailTemplate){
		mailTemplate = mailTemplate.replaceAll("#repository-key#", transferGroup.getRepositoryKey());
		mailTemplate = mailTemplate.replaceAll("#request-group-seq#", String.valueOf(transferGroup.getTransferGroupSeq()));
		mailTemplate = mailTemplate.replaceAll("%n", "\n");
		return mailTemplate;
	}

	public static String createTransferCompleteText(TransferGroup transferGroup, String mailTemplate){
		return createTransferCompleteSubject(transferGroup, mailTemplate);
	}
}
