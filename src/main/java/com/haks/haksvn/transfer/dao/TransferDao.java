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
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferSource;

@Repository
public class TransferDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public Paging<List<Transfer>> retrieveTransferList(Paging<Transfer> paging ){
		Session session = sessionFactory.getCurrentSession();
		Transfer search = paging.getModel();
		Criteria crit = session.createCriteria(Transfer.class)
				.createAlias("requestUser", "requser")
				.createAlias("transferStateCode", "stcode")
				.setFirstResult((int)paging.getStart())
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositorySeq", search.getRepositorySeq()))
				.addOrder(Order.desc("requestDate"))
				.addOrder(Order.desc("transferSeq"));
		
		if( search.getRequestUser() != null ){
			String requestUserId = search.getRequestUser().getUserId();
			if( requestUserId != null && requestUserId.length() > 0 ) crit.add(Restrictions.eq("requser.userId", requestUserId));
		}
		if( search.getTransferStateCode() != null ){
			String transferStateCode = search.getTransferStateCode().getCodeId();
			if( transferStateCode != null && transferStateCode.length() > 0 ) crit.add(Restrictions.eq("stcode.codeId", transferStateCode));
		}
		
		@SuppressWarnings("unchecked") List<Transfer> result = (List<Transfer>)crit.list();
		Paging<List<Transfer>> resultPaging = new Paging<List<Transfer>>();
		resultPaging.setModel(result);
		Paging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart());
		
		return resultPaging;
	}
	
	public Transfer addTransfer(Transfer transfer){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(transfer);
		return transfer;
	}
	
	public Transfer updateTransfer(Transfer transfer){
		Session session = sessionFactory.getCurrentSession();
		session.update(transfer);
		return transfer;
	}
	
	public Transfer retrieveTransferByTransferSeq( int transferSeq ){
		Session session = sessionFactory.getCurrentSession();
		return (Transfer)session.get(Transfer.class, transferSeq );
	}
	
	public Transfer deleteTransfer(Transfer transfer){
		Session session = sessionFactory.getCurrentSession();
		session.delete(transfer);
		return transfer;
	}
	
	public Transfer retrieveLockedTransferBySourcePath(String path){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(Transfer.class)
				.createAlias("sourceList", "src")
				.createAlias("transferStateCode", "stcode")
				.add(Restrictions.ne("stcode.codeId", "transfer.state.code.complete"))
				.add(Restrictions.ne("stcode.codeId", "transfer.state.code.reject"))
				.add(Restrictions.eq("src.path", path));
		
		return (Transfer)crit.uniqueResult();
	}
	
	public List<TransferSource> retrieveTransferSourceList(int transferSeq){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(Transfer.class)
				.add(Restrictions.eq("transferSeq", transferSeq))
				.createAlias("sourceList", "src")
				.addOrder(Order.asc("src.path"));
		Transfer transfer = (Transfer)crit.uniqueResult();		
		List<TransferSource> result = transfer.getSourceList();
		for( @SuppressWarnings("unused") TransferSource transferSource : result ){
			// do nothing; // fetching
		}
		return result;
	}
	
}
