package com.haks.haksvn.source.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.service.CodeService;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.dao.ReviewDao;
import com.haks.haksvn.source.model.Review;
import com.haks.haksvn.source.model.ReviewAuth;
import com.haks.haksvn.source.model.ReviewComment;
import com.haks.haksvn.source.model.ReviewCommentAuth;
import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.source.model.ReviewScore;
import com.haks.haksvn.source.model.ReviewSummary;
import com.haks.haksvn.user.service.UserService;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class ReviewService {

	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private RepositoryService repositoryService;
	
	public ReviewSummary retrieveReviewSummary(String repositoryKey, long revision){
		repositoryService.checkRepositoryAccessRight(repositoryKey);
		List<ReviewScore> reviewScoreList = reviewDao.retrieveReviewScoreListByRevision(repositoryKey, revision);
		int totalScore = 0;
		List<ReviewScore> reviewScorePositiveList = new ArrayList<ReviewScore>(0);
		List<ReviewScore> reviewScoreNeutralList = new ArrayList<ReviewScore>(0);
		List<ReviewScore> reviewScoreNegativeList = new ArrayList<ReviewScore>(0);
		int positiveScore = NumberUtils.toInt(codeService.retrieveCode(CodeUtils.getPositiveReviewScoreCodeId()).getCodeValue());
		int negativeScore = NumberUtils.toInt(codeService.retrieveCode(CodeUtils.getNegativeReviewScoreCodeId()).getCodeValue());
		for( ReviewScore reviewScore : reviewScoreList ){
			totalScore += reviewScore.getScore();
			if( reviewScore.getScore() == positiveScore ){
				reviewScorePositiveList.add(reviewScore);
			}else if ( reviewScore.getScore() == negativeScore ){
				reviewScoreNegativeList.add(reviewScore);
			}else{
				reviewScoreNeutralList.add(reviewScore);
			}
		}
		
		List<ReviewComment> reviewCommentList = reviewDao.retrieveReviewCommentListByRevision(repositoryKey, revision);
		for( ReviewComment reviewComment : reviewCommentList ){
			reviewComment.setReviewCommentAuth(ReviewCommentAuth.Builder.getBuilder().reviewerId(reviewComment.getReviewer().getUserId()).build());
		}
		
		ReviewSummary reviewSummary = ReviewSummary.Builder.getBuilder().isReviewed(reviewScoreList.size() > 0)
			.totalScore(totalScore)
			.reviewCommentList(reviewCommentList)
			.reviewScoreNegativeList(reviewScoreNegativeList)
			.reviewScoreNeutralList(reviewScoreNeutralList)
			.reviewScorePositiveList(reviewScorePositiveList).build();
		return reviewSummary;
	}
	
	public Review retrieveYourReview(String repositoryKey, long revision){
		return retrieveReviewByReviewId(ReviewId.Builder.getBuilder().repositoryKey(repositoryKey).revision(revision).reviewer(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId())).build());
	}
	
	public Review retrieveReviewByReviewId(ReviewId reviewId){
		ReviewScore reviewScore = reviewDao.retrieveReviewScoreByReviewId(reviewId);
		int score = (reviewScore != null ) ? reviewScore.getScore():0;
		return Review.Builder.getBuilder().score(score).build();
	}
	
	public void saveReview(Review review){
		repositoryService.checkRepositoryAccessRight(review.getReviewId().getRepositoryKey());
		if( !ReviewAuth.Builder.getBuilder().build().getIsCreatable() ) throw new HaksvnException("Insufficient privileges.");
		if( review.getComment() != null && review.getComment().length() > 0 ){
			reviewDao.saveReviewComment(ReviewComment.Builder.getBuilder()
					.comment(review.getComment()).commentDate(System.currentTimeMillis())
					.repositoryKey(review.getReviewId().getRepositoryKey()).reviewCommentSeq(0)
					.reviewer(review.getReviewId().getReviewer())
					.revision(review.getReviewId().getRevision()).build());
		}
		reviewDao.saveReviewScore(ReviewScore.Builder.getBuilder().reviewId(review.getReviewId()).score(review.getScore()).build());
	}
	
	public void deleteReviewComment(ReviewComment reviewComment){
		reviewComment = reviewDao.retrieveReviewComment(reviewComment.getReviewCommentSeq());
		repositoryService.checkRepositoryAccessRight(reviewComment.getRepositoryKey());
		if(!ReviewCommentAuth.Builder.getBuilder().reviewerId(reviewComment.getReviewer().getUserId()).build().getIsDeletable()) throw new HaksvnException("Insufficient privileges.");
		reviewDao.deleteRevieComment(reviewComment);
	}
}
