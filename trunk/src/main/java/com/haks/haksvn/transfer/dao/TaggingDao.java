package com.haks.haksvn.transfer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.Tagging;

@Repository
public class TaggingDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public Paging<List<Tagging>> retrieveTaggingList(Paging<Tagging> paging ){
		Session session = sessionFactory.getCurrentSession();
		Tagging search = paging.getModel();
		Criteria crit = session.createCriteria(Tagging.class)
				.createAlias("taggingUser", "tagger")
				.createAlias("taggingTypeCode", "ttcode")
				.setFirstResult((int)paging.getStart())
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositoryKey", search.getRepositoryKey()))
				.addOrder(Order.desc("taggingDate"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if( search.getTaggingUser() != null ){
			String taggingUserId = search.getTaggingUser().getUserId();
			if( taggingUserId != null && taggingUserId.length() > 0 ) crit.add(Restrictions.eq("tagger.userId", taggingUserId));
		}
		if( search.getTaggingTypeCode() != null ){
			String taggingTypeCode = search.getTaggingTypeCode().getCodeId();
			if( taggingTypeCode != null && taggingTypeCode.length() > 0 ) crit.add(Restrictions.eq("ttcode.codeId", taggingTypeCode));
		}
		
		@SuppressWarnings("unchecked") List<Tagging> result = (List<Tagging>)crit.list();
		Paging<List<Tagging>> resultPaging = new Paging<List<Tagging>>();
		resultPaging.setModel(result);
		Paging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart());
		
		return resultPaging;
	}
	
	public List<Tagging> retrieveTaggingListByTagName(Tagging tagging){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Tagging> taggingList= session.createCriteria(Tagging.class)
				.add(Restrictions.eq("repositoryKey", tagging.getRepositoryKey()))
				.add(Restrictions.eq("tagName", tagging.getTagName()))
				.addOrder(Order.desc("taggingDate")).list();
		return taggingList;
	}
	
	public List<Tagging> retrieveTaggingListByTaggingDate(String repositoryKey, long startDate, long endDate){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Tagging> taggingList= session.createCriteria(Tagging.class)
				.add(Restrictions.eq("repositoryKey", repositoryKey))
				.add(Restrictions.between("taggingDate", startDate, endDate))
				.addOrder(Order.desc("taggingDate")).list();
		return taggingList;
	}
	
	public Tagging retrieveLatestSyncTagging(Tagging tagging){
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Tagging> taggingList= session.createCriteria(Tagging.class)
				.add(Restrictions.eq("repositoryKey", tagging.getRepositoryKey()))
				.addOrder(Order.desc("taggingDate")).list();
		if( taggingList.size() < 1 ) return null;
		return taggingList.get(0);
	}
	
	
	public Tagging retrieveTaggingByTaggingSeq( int taggingSeq ){
		Session session = sessionFactory.getCurrentSession();
		return (Tagging)session.get(Tagging.class, taggingSeq );
	}
	
	public Tagging saveTagging(Tagging tagging){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(tagging);
		return tagging;
	}
	
	
}
