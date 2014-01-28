package com.haks.haksvn.transfer.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferSource;

@Repository
public class TransferDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public Paging<List<Transfer>> retrieveTransferList(Paging<Transfer> paging ){
		Session session = sessionFactory.getCurrentSession();
		Transfer search = paging.getModel();
		Criteria crit = session.createCriteria(Transfer.class,"t_transfer")
				.setFirstResult((int)paging.getStart())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositoryKey", search.getRepositoryKey()))
				.addOrder(Order.desc("requestDate"))
				.addOrder(Order.desc("transferSeq"));
		
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
		// path 포기.. 시박
		/*
		if( search.getPath() != null && search.getPath().length() > 0 ){
			// subselect 개념인데 걍 join 하고 maxresult, distinct 하면 distinct 전에 maxresult 가 적용되고 distinct 한다
			// 10*10 에서 max를 20,으로 하면 실행하면 조인해서 20개 뽑고 그거를 distinct 하네.. 
			// 아래와 같이 fetchmode 설정을 해주믄 된다.
			crit.createAlias("t_transfer.sourceList", "t_source")
				.setFetchMode("t_source", FetchMode.SELECT)
				.add(Restrictions.like("t_source.path","%" + search.getPath() + "%"));
		}
		*/
		
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
		Transfer transfer = (Transfer)session.get(Transfer.class, transferSeq );
		// eager load nested elements
		if( transfer != null ){
			Hibernate.initialize(transfer.getTransferGroup());
			Hibernate.initialize(transfer.getSourceList());
		}
		return transfer;
	}
	
	public List<Transfer> retrieveTransferListByTransferGroupSeq( int transferGroupSeq ){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Transfer.class)
				.add(Restrictions.eq("transferGroup.transferGroupSeq", transferGroupSeq))
				.addOrder(Order.asc("transferSeq"));
		@SuppressWarnings("unchecked") List<Transfer> result = criteria.list();
		
		return result;
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
	
	public TransferSource retrieveLockedTransferSource(String path, String repositoryKey){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(TransferSource.class)
				.createAlias("transfer", "tr")
				.createAlias("tr.transferStateCode", "trc")
				//.add(Restrictions.ne("trc.codeId", CodeUtils.getTransferApprovedCodeId()))
				//.add(Restrictions.ne("trc.codeId", CodeUtils.getTransferRejectCodeId()))
				.add(Restrictions.ne("trc.codeId", CodeUtils.getTransferTransferedCodeId()))
				.add(Restrictions.eq("tr.repositoryKey", repositoryKey))
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
