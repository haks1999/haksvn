package com.haks.haksvn.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.source.dao.ReviewDao;
import com.haks.haksvn.source.model.Review;
import com.haks.haksvn.source.model.ReviewComment;
import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.source.model.ReviewScore;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class ReviewService {

	@Autowired
	private ReviewDao reviewDao;
	
	public Review retrieveReviewByReviewId(String repositoryKey, long revision){
		ReviewId reviewId = ReviewId.Builder.getBuilder().repositoryKey(repositoryKey).revision(revision).build();
		List<ReviewScore> reviewScoreList = reviewDao.retrieveReviewScoreListByReviewId(reviewId);
		List<ReviewComment> reviewCommentList = reviewDao.retrieveReviewCommentListByReviewId(reviewId);
		//return Review.Builder.getBuilder().reviewCommentList(reviewCommentList).reviewScorePositiveList(reviewScoreList).score(5).build();
		return Review.Builder.getBuilder().score(0).build();
	}
}
