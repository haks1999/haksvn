package com.haks.haksvn.source.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.source.model.ReviewComment;
import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.source.model.ReviewScore;

@Repository
public class ReviewDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<ReviewScore> retrieveReviewScoreListByReviewId(ReviewId reviewId){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<ReviewScore> reviewScoreList= session.createCriteria(ReviewScore.class)
				.add(Restrictions.eq("reviewId.repositoryKey", reviewId.getRepositoryKey()))
				.add(Restrictions.eq("reviewId.revision", reviewId.getRevision()))
				.list();
		return reviewScoreList;
	}
	
	public List<ReviewComment> retrieveReviewCommentListByReviewId(ReviewId reviewId){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<ReviewComment> reviewScoreList= session.createCriteria(ReviewComment.class)
				.add(Restrictions.eq("repositoryKey", reviewId.getRepositoryKey()))
				.add(Restrictions.eq("revision", reviewId.getRevision()))
				.list();
		return reviewScoreList;
	}
	
}
