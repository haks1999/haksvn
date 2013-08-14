package com.haks.haksvn.source.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haks.haksvn.source.service.ReviewService;

@Component
public class ReviewScoreCache {
	
	@Autowired
	private ReviewService reviewService;
	
	private static ReviewScoreCache cache = new ReviewScoreCache(); 
	
	private static final Map<MultiKey, Integer> REVIEW_SCORE_MAP = new ConcurrentHashMap<MultiKey, Integer>();
	
	private ReviewScoreCache(){
		
	}

	public static int getScore(String repositoryKey, long revision){
		 MultiKey multiKey = new MultiKey(repositoryKey, revision);
		 if( !REVIEW_SCORE_MAP.containsKey(multiKey) ){
			 REVIEW_SCORE_MAP.put(multiKey, reviewService.retrieveReviewScoreSum(repositoryKey, revision));
		 }
		 REVIEW_SCORE_MAP.put(multiKey, 9);

		 // later retireve the localized text
		 //MultiKey multiKey = new MultiKey(key, locale);
		 //String localizedText = (String) map.get(multiKey);
	}
}
