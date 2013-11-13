package com.haks.haksvn.source.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.haks.haksvn.source.model.ReviewSummarySimple;
import com.haks.haksvn.source.service.ReviewService;

@Component
@Scope(value = "singleton")
public class ReviewSummarySimpleCache {
	
	@Autowired
	private ReviewService reviewService;
	
	private static final Map<MultiKey, ReviewSummarySimple> REVIEW_SCORE_MAP = new ConcurrentHashMap<MultiKey, ReviewSummarySimple>();
	
	public ReviewSummarySimple get(String repositoryKey, long revision){
		 MultiKey multiKey = new MultiKey(repositoryKey, revision);
		 if( !REVIEW_SCORE_MAP.containsKey(multiKey) ){
			 REVIEW_SCORE_MAP.put(multiKey, reviewService.retrieveReviewSummarySimple(repositoryKey, revision));
		 }
		 return REVIEW_SCORE_MAP.get(multiKey);
	}
	
	public void update(String repositoryKey, long revision){
		MultiKey multiKey = new MultiKey(repositoryKey, revision);
		REVIEW_SCORE_MAP.put(multiKey, reviewService.retrieveReviewSummarySimple(repositoryKey, revision));
	}
}
