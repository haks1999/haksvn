package com.haks.haksvn.transfer.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
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
				.createAlias("sourceList", "trsrc")
				.setFirstResult((int)paging.getStart())
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositorySeq", search.getRepositorySeq()))
				.addOrder(Order.desc("requestDate"))
				.addOrder(Order.desc("transferSeq"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		
		if( search.getRequestUser() != null ){
			String requestUserId = search.getRequestUser().getUserId();
			if( requestUserId != null && requestUserId.length() > 0 ) crit.add(Restrictions.eq("requser.userId", requestUserId));
		}
		if( search.getTransferStateCode() != null ){
			String transferStateCode = search.getTransferStateCode().getCodeId();
			if( transferStateCode != null && transferStateCode.length() > 0 ) crit.add(Restrictions.eq("stcode.codeId", transferStateCode));
		}
		if( search.getPath() != null && search.getPath().length() > 0 ){
			crit.add(Restrictions.like("trsrc.path","%" + search.getPath() + "%"));
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
	
	public TransferSource deleteTransferSource(TransferSource transferSource){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("DELETE FROM transfer_source WHERE transfer_source_seq = :transferSourceSeq");
		query.setParameter("transferSourceSeq", transferSource.getTransferSourceSeq());
		query.executeUpdate();
		return transferSource;
	}
	
	public TransferSource retrieveLockedTransferSource(String path, int repositorySeq){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(TransferSource.class)
				.createAlias("transfer", "tr")
				.createAlias("tr.transferStateCode", "trc")
				.add(Restrictions.ne("trc.codeId", "transfer.state.code.complete"))
				.add(Restrictions.ne("trc.codeId", "transfer.state.code.reject"))
				.add(Restrictions.eq("tr.repositorySeq", repositorySeq))
				.add(Restrictions.eq("path", path));
		
		return (TransferSource)crit.uniqueResult();
	}
	
	public List<TransferSource> retrieveTransferSourceList(int transferSeq){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(Transfer.class)
				.add(Restrictions.eq("transferSeq", transferSeq))
				.createAlias("sourceList", "src")
				.addOrder(Order.asc("src.path"));
		Transfer transfer = (Transfer)crit.uniqueResult();		
		List<TransferSource> result = transfer.getSourceList();
		
		Collections.sort(result, new Comparator<TransferSource>(){
		     public int compare(TransferSource src1, TransferSource src2){
		    	 return src1.getPath().compareToIgnoreCase(src2.getPath());
		     }
		});
		return result;
	}
	
}
