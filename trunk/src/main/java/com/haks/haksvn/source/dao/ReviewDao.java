package com.haks.haksvn.source.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
	
	public void saveReviewScore(ReviewScore reviewScore){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(reviewScore);
	}
	
}
