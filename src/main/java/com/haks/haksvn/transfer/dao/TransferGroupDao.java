package com.haks.haksvn.transfer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.TransferGroup;

@Repository
public class TransferGroupDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public Paging<List<TransferGroup>> retrieveTransferGroupList(Paging<TransferGroup> paging ){
		Session session = sessionFactory.getCurrentSession();
		TransferGroup search = paging.getModel();
		Criteria crit = session.createCriteria(TransferGroup.class,"t_transferGroup")
				.createAlias("t_transferGroup.transferGroupStateCode", "t_state_code", JoinType.INNER_JOIN)
				.setFirstResult((int)paging.getStart())
				.setMaxResults((int)paging.getLimit())
				.add(Restrictions.eq("repositorySeq", search.getRepositorySeq()))
				.addOrder(Order.asc("t_state_code.codeOrder"))
				.addOrder(Order.desc("transferGroupSeq"));
		
		if( search.getTitle() != null ){
			crit.add(Restrictions.like("title", "%" + search.getTitle() + "%"));
		}
		
		if( search.getTransferGroupStateCode() != null ){
			String transferGroupStateCode = search.getTransferGroupStateCode().getCodeId();
			if( transferGroupStateCode != null && transferGroupStateCode.length() > 0 ){
				crit.add(Restrictions.eq("t_state_code.codeId", transferGroupStateCode));
			}
		}
		
		if( search.getTransferGroupTypeCode() != null ){
			String transferGroupTypeCode = search.getTransferGroupTypeCode().getCodeId();
			if( transferGroupTypeCode != null && transferGroupTypeCode.length() > 0 ){
				crit.createAlias("t_transferGroup.transferGroupTypeCode", "t_type_code")
					.add(Restrictions.eq("t_type_code.codeId", transferGroupTypeCode));
			}
		}
		
		@SuppressWarnings("unchecked") List<TransferGroup> result = (List<TransferGroup>)crit.list();
		Paging<List<TransferGroup>> resultPaging = new Paging<List<TransferGroup>>();
		resultPaging.setModel(result);
		Paging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart());
		
		return resultPaging;
	}
	
	public TransferGroup retrieveTransferGroupByTransferGroupSeq( int transferGroupSeq ){
		Session session = sessionFactory.getCurrentSession();
		return (TransferGroup)session.get(TransferGroup.class, transferGroupSeq );
	}
	
	public TransferGroup saveTransferGroup(TransferGroup transferGroup){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(transferGroup);
		return transferGroup;
	}
	
	public TransferGroup updateTransferGroup(TransferGroup transferGroup){
		Session session = sessionFactory.getCurrentSession();
		session.update(transferGroup);
		return transferGroup;
	}
	
	public void releaseTransferTransferGroup(TransferGroup transferGroup){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("UPDATE transfer SET transfer_group_seq=null, transfer_state=:transferState WHERE transfer_group_seq = :transferGroupSeq");
		query.setParameter("transferState", CodeUtils.getTransferApprovedCodeId());
		query.setParameter("transferGroupSeq", transferGroup.getTransferGroupSeq());
		query.executeUpdate();
	}
	
	public TransferGroup deleteTransferGroup(TransferGroup transferGroup){
		Session session = sessionFactory.getCurrentSession();
		session.delete(transferGroup);
		return transferGroup;
	}
	
	
}
