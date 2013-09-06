package com.haks.haksvn.general.util;

import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.transfer.model.Transfer;

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
}
