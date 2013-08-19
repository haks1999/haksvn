package com.haks.haksvn.general.util;

import com.haks.haksvn.source.model.ReviewId;

public class MailTemplateUtils {
	
	public static String createRequestReviewSubject(ReviewId reviewId, String mailTemplate){
		mailTemplate = mailTemplate.replaceAll("#repository-key#", reviewId.getRepositoryKey());
		mailTemplate = mailTemplate.replaceAll("#revision#",String.valueOf(reviewId.getRevision()));
		mailTemplate = mailTemplate.replaceAll("%n", "\n");
		return mailTemplate;
	}

	public static String createRequestReviewText(ReviewId reviewId, String mailTemplate){
		mailTemplate = mailTemplate.replaceAll("#repository-key#", reviewId.getRepositoryKey());
		mailTemplate = mailTemplate.replaceAll("#revision#",String.valueOf(reviewId.getRevision()));
		mailTemplate = mailTemplate.replaceAll("%n", "\n");
		return mailTemplate;
	}
}
