package com.haks.haksvn.source.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.source.model.ReviewComment;
import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.source.model.ReviewRequest;
import com.haks.haksvn.source.model.ReviewScore;
import com.haks.haksvn.source.model.ReviewSummarySimple;

@Repository
public class ReviewDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<ReviewScore> retrieveReviewScoreListByRevision(String repositoryKey, long revision){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<ReviewScore> reviewScoreList= session.createCriteria(ReviewScore.class)
				.add(Restrictions.eq("reviewId.repositoryKey", repositoryKey))
				.add(Restrictions.eq("reviewId.revision", revision))
				.list();
		return reviewScoreList;
	}
	
	public ReviewScore retrieveReviewScoreByReviewId(ReviewId reviewId){
		Session session = sessionFactory.getCurrentSession();
		return (ReviewScore)session.get(ReviewScore.class, reviewId);
	}
	
	public ReviewSummarySimple retrieveReviewSummarySimple(String repositoryKey, long revision){
		ReviewSummarySimple reviewSummarySimple = new ReviewSummarySimple();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ReviewScore.class)
				.add(Restrictions.eq("reviewId.repositoryKey", repositoryKey))
				.add(Restrictions.eq("reviewId.revision", revision));
		@SuppressWarnings("unchecked") List<ReviewScore> reviewScoreList = criteria.list();
		reviewSummarySimple.setIsReviewed(reviewScoreList != null && reviewScoreList.size() > 0);
		if( reviewSummarySimple.getIsReviewed() ){
			criteria.setProjection(Projections.sum("score"));
			reviewSummarySimple.setTotalScore(((Long)criteria.uniqueResult()).intValue());
		}
		return reviewSummarySimple;
	}
	
	public List<ReviewComment> retrieveReviewCommentListByRevision(String repositoryKey, long revision){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<ReviewComment> reviewScoreList= session.createCriteria(ReviewComment.class)
				.add(Restrictions.eq("repositoryKey", repositoryKey))
				.add(Restrictions.eq("revision", revision))
				.addOrder(Order.desc("commentDate"))
				.list();
		return reviewScoreList;
	}
	
	public void saveReviewComment(ReviewComment reviewComment){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(reviewComment);
	}
	
	public void deleteRevieComment(ReviewComment reviewComment){
		Session session = sessionFactory.getCurrentSession();
		session.delete(reviewComment);
	}
	
	public ReviewComment retrieveReviewComment(int reviewCommentSeq){
		Session session = sessionFactory.getCurrentSession();
		return (ReviewComment)session.get(ReviewComment.class, reviewCommentSeq);
	}
	
	public void saveReviewScore(ReviewScore reviewScore){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(reviewScore);
	}
	
	public Paging<List<ReviewRequest>> retrieveReviewRequestList(Paging<ReviewRequest> paging ){
		Session session = sessionFactory.getCurrentSession();
		ReviewRequest search = paging.getModel();
		Criteria crit = session.createCriteria(ReviewRequest.class,"t_reveiw_request")
				.setFirstResult((int)paging.getStart())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositoryKey", search.getRepositoryKey()))
				.addOrder(Order.desc("requestDate"))
				.addOrder(Order.desc("reviewRequestSeq"));
		/*
		if( search.getRequestUser() != null ){
			String requestUserId = search.getRequestUser().getUserId();
			if( requestUserId != null && requestUserId.length() > 0 ){
				
				crit.createAlias("t_transfer.requestUser", "t_user")
					.add(Restrictions.eq("t_user.userId", requestUserId));
			}
		}
		if( search.getTransferStateCode() != null ){
			String transferStateCode = search.getTransferStateCode().getCodeId();
			if( transferStateCode != null && transferStateCode.length() > 0 ){
				crit.createAlias("t_transfer.transferStateCode", "t_code")
					.add(Restrictions.eq("t_code.codeId", transferStateCode));
			}
		}
		*/
		
		@SuppressWarnings("unchecked") List<ReviewRequest> result = (List<ReviewRequest>)crit.list();
		Paging<List<ReviewRequest>> resultPaging = new Paging<List<ReviewRequest>>();
		resultPaging.setModel(result);
		Paging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart());
		
		return resultPaging;
	}
	
	
}
